package com.lothrazar.cyclicmagic.registry;
import java.lang.reflect.Field;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilReflection;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityVillager;

public class ReflectionRegistry {
  public static Field fieldCareer;// = UtilReflection.getPrivateField("careerId", "field_175563_bv", merchant);
  
  public static IAttribute horseJumpStrength = null;
  public static void register() {
    
    fieldCareer = UtilReflection.getPrivateField("careerId", "field_175563_bv", EntityVillager.class);
    
    
    // version 1.1.0
    // new item for speed
    // new item for jump
    for (Field f : EntityHorse.class.getDeclaredFields()) {
      try {
        // if(f.get(null) instanceof IAttribute)
        // interface net.minecraft.entity.ai.attributes.IAttribute
        // field_76425_a
        if (f.getName().equals("horseJumpStrength") || f.getName().equals("JUMP_STRENGTH") || f.getName().equals("field_110270_bw") || "interface net.minecraft.entity.ai.attributes.IAttribute".equals(f.getType() + "")) {
          f.setAccessible(true);
          // save pointer to the obj so we can reference it later
          horseJumpStrength = (IAttribute) f.get(null);
          break;
        }
      }
      catch (Exception e) {
        ModCyclic.logger.error("Severe error, please report this to the mod author:");
        ModCyclic.logger.error(e.getStackTrace().toString());
      }
    }
    if (horseJumpStrength == null) {
      ModCyclic.logger.error(Const.MODID + ":horseJumpStrength: Error - field not found using reflection");
    }
  }
}
