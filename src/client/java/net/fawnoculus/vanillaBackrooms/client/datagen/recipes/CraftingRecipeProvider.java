package net.fawnoculus.vanillaBackrooms.client.datagen.recipes;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fawnoculus.vanillaBackrooms.items.ModItems;
import net.fawnoculus.vanillaBackrooms.misc.tags.ModItemTags;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class CraftingRecipeProvider extends FabricRecipeProvider {
	public CraftingRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
		return new RecipeGenerator(registryLookup, exporter) {
			@Override
			public void generate() {
				luckyOMilkCooling(ModItems.WARM_PLAIN_LUCKY_O_MILK, ModItems.COLD_PLAIN_LUCKY_O_MILK, "plain_lucky_o_milk");
				luckyOMilkCooling(ModItems.WARM_STRAWBERRY_LUCKY_O_MILK, ModItems.COLD_STRAWBERRY_LUCKY_O_MILK, "strawberry_lucky_o_milk");
				luckyOMilkCooling(ModItems.WARM_CHOCO_LUCKY_O_MILK, ModItems.COLD_CHOCO_LUCKY_O_MILK, "choco_lucky_o_milk");
				luckyOMilkCooling(ModItems.WARM_MATCHA_LUCKY_O_MILK, ModItems.COLD_MATCHA_LUCKY_O_MILK, "matcha_lucky_o_milk");
				luckyOMilkCooling(ModItems.WARM_BANANA_LUCKY_O_MILK, ModItems.COLD_BANANA_LUCKY_O_MILK, "banana_lucky_o_milk");
				luckyOMilkCooling(ModItems.WARM_LUCK_LUCKY_O_MILK, ModItems.COLD_LUCK_LUCKY_O_MILK, "luck_lucky_o_milk");

				this.createShapeless(RecipeCategory.FOOD, ModItems.BERRY_MATCHA_BLAST)
				  .input(ModItemTags.STRAWBERRY_LUCKY_O_MILK)
				  .input(ModItemTags.MATCHA_LUCKY_O_MILK)
				  .input(Items.BOWL)
				  .group("berry_matcha_blast")
				  .criterion("has_strawberry_lucky_o_milk", this.conditionsFromTag(ModItemTags.STRAWBERRY_LUCKY_O_MILK))
				  .criterion("has_matcha_lucky_o_milk", this.conditionsFromTag(ModItemTags.MATCHA_LUCKY_O_MILK))
				  .offerTo(this.exporter);

			}

			private void luckyOMilkCooling(Item warmMilk, Item coldMilk, String group) {
				this.createShapeless(RecipeCategory.FOOD, coldMilk)
				  .input(ModItemTags.COLD)
				  .input(warmMilk)
				  .group(group)
				  .criterion("has_cold_milk", this.conditionsFromItem(coldMilk))
				  .offerTo(this.exporter);
			}
		};
	}

	@Override
	public String getName() {
		return "Crafting Recipe";
	}
}
