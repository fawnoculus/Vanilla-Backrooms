package net.fawnoculus.vanillaBackrooms;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VanillaBackroomsConfig implements ModInitializer {
    public static final String MOD_ID = "vanilla_backrooms";
    public static final ModContainer CONTAINER = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow();
    public static final String NAME = CONTAINER.getMetadata().getName();
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    @Override
    public void onInitialize() {
        PolymerResourcePackUtils.addModAssets(MOD_ID);
    }

    @Contract("_ -> new")
    public static @NotNull Identifier id(String name) {
        return Identifier.of(MOD_ID, name);
    }
}
