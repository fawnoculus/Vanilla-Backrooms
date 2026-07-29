package net.fawnoculus.vanilla_backrooms.misc.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public interface PlayerRespawnEvent {
    Event<PlayerRespawnEvent> EVENT = EventFactory.createArrayBacked(PlayerRespawnEvent.class, callbacks ->
      (player, alive, removalReason) -> {
          for (PlayerRespawnEvent callback : callbacks) {
              callback.onRespawn(player, alive, removalReason);
          }
      });

    void onRespawn(ServerPlayer player, boolean alive, Entity.RemovalReason removalReason);
}
