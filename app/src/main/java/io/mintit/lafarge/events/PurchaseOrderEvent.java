package io.mintit.lafarge.events;

import io.mintit.lafarge.model.Purchase;

public class PurchaseOrderEvent {
    private final boolean update;
    private final int position;
    Purchase purchase;

    public PurchaseOrderEvent(Purchase purchase, boolean update, int position) {
        this.purchase = purchase;
        this.update = update;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public boolean isUpdate() {
        return update;
    }

    public Purchase getPurchase() {
        return purchase;
    }
}
