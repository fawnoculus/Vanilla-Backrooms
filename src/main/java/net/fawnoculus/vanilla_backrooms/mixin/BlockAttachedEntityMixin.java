package net.fawnoculus.vanilla_backrooms.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fawnoculus.vanilla_backrooms.util.MixinUtil;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockAttachedEntity.class)
public class BlockAttachedEntityMixin {
    @WrapOperation(method = "readAdditionalSaveData", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;)V"))
    protected void suppressError(Logger instance, String s, Object o, Operation<Void> original) {
        if (!MixinUtil.suppressBlockAttachedEntityError()) {
            original.call(instance, s, o);
        }
    }
}
