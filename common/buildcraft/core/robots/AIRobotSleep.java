/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.robots;

import buildcraft.api.robots.AIRobot;
import buildcraft.api.robots.EntityRobotBase;
import buildcraft.silicon.statements.ActionRobotWakeUp;
import buildcraft.transport.gates.ActionIterator;
import buildcraft.transport.gates.ActionSlot;

public class AIRobotSleep extends AIRobot {

	private static final int SLEEPING_TIME = 60 * 20;
	private int sleptTime = 0;

	public AIRobotSleep(EntityRobotBase iRobot) {
		super(iRobot);
	}

	@Override
	public void preempt(AIRobot ai) {
		for (ActionSlot s : new ActionIterator(((DockingStation) robot.getLinkedStation()).pipe.pipe)) {
			if (s.action instanceof ActionRobotWakeUp) {
				terminate();
			}
		}
	}

	@Override
	public void update() {
		sleptTime++;

		if (sleptTime > SLEEPING_TIME) {
			terminate();
		}
	}

	@Override
	public double getEnergyCost() {
		return 0.01;
	}
}
