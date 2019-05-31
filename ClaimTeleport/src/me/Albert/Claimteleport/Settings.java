package me.Albert.Claimteleport;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class Settings {
	private static File SettingsFile = new File(CTMain.getInstance().getDataFolder(), "Settings.yml");
	private static YamlConfiguration config;
	private static String teleportSuccess = "&aTeleporting...";
	private static String teleporting;
	private static String playermoved;
	private static String incooldown;
	private static String noPermission;
	private static String invalidClaimID;
	private static String notNumeric;
	private static String nullInput;
	private static int delay;
	private static boolean sound;
	private static String invalidplayer;

	public static void reloadSettings() {
		if (!SettingsFile.exists()) {
			CTMain.getInstance().saveResource("Settings.yml", true);
		}

		teleportSuccess = getMessage("TeleportSuccess", teleportSuccess);
		teleporting = getMessage("Teleporting", teleporting);

		config = YamlConfiguration.loadConfiguration(SettingsFile);
		delay = config.getInt("delay");
		playermoved = ChatColor.translateAlternateColorCodes('&', config.getString("Messages.playermoved"));
		incooldown = ChatColor.translateAlternateColorCodes('&', config.getString("Messages.incooldown"));
		teleporting = ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Teleporting"));
		teleportSuccess = getMessage("TeleportSuccess", teleportSuccess);
		//setNoPermission(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.NoPermission")));
		invalidClaimID = ChatColor.translateAlternateColorCodes('&', config.getString("Messages.InvalidClaimID"));
		notNumeric = ChatColor.translateAlternateColorCodes('&', config.getString("Messages.NotNumeric"));
		nullInput = ChatColor.translateAlternateColorCodes('&', config.getString("Messages.NullInput"));
		sound = config.getBoolean("Sound.Enabled");
		invalidplayer = ChatColor.translateAlternateColorCodes('&', config.getString("Messages.InvalidPlayer"));

	}

	private static String getMessage(String key, String defaultValue) {
		return ChatColor.translateAlternateColorCodes('&', config.getString("Messages." + key, defaultValue));
	}

	public static boolean isSoundEnabled() {
		return sound;
	}

	public static String invalidClaimID() {
		return invalidClaimID;
	}

	public static String noPermission() {
		return noPermission;
	}

	public static String notNumeric() {
		return notNumeric;
	}

	public static void setNotNumeric(String notNumeric) {
		Settings.notNumeric = notNumeric;
	}

	public static int getDelay() {
		return delay;
	}
}