package org.lsbink.myDiamond;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Main extends JavaPlugin {

    private Connection connection;

    @Override
    public void onEnable() {
        connectToDatabase();
        createTable();
        getCommand("givediamond").setExecutor(new GiveDiamondCommand());
        getCommand("mydiamond").setExecutor(new MyDiamondCommand());

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getLogger().info("DiamondBalance Plugin Enabled!");
    }

    @Override
    public void onDisable() {
        closeDatabase();
        getLogger().info("DiamondBalance Plugin Disabled!");
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:plugins/DiamondBalance.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS diamond_balance (uuid TEXT PRIMARY KEY, balance INTEGER)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void closeDatabase() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public class GiveDiamondCommand implements TabExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Использование: /givediamond {игрок} {кол-во}");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Игрок не найден!");
                return true;
            }

            int amount;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Введите корректное число!");
                return true;
            }

            if (amount <= 0) {
                sender.sendMessage(ChatColor.RED + "Количество должно быть больше 0!");
                return true;
            }

            UUID uuid = target.getUniqueId();
            try (PreparedStatement stmt = getConnection().prepareStatement(
                    "INSERT INTO diamond_balance (uuid, balance) VALUES (?, ?) ON CONFLICT(uuid) DO UPDATE SET balance = balance + ?")) {
                stmt.setString(1, uuid.toString());
                stmt.setInt(2, amount);
                stmt.setInt(3, amount);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage(ChatColor.RED + "Ошибка базы данных!");
                return true;
            }

            sender.sendMessage(ChatColor.GREEN + "Вы начислили " + amount + " алмазов игроку " + target.getName() + ".");
            target.sendMessage(ChatColor.YELLOW + "Вам начислено " + amount + " алмазов! Введите /mydiamond для вывода.");

            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            if (args.length == 1) {
                return null; // Показывать список онлайн-игроков
            }
            return Collections.emptyList();
        }
    }

    public class MyDiamondCommand implements TabExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Эту команду может использовать только игрок!");
                return true;
            }

            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            int balance = 0;

            try (PreparedStatement stmt = getConnection().prepareStatement(
                    "SELECT balance FROM diamond_balance WHERE uuid = ?")) {
                stmt.setString(1, uuid.toString());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    balance = rs.getInt("balance");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                player.sendMessage(ChatColor.RED + "Ошибка базы данных!");
                return true;
            }

            if (balance <= 0) {
                player.sendMessage(ChatColor.RED + "У вас нет алмазов на балансе.");
                return true;
            }

            player.getInventory().addItem(new ItemStack(Material.DIAMOND, balance));

            try (PreparedStatement stmt = getConnection().prepareStatement(
                    "UPDATE diamond_balance SET balance = 0 WHERE uuid = ?")) {
                stmt.setString(1, uuid.toString());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                player.sendMessage(ChatColor.RED + "Ошибка базы данных!");
                return true;
            }

            player.sendMessage(ChatColor.GREEN + "Вы забрали " + balance + " алмазов!");

            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            return Collections.emptyList();
        }
    }
}
