package net.fawnoculus.vanillaBackrooms.client.datagen.recipes;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fawnoculus.vanillaBackrooms.items.ModItems;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SmeltingRecipeProvider extends FabricRecipeProvider {
	public SmeltingRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
		return new RecipeGenerator(registryLookup, exporter) {
			@Override
			public void generate() {
				luckyOMilkHeating(ModItems.COLD_PLAIN_LUCKY_O_MILK, ModItems.WARM_PLAIN_LUCKY_O_MILK, "plain_milk");
				luckyOMilkHeating(ModItems.COLD_STRAWBERRY_LUCKY_O_MILK, ModItems.WARM_STRAWBERRY_LUCKY_O_MILK, "strawberry_milk");
				luckyOMilkHeating(ModItems.COLD_CHOCO_LUCKY_O_MILK, ModItems.WARM_CHOCO_LUCKY_O_MILK, "choco_milk");
				luckyOMilkHeating(ModItems.COLD_MATCHA_LUCKY_O_MILK, ModItems.WARM_MATCHA_LUCKY_O_MILK, "matcha_milk");
				luckyOMilkHeating(ModItems.COLD_BANANA_LUCKY_O_MILK, ModItems.WARM_BANANA_LUCKY_O_MILK, "banana_milk");
				luckyOMilkHeating(ModItems.COLD_LUCK_LUCKY_O_MILK, ModItems.WARM_LUCK_LUCKY_O_MILK, "luck_milk");
			}

			private void luckyOMilkHeating(Item coldMilk, Item warmMilk, String group) {
				this.offerSmelting(
				  List.of(coldMilk),
				  RecipeCategory.FOOD,
				  warmMilk,
				  0.2f,
				  200,
				  group
				);
			}
		};
	}

	@Override
	public String getName() {
		return "Smelting Recipe";
	}
}
