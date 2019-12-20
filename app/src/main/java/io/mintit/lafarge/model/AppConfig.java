package io.mintit.lafarge.model;

public class AppConfig {

    private boolean isActif;
    private int newCart;
    private int listCart;

    private int newCustomer;
    private int listCustomer;

    private int newReservation;
    private int listReservation;

    private int catalogProduct;

    private int menuOperations;
    private int menuSettings;

    public AppConfig() {
    }

    public boolean getIsActif() {
        return isActif;
    }

    public void setIsActif(boolean actif) {
        isActif = actif;
    }

    public int getNewCart() {
        return newCart;
    }

    public void setNewCart(int newCart) {
        this.newCart = newCart;
    }

    public int getListCart() {
        return listCart;
    }

    public void setListCart(int listCart) {
        this.listCart = listCart;
    }

    public int getNewCustomer() {
        return newCustomer;
    }

    public void setNewCustomer(int newCustomer) {
        this.newCustomer = newCustomer;
    }

    public int getListCustomer() {
        return listCustomer;
    }

    public void setListCustomer(int listCustomer) {
        this.listCustomer = listCustomer;
    }

    public int getCatalogProduct() {
        return catalogProduct;
    }

    public void setCatalogProduct(int catalogProduct) {
        this.catalogProduct = catalogProduct;
    }

    public int getMenuOperations() {
        return menuOperations;
    }

    public void setMenuOperations(int menuOperations) {
        this.menuOperations = menuOperations;
    }

    public int getMenuSettings() {
        return menuSettings;
    }

    public void setMenuSettings(int menuSettings) {
        this.menuSettings = menuSettings;
    }

    public int getNewReservation() {
        return newReservation;
    }

    public void setNewReservation(int newReservation) {
        this.newReservation = newReservation;
    }

    public int getListReservation() {
        return listReservation;
    }

    public void setListReservation(int listReservation) {
        this.listReservation = listReservation;
    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "isActif=" + isActif +
                ", newCart=" + newCart +
                ", listCart=" + listCart +
                ", newCustomer=" + newCustomer +
                ", listCustomer=" + listCustomer +
                ", newReservation=" + newReservation +
                ", listReservation=" + listReservation +
                ", catalogProduct=" + catalogProduct +
                ", menuOperations=" + menuOperations +
                ", menuSettings=" + menuSettings +
                '}';
    }
}
