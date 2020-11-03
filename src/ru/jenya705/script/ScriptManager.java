package ru.jenya705.script;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import ru.jenya705.Loader;

public class ScriptManager {

	private static Map<String, Object> scripts = new HashMap<>();

	public static Class<?> getScript(String name) {
		
		Loader.log(Level.INFO, name);
		try (URLClassLoader classLoader = new URLClassLoader(new URL[] {new File(Loader.getLoader().getDataFolder(), "\\scripts").toURI().toURL()})){
			return classLoader.loadClass(name);
		}
		catch(Exception e) {
			Loader.log(Level.SEVERE, "Exception while loading script");
			e.printStackTrace();
		}
		
		return null;
		
	}

	public static Map<String, Object> getScripts() {
		return scripts;
	}
	public static void addScript(String name, Object script) {
		scripts.put(name, script);
	}
	public static void removeScript(String name) {
		HandlerList.unregisterAll((Listener) scripts.get(name));
		scripts.remove(name);
	}
	public static Object getLoadedScript(String name) {
		return scripts.get(name);
	}
	public static void setScripts(Map<String, Object> scripts) {
		ScriptManager.scripts = scripts;
	}
	
	
}
