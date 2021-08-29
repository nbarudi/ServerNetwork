package ca.bungo.core.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import ca.bungo.core.api.CoreAPI;
import ca.bungo.core.api.CoreAPI.PlayerInfo;
import ca.bungo.core.cmds.CoreCommands;
import ca.bungo.core.cmds.Administration.ExperienceCommand;
import ca.bungo.core.cmds.Administration.GBanCommand;
import ca.bungo.core.cmds.Administration.GMCommand;
import ca.bungo.core.cmds.Administration.KickCommand;
import ca.bungo.core.cmds.Administration.MuteCommand;
import ca.bungo.core.cmds.Administration.RankCommand;
import ca.bungo.core.cmds.Administration.ReloadCommand;
import ca.bungo.core.cmds.Administration.SBanCommand;
import ca.bungo.core.cmds.Administration.TPCommand;
import ca.bungo.core.cmds.Administration.UnMuteCommand;
import ca.bungo.core.cmds.Administration.VanishCommand;
import ca.bungo.core.cmds.Player.InfoCommand;
import ca.bungo.core.cmds.Player.NameCommand;
import ca.bungo.core.cmds.Player.WhisperCommand;
import ca.bungo.core.events.ChatEvent;
import ca.bungo.core.events.PlayerEventManagement;
import net.md_5.bungee.api.ChatColor;

public class Core extends JavaPlugin {
	
	private class Database {
		
		private String host = "localhost";
		private int port = 3306;
		private String database = "";
		private String user = "";
		private String password = "";
		
		public String getHost() {
			return host;
		}


		public int getPort() {
			return port;
		}


		public String getDatabase() {
			return database;
		}


		public String getUser() {
			return user;
		}


		public String getPassword() {
			return password;
		}
		
		public String toString() {
			return host + ":" + port + " | " + database + " | " + user + " | " + password;
		}


		public Database() {
			this.host = getConfig().getString("database.host");
			this.port = getConfig().getInt("database.port");
			this.database = getConfig().getString("database.database");
			this.user = getConfig().getString("database.user");
			this.password = getConfig().getString("database.password");
		}
	}

	public ArrayList<PlayerInfo> pInfo = new ArrayList<>();
	
	public ArrayList<CoreCommands> coreCommands = new ArrayList<>();
	public ArrayList<CoreCommands> helpList = new ArrayList<>();
	public boolean useCoreChat = true;
	
	public MysqlDataSource source;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		logConsole("&aCore Systems are loading!");
		logConsole("&3Connecting to MySql Database...");
		source = initMySql();
		
		if(source == null) {
			logConsole("&4Failed to obtain MySql Database!");
			return;
		}else {
			logConsole("&aConnected to MySql Server!");
		}
		
		logConsole("&3Registering Events...");
		registerEvents();
		logConsole("&aEvents Registered...");
		logConsole("&3Registering Commands...");
		registerCommands();
		logConsole("&aCommands Registered...");
		logConsole("&3Loading Configs...");
		loadConfigs();
		logConsole("&aConfigs Loaded...");
		
		CoreAPI cAPI = new CoreAPI(this);
		logConsole("&3Registering any currently active players..");
		for(Player player : Bukkit.getOnlinePlayers()) {
			cAPI.createPlayer(player);
		}
	}
	
	@Override
	public void onDisable() {
		logConsole("&4Core Systems shuting down");
		
		CoreAPI cAPI = new CoreAPI(this);
		cAPI.saveAllPlayers();
	}
	
	private void registerEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerEventManagement(this), this);
		pm.registerEvents(new ChatEvent(this), this);
	}
	
	private void registerCommands() {
		coreCommands.add(new RankCommand(this, "Rank"));
		coreCommands.add(new InfoCommand(this, "Stats"));
		coreCommands.add(new NameCommand(this, "Name"));
		coreCommands.add(new SBanCommand(this, "SBan"));
		coreCommands.add(new GBanCommand(this, "GBan"));
		coreCommands.add(new ExperienceCommand(this, "Exp"));
		coreCommands.add(new ReloadCommand(this, "CReload"));
		coreCommands.add(new GMCommand(this, "GM"));
		coreCommands.add(new KickCommand(this, "Kick"));
		coreCommands.add(new TPCommand(this, "Teleport"));
		coreCommands.add(new WhisperCommand(this, "Whisper"));
		coreCommands.add(new VanishCommand(this, "Vanish"));
		coreCommands.add(new MuteCommand(this, "Mute"));
		coreCommands.add(new UnMuteCommand(this, "UnMute"));
		//this.getCommand("").setExecutor(null);
		
		for(CoreCommands cmd : coreCommands) {
			try {
				this.getCommand(cmd.name).setExecutor(cmd);
			}catch (Exception e) {
				logConsole("&4Failed to register command: &e" + cmd.name);
			}
		}
		helpList.addAll(coreCommands);
		coreCommands.clear();
	}
	
	public void reregisterCommands(JavaPlugin pl) {
		for(CoreCommands cmd : coreCommands) {
			try {
				pl.getCommand(cmd.name).setExecutor(cmd);
			}catch (Exception e) {
				e.printStackTrace();
				logConsole("&4Failed to register command: &e" + cmd.name);
			}
		}
		helpList.addAll(coreCommands);
		coreCommands.clear();
	}
	
	private void loadConfigs() {
		boolean coreChat = getConfig().getBoolean("core-chat");
		useCoreChat = coreChat;
	}

	
	
	public void logConsole(String message) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
	
	public void broadcast(String message) {
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&9Message> &7" + message));
	}

	
	private void testDataSource(MysqlDataSource dataSource) throws SQLException {
		try (Connection conn = dataSource.getConnection()) {
	        if (!conn.isValid(4000)) {
	            throw new SQLException("Could not establish database connection.");
	        }
	    }
	}
	
	
	private MysqlDataSource initMySql() {
		
		MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
		
		Database database = new Database();
		
		dataSource.setServerName(database.getHost());
		dataSource.setPortNumber(database.getPort());
		dataSource.setDatabaseName(database.getDatabase());
		dataSource.setUser(database.getUser());
		dataSource.setPassword(database.getPassword());
		
		
		try {
			testDataSource(dataSource);
		} catch(SQLException e) {
			logConsole("&4Something Went Wrong...");
			e.printStackTrace();
			return null;
		}
		
		return dataSource;
	}
	
}
