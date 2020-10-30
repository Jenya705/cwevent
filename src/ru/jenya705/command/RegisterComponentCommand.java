package ru.jenya705.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import ru.jenya705.GameEvent;
import ru.jenya705.Loader;
import ru.jenya705.component.Component;
import ru.jenya705.parsing.DataParser.ParseData;

public class RegisterComponentCommand implements CommandExecutor, TabExecutor{

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
		List<String> result = new ArrayList<>();
		
		return result;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		
		if (args.length >= 3) {
	
			GameEvent gameEvent = GameEvent.getGameEventByIdentifier(args[0]);
			Component component = Component.getComponent(args[2]);
			String componentUUID = args[1];
			if (component != null && gameEvent != null) {
				component.create(gameEvent, componentUUID, new ParseData(3, args));
			}
			else {
				Loader.log(Level.WARNING, "Component is not exist. " + commandSender.getName() + " " + Arrays.toString(args));
			}
			return true;
			
		}
		
		return false;
	}

}
