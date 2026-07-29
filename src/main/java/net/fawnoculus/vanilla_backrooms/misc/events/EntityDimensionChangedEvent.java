package net.fawnoculus.vanilla_backrooms.misc.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface EntityDimensionChangedEvent {
    Event<EntityDimensionChangedEvent> EVENT = EventFactory.createArrayBacked(EntityDimensionChangedEvent.class, callbacks ->
      (entity, world) -> {
          for (EntityDimensionChangedEvent callback : callbacks) {
              callback.onDimensionChanged(entity, world);
          }
      });

    void onDimensionChanged(@NotNull Entity entity, @NotNull ServerLevel world);
}
