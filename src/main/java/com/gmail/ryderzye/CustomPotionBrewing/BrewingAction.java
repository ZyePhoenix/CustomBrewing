package com.gmail.ryderzye.CustomPotionBrewing;

import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.BrewerInventory;

public class BrewingAction {
    public BrewingAction() {
    }

    public void brew(BrewingStand stand, BrewingRecipe recipe, int slot) {
        BrewerInventory inv = stand.getInventory();
        inv.setItem(slot, recipe.getResult());
    }
}
