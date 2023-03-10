package com.lothrazar.cyclicmagic.item.gear;
import java.util.List;
import com.lothrazar.cyclicmagic.ICanToggleOnOff;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

@SuppressWarnings("incomplete-switch")
public class ItemPowerArmor extends ItemArmor implements IHasRecipe, ICanToggleOnOff {
  private static final float SNEAKSPEED = 0.077F;
  public static final String NBT_GLOW = Const.MODID + "_glow";
  public static final String NBT_STEP = Const.MODID + "_step";
  private final static String NBT_STATUS = "onoff";
  public ItemPowerArmor(ArmorMaterial material, EntityEquipmentSlot armorType) {
    super(material, 0, armorType);
  }
  @Override
  public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
    boolean isTurnedOn = this.isOn(itemStack);
    switch (this.armorType) {
      case CHEST:
        if (isTurnedOn)
          setSneakspeed(player);
      break;
      case FEET:
        if (isTurnedOn)
          setLiquidWalk(world, player);
      break;
      case HEAD:
        setGlowing(player, isTurnedOn);
        if(isTurnedOn)
          setNightVision(player);
      break;
      case LEGS:
        setStepHeight(player, isTurnedOn);
      break;
    }
  }
  private void setNightVision(EntityPlayer player) {
    player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 20 * Const.TICKS_PER_SEC, 0));
  }
  private void setSneakspeed(EntityPlayer player) {
    if (player.isSneaking() && player.moveForward > 0) {
      UtilEntity.speedupEntity(player, SNEAKSPEED);
    }
  }
  private void setLiquidWalk(World world, EntityPlayer player) {
    BlockPos belowPos = player.getPosition().down();
    if (world.containsAnyLiquid(new AxisAlignedBB(belowPos)) && world.isAirBlock(player.getPosition()) && player.motionY < 0
        && !player.isSneaking()) {// let them slip down into it when sneaking
      player.motionY = 0;// stop falling
      player.onGround = true; // act as if on solid ground
    }
  }
  public static void setStepHeight(EntityPlayer player, boolean on) {
    player.stepHeight = (on) ? 1.0F : 0.5F;
    player.getEntityData().setBoolean(NBT_STEP, on);
  }
  public static void checkIfLegsOff(EntityPlayer player) {
    Item itemInSlot = UtilPlayer.getItemArmorSlot(player, EntityEquipmentSlot.LEGS);
    if (player.getEntityData().getBoolean(ItemPowerArmor.NBT_STEP) &&
        (itemInSlot == null || !(itemInSlot instanceof ItemPowerArmor))) {
      //turn it off once, from the message
      setStepHeight(player, false);
    }
  }
  public static void checkIfHelmOff(EntityPlayer player) {
    Item itemInSlot = UtilPlayer.getItemArmorSlot(player, EntityEquipmentSlot.HEAD);
    if (player.getEntityData().getBoolean(ItemPowerArmor.NBT_GLOW) &&
        (itemInSlot == null || !(itemInSlot instanceof ItemPowerArmor))) {
      //turn it off once, from the message
      setGlowing(player, false);
    }
  }
  public static void setGlowing(EntityPlayer player, boolean hidden) {
    player.setGlowing(hidden);//hidden means dont render
    //flag it so we know the purple glow was from this item, not something else
    player.getEntityData().setBoolean(NBT_GLOW, hidden);
  }
  @Override
  public void addInformation(ItemStack held, EntityPlayer player, List<String> list, boolean par4) {
    list.add(UtilChat.lang(this.getUnlocalizedName() + ".tooltip"));
    String onoff = this.isOn(held) ? "on" : "off";
    list.add(UtilChat.lang("item.cantoggle.tooltip.info") + UtilChat.lang("item.cantoggle.tooltip." + onoff));
  }
  @Override
  public void addRecipe() {
    switch (this.armorType) {
      case CHEST:
        GameRegistry.addShapedRecipe(new ItemStack(this),
            "p p", "oio", "ooo",
            'i', new ItemStack(Items.CHAINMAIL_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE),
            'o', Blocks.OBSIDIAN,
            'p', new ItemStack(Items.DYE, 1, EnumDyeColor.PURPLE.getDyeDamage()));
      break;
      case FEET:
        GameRegistry.addShapedRecipe(new ItemStack(this),
            "   ", "p p", "oio",
            'i', new ItemStack(Items.CHAINMAIL_BOOTS, 1, OreDictionary.WILDCARD_VALUE),
            'o', Blocks.OBSIDIAN,
            'p', new ItemStack(Items.DYE, 1, EnumDyeColor.PURPLE.getDyeDamage()));
      break;
      case HEAD:
        GameRegistry.addShapedRecipe(new ItemStack(this),
            "oio", "p p", "   ",
            'i', new ItemStack(Items.CHAINMAIL_HELMET, 1, OreDictionary.WILDCARD_VALUE),
            'o', Blocks.OBSIDIAN,
            'p', new ItemStack(Items.DYE, 1, EnumDyeColor.PURPLE.getDyeDamage()));
      break;
      case LEGS:
        GameRegistry.addShapedRecipe(new ItemStack(this),
            "oio", "p p", "o o",
            'i', new ItemStack(Items.CHAINMAIL_LEGGINGS, 1, OreDictionary.WILDCARD_VALUE),
            'o', Blocks.OBSIDIAN,
            'p', new ItemStack(Items.DYE, 1, EnumDyeColor.PURPLE.getDyeDamage()));
      break;
    }
  }
  public void toggleOnOff(ItemStack held) {
    NBTTagCompound tags = UtilNBT.getItemStackNBT(held);
    int vnew = isOn(held) ? 0 : 1;
    tags.setInteger(NBT_STATUS, vnew);
  }
  public boolean isOn(ItemStack held) {
    NBTTagCompound tags = UtilNBT.getItemStackNBT(held);
    if (tags.hasKey(NBT_STATUS) == false) { return true;//default for newlycrafted//legacy items
    }
    return tags.getInteger(NBT_STATUS) == 1;
  }
}
