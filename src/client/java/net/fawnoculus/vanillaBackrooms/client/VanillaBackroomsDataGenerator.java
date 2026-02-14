package net.fawnoculus.vanillaBackrooms.client;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fawnoculus.vanillaBackrooms.client.datagen.loot.BlockLootTableProvider;
import net.fawnoculus.vanillaBackrooms.client.datagen.loot.ChestLootTableProvider;
import net.fawnoculus.vanillaBackrooms.client.datagen.tag.BlockTagProvider;

public class VanillaBackroomsDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(BlockTagProvider::new);

		pack.addProvider(BlockLootTableProvider::new);
		pack.addProvider(ChestLootTableProvider::new);
	}
}
