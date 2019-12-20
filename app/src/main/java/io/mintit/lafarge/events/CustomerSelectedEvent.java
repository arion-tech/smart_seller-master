package io.mintit.lafarge.events;

import io.mintit.lafarge.model.Customer;

/**
 * Created by mint on 03/04/18.
 */

public class CustomerSelectedEvent {
    Customer customer;

    public CustomerSelectedEvent(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

}
