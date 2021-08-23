package com.gmail.ryderzye.CustomPotionBrewing.Commands;

import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandPermissions;
import cl.bgmp.minecraft.util.commands.annotations.CommandScopes;
import cl.bgmp.minecraft.util.commands.exceptions.CommandException;
import com.gmail.ryderzye.CustomPotionBrewing.CustomBrewing;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

public class MilkPotionCommand {
    private static final String MILK_POTION_NAME = CustomBrewing.get().getConfig().getString("potions.MILK_POTION_NAME");
    private static final String COLORED_MILK_POTION_NAME = ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(MILK_POTION_NAME));
    private static final String DEBUFF_ONLY_NAME = CustomBrewing.get().getConfig().getString("potions.DEBUFF_ONLY_NAME");
    private static final String COLORED_DEBUFF_ONLY_NAME = ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(DEBUFF_ONLY_NAME));
    private static final String MESSAGE_RECEIVE = CustomBrewing.get().getConfig().getString("messages.RECEIVE_POTION");
    private static final String COLORED_MESSAGE_RECEIVE = ChatColor.translateAlternateColorCodes('&', String.valueOf(MESSAGE_RECEIVE));

    private static final int MILK_RGB_RED = CustomBrewing.get().getConfig().getInt("potions.MILK_POTION_RGB_COLOR.RED");
    private static final int MILK_RGB_GREEN = CustomBrewing.get().getConfig().getInt("potions.MILK_POTION_RGB_COLOR.GREEN");
    private static final int MILK_RGB_BLUE = CustomBrewing.get().getConfig().getInt("potions.MILK_POTION_RGB_COLOR.BLUE");
    private static final int DEBUFF_ONLY_RGB_RED = CustomBrewing.get().getConfig().getInt("potions.DEBUFF_ONLY_RGB_COLOR.RED");
    private static final int DEBUFF_ONLY_RGB_GREEN = CustomBrewing.get().getConfig().getInt("potions.DEBUFF_ONLY_RGB_COLOR.GREEN");
    private static final int DEBUFF_ONLY_RGB_BLUE = CustomBrewing.get().getConfig().getInt("potions.DEBUFF_ONLY_RGB_COLOR.BLUE");

    private static final List<String> MILK_LORE = CustomBrewing.get().getConfig().getStringList("potions.MILK_POTION_LORE");
    private static final List<String> DEBUFF_ONLY_LORE = CustomBrewing.get().getConfig().getStringList("potions.DEBUFF_ONLY_LORE");

    public MilkPotionCommand() {
    }

    @Command(
            aliases = {"milkpotion"},
            usage = "<amount>",
            desc = "Give amount of MilkPotions."
    )
    @CommandPermissions({"milkpotion.command.use"})
    @CommandScopes({"player"})
    public static void milkpotion(CommandContext args, CommandSender sender) throws CommandException {
        Player p = (Player)sender;
        int amount = args.getInteger(0);
        ItemStack milkPotion = new ItemStack(Material.SPLASH_POTION, amount);
        PotionMeta meta = (PotionMeta)milkPotion.getItemMeta();
        meta.setColor(Color.fromRGB(MILK_RGB_RED, MILK_RGB_GREEN, MILK_RGB_BLUE));
        milkPotion.setItemMeta(meta);
        meta.setDisplayName(COLORED_MILK_POTION_NAME);
        meta.setLore(colorList(MILK_LORE));
        milkPotion.setItemMeta(meta);
        p.getInventory().addItem(new ItemStack[]{milkPotion});
        p.sendMessage(COLORED_MESSAGE_RECEIVE);
    }

    @Command(
            aliases = {"debuffpotion"},
            usage = "<amount>",
            desc = "Give amount of DebuffRemovingPotions."
    )
    @CommandPermissions({"milkpotion.command.use"})
    @CommandScopes({"player"})
    public static void debuffpotion(CommandContext args, CommandSender sender) throws CommandException {
        Player p = (Player)sender;
        int amount = args.getInteger(0);
        ItemStack milkPotion = new ItemStack(Material.SPLASH_POTION, amount);
        PotionMeta meta = (PotionMeta)milkPotion.getItemMeta();
        meta.setColor(Color.fromRGB(DEBUFF_ONLY_RGB_RED, DEBUFF_ONLY_RGB_GREEN, DEBUFF_ONLY_RGB_BLUE));
        milkPotion.setItemMeta(meta);
        meta.setDisplayName(COLORED_DEBUFF_ONLY_NAME);
        meta.setLore(colorList(DEBUFF_ONLY_LORE));
        milkPotion.setItemMeta(meta);
        p.getInventory().addItem(new ItemStack[]{milkPotion});
        p.sendMessage(COLORED_MESSAGE_RECEIVE);
    }

    public static List<String> colorList(List<String> input) {
        List<String> list = new ArrayList();
        Iterator var2 = input.iterator();

        while(var2.hasNext()) {
            String line = (String)var2.next();
            list.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        return list;
    }
}
