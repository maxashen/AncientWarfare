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
package shadowmage.ancient_warfare.common.soldiers.ai.objectives;

import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.soldiers.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.soldiers.ai.tasks.AIAttackTarget;
import shadowmage.ancient_warfare.common.soldiers.ai.tasks.AIChooseAttackTarget;
import shadowmage.ancient_warfare.common.soldiers.ai.tasks.AIMoveToTarget;
import shadowmage.ancient_warfare.common.soldiers.helpers.NpcTargetHelper;
import shadowmage.ancient_warfare.common.soldiers.helpers.targeting.AIAggroEntry;
import shadowmage.ancient_warfare.common.config.Config;

public class AIAttackTargets extends NpcAIObjective
{

int minRange = 20;
int maxRange = 20;
/**
 * @param npc
 * @param maxPriority
 */
public AIAttackTargets(NpcBase npc, int maxPriority, int minRange, int maxRange)
  {
  super(npc, maxPriority);
  this.minRange = minRange;
  this.maxRange = maxRange;
  }

@Override
public void addTasks()
  {
  this.aiTasks.add(new AIChooseAttackTarget(npc, maxRange));
  this.aiTasks.add(new AIMoveToTarget(npc, 1.f, true));  
  this.aiTasks.add(new AIAttackTarget(npc));
  }

@Override
public void updatePriorityTick()
  {  
  if(npc.targetHelper.areTargetsInRange(NpcTargetHelper.TARGET_ATTACK, maxRange))
    {
    if(this.currentPriority<this.maxPriority)
      {
      this.currentPriority++;
      }
    }
  else
    {
    this.currentPriority = 0;
    }
  }

@Override
public void onRunningTick()
  {
  
  }

@Override
public void onObjectiveStart()
  {
  
  }


}
