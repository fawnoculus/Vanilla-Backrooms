package net.fawnoculus.vanilla_backrooms.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class PlayerUtil {
    private final static HashMap<UUID, CompoundTag> DATA_CACHE = new HashMap<>();

    /**
     * Sets Custom Data that stays the same even if the player dies.
     */
    public static void setPermanentCustomData(@NotNull ServerPlayer player, CompoundTag nbt) {
        MinecraftServer server = player.level().getServer();

        Path playerData = server.getFile("data")
          .resolve("vanilla_backrooms")
          .resolve("permanent_player_data")
          .resolve(player.getStringUUID() + ".dat");

        if (nbt == null || nbt.isEmpty()) {
            if (playerData.toFile().exists()) {
                boolean ignored = playerData.toFile().delete();
            }

            DATA_CACHE.remove(player.getUUID());
            return;
        }

        try {
            boolean ignored = playerData.getParent().toFile().mkdirs();
            if (playerData.toFile().exists()) {
                boolean ignored2 = playerData.toFile().delete();
            }
            boolean ignored3 = playerData.toFile().createNewFile();

            NbtIo.write(nbt, playerData);
            DATA_CACHE.put(player.getUUID(), nbt);
        } catch (IOException ignored) {
        }
    }

    /**
     * Returns Custom Data that stays the same even if the player dies
     * (If you set values in the NbtCompound you must also use {@link PlayerUtil#setPermanentCustomData(ServerPlayer, CompoundTag)} for the custom data to actually stay)
     */
    public static CompoundTag getPermanentCustomData(@NotNull ServerPlayer player) {
        if (DATA_CACHE.containsKey(player.getUUID())) {
            return DATA_CACHE.get(player.getUUID());
        }

        MinecraftServer server = player.level().getServer();

        Path playerData = server.getFile("data")
          .resolve("vanilla_backrooms")
          .resolve("permanent_player_data")
          .resolve(player.getStringUUID() + ".dat");

        CompoundTag nbt = new CompoundTag();
        try {
            nbt = Objects.requireNonNull(NbtIo.read(playerData));
        } catch (Exception ignored) {
        }

        DATA_CACHE.put(player.getUUID(), nbt);

        return nbt;
    }
}
