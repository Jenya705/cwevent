package ru.jenya705.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import ru.jenya705.util.Util;
import ru.jenya705.variable.VariableContainer;

public class SetVarCommand implements CommandExecutor, TabExecutor{

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		
		if (args.length >= 2) {
			
			VariableContainer.setVariable(args[0], Util.connectStringsFromArrayWithCut(args, 1, args.length, " "));
			return true;
			
		}
		
		return false;
	}

	
	
}
