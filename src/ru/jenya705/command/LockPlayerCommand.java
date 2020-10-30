package ru.jenya705.command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import ru.jenya705.Loader;
import ru.jenya705.parsing.DataParser;
import ru.jenya705.parsing.DataParser.ParseData;

public class LockPlayerCommand implements CommandExecutor, TabExecutor, Listener {
	
	public static class PlayerWithOldLocation{
		private Player player;
		private Location oldLocation;
		private GameMode oldGameMode;
		public PlayerWithOldLocation(Player player, Location oldLocation, GameMode gameMode) {
			setPlayer(player);
			setOldLocation(oldLocation);
			setOldGameMode(gameMode);
		}
		public Player getPlayer() {
			return player;
		}
		public void setPlayer(Player player) {
			this.player = player;
		}
		public Location getOldLocation() {
			return oldLocation;
		}
		public void setOldLocation(Location oldLocation) {
			this.oldLocation = oldLocation;
		}
		public GameMode getOldGameMode() {
			return oldGameMode;
		}
		public void setOldGameMode(GameMode oldGameMode) {
			this.oldGameMode = oldGameMode;
		}
	}
	public static class GM3Runnable extends BukkitRunnable{
		private PlayerWithOldLocation[] players;
		private UUID uuid;
		public GM3Runnable(PlayerWithOldLocation[] players, UUID uuid) {
			setPlayers(players);
			setUuid(uuid);
		}
		@Override
		public void run() {
			for (PlayerWithOldLocation player : players) {
				removePlayerFromGlobal(player.getPlayer().getName());
				player.getPlayer().teleport(player.getOldLocation());
				player.getPlayer().setGameMode(player.getOldGameMode());
				Bukkit.getEntity(uuid).remove();
			}
		}
		public PlayerWithOldLocation[] getPlayersName() {
			return players;
		}
		public void removePlayerFromGlobal(String name) {
			LockPlayerCommand.allowPlayerTeleport(name);
		}
		public void setPlayers(PlayerWithOldLocation[] players) {
			this.players = players;
		}
		public UUID getUuid() {
			return uuid;
		}
		public void setUuid(UUID uuid) {
			this.uuid = uuid;
		}
	}
	public static class LockPlayerRunnable extends BukkitRunnable{
		private PlayerWithOldLocation[] players;
		public LockPlayerRunnable(PlayerWithOldLocation[] players) {
			setPlayers(players);
		}
		@Override
		public void run() {
			for (PlayerWithOldLocation player : getPlayers()) {
				player.getPlayer().teleport(player.getOldLocation());
			}
		}
		public PlayerWithOldLocation[] getPlayers() {
			return players;
		}
		public void setPlayers(PlayerWithOldLocation[] players) {
			this.players = players;
		}
		
	}
	public static class LockPlayerEnd extends BukkitRunnable {
		private PlayerWithOldLocation[] players;
		private BukkitTask lockPlayerTask;
		public LockPlayerEnd(PlayerWithOldLocation[] players, BukkitTask lockPlayerTask) {
			setPlayers(players);
			setLockPlayerTask(lockPlayerTask);
		}
		@Override
		public void run() {
			for (PlayerWithOldLocation player : players) {
				player.getPlayer().teleport(player.getOldLocation());
				player.getPlayer().setGameMode(player.getOldGameMode());
			}
			lockPlayerTask.cancel();
		}
		public PlayerWithOldLocation[] getPlayers() {
			return players;
		}
		public void setPlayers(PlayerWithOldLocation[] players) {
			this.players = players;
		}
		public BukkitTask getLockPlayerTask() {
			return lockPlayerTask;
		}
		public void setLockPlayerTask(BukkitTask lockPlayerTask) {
			this.lockPlayerTask = lockPlayerTask;
		}
		
	}
	
	private static List<String> playersCancellingTeleport = new ArrayList<>();
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (playersCancellingTeleport.contains(event.getPlayer().getName())) event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerLeftFromPlayer(PlayerToggleSneakEvent event) {
		if (isPlayerAllowedTeleport(event.getPlayer().getName())) {
			event.setCancelled(true);
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if (args.length >= 8) {
			
			ParseData parseData = new ParseData(0, args);
			
			long time = DataParser.parseLong(parseData);
			Location location = DataParser.parseLocation(parseData);
			float yaw = DataParser.parseFloat(parseData);
			float pitch = DataParser.parseFloat(parseData);
			location.setYaw(yaw);
			location.setPitch(pitch);
			boolean gm3 = DataParser.parseBoolean(parseData);
			Player[] players = DataParser.parseArray(Player.class, parseData, args.length - 8);
	
			if (gm3 == true) {
				Villager npc = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
				npc.setAI(false);
				PlayerWithOldLocation[] playersWithOldLocation = new PlayerWithOldLocation[players.length];
				for (int i = 0; i < players.length; ++i) {
					playersWithOldLocation[i] = new PlayerWithOldLocation(players[i], players[i].getLocation(), players[i].getGameMode());
					players[i].setGameMode(GameMode.SPECTATOR);
					players[i].setSpectatorTarget(npc);
					playersWithOldLocation[i].setPlayer(players[i]);
					addPlayerCancellingTeleport(players[i].getName());
				}
				(new GM3Runnable(playersWithOldLocation, npc.getUniqueId())).runTaskLater(Loader.getLoader(), time);
			}
			else {
				PlayerWithOldLocation[] playersWithOldLocation = new PlayerWithOldLocation[players.length];
				for (int i = 0; i < players.length; ++i) {
					playersWithOldLocation[i] = new PlayerWithOldLocation(players[i], players[i].getLocation(), players[i].getGameMode());
					players[i].teleport(location);
				}
				BukkitTask lockPlayerTask = (new LockPlayerRunnable(playersWithOldLocation)).runTask(Loader.getLoader());
				(new LockPlayerEnd(playersWithOldLocation, lockPlayerTask)).runTaskLater(Loader.getLoader(), time);
			}
			
		}
		
		return false;
	}

	public static List<String> getPlayersCancellingTeleport() {
		return playersCancellingTeleport;
	}
	public static void addPlayerCancellingTeleport(String name) {
		playersCancellingTeleport.add(name.toLowerCase());
	}
	public static void allowPlayerTeleport(String name) {
		playersCancellingTeleport.remove(name.toLowerCase());
	}
	public static void setPlayersCancellingTeleport(List<String> playerCancellingTeleport) {
		LockPlayerCommand.playersCancellingTeleport = playerCancellingTeleport;
	}
	public static boolean isPlayerAllowedTeleport(String name) {
		return playersCancellingTeleport.contains(name.toLowerCase());
	}


}
