package net.fawnoculus.vanillaBackrooms.misc.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;

public interface PlayerRespawnEvent {
    Event<PlayerRespawnEvent> EVENT = EventFactory.createArrayBacked(PlayerRespawnEvent.class, callbacks ->
      (player, alive, removalReason) -> {
          for (PlayerRespawnEvent callback : callbacks) {
              callback.onRespawn(player, alive, removalReason);
          }
      });

    void onRespawn(ServerPlayerEntity player, boolean alive, Entity.RemovalReason removalReason);
}
