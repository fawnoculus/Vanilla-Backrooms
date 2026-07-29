package net.fawnoculus.vanilla_backrooms.misc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import org.jetbrains.annotations.NotNull;

public interface CustomDataHolder {
    String KEY = "vanilla_backrooms.custom_data";

    static CustomDataHolder from(Entity entity) {
        return (CustomDataHolder) entity;
    }

    @NotNull CompoundTag VanillaBackrooms$getCustomData();

    void VanillaBackrooms$setCustomData(CompoundTag customData);
}
