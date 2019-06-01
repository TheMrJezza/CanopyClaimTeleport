package com.canopymc.claim_teleport;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class Settings {
	private static File file;
	private static YamlConfiguration settings;

	// Doubles
	private static double COUNTDOWN_DELAY;

	// Booleans
	private static boolean ENABLE_SOUND, CANCEL_ON_MOVE;

	// Messages
	private static String TELEPORT_SUCCESS, COUNTDOWN_INITIALIZED, COUNTDOWN_CANCELLED, INVALID_ARGUMENT,
			INDEX_OUT_OF_BOUNDS;

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

	public static void updateSettings() {
		file = new File(CTMain.getInstance().getDataFolder(), "Settings.yml");
		if (!file.exists()) {
			CTMain.getInstance().saveResource("Settings.yml", true);
		}
		settings = YamlConfiguration.loadConfiguration(file);
		COUNTDOWN_DELAY = settings.getDouble("TeleportDelay.TimeInSeconds", 0);

		ENABLE_SOUND = settings.getBoolean("EnableSound", true);
		CANCEL_ON_MOVE = settings.getBoolean("TeleportDelay.CancelOnMove", COUNTDOWN_DELAY > 0);
		//
	}

	public static void reloadSettings() {

		teleportSuccess = getMessage("TeleportSuccess", teleportSuccess);
		teleporting = getMessage("Teleporting", teleporting);

	}

	private static String getMessage(String key, String defaultValue) {
		return ChatColor.translateAlternateColorCodes('&', settings.getString("Messages." + key, defaultValue));
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