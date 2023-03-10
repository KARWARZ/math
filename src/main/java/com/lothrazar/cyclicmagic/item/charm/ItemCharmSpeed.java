package com.lothrazar.cyclicmagic.item.charm;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseCharm;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCharmSpeed extends BaseCharm implements IHasRecipe {
  private static final int durability = 2000;
  private static final float speedfactor = 0.08F;
  public ItemCharmSpeed() {
    super(durability);
  }
  /**
   * Called each tick as long the item is on a player inventory. Uses by maps to
   * check if is on a player hand and update it's contents.
   */
  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (entityIn instanceof EntityPlayer) {
      onTick(stack, (EntityPlayer) entityIn);
    }
  }
  @Override
  public void onTick(ItemStack stack, EntityPlayer player) {
    if (!this.canTick(stack)) { return; }
    UtilEntity.speedupEntityIfMoving(player, speedfactor);
    if (player.getEntityWorld().rand.nextDouble() < 0.1) {
      super.damageCharm(player, stack);
    }
  }
  @Override
  public void addRecipe() {
    super.addRecipeAndRepair(new ItemStack(Blocks.EMERALD_BLOCK));
  }
}
