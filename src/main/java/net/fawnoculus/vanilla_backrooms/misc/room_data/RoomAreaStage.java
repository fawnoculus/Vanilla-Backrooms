package net.fawnoculus.vanilla_backrooms.misc.room_data;

public record RoomAreaStage(boolean northConnections, boolean eastConnections, boolean southConnections,
                            boolean westConnections) {
    public static final RoomAreaStage COMPLETE = new RoomAreaStage(true, true, true, true);
}
