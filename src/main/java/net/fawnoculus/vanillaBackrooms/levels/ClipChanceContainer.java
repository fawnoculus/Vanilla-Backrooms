package net.fawnoculus.vanillaBackrooms.levels;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import net.fawnoculus.vanillaBackrooms.misc.BackroomsHandler;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public record ClipChanceContainer(Map<Identifier, ClipChance> clipChances) {
	public static final Codec<ClipChanceContainer> CODEC = Codec.unboundedMap(Identifier.CODEC, ClipChance.CODEC).xmap(ClipChanceContainer::new, ClipChanceContainer::clipChances);
	public static final ClipChanceContainer DEFAULT = makeDefault();

	public ClipChanceContainer(Map<Identifier, ClipChance> clipChances) {
		this.clipChances = new HashMap<>(clipChances);

		if (DEFAULT != null) {
			for (Map.Entry<Identifier, ClipChance> entry : DEFAULT.clipChances().entrySet()) {
				if(!this.clipChances.containsKey(entry.getKey())) {
					this.clipChances.put(entry.getKey(), entry.getValue());
				}
			}
		}
	}

	private static @NotNull ClipChanceContainer makeDefault() {
		ImmutableMap<Identifier, Integer> level0Chances = new ImmutableMap.Builder<Identifier, Integer>()
		  .put(BackroomsHandler.getLevelId(1), 100)
		  .put(BackroomsHandler.getLevelId(2), 5)
		  .build();

		ImmutableMap<Identifier, Integer> level1Chances = new ImmutableMap.Builder<Identifier, Integer>()
		  .put(BackroomsHandler.getLevelId(2), 100)
		  .build();

		ImmutableMap<Identifier, Integer> level2Chances = new ImmutableMap.Builder<Identifier, Integer>()
		  .put(BackroomsHandler.getLevelId(0), 5)
		  .put(BackroomsHandler.getLevelId(1), 5)
		  .put(BackroomsHandler.getLevelId(3), 200)
		  .put(BackroomsHandler.getLevelId(4), 10)
		  .build();

		ImmutableMap<Identifier, Integer> level3Chances = new ImmutableMap.Builder<Identifier, Integer>()
		  .put(BackroomsHandler.getLevelId(2), 5)
		  .put(BackroomsHandler.getLevelId(4), 200)
		  .put(BackroomsHandler.getLevelId(5), 10)
		  .build();

		ImmutableMap<Identifier, Integer> level4Chances = new ImmutableMap.Builder<Identifier, Integer>()
		  .put(BackroomsHandler.getLevelId(3), 5)
		  .put(BackroomsHandler.getLevelId(5), 200)
		  .build();

		ImmutableMap<Identifier, Integer> level5Chances = new ImmutableMap.Builder<Identifier, Integer>()
		  .put(World.OVERWORLD.getValue(), 200)
		  .put(BackroomsHandler.getLevelId(0), 2)
		  .put(BackroomsHandler.getLevelId(1), 4)
		  .put(BackroomsHandler.getLevelId(2), 8)
		  .put(BackroomsHandler.getLevelId(3), 16)
		  .put(BackroomsHandler.getLevelId(4), 32)
		  .build();

		ImmutableMap<Identifier, ClipChance> clipChances = new ImmutableMap.Builder<Identifier, ClipChance>()
		  .put(BackroomsHandler.getLevelId(0), new ClipChance(level0Chances))
		  .put(BackroomsHandler.getLevelId(1), new ClipChance(level1Chances))
		  .put(BackroomsHandler.getLevelId(2), new ClipChance(level2Chances))
		  .put(BackroomsHandler.getLevelId(3), new ClipChance(level3Chances))
		  .put(BackroomsHandler.getLevelId(4), new ClipChance(level4Chances))
		  .put(BackroomsHandler.getLevelId(5), new ClipChance(level5Chances))
		  .build();

		return new ClipChanceContainer(clipChances);
	}

	public ClipChance get(Identifier levelId) {
		return Objects.requireNonNull(clipChances.get(levelId), "Failed to get clip Chances for world with id '" + levelId + "', please make sure that the config defines clip chances for every dimension from which you can clip");
	}
}
