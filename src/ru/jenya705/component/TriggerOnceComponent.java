package ru.jenya705.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import ru.jenya705.GameEvent;
import ru.jenya705.Loader;
import ru.jenya705.parsing.DataParser;
import ru.jenya705.parsing.DataParser.ParseData;
import ru.jenya705.util.MinecraftUtil;
import ru.jenya705.util.Util;

public class TriggerOnceComponent extends Component{

	public static class Trigger extends ComponentObject{
		
		private Location commandBlockLocation;
		private Location minLocation;
		private Location maxLocation;
		private List<String> playersEnteredTrigger;
		
		public Trigger(GameEvent gameEvent, String uuid, Location commandBlockLocation, Location firstLocation, Location secondLocation) {
			super(gameEvent, uuid);
			
			double maxX, maxY, maxZ;
			double minX, minY, minZ;
			
			if (firstLocation.getX() > secondLocation.getX()) {
				maxX = firstLocation.getX();
				minX = secondLocation.getX();
			}
			else {
				maxX = secondLocation.getX();
				minX = firstLocation.getX();
			}
			
			if (firstLocation.getY() > secondLocation.getY()) {
				maxY = firstLocation.getY();
				minY = secondLocation.getY();
			}
			else {
				maxY = secondLocation.getY();
				minY = firstLocation.getY();
			}
			
			if (firstLocation.getZ() > secondLocation.getZ()) {
				maxZ = firstLocation.getZ();
				minZ = secondLocation.getZ();
			}
			else {
				maxZ = secondLocation.getZ();
				minZ = firstLocation.getZ();
			}
			
			maxX += 0.5d;
			minX -= 0.5d;
			maxY += 1.0d;
			minY -= 1.0d;
			maxZ += 0.5d;
			minZ -= 0.5d;
			
			setCommandBlockLocation(commandBlockLocation);
			setMaxLocation(new Location(firstLocation.getWorld(), maxX, maxY, maxZ));
			setMinLocation(new Location(firstLocation.getWorld(), minX, minY, minZ));
			setPlayersEnteredTrigger(new ArrayList<>());
			
		}
		
		public boolean isPlayerStayingTrigger(Player player) {
			
			if (getMaxLocation().getX() >= player.getLocation().getX() && getMaxLocation().getY() >= player.getLocation().getY() 
					&& getMaxLocation().getZ() >= player.getLocation().getZ() && getMinLocation().getX() <= player.getLocation().getX()
					&& getMinLocation().getY() <= player.getLocation().getY() && getMinLocation().getZ() <= player.getLocation().getZ()) {
				return true;
			}
			return false;
			
		}
		
		public Location getCommandBlockLocation() {
			return commandBlockLocation;
		}
		public void setCommandBlockLocation(Location commandBlockLocation) {
			this.commandBlockLocation = commandBlockLocation;
		}
		public Location getMinLocation() {
			return minLocation;
		}
		public void setMinLocation(Location minLocation) {
			this.minLocation = minLocation;
		}
		public Location getMaxLocation() {
			return maxLocation;
		}
		public void setMaxLocation(Location maxLocation) {
			this.maxLocation = maxLocation;
		}
		public List<String> getPlayersEnteredTrigger() {
			return playersEnteredTrigger;
		}
		public boolean isPlayerEnteredTrigger(String name) {
			return playersEnteredTrigger.contains(name.toLowerCase());
		}
		public void setPlayersEnteredTrigger(List<String> playersEnteredTrigger) {
			this.playersEnteredTrigger = playersEnteredTrigger;
		}
		public void playerEnteredTrigger(String name) {
			playersEnteredTrigger.add(name.toLowerCase());
			MinecraftUtil.dispatchChain((CommandBlock) getCommandBlockLocation().getBlock().getState(), name);
		}
		public void playerExitedTrigger(String name) {
			playersEnteredTrigger.remove(name.toLowerCase());
		}
		public void playerExitedTrigger(int index) {
			playersEnteredTrigger.remove(index);
		}
		
	}
	
	private static List<Trigger> triggers = new ArrayList<>();
	
	@EventHandler
	public void onPlayerMovement(PlayerMoveEvent event) {
		
		Player player = event.getPlayer();
		for (Trigger trigger : triggers) {
			
			if (trigger.isPlayerEnteredTrigger(player.getName()) == false) {
				
				if (trigger.isPlayerStayingTrigger(player)) {
					trigger.playerEnteredTrigger(player.getName());
				}
				
			}
			else {
				if (trigger.isPlayerStayingTrigger(player) == false) {
					trigger.playerExitedTrigger(player.getName());
				}
			}
			
		}
		
	}
	
	@Override
	public void registerComponent() {
		Component.addComponent("triggeronce", this);
		addFunction("players", TriggerOnceComponent::countPlayersEnteredTrigger);
		Loader.getLoader().getServer().getPluginManager().registerEvents(this, Loader.getLoader());
	}
	
	@Override
	public void create(GameEvent gameEvent, String uuid, ParseData parseData) {
		create(new Trigger(gameEvent, uuid, DataParser.parseLocation(parseData), 
				DataParser.parseLocation(parseData), DataParser.parseLocation(parseData)));
	}
	
	@Override
	public List<? extends ComponentObject> getComponentObjects() {
		return triggers;
	}
	
	@Override
	public void removeComponentObject(int i) {
		triggers.remove(i);
	}
	
	@Override
	public void removeComponentObject(Object object) {
		triggers.remove(object);
	}
	
	public static void countPlayersEnteredTrigger(ComponentMethodData data) {
		Trigger trigger = Component.getObjectByUUID(data.getArgs()[0], triggers);
		data.setResult(Integer.toString(trigger.getPlayersEnteredTrigger().size()));
	}
	
	public static void playersEnteredTrigger(ComponentMethodData data) {
		Trigger trigger = Component.getObjectByUUID(data.getArgs()[0], triggers);
		Object[] array = trigger.getPlayersEnteredTrigger().toArray();
		data.setResult(Util.connectStringsFromArray(Arrays.copyOf(array, array.length, String[].class), " "));
	}
	
	public static void create(Trigger trigger) {
		addTrigger(trigger);
	}
	
	public static void create(GameEvent gameEvent, String uuid, Location commandBlockLocation, Location firstLocation, Location secondLocation) {
		create(new Trigger(gameEvent, uuid, commandBlockLocation, firstLocation, secondLocation));
	}
	
	public static List<Trigger> getTriggers(){
		return triggers;
	}
	public static Trigger getTrigger(int index) {
		return triggers.get(index);
	}
	public static void setTriggers(List<Trigger> triggers) {
		TriggerOnceComponent.triggers = triggers;
	}
	public static void addTrigger(Trigger trigger) {
		triggers.add(trigger);
	}
	public static void removeTrigger(int index) {
		triggers.remove(index);
	}
	
}
