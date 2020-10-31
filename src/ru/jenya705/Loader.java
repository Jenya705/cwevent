package ru.jenya705;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import ru.jenya705.command.DispatchChainCommand;
import ru.jenya705.command.DispatchCommand;
import ru.jenya705.command.ExecuteModifiedCommand;
import ru.jenya705.command.FinishEventCommand;
import ru.jenya705.command.GiveChestCommand;
import ru.jenya705.command.IfCommand;
import ru.jenya705.command.LockPlayerCommand;
import ru.jenya705.command.RegisterComponentCommand;
import ru.jenya705.command.RegisterEventCommand;
import ru.jenya705.command.SendMessageCommand;
import ru.jenya705.command.SetVarCommand;
import ru.jenya705.component.TriggerComponent;
import ru.jenya705.component.TriggerOnceComponent;
import ru.jenya705.component.TriggerPlayerOnceComponent;

public class Loader extends JavaPlugin {

	private static Logger logger;
	private static Loader loader;
	
	public Loader() {
		
		Loader.logger = getLogger();
		Loader.loader = this;
		
	}
	
	@Override
	public void onEnable() {
		
		createPluginFolder();
		
		GameEvent.loadEvents();
		
		setCommand("executemod", new ExecuteModifiedCommand());
		setCommand("sendmsg", new SendMessageCommand());
		setCommand("regcomp", new RegisterComponentCommand());
		setCommand("lockpl", new LockPlayerCommand());
		setCommand("regevent", new RegisterEventCommand());
		setCommand("dispatch", new DispatchCommand());
		setCommand("finishevent", new FinishEventCommand());
		setCommand("dispatchchain", new DispatchChainCommand());
		setCommand("chgive", new GiveChestCommand());
		setCommand("if", new IfCommand());
		setCommand("setvar", new SetVarCommand());
		
		(new TriggerComponent()).registerComponent();
		(new TriggerOnceComponent()).registerComponent();
		(new TriggerPlayerOnceComponent()).registerComponent();
		
	}
	
	@Override
	public void onDisable() {
		
	}
	
	private void createPluginFolder() {
		getDataFolder().mkdir();
	}
	
	public void setCommand(String command, Object executor) {
		if (executor instanceof CommandExecutor) {
			getCommand(command).setExecutor((CommandExecutor)executor); 
		}
		if (executor instanceof TabExecutor) {
			getCommand(command).setTabCompleter((TabExecutor)executor);
		}
		if (executor instanceof Listener) {
			getServer().getPluginManager().registerEvents((Listener) executor, this);
		}
	}
	
	public static void log(Level level, String message) {
		Loader.logger.log(level, message);
	}
	public static Loader getLoader() {
		return Loader.loader;
	}
	
	
}
