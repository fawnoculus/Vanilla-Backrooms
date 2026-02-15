package net.fawnoculus.vanillaBackrooms.client.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fawnoculus.vanillaBackrooms.items.ModItems;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;

public class ModelProvider extends FabricModelProvider {
	public ModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		itemModelGenerator.register(ModItems.ALMOND_WATTER);

		itemModelGenerator.register(ModItems.COLD_PLAIN_LUCKY_O_MILK);
		itemModelGenerator.register(ModItems.COLD_STRAWBERRY_LUCKY_O_MILK);
		itemModelGenerator.register(ModItems.COLD_CHOCO_LUCKY_O_MILK);
		itemModelGenerator.register(ModItems.COLD_MATCHA_LUCKY_O_MILK);
		itemModelGenerator.register(ModItems.COLD_BANANA_LUCKY_O_MILK);
		itemModelGenerator.register(ModItems.COLD_LUCK_LUCKY_O_MILK);

		itemModelGenerator.register(ModItems.WARM_PLAIN_LUCKY_O_MILK);
		itemModelGenerator.register(ModItems.WARM_STRAWBERRY_LUCKY_O_MILK);
		itemModelGenerator.register(ModItems.WARM_CHOCO_LUCKY_O_MILK);
		itemModelGenerator.register(ModItems.WARM_MATCHA_LUCKY_O_MILK);
		itemModelGenerator.register(ModItems.WARM_BANANA_LUCKY_O_MILK);
		itemModelGenerator.register(ModItems.WARM_LUCK_LUCKY_O_MILK);

		itemModelGenerator.register(ModItems.BERRY_MATCHA_BLAST);
	}
}
