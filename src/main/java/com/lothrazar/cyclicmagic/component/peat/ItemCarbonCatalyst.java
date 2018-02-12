package com.lothrazar.cyclicmagic.component.peat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.component.wireless.TileEntityWirelessRec;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.item.base.BaseItem;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilOreDictionary;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

public class ItemCarbonCatalyst extends BaseItem implements IHasConfig {
  public static List<String> plants = new ArrayList<String>();
  public static boolean isPlantMatter(ItemStack found) {
    return UtilOreDictionary.doesMatchOreDict(found, plants);
  }
  @SubscribeEvent
  public static void onLeftClickBlock(PlayerInteractEvent.RightClickBlock event) {
    World world = event.getWorld();
    ItemStack stack = event.getItemStack();//player held item
    if (stack.getItem() == Item.getByNameOrId(Const.MODRES + "peat_carbon") == false) {
      return;
    }
    BlockPos posSpace = event.getPos().offset(event.getFace());
    IBlockState blockState = world.getBlockState(posSpace);
    if (blockState.getBlock() == Blocks.WATER || blockState.getBlock() == Blocks.FLOWING_WATER) {
      List<EntityItem> all = world.getEntitiesWithinAABB(EntityItem.class,
          UtilEntity.makeBoundingBox(posSpace, 1, 1));
      List<EntityItem> validPlants = new ArrayList<EntityItem>();
      Map<String, Boolean> keyFound = new HashMap<String, Boolean>();
      String key;
      for (EntityItem eItem : all) {
        key = eItem.getItem().getUnlocalizedName() + eItem.getItem().getItemDamage();
        ModCyclic.logger.info("EITEM:" + key);
        if (eItem != null
            && eItem.isDead == false
            && eItem.getItem() != null
            //a valid item in world, not one that weve found yet
            && keyFound.containsKey(key) == false
            && isPlantMatter(eItem.getItem())) {
          //so its DIFFERENT than everything in the current list
          validPlants.add(eItem);
          ModCyclic.logger.info("VALID " + key);
          keyFound.put(key, true);
        }
      }
      if (validPlants.size() < 4) {
        UtilChat.sendStatusMessage(event.getEntityPlayer(), "peat.plantmatter" + validPlants.size());
        return;
      }
      // done so convert all of them
      //TODO: make carbon take durability damage?
      ItemStack toSpawn;
      for (EntityItem eItem : validPlants) {
        toSpawn = new ItemStack(Item.getByNameOrId(Const.MODRES + "peat_biomass"));
        toSpawn.setCount(eItem.getItem().getCount());
        eItem.setDead();
        //TODO: find 4 different types at once?
        UtilItemStack.dropItemStackInWorld(world, posSpace, toSpawn);
        UtilParticle.spawnParticle(world, EnumParticleTypes.WATER_BUBBLE, posSpace);
      }
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    //TODO: actual config as well as hardcoded
    OreDictionary.registerOre("flower", Blocks.YELLOW_FLOWER);
    OreDictionary.registerOre("flower", Blocks.RED_FLOWER);
    OreDictionary.registerOre("flower", Blocks.DOUBLE_PLANT);
    OreDictionary.registerOre("lilypad", Blocks.WATERLILY);
    OreDictionary.registerOre("mushroom", Blocks.BROWN_MUSHROOM);
    OreDictionary.registerOre("mushroom", Blocks.RED_MUSHROOM);
    plants.add("treeSapling");
    plants.add("treeLeaves");
    plants.add("vine");
    plants.add("sugarcane");
    plants.add("blockCactus");
    plants.add("flower");
    plants.add("lilypad");
  }
}
