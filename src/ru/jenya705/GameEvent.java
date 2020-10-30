package ru.jenya705;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ru.jenya705.component.Component;
import ru.jenya705.component.ComponentObject;
import ru.jenya705.util.MinecraftUtil;
import ru.jenya705.util.Util;

public class GameEvent {
	
	public static enum BroadcastTime {
		ONE_DAY(3600 * 12 * 24, "Остался 1 день", true, true),
		TWENTY_HOURS(3600 * 12, "Осталось 12 часов", true, true),
		ONE_HOUR(3600, "Остался 1 час", true, true),
		TEN_MINUTES(600, "Осталось 10 минут", true, true),
		FIVE_MINUTES(300, "Осталось 5 минут", true, true),
		ONE_MINUTE(60, "Осталась 1 минута", true, true),
		FIVE_SECONDS(5, "5", true, false),
		FOUR_SECONDS(4, "4", false, false),
		THREE_SECONDS(3, "3", false, false),
		TWO_SECONDS(2, "2", false, false),
		ONE_SECOND(1, "1", false, false);
		private long delay;
		private String localizedTimeString;
		private boolean writeCoordinates;
		private boolean writeEventInfo;
		private BroadcastTime(long delay, String localizedTimeString, boolean writeCoordinates, boolean writeEventInfo) {
			this.delay = delay;
			this.localizedTimeString = localizedTimeString;
			this.writeCoordinates = writeCoordinates;
			this.writeEventInfo = writeEventInfo;
		}
		public long getDelay() {
			return delay;
		}
		public String getLocalizedTimeString() {
			return localizedTimeString;
		}
		public boolean isWriteCoordinates() {
			return writeCoordinates;
		}
		public boolean isWriteEventInfo() {
			return writeEventInfo;
		}
	}
	public static class BroadcastExecutor extends BukkitRunnable{
		
		private BroadcastTime broadCastTime;
		private GameEvent gameEvent;
		
		public BroadcastExecutor(BroadcastTime broadCastTime, GameEvent gameEvent) {
			setBroadCastTime(broadCastTime);
			setGameEvent(gameEvent);
		}
		
		@Override
		public void run() {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(getBroadCastTime().getLocalizedTimeString());
			if (getBroadCastTime().isWriteEventInfo()) {
				stringBuilder.append(" до события " + getGameEvent().getName());
			}
			if (getBroadCastTime().isWriteCoordinates()) {
				stringBuilder.append(". Координаты в чате");
				BroadcastExecutor.sendCoordinatesToAll(getGameEvent());
			}
			MinecraftUtil.sendTitleToAll("", stringBuilder.toString(), 10, 70, 20);
		}

		public static void sendCoordinatesToAll(GameEvent gameEvent) {
			Bukkit.broadcastMessage("Координаты события. x: " + gameEvent.getStartLocation().getBlockX() + 
					" z: " + gameEvent.getStartLocation().getBlockZ());
		}
		
		public BroadcastTime getBroadCastTime() {
			return broadCastTime;
		}

		public void setBroadCastTime(BroadcastTime broadCastTime) {
			this.broadCastTime = broadCastTime;
		}

		public GameEvent getGameEvent() {
			return gameEvent;
		}

		public void setGameEvent(GameEvent gameEvent) {
			this.gameEvent = gameEvent;
		}
		
	}
	public static class EventExecutor extends BukkitRunnable{
		
		private GameEvent gameEvent;
		
		public EventExecutor(GameEvent gameEvent) {
			setGameEvent(gameEvent);
		}
		
		@Override
		public void run() {
			MinecraftUtil.sendTitleToAll("Событие " + getGameEvent().getName(), "началось!", 10, 70, 20);
			MinecraftUtil.playSoundForAll(Sound.ENTITY_ENDER_DRAGON_GROWL);
			BroadcastExecutor.sendCoordinatesToAll(getGameEvent());
			getGameEvent().start();
		}

		public GameEvent getGameEvent() {
			return gameEvent;
		}
		public void setGameEvent(GameEvent gameEvent) {
			this.gameEvent = gameEvent;
		}
	}
	/**
	 * async task
	 * @author jenya
	 */
	public static class DeleteEventMentions extends BukkitRunnable {
		
