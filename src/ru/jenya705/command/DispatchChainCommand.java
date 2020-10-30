package ru.jenya705.command;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import ru.jenya705.parsing.DataParser;
import ru.jenya705.util.MinecraftUtil;

public class DispatchChainCommand implements CommandExecutor, TabExecutor{

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		
		if (args.length >= 4) {
			
			Location location = DataParser.parseLocation(new DataParser.ParseData(0, args));
			MinecraftUtil.dispatchChain((CommandBlock) location.getBlock().getState(), args.length >= 5 ? args[4] : "");
			return true;
			
		}
		
		return false;
	}

}
