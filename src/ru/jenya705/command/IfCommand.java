package ru.jenya705.command;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import ru.jenya705.parsing.DataParser;
import ru.jenya705.parsing.DataParser.ParseData;
import ru.jenya705.util.MinecraftUtil;

public class IfCommand implements CommandExecutor, TabExecutor{

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		
		if (args.length >= 1) {
			
			ParseData parseData = new ParseData(0, args);
			String executor = parseData.next();
			int ifCount = (args.length - 1) / 7;
			for (int i = 0; i < ifCount; ++i) {
				String firstValue = parseData.next();
				String operator = parseData.next();
				String secondValue = parseData.next();
				Location commandBlockLocation = DataParser.parseLocation(parseData);
				if (operator.equals("==")) { 
					if (firstValue.equals(secondValue)) {
						MinecraftUtil.dispatchChain((CommandBlock) commandBlockLocation.getBlock().getState(), executor);
						break;
					}
				}
				else if (operator.equals(">=")) { // ONLY FOR NUMBERS
					if (Float.parseFloat(firstValue) >= Float.parseFloat(secondValue)) {
						MinecraftUtil.dispatchChain((CommandBlock) commandBlockLocation.getBlock().getState(), executor);
						break;
					}
				}
				else if (operator.equals("<=")) { // ONLY FOR NUMBERS
					if (Float.parseFloat(firstValue) <= Float.parseFloat(secondValue)) {
						MinecraftUtil.dispatchChain((CommandBlock) commandBlockLocation.getBlock().getState(), executor);
						break;
					}
				}
				else if (operator.equals(">")) { // ONLY FOR NUMBERS
					if (Float.parseFloat(firstValue) > Float.parseFloat(secondValue)) {
						MinecraftUtil.dispatchChain((CommandBlock) commandBlockLocation.getBlock().getState(), executor);
						break;
					}
				}
				else if (operator.equals("<")) { // ONLY FOR NUMBERS
					if (Float.parseFloat(firstValue) < Float.parseFloat(secondValue)) {
						MinecraftUtil.dispatchChain((CommandBlock) commandBlockLocation.getBlock().getState(), executor);
						break;
					}
				}
			}
			return true;
			
		}
		
		return false;
	}

	
	
}
