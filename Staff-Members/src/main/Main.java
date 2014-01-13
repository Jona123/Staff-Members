package main;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
  implements Listener
{
  public final Logger logger = Logger.getLogger("Minecraft");
  public static Main plugin;

  public void onEnable()
  {
    PluginDescriptionFile pdfFile = getDescription();
    this.logger.info(pdfFile.getName() + " Succesfully started up." + pdfFile.getVersion());
    Bukkit.getServer().getPluginManager().registerEvents(this, this);
    getConfig().options().copyDefaults(true);
    getConfig();
    saveConfig();
  }

  public void onDisable() {
    PluginDescriptionFile pdfFile = getDescription();
    this.logger.info(pdfFile.getName() + " Succesfully shutted down.");
  }

  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
  {
    if (commandLabel.equalsIgnoreCase("staff")) {
      if (args.length == 0) {
        if (sender.hasPermission("staffs.staff")) {
          sender.sendMessage("§6Owner(s): " + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Owner")));
          sender.sendMessage("§1Admin(s): " + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Admin")));
          sender.sendMessage("§aModerator(s): " + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Mod")));
          sender.sendMessage("§cOn the server: " + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Server")));
        } else {
          sender.sendMessage(ChatColor.RED + "You don't have permissions to do this!");
          return true;
        }

        return false;
      }
      if (args.length == 1) {
        if (args[0].equalsIgnoreCase("help")) {
          if (sender.hasPermission("staffs.staff")) {
            if (args[0].equalsIgnoreCase("help")) {
              sender.sendMessage("§f[Staffs version§b " + getDescription().getVersion() + "§f] §3This is the help menu of Staff-Members, made by DoraKlikOpDora (bram3535)");
              sender.sendMessage("§6/staff §bshows you the staff of the server");
              sender.sendMessage("§6/staff help §bshows you this menu");
              sender.sendMessage("§6/staff reload §breloads the staff config");
              return true;
            }
          } else {
            sender.sendMessage("§cYou dont have permission to do this!");
            return true;
          }
        } else if (args[0].equalsIgnoreCase("reload")) {
          if (sender.hasPermission("staffs.admin")) {
            reloadConfig();
            sender.sendMessage("§dThe staff config is reloaded!");
            return true;
          }
        } else {
          sender.sendMessage("§cThis command doesn't exist! Use /staff help for info!");
          return true;
        }
      }
    }

    return false;
  }

  @EventHandler
  public void onPlayerPlace(SignChangeEvent e) {
    if ((e.getPlayer().hasPermission("staffs.admin")) && 
      (e.getLine(0).contains("[staff]"))) {
      e.setLine(0, "§a[§bStaff§a]");
      e.setLine(1, "§eClick here to");
      e.setLine(2, "§esee the");
      e.setLine(3, "§eserver's staff");
    }
  }

  @EventHandler
  public void interact(PlayerInteractEvent e) {
    Player p = e.getPlayer();
    if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) && 
      (p.hasPermission("staffs.staff")) && (
      (e.getClickedBlock().getType().equals(Material.WALL_SIGN)) || (e.getClickedBlock().getType().equals(Material.SIGN_POST)))) {
      Sign sign = (Sign)e.getClickedBlock().getState();
      if ((sign.getLine(0).contains("§a[§bStaff§a]")) && (sign.getLine(1).contains("§eClick here to")) && (p.hasPermission("staffs.staff"))) {
        p.sendMessage("§6Owner(s): " + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Owner")));
        p.sendMessage("§1Admin(s): " + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Admin")));
        p.sendMessage("§aModerator(s): " + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Mod")));
        p.sendMessage("§cOn the server: " + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Server")));
      }
    }
  }
}