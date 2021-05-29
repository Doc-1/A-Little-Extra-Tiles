package com.alet.common.command;

import com.alet.ALET;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class UpdateFontsCommand extends CommandBase {
	
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
	
	@Override
	public String getName() {
		return "alet-updatefont";
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "/alet-updatefont update the fonts in that are in the font folder.";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		//ALET.getFonts();
		System.out.println(ALET.getFonts());
		sender.sendMessage(new TextComponentString("Fonts have been updated"));
	}
	
}
