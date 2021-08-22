package com.gmail.ryderzye.CustomPotionBrewing.Listener;

import com.gmail.ryderzye.CustomPotionBrewing.BrewingClock;
import com.gmail.ryderzye.CustomPotionBrewing.BrewingController;
import com.gmail.ryderzye.CustomPotionBrewing.BrewingRecipe;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

public class PotionEvent implements Listener {
    private Plugin plugin;
    private BrewingController brewingController;
    private static HashMap<BrewingStand, BrewingClock> activeBrews = new HashMap();

    public PotionEvent(Plugin plugin, BrewingController brewingController) {
        this.plugin = plugin;
        this.brewingController = brewingController;
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onBrewingInventoryClickEvent(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();
        if (inv != null && inv.getType() == InventoryType.BREWING) {
            this.manageBrewerInventory(event);
            BrewerInventory bInv = (BrewerInventory)inv;
            ItemStack ing = bInv.getIngredient();
            boolean canBrew = false;
            int maxTime = 0;
            int maxFuel = 0;
            BrewingStand bStand = bInv.getHolder();
            BrewingClock brewingClock = (BrewingClock)activeBrews.get(bStand);
            Logger l = this.plugin.getLogger();
            int fuel = BrewingController.totalFuelInBrewingStand(bStand);
            if (brewingClock != null && !brewingClock.isCancelled()) {
                fuel += brewingClock.getFuelUse();
            }

            l.info("totalFuel: " + fuel);

            for(int i = 0; i < 3; ++i) {
                ItemStack base = bInv.getItem(i);
                BrewingRecipe r = this.brewingController.getRecipe(ing, base, fuel);
                if (r != null) {
                    canBrew = true;
                    maxTime = Math.max(maxTime, r.getBrewingTime());
                    maxFuel = Math.max(maxFuel, r.getFuelUse());
                    l.info(r.toString());
                }
            }

            l.info("canBrew: " + canBrew);
            l.info(activeBrews.toString());
            if (brewingClock != null && !brewingClock.isCancelled()) {
                if (!canBrew) {
                    brewingClock.cancel();
                    brewingClock = null;
                    l.info("Stopped brew");
                } else {
                    if (brewingClock.getFuelUse() != maxFuel) {
                        brewingClock.setFuelUse(maxFuel);
                    }

                    if (brewingClock.getStopTime() != maxTime) {
                        brewingClock.setStopTime(maxTime);
                    }

                    l.info("Changed active brew");
                }
            } else if (canBrew) {
                brewingClock = new BrewingClock(this.plugin, this.brewingController, bStand, maxTime, maxFuel);
                activeBrews.put(bStand, brewingClock);
                l.info("Started new brew");
            }

        }
    }

    private void manageBrewerInventory(InventoryClickEvent event) {
        event.setCancelled(true);
        ClickType cl = event.getClick();
        Player p = (Player)event.getWhoClicked();
        ItemStack slot = event.getCurrentItem();
        ItemStack held = event.getCursor();
        int slotC = slot.getAmount();
        int heldC = held.getAmount();
        ItemStack empty = new ItemStack(Material.AIR);
        boolean dropAll = true;
        int i;
        label104:
        switch(cl) {
            case LEFT:
                if (slot.isSimilar(held)) {
                    if (slotC + heldC > slot.getMaxStackSize()) {
                        slot.setAmount(slot.getMaxStackSize());
                        event.setCurrentItem(slot);
                        held.setAmount(heldC - (slot.getMaxStackSize() - slotC));
                        p.setItemOnCursor(held);
                    } else {
                        slot.setAmount(slotC + heldC);
                        event.setCurrentItem(slot);
                        p.setItemOnCursor(empty);
                    }
                } else {
                    event.setCurrentItem(held);
                    p.setItemOnCursor(slot);
                }
                break;
            case RIGHT:
                if (heldC > 0 && (slot.isSimilar(held) || slotC == 0) && slotC + 1 <= held.getMaxStackSize()) {
                    held.setAmount(slotC + 1);
                    event.setCurrentItem(held);
                    if (heldC - 1 > 0) {
                        held.setAmount(heldC - 1);
                    } else {
                        held = empty;
                    }

                    p.setItemOnCursor(held);
                } else if (heldC == 0) {
                    slot.setAmount(slotC / 2);
                    event.setCurrentItem(slot);
                    slot.setAmount(slotC - slotC / 2);
                    p.setItemOnCursor(slot);
                } else {
                    event.setCurrentItem(held);
                    p.setItemOnCursor(slot);
                }
                break;
            case SHIFT_LEFT:
            case SHIFT_RIGHT:
                HashMap<Integer, ItemStack> overflow = p.getInventory().addItem(new ItemStack[]{slot});
                if (overflow.size() > 0) {
                    Iterator var19 = overflow.keySet().iterator();

                    while(true) {
                        if (!var19.hasNext()) {
                            break label104;
                        }

                        Integer i1 = (Integer)var19.next();
                        event.setCurrentItem((ItemStack)overflow.get(i1));
                    }
                } else {
                    event.setCurrentItem(empty);
                    break;
                }
            case DROP:
                dropAll = false;
            case CONTROL_DROP:
                if (slotC != 0 && heldC <= 0) {
                    ItemStack hand = p.getInventory().getItemInMainHand();
                    p.getInventory().setItemInMainHand(slot);
                    p.dropItem(dropAll);
                    p.getInventory().setItemInMainHand(hand);
                    int drop = dropAll ? slotC : 1;
                    if (slotC - drop > 0) {
                        slot.setAmount(slotC - drop);
                    } else {
                        slot = empty;
                    }

                    event.setCurrentItem(slot);
                }
                break;
            case SWAP_OFFHAND:
                event.setCurrentItem(p.getInventory().getItemInOffHand());
                p.getInventory().setItemInOffHand(slot);
                break;
            case NUMBER_KEY:
                i = event.getHotbarButton();
                event.setCurrentItem(p.getInventory().getItem(i));
                p.getInventory().setItem(i, slot);
                break;
            case DOUBLE_CLICK:
                i = 0;
                ItemStack[] var11 = event.getInventory().getContents();
                int var12 = var11.length;

                int var13;
                ItemStack stack;
                int stackC;
                for(var13 = 0; var13 < var12; ++var13) {
                    stack = var11[var13];
                    if (stack != null && stack.isSimilar(held)) {
                        stackC = stack.getAmount();
                        if (stackC + held.getAmount() > held.getMaxStackSize()) {
                            stack.setAmount(held.getAmount() - (held.getMaxStackSize() - stackC));
                            held.setAmount(held.getMaxStackSize());
                            event.getInventory().setItem(i, stack);
                            p.setItemOnCursor(held);
                        } else {
                            held.setAmount(stackC + held.getAmount());
                            event.getInventory().setItem(i, empty);
                        }
                    }

                    p.setItemOnCursor(held);
                    ++i;
                }

                if (held.getAmount() < held.getMaxStackSize()) {
                    i = 0;
                    var11 = p.getInventory().getContents();
                    var12 = var11.length;

                    for(var13 = 0; var13 < var12; ++var13) {
                        stack = var11[var13];
                        if (stack != null && stack.isSimilar(held)) {
                            stackC = stack.getAmount();
                            if (stackC + held.getAmount() > held.getMaxStackSize()) {
                                stack.setAmount(held.getAmount() - (held.getMaxStackSize() - stackC));
                                held.setAmount(held.getMaxStackSize());
                                p.getInventory().setItem(i, stack);
                                p.setItemOnCursor(held);
                            } else {
                                held.setAmount(stackC + held.getAmount());
                                p.getInventory().setItem(i, empty);
                            }
                        }

                        p.setItemOnCursor(held);
                        ++i;
                    }
                }
        }

        ((BrewerInventory)event.getInventory()).getHolder().update(true);
    }
}
