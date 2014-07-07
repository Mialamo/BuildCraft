/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.robots.boards;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import buildcraft.api.boards.RedstoneBoardRobot;
import buildcraft.api.boards.RedstoneBoardRobotNBT;
import buildcraft.api.robots.EntityRobotBase;
import buildcraft.core.robots.EntityRobot;
import buildcraft.core.utils.NBTUtils;
import buildcraft.core.utils.StringUtils;

public final class BoardRobotPickerNBT extends RedstoneBoardRobotNBT {

	public static BoardRobotPickerNBT instance = new BoardRobotPickerNBT();

	public IIcon icon;

	private BoardRobotPickerNBT() {
	}

	@Override
	public String getID() {
		return "buildcraft:boardRobotPicker";
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
		list.add(StringUtils.localize("buildcraft.boardRobotPicker"));

		NBTTagCompound nbt = NBTUtils.getItemData(stack);
		list.add(StringUtils.localize("buildcraft.boardDetail.range") + ": " + nbt.getInteger("range"));
	}

	@Override
	public RedstoneBoardRobot create(NBTTagCompound nbt, EntityRobotBase object) {
		return new BoardRobotPicker(object, nbt);
	}

	@Override
	public IIcon getIcon(NBTTagCompound nbt) {
		return icon;
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {
		icon = iconRegister.registerIcon("buildcraft:board_green");
	}

	@Override
	public void createRandomBoard(NBTTagCompound nbt) {
		int range = (int) Math.floor(nextFloat(10) * 500) + 10;
		nbt.setInteger("range", range);
	}

	@Override
	public void createDefaultBoard(NBTTagCompound nbt) {
		nbt.setInteger("range", 250);
	}

	@Override
	public ResourceLocation getRobotTexture() {
		return EntityRobot.ROBOT_TRANSPORT;
	}
}
