package ru.jenya705.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.data.Directional;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.jenya705.Loader;
import ru.jenya705.component.Component;
import ru.jenya705.component.ComponentMethodData;

public class MinecraftUtil {
	
	protected static class MultiplyCommand { 
		private List<String> values;
		private String leftSide;
		private String rightSide;
		
		public MultiplyCommand(List<String> values, String leftSide, String rightSide) {
			setValues(values);
			setLeftSide(leftSide);
			setRightSide(rightSide);
		}
		
		public void dispatchCommands(CommandSender commandSender, String executorName) {
			for (String value : getValues()) {
				dispatchCommand(commandSender, leftSide + value + rightSide, executorName);
			}
		}
		
		public String getLeftSide() {
			return leftSide;
		}
		public void setLeftSide(String leftSide) {
			this.leftSide = leftSide;
		}
		public List<String> getValues() {
			return values;
		}
		public void setValues(List<String> values) {
			this.values = values;
		}
		public String getRightSide() {
			return rightSide;
		}
		public void setRightSide(String rightSide) {
			this.rightSide = rightSide;
		}
	}
	
	public static void sendTitleToAll(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
		}
	}
	
	public static void sendTitleToAll(String title, String subtitle) {
		MinecraftUtil.sendTitleToAll(title, subtitle, 10, 70, 20);
	}
	
	public static void playSoundForAll(Sound sound) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
		}
	}
	
	public static void dispatchChain(CommandBlock commandBlock, String executorName) {
		
		CommandBlock currentCommandBlock = commandBlock;
		while (currentCommandBlock != null) {
			currentCommandBlock = MinecraftUtil.dispatchCommandBlock(currentCommandBlock, executorName);
		}
		
	}
	
	public static String[] modifyCommand(String executorName, String[] args) {
		args = getCommandWithEFlag(args, executorName);
		args = getCommandWithFunctionExecuted(executorName, args);
		return args;
	}
	
	public static String[] getCommandWithFunctionExecuted(String executorName, String[] args) {
		
		for (int i = 0; i < args.length; ++i) {
			String arg = args[i];
			if (arg.length() != 0 && arg.charAt(0) == '{' && arg.charAt(arg.length() - 1) == '}') {
				String componentCommand = arg.substring(1, arg.length() - 1);
				String[] componentXCommand = componentCommand.split(":");
				if (componentXCommand.length == 3) {
					Component component = Component.getComponent(componentXCommand[0]);
					ComponentMethodData componentMethodData = new ComponentMethodData(0, getCommandWithFunctionExecuted(executorName, componentXCommand[2].split(",")));
					component.applyFunction(componentXCommand[1], componentMethodData);
					/**
					Consumer<ComponentMethodData> componentFunction = component.getFunction(componentXCommand[1]);
					componentFunction.accept(componentMethodData);
					*/
					if (componentMethodData.getResult() == null) Loader.log(Level.SEVERE, "command function executed " + Arrays.toString(args));
					else args[i] = componentMethodData.getResult();
				}
				else {
					ComponentMethodData componentMethodData = new ComponentMethodData(0, getCommandWithFunctionExecuted(executorName, componentXCommand[1].split(",")));
					Component.applyGlobalFunction(componentXCommand[0], componentMethodData);
					if (componentMethodData.getResult() == null) Loader.log(Level.SEVERE, "command function executed " + Arrays.toString(args));
					else args[i] = componentMethodData.getResult();
				}
			}
		}
		
		return args;
	}
	
	public static String[] getCommandWithEFlag(String[] args, String executorName) {
		for (int i = 0; i < args.length; ++i) {
			if (args[i].equals("@e")) args[i] = executorName;
		}
		return args;
	}
	
	public static MultiplyCommand getMultiplyCommand(String[] args) {
		
		List<String> values = null;
		
		for (int i = 0; i < args.length; ++i) {
			String str = args[i];
			if (values != null) {
				if (str.equals("@m")) return new MultiplyCommand(values, Util.connectStringsFromArrayWithCut(args, 0, i - values.size() - 1, " "), 
						Util.connectStringsFromArrayWithCut(args, i + 1, args.length, " "));
				else values.add(str);
			}
			else {
				if (str.equals("@m")) values = new ArrayList<>();
			}
		}
		
		return null;
		
	}
	
	protected static CommandBlock dispatchCommandBlock(CommandBlock commandBlock, String executorName) {
		
		dispatchCommand(Bukkit.getConsoleSender(), commandBlock.getCommand(), executorName);
		
		BlockFace blockFace = ((Directional) commandBlock.getBlockData()).getFacing();
		Location nextCommandBlockLocation = new Location(commandBlock.getLocation().getWorld(), commandBlock.getLocation().getX() + blockFace.getModX(), 
			commandBlock.getLocation().getY() + blockFace.getModY(), commandBlock.getLocation().getZ() + blockFace.getModZ());
		
		if (nextCommandBlockLocation.getBlock().getState() instanceof CommandBlock) {
			return (CommandBlock) nextCommandBlockLocation.getBlock().getState();
		}
		return null;
		
	}
	
	public static void dispatchCommand(CommandSender commandSender, String command, String executorName) {
		
		String[] commandArgs = command.split(" ");
		commandArgs = MinecraftUtil.modifyCommand(executorName, commandArgs);
		MultiplyCommand multCommand = getMultiplyCommand(commandArgs);
		
		if (multCommand != null) {
			multCommand.dispatchCommands(commandSender, executorName);
		}
		else {
			Bukkit.dispatchCommand(commandSender, Util.connectStringsFromArray(commandArgs, " "));
		}
		
	}
	
}