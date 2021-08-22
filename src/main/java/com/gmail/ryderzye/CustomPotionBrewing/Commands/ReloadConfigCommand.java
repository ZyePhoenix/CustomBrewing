package com.gmail.ryderzye.CustomPotionBrewing.Commands;

import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandPermissions;
import cl.bgmp.minecraft.util.commands.annotations.CommandScopes;
import cl.bgmp.minecraft.util.commands.annotations.NestedCommand;
import cl.bgmp.minecraft.util.commands.annotations.TabCompletion;
import com.gmail.ryderzye.CustomPotionBrewing.CustomBrewing;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

public class ReloadConfigCommand {
    public ReloadConfigCommand() {
    }

    @Command(
            aliases = {"reload"},
            desc = "Reload config files."
    )
    @CommandScopes({"player", "console"})
    public static void reload(CommandContext args, CommandSender sender) {
        Plugin plugin = CustomBrewing.getPlugin(CustomBrewing.class);
        plugin.reloadConfig();
        sender.sendMessage(ChatColor.YELLOW + "Config files are now reloaded!");
    }

    @TabCompletion
    public static class MilkPotionTabComplete implements TabCompleter {
        public MilkPotionTabComplete() {
        }

        public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
            switch(args.length) {
                case 1:
                    return Arrays.asList("reload");
                default:
                    return Collections.emptyList();
            }
        }
    }

    public static class MilkPotionNCommand {
        public MilkPotionNCommand() {
        }

        @Command(
                aliases = {"milkadmin"},
                desc = "MilkPotion admin main command"
        )
        @CommandPermissions({"milkpotion.command.admin"})
        @CommandScopes({"player", "console"})
        @NestedCommand({ReloadConfigCommand.class})
        public static void milkadmin(CommandContext args, CommandSender sender) {
            String b = ChatColor.GRAY + "[" + ChatColor.RED + "»" + ChatColor.GRAY + "] ";
            String lines = ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "------" + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "---------------" + ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "------";
            sender.sendMessage(lines);
            sender.sendMessage(b + ChatColor.RED + "() = Optional, <> = Required");
            sender.sendMessage("");
            sender.sendMessage(b + ChatColor.RED + "/milkadmin reload " + ChatColor.GRAY + ": Reload all config files from UtilityCore.");
            sender.sendMessage(lines);
        }
    }
}
