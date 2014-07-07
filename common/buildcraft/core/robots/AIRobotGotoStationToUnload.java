/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.robots;

import net.minecraftforge.common.util.ForgeDirection;

import buildcraft.api.core.IBox;
import buildcraft.api.core.IInvSlot;
import buildcraft.api.robots.AIRobot;
import buildcraft.api.robots.EntityRobotBase;
import buildcraft.core.inventory.InventoryIterator;
import buildcraft.silicon.statements.ActionStationRequestItems;
import buildcraft.transport.Pipe;
import buildcraft.transport.gates.ActionIterator;
import buildcraft.transport.gates.ActionSlot;

public class AIRobotGotoStationToUnload extends AIRobot {

	public boolean found = false;

	private IBox box;

	public AIRobotGotoStationToUnload(EntityRobotBase iRobot) {
		super(iRobot);
	}

	public AIRobotGotoStationToUnload(EntityRobotBase iRobot, IBox iBox) {
		super(iRobot);

		box = iBox;

	}

	@Override
	public void start() {
		startDelegateAI(new AIRobotSearchAndGotoStation(robot, new StationInventory(), box));
	}

	@Override
	public void delegateAIEnded(AIRobot ai) {
		if (ai instanceof AIRobotSearchAndGotoStation) {
			found = ((AIRobotSearchAndGotoStation) ai).targetStation != null;

			terminate();
		}
	}

	private class StationInventory implements IStationFilter {
		@Override
		public boolean matches(DockingStation station) {
			Pipe pipe = station.pipe.pipe;

			for (IInvSlot robotSlot : InventoryIterator.getIterable(robot, ForgeDirection.UNKNOWN)) {
				if (robotSlot.getStackInSlot() == null) {
					continue;
				}

				for (ActionSlot s : new ActionIterator(station.pipe.pipe)) {
					if (s.action instanceof ActionStationRequestItems) {
						if (((ActionStationRequestItems) s.action).insert(station, (EntityRobot) robot, s, robotSlot, false)) {
							return true;
						}
					}
				}
			}

			return false;
		}
	}

}
