package net.fawnoculus.vanillaBackrooms.levels.generators;

import net.fawnoculus.vanillaBackrooms.VanillaBackrooms;
import net.fawnoculus.vanillaBackrooms.levels.BackroomsGenerator;
import net.fawnoculus.vanillaBackrooms.util.MixinUtil;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.ChunkRandom;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record SimpleBackroomsGenerator(boolean rotate,
                                       RegistryKey<StructurePool> structurePoolKey) implements BackroomsGenerator {
    @Contract("_ -> new")
    public static @NotNull SimpleBackroomsGenerator of(String name) {
        return of(true, name);
    }

    @Contract("_, _ -> new")
    public static @NotNull SimpleBackroomsGenerator of(boolean rotate, String name) {
        return new SimpleBackroomsGenerator(rotate, RegistryKey.of(RegistryKeys.TEMPLATE_POOL, VanillaBackrooms.id(name)));
    }

    @Override
    public void placeBackroomsSegment(@NotNull ServerWorld world, @NotNull BlockPos pos) throws RuntimeException {
        RegistryEntry.Reference<StructurePool> structurePool = world.getRegistryManager().getOrThrow(RegistryKeys.TEMPLATE_POOL).getOrThrow(this.structurePoolKey);

        if (this.rotate) {
            ChunkRandom chunkRandom = new ChunkRandom(new CheckedRandom(0L));
            chunkRandom.setCarverSeed(
              world.getSeed(),
              ChunkSectionPos.getSectionCoord(pos.getX()),
              ChunkSectionPos.getSectionCoord(pos.getZ())
            );
            placeSegmentWithRotation(world, pos, structurePool, BlockRotation.random(chunkRandom));
            return;
        }

        placeSegmentWithRotation(world, pos, structurePool, BlockRotation.NONE);
    }

    private void placeSegmentWithRotation(
      @NotNull ServerWorld world,
      @NotNull BlockPos pos,
      @NotNull RegistryEntry.Reference<StructurePool> structurePool,
      @NotNull BlockRotation rotation
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
}
