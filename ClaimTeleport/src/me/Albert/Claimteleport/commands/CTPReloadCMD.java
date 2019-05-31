package me.Albert.Claimteleport.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.Albert.Claimteleport.Settings;

public class CTPReloadCMD implements CommandExecutor {

	public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args) {
		if (!cs.hasPermission("ctp.reload")) {
			cs.sendMessage(Settings.noPermission());
			return true;
		}
		Settings.reloadSettings();
		cs.sendMessage("§aConfig Reloaded");
		return true;
	}
}