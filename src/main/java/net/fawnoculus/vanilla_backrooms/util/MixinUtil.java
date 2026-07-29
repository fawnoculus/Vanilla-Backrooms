package net.fawnoculus.vanilla_backrooms.util;

import net.minecraft.world.level.block.Rotation;

import org.jetbrains.annotations.Nullable;

public class MixinUtil {
    private static @Nullable Rotation randomBlockRotationOverride = null;
    private static boolean suppressBlockAttachedEntityError = false;

    public static @Nullable Rotation getRandomBlockRotationOverride() {
        return MixinUtil.randomBlockRotationOverride;
    }

    public static void setRandomBlockRotationOverride(@Nullable Rotation override) {
        MixinUtil.randomBlockRotationOverride = override;
    }

    public static boolean suppressBlockAttachedEntityError() {
        return suppressBlockAttachedEntityError;
    }

    public static void setSuppressBlockAttachedEntityError(boolean suppressBlockAttachedEntityError) {
        MixinUtil.suppressBlockAttachedEntityError = suppressBlockAttachedEntityError;
    }
}
