package com.lothrazar.cyclicmagic.util;
import net.minecraft.util.ResourceLocation;

public class Const {
  public static class Res {
    public static final String folder = "textures/gui/";
    public static final ResourceLocation SLOT = new ResourceLocation(Const.MODID, folder + "inventory_slot.png");
    public static final ResourceLocation PROGRESS = new ResourceLocation(Const.MODID, folder + "progress.png");
    public static final ResourceLocation PROGRESSCTR = new ResourceLocation(Const.MODID, folder + "progress_ctr.png");
    public static final ResourceLocation TABLEDEFAULT = new ResourceLocation(Const.MODID, folder + "table.png");
    public static final ResourceLocation TABLEPLAIN = new ResourceLocation(Const.MODID, folder + "table_plain.png");
  }
  public static final String MODID = "cyclicmagic";
  public static final String MODRES = Const.MODID + ":";
  public static final String MODCONF = Const.MODID + ".";
  public class ConfigCategory {
    //to store categories. basically an enum/lookup table
    public static final String player = MODCONF + "player";
    public static final String worldGen = MODCONF + "world generation";
    public static final String mobs = MODCONF + "mobs";
    public static final String mobspawns = MODCONF + "Mob Spawns";
    public static final String blocks = MODCONF + "blocks";
    public static final String inventory = MODCONF + "Inventory";
    public static final String items = MODCONF + "items";
    public static final String itemsTack = items + ".StackSize";
    public static final String recipes = MODCONF + "recipes";
    public static final String villagers = MODCONF + "villagers";
    public static final String content = MODCONF + "content";
    public static final String contentDefaultText = "Set false to delete - requires restart";
    public static final String modpackMisc = "modpacks";
    public static final String uncrafter = modpackMisc + ".uncrafter";
    public static final String saplingBiomes = modpackMisc + ".SaplingGrowthDetail";
    public static final String InventoryButtonsModpack = modpackMisc + ".TerrariaButtons";
    public static final String commands = modpackMisc + ".Commands";
    public static final String worldGenOceans = worldGen + ".ocean";
  }
  public class ToolStrings {
    public static final String pickaxe = "pickaxe";
    public static final String shovel = "shovel";
    public static final String axe = "axe";
  }
  public static final int SQ = 18;
  public static final int ARMOR_SIZE = 4;
  public static final int ROWS_VANILLA = 3;
  public static final int COLS_VANILLA = 9;
  public static final int btnHeight = 20;
  // not a regular propert. : class ItemSkull:  {"skeleton", "wither", "zombie", "char", "creeper", "dragon"};
  public static final int skull_skeleton = 0;
  public static final int skull_wither = 1;
  public static final int skull_zombie = 2;
  public static final int skull_player = 3;
  public static final int skull_creeper = 4;
  //saplings
  public static final int sapling_oak = 0;
  public static final int sapling_spruce = 1;
  public static final int sapling_birch = 2;
  public static final int sapling_jungle = 3;
  public static final int sapling_acacia = 4;
  public static final int sapling_darkoak = 5;
  //	public static final int skull_dragon = 5;
  public static final int NOTIFY = 2;
  public static final int TICKS_PER_SEC = 20;
  public static final int CHUNK_SIZE = 16;
  public static final int DIR_WEST = 1;
  public static final int DIR_SOUTH = 0;
  public static final int DIR_EAST = 3;
  public static final int DIR_NORTH = 2;
  public static final int HOTBAR_SIZE = 9;
  public static final String SkullOwner = "SkullOwner";
  public class Dimension {
    public static final int overworld = 0;
    public static final int end = 1;
    public static final int nether = -1;
  }
  public class Potions {
    public final static int I = 0;
    public final static int II = 1;
    public final static int III = 2;
    public final static int IV = 3;
    public final static int V = 4;
  }
  public final static int SPAWN_RADIUS = 8 * Const.CHUNK_SIZE;// 128 is spawn size
  public static final int WORLDHEIGHT = 256;
  //http://minecraft.gamepedia.com/Light#Mobs
  //this or lower
  public static final int LIGHT_MOBSPAWN = 7;
  public static final int LIGHT_MOBSPAWN_BLAZE = 11;
  public static final int padding = 8;
  public static class HorseMeta {
    public static final int variant_white = 0;
    public static final int variant_creamy = 1;
    public static final int variant_chestnut = 2;
    public static final int variant_brown = 3;
    public static final int variant_black = 4;
    public static final int variant_gray = 5;
    public static final int variant_brown_dark = 6;
    public static final int type_standard = 0;
    public static final int type_donkey = 1;
    public static final int type_mule = 2;
    public static final int type_zombie = 3;
    public static final int type_skeleton = 4;
  }
}// ends class reference