package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilShape;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.LootTableManager;

public class TileEntityFishing extends TileEntityBaseMachineInvo implements ITickable {
  private static final String NBT_INV = "Inventory";
  private static final String NBT_SLOT = "Slot";
  public static final int RODSLOT = 1;
  public static final int FISHSLOTS = 15;
  public static final int MINIMUM_WET_SIDES = 2;
  public static final float SPEEDFACTOR = 0.00089F;//// bigger == faster
  private int toolSlot = 0;
  public ArrayList<Block> waterBoth = new ArrayList<Block>();
  private ItemStack[] inv;
  public TileEntityFishing() {
    inv = new ItemStack[RODSLOT + FISHSLOTS];
    waterBoth.add(Blocks.FLOWING_WATER);
    waterBoth.add(Blocks.WATER);
  }
  //new idea: speed depends on number of sides covered in water in the 6 sides
  //minimmum 3ish
  public boolean isValidPosition() { //make sure surrounded by water
    return this.countWetSides() >= MINIMUM_WET_SIDES;
  }
  /**
   * how much surrounded by water. TODO: update text on tooltip
   * 
   * @return [0,6]
   */
  public int countWetSides() {
    int cov = 0;
    List<BlockPos> areas = Arrays.asList(pos.down(), pos.north(), pos.east(), pos.west(), pos.south(), pos.up());
    World world = this.getWorld();
    for (BlockPos adj : areas) {
      if (waterBoth.contains(world.getBlockState(adj).getBlock()))
        cov++;
    }
    return cov;
  }
  /**
   * [0,17]
   * 
   * @return
   */
  public int countWaterFlowing() {
    int cov = 0;
    List<BlockPos> areas = this.getWaterArea();
    World world = this.getWorld();
    for (BlockPos adj : areas) {
      if (world.getBlockState(adj).getBlock() == Blocks.FLOWING_WATER)
        cov++;
    }
    return cov;
  }
  private List<BlockPos> getWaterArea() {
    return UtilShape.cubeFilled(this.getPos().down(2), 2, 2);
  }
  public int countWater() {
    int cov = 0;
    List<BlockPos> areas = getWaterArea();
    World world = this.getWorld();
    for (BlockPos adj : areas) {
      if (world.getBlockState(adj).getBlock() == Blocks.WATER)
        cov++;
    }
    return cov;
  }
  public boolean isEquipmentValid() {
    ItemStack equip = this.getStackInSlot(toolSlot);
    if(equip == null){
      return false;
    }    
    String itemsClass = equip.getItem().getClass().getName();
    
    if (equip.getItem() instanceof ItemFishingRod) { return true; }
    String aquaBase = "com.teammetallurgy.aquaculture.items.";
    if (itemsClass.equals(aquaBase + "ItemAquacultureWoodenFishingRod")
        || itemsClass.equals(aquaBase + "ItemAquacultureFishingRod")
        || itemsClass.equals(aquaBase + "ItemAdminAquacultureFishingRod")
        || itemsClass.equals(aquaBase + "ItemAdminFishingRod")
          ) { return true; }
    
    return false;
  }
  @Override
  public void update() {
    World world = this.getWorld();
    Random rand = world.rand;
    if (rand.nextDouble() < this.getSpeed() &&
        isValidPosition() && isEquipmentValid() &&
        world instanceof WorldServer && world != null &&
        world.getWorldTime() % Const.TICKS_PER_SEC == 0) {
      LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer) world);
      int luck = EnchantmentHelper.getEnchantmentLevel(Enchantments.LUCK_OF_THE_SEA, this.inv[toolSlot]);
      lootcontext$builder.withLuck((float) luck);
      //      java.lang.NullPointerException: Ticking block entity    at com.lothrazar.cyclicmagic.block.tileentity.TileEntityFishing.func_73660_a(TileEntityFishing.java:58)
      LootTableManager loot = world.getLootTableManager();
      if (loot == null) { return; }
      LootTable table = loot.getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING);
      if (table == null) { return; }
      LootContext context = lootcontext$builder.build();
      if (context == null) { return; }
      for (ItemStack itemstack : table.generateLootForPools(rand, context)) {
        UtilParticle.spawnParticle(world, EnumParticleTypes.WATER_WAKE, pos.up());
        //damage phase.
        int mending = EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, this.inv[toolSlot]);
        if (mending == 0) {
          damageTool();
        }
        else {
          if (rand.nextDouble() < 0.25) {//25% chance damage
            damageTool();
          }
          else if (rand.nextDouble() < 0.60) {//60-25 = 40 chance repair
            attemptRepairTool();
          }
          //else do nothing, leave it flat. mimics getting damaged and repaired right away
        }
        //loot phase
        this.tryAddToInventory(itemstack);
      }
    }
  }
  private void tryAddToInventory(ItemStack itemstack) {
    for (int i = RODSLOT; i <= FISHSLOTS; i++) {
      if (itemstack != null && itemstack.stackSize != 0) {
        itemstack = tryMergeStackIntoSlot(itemstack, i);
      }
    }
    if (itemstack != null && itemstack.stackSize != 0) { //FULL
      UtilItemStack.dropItemStackInWorld(this.getWorld(), this.pos.down(), itemstack);
    }
  }
  public double getSpeed() {
    //flowing water is usually zero, unless water levels are constantly fluctuating then it spikes
    int mult = this.countWaterFlowing() * 4 + this.countWater();// water in motion worth more so it varies a bit
    double randFact = 0;
    if (Math.random() > 0.9) {
      randFact = Math.random() / 10000;
    }
    return mult * SPEEDFACTOR + randFact;//+ Math.random()/10;
  }
  private void attemptRepairTool() {
    if (inv[toolSlot] != null && inv[toolSlot].getItemDamage() > 0) {//if it has zero damage, its fully repaired already
      inv[toolSlot].setItemDamage(inv[toolSlot].getItemDamage() - 1);//repair by one point
    }
  }
  private void damageTool() {
    ItemStack tool = inv[toolSlot];
    if (tool != null) {
      tool.attemptDamageItem(1, getWorld().rand);//does respect unbreaking
      //IF enchanted and IF about to break, then spit it out
      int damageRem = tool.getMaxDamage() - tool.getItemDamage();
      if (damageRem == 1 && EnchantmentHelper.getEnchantments(tool).size() > 0) {
        tryAddToInventory(tool);
        inv[toolSlot] = null;
      }
      //otherwise we also make sure if its fullly damanged
      if (tool.getItemDamage() >= tool.getMaxDamage()) {
        inv[toolSlot] = null;
      }
    }
  }
  private ItemStack tryMergeStackIntoSlot(ItemStack held, int furnaceSlot) {
    ItemStack current = this.getStackInSlot(furnaceSlot);
    boolean success = false;
    if (current == null) {
      this.setInventorySlotContents(furnaceSlot, held);
      held = null;
      success = true;
    }
    else if (held.isItemEqual(current)) {
      success = true;
      UtilItemStack.mergeItemsBetweenStacks(held, current);
    }
    if (success) {
      if (held != null && held.stackSize == 0) {// so now we just fix if something is size zero
        held = null;
      }
      this.markDirty();
    }
    return held;
  }
  @Override
  public int getSizeInventory() {
    return inv.length;
  }
  @Override
  public ItemStack getStackInSlot(int index) {
    return inv[index];
  }
  @Override
  public ItemStack decrStackSize(int index, int count) {
    ItemStack stack = getStackInSlot(index);
    if (stack != null) {
      if (stack.stackSize <= count) {
        setInventorySlotContents(index, null);
      }
      else {
        stack = stack.splitStack(count);
        if (stack.stackSize == 0) {
          setInventorySlotContents(index, null);
        }
      }
    }
    return stack;
  }
  @Override
  public ItemStack removeStackFromSlot(int index) {
    ItemStack stack = getStackInSlot(index);
    if (stack != null) {
      setInventorySlotContents(index, null);
    }
    return stack;
  }
  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    inv[index] = stack;
    if (stack != null && stack.stackSize > getInventoryStackLimit()) {
      stack.stackSize = getInventoryStackLimit();
    }
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    if (side == EnumFacing.UP) { return new int[] { 0 }; }
    return new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };//for outputting stuff
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    NBTTagList tagList = tagCompound.getTagList(NBT_INV, 10);
    for (int i = 0; i < tagList.tagCount(); i++) {
      NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
      byte slot = tag.getByte(NBT_SLOT);
      if (slot >= 0 && slot < inv.length) {
        inv[slot] = ItemStack.loadItemStackFromNBT(tag);
      }
    }
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    NBTTagList itemList = new NBTTagList();
    for (int i = 0; i < inv.length; i++) {
      ItemStack stack = inv[i];
      if (stack != null) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte(NBT_SLOT, (byte) i);
        stack.writeToNBT(tag);
        itemList.appendTag(tag);
      }
    }
    tagCompound.setTag(NBT_INV, itemList);
    return super.writeToNBT(tagCompound);
  }
}
