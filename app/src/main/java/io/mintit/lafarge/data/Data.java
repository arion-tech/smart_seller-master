package io.mintit.lafarge.data;

import java.util.ArrayList;
import java.util.List;

import io.mintit.lafarge.model.Article;
import io.mintit.lafarge.model.Cart;
import io.mintit.lafarge.model.Category;
import io.mintit.lafarge.model.Customer;
import io.mintit.lafarge.model.Purchase;
import io.mintit.lafarge.model.Seller;
import io.mintit.lafarge.model.Supplier;

/**
 * Created by mint on 05/04/18.
 */

public class Data {
    private static final Data ourInstance = new Data();
    private ArrayList<Article> listProducts;
    private ArrayList<Customer> listCustomers;
    private ArrayList<Cart> listCarts;
    private ArrayList<Category> categoriesList;
    private List<Seller> listSellers;
    private ArrayList<Purchase> purchaseList = new ArrayList<>();
    private ArrayList<Supplier> suppliers;
    private ArrayList<Article> listProductsFiltredByCategory;

    private Data() {
    }

    public static Data getInstance() {
        return ourInstance;
    }

    public ArrayList<Article> getListProductsFiltredByCategory() {
        return listProductsFiltredByCategory;
    }

    public void setListProductsFiltredByCategory(ArrayList<Article> listProductsFiltredByCategory) {
        this.listProductsFiltredByCategory = listProductsFiltredByCategory;
    }

    public ArrayList<Article> getListProducts() {
        return listProducts;
    }

    public void setListProducts(ArrayList<Article> listProducts) {
        this.listProducts = listProducts;
    }

    public ArrayList<Customer> getListCustomers() {
        return listCustomers;
    }

    public void setListCustomers(ArrayList<Customer> listCustomers) {
        this.listCustomers = listCustomers;
    }

    public ArrayList<Cart> getListCarts() {
        return listCarts;
    }

    public void setListCarts(ArrayList<Cart> listCarts) {
        this.listCarts = listCarts;
    }

    public ArrayList<Category> getCategoriesList() {
        return categoriesList;
    }

    public void setCategoriesList(ArrayList<Category> categoriesList) {
        this.categoriesList = categoriesList;
    }

    public List<Seller> getListSellers() {
        return listSellers;
    }

    public void setListSellers(List<Seller> listSellers) {
        this.listSellers = listSellers;
    }

    public ArrayList<Purchase> getPurchaseList() {
        return purchaseList;
    }

    public void setPurchaseList(ArrayList<Purchase> purchaseList) {
        this.purchaseList = purchaseList;
    }

    public ArrayList<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(ArrayList<Supplier> suppliers) {
        this.suppliers = suppliers;
    }
}
