package by.mens.XrayProtection.eventhandlers.Database;


import by.mens.XrayProtection.Main;
import by.mens.XrayProtection.msg.Prefix;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.*;

public class Database_connection {

    public Main pl;
    public Database_connection(by.mens.XrayProtection.Main pl) {
        this.pl = pl;
    }

    public static Connection con = null;

    public static Connection openConnection() {
        if(Main.getPlugin().getConfig().getBoolean("Connection.Enable")) {

            String HOST = Main.getPlugin().getConfig().getString("Connection.Host");
            String PORT = Main.getPlugin().getConfig().getString("Connection.Port");
            String DATABASE = Main.getPlugin().getConfig().getString("Connection.Database");
            String USER = Main.getPlugin().getConfig().getString("Connection.User");
            String PASSWORD = Main.getPlugin().getConfig().getString("Connection.Password");

            String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;

            try {
                con = DriverManager.getConnection(URL, USER, PASSWORD);
                createTable();
                Bukkit.getConsoleSender().sendMessage(Prefix.prefix + "Connection to §eMYSQL §bsucceeded!");
            } catch (SQLException e) {
                e.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(Prefix.prefix + "Connection to §eMYSQL §bfailed!");
                Main.getPlugin().getPluginLoader().disablePlugin(Main.getPlugin());
            }
        }

        else if(!Main.getPlugin().getConfig().getBoolean("Connection.Enable")) {

            File file = new File(Main.getPlugin().getDataFolder(), "database.db");
            String URL = "jdbc:sqlite:" + file;
            try {
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection(URL);
                createTable();
                Bukkit.getConsoleSender().sendMessage(Prefix.prefix + "Connection to §eSQLite §bsucceeded!");
            } catch (Exception e) {
                e.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(Prefix.prefix + "Connection to §eSQLite §bfailed!");
                Main.getPlugin().getPluginLoader().disablePlugin(Main.getPlugin());
            }
        }
        else {
            Main.getPlugin().getPluginLoader().disablePlugin(Main.getPlugin());
        }
        return con;
    }


    public static void createTable() {
        try {
            PreparedStatement stm = con.prepareStatement("CREATE TABLE IF NOT EXISTS block_break (nick VARCHAR(32), block VARCHAR(32), x INT(16), y INT(16), z INT(16), world VARCHAR(32), break_date TIMESTAMP)");
            stm.execute();
            stm.close();
            Bukkit.getConsoleSender().sendMessage(Prefix.prefix + "Table §eblock_break §bhas been successfully created / loaded!");
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(Prefix.prefix + "Can not create table");
            Main.getPlugin().getPluginLoader().disablePlugin(Main.getPlugin());
        }
    }

    public static void close() {
        if(con != null) {
            try {
                con.close();
                con = null;
                Bukkit.getConsoleSender().sendMessage(Prefix.prefix + "The connection to the database has been closed!");
            } catch (SQLException e) {
                e.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(Prefix.prefix + "Could not close connection!");
            }
        }
    }
}
