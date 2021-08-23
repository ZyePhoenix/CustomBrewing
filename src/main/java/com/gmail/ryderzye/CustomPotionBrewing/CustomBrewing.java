package com.gmail.ryderzye.CustomPotionBrewing;

import cl.bgmp.bukkit.util.BukkitCommandsManager;
import cl.bgmp.bukkit.util.CommandsManagerRegistration;
import cl.bgmp.minecraft.util.commands.CommandsManager;
import cl.bgmp.minecraft.util.commands.annotations.TabCompletion;
import cl.bgmp.minecraft.util.commands.exceptions.CommandException;
import cl.bgmp.minecraft.util.commands.exceptions.CommandPermissionsException;
import cl.bgmp.minecraft.util.commands.exceptions.CommandUsageException;
import cl.bgmp.minecraft.util.commands.exceptions.MissingNestedCommandException;
import cl.bgmp.minecraft.util.commands.exceptions.ScopeMismatchException;
import cl.bgmp.minecraft.util.commands.exceptions.WrappedCommandException;
import com.gmail.ryderzye.CustomPotionBrewing.Listener.MilkBottleListener;
import com.gmail.ryderzye.CustomPotionBrewing.Commands.MilkPotionCommand;
import com.gmail.ryderzye.CustomPotionBrewing.Commands.ReloadConfigCommand;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomBrewing extends JavaPlugin {
    public static CustomBrewing cb;
    private BrewingController bc;
    private CommandsManager commandsManager;
    private CommandsManagerRegistration defaultRegistration;

    public CustomBrewing() {
    }

    public void onEnable() {
        cb = this;
        this.bc = new BrewingController(this);
        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();

        int MILK_RGB_RED = get().getConfig().getInt("potions.MILK_POTION_RGB_COLOR.RED");
        int MILK_RGB_GREEN = get().getConfig().getInt("potions.MILK_POTION_RGB_COLOR.GREEN");
        int MILK_RGB_BLUE = get().getConfig().getInt("potions.MILK_POTION_RGB_COLOR.BLUE");
        int DEBUFF_ONLY_RGB_RED = get().getConfig().getInt("potions.DEBUFF_ONLY_RGB_COLOR.RED");
        int DEBUFF_ONLY_RGB_GREEN = get().getConfig().getInt("potions.DEBUFF_ONLY_RGB_COLOR.GREEN");
        int DEBUFF_ONLY_RGB_BLUE = get().getConfig().getInt("potions.DEBUFF_ONLY_RGB_COLOR.BLUE");

        String MILK_POTION_NAME = get().getConfig().getString("potions.MILK_POTION_NAME");
        String COLORED_MILK_POTION_NAME = ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(MILK_POTION_NAME));
        String DEBUFF_ONLY_NAME = get().getConfig().getString("potions.DEBUFF_ONLY_NAME");
        String COLORED_DEBUFF_ONLY_NAME = ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(DEBUFF_ONLY_NAME));

        List<String> MILK_BOTTLE_LORE = Arrays.asList("", "&4BREWING MATERIAL ONLY", "&4DRINKING THIS HAS NO EFFECT");
        List<String> MILK_LORE = get().getConfig().getStringList("potions.MILK_POTION_LORE");
        List<String> DEBUFF_ONLY_LORE = get().getConfig().getStringList("potions.DEBUFF_ONLY_LORE");

        new MilkBottleListener(this);

        ItemStack milkBottle = this.customPotion("&f&lBottle of Milk", false, MILK_RGB_RED, MILK_RGB_GREEN, MILK_RGB_BLUE, MILK_BOTTLE_LORE);
        ItemStack splashMilk = this.customPotion(COLORED_MILK_POTION_NAME, true, MILK_RGB_RED, MILK_RGB_GREEN, MILK_RGB_BLUE, MILK_LORE);
        ItemStack splashDebuffRemover = this.customPotion(COLORED_DEBUFF_ONLY_NAME, true, DEBUFF_ONLY_RGB_RED, DEBUFF_ONLY_RGB_GREEN, DEBUFF_ONLY_RGB_BLUE, DEBUFF_ONLY_LORE);

        BrewingRecipe milkBottleRecipe = new BrewingRecipe(new NamespacedKey(this, "milkBottle"), new ItemStack(milkBottle), new ItemStack(Material.MILK_BUCKET), new ItemStack(Material.GLASS_BOTTLE));
        BrewingRecipe splashMilkRecipe = new BrewingRecipe(new NamespacedKey(this, "milkBottle"), new ItemStack(splashMilk), new ItemStack(Material.GUNPOWDER), new ItemStack(milkBottle));
        BrewingRecipe splashDebuffRemoverRecipe = new BrewingRecipe(new NamespacedKey(this, "milkBottle"), new ItemStack(splashDebuffRemover), new ItemStack(Material.NETHERITE_INGOT), new ItemStack(splashMilk));
        this.bc.addRecipe(milkBottleRecipe);
        this.bc.addRecipe(splashMilkRecipe);
        this.bc.addRecipe(splashDebuffRemoverRecipe);

        this.commandsManager = new BukkitCommandsManager();
        this.defaultRegistration = new CommandsManagerRegistration(this, this.commandsManager);
        this.registerCommands(MilkPotionCommand.class, ReloadConfigCommand.class);
        this.getLogger().info("CustomBrewing is enabled");
    }

    public void onDisable() {
        this.bc.stop();
        this.getLogger().info("CustomBrewing is disabled");
    }

    public static CustomBrewing get() {
        return cb;
    }

    public ItemStack customPotion(String potionName, Boolean isSplash, int rgbRed, int rgbGreen, int rgbBlue, List<String> lore) {
        ItemStack customPotion;
        if (isSplash) {
            customPotion = new ItemStack(Material.SPLASH_POTION, 1);
        } else {
            customPotion = new ItemStack(Material.POTION, 1);
        }

        PotionMeta meta = (PotionMeta)customPotion.getItemMeta();
        meta.setColor(Color.fromRGB(rgbRed, rgbGreen, rgbBlue));
        customPotion.setItemMeta(meta);
        String coloredPotionName = ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(potionName));
        meta.setDisplayName(coloredPotionName);
        meta.setLore(colorList(lore));
        customPotion.setItemMeta(meta);
        return customPotion;
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

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            this.commandsManager.execute(command.getName(), args, sender, new Object[]{sender});
        } catch (ScopeMismatchException var7) {
            String[] scopes = var7.getScopes();
            if (!Arrays.asList(scopes).contains("player")) {
                sender.sendMessage(ChatConstant.NO_PLAYER.getFormattedMessage(ChatColor.RED));
            } else {
                sender.sendMessage(ChatConstant.NO_CONSOLE.getFormattedMessage(ChatColor.RED));
            }
        } catch (CommandPermissionsException var8) {
            sender.sendMessage(ChatConstant.COMMAND_NO_PERMISSION.getFormattedMessage(ChatColor.RED));
        } catch (MissingNestedCommandException var9) {
            sender.sendMessage(ChatColor.YELLOW + "⚠ " + ChatColor.RED + var9.getUsage());
        } catch (CommandUsageException var10) {
            sender.sendMessage(ChatColor.RED + var10.getMessage());
            sender.sendMessage(ChatColor.RED + var10.getUsage());
        } catch (WrappedCommandException var11) {
            if (var11.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatConstant.COMMAND_NUMBER.getFormattedMessage(ChatColor.RED));
            } else {
                sender.sendMessage(ChatConstant.COMMAND_ERROR.getFormattedMessage(ChatColor.RED));
                var11.printStackTrace();
            }
        } catch (CommandException var12) {
            sender.sendMessage(ChatColor.RED + var12.getMessage());
        }

        return true;
    }

    public void registerCommands(Class<?>... classes) {
        Class[] var2 = classes;
        int var3 = classes.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Class<?> clazz = var2[var4];
            Class<?>[] subclasses = clazz.getClasses();
            if (subclasses.length == 0) {
                this.defaultRegistration.register(clazz);
            } else {
                TabCompleter tabCompleter = null;
                Class<?> nestNode = null;
                Class[] var9 = subclasses;
                int var10 = subclasses.length;

                for(int var11 = 0; var11 < var10; ++var11) {
                    Class<?> subclass = var9[var11];
                    if (subclass.isAnnotationPresent(TabCompletion.class)) {
                        try {
                            tabCompleter = (TabCompleter)subclass.newInstance();
                        } catch (IllegalAccessException | InstantiationException var14) {
                            var14.printStackTrace();
                        }
                    } else {
                        nestNode = subclass;
                    }
                }

                if (tabCompleter == null) {
                    this.defaultRegistration.register(subclasses[0]);
                } else {
                    CommandsManagerRegistration customRegistration = new CommandsManagerRegistration(this, this, tabCompleter, this.commandsManager);
                    if (subclasses.length == 1) {
                        customRegistration.register(clazz);
                    } else {
                        customRegistration.register(nestNode);
                    }
                }
            }
        }

    }
}
