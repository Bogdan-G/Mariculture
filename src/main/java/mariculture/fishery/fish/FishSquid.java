package mariculture.fishery.fish;

import static mariculture.api.core.Environment.Salinity.SALINE;
import static mariculture.core.lib.MCLib.dropletWater;
import static mariculture.core.lib.MCLib.ink;
import mariculture.api.core.Environment.Salinity;
import mariculture.api.fishery.RodType;
import mariculture.api.fishery.fish.FishSpecies;
import net.minecraft.world.World;

public class FishSquid extends FishSpecies {
    @Override
    public int getTemperatureBase() {
        return 12;
    }

    @Override
    public int getTemperatureTolerance() {
        return 6;
    }

    @Override
    public Salinity getSalinityBase() {
        return SALINE;
    }

    @Override
    public int getSalinityTolerance() {
        return 1;
    }

    @Override
    public boolean isDominant() {
        return false;
    }

    @Override
    public int getLifeSpan() {
        return 5;
    }

    @Override
    public int getFertility() {
        return 2500;
    }

    @Override
    public boolean requiresFood() {
        return true;
    }

    @Override
    public int getWaterRequired() {
        return 20;
    }

    @Override
    public void addFishProducts() {
        addProduct(dropletWater, 1.5D);
        addProduct(ink, 15D);
    }

    @Override
    public double getFishOilVolume() {
        return 0D;
    }

    @Override
    public int getFishMealSize() {
        return 0;
    }

    @Override
    public RodType getRodNeeded() {
        return RodType.DIRE;
    }

    @Override
    public double getCatchChance(World world, int height) {
        return 10D;
    }

    @Override
    public double getCaughtAliveChance(World world, int height) {
        return world.isDaytime() ? 85D : 55D;
    }
}
