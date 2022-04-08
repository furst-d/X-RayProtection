package by.mens.XrayProtection.eventhandlers;

import by.mens.XrayProtection.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class onPlace implements Listener {

    public Main pl;
    public onPlace(by.mens.XrayProtection.Main pl) {
        this.pl = pl;
    }

    @EventHandler
    public void OnPlace(BlockPlaceEvent event) {
        if ((pl.getConfig().getBoolean( "Protection." + event.getBlock().getType() )) && (pl.getConfig().getBoolean("Enable"))) {
            event.getBlock().setMetadata(event.getBlock().getType() + " Placed", new FixedMetadataValue(pl, Boolean.valueOf(true)));
        }
    }
}
