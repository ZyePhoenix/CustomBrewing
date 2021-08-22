package com.gmail.ryderzye.CustomPotionBrewing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.gmail.ryderzye.CustomPotionBrewing.Listener.PotionEvent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class BrewingController {
    private List<BrewingRecipe> recipes;
    private Listener potionEventListner;

    public BrewingController() {
    }

    public BrewingController(Plugin plugin) {
        this.recipes = new ArrayList();
        this.start(plugin);
    }

    public void start(Plugin plugin) {
        this.stop();
        this.potionEventListner = new PotionEvent(plugin, this);
        plugin.getServer().getPluginManager().registerEvents(this.potionEventListner, plugin);
    }

    public void stop() {
        if (this.potionEventListner != null) {
            HandlerList.unregisterAll(this.potionEventListner);
            this.potionEventListner = null;
        }

    }

    public void addRecipe(BrewingRecipe recipe) {
        this.recipes.add(recipe);
    }

    public void removeRecipe(BrewingRecipe recipe) {
        this.recipes.remove(recipe);
    }

    public BrewingRecipe getRecipe(NamespacedKey key) {
        BrewingRecipe ret = null;
        Iterator var3 = this.recipes.iterator();

        while(var3.hasNext()) {
            BrewingRecipe recipe = (BrewingRecipe)var3.next();
            if (recipe.getKey().equals(key)) {
                ret = recipe;
                break;
            }
        }

        return ret;
    }

    public BrewingRecipe getRecipe(ItemStack inputIngredient, ItemStack inputBase) {
        BrewingRecipe ret = null;
        Iterator var4 = this.recipes.iterator();

        while(var4.hasNext()) {
            BrewingRecipe recipe = (BrewingRecipe)var4.next();
            ItemStack rIng = recipe.getInputIngredient();
            ItemStack rBase = recipe.getInputBase();
            if (rIng.isSimilar(inputIngredient) && rIng.getAmount() <= inputIngredient.getAmount() && rBase.equals(inputBase)) {
                ret = recipe;
                break;
            }
        }

        return ret;
    }

    public BrewingRecipe getRecipe(ItemStack inputIngredient, ItemStack inputBase, int fuel) {
        BrewingRecipe ret = null;
        Iterator var5 = this.recipes.iterator();

        while(var5.hasNext()) {
            BrewingRecipe recipe = (BrewingRecipe)var5.next();
            ItemStack rIng = recipe.getInputIngredient();
            ItemStack rBase = recipe.getInputBase();
            if (rIng.isSimilar(inputIngredient) && rIng.getAmount() <= inputIngredient.getAmount() && rBase.equals(inputBase) && fuel >= recipe.getFuelUse()) {
                ret = recipe;
                break;
            }
        }

        return ret;
    }

    public static int totalFuelInBrewingStand(BrewingStand stand) {
        int total = stand.getFuelLevel();
        if (stand.getInventory().getFuel() != null && stand.getInventory().getFuel().getType() == Material.BLAZE_POWDER) {
            total += 20 * stand.getInventory().getFuel().getAmount();
        }

        return total;
    }

    public List<BrewingRecipe> getRecipes() {
        return this.recipes;
    }

    public void setRecipes(List<BrewingRecipe> recipes) {
        this.recipes = recipes;
    }

    public void clearRecipes() {
        this.recipes = new ArrayList();
    }
}
