package net.fawnoculus.vanillaBackrooms.misc;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public interface CustomDataHolder {
  String KEY = "ntm.custom_data";

  @NotNull NbtCompound CA$getCustomData();

  void CA$setCustomData(NbtCompound customData);

  static CustomDataHolder from(LivingEntity entity) {
    return (CustomDataHolder) entity;
  }

  static CustomDataHolder from(PlayerEntity entity) {
    return (CustomDataHolder) entity;
  }
}
