package com.lothrazar.cyclicmagic.entity.projectile;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops.HarestCropsConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityHarvestBolt extends EntityThrowableDispensable {
  public static Item renderSnowball;
  public static int range_main = 6;
  public static int range_offset = 4;
  public EntityHarvestBolt(World worldIn) {
    super(worldIn);
  }
  public EntityHarvestBolt(World worldIn, EntityLivingBase ent) {
    super(worldIn, ent);
  }
  public EntityHarvestBolt(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  @Override
  protected void onImpact(RayTraceResult mop) {
    if (this.getThrower() != null && mop.sideHit != null) {
      BlockPos offset = mop.getBlockPos().offset(mop.sideHit);
      HarestCropsConfig conf = new HarestCropsConfig();
      // it harvests a horizontal slice each time
      World world = getEntityWorld();
      UtilHarvestCrops.harvestArea(world, mop.getBlockPos(), range_main, conf);
      UtilHarvestCrops.harvestArea(world, offset, range_main, conf);
      UtilHarvestCrops.harvestArea(world, offset.up(), range_offset, conf);
      UtilHarvestCrops.harvestArea(world, offset.down(), range_offset, conf);
    }
    this.setDead();
  }
}