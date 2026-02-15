package net.fawnoculus.vanillaBackrooms.client.datagen.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fawnoculus.vanillaBackrooms.items.ModItems;
import net.fawnoculus.vanillaBackrooms.misc.tags.ModItemTags;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends FabricTagProvider.ItemTagProvider {
	public ItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		valueLookupBuilder(ModItemTags.COLD)
		  .add(Items.ICE)
		  .add(Items.PACKED_ICE)
		  .add(Items.BLUE_ICE);

		valueLookupBuilder(ModItemTags.PLAIN_LUCKY_O_MILK)
		  .add(ModItems.COLD_PLAIN_LUCKY_O_MILK)
		  .add(ModItems.WARM_PLAIN_LUCKY_O_MILK);

		valueLookupBuilder(ModItemTags.STRAWBERRY_LUCKY_O_MILK)
		  .add(ModItems.COLD_STRAWBERRY_LUCKY_O_MILK)
		  .add(ModItems.WARM_STRAWBERRY_LUCKY_O_MILK);

		valueLookupBuilder(ModItemTags.CHOCO_LUCKY_O_MILK)
		  .add(ModItems.COLD_CHOCO_LUCKY_O_MILK)
		  .add(ModItems.WARM_CHOCO_LUCKY_O_MILK);

		valueLookupBuilder(ModItemTags.MATCHA_LUCKY_O_MILK)
		  .add(ModItems.COLD_MATCHA_LUCKY_O_MILK)
		  .add(ModItems.WARM_MATCHA_LUCKY_O_MILK);

		valueLookupBuilder(ModItemTags.BANANA_LUCKY_O_MILK)
		  .add(ModItems.COLD_BANANA_LUCKY_O_MILK)
		  .add(ModItems.WARM_BANANA_LUCKY_O_MILK);

		valueLookupBuilder(ModItemTags.LUCK_LUCKY_O_MILK)
		  .add(ModItems.COLD_LUCK_LUCKY_O_MILK)
		  .add(ModItems.WARM_LUCK_LUCKY_O_MILK);
	}
}
