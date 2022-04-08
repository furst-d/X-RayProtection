package by.mens.XrayProtection.methods;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class getBlockLocation {
    public static Location run(Block block) {
        Location bloc = block.getLocation();
        Location blocklocation = new Location(bloc.getWorld(), bloc.getX(), bloc.getY(), bloc.getZ());
        return blocklocation;
    }
}
