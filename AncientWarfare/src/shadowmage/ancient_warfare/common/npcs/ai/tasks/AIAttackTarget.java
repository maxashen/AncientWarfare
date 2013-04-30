/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public Licence.
   Please see COPYING for precise license information.

   This file is part of Ancient Warfare.

   Ancient Warfare is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   Ancient Warfare is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with Ancient Warfare.  If not, see <http://www.gnu.org/licenses/>.
 */
package shadowmage.ancient_warfare.common.npcs.ai.tasks;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.ITargetEntry;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAITask;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.Trig;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class AIAttackTarget extends NpcAITask
{

int blockAttackHits = 0;

/**
 * @param npc
 */
public AIAttackTarget(NpcBase npc)
  {
  super(npc);
  // TODO Auto-generated constructor stub
  }

@Override
public void onTick()
  {
  ITargetEntry target = npc.getTarget();
  if(npc.isRidingVehicle())
    {
    if(this.checkIfTargetDead(target))
      {
      npc.targetHelper.removeTarget(target);
      npc.setTargetAW(null);
      } 
    else
      {
      attackTargetMounted(target);
      if(this.checkIfTargetDead(target))
        {
        npc.targetHelper.removeTarget(target);
        npc.setTargetAW(null);
        } 
      }
    }
  else
    {
    if(this.checkIfTargetDead(target))
      {
      npc.targetHelper.removeTarget(target);
      npc.setTargetAW(null);
      } 
    else if(npc.actionTick<=0)
      {
      this.attackTarget(target); 
      if(this.checkIfTargetDead(target))
        {
        npc.targetHelper.removeTarget(target);
        npc.setTargetAW(null);
        } 
      }   
    }    
  }

protected void attackTarget(ITargetEntry target)
  { 
  npc.setActionTicksToMax(); 
  if(!target.isEntityEntry())
    {
//    Config.logDebug("doing block attack");
    blockAttackHits++;    
    npc.swingItem();
    int id = npc.worldObj.getBlockId((int)target.posX(), (int)target.posY(),(int)target.posZ());
    Block block = Block.blocksList[id];
    if(id!=0 && block!=null)
      {
      if(blockAttackHits>=(int)block.getBlockHardness(npc.worldObj, (int)target.posX(), (int)target.posY(),(int)target.posZ()))
        {
        BlockTools.breakBlockAndDrop(npc.worldObj, (int)target.posX(), (int)target.posY(),(int)target.posZ());
        }
      }
    }
  else
    {
//    Config.logDebug("doing entity atack: "+npc.getTarget());
    Entity ent = target.getEntity();
    if(ent!=null)
      {
      npc.swingItem();
      npc.attackEntityAsMob(ent);
      }
    }  
  }

protected void attackTargetMounted(ITargetEntry target)
  {
  VehicleBase vehicle = (VehicleBase) npc.ridingEntity;
  
  //check to see if yaw to target is within the range reachable by just turret rotation
  float yaw = Trig.getYawTowardsTarget(vehicle.posX, vehicle.posZ, target.posX(), target.posZ(), vehicle.rotationYaw);  
  byte s = 0;
  boolean turning = false;
  if(!Trig.isAngleBetween(vehicle.rotationYaw+yaw, vehicle.localTurretRotationHome-vehicle.currentTurretRotationMax-2.f, vehicle.localTurretRotationHome+vehicle.currentTurretRotationMax+2.f))//expand the bounds a bit
    {      
    if(yaw<0)
      {
      s = 1;//left
      }
    else
      {
      s = -1;//right
      }
    turning = true;
//    Config.logDebug("yaw diff to target: "+yaw);
    }
  vehicle.moveHelper.handleMotionInput((byte) 0, s);
  vehicle.firingHelper.handleSoldierTargetInput(target.posX(), target.posY(), target.posZ());
  if(turning)
    {
    return;
    }
  if(vehicle.firingHelper.isAtTarget())
    {
    if(npc.actionTick<=0)
      {
      vehicle.firingHelper.handleFireUpdate();
      this.npc.actionTick = (vehicle.currentReloadTicks + 20);
      vehicle.moveHelper.handleMotionInput((byte)0, (byte)0);
      }    
    }
  else//delay a bit to line up to target 
    {
    this.npc.actionTick = 1;
    } 
  }

protected boolean checkIfTargetDead(ITargetEntry target)
  {
  if(target==null)
    {
    return true;
    }
  if(target.getEntity()!=null)
    {
    if(target.getEntity().isDead)
      {
//      Config.logDebug("target is dead!!");
      return true;
      }
    if(target.getEntity() instanceof EntityLiving)
      {
      EntityLiving liv = (EntityLiving)target.getEntity();
      if(liv.getHealth()<=0)
        {
//        Config.logDebug("target had no health, counting as dead!!");
        return true;
        }
      }
//    Config.logDebug("target not dead!!");
    return false;
    }
  else if(target.getEntity()==null)
    {
    if(npc.worldObj.getBlockId((int)target.posX(), (int)target.posY(),(int)target.posZ())==0)
      {
      return true;
      }
    } 
  return false;
  }

@Override
public boolean shouldExecute()
  {  
  return npc.getTarget()!=null && npc.getTargetType()==TargetType.ATTACK && npc.getDistanceFromTarget(npc.getTarget()) <= npc.targetHelper.getAttackDistance(npc.getTarget());
  }

}
