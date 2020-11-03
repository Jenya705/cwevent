package ru.jenya705.script;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.Listener;

import ru.jenya705.Loader;
import ru.jenya705.command.ArgumentObject;
import ru.jenya705.parsing.DataParser;
import ru.jenya705.parsing.DataParser.ParseData;
import ru.jenya705.util.Util;

public class ScriptCommand implements TabExecutor, CommandExecutor {

	public static class LoadArgument extends ArgumentObject{

		@Override
		public boolean onCommand(CommandSender commandSender, Command command, String label, ParseData parseData) {
			
			try {
				if (parseData.getArgs().length >= 2) {
					String scriptName = parseData.next();
					Class<?> scriptClass = ScriptManager.getScript(scriptName);
					Listener listener = (Listener) scriptClass.newInstance();
					ScriptManager.addScript(scriptName, listener);
					Loader.getLoader().getServer().getPluginManager().registerEvents(listener, Loader.getLoader());
					return true;
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			
			return false;
		}

		@Override
		public List<String> onTabComplete(CommandSender commandSender, Command command, String label, ParseData parseData) {
			List<File> files = Util.getFilesInDirectory(new File(Loader.getLoader().getDataFolder(), "\\scripts"));
			List<String> tab = new ArrayList<>();
			for (File file : files) {
				tab.add(file.getPath().substring(23).substring(0, file.getPath().length() - 23 - 6));
			}
			return tab;
		}
		
	}
	public static class VarArgument extends ArgumentObject{

		@Override
		public boolean onCommand(CommandSender commandSender, Command command, String label, ParseData parseData) {
			return false;
		}

		@Override
		public List<String> onTabComplete(CommandSender commandSender, Command command, String label, ParseData parseData) {
			List<String> tab = null;
			if (parseData.getArgs().length - parseData.getStart() == 1) {
				tab = new ArrayList<>();
				for (Map.Entry<String, Object> script : ScriptManager.getScripts().entrySet()) tab.add(script.getKey());
			}
			
			return tab;
		}
		
	}
	public static class FuncArgument extends ArgumentObject{

		@Override
		public boolean onCommand(CommandSender commandSender, Command command, String label, ParseData parseData) {
			try {
				if (parseData.getArgs().length - parseData.getStart() >= 2) {
					String scriptName = parseData.next();
					String methodName = parseData.next();
					Object script = ScriptManager.getLoadedScript(scriptName);
					Method method = script.getClass().getMethod(methodName, String[].class);
					method.invoke(script, (Object) DataParser.parseArray(parseData, parseData.getArgs().length - 3));
					return true;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			return false;
		}

		@Override
		public List<String> onTabComplete(CommandSender commandSender, Command command, String label, ParseData parseData) {
			return null;
		}
		
	}
	public static class LoadedArgument extends ArgumentObject{

		@Override
		public boolean onCommand(CommandSender commandSender, Command command, String label, ParseData parseData) {
			return false;
		}

		@Override
		public List<String> onTabComplete(CommandSender commandSender, Command command, String label, ParseData parseData) {
			return null;
		}
		
	}
	public static class FinishArgument extends ArgumentObject{

		@Override
		public boolean onCommand(CommandSender commandSender, Command command, String label, ParseData parseData) {
			
			if (parseData.getArgs().length - parseData.getStart() >= 1) {
				String scriptName = parseData.next();
				ScriptManager.removeScript(scriptName);
				return true;
			}
			
			return false;
		}

		@Override
		public List<String> onTabComplete(CommandSender commandSender, Command command, String label, ParseData parseData) {
			
			List<String> tab = new ArrayList<>();
			for (Map.Entry<String, Object> script : ScriptManager.getScripts().entrySet()) {
				tab.add(script.getKey());
			}
			return tab;
			
		}
		
	}
	
	private static Map<String, ArgumentObject> argumentObjects = createArgumentObjects();
	
	protected static Map<String, ArgumentObject> createArgumentObjects(){
		Map<String, ArgumentObject> argumentObjects = new HashMap<>();
		argumentObjects.put("load", new LoadArgument());
		argumentObjects.put("var", new VarArgument());
		argumentObjects.put("func", new FuncArgument());
		argumentObjects.put("loaded", new LoadedArgument());
		argumentObjects.put("finish", new FinishArgument());
		return argumentObjects;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
		
		List<String> tab = new ArrayList<>();
		ParseData parseData = new ParseData(0, args);
		ArgumentObject argumentObject = argumentObjects.getOrDefault(parseData.next(), null);
		if (argumentObject != null) return argumentObject.onTabComplete(commandSender, command, label, parseData);
		else {
			for (Map.Entry<String, ArgumentObject> argObj : argumentObjects.entrySet()) tab.add(argObj.getKey());
		}
		return tab;
		
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		
		ParseData parseData = new ParseData(0, args);
		ArgumentObject argumentObject = argumentObjects.getOrDefault(parseData.next(), null);
		if (argumentObject != null) return argumentObject.onCommand(commandSender, command, label, parseData);
		return false;
	}
	
	


}
