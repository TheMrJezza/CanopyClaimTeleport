package me.Albert.Claimteleport.commands;

import java.util.UUID;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.google.common.primitives.Ints;

import me.Albert.Claimteleport.CTMain;
import me.Albert.Claimteleport.Settings;
import me.Albert.Claimteleport.Sounds;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;

public class ClaimTPCMD implements CommandExecutor {

	public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args) {
		if (!(cs instanceof Player)) {
			if (cs instanceof ConsoleCommandSender) {
				CTMain.getInstance().info("§cOnly ingame players can excute this command");
			} else
				cs.sendMessage("§cOnly ingame players can excute this command");
			return true;
		}
		Player player = (Player) cs;
		if (!cs.hasPermission("ctp.use")) {
			cs.sendMessage(Settings.noPermission());
			CTMain.getInstance().playSound(player, Sounds.VILLAGER_NO.getBukkitSound());
			return true;
		}
		if (args.length < 1) {
			PlayerData data = GriefPrevention.instance.dataStore.getPlayerData(player.getUniqueId());
			Vector<Claim> claims = data.getClaims();
			if (claims.isEmpty()) {
				cs.sendMessage("§cYou haven't made any claims yet!");
				return true;
			}
			StringBuilder builder = new StringBuilder("§e " + claims.size() + " Available Claims:");

			for (int i = 0; i < claims.size(); i++) {
				Location lesser = claims.get(i).getLesserBoundaryCorner();
				builder.append(String.format(" - %s (near x: %S z:%s world: %s)", i, lesser.getBlockX(),
						lesser.getBlockZ(), lesser.getWorld().getName()));
			}

			cs.sendMessage(builder.toString());
			return true;
		}
		if (args.length >= 2 && cs.hasPermission("ctp.other")) {
			Player target = Bukkit.getPlayer(args[1]);
			UUID targetUUID = null;
			if (target == null) {
				for (OfflinePlayer offPlayer : Bukkit.getOfflinePlayers()) {
					if (offPlayer.getName().equalsIgnoreCase(args[1])) {
						targetUUID = offPlayer.getUniqueId();
						break;
					}
				}
			} else
				targetUUID = target.getUniqueId();
			if (targetUUID == null) {
				cs.sendMessage("§cThat players Claim Data couldn't be found!");
				CTMain.getInstance().playSound(player, Sounds.VILLAGER_NO.getBukkitSound());
				return true;
			}
			process(player, args[0], targetUUID);
		}
		process(player, args[0], null);
		return true;
	}

	private void process(Player player, String arg, UUID override) {
		String message = "§cThat Player hasn't made any claims yet!";
		if (override == null) {
			override = player.getUniqueId();
			message = "§cYou haven't made any claims yet!";
		}
		PlayerData data = GriefPrevention.instance.dataStore.getPlayerData(override);
		Vector<Claim> claims = data.getClaims();
		if (claims.isEmpty()) {
			player.sendMessage(message);
			return;
		}
		Integer claimNum = Ints.tryParse(arg);
		if (claimNum == null) {
			player.sendMessage(Settings.notNumeric());
			CTMain.getInstance().playSound(player, Sounds.VILLAGER_NO.getBukkitSound());
			return;
		}
		if (claimNum < 0 || claimNum >= claims.size()) {
			player.sendMessage(Settings.invalidClaimID());
			CTMain.getInstance().playSound(player, Sounds.VILLAGER_NO.getBukkitSound());
			return;
		}

		teleport(player, claims.get(claimNum));
	}

	public void teleport(Player player, Claim claim) {
		BukkitTask current = CTMain.getCountdowns().remove(player.getUniqueId());
		if (current != null) {
			current.cancel();
		}
		Location greater = claim.getGreaterBoundaryCorner(), lesser = claim.getLesserBoundaryCorner();
		int x1 = greater.getBlockX(), x2 = lesser.getBlockX();
		int z1 = greater.getBlockZ(), z2 = lesser.getBlockZ();
		int x = (x1 - x2) / 2 + x2, z = (z1 - z2) / 2 + z2, y = lesser.getWorld().getHighestBlockYAt(x, z);

		Location result = new Location(lesser.getWorld(), x, y, z);

		CTMain.getCountdowns().put(player.getUniqueId(), new BukkitRunnable() {
			public void run() {
				result.getChunk(); // Load the chunk
				player.teleport(result);
				CTMain.getCountdowns().remove(player.getUniqueId());
				new BukkitRunnable() {
					public void run() {
						CTMain.getInstance().playSound(player, Sounds.ENDERMAN_TELEPORT.getBukkitSound());
					}
				}.runTaskLater(CTMain.getInstance(), 2l);
			}
		}.runTaskLater(CTMain.getInstance(), 20 * Settings.getDelay()));
	}
}