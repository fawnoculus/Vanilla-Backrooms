package net.fawnoculus.vanilla_backrooms.misc;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fawnoculus.vanilla_backrooms.VanillaBackrooms;
import net.minecraft.resources.Identifier;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class ModDataAttachments {
    public static final AttachmentType<Boolean> IS_IN_BACKROOMS = AttachmentRegistry.create(id("is_in_backrooms"), builder -> {
        builder.initializer(() -> false);
        builder.copyOnDeath();
        builder.persistent(Codec.BOOL);
    });


    private static Identifier id(String name) {
        return VanillaBackrooms.id(name);
    }
}
