package net.fawnoculus.vanillaBackrooms.mixin;

import net.fawnoculus.vanillaBackrooms.util.MixinUtil;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRotation.class)
public class BlockRotationMixin {

	@Inject(at = @At("HEAD"), method = "random", cancellable = true)
	private static void manipulateRandom(Random random, CallbackInfoReturnable<BlockRotation> cir) {
		BlockRotation rotation = MixinUtil.getRandomBlockRotationOverride();

		if (rotation != null) {
			MixinUtil.setRandomBlockRotationOverride(null);
			cir.setReturnValue(rotation);
		}
	}
}
