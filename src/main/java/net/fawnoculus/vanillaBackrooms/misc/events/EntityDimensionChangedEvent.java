package net.fawnoculus.vanillaBackrooms.misc.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;

@FunctionalInterface
public interface EntityDimensionChanged {
  Event<EntityDimensionChanged> EVENT = EventFactory.createArrayBacked(EntityDimensionChanged.class, callbacks ->
    (entity, world, source, amount) -> {
    for (EntityDimensionChanged callback : callbacks) {
      callback.onDamaged(entity, world, source, amount);
    }
  });

  void onDamaged(LivingEntity entity, ServerWorld world, DamageSource source, float amount);
}
