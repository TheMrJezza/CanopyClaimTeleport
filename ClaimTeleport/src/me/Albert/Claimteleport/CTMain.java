package me.Albert.Claimteleport;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import me.Albert.Claimteleport.commands.CTPReloadCMD;
import me.Albert.Claimteleport.commands.ClaimTPCMD;

public class CTMain extends JavaPlugin implements Listener {
	private PluginManager pm = Bukkit.getPluginManager();
	private static CTMain instance;
	
	private static HashMap<UUID, BukkitTask> countdowns = new HashMap<>();

	public void onEnable() {
		if (!pm.isPluginEnabled("GriefPrevention")) {
			info("§cGriefPrevention hasn't loaded! (Is it installed?)");
			info("ClaimTeleport will be Disabled.");
			pm.disablePlugin(this);
			return;
		}
		instance = this;
		Settings.reloadSettings();
		getCommand("claimteleport").setExecutor(new ClaimTPCMD());
		getCommand("ctpreload").setExecutor(new CTPReloadCMD());
		pm.registerEvents(this, this);
		new Metrics(this);
		info("Plugin Loaded Successfully!");
	}

	public void onDisable() {
		getCommand("claimteleport").setExecutor(null);
		getCommand("ctpreload").setExecutor(null);
		HandlerList.unregisterAll((Plugin) this);
		instance = null;
	}

	public void info(String input) {
		Bukkit.getConsoleSender().sendMessage("§7[§aClaimTeleport§7] §e" + input);
	}

	public static CTMain getInstance() {
		return instance;
	}

	public void playSound(Player player, Sound sound) {
		if (Settings.isSoundEnabled() && sound != null && player != null)
			player.playSound(player.getLocation(), sound, 10.0F, 1.1F);
	}

	public static HashMap<UUID, BukkitTask> getCountdowns() {
		return countdowns;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent evt) {
		BukkitTask task = countdowns.remove(evt.getPlayer().getUniqueId());
		if (task != null) {
			task.cancel();
			evt.getPlayer().sendMessage("§cClaim Teleport has been cancelled because you moved!");
		}
	}
}
