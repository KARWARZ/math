package com.lothrazar.cyclicmagic.entity.projectile;
import com.lothrazar.cyclicmagic.registry.PotionEffectRegistry;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySnowballBolt extends EntityThrowableDispensable {
  public static Item renderSnowball;
  public EntitySnowballBolt(World worldIn) {
    super(worldIn);
  }
  public EntitySnowballBolt(World worldIn, EntityLivingBase ent) {
    super(worldIn, ent);
  }
  public EntitySnowballBolt(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  @Override
  protected void onImpact(RayTraceResult mop) {
    World world = getEntityWorld();
    if (mop.entityHit != null && mop.entityHit instanceof EntityLivingBase) {
      EntityLivingBase e = (EntityLivingBase) mop.entityHit;
      if (e.isBurning()) {
        e.extinguish();
      }
      else {
        e.addPotionEffect(new PotionEffect(PotionEffectRegistry.snowEffect, 20 * 10));
      }
      float damage = 0;
      if (mop.entityHit instanceof EntityBlaze) {
        damage = 1;
      }
      // do the snowball damage, which should be none. put out the fire
      mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
    }
    BlockPos pos = mop.getBlockPos();
    if (pos == null) { return; } // hasn't happened yet, but..
    //		UtilParticle.spawnParticle(this.worldObj, EnumParticleTypes.SNOWBALL, pos);
    UtilParticle.spawnParticle(world, EnumParticleTypes.SNOW_SHOVEL, pos);
    if (mop.sideHit != null && this.getThrower() instanceof EntityPlayer) {
      world.extinguishFire((EntityPlayer) this.getThrower(), pos, mop.sideHit);
    }
    if (this.isInWater()) {
      BlockPos posWater = this.getPosition();
      if (world.getBlockState(posWater) != Blocks.WATER.getDefaultState()) {
        posWater = null;// look for the closest water source, sometimes it was
        // air and we
        // got ice right above the water if we dont do this check
        if (world.getBlockState(mop.getBlockPos()) == Blocks.WATER.getDefaultState())
          posWater = mop.getBlockPos();
        else if (world.getBlockState(mop.getBlockPos().offset(mop.sideHit)) == Blocks.WATER.getDefaultState())
          posWater = mop.getBlockPos().offset(mop.sideHit);
      }
      if (posWater != null) // rarely happens but it does
      {
        world.setBlockState(posWater, Blocks.ICE.getDefaultState());
      }
    }
    else {
      // on land, so snow?
      BlockPos hit = pos;
      BlockPos hitDown = hit.down();
      BlockPos hitUp = hit.up();
      IBlockState hitState = world.getBlockState(hit);
      if (hitState.getBlock() == Blocks.SNOW_LAYER) {
        setMoreSnow(world, hit);
      } // these other cases do not really fire, i think. unless the entity goes
      // inside a
      // block before despawning
      else if (world.getBlockState(hitDown).getBlock() == Blocks.SNOW_LAYER) {
        setMoreSnow(world, hitDown);
      }
      else if (world.getBlockState(hitUp).getBlock() == Blocks.SNOW_LAYER) {
        setMoreSnow(world, hitUp);
      }
      else if (world.isAirBlock(hit) == false // one below us cannot be
          // air
          && // and we landed at air or replaceable
          world.isAirBlock(hitUp) == true)
      // this.worldObj.getBlockState(this.getPosition()).getBlock().isReplaceable(this.worldObj,
      // this.getPosition()))
      {
        setNewSnow(world, hitUp);
      }
      else if (world.isAirBlock(hit) == false // one below us cannot be
          // air
          && // and we landed at air or replaceable
          world.isAirBlock(hitUp) == true)
      // this.worldObj.getBlockState(this.getPosition()).getBlock().isReplaceable(this.worldObj,
      // this.getPosition()))
      {
        setNewSnow(world, hitUp);
      }
    }
    this.setDead();
  }
  private static void setMoreSnow(World world, BlockPos pos) {
    IBlockState hitState = world.getBlockState(pos);
    int m = hitState.getBlock().getMetaFromState(hitState) + 1;
    if (BlockSnow.LAYERS.getAllowedValues().contains(m + 1)) {
      world.setBlockState(pos,
          Blocks.SNOW_LAYER.getDefaultState().withProperty(BlockSnow.LAYERS, m + 1));
      UtilSound.playSound(world, pos, SoundEvents.BLOCK_SNOW_PLACE, SoundCategory.BLOCKS);
    }
    else {
      setNewSnow(world, pos.up());
    }
  }
  private static void setNewSnow(World world, BlockPos pos) {
    world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState());
    UtilSound.playSound(world, pos, SoundEvents.BLOCK_SNOW_PLACE, SoundCategory.BLOCKS);
  }
}