package net.fawnoculus.vanilla_backrooms.levels;

import net.fawnoculus.vanilla_backrooms.VanillaBackrooms;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;

import org.jetbrains.annotations.NotNull;

public interface BackroomsGenerator {
    Identifier STRUCTURE_START = VanillaBackrooms.id("backrooms_start");
    int HORIZONTAL_OFFSET = 48;
    BackroomsGenerator NO_GENERATOR = (world, pos) -> {
    };

    void placeBackroomsSegment(@NotNull ServerLevel world, @NotNull BlockPos pos) throws RuntimeException;
}
