package net.fawnoculus.vanillaBackrooms.levels;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public interface BackroomsGenerator {
	Identifier STRUCTURE_START = Identifier.of("craft_attack", "backrooms_start");
	int HORIZONTAL_OFFSET = 48;
	BackroomsGenerator NO_GENERATOR = (world, pos) -> {
	};

	void placeBackroomsSegment(ServerWorld world, BlockPos pos);
}
