package ca.bungo.core.api;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import ca.bungo.core.core.Core;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class EconomyAPI {
	
	public boolean enabled = false;
	
	private CoreAPI cAPI;
	
	private Economy econ;
	
	public EconomyAPI(Core core) {
		if(!core.getConfig().getBoolean("core-economy")) {
			enabled = false;
			return;
		}
		else if(!setupEconomy()) {
			enabled = false;
			return;
		}
		this.cAPI = new CoreAPI(core);
	}
	
	private boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	public boolean depositPlayer(String username, double amount) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(cAPI.getPlayerInfo(Bukkit.getPlayer(username)).uuid));
		
		EconomyResponse resp = econ.depositPlayer(player, amount);
		if(!(resp.type.equals(ResponseType.SUCCESS)))
			return false;
		return true;
	}
	
	public boolean withdrawPlayer(String username, double amount) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(cAPI.getPlayerInfo(Bukkit.getPlayer(username)).uuid));
		
		EconomyResponse resp = econ.withdrawPlayer(player, amount);
		if(!(resp.type.equals(ResponseType.SUCCESS)))
			return false;
		return true;
	}
	
	public boolean hasCredits(String username, double amount) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(cAPI.getPlayerInfo(Bukkit.getPlayer(username)).uuid));
		return econ.has(player, amount);
	}
	
	public double getBalance(String username) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(cAPI.getPlayerInfo(Bukkit.getPlayer(username)).uuid));
		return econ.getBalance(player);
	}

}
