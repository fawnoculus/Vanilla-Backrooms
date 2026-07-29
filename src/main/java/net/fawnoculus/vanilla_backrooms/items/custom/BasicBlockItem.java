package net.fawnoculus.vanilla_backrooms.items.custom;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.Block;
import xyz.nucleoid.packettweaker.PacketContext;

import org.jetbrains.annotations.Nullable;

public class BasicBlockItem extends BlockItem implements PolymerItem {
    private final BlockItemStateProperties blockState;
    private final Component backupName;
    private final Item polymerItem;
    private final boolean polymerUseModel;

    public BasicBlockItem(Block block, Properties settings, Item polymerItem, Component backupName) {
        this(block, settings, polymerItem, false, backupName, BlockItemStateProperties.EMPTY);
    }

    public BasicBlockItem(Block block, Properties settings, Item polymerItem, boolean useModel, Component backupName, @Nullable BlockItemStateProperties blockState) {
        super(block, settings);
        this.blockState = blockState;
        this.polymerItem = polymerItem;
        this.polymerUseModel = useModel;
        this.backupName = backupName;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return this.polymerItem;
    }

    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        /*
        if (this.polymerUseModel && PolymerResourcePackUtils.hasMainPack(context)) {
            return PolymerItem.super.getPolymerItemModel(stack, context);
        }
         */
        return null;
    }

    @Override
    public void modifyBasePolymerItemStack(ItemStack out, ItemStack stack, PacketContext context) {
        if (!PolymerResourcePackUtils.hasMainPack(context)) {
            out.set(DataComponents.CUSTOM_NAME, backupName.copy().setStyle(Style.EMPTY.withItalic(false)));
        }

        if (blockState != null) {
            out.set(DataComponents.BLOCK_STATE, blockState);
        }
    }
}
