package com.lothrazar.cyclicmagic.util;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class UtilFluid {
  public static ItemStack dispenseStack(World world, BlockPos pos, ItemStack stack, EnumFacing facing) {
    if (FluidUtil.getFluidContained(stack) != null) {
      return dumpContainer(world, pos, stack);
    }
    else {
      return fillContainer(world, pos, stack, facing);
    }
  }
  /**
   * Picks up fluid fills a container with it.
   */
  public static ItemStack fillContainer(World world, BlockPos pos, ItemStack stackIn, EnumFacing facing) {
    ItemStack result = stackIn.copy();
     result = FluidUtil.tryPickUpFluid(result, null, world, pos, facing);
//    if (--stackIn.stackSize == 0) {
//      stackIn.deserializeNBT(result.serializeNBT());
//    }
    return result;
  }
  /**
   * Drains a filled container and places the fluid.
   */
  public static ItemStack dumpContainer(World world, BlockPos pos, ItemStack stackIn) {
    //    BlockSourceImpl blocksourceimpl = new BlockSourceImpl(world, pos);
    ItemStack dispensedStack = stackIn.copy();
    dispensedStack.stackSize = 1;
    IFluidHandler fluidHandler = FluidUtil.getFluidHandler(dispensedStack);
    if (fluidHandler == null) { return null; }
    FluidStack fluidStack = fluidHandler.drain(Fluid.BUCKET_VOLUME, false);

    if (fluidStack != null && fluidStack.amount == Fluid.BUCKET_VOLUME && FluidUtil.tryPlaceFluid(null, world, fluidStack, pos)) {
      fluidHandler.drain(Fluid.BUCKET_VOLUME, true);
//      if (--stackIn.stackSize == 0) {
//        stackIn.deserializeNBT(dispensedStack.serializeNBT());
//      }
    }
    return dispensedStack;
  }
  
  public static IFluidHandler getFluidHandler(TileEntity tile, EnumFacing side) {
    return (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)) ? tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side) : null;
}
}
