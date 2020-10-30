package ru.jenya705.component;

import ru.jenya705.GameEvent;

public abstract class ComponentObject {

	private GameEvent gameEvent;
	private String uuid;
	
	public ComponentObject(GameEvent gameEvent, String uuid) {
		setGameEvent(gameEvent);
		setUuid(uuid);
	}
	
	public GameEvent getGameEvent() {
		return gameEvent;
	}

	public void setGameEvent(GameEvent gameEvent) {
		this.gameEvent = gameEvent;
	}

	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	
	
}
