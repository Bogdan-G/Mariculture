package mariculture.fishery.gui;

import java.util.List;

import mariculture.core.gui.GuiMariculture;
import mariculture.core.gui.feature.FeatureBubbles;
import mariculture.core.gui.feature.FeatureEject;
import mariculture.core.gui.feature.FeatureNotifications;
import mariculture.core.gui.feature.FeatureRedstone;
import mariculture.core.gui.feature.FeatureTank;
import mariculture.core.gui.feature.FeatureUpgrades;
import mariculture.core.gui.feature.FeatureNotifications.NotificationType;
import mariculture.core.gui.feature.FeatureTank.TankSize;
import mariculture.core.util.MCTranslate;
import mariculture.fishery.FishFoodHandler;
import mariculture.fishery.tile.TileFeeder;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class GuiFeeder extends GuiMariculture {
    private TileFeeder tile;

    public GuiFeeder(InventoryPlayer player, TileFeeder tile) {
        super(new ContainerFeeder(tile, player), "feeder", 10);
        this.tile = tile;
        features.add(new FeatureUpgrades());
        features.add(new FeatureBubbles(tile, 104, 17));
        features.add(new FeatureNotifications(tile, new NotificationType[] { NotificationType.NO_FOOD, NotificationType.NO_MALE, NotificationType.NO_FEMALE, NotificationType.BAD_ENV }));
        features.add(new FeatureRedstone(tile));
        features.add(new FeatureEject(tile));
        features.add(new FeatureTank(tile, 33, 19, TankSize.DOUBLE));
    }

    @Override
    public void addToolTip() {
        if (mouseX >= 95 && mouseX <= 98) {
            if (mouseY >= 24 && mouseY <= 41) {
                tooltip = tile.getTooltip(TileFeeder.male, tooltip);
            }
            
            if (mouseY >= 52 && mouseY <= 69) {
                tooltip = tile.getTooltip(TileFeeder.female, tooltip);
            }
        }
    }

    @Override
    public void addItemToolTip(ItemStack stack, List<String> list) {
        if (stack != null) {
            int value = FishFoodHandler.getValue(stack);
            if (value > 0) {
                list.add(MCTranslate.translate("provides") + " " + value + " " + MCTranslate.translate("fishFood"));
            }
        }
    }

    @Override
    public void drawBackground(int x, int y) {
        int fish1 = tile.getFishLifeScaled(tile.getStackInSlot(TileFeeder.male), 17);
        if (fish1 > -1) {
            drawTexturedModalRect(x + 95, y + 24 + 17 - fish1, 0, 208 + 17 - fish1, 4, fish1 + 2);
        } else {
            drawTexturedModalRect(x + 95, y + 24, 4, 208, 4, 18);
        }

        int fish2 = tile.getFishLifeScaled(tile.getStackInSlot(TileFeeder.female), 17);
        if (fish2 > -1) {
            drawTexturedModalRect(x + 95, y + 52 + 17 - fish2, 0, 208 + 17 - fish2, 4, fish2 + 2);
        } else {
            drawTexturedModalRect(x + 95, y + 52, 4, 208, 4, 18);
        }
    }
}
