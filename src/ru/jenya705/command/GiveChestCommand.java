package ru.jenya705.command;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ru.jenya705.parsing.DataParser;
import ru.jenya705.parsing.DataParser.ParseData;

/**
 * 
 * this command need to execute by dispatch
 * 
 * @author jenya
 *
 */
public class GiveChestCommand implements CommandExecutor, TabExecutor{

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		
		if (args.length >= 5) {
			
			ParseData parseData = new ParseData(0, args);
			Location location = DataParser.parseLocation(parseData);
			Player player = DataParser.parsePlayer(parseData);
			
			Chest chest = (Chest) location.getBlock().getState();
			
			for (ItemStack itemStack : chest.getInventory().getStorageContents()) {
				if (itemStack != null) player.getInventory().addItem(itemStack);
			}
			return true;
			
		}
		
		return false;
	}

}
