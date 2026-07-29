package net.fawnoculus.vanilla_backrooms.client.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fawnoculus.vanilla_backrooms.items.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;

public class ModelProvider extends FabricModelProvider {
    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.declareCustomModelItem(ModItems.ALMOND_WATTER);

        itemModelGenerator.declareCustomModelItem(ModItems.COLD_PLAIN_LUCKY_O_MILK);
        itemModelGenerator.declareCustomModelItem(ModItems.COLD_STRAWBERRY_LUCKY_O_MILK);
        itemModelGenerator.declareCustomModelItem(ModItems.COLD_CHOCO_LUCKY_O_MILK);
        itemModelGenerator.declareCustomModelItem(ModItems.COLD_MATCHA_LUCKY_O_MILK);
        itemModelGenerator.declareCustomModelItem(ModItems.COLD_BANANA_LUCKY_O_MILK);
        itemModelGenerator.declareCustomModelItem(ModItems.COLD_LUCK_LUCKY_O_MILK);

        itemModelGenerator.declareCustomModelItem(ModItems.WARM_PLAIN_LUCKY_O_MILK);
        itemModelGenerator.declareCustomModelItem(ModItems.WARM_STRAWBERRY_LUCKY_O_MILK);
        itemModelGenerator.declareCustomModelItem(ModItems.WARM_CHOCO_LUCKY_O_MILK);
        itemModelGenerator.declareCustomModelItem(ModItems.WARM_MATCHA_LUCKY_O_MILK);
        itemModelGenerator.declareCustomModelItem(ModItems.WARM_BANANA_LUCKY_O_MILK);
        itemModelGenerator.declareCustomModelItem(ModItems.WARM_LUCK_LUCKY_O_MILK);

        itemModelGenerator.declareCustomModelItem(ModItems.BERRY_MATCHA_BLAST);

        itemModelGenerator.declareCustomModelItem(ModItems.LEVEL_KEY);
    }
}
