package by.mens.XrayProtection.methods;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class getLightLevel {

    public static int run(Player player) {
        Location ploc = player.getLocation();
        Location playerlocation = new Location(ploc.getWorld(), ploc.getX(), ploc.getY(), ploc.getZ());
        int LightLevel = playerlocation.getBlock().getLightLevel();
        return LightLevel;
    }
}
