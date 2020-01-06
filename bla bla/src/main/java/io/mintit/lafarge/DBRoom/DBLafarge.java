package io.mintit.lafarge.DBRoom;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import io.mintit.lafarge.model.ActionRequest;
import io.mintit.lafarge.model.Article;
import io.mintit.lafarge.model.Cart;
import io.mintit.lafarge.model.Category;
import io.mintit.lafarge.model.CategoryByArticle;
import io.mintit.lafarge.model.Customer;
import io.mintit.lafarge.model.Daily;
import io.mintit.lafarge.model.Dimension;
import io.mintit.lafarge.model.Inventory;
import io.mintit.lafarge.model.InventoryArticle;
import io.mintit.lafarge.model.Purchase;
import io.mintit.lafarge.model.Seller;
import io.mintit.lafarge.model.Stock;
import io.mintit.lafarge.model.Supplier;
import io.mintit.lafarge.model.Tarif;
import io.mintit.lafarge.model.User;

@Database(entities = {Inventory.class,User.class,Seller.class,Tarif.class,Stock.class,
        Dimension.class,Category.class,Supplier.class,CategoryByArticle.class,
        InventoryArticle.class,Customer.class, Cart.class, Daily.class,Purchase.class,
        ActionRequest.class, Article.class}, version = 25)
public abstract class DBLafarge extends RoomDatabase {
    public abstract InventoryDao inventoryDao();
    public abstract UserDao userDao();
    public abstract SellerDao sellerDao();
    public abstract TarifDao tarifDao();
    public abstract StockDao stockDao();
    public abstract DimensionDao dimensionDao();
    public abstract CategoryDao categoryDao();
    public abstract SupplierDao supplierDao();
    public abstract CustomerDao customerDao();
    public abstract CartDao cartDao();
    public abstract DailyDao dailyDao();
    public abstract PurchaseDao purchaseDao();
    public abstract ActionRequestDao actionRequestDao();
    public abstract ArticleDao articleDao();

}



