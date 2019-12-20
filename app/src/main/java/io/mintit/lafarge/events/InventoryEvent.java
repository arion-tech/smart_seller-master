package io.mintit.lafarge.events;

import io.mintit.lafarge.model.Inventory;

public class InventoryEvent {
    Inventory inventory;

    public InventoryEvent(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
