package net.fawnoculus.vanilla_backrooms.levels.generators;

import net.fawnoculus.vanilla_backrooms.levels.BackroomsGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public record MultiBackroomsGenerator(@Unmodifiable @NotNull List<Entry> entries,
                                      @Range(from = 0, to = Integer.MAX_VALUE) int sectionSize) implements BackroomsGenerator {
    public static final BiPredicate<ServerLevel, BlockPos> ALWAYS = (serverWorld, pos) -> true;
    private static final BiPredicate<ServerLevel, BlockPos> NOT_CENTER = (serverWorld, pos) -> !pos.equals(BlockPos.ZERO);

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Builder builder(int sectionSize) {
        return new Builder(sectionSize);
    }

    @Override
    public void placeBackroomsSegment(@NotNull ServerLevel world, @NotNull BlockPos pos) throws RuntimeException {
        getEntry(world, pos).generator().placeBackroomsSegment(world, pos);
    }

    public Entry getEntry(@NotNull ServerLevel world, @NotNull BlockPos pos) {
        int totalWeight = 0;
        List<Entry> validEntries = new ArrayList<>(entries.size());

        for (Entry entry : entries) {
            if (entry.requirement.test(world, pos)) {
                totalWeight += entry.weight;
                validEntries.add(entry);
            }
        }

        WorldgenRandom sectionRandom = new WorldgenRandom(new LegacyRandomSource(0L));
        sectionRandom.setLargeFeatureSeed(
          world.getSeed(),
          SectionPos.blockToSectionCoord(pos.getX()) / sectionSize,
          SectionPos.blockToSectionCoord(pos.getZ()) / sectionSize
        );

        int chosenWeight = sectionRandom.nextInt(totalWeight);

        for (Entry entry : validEntries) {
            chosenWeight -= entry.weight;
            if (chosenWeight <= 0) {
                return entry;
            }
        }

        return entries.getFirst();
    }

    public static class Builder {
        private final @Range(from = 0, to = Integer.MAX_VALUE) int sectionSize;
        private final List<Entry> entries = new ArrayList<>();

        public Builder(@Range(from = 0, to = Integer.MAX_VALUE) int sectionSize) {
            this.sectionSize = sectionSize;
        }

        public Builder addNonCenter(@Range(from = 0, to = Integer.MAX_VALUE) int weight, @NotNull BackroomsGenerator generator) {
            return add(weight, NOT_CENTER, generator);
        }

        public Builder addAlways(@Range(from = 0, to = Integer.MAX_VALUE) int weight, @NotNull BackroomsGenerator generator) {
            return add(weight, ALWAYS, generator);
        }

        public Builder add(
          @Range(from = 0, to = Integer.MAX_VALUE) int weight,
          @NotNull BiPredicate<ServerLevel, BlockPos> requirement,
          @NotNull BackroomsGenerator generator
        ) {
            entries.add(new Entry(weight, requirement, generator));
            return this;
        }

        public MultiBackroomsGenerator build() {
            if (entries.isEmpty()) {
                throw new IllegalStateException("Tried to build MultiBackroomsGenerator with no entries");
            }

            return new MultiBackroomsGenerator(List.copyOf(entries), sectionSize);
        }
    }

    public record Entry(@Range(from = 0, to = Integer.MAX_VALUE) int weight,
                        @NotNull BiPredicate<ServerLevel, BlockPos> requirement,
                        @NotNull BackroomsGenerator generator) {
    }
}
