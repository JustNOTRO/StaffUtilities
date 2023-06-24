package me.notro.staffutilities.managers;

import lombok.Getter;
import lombok.NonNull;
import me.notro.staffutilities.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@Getter
public class GUIManager {

    private Inventory inventory;

    public void createMenu(InventoryHolder inventoryHolder, int size, @NonNull Component title) {
        inventory = Bukkit.createInventory(inventoryHolder, size, title);
    }

    public void addMenuItem(@NonNull ItemBuilder itemBuilder) {
        inventory.addItem(itemBuilder.build());
    }

    public void setMenuItem(int index, @NonNull ItemBuilder itemBuilder) {
        inventory.setItem(index, itemBuilder.build());
    }
}
