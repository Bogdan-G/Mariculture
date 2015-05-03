package mariculture.fishery.fish;

import static mariculture.api.core.Environment.Salinity.SALINE;
import static mariculture.core.lib.MCLib.dropletEarth;
import static mariculture.core.lib.MCLib.dropletPoison;
import static mariculture.core.lib.MCLib.dropletWater;
import mariculture.api.core.Environment.Salinity;
import mariculture.api.fishery.RodType;
import mariculture.api.fishery.fish.FishSpecies;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class FishBlaasop extends FishSpecies {
    @Override
    public int getTemperatureBase() {
        return 12;
    }

    @Override
    public int getTemperatureTolerance() {
        return 3;
    }

    @Override
    public Salinity getSalinityBase() {
        return SALINE;
    }

    @Override
    public int getSalinityTolerance() {
        return 2;
    }

    @Override
    public boolean isDominant() {
        return true;
    }

    @Override
    public int getLifeSpan() {
        return 12;
    }

    @Override
    public int getFertility() {
        return 150;
    }

    @Override
    public int getWaterRequired() {
        return 40;
    }

    @Override
    public int getAreaOfEffectBonus(ForgeDirection dir) {
        return dir == ForgeDirection.DOWN ? 3 : 0;
    }

    @Override
    public void addFishProducts() {
        addProduct(dropletWater, 2.5D);
        addProduct(dropletPoison, 0.5D);
        addProduct(dropletEarth, 2.5D);
    }

    @Override
    public double getFishOilVolume() {
        return 0.450D;
    }

    @Override
    public boolean canAlwaysEat() {
        return true;
    }

    @Override
    public void onConsumed(World world, EntityPlayer player) {
        player.addPotionEffect(new PotionEffect(Potion.harm.id, 1, 0));
        player.addPotionEffect(new PotionEffect(Potion.poison.id, 200, 5));
        player.addPotionEffect(new PotionEffect(Potion.confusion.id, 500, 0));
    }
    
    @Override
    public boolean hasLivingEffect() {
        return true;
    }

    @Override
    public void affectLiving(EntityLivingBase entity) {
        entity.addPotionEffect(new PotionEffect(Potion.confusion.id, 150, 1, true));
        entity.addPotionEffect(new PotionEffect(Potion.wither.id, 250, 0, true));
    }

    @Override
    public boolean canWorkAtThisTime(boolean isDay) {
        return !isDay;
    }

    @Override
    public RodType getRodNeeded() {
        return RodType.OLD;
    }

    @Override
    public double getCatchChance(World world, int height) {
        return height < 32 ? 40D : 0D;
    }

    @Override
    public double getCaughtAliveChance(World world, int height) {
        return 75D;
    }
}
