package io.mintit.lafarge.DBRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import io.mintit.lafarge.model.Product;
import io.reactivex.Flowable;

@Dao
public abstract class ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAllProducts(List<Product> products);
/***/
    @Query("SELECT * FROM Product INNER JOIN Stock ON  Product.productCode = stock.mCodeArticle " +
            "WHERE Product.id = :id")
    public abstract Flowable<List<Product>> getArticleByIDAndCodeArticle(Integer id);

    @Query("SELECT * FROM Product  " +
            "WHERE name like :constraint COLLATE NOCASE OR productCode like :constraint")
    public abstract Flowable<List<Product>> getArticleByCodeBarreOrLibelle(String constraint);

    @Query("SELECT * FROM Product INNER JOIN Stock ON  Product.productCode = stock.mCodeArticle " +
            "WHERE Product.eanCode = :code LIMIT 1")
    public abstract Flowable<List<Product>> getArticleByStockAndCodeBarre(String code);

    @Query("SELECT * FROM Product INNER JOIN Stock ON  Product.productCode = stock.mCodeArticle ")
    public abstract Flowable<List<Product>> getArticleByStock();

    @Query("SELECT * FROM Product WHERE eanCode = :code ")
    public abstract Flowable<List<Product>> getArticleByStock(String code);

    @Query("DELETE FROM Product")
    public abstract void deleteArtiles();


    @Transaction
    public void deleteAndInsertArticles(List<Product> products) {
        deleteArtiles();
        insertAllProducts(products);
    }






}
