package com.lothrazar.cyclicmagic.entity.projectile;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityBlazeBolt extends EntityThrowableDispensable {
  public static Item renderSnowball;
  public static final int fireSeconds = 3;
  public static final boolean damageEntityOnHit = true;
  public EntityBlazeBolt(World worldIn) {
    super(worldIn);
  }
  public EntityBlazeBolt(World worldIn, EntityLivingBase ent) {
    super(worldIn, ent);
  }
  public EntityBlazeBolt(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  @Override
  protected void onImpact(RayTraceResult mop) {
    if (mop.entityHit != null) {
      // do the snowball damage, which should be none. put out the fire
      if (damageEntityOnHit) {
        float damage = 1;
        if (mop.entityHit instanceof EntityBlaze) {
          damage = 0;
        }
        mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
      }
      if (mop.entityHit.isBurning() == false && fireSeconds > 0) {
        mop.entityHit.setFire(fireSeconds);
      }
    }
    BlockPos pos = mop.getBlockPos();
    BlockPos offset = null;
    if (pos == null) { return; } // hasn't happened yet, but..
    ArrayList<Block> waterBoth = new ArrayList<Block>();
    waterBoth.add(Blocks.FLOWING_WATER);
    waterBoth.add(Blocks.WATER);
    World world = this.getEntityWorld();
    if (mop.sideHit != null && this.getThrower() instanceof EntityPlayer) {
      world.extinguishFire((EntityPlayer) this.getThrower(), pos, mop.sideHit);
      offset = mop.getBlockPos().offset(mop.sideHit);
    }
    // Block hitBlock = this.worldObj.getBlockState(pos).getBlock();
    if (mop.sideHit != null)
      offset = pos.offset(mop.sideHit);
    ArrayList<BlockPos> toSetFire = new ArrayList<BlockPos>();
    if (this.isInWater() == false) {
      if (world.isAirBlock(pos)) {
        toSetFire.add(pos);
        // turn flowing water into solid
        world.setBlockState(pos, Blocks.FIRE.getDefaultState());
      }
      if (world.isAirBlock(pos.offset(EnumFacing.EAST))) {
        toSetFire.add(pos.offset(EnumFacing.EAST));
      }
      if (world.isAirBlock(pos.offset(EnumFacing.NORTH))) {
        toSetFire.add(pos.offset(EnumFacing.NORTH));
      }
      if (world.isAirBlock(pos.offset(EnumFacing.SOUTH))) {
        toSetFire.add(pos.offset(EnumFacing.SOUTH));
      }
      if (world.isAirBlock(pos.offset(EnumFacing.WEST))) {
        toSetFire.add(pos.offset(EnumFacing.WEST));
      }
      if (world.isAirBlock(pos.offset(EnumFacing.UP))) {
        toSetFire.add(pos.offset(EnumFacing.UP));
      }
      if (offset != null && world.isAirBlock(offset)) {
        toSetFire.add(offset);
        if (world.isAirBlock(offset.offset(EnumFacing.EAST))) {
          toSetFire.add(offset.offset(EnumFacing.EAST));
        }
        if (world.isAirBlock(offset.offset(EnumFacing.NORTH))) {
          toSetFire.add(offset.offset(EnumFacing.NORTH));
        }
        if (world.isAirBlock(offset.offset(EnumFacing.SOUTH))) {
          toSetFire.add(offset.offset(EnumFacing.SOUTH));
        }
        if (world.isAirBlock(offset.offset(EnumFacing.WEST))) {
          toSetFire.add(offset.offset(EnumFacing.WEST));
        }
        if (world.isAirBlock(offset.offset(EnumFacing.UP))) {
          toSetFire.add(offset.offset(EnumFacing.UP));
        }
      }
      for (BlockPos p : toSetFire) {
        world.setBlockState(p, Blocks.FIRE.getDefaultState());
        world.spawnParticle(EnumParticleTypes.FLAME, p.up().getX(), p.up().getY(), p.up().getZ(), 0, 0, 0);
      }
    }
    this.setDead();
  }
}