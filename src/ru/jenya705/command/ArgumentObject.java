package ru.jenya705.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import ru.jenya705.parsing.DataParser.ParseData;

public abstract class ArgumentObject {

	public abstract boolean onCommand(CommandSender commandSender, Command command, String label, ParseData parseData);
	public abstract List<String> onTabComplete(CommandSender commandSender, Command command, String label, ParseData parseData);
	
}
