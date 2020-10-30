package ru.jenya705.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import ru.jenya705.Loader;
import ru.jenya705.util.Util;

public class SendMessageCommand implements CommandExecutor, TabExecutor{

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
		List<String> result = new ArrayList<>();
		for (Player player : Bukkit.getOnlinePlayers()) {
			result.add(player.getName());
		}
		result.add("@a");
		return result;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		
		if (args.length >= 2) {
			Player player;
			if (args[0].equalsIgnoreCase("@a")) {
				Bukkit.broadcastMessage(Util.connectStringsFromArrayWithCut(args, 1, args.length, " "));
				Loader.log(Level.INFO, "Sendmsg command executed " + commandSender.getName() + " " + Arrays.toString(args));
			}
			else if ((player = Bukkit.getPlayer(args[0])) != null) {
				player.sendMessage(Util.connectStringsFromArrayWithCut(args, 1, args.length, " "));
				Loader.log(Level.INFO, "Sendmsg command executed " + commandSender.getName() + " " + Arrays.toString(args));
			}
			else {
				commandSender.sendMessage("Player is not online");
			}
			return true;
		}
		
		return false;
	}

}
