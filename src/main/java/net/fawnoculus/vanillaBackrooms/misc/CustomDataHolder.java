package net.fawnoculus.vanillaBackrooms.misc;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public interface CustomDataHolder {
	String KEY = "vanilla_backrooms.custom_data";

	static CustomDataHolder from(Entity entity) {
		return (CustomDataHolder) entity;
	}

	@NotNull NbtCompound VanillaBackrooms$getCustomData();

	void VanillaBackrooms$setCustomData(NbtCompound customData);
}
