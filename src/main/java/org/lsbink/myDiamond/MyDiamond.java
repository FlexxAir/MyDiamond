package org.lsbink.myDiamond;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.UUID;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MyDiamond extends JavaPlugin implements Listener {
    private Connection connection;
    private HashMap<UUID, Long> lastReminder = new HashMap<>();
    private int reminderInterval;
    private FileConfiguration langConfig;
    private String language;

    @Override
    public void onEnable() {
        loadLanguage();
        loadConfigValues();
        saveDefaultConfig();
        checkForUpdates();
        getCommand("mydiamond").setTabCompleter(new MyDiamondTabCompleter());
        getLogger().info("MyDiamond Enabled!");
        connectDatabase();
        getCommand("mydiamond").setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);
        startReminderTask();
    }
    private static final int RESOURCE_ID = 122920;

    private void checkForUpdates() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + RESOURCE_ID);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(5000);
                connection.setConnectTimeout(5000);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String latestVersion = reader.readLine();
                reader.close();

                if (latestVersion != null && !latestVersion.isEmpty()) {
                    String currentVersion = getDescription().getVersion();
                    if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                        getLogger().warning(getMessage("update.available")
                                .replace("%current%", currentVersion)
                                .replace("%latest%", latestVersion));
                        getLogger().warning(getMessage("update.download")
                                .replace("%resource_id%", String.valueOf(RESOURCE_ID)));
                    } else {
                        getLogger().info(getMessage("update.up_to_date"));
                    }
                }
            } catch (IOException e) {
                getLogger().severe(getMessage("update.error").replace("%error%", e.getMessage()));
            }
        });
    }

    private String getMessage(String path) {
        return ChatColor.translateAlternateColorCodes('&', langConfig.getString(path, path));
    }


    @Override
    public void onDisable() {
        getLogger().info("MyDiamond Disabled!");
        disconnectDatabase();
    }

    private void connectDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:plugins/MyDiamond/database.db");
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS diamonds (player TEXT PRIMARY KEY, amount INTEGER)");
            statement.close();
        } catch (SQLException e) {
            getLogger().severe("Database connection error: " + e.getMessage());
        }
    }

    private void disconnectDatabase() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            getLogger().severe("Error closing the Database connection: " + e.getMessage());
        }
    }

    private void loadLanguage() {
        language = getConfig().getString("language", "ru_RU");
        File langFolder = new File(getDataFolder(), "lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        File langFile = new File(langFolder, language + ".yml");
        if (!langFile.exists()) {
            saveResource("lang/" + language + ".yml", false);
        }

        langConfig = YamlConfiguration.loadConfiguration(langFile);
    }

    private String getMessage(String key, String... placeholders) {
        String message = langConfig.getString("messages." + key, "§c[Localization error: " + key + "]");
        for (int i = 0; i < placeholders.length; i += 2) {
            message = message.replace(placeholders[i], placeholders[i + 1]);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private void loadConfigValues() {
        reminderInterval = getConfig().getInt("reminder-interval", 5) * 60;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (getDiamonds(player.getName()) > 0) {
            player.sendMessage(getMessage("messages.reminder"));
        }
    }

    private void startReminderTask() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (getDiamonds(player.getName()) > 0) {
                    UUID uuid = player.getUniqueId();
                    long lastTime = lastReminder.getOrDefault(uuid, 0L);
                    if (System.currentTimeMillis() / 1000 - lastTime >= reminderInterval) {
                        player.sendMessage(getMessage("messages.reminder"));
                        lastReminder.put(uuid, System.currentTimeMillis() / 1000);
                    }
                }
            }
        }, 20 * 60, 20 * 60);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mydiamond")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    if (!sender.hasPermission("mydiamond")) {
                        sender.sendMessage(getMessage("messages.no_permission"));
                        return true;
                    }
                    Player player = (Player) sender;
                    int diamonds = getDiamonds(player.getName());
                    if (diamonds > 0) {
                        removeDiamonds(player.getName(), diamonds);
                        player.getInventory().addItem(new ItemStack(Material.DIAMOND, diamonds));
                        player.sendMessage(getMessage("collect_success", "%amount%", String.valueOf(diamonds)));
                    } else {
                        player.sendMessage(getMessage("messages.no_diamonds"));
                    }
                } else {
                    sender.sendMessage(getMessage("messages.console_player"));
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("mydiamond.reload")) {
                    sender.sendMessage(getMessage("messages.no_permission"));
                    return true;
                }
                reloadConfig();
                loadLanguage();
                sender.sendMessage(getMessage("messages.reload_success"));
                return true;
            }

            if (args[0].equalsIgnoreCase("see")) {
                if (!sender.hasPermission("mydiamond.see")) {
                    sender.sendMessage(getMessage("messages.no_permission"));
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(getMessage("messages.command_usage"));
                    return true;
                }
                String targetPlayer = args[1];
                int diamonds = getDiamonds(targetPlayer);
                sender.sendMessage(getMessage("diamonds_balance", "%player%", targetPlayer, "%amount%", String.valueOf(diamonds)));
                return true;
            }

            if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("take")) {
                if (args[0].equalsIgnoreCase("give") && !sender.hasPermission("mydiamond.give")) {
                    sender.sendMessage(getMessage("messages.no_permission"));
                    return true;
                }
                if (args[0].equalsIgnoreCase("take") && !sender.hasPermission("mydiamond.take")) {
                    sender.sendMessage(getMessage("messages.no_permission"));
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage(getMessage("messages.command_usage"));
                    return true;
                }
                String targetPlayer = args[1];

                int amount;
                try {
                    amount = Integer.parseInt(args[2]);
                    if (amount <= 0) {
                        sender.sendMessage(getMessage("messages.invalid_amount"));
                        return true;
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(getMessage("messages.invalid_number"));
                    return true;
                }

                if (args[0].equalsIgnoreCase("give")) {
                    addDiamonds(targetPlayer, amount);
                    sender.sendMessage(getMessage("give_success", "%player%", targetPlayer, "%amount%", String.valueOf(amount)));
                    return true;
                }

                if (args[0].equalsIgnoreCase("take")) {
                    removeDiamonds(targetPlayer, amount);
                    sender.sendMessage(getMessage("take_success", "%player%", targetPlayer, "%amount%", String.valueOf(amount)));
                    return true;
                }
            }

            if (args[0].equalsIgnoreCase("collect")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(getMessage("messages.console_player"));
                    return true;
                }
                if (!sender.hasPermission("mydiamond.collect")) {
                    sender.sendMessage(getMessage("messages.no_permission"));
                    return true;
                }
                Player player = (Player) sender;
                int diamonds = getDiamonds(player.getName());
                if (diamonds > 0) {
                    removeDiamonds(player.getName(), diamonds);
                    player.getInventory().addItem(new ItemStack(Material.DIAMOND, diamonds));
                    player.sendMessage(getMessage("collect_success", "%amount%", String.valueOf(diamonds)));
                } else {
                    player.sendMessage(getMessage("messages.no_diamonds"));
                }
                return true;
            }

            sender.sendMessage(getMessage("messages.command_usage"));
            return true;
        }
        return false;
    }




    private void removeDiamonds(String playerName, int amount) {
        try {
            int currentDiamonds = getDiamonds(playerName);
            if (currentDiamonds < amount) {
                amount = currentDiamonds;
            }
            PreparedStatement stmt = connection.prepareStatement("UPDATE diamonds SET amount = amount - ? WHERE player = ?");
            stmt.setInt(1, amount);
            stmt.setString(2, playerName);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            getLogger().severe("Error when removing diamonds: " + e.getMessage());
        }
    }


    private int getDiamonds(String playerName) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT amount FROM diamonds WHERE player = ?");
            stmt.setString(1, playerName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("amount");
            }
        } catch (SQLException e) {
            getLogger().severe("Error when getting the number of diamonds: " + e.getMessage());
        }
        return 0;
    }

    private void addDiamonds(String playerName, int amount) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO diamonds (player, amount) VALUES (?, ?) ON CONFLICT(player) DO UPDATE SET amount = amount + ?");
            stmt.setString(1, playerName);
            stmt.setInt(2, amount);
            stmt.setInt(3, amount);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            getLogger().severe("Error when adding diamonds: " + e.getMessage());
        }
    }
}