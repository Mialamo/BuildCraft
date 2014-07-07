/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.robots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.common.util.ForgeDirection;

import buildcraft.api.core.IInvSlot;
import buildcraft.api.robots.AIRobot;
import buildcraft.api.robots.EntityRobotBase;
import buildcraft.core.inventory.ITransactor;
import buildcraft.core.inventory.InventoryIterator;
import buildcraft.core.inventory.Transactor;
import buildcraft.core.inventory.filters.IStackFilter;
import buildcraft.core.inventory.filters.StatementParameterStackFilter;
import buildcraft.silicon.statements.ActionStationProvideItems;
import buildcraft.transport.gates.ActionIterator;
import buildcraft.transport.gates.ActionSlot;

public class AIRobotLoad extends AIRobot {

	private IStackFilter filter;
	private int waitedCycles = 0;

	public AIRobotLoad(EntityRobotBase iRobot) {
		super(iRobot);
	}

	public AIRobotLoad(EntityRobotBase iRobot, IStackFilter iFilter) {
		super(iRobot);

		filter = iFilter;
	}

	@Override
	public void update() {
		waitedCycles++;

		if (waitedCycles > 40) {
			doLoad();
			terminate();
		}
	}

	private void doLoad() {
		if (robot.getDockingStation() != null) {
			DockingStation station = (DockingStation) robot.getDockingStation();

			ItemStack itemFound = null;

			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				TileEntity nearbyTile = robot.worldObj.getTileEntity(station.pipe.xCoord + dir.offsetX,
						station.pipe.yCoord
								+ dir.offsetY, station.pipe.zCoord + dir.offsetZ);

				if (nearbyTile != null && nearbyTile instanceof IInventory) {
					IInventory tileInventory = (IInventory) nearbyTile;
					ITransactor robotTransactor = Transactor.getTransactorFor(robot);

					for (int i = 0; i < robot.getSizeInventory(); ++i) {
						if (robot.getStackInSlot(i) == null) {
							for (IInvSlot slot : InventoryIterator.getIterable(tileInventory, dir.getOpposite())) {
								ItemStack stack = slot.getStackInSlot();

								if (stack != null) {
									boolean allowed = false;

									for (ActionSlot s : new ActionIterator(station.pipe.pipe)) {
										if (s.action instanceof ActionStationProvideItems) {
											StatementParameterStackFilter param = new StatementParameterStackFilter(
													s.parameters);

											if (!param.hasFilter() || param.matches(stack)) {
												allowed = true;
												break;
											}
										}
									}

									if (allowed && filter.matches(stack)) {
										slot.setStackInSlot(null);
										robot.setInventorySlotContents(i, stack);
										break;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public double getEnergyCost() {
		return 2;
	}
}
