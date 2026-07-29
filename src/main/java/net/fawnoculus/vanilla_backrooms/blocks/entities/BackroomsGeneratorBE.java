package net.fawnoculus.vanilla_backrooms.blocks.entities;

import net.fawnoculus.vanilla_backrooms.VanillaBackroomsConfig;
import net.fawnoculus.vanilla_backrooms.blocks.ModBlockEntities;
import net.fawnoculus.vanilla_backrooms.blocks.ModBlocks;
import net.fawnoculus.vanilla_backrooms.levels.BackroomsGenerator;
import net.fawnoculus.vanilla_backrooms.levels.BackroomsLevel;
import net.fawnoculus.vanilla_backrooms.util.MixinUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

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

    public static Optional<Throwable> tryPlaceSegment(ServerLevel world, BlockPos pos, BackroomsGenerator generator) {
        if (!shouldPlaceSegment(world, pos)) {
            return Optional.empty();
        }

        try {
            MixinUtil.setSuppressBlockAttachedEntityError(VanillaBackroomsConfig.SUPPRESS_BLOCK_ATTACHED_ENTITY_ERROR.getValue());
            generator.placeBackroomsSegment(world, pos);
            MixinUtil.setSuppressBlockAttachedEntityError(false);
            world.setBlockAndUpdate(pos, ModBlocks.BACKROOMS_GENERATOR.defaultBlockState());
            return Optional.empty();
        } catch (Throwable throwable) {
            return Optional.of(throwable);
        }
    }

    public static boolean shouldPlaceSegment(@NotNull ServerLevel world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        return block != ModBlocks.FINISHED_BACKROOMS_GENERATOR && block != ModBlocks.BACKROOMS_GENERATOR;
    }

    public static boolean isSectionLoaded(@NotNull ServerLevel world, @NotNull BlockPos pos) {
        int chunkX = SectionPos.blockToSectionCoord(pos.getX());
        int chunkZ = SectionPos.blockToSectionCoord(pos.getZ());

        return world.hasChunk(chunkX, chunkZ)
          && world.hasChunk(chunkX + 1, chunkZ)
          && world.hasChunk(chunkX + 2, chunkZ)
          && world.hasChunk(chunkX, chunkZ + 1)
          && world.hasChunk(chunkX + 1, chunkZ + 1)
          && world.hasChunk(chunkX + 2, chunkZ + 1)
          && world.hasChunk(chunkX, chunkZ + 2)
          && world.hasChunk(chunkX + 1, chunkZ + 2)
          && world.hasChunk(chunkX + 2, chunkZ + 2);
    }

    public static void tick(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState ignored, @NotNull BackroomsGeneratorBE backroomsGeneratorBE) {
        if (pos.getY() == Y_LEVEL && world instanceof ServerLevel serverWorld) {
            BackroomsLevel level = BackroomsLevel.getLevel(world.dimension().identifier());
            if (level == null) {
                world.setBlock(pos, Blocks.BARRIER.defaultBlockState(), Block.UPDATE_CLIENTS);
                return;
            }

            backroomsGeneratorBE.tryPlaceNeighbours(serverWorld, pos, level.generator());
        }
    }

    private void tryPlaceNeighbours(@NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull BackroomsGenerator generator) {
        if (!this.hasPlacedNorth && isSectionLoaded(world, pos.offset(0, 0, -HORIZONTAL_OFFSET))) {
            this.hasPlacedNorth = tryPlaceSegment(world, pos.offset(0, 0, -HORIZONTAL_OFFSET), generator).isEmpty();
        }
        if (!this.hasPlacedEast && isSectionLoaded(world, pos.offset(HORIZONTAL_OFFSET, 0, 0))) {
            this.hasPlacedEast = tryPlaceSegment(world, pos.offset(HORIZONTAL_OFFSET, 0, 0), generator).isEmpty();
        }
        if (!this.hasPlacedSouth && isSectionLoaded(world, pos.offset(0, 0, HORIZONTAL_OFFSET))) {
            this.hasPlacedSouth = tryPlaceSegment(world, pos.offset(0, 0, HORIZONTAL_OFFSET), generator).isEmpty();
        }
        if (!this.hasPlacedWest && isSectionLoaded(world, pos.offset(-HORIZONTAL_OFFSET, 0, 0))) {
            this.hasPlacedWest = tryPlaceSegment(world, pos.offset(-HORIZONTAL_OFFSET, 0, 0), generator).isEmpty();
        }

        if (this.hasPlacedAll()) {
            world.setBlockAndUpdate(pos, ModBlocks.FINISHED_BACKROOMS_GENERATOR.defaultBlockState());
        }
    }

    @Contract(pure = true)
    private boolean hasPlacedAll() {
        return this.hasPlacedNorth && this.hasPlacedEast && this.hasPlacedSouth && this.hasPlacedWest;
    }
}
