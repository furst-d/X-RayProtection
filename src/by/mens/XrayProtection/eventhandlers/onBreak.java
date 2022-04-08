package by.mens.XrayProtection.eventhandlers;

import by.mens.XrayProtection.Main;
import by.mens.XrayProtection.eventhandlers.Database.ControlDB;
import by.mens.XrayProtection.methods.getBlockLocation;
import by.mens.XrayProtection.methods.getLightLevel;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.sql.SQLException;


@SuppressWarnings("Duplicates")
public class onBreak implements Listener {

    public Main pl;
    public onBreak(by.mens.XrayProtection.Main pl) {
        this.pl = pl;
    }

   @EventHandler
    public void onBreak(BlockBreakEvent event) throws SQLException {
       Player player = event.getPlayer();
       Block block = event.getBlock();
       int lightlevel = getLightLevel.run(player);
       Location blocklocation = getBlockLocation.run(block);
       String light;

       if(lightlevel == 0) {
           light = "§c" + lightlevel;
       } else if(lightlevel <=7) {
           light = "§6" + lightlevel;
       } else {
           light = "§a" + lightlevel;
            }

            if ((pl.getConfig().getBoolean( "Protection." + block.getType())) && (pl.getConfig().getBoolean("Enable"))) {

                String msgPlayer = this.pl.getConfig().getString("Messages." + block.getType())
                        .replaceAll("%PLAYER%", "" + player.getDisplayName() + "")
                        .replaceAll("%LIGHTLEVEL%", "" + light + "");

                String msgConsole = msgPlayer.replaceAll("➥", "");

                if ((player.getGameMode() == GameMode.SURVIVAL) && (!block.hasMetadata(block.getType() + " Placed"))) {
                    int block_count = ControlDB.run(player, block, blocklocation);

                    Integer days = Main.getPlugin().getConfig().getInt("Days to display");

                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        TextComponent message = new TextComponent(msgPlayer);
                        message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp " + onlinePlayer.getName() + " " + (int)blocklocation.getX() + " " + (int)blocklocation.getY() + " " + (int)blocklocation.getZ()));
                        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(player.getDisplayName()
                                + "\n§7Location: " + "\n  §7- X: " + ChatColor.RED + (int)blocklocation.getX()
                                + "\n  §7- Y: " + ChatColor.RED + (int)blocklocation.getY()
                                + "\n  §7- Z " + ChatColor.RED + (int)blocklocation.getZ()
                                + "\n  §7- World: " + ChatColor.RED + blocklocation.getWorld().getName()
                                + "\n§7Sum(-" + days + "d): " + ChatColor.RED + block_count)));
                    if(onlinePlayer.hasPermission("mens.xray")) {
                        onlinePlayer.spigot().sendMessage(message);
                    }
                }

                if(this.pl.getConfig().getBoolean("Log in console")) {
                    Bukkit.getServer().getConsoleSender().sendMessage(msgConsole);
                }
            }
        }
    }
}
