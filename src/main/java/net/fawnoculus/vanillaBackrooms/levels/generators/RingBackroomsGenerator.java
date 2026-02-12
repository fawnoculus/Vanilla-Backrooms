package net.fawnoculus.vanillaBackrooms.levels.generators;

import net.fawnoculus.vanillaBackrooms.VanillaBackrooms;
import net.fawnoculus.vanillaBackrooms.levels.BackroomsGenerator;
import net.fawnoculus.vanillaBackrooms.util.MixinUtil;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.ChunkRandom;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public record RingBackroomsGenerator(
  boolean rotateStructures,
  List<Pair<Integer, RegistryKey<StructurePool>>> structures
) implements BackroomsGenerator {

	public static Builder builder(boolean rotateStructures) {
		return new Builder(rotateStructures);
	}

	private static void placeSegmentWithRotation(
	  ServerWorld world,
	  BlockPos pos,
	  RegistryEntry.Reference<StructurePool> structurePool,
	  BlockRotation rotation
	) {
		int xOffset = 0;
		int zOffset = 0;

		switch (rotation) {
			case NONE -> {
			}
			case CLOCKWISE_90 -> xOffset = HORIZONTAL_OFFSET - 1;
			case COUNTERCLOCKWISE_90 -> zOffset = HORIZONTAL_OFFSET - 1;
			case CLOCKWISE_180 -> {
				xOffset = HORIZONTAL_OFFSET - 1;
				zOffset = HORIZONTAL_OFFSET - 1;
			}
		}

		BlockPos placementPos = pos.add(xOffset, 1, zOffset);

		MixinUtil.setRandomBlockRotationOverride(rotation);

		boolean success = StructurePoolBasedGenerator.generate(world, structurePool, STRUCTURE_START, 20, placementPos, false);

		if (!success) {
			VanillaBackrooms.LOGGER.warn("Failed to generate backrooms segment");
		}
	}

	public RegistryKey<StructurePool> poolFromPos(BlockPos pos) {
		double distanceFromCenter = Math.abs((double) pos.getX() * pos.getX() + pos.getZ() * pos.getZ());

		for (Pair<Integer, RegistryKey<StructurePool>> pair : structures) {
			if (distanceFromCenter >= pair.getLeft()) {
				return pair.getRight();
			}
		}

		return structures.getLast().getRight();
	}

	@Override
	public void placeBackroomsSegment(ServerWorld world, BlockPos pos) {
		Registry<StructurePool> registry = world.getRegistryManager().getOrThrow(RegistryKeys.TEMPLATE_POOL);
		Optional<RegistryEntry.Reference<StructurePool>> registryEntry = registry.getOptional(this.poolFromPos(pos));
		if (registryEntry.isEmpty()) {
			return;
		}

		BlockRotation rotation = BlockRotation.NONE;

		if (this.rotateStructures) {
			ChunkRandom chunkRandom = new ChunkRandom(new CheckedRandom(0L));
			chunkRandom.setCarverSeed(
			  world.getSeed(),
			  ChunkSectionPos.getSectionCoord(pos.getX()),
			  ChunkSectionPos.getSectionCoord(pos.getZ())
			);
			rotation = BlockRotation.random(chunkRandom);
		}

		placeSegmentWithRotation(world, pos, registryEntry.get(), rotation);
	}

	public static class Builder {
		private final boolean rotateStructures;
		private final List<Pair<Integer, RegistryKey<StructurePool>>> structures = new ArrayList<>();

		public Builder(boolean rotateStructures) {
			this.rotateStructures = rotateStructures;
		}

		public Builder addStructure(int distanceFromCenter, String name) {
			int squareDistance = distanceFromCenter * distanceFromCenter;

			this.structures.add(
			  new Pair<>(squareDistance, RegistryKey.of(RegistryKeys.TEMPLATE_POOL, VanillaBackrooms.id(name)))
			);

			return this;
		}

		public RingBackroomsGenerator build() {
			if (this.structures.isEmpty()) {
				throw new IllegalStateException("Cannot build Dimension data without any structure pools");
			}
			structures.sort(Comparator.comparingInt(Pair::getLeft));
			return new RingBackroomsGenerator(this.rotateStructures, List.copyOf(structures.reversed()));
		}
	}
}
