package net.fawnoculus.vanillaBackrooms.misc.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

@FunctionalInterface
public interface EntityDimensionChangedEvent {
	Event<EntityDimensionChangedEvent> EVENT = EventFactory.createArrayBacked(EntityDimensionChangedEvent.class, callbacks ->
	  (entity, world) -> {
		  for (EntityDimensionChangedEvent callback : callbacks) {
			  callback.onDimensionChanged(entity, world);
		  }
	  });

	void onDimensionChanged(Entity entity, ServerWorld world);
}
