package ru.jenya705.parsing;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import ru.jenya705.GameEvent;

public class DataParser {
	
	public static class ParseData {
		
		private int start;
		private String[] args;
		
		public ParseData(int start, String[] args) {
			setStart(start);
			setArgs(args);
		}
		
		public int getStart() {
			return start;
		}
		
		public void setStart(int start) {
			this.start = start;
		}
		
		public void addToStart(int toAdd) {
			start += toAdd;
		}
		
		public String[] getArgs() {
			return args;
		}
		
		public void setArgs(String[] args) {
			this.args = args;
		}
		
		public String next() {
			return args[start++];
		}
		
	}
	
	private static Map<Class<? extends Object>, Function<ParseData, Object>> parseFunctions = createParseFunction();
	
	protected static Map<Class<? extends Object>, Function<ParseData, Object>> createParseFunction(){
		DataParser.parseFunctions = new HashMap<>();
		DataParser.parseFunctions.put(Boolean.class, DataParser::parseBoolean);
		DataParser.parseFunctions.put(Float.class, DataParser::parseFloat);
		DataParser.parseFunctions.put(Double.class, DataParser::parseDouble);
		DataParser.parseFunctions.put(Short.class, DataParser::parseShort);
		DataParser.parseFunctions.put(Integer.class, DataParser::parseInteger);
		DataParser.parseFunctions.put(Long.class, DataParser::parseLong);
		DataParser.parseFunctions.put(Player.class, DataParser::parsePlayer);
		DataParser.parseFunctions.put(GameEvent.class, DataParser::parseGameEvent);
		DataParser.parseFunctions.put(World.class, DataParser::parseWorld);
		DataParser.parseFunctions.put(Location.class, DataParser::parseLocation);
		DataParser.parseFunctions.put(Date.class, DataParser::parseDate);
		return DataParser.parseFunctions;
	}
	
	public static Boolean parseBoolean(ParseData parseData) {
		return Boolean.parseBoolean(parseData.next());
	}
	public static Float parseFloat(ParseData parseData) {
		return Float.parseFloat(parseData.next());
	}
	
	public static Double parseDouble(ParseData parseData) {
		return Double.parseDouble(parseData.next());
	}
	
	public static Short parseShort(ParseData parseData) {
		return Short.parseShort(parseData.next());
	}
	
	public static Integer parseInteger(ParseData parseData) {
		return Integer.parseInt(parseData.next());
	}
	
	public static Long parseLong(ParseData parseData) {
		return Long.parseLong(parseData.next());
	}
	
	public static Player parsePlayer(ParseData parseData) {
		return Bukkit.getPlayer(parseData.next());
	}

	public static GameEvent parseGameEvent(ParseData parseData) {
		return GameEvent.getGameEventByIdentifier(parseData.next());
	}
	
	/**
	 * 
	 * DateFormat dd-MM-yyyy-HH-mm-ss
	 * 
	 * @param parseData
	 * @return parsed date
	 */
	public static Date parseDate(ParseData parseData) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
		try {
			return dateFormat.parse(parseData.next());
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static World parseWorld(ParseData parseData) {
		return Bukkit.getWorld(parseData.next());
	}
	
	public static Location parseLocation(ParseData parseData) {
		return new Location(parseWorld(parseData), parseDouble(parseData), parseDouble(parseData), parseDouble(parseData));
	}
	
	public static <T extends Object> T[] parseArray(Class<T> clazz, ParseData parseData, int len) {
		T[] arr = (T[]) Array.newInstance(clazz, len);
		Function<ParseData, Object> parseFunction = getParseFunction(clazz);
		if (parseFunction != null) {
			for (int i = 0; i < len; ++i) {
				arr[i] = (T) parseFunction.apply(parseData);
			}
			return arr;
		}
		return null;
	}
	
	public static Function<ParseData, Object> getParseFunction(Class<? extends Object> clazz){
		return DataParser.parseFunctions.getOrDefault(clazz, null);
	}
	
	public static Object parse(Class<? extends Object> clazz, ParseData parseData) {
		Function<ParseData, Object> function = getParseFunction(clazz);
		if (function != null) {
			return function.apply(parseData);
		}
		return null;
	}
	
	public static Map<Class<? extends Object>, Function<ParseData, Object>> getParseFunctions(){
		return DataParser.parseFunctions;
	}

}
