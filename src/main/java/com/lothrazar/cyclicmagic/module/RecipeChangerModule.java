package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilRecipe;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeChangerModule extends BaseModule implements IHasConfig {
  private boolean playerSkull;
  private boolean mushroomBlocks;
  private boolean simpleDispenser;
  private boolean repeaterSimple;
  private boolean minecartsSimple;
  private boolean notchApple;
  private boolean elytraRepair;
  private boolean melonToSlice;
  private boolean difficultEarlygameRecipes;
  private boolean snowBlocksToBalls;
  private boolean quartzBlocksToItem;
  private boolean glowstoneBlockToDust;
  private boolean netherwartBlockReverse;
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.recipes;
    config.setCategoryComment(category, "New and altered recipes");
    playerSkull = config.get(category, "Player Skulls",
        true, "Create a player skull by combining wither, skeleton, zombie, and creeper skulls").getBoolean();
    mushroomBlocks = config.get(category, "Mushroom Blocks",
        true, "Create mushroom blocks from items").getBoolean();
    simpleDispenser = config.get(category, "Simple Dispenser",
        true, "Craft a dispenser with string instead of a bow").getBoolean();
    repeaterSimple = config.get(category, "Simple Repeater",
        true, "Craft repeaters using sticks and redstone in place of redstone torches").getBoolean();
    minecartsSimple = config.get(category, "Simple Minecarts",
        true, "Craft the minecart combinations using five iron as well as minecarts").getBoolean();
    notchApple = config.get(category, "Notch Apple",
        true, "Craft a notch apple with golden blocks as usual").getBoolean();
    elytraRepair = config.get(category, "Elytra Repair",
        true, "You can mostly repair elytra wings with a wither skull; but it loses all enchants").getBoolean();
    melonToSlice = config.get(category, "Melon Block Slices",
        true, "Craft a Melon block into nine slices").getBoolean();
    category = Const.ConfigCategory.recipes;
    difficultEarlygameRecipes = config.get(category, "Altered Stone",
        false,
        "WARNING: this removes vanilla recipes:  True will mean that the furnace recipe requires coal in the middle, and stone tools require smoothstone.")
        .getBoolean();
    snowBlocksToBalls = config.get(category, "SnowBlockBalls",
        true, "Craft Snow blocks back into snowballs").getBoolean();
    quartzBlocksToItem = config.get(category, "QuartzBlockToItem",
        true, "Craft Quartz blocks back to the items").getBoolean();
    glowstoneBlockToDust = config.get(category, "GlowstoneBlockToDust",
        true, "Craft Glowstone blocks back to dust").getBoolean();
    netherwartBlockReverse = config.get(category, "NetherwartBlockReverse",
        true, "Craft Netherwart blocks back to item").getBoolean();
  }
  @Override
  public void onInit() {
    if (glowstoneBlockToDust) {
      glowstoneBlockToDust();
    }
    if (quartzBlocksToItem) {
      quartzBlocksItem();
    }
    if (snowBlocksToBalls) {
      snowBlocksBalls();
    }
    if (playerSkull) {
      playerSkull();
    }
    if (mushroomBlocks) {
      mushroomBlocks();
    }
    if (simpleDispenser) {
      simpleDispenser();
    }
    if (repeaterSimple) {
      repeaterSimple();
    }
    if (minecartsSimple) {
      minecartsSimple();
    }
    if (notchApple) {
      notchApple();
    }
    if (elytraRepair) {
      elytraRepair();
    }
    if (melonToSlice) {
      melonToSlice();
    }
    if (difficultEarlygameRecipes) {
      smoothstoneRequired();
      furnaceNeedsCoal();
    }
    if (netherwartBlockReverse) {
      netherwartBlockReverse();//bone block reverse is already in game, why not this
    }
    // https://github.com/PrinceOfAmber/SamsPowerups/blob/master/Recipes/src/main/java/com/lothrazar/samsrecipes/RecipeRegistry.java
  }
  private void netherwartBlockReverse() {
    GameRegistry.addShapelessRecipe(new ItemStack(Items.NETHER_WART, 9),
        new ItemStack(Blocks.NETHER_WART_BLOCK)); // nether_wart_block
  }
  private void glowstoneBlockToDust() {
    GameRegistry.addShapelessRecipe(new ItemStack(Items.GLOWSTONE_DUST, 4),
        new ItemStack(Blocks.GLOWSTONE));
  }
  private void snowBlocksBalls() {
    GameRegistry.addShapelessRecipe(new ItemStack(Items.SNOWBALL, 4),
        new ItemStack(Blocks.SNOW));
  }
  private void quartzBlocksItem() {
    GameRegistry.addShapelessRecipe(new ItemStack(Items.QUARTZ, 4),
        new ItemStack(Blocks.QUARTZ_BLOCK));
  }
  private void melonToSlice() {
    GameRegistry.addShapelessRecipe(new ItemStack(Items.MELON, 9),
        new ItemStack(Blocks.MELON_BLOCK));
  }
  private void elytraRepair() {
    GameRegistry.addShapelessRecipe(new ItemStack(Items.ELYTRA, 1, UtilItemStack.getMaxDmgFraction(Items.ELYTRA, 10)),
        new ItemStack(Items.SKULL, 1, Const.skull_wither),
        new ItemStack(Items.ELYTRA, 1, OreDictionary.WILDCARD_VALUE));
  }
  private void notchApple() {
    // https://www.reddit.com/r/minecraftsuggestions/comments/4d20g5/bring_back_the_notch_apple_crafting_recipe/
    GameRegistry.addRecipe(new ItemStack(Items.GOLDEN_APPLE, 1, 1), "ggg", "gag", "ggg", 'g', new ItemStack(Blocks.GOLD_BLOCK), 'a', new ItemStack(Items.APPLE));
  }
  private void playerSkull() {
    GameRegistry.addShapelessRecipe(new ItemStack(Items.SKULL, 4, Const.skull_player),
        new ItemStack(Items.SKULL, 1, Const.skull_wither),
        new ItemStack(Items.SKULL, 1, Const.skull_skeleton),
        new ItemStack(Items.SKULL, 1, Const.skull_zombie),
        new ItemStack(Items.SKULL, 1, Const.skull_creeper));
  }
  private void mushroomBlocks() {
    GameRegistry.addRecipe(new ItemStack(Blocks.RED_MUSHROOM_BLOCK),
        "mm", "mm", 'm', Blocks.RED_MUSHROOM);
    GameRegistry.addRecipe(new ItemStack(Blocks.BROWN_MUSHROOM_BLOCK),
        "mm", "mm", 'm', Blocks.BROWN_MUSHROOM);
  }
  private void repeaterSimple() {
    GameRegistry.addRecipe(new ItemStack(Items.REPEATER),
        "r r", "srs", "ttt",
        't', new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.STONE.ordinal()), 's', new ItemStack(Items.STICK), 'r', new ItemStack(Items.REDSTONE));
  }
  private void minecartsSimple() {
    // normally you would need the minecart created in a different step. this is
    // faster
    GameRegistry.addRecipe(new ItemStack(Items.CHEST_MINECART), "   ", "ici", "iii", 'i', Items.IRON_INGOT, 'c', Blocks.CHEST);
    GameRegistry.addRecipe(new ItemStack(Items.TNT_MINECART), "   ", "ici", "iii", 'i', Items.IRON_INGOT, 'c', Blocks.TNT);
    GameRegistry.addRecipe(new ItemStack(Items.HOPPER_MINECART), "   ", "ici", "iii", 'i', Items.IRON_INGOT, 'c', Blocks.HOPPER);
    GameRegistry.addRecipe(new ItemStack(Items.FURNACE_MINECART), "   ", "ici", "iii", 'i', Items.IRON_INGOT, 'c', Blocks.FURNACE);
  }
  private void simpleDispenser() {
    GameRegistry.addRecipe(new ItemStack(Blocks.DISPENSER),
        "ccc", "csc", "crc",
        'c', Blocks.COBBLESTONE, 's', Items.STRING, 'r', Items.REDSTONE);
  }
  private void furnaceNeedsCoal() {
    UtilRecipe.removeRecipe(Blocks.FURNACE);
    GameRegistry.addRecipe(new ItemStack(Blocks.FURNACE), "bbb", "bcb", "bbb", 'b', Blocks.COBBLESTONE, 'c', Items.COAL);
  }
  private void smoothstoneRequired() {
    UtilRecipe.removeRecipe(Items.STONE_PICKAXE);
    //    GameRegistry.addRecipe(
    //        new ItemStack(Items.STONE_PICKAXE, 1, UtilItem.getMaxDmgFraction(Items.STONE_PICKAXE, 4)),
    //        "sss", " t ", " t ", 's', Blocks.COBBLESTONE, 't', Items.STICK);
    //    GameRegistry.addRecipe(new ItemStack(Items.STONE_PICKAXE, 1, UtilItem.getMaxDmgFraction(Items.STONE_PICKAXE, 4)), "sss", " t ", " t ", 's', Blocks.COBBLESTONE, 't', Items.STICK);
    GameRegistry.addRecipe(new ItemStack(Items.STONE_PICKAXE),
        "sss", " t ", " t ",
        's', new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.STONE.ordinal()),
        't', Items.STICK);
    UtilRecipe.removeRecipe(Items.STONE_SWORD);
    //    GameRegistry.addRecipe(new ItemStack(Items.STONE_SWORD, 1, UtilItem.getMaxDmgFraction(Items.STONE_SWORD, 4)), " s ", " s ", " t ", 's', Blocks.COBBLESTONE, 't', Items.STICK);
    GameRegistry.addRecipe(new ItemStack(Items.STONE_SWORD), " s ", " s ", " t ",
        's', new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.STONE.ordinal()),
        't', Items.STICK);
    UtilRecipe.removeRecipe(Items.STONE_AXE);
    //    GameRegistry.addRecipe(new ItemStack(Items.STONE_AXE, 1, UtilItem.getMaxDmgFraction(Items.STONE_AXE, 4)), "ss ", "st ", " t ", 's', Blocks.COBBLESTONE, 't', Items.STICK);
    //    GameRegistry.addRecipe(new ItemStack(Items.STONE_AXE, 1, UtilItem.getMaxDmgFraction(Items.STONE_AXE, 4)), " ss", " ts", " t ", 's', Blocks.COBBLESTONE, 't', Items.STICK);
    GameRegistry.addRecipe(new ItemStack(Items.STONE_AXE), "ss ", "st ", " t ",
        's', new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.STONE.ordinal()),
        't', Items.STICK);
    GameRegistry.addRecipe(new ItemStack(Items.STONE_AXE), " ss", " ts", " t ",
        's', new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.STONE.ordinal()),
        't', Items.STICK);
    UtilRecipe.removeRecipe(Items.STONE_HOE);
    //    GameRegistry.addRecipe(new ItemStack(Items.STONE_HOE, 1, UtilItem.getMaxDmgFraction(Items.STONE_HOE, 4)), "ss ", " t ", " t ", 's', Blocks.COBBLESTONE, 't', Items.STICK);
    //    GameRegistry.addRecipe(new ItemStack(Items.STONE_HOE, 1, UtilItem.getMaxDmgFraction(Items.STONE_HOE, 4)), " ss", " t ", " t ", 's', Blocks.COBBLESTONE, 't', Items.STICK);
    GameRegistry.addRecipe(new ItemStack(Items.STONE_HOE), "ss ", " t ", " t ",
        's', new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.STONE.ordinal()),
        't', Items.STICK);
    GameRegistry.addRecipe(new ItemStack(Items.STONE_HOE), " ss", " t ", " t ",
        's', new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.STONE.ordinal()),
        't', Items.STICK);
    UtilRecipe.removeRecipe(Items.STONE_SHOVEL);
    //    GameRegistry.addRecipe(new ItemStack(Items.STONE_SHOVEL, 1, UtilItem.getMaxDmgFraction(Items.STONE_SHOVEL, 4)), " s ", " t ", " t ", 's', Blocks.COBBLESTONE, 't', Items.STICK);
    GameRegistry.addRecipe(new ItemStack(Items.STONE_SHOVEL), " s ", " t ", " t ",
        's', new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.STONE.ordinal()),
        't', Items.STICK);
    //    GameRegistry.addRecipe(new ItemStack(Items.IRON_CHESTPLATE, 1, UtilItem.getMaxDmgFraction(Items.IRON_CHESTPLATE, 2)), 
    //        "i i", "iii", "iii", 'i',Items.IRON_INGOT);
    //
    //    GameRegistry.addShapelessRecipe(new ItemStack(Items.IRON_CHESTPLATE),
    //        new ItemStack(Items.IRON_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE),
    //        new ItemStack(Items.LEATHER_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE)
    //        );
  }
}
