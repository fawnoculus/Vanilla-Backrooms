package net.fawnoculus.vanillaBackrooms.levels;

import com.mojang.serialization.Codec;
import net.fawnoculus.vanillaBackrooms.VanillaBackrooms;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ClipChance {
	public static final Codec<ClipChance> CODEC = Codec
	  .unboundedMap(Identifier.CODEC, Codec.INT)
	  .xmap(ClipChance::new, ClipChance::getClipChances);
	private final Map<Identifier, Integer> clipChances;
	private final int totalClipChance;

	public ClipChance(Map<Identifier, Integer> map) {
		int total = 0;

		for (int chance : map.values()) {
			total += chance;
		}

		clipChances = new HashMap<>(map);
		totalClipChance = total;
	}

	public Map<Identifier, Integer> getClipChances() {
		return clipChances;
	}

	public Identifier get(Random random) {
		int selected = random.nextInt(0, totalClipChance);
		VanillaBackrooms.LOGGER.info("{}/{}, {}", selected, totalClipChance, clipChances);

		for (Map.Entry<Identifier, Integer> entry : clipChances.entrySet()) {
			selected -= entry.getValue();
			if (selected <= 0) {
				if (entry.getKey().equals(World.OVERWORLD.getValue())) {
					VanillaBackrooms.LOGGER.warn("YAY");
				}
				return entry.getKey();
			}
		}

		throw new IllegalStateException("Failed to calculate Clip Chance");
	}
}
