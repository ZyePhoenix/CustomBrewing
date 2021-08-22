package com.gmail.ryderzye.CustomPotionBrewing;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class BrewingRecipe {
    private final NamespacedKey key;
    private final ItemStack result;
    private final ItemStack inputIngredient;
    private final ItemStack inputBase;
    private int fuelUse;
    private int brewingTime;
    private BrewingAction action;

    public BrewingRecipe(NamespacedKey key, ItemStack result, ItemStack inputIngredient, ItemStack inputBase) {
        this(key, result, inputIngredient, inputBase, 1, 400, new BrewingAction());
    }

    public BrewingRecipe(NamespacedKey key, ItemStack result, ItemStack inputIngredient, ItemStack inputBase, int fuelUse, int cookingTime) {
        this(key, result, inputIngredient, inputBase, fuelUse, cookingTime, new BrewingAction());
    }

    public BrewingRecipe(NamespacedKey key, ItemStack result, ItemStack inputIngredient, ItemStack inputBase, int fuelUse, int cookingTime, BrewingAction action) {
        this.key = key;
        this.result = result;
        this.inputIngredient = inputIngredient == null ? new ItemStack(Material.AIR) : inputIngredient;
        this.inputBase = inputBase == null ? new ItemStack(Material.AIR) : inputBase;
        this.fuelUse = fuelUse;
        this.brewingTime = cookingTime;
        this.action = action;
    }

    public NamespacedKey getKey() {
        return this.key;
    }

    public ItemStack getResult() {
        return this.result;
    }

    public ItemStack getInputIngredient() {
        return this.inputIngredient;
    }

    public ItemStack getInputBase() {
        return this.inputBase;
    }

    public int getFuelUse() {
        return this.fuelUse;
    }

    public void setFuelUse(int fuelUse) {
        this.fuelUse = fuelUse;
    }

    public int getBrewingTime() {
        return this.brewingTime;
    }

    public void setBrewingTime(int cookingTime) {
        this.brewingTime = this.brewingTime;
    }

    public BrewingAction getAction() {
        return this.action;
    }

    public void setAction(BrewingAction action) {
        this.action = action;
    }

    public String toString() {
        return "BrewingRecipe [cookingTime=" + this.brewingTime + ", fuelUse=" + this.fuelUse + ", inputBase=" + this.inputBase.toString() + ", inputIngredient=" + this.inputIngredient.toString() + ", key=" + this.key.toString() + ", result=" + this.result.toString() + "]";
    }
}
