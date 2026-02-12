package net.fawnoculus.vanillaBackrooms.blocks.entities;

import net.fawnoculus.vanillaBackrooms.blocks.ModBlockEntities;
import net.fawnoculus.vanillaBackrooms.blocks.ModBlocks;
import net.fawnoculus.vanillaBackrooms.levels.BackroomsGenerator;
import net.fawnoculus.vanillaBackrooms.levels.BackroomsLevel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class BackroomsGeneratorBE extends BlockEntity {
	private static final int HORIZONTAL_OFFSET = 48;
	private static final int Y_LEVEL = 0;
	private int attempts = 0;

	public BackroomsGeneratorBE(BlockPos pos, BlockState state) {
		super(ModBlockEntities.BACKROOMS_GENERATOR_BE, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState ignored, BackroomsGeneratorBE backroomsGeneratorBE) {
		if (pos.getY() == Y_LEVEL && world instanceof ServerWorld serverWorld) {
			try {
				backroomsGeneratorBE.attempts++;
				tryPlaceNeighbours(
				  serverWorld,
				  pos,
				  Objects.requireNonNull(
					BackroomsLevel.getLevel(world.getRegistryKey().getValue())
				  ).generator()
				);
			} catch (Throwable ignored2) {
				if (backroomsGeneratorBE.attempts > 5) {
					world.setBlockState(pos, ModBlocks.FINISHED_BACKROOMS_GENERATOR.getDefaultState());
				}
			}
		}
	}

	private static void tryPlaceNeighbours(ServerWorld world, BlockPos pos, BackroomsGenerator generator) {
		if (shouldPlaceSegment(world, pos.add(0, 0, HORIZONTAL_OFFSET))) {
			generator.placeBackroomsSegment(world, pos.add(0, 0, HORIZONTAL_OFFSET));
			world.setBlockState(pos.add(0, 0, HORIZONTAL_OFFSET), ModBlocks.BACKROOMS_GENERATOR.getDefaultState());
		}
		if (shouldPlaceSegment(world, pos.add(HORIZONTAL_OFFSET, 0, 0))) {
			generator.placeBackroomsSegment(world, pos.add(HORIZONTAL_OFFSET, 0, 0));
			world.setBlockState(pos.add(HORIZONTAL_OFFSET, 0, 0), ModBlocks.BACKROOMS_GENERATOR.getDefaultState());
		}
		if (shouldPlaceSegment(world, pos.add(0, 0, -HORIZONTAL_OFFSET))) {
			generator.placeBackroomsSegment(world, pos.add(0, 0, -HORIZONTAL_OFFSET));
			world.setBlockState(pos.add(0, 0, -HORIZONTAL_OFFSET), ModBlocks.BACKROOMS_GENERATOR.getDefaultState());
		}
		if (shouldPlaceSegment(world, pos.add(-HORIZONTAL_OFFSET, 0, 0))) {
			generator.placeBackroomsSegment(world, pos.add(-HORIZONTAL_OFFSET, 0, 0));
			world.setBlockState(pos.add(-HORIZONTAL_OFFSET, 0, 0), ModBlocks.BACKROOMS_GENERATOR.getDefaultState());
		}

		world.setBlockState(pos, ModBlocks.FINISHED_BACKROOMS_GENERATOR.getDefaultState());
	}

	public static boolean shouldPlaceSegment(ServerWorld world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		return block != ModBlocks.FINISHED_BACKROOMS_GENERATOR && block != ModBlocks.BACKROOMS_GENERATOR;
	}
}
