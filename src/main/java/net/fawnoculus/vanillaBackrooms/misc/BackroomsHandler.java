package net.fawnoculus.vanillaBackrooms.misc;

import net.fawnoculus.vanillaBackrooms.VanillaBackrooms;
import net.fawnoculus.vanillaBackrooms.VanillaBackroomsConfig;
import net.fawnoculus.vanillaBackrooms.blocks.entities.BackroomsGeneratorBE;
import net.fawnoculus.vanillaBackrooms.levels.BackroomsLevel;
import net.fawnoculus.vanillaBackrooms.util.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.Inventory;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

// This entire class is a huge mess,
// but it handles so many things that re-writing would take to long
public final class BackroomsHandler {
    private BackroomsHandler() {
    }

    @Contract("_ -> new")
    public static @NotNull Identifier getLevelId(int levelNumber) {
        return VanillaBackrooms.id("level_" + levelNumber);
    }

    public static RegistryKey<World> getLevelKey(int levelNumber) {
        return RegistryKey.of(RegistryKeys.WORLD, getLevelId(levelNumber));
    }

    public static boolean isInBackrooms(Entity entity) {
        return BackroomsLevel.isLevel(entity.getWorld().getRegistryKey().getValue());
    }

    public static boolean noclip(MinecraftServer server, Entity entity) {
        entity.detach();
        RegistryKey<World> nextDimension = getNextDimension(entity.getWorld().getRegistryKey(), new Random());
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

        Optional<Throwable> error = BackroomsGeneratorBE.tryPlaceSegment(world, BlockPos.ORIGIN, level.generator());
        if (error.isPresent()) {
            VanillaBackrooms.LOGGER.error("Failed to generate center segment for backrooms level {}", targetDimension.getValue(), error.get());
            return false;
        }

        Vec3d spawnPos = level.spawnPos();

        if (entity instanceof ServerPlayerEntity player) {
            if (VanillaBackroomsConfig.CLEAR_INV.getValue()
              && !isInBackrooms(entity) // make sure the player wasn't already in the backrooms before
              && (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE)
            ) {
                try {
                    savePlayerData(player);
                } catch (Exception e) {
                    VanillaBackrooms.LOGGER.error("Failed to save Player Data for Player '{}', they will not be noclipped", player.getGameProfile().getName());
                    return false;
                }
            }

            if (VanillaBackroomsConfig.ANNOUNCE_LEVEL.getValue()) {
                player.networkHandler.sendPacket(new TitleS2CPacket(Text.literal(level.levelName())));
                player.networkHandler.sendPacket(new SubtitleS2CPacket(Text.literal(level.name())));
            }

            ServerPlayerEntity.Respawn respawn = player.getRespawn();
            if (respawn != null) {
                NbtCompound permanentCustomData = PlayerUtil.getPermanentCustomData(player);
                permanentCustomData.put("outOfBackroomsRespawn", ServerPlayerEntity.Respawn.CODEC, respawn);
                PlayerUtil.setPermanentCustomData(player, permanentCustomData);
            }

            player.setSpawnPoint(new ServerPlayerEntity.Respawn(world.getRegistryKey(), BlockPos.ofFloored(spawnPos), world.getSpawnAngle(), true), false);
        }

        entity.fallDistance = 0;
        entity.teleport(world, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), Set.of(), entity.getYaw(), entity.getPitch(), false);

        return true;
    }

    public static void exitBackrooms(MinecraftServer server, Entity entity, RegistryKey<World> targetWorld) {
        if (entity instanceof ServerPlayerEntity player) {
            NbtCompound permanentCustomData = PlayerUtil.getPermanentCustomData(player);
            if (permanentCustomData.getBoolean("hasSavedData", false)) {
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

            ServerPlayerEntity.Respawn respawn = permanentCustomData.get("outOfBackroomsRespawn", ServerPlayerEntity.Respawn.CODEC)
              .filter(value -> !BackroomsLevel.isLevel(value.dimension().getValue()))
              .orElseGet(() -> new ServerPlayerEntity.Respawn(server.getOverworld().getRegistryKey(), server.getOverworld().getSpawnPos(), server.getOverworld().getSpawnAngle(), true));
            player.setSpawnPoint(respawn, false);
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

    /**
     * Called when an entity enters the backrooms, not when they switch between different levels of the backrooms
     *
     * @param entity The entity that entered the backrooms
     */
    public static void onEnterBackrooms(Entity entity) {
        NbtCompound customData = CustomDataHolder.from(entity).VanillaBackrooms$getCustomData();
        customData.putBoolean("isInBackrooms", true);
        CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(customData);

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

            if (VanillaBackroomsConfig.ADVENTURE_IN_BACKROOMS.getValue() && player.getGameMode() == GameMode.SURVIVAL) {
                player.changeGameMode(GameMode.ADVENTURE);
            }

            player.getEnderPearls().forEach(Entity::discard);

            player.clearStatusEffects();
            player.getHungerManager().setFoodLevel(20);
            player.getHungerManager().setSaturationLevel(20F);

            if (VanillaBackroomsConfig.CLEAR_INV.getValue()) {
                player.getInventory().clear();
                player.setExperienceLevel(0);
                player.setExperiencePoints(0);
            }
        }
    }

    public static void onExitBackrooms(Entity entity) {
        NbtCompound customData = CustomDataHolder.from(entity).VanillaBackrooms$getCustomData();
        customData.remove("isInBackrooms");
        CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(customData);

        if (entity instanceof ItemEntity item && item.getStack().getRegistryEntry().isIn(VanillaBackroomsConfig.BACKROOMS_NOT_RETURN.getValue())) {
            item.kill((ServerWorld) item.getWorld());
            item.discard();
            return;
        }

        if (entity instanceof InventoryOwner owner) {
            SimpleInventory inventory = owner.getInventory();

            for (int i = 0; i < inventory.size(); i++) {
                if (inventory.getStack(i).getRegistryEntry().isIn(VanillaBackroomsConfig.BACKROOMS_NOT_RETURN.getValue())) {
                    inventory.setStack(i, ItemStack.EMPTY);
                }
            }
        }

        if (entity instanceof Inventory inventory) {
            for (int i = 0; i < inventory.size(); i++) {
                if (inventory.getStack(i).getRegistryEntry().isIn(VanillaBackroomsConfig.BACKROOMS_NOT_RETURN.getValue())) {
                    inventory.setStack(i, ItemStack.EMPTY);
                }
            }
        }

        if (entity instanceof ServerPlayerEntity player && (VanillaBackroomsConfig.DISABLE_XAERO_MINIMAP.getValue() || VanillaBackroomsConfig.XAERO_FAIR.getValue())) {
            player.sendMessage(Text.literal("§r§e§s§e§t§x§a§e§r§o"));
        }
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

        GameMode previousGameMode = player.getGameMode();

        player.readData(view);

        player.teleportTo(player.getRespawnTarget(false, TeleportTarget.NO_OP));
        player.readRootVehicle(view);
        player.readGameModeData(view);
        player.getServer().getPlayerManager().sendStatusEffects(player);

        GameMode newGameMode = player.getGameMode();

        // Make the client actually know what game-mode it is supposed to be
        player.changeGameMode(previousGameMode);
        player.changeGameMode(newGameMode);
    }
}
