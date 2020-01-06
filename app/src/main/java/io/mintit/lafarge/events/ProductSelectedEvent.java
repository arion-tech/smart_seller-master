package io.mintit.lafarge.events;

import io.mintit.lafarge.model.Product;

/**
 * Created by mint on 03/04/18.
 */

public class ProductSelectedEvent {
    private Product product;
    private int qte;
    private boolean fromDetail;

    public ProductSelectedEvent(Product product, int qte, boolean fromDetail) {
        this.product = product;
        this.qte = qte;
        this.fromDetail = fromDetail;
    }

    public int getQte() {
        return qte;
    }

    public Product getProduct() {
        return product;
    }

    public boolean isFromDetail() {
        return fromDetail;
    }
}
