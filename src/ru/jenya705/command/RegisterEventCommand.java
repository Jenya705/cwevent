package ru.jenya705.command;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import ru.jenya705.GameEvent;
import ru.jenya705.parsing.DataParser;
import ru.jenya705.parsing.DataParser.ParseData;

public class RegisterEventCommand implements CommandExecutor, TabExecutor{

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
		List<String> result = new ArrayList<>();
		result.add("dd-MM-yyyy-HH-mm-ss");
		return result;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		
		// regcomp <start_location> <event_identifier> <event_name> <date>
		
		if (args.length >= 7) {
			
			ParseData parseData = new ParseData(0, args);
			Location location = DataParser.parseLocation(parseData);
			String eventIdentifier = parseData.next();
			String eventName = parseData.next();
			Date date = DataParser.parseDate(parseData);
			
			GameEvent gameEvent = new GameEvent(eventName, eventIdentifier, date, location);
			GameEvent.addGameEvent(gameEvent);
			return true;
		}
		
		return false;

	}

}
