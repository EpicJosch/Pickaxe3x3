package ru.sht3nd.pickaxe3x3api.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public final class PickaxeTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase("pickaxe3x3")) {
            return Collections.emptyList();
        }
        if (args.length == 1) {
            String prefix = args[0].toLowerCase(Locale.ROOT);
            List<String> suggestions = new ArrayList<>();
            for (String pickaxe : new String[]{"diamond", "netherite"}) {
                if (pickaxe.startsWith(prefix)) {
                    suggestions.add(pickaxe);
                }
            }
            return suggestions;
        }
        if (args.length == 2) {
            String prefix = args[1].toLowerCase(Locale.ROOT);
            List<String> out = new ArrayList<>();
            sender.getServer().getOnlinePlayers().forEach(p -> {
                String name = p.getName();
                if (name.toLowerCase(Locale.ROOT).startsWith(prefix)) {
                    out.add(name);
                }
            });
            return out;
        }
        return Collections.emptyList();
    }
}