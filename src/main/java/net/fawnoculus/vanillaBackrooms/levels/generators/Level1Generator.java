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

import java.util.Optional;

public class Level1Generator implements BackroomsGenerator {
	private static final RegistryKey<StructurePool> START = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, VanillaBackrooms.id("level_1/start"));
	private static final RegistryKey<StructurePool> PARKING = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, VanillaBackrooms.id("level_1/parking"));
	private static final int RING_0_DISTANCE = 1;
	private static final RegistryKey<StructurePool> RING_0 = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, VanillaBackrooms.id("level_1/ring0"));
	private static final int RING_1_DISTANCE = 200 * 200;
	private static final RegistryKey<StructurePool> RING_1 = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, VanillaBackrooms.id("level_1/ring1"));
	private static final int RING_2_DISTANCE = 400 * 400;
	private static final RegistryKey<StructurePool> RING_2 = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, VanillaBackrooms.id("level_1/ring2"));
	private static final int RING_3_DISTANCE = 600 * 600;
	private static final RegistryKey<StructurePool> RING_3 = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, VanillaBackrooms.id("level_1/ring3"));
	private static final int RING_4_DISTANCE = 800 * 800;
	private static final RegistryKey<StructurePool> RING_4 = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, VanillaBackrooms.id("level_1/ring4"));

	@Override
	public void placeBackroomsSegment(ServerWorld world, BlockPos pos) {
		Pair<BlockRotation, RegistryKey<StructurePool>> structure = getStructure(world, pos);

		Registry<StructurePool> registry = world.getRegistryManager().getOrThrow(RegistryKeys.TEMPLATE_POOL);
		Optional<RegistryEntry.Reference<StructurePool>> registryEntry = registry.getOptional(structure.getRight());
		if (registryEntry.isEmpty()) {
			return;
		}

		int xOffset = 0;
		int zOffset = 0;

		switch (structure.getLeft()) {
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

		MixinUtil.setRandomBlockRotationOverride(structure.getLeft());

		boolean success = StructurePoolBasedGenerator.generate(world, registryEntry.get(), STRUCTURE_START, 20, placementPos, false);

		if (!success) {
			VanillaBackrooms.LOGGER.warn("Failed to generate backrooms segment");
		}
	}

	public Pair<BlockRotation, RegistryKey<StructurePool>> getStructure(ServerWorld world, BlockPos pos) {
		ChunkRandom chunkRandom = new ChunkRandom(new CheckedRandom(0L));
		chunkRandom.setCarverSeed(
		  world.getSeed(),
		  ChunkSectionPos.getSectionCoord(pos.getX()) / 8,
		  ChunkSectionPos.getSectionCoord(pos.getZ()) / 8
		);

		double distanceFromCenter = Math.abs((double) pos.getX() * pos.getX() + pos.getZ() * pos.getZ());
		if (distanceFromCenter < RING_0_DISTANCE) {
			return new Pair<>(BlockRotation.NONE, START);
		}


		if (chunkRandom.nextInt(8) == 0) {
			return new Pair<>(BlockRotation.NONE, PARKING);
		}

		chunkRandom.setCarverSeed(
		  world.getSeed(),
		  ChunkSectionPos.getSectionCoord(pos.getX()),
		  ChunkSectionPos.getSectionCoord(pos.getZ())
		);
		BlockRotation rotation = BlockRotation.random(chunkRandom);

		if (distanceFromCenter < RING_1_DISTANCE) {
			return new Pair<>(rotation, RING_0);
		}

		if (distanceFromCenter < RING_2_DISTANCE) {
			return new Pair<>(rotation, RING_1);
		}

		if (distanceFromCenter < RING_3_DISTANCE) {
			return new Pair<>(rotation, RING_2);
		}

		if (distanceFromCenter < RING_4_DISTANCE) {
			return new Pair<>(rotation, RING_3);
		}

		return new Pair<>(rotation, RING_4);
	}
}
