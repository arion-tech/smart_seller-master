package io.mintit.lafarge.model;

public class SynchronisationObject {
    private String startTime;
    private String endTime;
    private String reference;

    public SynchronisationObject() {
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }
}