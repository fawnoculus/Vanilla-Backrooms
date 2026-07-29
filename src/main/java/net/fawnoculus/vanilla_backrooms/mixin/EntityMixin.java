package net.fawnoculus.vanilla_backrooms.mixin;

import net.fawnoculus.vanilla_backrooms.misc.CustomDataHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import org.jetbrains.annotations.NotNull;

@Mixin(Entity.class)
public class EntityMixin implements CustomDataHolder {
    @Unique
    CompoundTag VanillaBackrooms$customData = new CompoundTag();

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;readCustomData(Lnet/minecraft/storage/ReadView;)V"), method = "readData")
    protected void readCustomData(ValueInput view, CallbackInfo ci) {
        Tag data = view.read(CustomDataHolder.KEY, CompoundTag.CODEC).orElse(new CompoundTag());
        if (data instanceof CompoundTag nbtCompound) {
            VanillaBackrooms$customData = nbtCompound;
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;writeCustomData(Lnet/minecraft/storage/WriteView;)V"), method = "writeData")
    protected void writeCustomData(ValueOutput view, CallbackInfo ci) {
        view.store(CustomDataHolder.KEY, CompoundTag.CODEC, VanillaBackrooms$customData);
    }

    @Override
    public @NotNull CompoundTag VanillaBackrooms$getCustomData() {
        return VanillaBackrooms$customData;
    }

    @Override
    public void VanillaBackrooms$setCustomData(CompoundTag customData) {
        VanillaBackrooms$customData = customData;
    }
}
