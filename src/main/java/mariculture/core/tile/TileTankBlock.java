package mariculture.core.tile;

import java.util.List;

import mariculture.core.config.Machines.MachineSettings;
import mariculture.core.helpers.FluidHelper;
import mariculture.core.network.PacketHandler;
import mariculture.core.util.ITank;
import mariculture.core.util.Tank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileTankBlock extends TileEntity implements IFluidHandler, ITank, IInventory {
    public static final int COPPER_CAPACITY = 32000;
    public static final int ALUMINUM_CAPACITY = 64000;
    public static final int TITANIUM_CAPACITY = 128000;
    public static final int GAS_CAPACITY = 512000;

    private int differenceFill = 0;
    private int differenceDrain = 0;
    public Tank tank;

    public TileTankBlock() {
        tank = new Tank(COPPER_CAPACITY);
    }

    public float getFluidAmountScaled() {
        return tank.getFluid().amount / (tank.getCapacity() * 1.01F);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        int amount = tank.fill(resource, doFill);
        differenceFill += amount;
        if (amount > 0 && doFill && differenceFill >= MachineSettings.TANK_UPDATE_AMOUNT) {
            differenceFill = 0;
            if (!worldObj.isRemote) {
                PacketHandler.syncFluids(this, getFluid());
            }
        }

        return amount;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        FluidStack amount = tank.drain(maxDrain, doDrain);
        if (amount == null) return amount;

        differenceDrain += amount.amount;
        if (amount != null && doDrain && differenceDrain >= MachineSettings.TANK_UPDATE_AMOUNT) {
            differenceDrain = 0;
            if (!worldObj.isRemote) {
                PacketHandler.syncFluids(this, getFluid());
            }
        }

        return amount;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return drain(ForgeDirection.UNKNOWN, resource.amount, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { tank.getInfo() };
    }

    @Override
    public FluidStack getFluid(int transfer) {
        if (tank.getFluid() == null) return null;

        if (tank.getFluidAmount() - transfer < 0) return null;

        return new FluidStack(tank.getFluid(), transfer);
    }

    @Override
    public String getFluidName() {
        return FluidHelper.getFluidName(tank.getFluid());
    }

    @Override
    public List getFluidQty(List tooltip) {
        return FluidHelper.getFluidQty(tooltip, tank.getFluid(), tank.getCapacity());
    }

    @Override
    public FluidStack getFluid() {
        return tank.getFluid();
    }

    @Override
    public int getTankScaled(int i) {
        int qty = tank.getFluidAmount();
        int max = tank.getCapacity();

        return max != 0 ? qty * i / max : 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        tank.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tank.writeToNBT(tagCompound);
    }

    @Override
    public void setFluid(FluidStack fluid) {
        tank.setFluid(fluid);
    }

    @Override
    public FluidStack getFluid(byte tank) {
        return getFluid();
    }

    @Override
    public void setFluid(FluidStack fluid, byte tank) {
        setFluid(fluid);
    }

    public double getCapacity() {
        return tank.getCapacity();
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        return;
    }

    @Override
    public String getInventoryName() {
        return "tank";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(stack);
        if (liquid != null) {
            int amount = fill(ForgeDirection.UNKNOWN, liquid, false);
            if (amount >= liquid.amount) {
                fill(ForgeDirection.UNKNOWN, liquid, true);
                return true;
            } else return false;
        }

        return false;
    }
}
