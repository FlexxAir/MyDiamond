package org.lsbink.myDiamond;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyDiamondTabCompleter implements TabCompleter {

    private static final List<String> MAIN_COMMANDS = Arrays.asList("give", "take", "see", "reload");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // Подсказки для главной команды
            StringUtil.copyPartialMatches(args[0], MAIN_COMMANDS, completions);
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("see"))) {
            // Подсказки для ника игрока
            return null; // Bukkit автоматически подставит список онлайн-игроков
        } else if (args.length == 3 && (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("take"))) {
            // Подсказки для количества (amount)
            completions.add("<Amount>");
        }

        return completions;
    }
}

