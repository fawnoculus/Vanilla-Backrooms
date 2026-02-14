package net.fawnoculus.vanillaBackrooms.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class PlayerUtil {
	private final static HashMap<UUID, NbtCompound> DATA_CACHE = new HashMap<>();

	/**
	 * Sets Custom Data that stays the same even if the player dies.
	 */
	public static void setPermanentCustomData(@NotNull ServerPlayerEntity player, NbtCompound nbt) {
		MinecraftServer server = player.getServer();
		assert server != null;

		Path playerData = server.getPath("data")
		  .resolve("vanilla_backrooms")
		  .resolve("permanent_player_data")
		  .resolve(player.getUuidAsString() + ".dat");

		if (nbt == null || nbt.isEmpty()) {
			if (playerData.toFile().exists()) {
				boolean ignored = playerData.toFile().delete();
			}

			DATA_CACHE.remove(player.getUuid());
			return;
		}

		try {
			boolean ignored = playerData.getParent().toFile().mkdirs();
			if (playerData.toFile().exists()) {
				boolean ignored2 = playerData.toFile().delete();
			}
			boolean ignored3 = playerData.toFile().createNewFile();

			NbtIo.write(nbt, playerData);
			DATA_CACHE.put(player.getUuid(), nbt);
		} catch (IOException ignored) {
		}
	}

	/**
	 * Returns Custom Data that stays the same even if the player dies
	 */
	public static NbtCompound getPermanentCustomData(@NotNull ServerPlayerEntity player) {
		if (DATA_CACHE.containsKey(player.getUuid())) {
			return DATA_CACHE.get(player.getUuid());
		}

		MinecraftServer server = player.getServer();
		assert server != null;

		Path playerData = server.getPath("data")
		  .resolve("vanilla_backrooms")
		  .resolve("permanent_player_data")
		  .resolve(player.getUuidAsString() + ".dat");

		NbtCompound nbt = new NbtCompound();
		try {
			nbt = Objects.requireNonNull(NbtIo.read(playerData));
		} catch (Exception ignored) {
		}

		DATA_CACHE.put(player.getUuid(), nbt);

		return nbt;
	}
}
