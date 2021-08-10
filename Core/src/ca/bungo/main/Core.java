package ca.bungo.main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import ca.bungo.api.CoreAPI;
import ca.bungo.api.CoreAPI.PlayerInfo;
import ca.bungo.cmds.CoreCommands;
import ca.bungo.cmds.Administration.RankCommand;
import ca.bungo.cmds.Administration.SBanCommand;
import ca.bungo.cmds.Player.InfoCommand;
import ca.bungo.cmds.Player.NameCommand;
import ca.bungo.events.ChatEvent;
import ca.bungo.events.PlayerEventManagement;
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
		coreCommands.add(new InfoCommand(this, "Info"));
		coreCommands.add(new NameCommand(this, "Name"));
		coreCommands.add(new SBanCommand(this, "SBan"));
		//this.getCommand("").setExecutor(null);
		
		for(CoreCommands cmd : coreCommands) {
			try {
				this.getCommand(cmd.name).setExecutor(cmd);
			}catch (Exception e) {
				logConsole("&4Failed to register command: &e" + cmd.name);
			}
		}
	}
	
	private void loadConfigs() {
		boolean coreChat = getConfig().getBoolean("core-chat");
		useCoreChat = coreChat;
	}
	
	
	public void logConsole(String message) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
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
		
		logConsole(database.toString());
		
		
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
