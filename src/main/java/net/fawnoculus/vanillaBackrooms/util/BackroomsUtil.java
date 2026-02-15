package net.fawnoculus.vanillaBackrooms.util;

import net.fawnoculus.vanillaBackrooms.VanillaBackrooms;
import net.fawnoculus.vanillaBackrooms.VanillaBackroomsConfig;
import net.fawnoculus.vanillaBackrooms.blocks.ModBlocks;
import net.fawnoculus.vanillaBackrooms.blocks.entities.BackroomsGeneratorBE;
import net.fawnoculus.vanillaBackrooms.levels.BackroomsLevel;
import net.fawnoculus.vanillaBackrooms.misc.CustomDataHolder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.vehicle.VehicleInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
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
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameMode;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class BackroomsUtil {
	@Contract("_ -> new")
	public static @NotNull Identifier getLevelId(int levelNumber) {
		return VanillaBackrooms.id("level_" + levelNumber);
	}

	public static RegistryKey<World> getLevelKey(int levelNumber) {
		return RegistryKey.of(RegistryKeys.WORLD, getLevelId(levelNumber));
	}

	public static void onEntityDie(LivingEntity entity, ServerWorld world, DamageSource ignored, float damage) {
		if (VanillaBackroomsConfig.DEATH_NOCLIP.getValue().isFalse(world.getServer()) || damage < entity.getHealth()) {
			return;
		}

		if (!VanillaBackroomsConfig.DEATH_NOCLIP_IN_BACKROOMS.getValue() && BackroomsLevel.isLevel(world.getRegistryKey().getValue())) {
			return;
		}

		for (Hand hand : Hand.values()) {
			if (entity.getStackInHand(hand).get(DataComponentTypes.DEATH_PROTECTION) != null) {
				return;
			}
		}

		noclip(world.getServer(), entity);
		entity.setHealth(entity.getHealth() + damage); // Make the entity have with enough health to survive
	}

	public static void onEntitySuffocate(LivingEntity entity, ServerWorld world, DamageSource source, float ignored) {
		NbtCompound data = CustomDataHolder.from(entity).VanillaBackrooms$getCustomData();

		if (VanillaBackroomsConfig.SUFFOCATION_NOCLIP.getValue().isFalse(world.getServer()) || !source.isOf(DamageTypes.IN_WALL)) {
			data.remove("suffocationDamageTicks");
			CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(data);
			return;
		}

		if (!VanillaBackroomsConfig.SUFFOCATION_NOCLIP_IN_BACKROOMS.getValue() && BackroomsLevel.isLevel(world.getRegistryKey().getValue())) {
			data.remove("suffocationDamageTicks");
			CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(data);
			return;
		}


		int ticks = data.getInt("suffocationDamageTicks", 0);
		ticks++;

		if ((entity.getHealth() <= 4 && world.getRandom().nextBoolean())
		  || ticks >= world.getRandom().nextBetween(60, 100)
		) {
			data.remove("suffocationDamageTicks");
			CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(data);

			noclip(world.getServer(), entity);
			return;
		}

		data.putInt("suffocationDamageTicks", ticks);
		CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(data);
	}

	public static void onEntityDimensionChanged(Entity entity, ServerWorld world) {
		if (entity instanceof ServerPlayerEntity player) { // Players need special treatment, because they might have died, which would have reset their custom data
			NbtCompound permanentCustomData = PlayerUtil.getPermanentCustomData(player);
			boolean wasInBackrooms = permanentCustomData.getBoolean("isInBackrooms", false);
			if (BackroomsLevel.isLevel(world.getRegistryKey().getValue())) {
				if (wasInBackrooms) {
					return;
				}

				permanentCustomData.putBoolean("isInBackrooms", true);
				PlayerUtil.setPermanentCustomData(player, permanentCustomData);

				onEnterBackrooms(entity);
				return;
			}

			if (!wasInBackrooms) return;
			onExitBackrooms(entity);
		}

		NbtCompound customData = CustomDataHolder.from(entity).VanillaBackrooms$getCustomData();
		boolean wasInBackrooms = customData.getBoolean("isInBackrooms", false);

		if (BackroomsLevel.isLevel(world.getRegistryKey().getValue())) {
			if (wasInBackrooms) {
				return;
			}

			customData.putBoolean("isInBackrooms", true);
			CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(customData);

			if (entity instanceof ServerPlayerEntity player) {
				NbtCompound permanentCustomData = PlayerUtil.getPermanentCustomData(player);
				permanentCustomData.putBoolean("isInBackrooms", true);
				PlayerUtil.setPermanentCustomData(player, permanentCustomData);
			}

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

		BackroomsLevel level = BackroomsLevel.getLevel(targetDimension.getValue());
		if (level == null) {
			exitBackrooms(server, entity, targetDimension);
			return true;
		}

		ServerWorld world = server.getWorld(targetDimension);
		if (world == null) {
			VanillaBackrooms.LOGGER.error("Failed to find backrooms level ({}), will use level 0 instead", targetDimension.getValue());
			world = server.getWorld(getLevelKey(0));
			if (world == null) {
				VanillaBackrooms.LOGGER.error("Level 0 does not exits, something has gone terribly wrong!");
				return false;
			}
		}


		if (BackroomsGeneratorBE.shouldPlaceSegment(world, BlockPos.ORIGIN)) {
			try {
				level.generator().placeBackroomsSegment(world, BlockPos.ORIGIN);
			} catch (Throwable throwable) {
				VanillaBackrooms.LOGGER.error("Failed to generate center segment for backrooms level ({})", targetDimension.getValue(), throwable);
				return false;
			}
			world.setBlockState(BlockPos.ORIGIN, ModBlocks.BACKROOMS_GENERATOR.getDefaultState());
		}

		Vec3d spawnPos = level.spawnBlock();

		if (VanillaBackroomsConfig.CLEAR_INV.getValue()
		  && targetDimension.getValue().equals(getLevelId(0))
		  && entity instanceof ServerPlayerEntity player
		) {
			try {
				savePlayerData(player);
			} catch (Exception e) {
				VanillaBackrooms.LOGGER.error("Failed to save Player Data for Player '{}', they will not be noclipped", player.getGameProfile().getName());
				return false;
			}
		}

		entity.fallDistance = 0;
		entity.teleport(world, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), Set.of(), entity.getYaw(), entity.getPitch(), false);

		if (entity instanceof ServerPlayerEntity player) {
			if (VanillaBackroomsConfig.ANNOUNCE_LEVEL.getValue()) {
				player.networkHandler.sendPacket(new TitleS2CPacket(Text.literal(level.levelName())));
				player.networkHandler.sendPacket(new SubtitleS2CPacket(Text.literal(level.name())));
			}
			player.setSpawnPoint(new ServerPlayerEntity.Respawn(world.getRegistryKey(), BlockPos.ofFloored(spawnPos), world.getSpawnAngle(), true), false);
		}

		return true;
	}

	private static void exitBackrooms(MinecraftServer server, Entity entity, RegistryKey<World> targetWorld) {
		if (!onExitBackrooms(entity)) {
			return;
		}

		if (entity instanceof ServerPlayerEntity player) {
			if (VanillaBackroomsConfig.CLEAR_INV.getValue()) {
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
			}

			player.teleportTo(player.getRespawnTarget(false, TeleportTarget.NO_OP));

			return;
		}

		ServerWorld world = server.getWorld(targetWorld);
		if (world == null) {
			VanillaBackrooms.LOGGER.error("Failed to get Dimension ({}) for Backrooms exit, will use overworld instead", targetWorld.getValue());
			world = server.getOverworld();
		}

		Vec3d spawnPos = world.getSpawnPos().toCenterPos();
		entity.fallDistance = 0;
		entity.teleport(world, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), Set.of(), world.getSpawnAngle(), 0, false);
	}

	private static void onEnterBackrooms(Entity entity) {
		if (entity instanceof LivingEntity livingEntity) {
			livingEntity.setHealth(livingEntity.getMaxHealth());
		}

		if (entity instanceof ServerPlayerEntity player) {
			if (VanillaBackroomsConfig.DISABLE_XAERO_MINIMAP.getValue()) {
				player.sendMessage(Text.literal("§n§o§m§i§n§i§m§a§p"));
			}

			if (VanillaBackroomsConfig.XAERO_FAIR.getValue()) {
				player.sendMessage(Text.literal("§f§a§i§r§x§a§e§r§o"));
			}

			player.changeGameMode(GameMode.ADVENTURE);

			player.getEnderPearls().forEach(Entity::discard);

			player.clearStatusEffects();

			if (VanillaBackroomsConfig.CLEAR_INV.getValue()) {
				player.getInventory().clear();
				player.getHungerManager().setFoodLevel(20);
				player.getHungerManager().setSaturationLevel(20F);
				player.setExperienceLevel(0);
				player.setExperiencePoints(0);
			}

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

		if (entity instanceof VehicleInventory inventory) {
			for (int i = 0; i < inventory.size(); i++) {
				if (inventory.getStack(i).getRegistryEntry().isIn(VanillaBackroomsConfig.BACKROOMS_NOT_RETURN.getValue())) {
					inventory.setStack(i, ItemStack.EMPTY);
				}
			}
		}

		if (entity instanceof ServerPlayerEntity player) {
			player.sendMessage(Text.literal("§r§e§s§e§t§x§a§e§r§o"));

			NbtCompound permanentCustomData = PlayerUtil.getPermanentCustomData(player);
			permanentCustomData.remove("isInBackrooms");
			PlayerUtil.setPermanentCustomData(player, permanentCustomData);
		}

		return true;
	}

	public static RegistryKey<World> getNextDimension(RegistryKey<World> previousDimension, Random random) {
		try {
			return Objects.requireNonNull(RegistryKey.of(RegistryKeys.WORLD,
			  VanillaBackroomsConfig.NOCLIP_CHANCES.getValue().get(previousDimension.getValue()).get(random)
			));
		} catch (Throwable ignored) {
			return getLevelKey(0);
		}
	}

	public static void savePlayerData(ServerPlayerEntity player) throws IOException {
		NbtCompound permanentCustomData = PlayerUtil.getPermanentCustomData(player);
		boolean hasSavedData = permanentCustomData.getBoolean("hasSavedData", false);
		if (hasSavedData) {
			return;
		}

		permanentCustomData.putBoolean("hasSavedData", true);
		PlayerUtil.setPermanentCustomData(player, permanentCustomData);

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
		NbtCompound permanentCustomData = PlayerUtil.getPermanentCustomData(player);
		boolean hasSavedData = permanentCustomData.getBoolean("hasSavedData", false);
		if (!hasSavedData) {
			return;
		}

		permanentCustomData.putBoolean("hasSavedData", false);
		PlayerUtil.setPermanentCustomData(player, permanentCustomData);

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
		player.changeGameMode(player.getGameMode());
	}
}
