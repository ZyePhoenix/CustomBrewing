package com.gmail.ryderzye.CustomPotionBrewing;

import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BrewingClock extends BukkitRunnable {
    private Plugin plugin;
    BrewingController brewingController;
    private BrewingStand stand;
    private int stopTime;
    private int brewTime;
    private int fuelUse;

    public BrewingClock(Plugin plugin, BrewingController brewingController, BrewingStand stand, int stopTime, int fuelUse) {
        this.plugin = plugin;
        this.brewingController = brewingController;
        this.stand = stand;
        this.stopTime = stopTime;
        this.brewTime = 0;
        this.fuelUse = fuelUse;
        this.useUpFuel(fuelUse);
        this.runTaskTimer(this.plugin, 0L, 1L);
    }

    private void useUpFuel(int fuel) {
        for(int processedFuel = 0; processedFuel < fuel; this.stand.update()) {
            int inside = this.stand.getFuelLevel();
            if (inside == 0) {
                return;
            }

            if (this.stand.getFuelLevel() >= fuel) {
                this.stand.setFuelLevel(inside - fuel);
                processedFuel = fuel;
            } else {
                processedFuel += inside;
                this.stand.setFuelLevel(0);
            }
        }

    }

    private void updateTime() {
        this.stand.setBrewingTime((int)(400.0D * (1.0D - (double)this.brewTime / (double)this.stopTime)));
    }

    public int getStopTime() {
        return this.stopTime;
    }

    public void setStopTime(int stopTime) {
        this.stopTime = stopTime;
    }

    public int getFuelUse() {
        return this.fuelUse;
    }

    public void setFuelUse(int fuelUse) {
        if (fuelUse - this.fuelUse > 0) {
            this.useUpFuel(fuelUse - this.fuelUse);
        }

        this.fuelUse = fuelUse;
    }

    public void run() {
        this.updateTime();
        if (this.brewTime < this.stopTime) {
            ++this.brewTime;
        } else {
            BrewerInventory bInv = this.stand.getInventory();
            ItemStack ing = bInv.getIngredient();
            int maxIng = 0;

            for(int i = 0; i < 3; ++i) {
                ItemStack base = bInv.getItem(i);
                BrewingRecipe recipe = this.brewingController.getRecipe(ing, base);
                if (recipe != null) {
                    maxIng = Math.max(maxIng, recipe.getInputIngredient().getAmount());
                    recipe.getAction().brew(this.stand, recipe, i);
                }
            }

            ing.setAmount(ing.getAmount() - maxIng);
            if (ing.getAmount() == 0) {
                ing = new ItemStack(Material.AIR);
            }

            bInv.setIngredient(ing);
            this.cancel();
        }

    }
}
