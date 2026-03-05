package net.fawnoculus.vanillaBackrooms.blocks.entities;

import net.fawnoculus.vanillaBackrooms.VanillaBackroomsConfig;
import net.fawnoculus.vanillaBackrooms.blocks.ModBlockEntities;
import net.fawnoculus.vanillaBackrooms.blocks.ModBlocks;
import net.fawnoculus.vanillaBackrooms.levels.BackroomsGenerator;
import net.fawnoculus.vanillaBackrooms.levels.BackroomsLevel;
import net.fawnoculus.vanillaBackrooms.util.MixinUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BackroomsGeneratorBE extends BlockEntity {
    private static final int HORIZONTAL_OFFSET = 48;
    private static final int Y_LEVEL = 0;
    private boolean hasPlacedNorth = false;
    private boolean hasPlacedEast = false;
    private boolean hasPlacedSouth = false;
    private boolean hasPlacedWest = false;

    public BackroomsGeneratorBE(@NotNull BlockPos pos, @NotNull BlockState state) {
        super(ModBlockEntities.BACKROOMS_GENERATOR_BE, pos, state);
    }

    public static Optional<Throwable> tryPlaceSegment(ServerWorld world, BlockPos pos, BackroomsGenerator generator) {
        if (!shouldPlaceSegment(world, pos)) {
            return Optional.empty();
        }

        try {
            MixinUtil.setSuppressBlockAttachedEntityError(VanillaBackroomsConfig.SUPPRESS_BLOCK_ATTACHED_ENTITY_ERROR.getValue());
            generator.placeBackroomsSegment(world, pos);
            MixinUtil.setSuppressBlockAttachedEntityError(false);
            world.setBlockState(pos, ModBlocks.BACKROOMS_GENERATOR.getDefaultState());
            return Optional.empty();
        } catch (Throwable throwable) {
            return Optional.of(throwable);
        }
    }

    public static boolean shouldPlaceSegment(@NotNull ServerWorld world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        return block != ModBlocks.FINISHED_BACKROOMS_GENERATOR && block != ModBlocks.BACKROOMS_GENERATOR;
    }

    public static boolean isSectionLoaded(@NotNull ServerWorld world, @NotNull BlockPos pos) {
        int chunkX = ChunkSectionPos.getSectionCoord(pos.getX());
        int chunkZ = ChunkSectionPos.getSectionCoord(pos.getZ());

        return world.isChunkLoaded(chunkX, chunkZ)
          && world.isChunkLoaded(chunkX + 1, chunkZ)
          && world.isChunkLoaded(chunkX + 2, chunkZ)
          && world.isChunkLoaded(chunkX, chunkZ + 1)
          && world.isChunkLoaded(chunkX + 1, chunkZ + 1)
          && world.isChunkLoaded(chunkX + 2, chunkZ + 1)
          && world.isChunkLoaded(chunkX, chunkZ + 2)
          && world.isChunkLoaded(chunkX + 1, chunkZ + 2)
          && world.isChunkLoaded(chunkX + 2, chunkZ + 2);
    }

    public static void tick(@NotNull World world, @NotNull BlockPos pos, @NotNull BlockState ignored, @NotNull BackroomsGeneratorBE backroomsGeneratorBE) {
        if (pos.getY() == Y_LEVEL && world instanceof ServerWorld serverWorld) {
            BackroomsLevel level = BackroomsLevel.getLevel(world.getRegistryKey().getValue());
            if (level == null) {
                world.setBlockState(pos, Blocks.BARRIER.getDefaultState(), Block.NOTIFY_LISTENERS);
                return;
            }

            backroomsGeneratorBE.tryPlaceNeighbours(serverWorld, pos, level.generator());
        }
    }

    private void tryPlaceNeighbours(@NotNull ServerWorld world, @NotNull BlockPos pos, @NotNull BackroomsGenerator generator) {
        if (!this.hasPlacedNorth && isSectionLoaded(world, pos.add(0, 0, -HORIZONTAL_OFFSET))) {
            this.hasPlacedNorth = tryPlaceSegment(world, pos.add(0, 0, -HORIZONTAL_OFFSET), generator).isEmpty();
        }
        if (!this.hasPlacedEast && isSectionLoaded(world, pos.add(HORIZONTAL_OFFSET, 0, 0))) {
            this.hasPlacedEast = tryPlaceSegment(world, pos.add(HORIZONTAL_OFFSET, 0, 0), generator).isEmpty();
        }
        if (!this.hasPlacedSouth && isSectionLoaded(world, pos.add(0, 0, HORIZONTAL_OFFSET))) {
            this.hasPlacedSouth = tryPlaceSegment(world, pos.add(0, 0, HORIZONTAL_OFFSET), generator).isEmpty();
        }
        if (!this.hasPlacedWest && isSectionLoaded(world, pos.add(-HORIZONTAL_OFFSET, 0, 0))) {
            this.hasPlacedWest = tryPlaceSegment(world, pos.add(-HORIZONTAL_OFFSET, 0, 0), generator).isEmpty();
        }

        if (this.hasPlacedAll()) {
            world.setBlockState(pos, ModBlocks.FINISHED_BACKROOMS_GENERATOR.getDefaultState());
        }
    }

    @Contract(pure = true)
    private boolean hasPlacedAll() {
        return this.hasPlacedNorth && this.hasPlacedEast && this.hasPlacedSouth && this.hasPlacedWest;
    }
}
