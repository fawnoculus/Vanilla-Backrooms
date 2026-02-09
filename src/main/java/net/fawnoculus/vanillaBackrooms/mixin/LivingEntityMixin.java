package net.fawnoculus.vanillaBackrooms.mixin;

import net.fawnoculus.craft_attack.misc.CustomDataHolder;
import net.fawnoculus.craft_attack.misc.events.EntityDamagedEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements CustomDataHolder {
  @Unique
  NbtCompound CA$customData = new NbtCompound();

  @Inject(at = @At("HEAD"), method = "readCustomData")
  protected void readCustomData(ReadView view, CallbackInfo ci) {
    NbtElement data = view.read(CustomDataHolder.KEY, NbtCompound.CODEC).orElse(new NbtCompound());
    if (data instanceof NbtCompound nbtCompound) {
      CA$customData = nbtCompound;
    }
  }

  @Inject(at = @At("HEAD"), method = "writeCustomData")
  protected void writeCustomData(WriteView view, CallbackInfo ci) {
    view.put(CustomDataHolder.KEY, NbtCompound.CODEC, CA$customData);
  }

  @Inject(at = @At("HEAD"), method = "damage")
  protected void onDamage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
    EntityDamagedEvent.EVENT.invoker().onDamaged((LivingEntity) (Object) this ,world, source, amount);
  }

  @Override
  public @NotNull NbtCompound CA$getCustomData() {
    return CA$customData;
  }

  @Override
  public void CA$setCustomData(NbtCompound customData) {
    CA$customData = customData;
  }
}
