package ru.jenya705.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import ru.jenya705.GameEvent;
import ru.jenya705.parsing.DataParser;

public class FinishEventCommand implements TabExecutor, CommandExecutor{

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		
		if (args.length >= 1) {
			
			GameEvent gameEvent = DataParser.parseGameEvent(new DataParser.ParseData(0, args));
			gameEvent.finish();
			return true;
			
		}
		
		return false;
	}

}
