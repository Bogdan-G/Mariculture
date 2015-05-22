package maritech.tile;

import java.util.LinkedList;

import mariculture.api.util.CachedCoords;
import mariculture.core.config.Machines.Ticks;
import maritech.extensions.config.ExtensionMachines.ExtendedSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidHandler;

public class TileSluiceAdvanced extends TileSluice {
    public LinkedList<CachedCoords> queue;

    @Override
    public void updateEntity() {
        if (orientation.ordinal() > 1) {
            if (onTick((Ticks.SLUICE_TIMER - 1))) {
                pushToGenerator();
            }

            if (onTick(150)) {
                generateHPWater();
            }
        }

        if (onTick(ExtendedSettings.ADVANCED_SLUICE_TICK)) {
            if (!worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)) placeInTank();
            else pullFromTank();

            //Do the TankSwitching Always
            switchTanks();
        }
    }

    @Override
    public int getEnergyGenerated(int distance) {
        return (int) (super.getEnergyGenerated(distance) * 4);
    }

    private void addToList(int x, int y, int z) {
        CachedCoords coord = new CachedCoords(x, y, z);
        if (!queue.contains(coord)) queue.add(coord);
    }

    private boolean isValid(World world, int x, int y, int z, boolean drain) {
        if (drain) {
            Block block = world.getBlock(x, y, z);
            if (block instanceof IFluidBlock || block instanceof BlockStaticLiquid) {
                return world.getBlockMetadata(x, y, z) == 0;
            } else return false;
        } else {
            return canBeReplaced(x, y, z);
        }
    }

    private void updateArea(boolean drain) {
        queue = new LinkedList();
        int x = xCoord;
        int y = yCoord;
        int z = zCoord;

        if (drain) {
            x = xCoord + orientation.getOpposite().offsetX;
            y = yCoord + orientation.getOpposite().offsetY;
            z = zCoord + orientation.getOpposite().offsetZ;
        } else {
            x = xCoord + orientation.offsetX;
            y = yCoord + orientation.offsetY;
            z = zCoord + orientation.offsetZ;
        }

        if (isValid(worldObj, x, y, z, drain)) {
            addToList(x, y, z);
            int loop = !drain ? ExtendedSettings.ADVANCED_SLUICE_RADIUS / 2 : ExtendedSettings.ADVANCED_SLUICE_RADIUS;
            for (int j = 0; j < loop; j++) {
                LinkedList<CachedCoords> temp = (LinkedList<CachedCoords>) queue.clone();
                for (CachedCoords coord : temp) {
                    if (isValid(worldObj, coord.x + 1, coord.y, coord.z, drain)) {
                        addToList(coord.x + 1, coord.y, coord.z);
                    }

                    if (isValid(worldObj, coord.x - 1, coord.y, coord.z, drain)) {
                        addToList(coord.x - 1, coord.y, coord.z);
                    }

                    if (isValid(worldObj, coord.x, coord.y, coord.z + 1, drain)) {
                        addToList(coord.x, coord.y, coord.z + 1);
                    }

                    if (isValid(worldObj, coord.x, coord.y, coord.z - 1, drain)) {
                        addToList(coord.x, coord.y, coord.z - 1);
                    }

                    if (drain) {
                        if (isValid(worldObj, coord.x, coord.y + 1, coord.z, drain)) {
                            addToList(coord.x, coord.y + 1, coord.z);
                        }
                    } else {
                        if (isValid(worldObj, coord.x, coord.y - 1, coord.z, drain)) {
                            addToList(coord.x, coord.y - 1, coord.z);
                        }
                    }
                }
            }
        }
    }

    private CachedCoords getNextCoordinates(boolean drain) {
        CachedCoords cord = null;
        if (queue == null || queue.size() <= 0) {
            updateArea(drain);
        }

        if (queue != null && queue.size() > 0) {
            cord = queue.getLast();
            queue.removeLast();
        }

        return cord;
    }

    @Override
    public void placeInTank() {
        if (!ExtendedSettings.ENABLE_ADVANCED_SLUICE_DRAIN) {
            super.placeInTank();
            return;
        }

        TileEntity tile = mariculture.core.helpers.cofh.BlockHelper.getAdjacentTileEntity(this, orientation);
        if (tile != null && tile instanceof IFluidHandler) {
            CachedCoords cord = getNextCoordinates(true);
            if (cord != null) {
                removeFluid((IFluidHandler) tile, cord.x, cord.y, cord.z);
            }
        }
    }

    @Override
    public void pullFromTank() {
        TileEntity tile = mariculture.core.helpers.cofh.BlockHelper.getAdjacentTileEntity(this, orientation.getOpposite());
        if (tile != null && tile instanceof IFluidHandler) {
            CachedCoords cord = getNextCoordinates(false);
            if (cord != null) {
                placeFluid((IFluidHandler) tile, cord.x, cord.y, cord.z);
            }
        }
    }
}
