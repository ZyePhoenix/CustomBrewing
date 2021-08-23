package com.gmail.ryderzye.CustomPotionBrewing.Listener;

import com.gmail.ryderzye.CustomPotionBrewing.CustomBrewing;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class MilkBottleListener implements Listener {
    private final String TARGET_REMOVED_EFFECTS = "&cYour potion effects were removed";
    private final String TARGET_REMOVED_BAD_EFFECTS = "&cYour debuff potion effects were removed";
    private final String COLORED_TARGET_REMOVED_EFFECTS = ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(TARGET_REMOVED_EFFECTS));
    private final String COLORED_TARGET_REMOVED_BAD_EFFECTS = ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(TARGET_REMOVED_BAD_EFFECTS));
    private static final List<String> MILK_LORE = CustomBrewing.get().getConfig().getStringList("potions.MILK_POTION_LORE");

    private static final List<String> DEBUFF_ONLY_LORE = CustomBrewing.get().getConfig().getStringList("potions.DEBUFF_ONLY_LORE");

    public MilkBottleListener() {
    }

    public MilkBottleListener(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerHitOtherPlayer(PotionSplashEvent e) {
        if (e != null) {
            if (e.getEntity().getItem().hasItemMeta()) {
                Iterator var2;
                LivingEntity en;
                Iterator var4;
                PotionEffect effect;
                if (e.getEntity().getItem().getItemMeta().getLore().equals(colorList(MILK_LORE))) {
                    e.setCancelled(true);
                    var2 = e.getAffectedEntities().iterator();

                    while(var2.hasNext()) {
                        en = (LivingEntity)var2.next();
                        var4 = en.getActivePotionEffects().iterator();

                        while(var4.hasNext()) {
                            effect = (PotionEffect)var4.next();
                            en.removePotionEffect(effect.getType());
                        }

                        en.sendMessage(this.COLORED_TARGET_REMOVED_EFFECTS);
                    }
                } else if (e.getEntity().getItem().getItemMeta().getLore().equals(colorList(DEBUFF_ONLY_LORE))) {
                    var2 = e.getAffectedEntities().iterator();

                    while(var2.hasNext()) {
                        en = (LivingEntity)var2.next();
                        var4 = en.getActivePotionEffects().iterator();

                        while(var4.hasNext()) {
                            effect = (PotionEffect)var4.next();
                            en.removePotionEffect(PotionEffectType.BAD_OMEN);
                            en.removePotionEffect(PotionEffectType.BLINDNESS);
                            en.removePotionEffect(PotionEffectType.CONFUSION);
                            en.removePotionEffect(PotionEffectType.HARM);
                            en.removePotionEffect(PotionEffectType.HUNGER);
                            en.removePotionEffect(PotionEffectType.POISON);
                            en.removePotionEffect(PotionEffectType.SLOW);
                            en.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                            en.removePotionEffect(PotionEffectType.SLOW_FALLING);
                            en.removePotionEffect(PotionEffectType.UNLUCK);
                            en.removePotionEffect(PotionEffectType.WEAKNESS);
                            en.removePotionEffect(PotionEffectType.WITHER);
                        }

                        en.sendMessage(this.COLORED_TARGET_REMOVED_BAD_EFFECTS);
                    }
                }
            }

        }
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
