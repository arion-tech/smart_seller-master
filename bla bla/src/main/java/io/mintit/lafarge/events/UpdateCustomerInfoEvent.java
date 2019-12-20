package io.mintit.lafarge.events;

import io.mintit.lafarge.model.Customer;

/**
 * Created by mint on 13/04/18.
 */

public class UpdateCustomerInfoEvent {
    private final Customer customer;

    public UpdateCustomerInfoEvent(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}
