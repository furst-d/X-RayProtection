package by.mens.XrayProtection.eventhandlers.Database;

import by.mens.XrayProtection.Main;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.sql.*;

public class ControlDB {

    public static Connection con;

    static {
            con = Database_connection.openConnection();
    }

    public static int run(Player player, Block block, Location blocklocation) throws SQLException {
        addRecord(player, block, blocklocation);
        deleteRecord(player, block);
        int blockCount = getBlockCount(player, block);
        return blockCount;
     }

    public static void addRecord(Player player, Block block, Location blocklocation) {

        java.util.Date today = new java.util.Date();
        java.sql.Timestamp time = new java.sql.Timestamp(today.getTime());

        try {
            PreparedStatement stm = con.prepareStatement("INSERT INTO block_break (nick, block, x, y, z, world, break_date) VALUES "
                    + "('" + player.getName()
                    + "', '" + block.getType().toString()
                    + "', '" + (int)blocklocation.getX()
                    + "', '" + (int)blocklocation.getY()
                    + "', '" + (int)blocklocation.getZ()
                    + "', '" + blocklocation.getWorld().getName()
                    + "', '" + time
                    + "')");
            stm.execute();
            stm.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void getRecord(Player player) throws SQLException {
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            stm = con.prepareStatement("SELECT * FROM block_break WHERE nick = " +"'" + player.getName() + "'");
            rs = stm.executeQuery();
            player.sendMessage("§b--------------------§f");
            while (rs.next()) {
                String nick = rs.getString("nick");
                String block = rs.getString("block");
                String x = rs.getString("x");
                String y = rs.getString("y");
                String z = rs.getString("z");
                String world = rs.getString("world");
                String date = rs.getString("break_date");

                player.sendMessage(nick);
                player.sendMessage(block);
                player.sendMessage(x);
                player.sendMessage(y);
                player.sendMessage(z);
                player.sendMessage(world);
                player.sendMessage(date);
                player.sendMessage("§a---------");
            }
            player.sendMessage("§b--------------------§f\n");
        } catch (SQLException e) {
            e.printStackTrace();
            Main.getPlugin().getPluginLoader().disablePlugin(Main.getPlugin());
        }
    }

    public static int getBlockCount(Player player, Block block) throws SQLException {
        Integer days = Main.getPlugin().getConfig().getInt("Days to display");
        if(Main.getPlugin().getConfig().getBoolean("Connection.Enable")) {
            PreparedStatement stm = con.prepareStatement("SELECT COUNT(*) FROM block_break WHERE nick = " +"'" + player.getName() + "' AND block = " +"'" + block.getType() + "' AND break_date >= (now() - INTERVAL "+ days + " DAY)");
            ResultSet rs = stm.executeQuery();
            if(rs.next()) {
                int blockCount = rs.getInt(1);
                return blockCount;
            }
        }else if(!Main.getPlugin().getConfig().getBoolean("Connection.Enable")) {
            PreparedStatement stm = con.prepareStatement("SELECT COUNT(*) FROM block_break WHERE nick = " +"'" + player.getName() + "' AND block = " +"'" + block.getType() + "' AND break_date >= date('now', '-" + days + " days')");
            ResultSet rs = stm.executeQuery();
            int blockCount = rs.getInt(1);
            return blockCount;
        }
        return -1;
    }
    public static void deleteRecord(Player player, Block block) throws SQLException {
        Integer days = Main.getPlugin().getConfig().getInt("Days to display");
        if(Main.getPlugin().getConfig().getBoolean("Connection.Enable")) {
            PreparedStatement stm = con.prepareStatement("DELETE FROM block_break WHERE break_date < (now() - INTERVAL "+ days + " DAY)");
            stm.executeUpdate();
        }else if(!Main.getPlugin().getConfig().getBoolean("Connection.Enable")) {
            PreparedStatement stm = con.prepareStatement("DELETE FROM block_break WHERE break_date < date('now', '-" + days + " days')");
            stm.executeUpdate();
        }


    }
}
