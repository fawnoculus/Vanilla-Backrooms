package net.fawnoculus.vanilla_backrooms.mixin;

import net.fawnoculus.vanilla_backrooms.misc.events.EntityDamagedEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(at = @At("HEAD"), method = "damage")
    protected void onDamage(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        EntityDamagedEvent.EVENT.invoker().onDamaged((LivingEntity) (Object) this, world, source, amount);
    }
}
