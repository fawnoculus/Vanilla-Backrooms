package net.fawnoculus.vanilla_backrooms.misc.room_data;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Util;
import net.minecraft.world.level.ChunkPos;

import java.util.stream.IntStream;

public record RoomAreaPos(int x, int z) {
    public static final Codec<RoomAreaPos> CODEC = Codec.INT_STREAM
      .comapFlatMap(input -> Util.fixedSize(input, 2).map(ints -> new RoomAreaPos(ints[0], ints[1])), pos -> IntStream.of(pos.x(), pos.z()))
      .stable();

    public RoomAreaPos(ChunkPos chunkPos) {
        this(chunkPos.x() / 32, chunkPos.z() / 32);
    }

    public RoomAreaPos(BlockPos blockPos) {
        this(blockPos.getX() / 512, blockPos.getZ() / 512);
    }
}
