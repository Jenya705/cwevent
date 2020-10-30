package ru.jenya705.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.event.Listener;

import ru.jenya705.GameEvent;
import ru.jenya705.parsing.DataParser.ParseData;

public abstract class Component implements Listener{

	private static Map<String, Component> components = new HashMap<>();
	private static Map<String, Consumer<ComponentMethodData>> globalFunctions = createGlobalFunctions();
	
	private Map<String, Consumer<ComponentMethodData>> componentMethods = new HashMap<>();	
	
	public abstract void registerComponent();
	public abstract void create(GameEvent gameEvent, String componentUUID, ParseData parseData);
	public abstract List<? extends ComponentObject> getComponentObjects();
	public abstract void removeComponentObject(int i);
	public abstract void removeComponentObject(Object object);
	
	protected static Map<String, Consumer<ComponentMethodData>> createGlobalFunctions() {
		Map<String, Consumer<ComponentMethodData>> globalFunctions = new HashMap<>();
		globalFunctions.put("calculate", GlobalComponent::count);
		return globalFunctions;
	}
	
	
	protected static void addComponent(String name, Component component) {
		components.put(name, component);
	}
	public static Component getComponent(String name) {
		return components.getOrDefault(name, null);
	}
	public static Map<String, Component> getComponents(){
		return components;
	}
	public Map<String, Consumer<ComponentMethodData>> getComponentMethods() {
		return componentMethods;
	}
	public void addFunction(String name, Consumer<ComponentMethodData> function){
		componentMethods.put(name, function);
	}
	public Consumer<ComponentMethodData> getFunction(String name){
		return componentMethods.getOrDefault(name, null);
	}
	public void applyFunction(String name, ComponentMethodData data) {
		Consumer<ComponentMethodData> func = componentMethods.getOrDefault(name, null);
		if (func != null) func.accept(data);
	}
	public void setComponentMethods(Map<String, Consumer<ComponentMethodData>> componentMethods) {
		this.componentMethods = componentMethods;
	}
	public static <T extends ComponentObject> T getObjectByUUID(String uuid, List<T> objects) {
		for (T t : objects) {
			if (t.getUuid().equals(uuid)) return t;
		}
		return null;
	}
	public static Map<String, Consumer<ComponentMethodData>> getGlobalFunctions() {
		return globalFunctions;
	}
	public static void applyGlobalFunction(String name, ComponentMethodData data) {
		Consumer<ComponentMethodData> func = globalFunctions.getOrDefault(name, null);
		if (func != null) func.accept(data);
	}
	public static Consumer<ComponentMethodData> getGlobalFunction(String name){
		return globalFunctions.getOrDefault(name, null);
	}
	public static void setGlobalFunctions(Map<String, Consumer<ComponentMethodData>> globalFunctions) {
		Component.globalFunctions = globalFunctions;
	}
	public static void addGlobalFunction(String name, Consumer<ComponentMethodData> function) {
		Component.globalFunctions.put(name, function);
	}
	
}
