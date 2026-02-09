package net.fawnoculus.vanillaBackrooms.mixin;

import net.fawnoculus.vanillaBackrooms.misc.CustomDataHolder;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin implements CustomDataHolder {
	@Unique
	NbtCompound VanillaBackrooms$customData = new NbtCompound();

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;readCustomData(Lnet/minecraft/storage/ReadView;)V"), method = "readData")
	protected void readCustomData(ReadView view, CallbackInfo ci) {
		NbtElement data = view.read(CustomDataHolder.KEY, NbtCompound.CODEC).orElse(new NbtCompound());
		if (data instanceof NbtCompound nbtCompound) {
			VanillaBackrooms$customData = nbtCompound;
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;writeCustomData(Lnet/minecraft/storage/WriteView;)V"), method = "writeData")
	protected void writeCustomData(WriteView view, CallbackInfo ci) {
		view.put(CustomDataHolder.KEY, NbtCompound.CODEC, VanillaBackrooms$customData);
	}

	@Override
	public @NotNull NbtCompound VanillaBackrooms$getCustomData() {
		return VanillaBackrooms$customData;
	}

	@Override
	public void VanillaBackrooms$setCustomData(NbtCompound customData) {
		VanillaBackrooms$customData = customData;
	}
}
