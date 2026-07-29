package net.fawnoculus.vanilla_backrooms.levels.generators;

import net.fawnoculus.vanilla_backrooms.VanillaBackrooms;
import net.fawnoculus.vanilla_backrooms.levels.BackroomsGenerator;
import net.fawnoculus.vanilla_backrooms.util.MixinUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record SimpleBackroomsGenerator(boolean rotate,
                                       ResourceKey<StructureTemplatePool> structurePoolKey) implements BackroomsGenerator {
    @Contract("_ -> new")
    public static @NotNull SimpleBackroomsGenerator of(String name) {
        return of(true, name);
    }

    @Contract("_, _ -> new")
    public static @NotNull SimpleBackroomsGenerator of(boolean rotate, String name) {
        return new SimpleBackroomsGenerator(rotate, ResourceKey.create(Registries.TEMPLATE_POOL, VanillaBackrooms.id(name)));
    }

    @Override
    public void placeBackroomsSegment(@NotNull ServerLevel world, @NotNull BlockPos pos) throws RuntimeException {
        Holder.Reference<StructureTemplatePool> structurePool = world.registryAccess().lookupOrThrow(Registries.TEMPLATE_POOL).getOrThrow(this.structurePoolKey);

        if (this.rotate) {
            WorldgenRandom chunkRandom = new WorldgenRandom(new LegacyRandomSource(0L));
            chunkRandom.setLargeFeatureSeed(
              world.getSeed(),
              SectionPos.blockToSectionCoord(pos.getX()),
              SectionPos.blockToSectionCoord(pos.getZ())
            );
            placeSegmentWithRotation(world, pos, structurePool, Rotation.getRandom(chunkRandom));
            return;
        }

        placeSegmentWithRotation(world, pos, structurePool, Rotation.NONE);
    }

    private void placeSegmentWithRotation(
      @NotNull ServerLevel world,
      @NotNull BlockPos pos,
      @NotNull Holder.Reference<StructureTemplatePool> structurePool,
      @NotNull Rotation rotation
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

        BlockPos placementPos = pos.offset(xOffset, 1, zOffset);

        MixinUtil.setRandomBlockRotationOverride(rotation);

        boolean success = JigsawPlacement.generateJigsaw(world, structurePool, STRUCTURE_START, 20, placementPos, false);

        if (!success) {
            VanillaBackrooms.LOGGER.warn("Failed to generate backrooms segment");
        }
    }
}
