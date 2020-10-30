package ru.jenya705.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import ru.jenya705.GameEvent;
import ru.jenya705.Loader;
import ru.jenya705.component.TriggerComponent.Trigger;
import ru.jenya705.parsing.DataParser;
import ru.jenya705.parsing.DataParser.ParseData;
import ru.jenya705.util.Util;

public class TriggerPlayerOnceComponent extends Component{

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
			
		}
		
	}
	
	@Override
	public void registerComponent() {
		Component.addComponent("triggerplayeronce", this);
		addFunction("players", TriggerPlayerOnceComponent::countPlayersEnteredTrigger);
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
		TriggerPlayerOnceComponent.triggers = triggers;
	}
	public static void addTrigger(Trigger trigger) {
		triggers.add(trigger);
	}
	public static void removeTrigger(int index) {
		triggers.remove(index);
	}
	
}
