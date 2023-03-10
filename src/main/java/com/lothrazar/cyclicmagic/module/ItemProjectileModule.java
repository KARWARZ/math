package com.lothrazar.cyclicmagic.module;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.dispenser.BehaviorProjectileThrowable;
import com.lothrazar.cyclicmagic.entity.projectile.*;
import com.lothrazar.cyclicmagic.item.projectile.*;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileTNT.ExplosionType;
import com.lothrazar.cyclicmagic.registry.EntityProjectileRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.BlockDispenser;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemProjectileModule extends BaseModule implements IHasConfig {
  private boolean enableEnderBlaze;
  private boolean enableEnderDungeonFinder;
  private boolean enderFishing;
  private boolean enderSnow;
  private boolean enderWool;
  private boolean enderTorch;
  private boolean enderWater;
  private boolean enderLightning;
  private boolean enderBombsEnabled;
  ArrayList<BaseItemProjectile> projectiles = new ArrayList<BaseItemProjectile>();
  private boolean dynamiteSafe;
  private boolean dynamiteMining;
  private boolean magicNet;
  @Override
  public void onInit() {
    if (enableEnderBlaze) {
      ItemProjectileBlaze ender_blaze = new ItemProjectileBlaze();
      ItemRegistry.addItem(ender_blaze, "ender_blaze");
      EntityProjectileRegistry.registerModEntity(EntityBlazeBolt.class, "blazebolt", 1008);
      EntityBlazeBolt.renderSnowball = ender_blaze;
      projectiles.add(ender_blaze);
    }
    if (enableEnderDungeonFinder) {
      ItemProjectileDungeon ender_dungeon = new ItemProjectileDungeon();
      ItemRegistry.addItem(ender_dungeon, "ender_dungeon");
      EntityProjectileRegistry.registerModEntity(EntityDungeonEye.class, "dungeonbolt", 1006);
      EntityDungeonEye.renderSnowball = ender_dungeon;
      LootTableRegistry.registerLoot(ender_dungeon);
      ItemRegistry.registerWithJeiDescription(ender_dungeon);
      projectiles.add(ender_dungeon);
    }
    if (enderFishing) {
      ItemProjectileFishing ender_fishing = new ItemProjectileFishing();
      ItemRegistry.addItem(ender_fishing, "ender_fishing");
      EntityProjectileRegistry.registerModEntity(EntityFishingBolt.class, "fishingbolt", 1004);
      EntityFishingBolt.renderSnowball = ender_fishing;
      ItemRegistry.registerWithJeiDescription(ender_fishing);
      projectiles.add(ender_fishing);
    }
    if (enderWool) {
      ItemProjectileWool ender_wool = new ItemProjectileWool();
      ItemRegistry.addItem(ender_wool, "ender_wool");
      EntityProjectileRegistry.registerModEntity(EntityShearingBolt.class, "woolbolt", 1003);
      EntityShearingBolt.renderSnowball = ender_wool;
      ItemRegistry.registerWithJeiDescription(ender_wool);
      projectiles.add(ender_wool);
    }
    if (enderTorch) {
      ItemProjectileTorch ender_torch = new ItemProjectileTorch();
      ItemRegistry.addItem(ender_torch, "ender_torch");
      EntityProjectileRegistry.registerModEntity(EntityTorchBolt.class, "torchbolt", 1002);
      EntityTorchBolt.renderSnowball = ender_torch;
      ItemRegistry.registerWithJeiDescription(ender_torch);
      projectiles.add(ender_torch);
    }
    if (enderWater) {
      ItemProjectileWater ender_water = new ItemProjectileWater();
      ItemRegistry.addItem(ender_water, "ender_water");
      EntityProjectileRegistry.registerModEntity(EntityWaterBolt.class, "waterbolt", 1000);
      EntityWaterBolt.renderSnowball = ender_water;
      ItemRegistry.registerWithJeiDescription(ender_water);
      projectiles.add(ender_water);
    }
    if (enderSnow) {
      ItemProjectileSnow ender_snow = new ItemProjectileSnow();
      ItemRegistry.addItem(ender_snow, "ender_snow");
      EntityProjectileRegistry.registerModEntity(EntitySnowballBolt.class, "frostbolt", 1001);
      EntitySnowballBolt.renderSnowball = ender_snow;
      ItemRegistry.registerWithJeiDescription(ender_snow);
      projectiles.add(ender_snow);
    }
    if (enderLightning) {
      ItemProjectileLightning ender_lightning = new ItemProjectileLightning();
      ItemRegistry.addItem(ender_lightning, "ender_lightning");
      EntityProjectileRegistry.registerModEntity(EntityLightningballBolt.class, "lightningbolt", 999);
      EntityLightningballBolt.renderSnowball = ender_lightning;
      LootTableRegistry.registerLoot(ender_lightning);
      ItemRegistry.registerWithJeiDescription(ender_lightning);
      projectiles.add(ender_lightning);
    }
    if (dynamiteSafe) {
      ItemProjectileTNT dynamite_safe = new ItemProjectileTNT(6, ExplosionType.BLOCKSAFE);
      ItemRegistry.addItem(dynamite_safe, "dynamite_safe");
      EntityProjectileRegistry.registerModEntity(EntityDynamiteBlockSafe.class, "tntblocksafebolt", 1009);
      EntityDynamiteBlockSafe.renderSnowball = dynamite_safe;
      projectiles.add(dynamite_safe);
      GameRegistry.addShapelessRecipe(new ItemStack(dynamite_safe, 6),
          new ItemStack(Items.GUNPOWDER), new ItemStack(Items.SUGAR), new ItemStack(Items.GUNPOWDER),
          new ItemStack(Items.PAPER), new ItemStack(Items.CLAY_BALL), new ItemStack(Blocks.BROWN_MUSHROOM),
          new ItemStack(Items.FEATHER), new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Blocks.COBBLESTONE));
    }
    if (magicNet) {
      ItemProjectileMagicNet magic_net = new ItemProjectileMagicNet();
      ItemRegistry.addItem(magic_net, "magic_net");
      EntityMagicNetEmpty.renderSnowball = magic_net;
      EntityMagicNetFull.renderSnowball = magic_net;
      EntityProjectileRegistry.registerModEntity(EntityMagicNetFull.class, "magicnetfull", 1011);
      EntityProjectileRegistry.registerModEntity(EntityMagicNetEmpty.class, "magicnetempty", 1012);
      projectiles.add(magic_net);
    }
    if (dynamiteMining) {
      ItemProjectileTNT dynamite_mining = new ItemProjectileTNT(6, ExplosionType.MINING);
      ItemRegistry.addItem(dynamite_mining, "dynamite_mining");
      EntityProjectileRegistry.registerModEntity(EntityDynamiteMining.class, "tntminingbolt", 1010);
      EntityDynamiteMining.renderSnowball = dynamite_mining;
      projectiles.add(dynamite_mining);
      GameRegistry.addShapelessRecipe(new ItemStack(dynamite_mining, 6),
          new ItemStack(Items.GUNPOWDER), new ItemStack(Items.IRON_INGOT),
          new ItemStack(Items.GUNPOWDER), new ItemStack(Items.PAPER),
          new ItemStack(Items.CLAY_BALL), new ItemStack(Blocks.RED_MUSHROOM),
          new ItemStack(Items.FEATHER), new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.NETHERBRICK));
    }
    if (enderBombsEnabled) {
      ItemProjectileTNT ender_tnt_1 = new ItemProjectileTNT(1, ExplosionType.NORMAL);
      ItemProjectileTNT ender_tnt_2 = new ItemProjectileTNT(2, ExplosionType.NORMAL);
      ItemProjectileTNT ender_tnt_3 = new ItemProjectileTNT(3, ExplosionType.NORMAL);
      ItemProjectileTNT ender_tnt_4 = new ItemProjectileTNT(4, ExplosionType.NORMAL);
      ItemProjectileTNT ender_tnt_5 = new ItemProjectileTNT(5, ExplosionType.NORMAL);
      ItemProjectileTNT ender_tnt_6 = new ItemProjectileTNT(6, ExplosionType.NORMAL);
      ItemRegistry.addItem(ender_tnt_1, "ender_tnt_1");
      ItemRegistry.addItem(ender_tnt_2, "ender_tnt_2");
      ItemRegistry.addItem(ender_tnt_3, "ender_tnt_3");
      ItemRegistry.addItem(ender_tnt_4, "ender_tnt_4");
      ItemRegistry.addItem(ender_tnt_5, "ender_tnt_5");
      ItemRegistry.addItem(ender_tnt_6, "ender_tnt_6");
      EntityProjectileRegistry.registerModEntity(EntityDynamite.class, "tntbolt", 1007);
      EntityDynamite.renderSnowball = ender_tnt_1;
      projectiles.add(ender_tnt_1);
      projectiles.add(ender_tnt_2);
      projectiles.add(ender_tnt_3);
      projectiles.add(ender_tnt_4);
      projectiles.add(ender_tnt_5);
      projectiles.add(ender_tnt_6);
      //first the basic recipes
      GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_1, 12), new ItemStack(Blocks.TNT), new ItemStack(Items.PAPER), new ItemStack(Items.CLAY_BALL), new ItemStack(Items.ENDER_PEARL));
      GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_2), new ItemStack(ender_tnt_1), new ItemStack(ender_tnt_1), new ItemStack(Items.CLAY_BALL));
      GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_2), new ItemStack(ender_tnt_2), new ItemStack(Items.CLAY_BALL));
      GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_3), new ItemStack(Items.CLAY_BALL));
      GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_5), new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_4), new ItemStack(Items.CLAY_BALL));
      GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_6), new ItemStack(ender_tnt_5), new ItemStack(ender_tnt_5), new ItemStack(Items.CLAY_BALL));
      //default recipes are added already insice the IRecipe
      GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_1), new ItemStack(ender_tnt_1), new ItemStack(ender_tnt_1), new ItemStack(ender_tnt_1), new ItemStack(Items.CLAY_BALL));
      //two 3s is four 2s
      GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_2), new ItemStack(ender_tnt_2), new ItemStack(ender_tnt_2), new ItemStack(ender_tnt_2), new ItemStack(Items.CLAY_BALL));
      //four 3s is two 4s is one 5
      GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_5), new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_3), new ItemStack(Items.CLAY_BALL));
      GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_6), new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_4), new ItemStack(Items.CLAY_BALL));
       
    }
  }
  @Override
  public void onPostInit() {
    for (BaseItemProjectile item : projectiles) {
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(item, new BehaviorProjectileThrowable(item));
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    magicNet = config.getBoolean("MonsterBall", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    dynamiteSafe = config.getBoolean("DynamiteSafe", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    dynamiteMining = config.getBoolean("DynamiteMining", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableEnderBlaze = config.getBoolean("EnderBlaze", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableEnderDungeonFinder = config.getBoolean("EnderDungeonFinder", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enderFishing = config.getBoolean("EnderFishing", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enderSnow = config.getBoolean("EnderSnow", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enderWool = config.getBoolean("EnderWool", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enderTorch = config.getBoolean("EnderTorch", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enderLightning = config.getBoolean("EnderLightning", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enderWater = config.getBoolean("EnderWater", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enderBombsEnabled = config.getBoolean("EnderBombs", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
}
