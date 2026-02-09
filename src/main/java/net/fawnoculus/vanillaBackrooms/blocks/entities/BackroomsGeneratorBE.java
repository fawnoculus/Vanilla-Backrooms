package net.fawnoculus.vanillaBackrooms.blocks.entities;

import com.google.common.collect.ImmutableMap;
import net.fawnoculus.vanillaBackrooms.VanillaBackrooms;
import net.fawnoculus.vanillaBackrooms.blocks.ModBlockEntities;
import net.fawnoculus.vanillaBackrooms.blocks.ModBlocks;
import net.fawnoculus.vanillaBackrooms.util.BackroomsUtil;
import net.fawnoculus.vanillaBackrooms.util.MixinUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BackroomsGeneratorBE extends BlockEntity {
	private static final ImmutableMap<Identifier, DimensionData> DIMENSION_DATA = new ImmutableMap.Builder<Identifier, DimensionData>()
	  .put(BackroomsUtil.LEVEL_0.getValue(), new SimpleDimensionDataBuilder(true)
		.addStructure(900, "level_0/ring9")
		.addStructure(800, "level_0/ring8")
		.addStructure(700, "level_0/ring7")
		.addStructure(600, "level_0/ring6")
		.addStructure(500, "level_0/ring5")
		.addStructure(400, "level_0/ring4")
		.addStructure(300, "level_0/ring3")
		.addStructure(200, "level_0/ring2")
		.addStructure(100, "level_0/ring1")
		.addStructure(1, "level_0/ring0")
		.addStructure(0, "level_0/spawn")
		.build()
	  )
	  .put(BackroomsUtil.LEVEL_1.getValue(), new SimpleDimensionDataBuilder(false)
		.addStructure(0, "level_1/spawn")
		.build()
	  )
	  .build();


	// TODO: Fix the structures so that we don't have to do this shit
	private static final Identifier STRUCTURE_START = Identifier.of("craft_attack", "backrooms_start");
	private static final int HORIZONTAL_OFFSET = 48;
	private static final int Y_LEVEL = 0;
	private DimensionData dimensionData = null;
	private boolean hashChecked = false;

	public BackroomsGeneratorBE(BlockPos pos, BlockState state) {
		super(ModBlockEntities.BACKROOMS_GENERATOR_BE, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState ignored, BackroomsGeneratorBE backroomsGeneratorBE) {
		if (!backroomsGeneratorBE.hashChecked) {
			backroomsGeneratorBE.dimensionData = DIMENSION_DATA.get(world.getRegistryKey().getValue());
			backroomsGeneratorBE.hashChecked = true;
		}

		if (backroomsGeneratorBE.dimensionData != null
		  && world instanceof ServerWorld serverWorld
		  && pos.getY() == Y_LEVEL
		) {
			tryPlaceNeighbours(serverWorld, pos, backroomsGeneratorBE.dimensionData);
		}
	}

	private static void tryPlaceNeighbours(ServerWorld world, BlockPos pos, DimensionData dimensionData) {
		if (world.getBlockState(pos.add(0, 0, HORIZONTAL_OFFSET)).getBlock() != ModBlocks.BACKROOMS_GENERATOR) {
			dimensionData.placeBackroomsSegment(world, pos.add(0, 0, HORIZONTAL_OFFSET));
		}
		if (world.getBlockState(pos.add(HORIZONTAL_OFFSET, 0, 0)).getBlock() != ModBlocks.BACKROOMS_GENERATOR) {
			dimensionData.placeBackroomsSegment(world, pos.add(HORIZONTAL_OFFSET, 0, 0));
		}
		if (world.getBlockState(pos.add(0, 0, -HORIZONTAL_OFFSET)).getBlock() != ModBlocks.BACKROOMS_GENERATOR) {
			dimensionData.placeBackroomsSegment(world, pos.add(0, 0, -HORIZONTAL_OFFSET));
		}
		if (world.getBlockState(pos.add(-HORIZONTAL_OFFSET, 0, 0)).getBlock() != ModBlocks.BACKROOMS_GENERATOR) {
			dimensionData.placeBackroomsSegment(world, pos.add(-HORIZONTAL_OFFSET, 0, 0));
		}
	}

	public static void placeBackroomsSegment(ServerWorld world, BlockPos pos) throws NullPointerException {
		Objects.requireNonNull(DIMENSION_DATA.get(world.getRegistryKey().getValue()), "No Backrooms dimension data found").placeBackroomsSegment(world, pos);
	}

	private interface DimensionData {
		void placeBackroomsSegment(ServerWorld world, BlockPos pos);
	}

	private record SimpleDimensionData(boolean rotateStructures,
									   List<Pair<Integer, RegistryKey<StructurePool>>> structures) implements DimensionData {
		public RegistryKey<StructurePool> poolFromPos(BlockPos pos) {
			double distanceFromCenter = Math.abs((double) pos.getX() * pos.getX() + pos.getZ() * pos.getZ());

			for (Pair<Integer, RegistryKey<StructurePool>> pair : structures) {
				if (distanceFromCenter >= pair.getLeft()) {
					return pair.getRight();
				}
			}

			return structures.getFirst().getRight();
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

			if (success) {
				world.setBlockState(pos, ModBlocks.BACKROOMS_GENERATOR.getDefaultState());
			} else {
				VanillaBackrooms.LOGGER.warn("Failed to generate backrooms segment");
			}
		}
	}

	private static class SimpleDimensionDataBuilder {
		private final boolean rotateStructures;
		private final List<Pair<Integer, RegistryKey<StructurePool>>> structures = new ArrayList<>();

		public SimpleDimensionDataBuilder(boolean rotateStructures) {
			this.rotateStructures = rotateStructures;
		}

		public SimpleDimensionDataBuilder addStructure(int distanceFromOrigin, String name) {
			int squareDistance = distanceFromOrigin * distanceFromOrigin;

			if (!this.structures.isEmpty() && this.structures.getLast().getLeft() < squareDistance) {
				throw new IllegalArgumentException("Wrong sort order");
			}

			this.structures.add(
			  new Pair<>(squareDistance, RegistryKey.of(RegistryKeys.TEMPLATE_POOL, VanillaBackrooms.id(name)))
			);

			return this;
		}

		public SimpleDimensionData build() {
			if (this.structures.isEmpty()) {
				throw new IllegalStateException("Cannot build Dimension data without any structure pools");
			}
			return new SimpleDimensionData(this.rotateStructures, List.copyOf(structures));
		}
	}
}
