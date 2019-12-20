package io.mintit.lafarge.events;

public class ChangeTarifEvent {
    private final String tarif;

    public String getTarif() {
        return tarif;
    }

    public ChangeTarifEvent(String tarif) {
        this.tarif = tarif;
    }
}
