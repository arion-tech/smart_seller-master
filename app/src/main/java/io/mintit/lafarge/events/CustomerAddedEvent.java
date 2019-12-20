package io.mintit.lafarge.events;

import io.mintit.lafarge.model.ActionRequest;

public class CustomerAddedEvent {
    ActionRequest request;

    public CustomerAddedEvent(ActionRequest request) {
        this.request = request;
    }
}