		private GameEvent gameEvent;
		
		public DeleteEventMentions(GameEvent gameEvent) {
			this.gameEvent = gameEvent;
		}
		
		@Override
		public void run() {
			
			gameEvents.remove(gameEvent);
			for (Map.Entry<String, Component> componentWithName : Component.getComponents().entrySet()) {
				Component component = componentWithName.getValue();
				List<? extends ComponentObject> componentObjects = component.getComponentObjects();
				for (int i = 0; i < componentObjects.size(); ++i) {
					ComponentObject componentObject = componentObjects.get(i);
					if (componentObject.getGameEvent().getIdentifier().equals(gameEvent.getIdentifier())) {
						component.removeComponentObject(i);
					}
				}
			}
			Loader.log(Level.INFO, "Delete all mentions about gameEvent" + gameEvent.getIdentifier());
			Loader.log(Level.INFO, "Save deleted gameEvent");
			SaveRunnable.saveFinished(gameEvent.getIdentifier());
		}
		
	}
	/**
	 * async task
	 * @author jenya
	 */
	public static class SaveRunnable extends BukkitRunnable {

		public static void saveFinished(String identifier) {
			try {
				File file = new File(Loader.getLoader().getDataFolder() , "events.json");
				String fileText = Util.readTextFromFile(file);
				JSONObject jsonObject = (JSONObject) (new JSONParser()).parse(fileText);
				JSONObject eventObject = (JSONObject) (jsonObject.get(identifier));
				eventObject.put("finished", true);
				jsonObject.put(identifier, eventObject);
				Util.writeTextInFile(jsonObject.toJSONString(), file);
			}
			catch(IOException exception) {
				Loader.log(Level.SEVERE, "IOException while saving finished event");
			} catch (ParseException e) {
				Loader.log(Level.SEVERE, "ParseException while saving finished event");
			} catch (ClassCastException e) {
				Loader.log(Level.SEVERE, "ClassCastException while saving finished event");
			}
		}
		
