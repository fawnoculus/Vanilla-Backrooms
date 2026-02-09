package net.fawnoculus.vanillaBackrooms.items.custom;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class BasicBlockItem extends BlockItem implements PolymerItem {
	private final BlockStateComponent blockState;
	private final Text backupName;
	private final Item polymerItem;
	private final boolean polymerUseModel;

	public BasicBlockItem(Block block, Settings settings, Item polymerItem, Text backupName) {
		this(block, settings, polymerItem, false, backupName, BlockStateComponent.DEFAULT);
	}

	public BasicBlockItem(Block block, Settings settings, Item polymerItem, boolean useModel, Text backupName, @Nullable BlockStateComponent blockState) {
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
		if (this.polymerUseModel && PolymerResourcePackUtils.hasMainPack(context)) {
			return PolymerItem.super.getPolymerItemModel(stack, context);
		}
		return null;
	}

	@Override
	public void modifyBasePolymerItemStack(ItemStack out, ItemStack stack, PacketContext context) {
		if (!PolymerResourcePackUtils.hasMainPack(context)) {
			out.set(DataComponentTypes.CUSTOM_NAME, backupName.copy().setStyle(Style.EMPTY.withItalic(false)));
		}

		if (blockState != null) {
			out.set(DataComponentTypes.BLOCK_STATE, blockState);
		}
	}
}
