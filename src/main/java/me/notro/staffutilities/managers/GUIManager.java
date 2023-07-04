package me.notro.staffutilities.managers;

import lombok.Getter;
import lombok.NonNull;
import me.notro.staffutilities.utils.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
public class GUIManager {

    private Inventory inventory;

    public void createMenu(InventoryHolder inventoryHolder, int size, @NonNull Component title) {
        inventory = Bukkit.createInventory(inventoryHolder, size, title);
    }

    public void addMenuItem(@NonNull ItemStack itemStack) {
        inventory.addItem(itemStack);
    }

    public void setMenuItem(int index, @NonNull ItemStack itemStack, @NonNull String displayName) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Message.fixColor(displayName));

        itemStack.setItemMeta(itemMeta);
        inventory.setItem(index, itemStack);
    }

    public void clearMenu(@NonNull Inventory inventory) {
        inventory.clear();
    }
}
