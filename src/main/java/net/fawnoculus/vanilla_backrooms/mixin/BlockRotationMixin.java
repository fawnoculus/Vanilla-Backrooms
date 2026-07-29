package net.fawnoculus.vanilla_backrooms.mixin;

import net.fawnoculus.vanilla_backrooms.util.MixinUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Rotation.class)
public class BlockRotationMixin {

    @Inject(at = @At("HEAD"), method = "random", cancellable = true)
    private static void manipulateRandom(RandomSource random, CallbackInfoReturnable<Rotation> cir) {
        Rotation rotation = MixinUtil.getRandomBlockRotationOverride();

        if (rotation != null) {
            MixinUtil.setRandomBlockRotationOverride(null);
            cir.setReturnValue(rotation);
        }
    }
}
