package mariculture.fishery.fish;

import static mariculture.api.core.Environment.Salinity.FRESH;
import static mariculture.core.lib.MCLib.dropletAqua;
import static mariculture.core.lib.MCLib.dropletFrozen;
import static mariculture.core.lib.MCLib.dropletWater;
import mariculture.api.core.Environment.Height;
import mariculture.api.core.Environment.Salinity;
import mariculture.api.fishery.RodType;
import mariculture.api.fishery.fish.FishSpecies;
import net.minecraft.world.World;

public class FishWalleye extends FishSpecies {
    @Override
    public int getTemperatureBase() {
        return 3;
    }

    @Override
    public int getTemperatureTolerance() {
        return 2;
    }

    @Override
    public Salinity getSalinityBase() {
        return FRESH;
    }

    @Override
    public int getSalinityTolerance() {
        return 0;
    }

    @Override
    public boolean isDominant() {
        return true;
    }

    @Override
    public int getLifeSpan() {
        return 15;
    }

    @Override
    public int getFertility() {
        return 10000;
    }

    @Override
    public int getFoodConsumption() {
        return 2;
    }

    @Override
    public int getWaterRequired() {
        return 100;
    }
   
    @Override
    public void addFishProducts() {
        addProduct(dropletWater, 8.5D);
        addProduct(dropletFrozen, 12.5D);
        addProduct(dropletAqua, 7.5D);
    }

    @Override
    public double getFishOilVolume() {
        return 3.155D;
    }

    @Override
    public RodType getRodNeeded() {
        return RodType.DIRE;
    }

    @Override
    public double getCatchChance(World world, int height) {
        return 20D;
    }
    
    @Override
    public double getCaughtAliveChance(World world, int height) {
        return 65D;
    }
}
