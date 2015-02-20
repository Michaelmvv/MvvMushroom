package michaelmvv.mushroom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.material.Wool;
import org.bukkit.plugin.java.JavaPlugin;

public class MvvPlugin extends JavaPlugin implements Listener {
	HashSet<UUID> enabled_users = new HashSet<UUID>();
	Logger log;
	boolean hasUpdate=false;
	String ver;

	@Override
	public void onEnable() {
		
		log = Bukkit.getLogger();
		log.info("MvvMushroom Plugin Starting");
		getServer().getPluginManager().registerEvents(this, this);
		ver = this.getDescription().getVersion();
		UpdateCheck();
	}

	@Override
	public void onDisable() {

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (command.getName().equalsIgnoreCase("ToggleMushroom")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (p.hasPermission("mushroom.toggle")) {

					if (enabled_users.contains(p.getUniqueId())) {
						enabled_users.remove(p.getUniqueId());
					} else {
						enabled_users.add(p.getUniqueId());
					}
				}
			}
		}
		if (command.getName().equalsIgnoreCase("MushroomUpdateCheck")) {
			if (sender instanceof Player) {
				if (sender.hasPermission("mushroom.updatecheck")) {
					UpdateCheck();
				}
			}
		}

		return false;
	}

	public void UpdateCheck() {
		ArrayList<String> information = new ArrayList<String>();
		try {
			URL remoteFile = new URL(
					"https://raw.githubusercontent.com/Michaelmvv/MvvMushroom/master/Update%20and%20DL/Update.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					remoteFile.openStream()));
			for (int i = 0; i < 3; i++) {
				information.add(reader.readLine());
			}
		} catch (Exception e) {
			log.info("Could not get update info, lets assume that there is no update");
		}

		if (information.get(0).equalsIgnoreCase(ver)) {
			log.info("No update found");
		} else {
			log.info(ChatColor.RED + "OpToggle (Ver. " + ver
					+ " ) is not in sync with github (Ver. "
					+ information.get(0) + ")");
			log.info("Get the update at: " + ChatColor.GREEN
					+ "https://github.com/Michaelmvv/MvvMushroom");
			hasUpdate = true;
		}

	}

	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (enabled_users.contains(event.getPlayer().getUniqueId())) {
			Material block = event.getBlock().getType();

			if (block == Material.WOOL) {

				try {
					Wool wool = (Wool) event.getBlock();
					log.info("Its Wool!");
					if (wool.getColor() == DyeColor.RED) {
						event.getBlock().setType(Material.RED_MUSHROOM);
						event.getBlock().setTypeId(15);
					}
					if (wool.getColor()==DyeColor.BROWN) {
						event.getBlock().setType(Material.BROWN_MUSHROOM);
						event.getBlock().setTypeId(15);
					}
					if (wool.getColor()== DyeColor.WHITE) {
						
					}

				} catch (Exception e) {
					log.warning("Oh NO!!!");

				}
			}
		}
	}
}
