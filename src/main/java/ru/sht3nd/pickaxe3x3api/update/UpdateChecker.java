package ru.sht3nd.pickaxe3x3api.update;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public final class UpdateChecker {
    private final JavaPlugin plugin;
    private final int resourceId;

    public UpdateChecker(JavaPlugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public void checkForUpdates() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setRequestProperty("User-Agent", "Pickaxe3x3Api UpdateChecker");

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String latest = reader.readLine();
                    if (latest != null && !latest.trim().isEmpty()) {
                        String current = plugin.getDescription().getVersion();
                        String cleanCurrent = current.replaceAll("[^\\d.]", "").trim();
                        String cleanLatest = latest.trim().replaceAll("[^\\d.]", "").trim();
                        if (!cleanCurrent.equalsIgnoreCase(cleanLatest)) {
                            plugin.getLogger().log(Level.WARNING,
                                    "Update available for " + plugin.getName() + ": " + latest.trim()
                                            + " (current: " + current + ")");
                        }
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.FINE, "Update check failed", e);
            }
        });
    }
}