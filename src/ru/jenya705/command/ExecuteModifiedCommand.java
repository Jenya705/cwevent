package ru.jenya705.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import ru.jenya705.util.Util;

public class ExecuteModifiedCommand implements CommandExecutor, TabExecutor{

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
		return new ArrayList<>();
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		
		if (args.length >= 2) {
			
			Player player = Bukkit.getPlayer(args[0]);
			if (player != null) {
				Bukkit.dispatchCommand(player, Util.connectStringsFromArrayWithCut(args, 1, args.length, " "));
			}
			else {
				commandSender.sendMessage("Player is not online");
			}
			
			return true;
		}
		
		return false;
	}

}
