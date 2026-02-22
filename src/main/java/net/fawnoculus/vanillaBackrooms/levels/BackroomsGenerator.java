package net.fawnoculus.vanillaBackrooms.levels;

import net.fawnoculus.vanillaBackrooms.VanillaBackrooms;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public interface BackroomsGenerator {
    Identifier STRUCTURE_START = VanillaBackrooms.id("backrooms_start");
    int HORIZONTAL_OFFSET = 48;
    BackroomsGenerator NO_GENERATOR = (world, pos) -> {
    };

    void placeBackroomsSegment(@NotNull ServerWorld world, @NotNull BlockPos pos) throws RuntimeException;
}
