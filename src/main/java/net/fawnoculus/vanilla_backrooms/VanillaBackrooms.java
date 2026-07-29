package net.fawnoculus.vanilla_backrooms;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fawnoculus.vanilla_backrooms.blocks.ModBlockEntities;
import net.fawnoculus.vanilla_backrooms.blocks.ModBlocks;
import net.fawnoculus.vanilla_backrooms.commands.ModCommands;
import net.fawnoculus.vanilla_backrooms.items.ModBlockItems;
import net.fawnoculus.vanilla_backrooms.items.ModItemGroups;
import net.fawnoculus.vanilla_backrooms.items.ModItems;
import net.fawnoculus.vanilla_backrooms.levels.BackroomsLevel;
import net.fawnoculus.vanilla_backrooms.misc.ModEvents;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class VanillaBackrooms implements ModInitializer {
    public static final String MOD_ID = "vanilla_backrooms";
    public static final ModContainer CONTAINER = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow();
    public static final String NAME = CONTAINER.getMetadata().getName();
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    @Contract("_ -> new")
    public static @NotNull Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }

    @Override
    public void onInitialize() {
        PolymerResourcePackUtils.addModAssets(MOD_ID);

        VanillaBackroomsConfig.initialize();
        BackroomsLevel.initialize();
        ModCommands.initialize();

        ModBlocks.initialize();
        ModBlockEntities.initialize();

        ModItems.initialize();
        ModBlockItems.initialize();
        ModItemGroups.initialize();

        ModEvents.initialize();
    }
}
