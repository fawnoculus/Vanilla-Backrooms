package net.fawnoculus.vanillaBackrooms.misc;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public interface CustomDataHolder {
	String KEY = "ntm.custom_data";

	@NotNull NbtCompound VanillaBackrooms$getCustomData();

	void VanillaBackrooms$setCustomData(NbtCompound customData);

	static CustomDataHolder from(Entity entity) {
		return (CustomDataHolder) entity;
	}
}
