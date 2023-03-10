package com.lothrazar.cyclicmagic.entity.projectile;
import com.lothrazar.cyclicmagic.world.ExplosionBlockSafe;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDynamiteBlockSafe extends EntityThrowableDispensable {
  
  public static Item renderSnowball;
  private float explosionLevel;
  public EntityDynamiteBlockSafe(World worldIn) {
    super(worldIn);
    this.explosionLevel = 6;
  }
  public EntityDynamiteBlockSafe(World worldIn, int explos) {
    super(worldIn);
    this.explosionLevel = explos;
  }
  public EntityDynamiteBlockSafe(World worldIn, EntityLivingBase ent, int strength) {
    super(worldIn, ent);
    this.explosionLevel = strength;
  }
  public EntityDynamiteBlockSafe(World worldIn, int strength, double x, double y, double z) {
    super(worldIn, x, y, z);
    this.explosionLevel = strength;
  }
  @Override
  protected void onImpact(RayTraceResult mop) {
 
        ExplosionBlockSafe explosion = new ExplosionBlockSafe(this.getEntityWorld(), this.getThrower(), posX, posY, posZ, explosionLevel, false, true);
        explosion.doExplosionA();
        explosion.doExplosionB(false);

    this.setDead();
  }
}