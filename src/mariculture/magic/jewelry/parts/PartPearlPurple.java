package mariculture.magic.jewelry.parts;

import net.minecraft.item.ItemStack;
import mariculture.core.Core;
import mariculture.core.lib.Text;
import mariculture.core.lib.Jewelry;
import mariculture.core.lib.PearlColor;

public class PartPearlPurple extends JewelryPart {
	@Override
	public boolean isVisible(int type) {
		return true;
	}
	
	@Override
	public String getPartName() {
		return "pearlPurple";
	}
	
	@Override
	public String getPartLang() {
		return "item.pearls." + getPartName() + ".name";
	}

	@Override
	public String getPartType(int type) {
		return (type == Jewelry.RING)? "jewel": "material";
	}
	
	@Override
	public String getColor() {
		return Text.PURPLE;
	}
	
	@Override
	public int getEnchantability() {
		return 4;
	}
	
	@Override
	public ItemStack getItemStack() {
		return new ItemStack(Core.pearls, 1, PearlColor.PURPLE);
	}
}