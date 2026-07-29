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
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record RingBackroomsGenerator(@Unmodifiable List<Entry> structures) implements BackroomsGenerator {
    @Contract("_ -> new")
    public static @NotNull Builder builder(boolean defaultRotate) {
        return new Builder(defaultRotate);
    }

    @Override
    public void placeBackroomsSegment(@NotNull ServerLevel world, @NotNull BlockPos pos) throws RuntimeException {
        Entry entry = this.entryFromPos(pos);
        Holder.Reference<StructureTemplatePool> structurePool = world.registryAccess().lookupOrThrow(Registries.TEMPLATE_POOL).getOrThrow(entry.structurePoolKey);

        if (entry.rotate) {
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

    public Entry entryFromPos(@NotNull BlockPos pos) {
        double squareDistanceFromCenter = Math.abs((double) pos.getX() * pos.getX() + pos.getZ() * pos.getZ());

        for (Entry entry : structures) {
            if (squareDistanceFromCenter >= entry.squareDistance) {
                return entry;
            }
        }

        return structures.getLast();
    }

    public static class Builder {
        private final boolean defaultRotate;
        private final List<Entry> structures = new ArrayList<>();

        public Builder(boolean defaultRotate) {
            this.defaultRotate = defaultRotate;
        }

        public Builder add(int distanceFromCenter, String name) {
            return add(this.defaultRotate, distanceFromCenter, name);
        }

        public Builder addRotate(int distanceFromCenter, String name) {
            return add(true, distanceFromCenter, name);
        }

        public Builder addNoRotate(int distanceFromCenter, String name) {
            return add(false, distanceFromCenter, name);
        }

        public Builder add(boolean rotate, int distanceFromCenter, String name) {
            int squareDistance = distanceFromCenter * distanceFromCenter;

            this.structures.add(
              new Entry(rotate, squareDistance, ResourceKey.create(Registries.TEMPLATE_POOL, VanillaBackrooms.id(name)))
            );

            return this;
        }

        public RingBackroomsGenerator build() {
            if (this.structures.isEmpty()) {
                throw new IllegalStateException("Can not build Dimension data without any structure pools");
            }
            structures.sort(Comparator.comparingInt(Entry::squareDistance));
            return new RingBackroomsGenerator(List.copyOf(structures.reversed()));
        }
    }

    public record Entry(boolean rotate, int squareDistance, ResourceKey<StructureTemplatePool> structurePoolKey) {
    }
}