		@Override
		public void run() {
			try {	
				File file = new File(Loader.getLoader().getDataFolder() , "events.json");
				String fileText = Util.readTextFromFile(file);
				JSONObject jsonObject = (JSONObject) (new JSONParser()).parse(fileText);
				for(GameEvent gameEvent : GameEvent.getGameEvents()) {
					if (jsonObject.containsKey(gameEvent.getIdentifier()) == false) {
						JSONObject eventObject = new JSONObject();
						eventObject.put("name", gameEvent.getName());
						eventObject.put("date", (new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss")).format(gameEvent.getDate()));
						eventObject.put("finished", false);
						JSONArray jsonArray = new JSONArray();
						jsonArray.add(gameEvent.getStartLocation().getWorld().getName());
						jsonArray.add(gameEvent.getStartLocation().getBlockX());
						jsonArray.add(gameEvent.getStartLocation().getBlockY());
						jsonArray.add(gameEvent.getStartLocation().getBlockZ());
						eventObject.put("startlocation", jsonArray);
						jsonObject.put(gameEvent.getIdentifier(), eventObject);
					}
				}
				Util.writeTextInFile(jsonObject.toJSONString(), file);
			}
			catch(IOException exception) {
				Loader.log(Level.SEVERE, "IOException while saving events");
				exception.printStackTrace();
			} catch (ParseException e) {
				Loader.log(Level.SEVERE, "ParseException while saving events");
				e.printStackTrace();
			} catch (ClassCastException e) {
				Loader.log(Level.SEVERE, "ClassCastException while saving events");
				e.printStackTrace();
			}
		}
		
	}
	
	private static List<GameEvent> gameEvents = new ArrayList<>();
	
	private String name;
	private String identifier;
	private Date date;
	private Location startLocation;
	
	public GameEvent(String name, String identifier, Date date, Location startLocation) {
		setName(name);
		setIdentifier(identifier);
		setDate(date);
		setStartLocation(startLocation);
		addBroadcastTasks();
	}
	
	public void start() {
		getStartLocation().getBlock().setType(Material.REDSTONE_BLOCK);
	}
	
	public void finish() {
		(new DeleteEventMentions(this)).runTaskAsynchronously(Loader.getLoader());
	}
	
	public void save() {
		(new SaveRunnable()).runTaskAsynchronously(Loader.getLoader());
	}
	
	protected void addBroadcastTasks() {
		for (BroadcastTime broadCastTime : BroadcastTime.values()) {
			long delay = (getDate().getTime() - (new Date()).getTime()) / 1000 - broadCastTime.getDelay();
			if (delay > 0) {
				(new BroadcastExecutor(broadCastTime, this)).runTaskLater(Loader.getLoader(), delay * 20);
			}
		}
		(new EventExecutor(this)).runTaskLater(Loader.getLoader(), (getDate().getTime() - (new Date()).getTime()) / 50);
	}
	
	public static void loadEvents() {
	
		try{
			File file = new File(Loader.getLoader().getDataFolder() , "events.json");
			if (file.createNewFile() == false) {
				String fileText = Util.readTextFromFile(file);
				JSONObject jsonObject = (JSONObject) (new JSONParser()).parse(fileText);
				Set<Map.Entry<Object, Object>> jsonObjectElementSet = jsonObject.entrySet();
				for (Map.Entry<Object, Object> element : jsonObjectElementSet) {
					String key = (String) element.getKey();
					JSONObject eventObject = (JSONObject) element.getValue();
					boolean finished = (boolean) eventObject.get("finished");
					if (finished == false) {
						String eventName = (String) eventObject.get("name");
						Date date = (new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss")).parse((String)eventObject.get("date"));
						JSONArray locationLikeArray  = (JSONArray) eventObject.get("startlocation");
						Location startLocation = new Location(Bukkit.getWorld((String)locationLikeArray.get(0)), 
								(long)locationLikeArray.get(1), (long)locationLikeArray.get(2), (long)locationLikeArray.get(3));
						GameEvent gameEvent = new GameEvent(eventName, key, date, startLocation);
						GameEvent.addGameEvent(gameEvent);
						Loader.log(Level.INFO, "Loaded event " + key);
					}
				}
			}
			else {
				Util.writeTextInFile("{}", file);
			}
		}
		catch(IOException exception) {
			Loader.log(Level.SEVERE, "IOException while loading events");
			Loader.log(Level.SEVERE, "IOException while loading events");
			Loader.log(Level.SEVERE, "IOException while loading events");
			exception.printStackTrace();
		}
		catch(ParseException exception) {
			Loader.log(Level.SEVERE, "ParseException while loading events");
			Loader.log(Level.SEVERE, "ParseException while loading events");
			Loader.log(Level.SEVERE, "ParseException while loading events");
			exception.printStackTrace();
		}
		catch(java.text.ParseException exception) {
			Loader.log(Level.SEVERE, "Date ParseException while loading events");
			Loader.log(Level.SEVERE, "Date ParseException while loading events");
			Loader.log(Level.SEVERE, "Date ParseException while loading events");
			exception.printStackTrace();
		}
		catch(ClassCastException exception) {
			Loader.log(Level.SEVERE, "ClassCastException while loading events");
			Loader.log(Level.SEVERE, "ClassCastException while loading events");
			Loader.log(Level.SEVERE, "ClassCastException while loading events");
			exception.printStackTrace();
		}
		
	}
	public static List<GameEvent> getGameEvents(){
		return gameEvents;
	}
	public static void addGameEvent(GameEvent gameEvent) {
		gameEvents.add(gameEvent);
		gameEvent.save();
	}
	public static GameEvent getGameEventByName(String name) {
		for (GameEvent gameEvent : gameEvents) {
			if (gameEvent.getName().equalsIgnoreCase(name)) {
				return gameEvent;
			}
		}
		return null;
	}
	public static GameEvent getGameEventByIdentifier(String identifier) {
		for (GameEvent gameEvent : gameEvents) {
			if (gameEvent.getIdentifier().equalsIgnoreCase(identifier)) {
				return gameEvent;
			}
		}
		return null;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public Location getStartLocation() {
		return startLocation;
	}
	public void setStartLocation(Location startLocation) {
		this.startLocation = startLocation;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public boolean isStarted() {
		return date.before(new Date());
	}

}
