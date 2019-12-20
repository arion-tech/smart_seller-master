package io.mintit.lafarge.events;

import io.mintit.lafarge.model.Cart;

/**
 * Created by mint on 13/04/18.
 */

public class UpdateCartEvent {
    private final Cart cart;

    public UpdateCartEvent(Cart cart) {
        this.cart = cart;
    }

    public Cart getCart() {
        return cart;
    }
}
