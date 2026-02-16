package net.fawnoculus.vanillaBackrooms.blocks.entities;

import net.fawnoculus.vanillaBackrooms.blocks.ModBlockEntities;
import net.fawnoculus.vanillaBackrooms.blocks.ModBlocks;
import net.fawnoculus.vanillaBackrooms.levels.BackroomsGenerator;
import net.fawnoculus.vanillaBackrooms.levels.BackroomsLevel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class BackroomsGeneratorBE extends BlockEntity {
	private static final int HORIZONTAL_OFFSET = 48;
	private static final int Y_LEVEL = 0;
	private int attempts = 0;
	private boolean hasPlacedNorth = false;
	private boolean hasPlacedEast = false;
	private boolean hasPlacedSouth = false;
	private boolean hasPlacedWest = false;

	public BackroomsGeneratorBE(BlockPos pos, BlockState state) {
		super(ModBlockEntities.BACKROOMS_GENERATOR_BE, pos, state);
	}

	public static Optional<Throwable> tryPlaceSegment(ServerWorld world, BlockPos pos, BackroomsGenerator generator) {
		if (!shouldPlaceSegment(world, pos)) {
			return Optional.empty();
		}

		try {
			generator.placeBackroomsSegment(world, pos.add(0, 0, HORIZONTAL_OFFSET));
			world.setBlockState(pos.add(0, 0, HORIZONTAL_OFFSET), ModBlocks.BACKROOMS_GENERATOR.getDefaultState());
			return Optional.empty();
		} catch (Throwable throwable) {
			return Optional.of(throwable);
		}
	}

	public static boolean shouldPlaceSegment(ServerWorld world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		return block != ModBlocks.FINISHED_BACKROOMS_GENERATOR && block != ModBlocks.BACKROOMS_GENERATOR;
	}

	public static void tick(World world, BlockPos pos, BlockState ignored, BackroomsGeneratorBE backroomsGeneratorBE) {
		if (pos.getY() == Y_LEVEL && world instanceof ServerWorld serverWorld) {
			BackroomsLevel level = BackroomsLevel.getLevel(world.getRegistryKey().getValue());
			if (level == null) {
				world.setBlockState(pos, Blocks.BARRIER.getDefaultState(), Block.NOTIFY_LISTENERS);
				return;
			}

			backroomsGeneratorBE.tryPlaceNeighbours(serverWorld, pos, level.generator());
		}
	}

	private void tryPlaceNeighbours(ServerWorld world, BlockPos pos, BackroomsGenerator generator) {
		this.attempts++;

		if (!this.hasPlacedNorth) {
			this.hasPlacedNorth = tryPlaceSegment(world, pos.add(0, 0, -HORIZONTAL_OFFSET), generator).isEmpty();
		}
		if (!this.hasPlacedEast) {
			this.hasPlacedEast = tryPlaceSegment(world, pos.add(HORIZONTAL_OFFSET, 0, 0), generator).isEmpty();
		}
		if (!this.hasPlacedSouth) {
			this.hasPlacedSouth = tryPlaceSegment(world, pos.add(0, 0, HORIZONTAL_OFFSET), generator).isEmpty();
		}
		if (!this.hasPlacedWest) {
			this.hasPlacedWest = tryPlaceSegment(world, pos.add(-HORIZONTAL_OFFSET, 0, 0), generator).isEmpty();
		}

		if (this.attempts > 5 || this.hasPlacedAll()) {
			world.setBlockState(pos, ModBlocks.FINISHED_BACKROOMS_GENERATOR.getDefaultState());
		}
	}

	private boolean hasPlacedAll() {
		return this.hasPlacedNorth && this.hasPlacedEast && this.hasPlacedSouth && this.hasPlacedWest;
	}
}
