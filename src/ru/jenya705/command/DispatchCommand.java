package ru.jenya705.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import ru.jenya705.util.MinecraftUtil;
import ru.jenya705.util.Util;

public class DispatchCommand implements CommandExecutor, TabExecutor{

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
		
		return null;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		
		MinecraftUtil.dispatchCommand(commandSender, Util.connectStringsFromArray(args, " "), "");
		
		return true;
	}

}
