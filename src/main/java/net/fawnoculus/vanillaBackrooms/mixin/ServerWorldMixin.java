package net.fawnoculus.vanillaBackrooms.mixin;

import net.fawnoculus.vanillaBackrooms.misc.events.EntityDimensionChangedEvent;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
	@Inject(at = @At("HEAD"), method = "onDimensionChanged")
	private void entityDimensionChanged(Entity entity, CallbackInfo ci) {
		EntityDimensionChangedEvent.EVENT.invoker().onDimensionChanged(entity, (ServerWorld) (Object) this);
	}
}
