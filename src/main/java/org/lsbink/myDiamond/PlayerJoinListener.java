package org.lsbink.myDiamond;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerJoinListener implements Listener {

    private final Main plugin;

    public PlayerJoinListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        int balance = 0;

        try (Connection connection = plugin.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT balance FROM diamond_balance WHERE uuid = ?")) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                balance = rs.getInt("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (balance > 0) {
            event.getPlayer().sendMessage(ChatColor.YELLOW + "У вас на балансе " + balance + " алмазов! Используйте /mydiamond для вывода.");
        }
    }
}
