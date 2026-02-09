package net.fawnoculus.vanillaBackrooms.util;

import net.fawnoculus.vanillaBackrooms.VanillaBackrooms;
import net.fawnoculus.vanillaBackrooms.VanillaBackroomsConfig;
import net.fawnoculus.vanillaBackrooms.blocks.ModBlocks;
import net.fawnoculus.vanillaBackrooms.blocks.entities.BackroomsGeneratorBE;
import net.fawnoculus.vanillaBackrooms.misc.CustomDataHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.NbtReadView;
import net.minecraft.storage.NbtWriteView;
import net.minecraft.storage.ReadView;
import net.minecraft.text.Text;
import net.minecraft.util.ErrorReporter;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameMode;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class BackroomsUtil {
	public static final RegistryKey<World> LEVEL_0 = RegistryKey.of(RegistryKeys.WORLD, VanillaBackrooms.id("level_0"));
	public static final RegistryKey<World> LEVEL_1 = RegistryKey.of(RegistryKeys.WORLD, VanillaBackrooms.id("level_1"));
	public static final RegistryKey<World> LEVEL_2 = RegistryKey.of(RegistryKeys.WORLD, VanillaBackrooms.id("level_2"));
	public static final RegistryKey<World> LEVEL_3 = RegistryKey.of(RegistryKeys.WORLD, VanillaBackrooms.id("level_3"));
	public static final RegistryKey<World> LEVEL_4 = RegistryKey.of(RegistryKeys.WORLD, VanillaBackrooms.id("level_4"));
	public static final RegistryKey<World> LEVEL_5 = RegistryKey.of(RegistryKeys.WORLD, VanillaBackrooms.id("level_5"));

	public static void onEntityDamaged(LivingEntity entity, ServerWorld world, DamageSource source, float ignored) {
		if (!VanillaBackroomsConfig.SUFFOCATION_NOCLIP.getValue() || !source.isOf(DamageTypes.IN_WALL)) {
			NbtCompound data = CustomDataHolder.from(entity).VanillaBackrooms$getCustomData();
			data.putInt("suffocationDamageTicks", 0);
			CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(data);
			return;
		}

		NbtCompound data = CustomDataHolder.from(entity).VanillaBackrooms$getCustomData();

		int ticks = data.getInt("suffocationDamageTicks", 0);
		ticks++;

		if (ticks >= world.getRandom().nextBetween(40, 80)) {
			data.putInt("suffocationDamageTicks", 0);
			CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(data);

			noclip(world.getServer(), entity);
			return;
		}

		data.putInt("suffocationDamageTicks", ticks);
		CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(data);
	}

	public static void onEntityDimensionChanged(Entity entity, ServerWorld world) {
		NbtCompound customData = CustomDataHolder.from(entity).VanillaBackrooms$getCustomData();
		boolean wasInBackrooms = customData.getBoolean("isInBackrooms", false);

		Identifier wordID = world.getRegistryKey().getValue();
		if (wordID.equals(LEVEL_0.getValue())
		  || wordID.equals(LEVEL_1.getValue())
		  || wordID.equals(LEVEL_2.getValue())
		  || wordID.equals(LEVEL_3.getValue())
		  || wordID.equals(LEVEL_4.getValue())
		  || wordID.equals(LEVEL_5.getValue())
		) {
			if (wasInBackrooms) {
				return;
			}

			customData.putBoolean("isInBackrooms", true);
			onEnterBackrooms(entity);
			return;
		}

		if (!wasInBackrooms) return;
		onExitBackrooms(entity);
	}

	public static boolean noclip(MinecraftServer server, Entity entity) {
		entity.detach();
		RegistryKey<World> nextDimension = getNextDimension(entity.getWorld().getRegistryKey(), entity.getRandom());
		return sendToDimension(server, entity, nextDimension);
	}

	public static boolean sendToDimension(MinecraftServer server, Entity entity, RegistryKey<World> targetDimension) {
		RegistryKey<World> previousDimension = entity.getWorld().getRegistryKey();

		if (previousDimension.getValue().equals(targetDimension.getValue())) {
			return false;
		}

		if (targetDimension.getValue().equals(World.OVERWORLD.getValue())) {
			exitBackrooms(server, entity);
			return true;
		}

		ServerWorld backroomsWorld = server.getWorld(targetDimension);

		if (backroomsWorld == null) {
			VanillaBackrooms.LOGGER.error("Failed to get Dimension : '{}'", targetDimension.getValue());
			return false;
		}

		if (backroomsWorld.getBlockState(BlockPos.ORIGIN).getBlock() != ModBlocks.BACKROOMS_GENERATOR) {
			try {
				BackroomsGeneratorBE.placeBackroomsSegment(backroomsWorld, BlockPos.ORIGIN);
			}catch (NullPointerException e) {
				VanillaBackrooms.LOGGER.error("Failed to generate center segment for dimension: {}, Exception: '{}'", targetDimension.getValue(), e.toString());
				return false;
			}
			backroomsWorld.setBlockState(BlockPos.ORIGIN, ModBlocks.BACKROOMS_GENERATOR.getDefaultState());
		}

		Vec3d spawnPos = new Vec3d(24, 2, 24);

		if (targetDimension.getValue().equals(LEVEL_0.getValue()) && entity instanceof ServerPlayerEntity player) {
			try {
				savePlayerData(player);
			} catch (Exception e) {
				VanillaBackrooms.LOGGER.error("Failed to save Player Data for Player '{}'", player.getGameProfile().getName());
				return false;
			}
		}

		entity.fallDistance = 0;
		entity.teleport(backroomsWorld, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), Set.of(), 0, 0, false);

		if (targetDimension.getValue().equals(LEVEL_0.getValue()) && entity instanceof ServerPlayerEntity player) {
			if (VanillaBackroomsConfig.DISABLE_XAERO_MINIMAP.getValue()) {
				player.sendMessage(Text.literal("§n§o§m§i§n§i§m§a§p"));
			}
			if (VanillaBackroomsConfig.XAERO_FAIR.getValue()) {
				player.sendMessage(Text.literal("§f§a§i§r§x§a§e§r§o"));
			}
			player.clearStatusEffects();
			player.getInventory().clear();
			player.setSpawnPoint(new ServerPlayerEntity.Respawn(backroomsWorld.getRegistryKey(), BlockPos.ofFloored(spawnPos), backroomsWorld.getSpawnAngle(), true), false);
			player.setHealth(player.getMaxHealth());
			player.getHungerManager().add(20, 20);
			player.setExperienceLevel(0);
			player.setExperiencePoints(0);
		}

		return true;
	}

	private static void exitBackrooms(MinecraftServer server, Entity entity) {
		if (!onExitBackrooms(entity)) {
			return;
		}

		if (entity instanceof ServerPlayerEntity player) {
			ArrayList<ItemStack> stacks = new ArrayList<>(player.getInventory().size());

			for (ItemStack stack : player.getInventory()) {
				if (stack.getRegistryEntry().isIn(VanillaBackroomsConfig.BACKROOMS_NOT_RETURN.getValue())) {
					continue;
				}

				stacks.add(stack.copy());
			}

			loadPlayerData(player);

			for (ItemStack stack : stacks) {
				player.giveOrDropStack(stack);
			}

			player.sendMessage(Text.literal("§r§e§s§e§t§x§a§e§r§o"));

			NbtCompound customData = PlayerUtil.getPermanentCustomData(player);
			customData.remove("isInBackrooms");
			PlayerUtil.setPermanentCustomData(player, customData);
			return;
		}

		ServerWorld overWorld = server.getOverworld();
		Vec3d spawnPos = overWorld.getSpawnPos().toCenterPos();
		entity.fallDistance = 0;
		entity.teleport(overWorld, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), Set.of(), overWorld.getSpawnAngle(), 0, false);
	}

	private static void onEnterBackrooms(Entity entity) {
		NbtCompound customData = CustomDataHolder.from(entity).VanillaBackrooms$getCustomData();
		customData.putBoolean("isInBackrooms", true);
		CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(customData);

		if (entity instanceof ServerPlayerEntity player) {
			if (VanillaBackroomsConfig.DISABLE_XAERO_MINIMAP.getValue()) {
				player.sendMessage(Text.literal("§n§o§m§i§n§i§m§a§p"));
			}
			if (VanillaBackroomsConfig.XAERO_FAIR.getValue()) {
				player.sendMessage(Text.literal("§f§a§i§r§x§a§e§r§o"));
			}
			player.changeGameMode(GameMode.ADVENTURE);

			player.getEnderPearls().forEach(Entity::discard);

			NbtCompound permanentCustomData = PlayerUtil.getPermanentCustomData(player);
			permanentCustomData.putBoolean("isInBackrooms", true);
			PlayerUtil.setPermanentCustomData(player, permanentCustomData);
		}
	}

	private static boolean onExitBackrooms(Entity entity) {
		NbtCompound customData = CustomDataHolder.from(entity).VanillaBackrooms$getCustomData();
		customData.remove("isInBackrooms");
		CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(customData);

		if (entity instanceof ItemEntity item && item.getStack().getRegistryEntry().isIn(VanillaBackroomsConfig.BACKROOMS_NOT_RETURN.getValue())) {
			item.kill((ServerWorld) item.getWorld());
			item.discard();
			return false;
		}

		if (entity instanceof InventoryOwner owner) {
			SimpleInventory inventory = owner.getInventory();

			for (int i = 0; i < inventory.size(); i++) {
				if (inventory.getStack(i).getRegistryEntry().isIn(VanillaBackroomsConfig.BACKROOMS_NOT_RETURN.getValue())) {
					inventory.setStack(i, ItemStack.EMPTY);
				}
			}
		}

		return true;
	}

	public static RegistryKey<World> getNextDimension(RegistryKey<World> previousDimension, Random random) {
		Identifier identifier = previousDimension.getValue();

		if (LEVEL_0.getValue().equals(identifier)) {
			if (random.nextDouble() >= VanillaBackroomsConfig.LEVEL_0_EXIT_CHANCE.getValue()) {
				return World.OVERWORLD;
			}
			return LEVEL_1;
		}

		if (LEVEL_1.getValue().equals(identifier)) {
			if (random.nextDouble() >= VanillaBackroomsConfig.LEVEL_1_EXIT_CHANCE.getValue()) {
				return World.OVERWORLD;
			}
			return LEVEL_2;
		}

		if (LEVEL_2.getValue().equals(identifier)) {
			if (random.nextDouble() >= VanillaBackroomsConfig.LEVEL_2_EXIT_CHANCE.getValue()) {
				return World.OVERWORLD;
			}
			return LEVEL_3;
		}

		if (LEVEL_3.getValue().equals(identifier)) {
			if (random.nextDouble() >= VanillaBackroomsConfig.LEVEL_3_EXIT_CHANCE.getValue()) {
				return World.OVERWORLD;
			}
			return LEVEL_4;
		}

		if (LEVEL_4.getValue().equals(identifier)) {
			if (random.nextDouble() >= VanillaBackroomsConfig.LEVEL_4_EXIT_CHANCE.getValue()) {
				return World.OVERWORLD;
			}
			return LEVEL_5;
		}

		if (LEVEL_5.getValue().equals(identifier)) {
			if (random.nextDouble() >= VanillaBackroomsConfig.LEVEL_5_EXIT_CHANCE.getValue()) {
				return World.OVERWORLD;
			}
		}

		return LEVEL_0;
	}

	public static void savePlayerData(ServerPlayerEntity player) throws IOException {
		MinecraftServer server = Objects.requireNonNull(player.getServer());

		Path playerData = server.getPath("data")
		  .resolve("vanilla_backrooms")
		  .resolve("backrooms_player_data")
		  .resolve(player.getUuidAsString() + ".dat");

		var ignored = playerData.getParent().toFile().mkdirs();

		if (playerData.toFile().exists()) {
			var ignored2 = playerData.toFile().delete();
		}
		var ignored3 = playerData.toFile().createNewFile();

		NbtCompound nbt = new NbtCompound();

		NbtWriteView view = NbtWriteView.create(ErrorReporter.EMPTY, player.getRegistryManager());
		player.writeData(view);
		nbt.put("data", view.getNbt());

		NbtIo.write(nbt, playerData);
	}


	public static void loadPlayerData(ServerPlayerEntity player) {
		MinecraftServer server = Objects.requireNonNull(player.getServer());

		Path playerData = server.getPath("data")
		  .resolve("vanilla_backrooms")
		  .resolve("backrooms_player_data")
		  .resolve(player.getUuidAsString() + ".dat");

		NbtCompound nbt = new NbtCompound();
		try {
			nbt = Objects.requireNonNull(NbtIo.read(playerData));
		} catch (Exception ignored) {
		}
		ReadView view = NbtReadView.create(ErrorReporter.EMPTY, player.getRegistryManager(), nbt.getCompoundOrEmpty("data"));


		player.readData(view);

		player.teleportTo(player.getRespawnTarget(false, TeleportTarget.NO_OP));
		player.readRootVehicle(view);
		player.readGameModeData(view);
		player.getServer().getPlayerManager().sendStatusEffects(player);
		player.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.GAME_MODE_CHANGED, player.getGameMode().getIndex()));
	}
}
