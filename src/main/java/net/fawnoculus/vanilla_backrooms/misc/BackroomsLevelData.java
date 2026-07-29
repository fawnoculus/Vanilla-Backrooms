package net.fawnoculus.vanilla_backrooms.misc;

import net.fawnoculus.vanilla_backrooms.misc.room_data.RoomAreaPos;
import net.fawnoculus.vanilla_backrooms.misc.room_data.RoomAreaStage;

import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BackroomsLevelData {
    private final Set<RoomAreaPos> fullyGenerated = new HashSet<>();
    private final Map<RoomAreaPos, RoomAreaStage> stageMap = new HashMap<>();

    public @Nullable RoomAreaStage getStage(RoomAreaPos pos) {
        if (fullyGenerated.contains(pos)) {
            return RoomAreaStage.COMPLETE;
        }
    }
}
