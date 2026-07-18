package ru.sht3nd.pickaxe3x3api;

import org.bstats.bukkit.Metrics;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import ru.sht3nd.pickaxe3x3api.command.PickaxeCommand;
import ru.sht3nd.pickaxe3x3api.command.PickaxeTabCompleter;
import ru.sht3nd.pickaxe3x3api.listener.PickaxeListener;
import ru.sht3nd.pickaxe3x3api.message.MessageService;
import ru.sht3nd.pickaxe3x3api.update.UpdateChecker;

public final class Pickaxe3x3Api extends JavaPlugin {
    private MessageService messages;
    private NamespacedKey pickaxeKey;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        pickaxeKey = new NamespacedKey(this, "pickaxeType");
        messages = new MessageService(this);
        getServer().getPluginManager().registerEvents(new PickaxeListener(this), this);

        PluginCommand command = getCommand("pickaxe3x3");
        if (command != null) {
            command.setExecutor(new PickaxeCommand(this));
            command.setTabCompleter(new PickaxeTabCompleter());
        } else {
            getLogger().warning("Command 'pickaxe3x3' is missing from plugin.yml.");
        }

        getLogger().info(messages.get("enabled"));
        new UpdateChecker(this, 117357).checkForUpdates();
        new Metrics(this, 22691);
    }

    @Override
    public void onDisable() {
        if (messages != null) {
            getLogger().info(messages.get("disabled"));
        }
    }

    public MessageService messages() {
        return messages;
    }

    public NamespacedKey pickaxeKey() {
        return pickaxeKey;
    }
}