package net.fawnoculus.vanillaBackrooms.util;

import net.fawnoculus.craft_attack.CraftAttack;
import net.fawnoculus.craft_attack.CraftAttackConfig;
import net.fawnoculus.craft_attack.blocks.ModBlocks;
import net.fawnoculus.craft_attack.blocks.entities.BackroomsGeneratorBE;
import net.fawnoculus.craft_attack.misc.CustomDataHolder;
import net.fawnoculus.craft_attack.misc.tags.ModItemTags;
import net.fawnoculus.craft_attack.util.PlayerUtil;
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
import net.minecraft.text.TextCodecs;
import net.minecraft.util.ErrorReporter;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class BackroomsUtil {
  public static final RegistryKey<World> BACKROOMS_WORLD = RegistryKey.of(RegistryKeys.WORLD, CraftAttack.id("backrooms"));
  public static final String PLAYER_DATA_EXTENSION = "backrooms";

  public static boolean sendToBackrooms(MinecraftServer server, Entity entity) {
    if(!CraftAttackConfig.BACKROOMS_ENABLED.getValue()){
      return false;
    }

    if(isInDimension(entity)) return false;

    ServerWorld backroomsWorld = server.getWorld(BACKROOMS_WORLD);
    if (backroomsWorld == null) {
      CraftAttack.LOGGER.error("(sendToBackrooms) Failed: Backrooms World == null");
      return false;
    }

    if(entity instanceof ServerPlayerEntity player) {
      try {
        savePlayerData(player);
      } catch (Exception e) {
        CraftAttack.LOGGER.error("(sendToBackrooms) Failed to save player data: {}", e.toString());
        return false;
      }
    }

    if (backroomsWorld.getBlockState(BlockPos.ORIGIN).getBlock() != ModBlocks.BACKROOMS_GENERATOR) {
      BackroomsGeneratorBE.placeBackroomsSegment(backroomsWorld, BlockPos.ORIGIN);
      backroomsWorld.setBlockState(BlockPos.ORIGIN, ModBlocks.BACKROOMS_GENERATOR.getDefaultState());
    }

    Vec3d spawnPos = new Vec3d(24, 2, 24);

    entity.fallDistance = 0;
    entity.teleport(backroomsWorld, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), Set.of(), 0, 0, false);

    if(entity instanceof ServerPlayerEntity player) {
      player.sendMessage(Text.literal("§n§o§m§i§n§i§m§a§p"));
      player.sendMessage(Text.literal("§f§a§i§r§x§a§e§r§o"));
      player.changeGameMode(GameMode.ADVENTURE);
      player.clearStatusEffects();
      player.getInventory().clear();
      player.setSpawnPoint(new ServerPlayerEntity.Respawn(backroomsWorld.getRegistryKey(), BlockPos.ofFloored(spawnPos), backroomsWorld.getSpawnAngle(), true), false);
      player.setHealth(player.getMaxHealth());
      player.getHungerManager().add(20, 20);
      player.setExperienceLevel(0);
      player.setExperiencePoints(0);

      player.getEnderPearls().forEach(Entity::discard);

      NbtCompound customData = PlayerUtil.getPermanentCustomData(player);
      customData.put("messageIgnoreReason", TextCodecs.CODEC, Text.literal("NUH UH!").formatted(Formatting.RED));
      customData.putBoolean("disableMinimap", true);
      customData.putBoolean("fairXaero", true);
      PlayerUtil.setPermanentCustomData(player, customData);
    }

    return true;
  }

  public static boolean removeFromBackrooms(MinecraftServer server, Entity entity) {
    if (!isInDimension(entity)) return false;
    if (entity instanceof ServerPlayerEntity player) {
      ArrayList<ItemStack> stacks = new ArrayList<>(player.getInventory().size());

      for (ItemStack stack : player.getInventory()) {
        if(stack.getRegistryEntry().isIn(ModItemTags.BACKROOMS_NON_RETURN)){
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
      customData.remove("messageIgnoreReason");
      customData.remove("disableMinimap");
      customData.remove("fairXaero");
      PlayerUtil.setPermanentCustomData(player, customData);

      return true;
    }

    if(entity instanceof InventoryOwner owner) {
      SimpleInventory inventory = owner.getInventory();

      for (int i = 0; i < inventory.size(); i++) {
        if(inventory.getStack(i).getRegistryEntry().isIn(ModItemTags.BACKROOMS_NON_RETURN)){
          inventory.setStack(i, ItemStack.EMPTY);
        }
      }
    }

    if(entity instanceof ItemEntity item && item.getStack().getRegistryEntry().isIn(ModItemTags.BACKROOMS_NON_RETURN)){
      item.kill((ServerWorld) item.getWorld());
      return true;
    }

    ServerWorld overWorld = server.getOverworld();
    Vec3d spawnPos = overWorld.getSpawnPos().toCenterPos();
    entity.fallDistance = 0;
    entity.teleport(overWorld, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), Set.of(), overWorld.getSpawnAngle(), 0, false);
    return true;
  }

  public static boolean switchDimension(MinecraftServer server, Entity entity) {
    if (isInDimension(entity)) {
      return removeFromBackrooms(server, entity);
    }

    return sendToBackrooms(server, entity);
  }

  public static void onEntityDamaged(LivingEntity entity, ServerWorld world, DamageSource source, float ignored) {
    if(!source.isOf(DamageTypes.IN_WALL) || !CraftAttackConfig.BACKROOMS_ENABLED.getValue()){
      NbtCompound data = CustomDataHolder.from(entity).CA$getCustomData();
      data.putInt("suffocationDamageTicks", 0);
      CustomDataHolder.from(entity).CA$setCustomData(data);
      return;
    }

    if(entity instanceof ServerPlayerEntity) {
      NbtCompound data = CustomDataHolder.from(entity).CA$getCustomData();

      int ticks = data.getInt("suffocationDamageTicks", 0);
      ticks++;

      if(ticks >= world.getRandom().nextBetween(40, 80)){
        data.putInt("suffocationDamageTicks", 0);
        CustomDataHolder.from(entity).CA$setCustomData(data);

        switchDimension(world.getServer(), entity);
        return;
      }

      data.putInt("suffocationDamageTicks", ticks);
      CustomDataHolder.from(entity).CA$setCustomData(data);
    }
  }

  public static boolean isInDimension(Entity entity) {
    return entity.getWorld().getRegistryKey().getValue().equals(BACKROOMS_WORLD.getValue());
  }

  public static void savePlayerData(ServerPlayerEntity player) throws IOException {
    MinecraftServer server = Objects.requireNonNull(player.getServer());

    Path playerData = server.getPath("data")
      .resolve("ca")
      .resolve("player_data")
      .resolve(PLAYER_DATA_EXTENSION)
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
      .resolve("ca")
      .resolve("player_data")
      .resolve(PLAYER_DATA_EXTENSION)
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
