package net.fawnoculus.vanilla_backrooms.client.datagen.recipes;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fawnoculus.vanilla_backrooms.items.ModItems;
import net.fawnoculus.vanilla_backrooms.misc.tags.ModItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class CraftingRecipeProvider extends FabricRecipeProvider {
    public CraftingRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                luckyOMilkCooling(ModItems.WARM_PLAIN_LUCKY_O_MILK, ModItems.COLD_PLAIN_LUCKY_O_MILK, "plain_lucky_o_milk");
                luckyOMilkCooling(ModItems.WARM_STRAWBERRY_LUCKY_O_MILK, ModItems.COLD_STRAWBERRY_LUCKY_O_MILK, "strawberry_lucky_o_milk");
                luckyOMilkCooling(ModItems.WARM_CHOCO_LUCKY_O_MILK, ModItems.COLD_CHOCO_LUCKY_O_MILK, "choco_lucky_o_milk");
                luckyOMilkCooling(ModItems.WARM_MATCHA_LUCKY_O_MILK, ModItems.COLD_MATCHA_LUCKY_O_MILK, "matcha_lucky_o_milk");
                luckyOMilkCooling(ModItems.WARM_BANANA_LUCKY_O_MILK, ModItems.COLD_BANANA_LUCKY_O_MILK, "banana_lucky_o_milk");
                luckyOMilkCooling(ModItems.WARM_LUCK_LUCKY_O_MILK, ModItems.COLD_LUCK_LUCKY_O_MILK, "luck_lucky_o_milk");

                this.shapeless(RecipeCategory.FOOD, ModItems.BERRY_MATCHA_BLAST)
                  .requires(ModItemTags.STRAWBERRY_LUCKY_O_MILK)
                  .requires(ModItemTags.MATCHA_LUCKY_O_MILK)
                  .requires(Items.BOWL)
                  .group("berry_matcha_blast")
                  .unlockedBy("has_strawberry_lucky_o_milk", this.has(ModItemTags.STRAWBERRY_LUCKY_O_MILK))
                  .unlockedBy("has_matcha_lucky_o_milk", this.has(ModItemTags.MATCHA_LUCKY_O_MILK))
                  .save(this.output);

            }

            private void luckyOMilkCooling(Item warmMilk, Item coldMilk, String group) {
                this.shapeless(RecipeCategory.FOOD, coldMilk)
                  .requires(ModItemTags.COLD)
                  .requires(warmMilk)
                  .group(group)
                  .unlockedBy("has_cold_milk", this.has(coldMilk))
                  .save(this.output);
            }
        };
    }

    @Override
    public String getName() {
        return "Crafting Recipe";
    }
}
