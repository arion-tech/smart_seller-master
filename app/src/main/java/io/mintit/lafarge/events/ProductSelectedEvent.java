package io.mintit.lafarge.events;

import io.mintit.lafarge.model.Article;

/**
 * Created by mint on 03/04/18.
 */

public class ProductSelectedEvent {
    private Article product;
    private int qte;
    private boolean fromDetail;

    public ProductSelectedEvent(Article product,int qte, boolean fromDetail) {
        this.product = product;
        this.qte = qte;
        this.fromDetail = fromDetail;
    }

    public int getQte() {
        return qte;
    }

    public Article getProduct() {
        return product;
    }

    public boolean isFromDetail() {
        return fromDetail;
    }
}
