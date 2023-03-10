package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemTrader extends BaseItem implements IHasRecipe {
  public static final int radius = 5;
  public ItemTrader() {
    super();
    this.setMaxStackSize(1);
  }
  public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World world, EntityPlayer player, EnumHand hand) {
    BlockPos p = player.getPosition();
    if (world.isRemote == false) {
      player.openGui(ModCyclic.instance, ModGuiHandler.GUI_INDEX_VILLAGER, world, p.getX(), p.getY(), p.getZ());
    }
    return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapedRecipe(new ItemStack(this), " e ", " b ", " q ",
        'e', Items.EMERALD,
        'b', Items.BOOK,
        'q', Blocks.BROWN_MUSHROOM);
    GameRegistry.addShapedRecipe(new ItemStack(this), " e ", " b ", " q ",
        'e', Items.EMERALD,
        'b', Items.BOOK,
        'q', Blocks.RED_MUSHROOM);
  }
}
