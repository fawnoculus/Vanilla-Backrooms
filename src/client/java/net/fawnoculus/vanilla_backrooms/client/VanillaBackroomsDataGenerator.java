package net.fawnoculus.vanilla_backrooms.client;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fawnoculus.vanilla_backrooms.client.datagen.ModelProvider;
import net.fawnoculus.vanilla_backrooms.client.datagen.loot.BlockLootTableProvider;
import net.fawnoculus.vanilla_backrooms.client.datagen.loot.ChestLootTableProvider;
import net.fawnoculus.vanilla_backrooms.client.datagen.recipes.CraftingRecipeProvider;
import net.fawnoculus.vanilla_backrooms.client.datagen.recipes.SmeltingRecipeProvider;
import net.fawnoculus.vanilla_backrooms.client.datagen.tag.BlockTagProvider;
import net.fawnoculus.vanilla_backrooms.client.datagen.tag.ItemTagProvider;

public class VanillaBackroomsDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ItemTagProvider::new);
        pack.addProvider(BlockTagProvider::new);

        pack.addProvider(CraftingRecipeProvider::new);
        pack.addProvider(SmeltingRecipeProvider::new);

        pack.addProvider(BlockLootTableProvider::new);
        pack.addProvider(ChestLootTableProvider::new);

        pack.addProvider(ModelProvider::new);
    }
}
