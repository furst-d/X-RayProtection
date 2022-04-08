package by.mens.XrayProtection;

import by.mens.XrayProtection.eventhandlers.*;
import by.mens.XrayProtection.eventhandlers.Database.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;



public class Main extends JavaPlugin {

    PluginManager pm = Bukkit.getPluginManager();


    @Override
    public void onEnable() {
        getLogger().info("Plugin enabled!");

        loadCommand();
        loadEvent();
        loadConfig();
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin closed!");

        Database_connection.close();

    }

    public void loadCommand() {

    }

    public void loadEvent() {
        pm.registerEvents(new onBreak(this), this);
        pm.registerEvents(new onPlace(this), this);
    }

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }
    public static Main getPlugin() {
        return Main.getPlugin(Main.class);
    }

}
