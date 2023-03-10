package com.lothrazar.cyclicmagic.block.tileentity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

/**
 * PLAN: gui to change power and vector.
 * 
 * make sure it saves data when you harvest and place
 * 
 * @author Sam
 *
 */
public class TileVector extends TileEntityBaseMachineInvo implements ITileRedstoneToggle {
  public static final int MAX_ANGLE = 90;
  public static final int MAX_YAW = 360;
  public static final int MAX_POWER = 300;///999 is op, maybe upgraes or config later
  public static final int DEFAULT_ANGLE = 45;
  public static final int DEFAULT_YAW = 90;
  public static final int DEFAULT_POWER = 250;
  public static final String NBT_ANGLE = "vectorAngle";
  public static final String NBT_POWER = "vectorPower";
  public static final String NBT_YAW = "vectorYaw";
  public static final String NBT_RED = "redst";
  private int angle = DEFAULT_ANGLE;
  private int power = DEFAULT_POWER;
  private int yaw = DEFAULT_YAW;
  private int playSound = 1;
  private int needsRedstone = 0;
  public static final String NBT_SOUND = "sound";
  public static enum Fields {
    ANGLE, POWER, YAW, SOUND, REDSTONE;
  }
  public TileVector() {}
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    power = tagCompound.getInteger(NBT_POWER);
    angle = tagCompound.getInteger(NBT_ANGLE);
    yaw = tagCompound.getInteger(NBT_YAW);
    playSound = tagCompound.getInteger(NBT_SOUND);
    needsRedstone = tagCompound.getInteger(NBT_RED);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_POWER, power);
    tagCompound.setInteger(NBT_ANGLE, angle);
    tagCompound.setInteger(NBT_YAW, yaw);
    tagCompound.setInteger(NBT_SOUND, playSound);
    tagCompound.setInteger(NBT_RED, needsRedstone);
    return super.writeToNBT(tagCompound);
  }
  public float getActualPower() {//stored as integer. used as decimal from 0.01 and up
    float actual = ((float) power) / 100F;//over 100 so that power 100 is = 1 and so on
    //also divide by 3 bc 999 is overpowered. so maximum actual is 333
    //actual = Math.max(actual / 3F, 0.01F);//but not lower than 1. so 1-5 is the same, is fine
    return actual;
  }
  public int getPower() {
    return power;
  }
  public int getAngle() {
    return angle;
  }
  public int getYaw() {
    return yaw;
  }
  public boolean playSound() {
    return this.playSound == 1;
  }
  @Override
  public int getField(int id) {
    if (id >= 0 && id < this.getFieldCount()) {
      switch (Fields.values()[id]) {
        case ANGLE:
          return angle;
        case POWER:
          return power;
        case YAW:
          return yaw;
        case SOUND:
          return playSound;
        case REDSTONE:
          return needsRedstone;
      }
    }
    return -1;
  }
  @Override
  public void setField(int id, int value) {
    if (id >= 0 && id < this.getFieldCount()) {
      switch (Fields.values()[id]) {
        case ANGLE:
          this.angle = Math.min(value, MAX_ANGLE);
        break;
        case POWER:
          this.power = Math.min(value, MAX_POWER);
          if (this.power <= 0) this.power = 1;
        break;
        case YAW:
          this.yaw = Math.min(value, MAX_YAW);
        break;
        case SOUND:
          this.playSound = value;
        case REDSTONE:
          this.needsRedstone = value;
        break;
      }
    }
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
  @Override
  public int getSizeInventory() {
    return 0;
  }
  @Override
  public ItemStack getStackInSlot(int index) {
    return null;
  }
  @Override
  public ItemStack decrStackSize(int index, int count) {
    return null;
  }
  @Override
  public ItemStack removeStackFromSlot(int index) {
    return null;
  }
  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {}
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return new int[] {};
  }
  @Override
  public void toggleNeedsRedstone() {
    int val = this.needsRedstone + 1;
    if (val > 1) {
      val = 0;//hacky lazy way
    }
    this.setField(Fields.REDSTONE.ordinal(), val);
  }
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
}
