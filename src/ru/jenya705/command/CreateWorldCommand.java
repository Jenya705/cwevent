package ru.jenya705.command;

import java.util.List;

import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import ru.jenya705.GameEvent;
import ru.jenya705.parsing.DataParser;
import ru.jenya705.parsing.DataParser.ParseData;
import ru.jenya705.util.MinecraftUtil;

public class CreateWorldCommand implements CommandExecutor, TabExecutor{

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		
		if (args.length >= 2) {
			
			ParseData parseData = new ParseData(0, args);
			GameEvent gameEvent = DataParser.parseGameEvent(parseData);
			String worldName = parseData.next();
			
			WorldCreator worldCreator = new WorldCreator(worldName);
			worldCreator.generator(new MinecraftUtil.VoidChunkGenerator());
			gameEvent.setWorld(worldCreator.createWorld());
			
			gameEvent.save();
			
			return true;
			
		}
		
		return false;
	}

}
