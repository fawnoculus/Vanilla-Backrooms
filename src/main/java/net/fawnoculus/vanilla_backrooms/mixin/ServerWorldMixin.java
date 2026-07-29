package net.fawnoculus.vanilla_backrooms.mixin;

import net.fawnoculus.vanilla_backrooms.misc.events.EntityDimensionChangedEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerWorldMixin {
    @Inject(at = @At("HEAD"), method = "onDimensionChanged")
    private void entityDimensionChanged(Entity entity, CallbackInfo ci) {
        EntityDimensionChangedEvent.EVENT.invoker().onDimensionChanged(entity, (ServerLevel) (Object) this);
    }
}
