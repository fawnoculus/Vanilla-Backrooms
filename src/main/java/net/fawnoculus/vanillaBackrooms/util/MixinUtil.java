package net.fawnoculus.vanillaBackrooms.util;

import net.minecraft.util.BlockRotation;
import org.jetbrains.annotations.Nullable;

public class MixinUtil {
    private static @Nullable BlockRotation randomBlockRotationOverride = null;
    private static boolean suppressBlockAttachedEntityError = false;

    public static @Nullable BlockRotation getRandomBlockRotationOverride() {
        return MixinUtil.randomBlockRotationOverride;
    }

    public static void setRandomBlockRotationOverride(@Nullable BlockRotation override) {
        MixinUtil.randomBlockRotationOverride = override;
    }

    public static boolean suppressBlockAttachedEntityError() {
        return suppressBlockAttachedEntityError;
    }

    public static void setSuppressBlockAttachedEntityError(boolean suppressBlockAttachedEntityError) {
        MixinUtil.suppressBlockAttachedEntityError = suppressBlockAttachedEntityError;
    }
}
