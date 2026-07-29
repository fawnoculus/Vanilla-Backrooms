package net.fawnoculus.vanilla_backrooms.misc.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface EntityDamagedEvent {
    Event<EntityDamagedEvent> EVENT = EventFactory.createArrayBacked(EntityDamagedEvent.class, callbacks ->
      (entity, world, source, amount) -> {
          for (EntityDamagedEvent callback : callbacks) {
              callback.onDamaged(entity, world, source, amount);
          }
      });

    void onDamaged(LivingEntity entity, @NotNull ServerLevel world, @NotNull DamageSource source, float amount);
}
