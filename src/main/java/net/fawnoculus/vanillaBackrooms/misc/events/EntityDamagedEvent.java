package net.fawnoculus.vanillaBackrooms.misc.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;

@FunctionalInterface
public interface EntityDamagedEvent {
	Event<EntityDamagedEvent> EVENT = EventFactory.createArrayBacked(EntityDamagedEvent.class, callbacks ->
	  (entity, world, source, amount) -> {
		  for (EntityDamagedEvent callback : callbacks) {
			  callback.onDamaged(entity, world, source, amount);
		  }
	  });

	void onDamaged(LivingEntity entity, ServerWorld world, DamageSource source, float amount);
}
