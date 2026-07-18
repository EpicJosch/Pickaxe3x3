package ru.sht3nd.pickaxe3x3api.message;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class MessageService {
    private final JavaPlugin plugin;
    private final Map<String, String> messages;

    public MessageService(JavaPlugin plugin) {
        this.plugin = plugin;
        this.messages = load();
    }

    public String get(String key) {
        return messages.getOrDefault(key, key);
    }

    private Map<String, String> load() {
        String lang = plugin.getConfig().getString("language", "ru");
        if (lang == null) {
            lang = "ru";
        }
        lang = lang.toLowerCase();

        String fileName = "messages_" + lang + ".yml";
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }

        YamlConfiguration cfg = loadUtf8(file);
        Map<String, String> map = new HashMap<>();

        for (String key : cfg.getKeys(false)) {
            String value = cfg.getString(key, key);
            if (value != null) {
                map.put(key, ChatColor.translateAlternateColorCodes('&', value));
            }
        }

        return Collections.unmodifiableMap(map);
    }

    private YamlConfiguration loadUtf8(File file) {
        try (Reader reader = new InputStreamReader(
                Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
            return YamlConfiguration.loadConfiguration(reader);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not load " + file.getName() + ": " + e.getMessage());
            return new YamlConfiguration();
        }
    }
}