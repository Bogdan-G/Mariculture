package mariculture.fishery.fish;

import static mariculture.core.lib.MCLib.dropletAir;
import static mariculture.core.lib.MCLib.dropletEarth;
import static mariculture.core.lib.MCLib.dropletTin;
import mariculture.api.core.Environment.Salinity;
import mariculture.api.fishery.RodType;
import mariculture.api.fishery.fish.FishSpecies;

public class FishTin extends FishSpecies {
    @Override
    public int getTemperatureBase() {
        return 15;
    }

    @Override
    public int getTemperatureTolerance() {
        return 8;
    }

    @Override
    public Salinity getSalinityBase() {
        return Salinity.FRESH;
    }

    @Override
    public int getSalinityTolerance() {
        return 1;
    }

    @Override
    public boolean isDominant() {
        return true;
    }

    @Override
    public int getLifeSpan() {
        return 6;
    }

    @Override
    public int getFertility() {
        return 60;
    }
    
    @Override
    public int getBaseProductivity() {
        return 0;
    }
    
    @Override
    public double getFishOilVolume() {
        return 0.0D;
    }
    
    @Override
    public int getFoodStat() {
        return -1;
    }
    
    @Override
    public int getFishMealSize() {
        return 0;
    }

    @Override
    public void addFishProducts() {
        addProduct(dropletTin, 10D);
        addProduct(dropletEarth, 7D);
        addProduct(dropletAir, 5D);
    }

    @Override
    public RodType getRodNeeded() {
        return RodType.SUPER;
    }
}
