package ca.bungo.hardcore.skills.Autosmelt;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.skills.Skill;
import ca.bungo.hardcore.util.player.PlayerData;

public class AutoSmelt3 extends Skill {
	
	
	public AutoSmelt3(Hardcore hardcore, String name) {
		super(hardcore, name);
		this.desc = "Automatically smelt ores you break! (Max Fortune: 2)";
		this.tier = 3;
		this.cost = 4;
		this.family = "ASM";
		this.requires = "Autosmelt 2";
	}
	
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		PlayerData data = hardcore.pm.getPlayerData(player);
		if(!data.hasPerk(this.name) || data.hasHigherPerk(this))
			return;
		
		if(!event.getBlock().getType().toString().contains("ORE"))
			return;
		
		Block block = event.getBlock();
		Location loc = block.getLocation();
		World world = loc.getWorld();
		
		Random rnd = new Random();
		
		int fMult = rnd.nextInt(getFortuneLevel(player.getInventory().getItemInMainHand()) + 1);
		
		switch(event.getBlock().getType()) {
		case IRON_ORE:
			if(!checkWeapon(player.getInventory().getItemInMainHand()))
				return;
			ItemStack item = new ItemStack(Material.IRON_INGOT);
			item.setAmount(1 + fMult);
			event.setDropItems(false);
			world.dropItemNaturally(loc, item);
			world.spawnParticle(Particle.FLAME, loc.getX(), loc.getY(), loc.getZ(), 25, 0.1, 0.1, 0.1);
			break;
		case GOLD_ORE:
			if(!checkWeapon(player.getInventory().getItemInMainHand()))
				return;
			item = new ItemStack(Material.GOLD_INGOT);
			item.setAmount(1 + fMult);
			event.setDropItems(false);
			world.dropItemNaturally(loc, item);
			world.spawnParticle(Particle.FLAME, loc.getX(), loc.getY(), loc.getZ(), 25, 0.1, 0.1, 0.1);
			break;
		default:
			break;
		}
	}
	
	private boolean checkWeapon(ItemStack weapon) {
		if(weapon.getType().toString().contains("PICKAXE")) 
			return !weapon.getEnchantments().containsKey(Enchantment.SILK_TOUCH);
		return false;
	}
	
	private int getFortuneLevel(ItemStack tool) {
		ItemMeta meta = tool.getItemMeta();
		if(meta == null)
			return 0;
		int level = meta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);
		if(level > 2)
			level = 2;
		return level;
	}
}
