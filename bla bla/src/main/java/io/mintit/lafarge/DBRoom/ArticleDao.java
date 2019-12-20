package io.mintit.lafarge.DBRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import org.reactivestreams.Subscriber;

import java.util.List;

import io.mintit.lafarge.model.Article;
import io.mintit.lafarge.model.Stock;
import io.reactivex.Flowable;

@Dao
public abstract class ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAllProducts(List<Article> articles);
/***/
    @Query("SELECT * FROM Article INNER JOIN Stock ON  article.productCode = stock.mCodeArticle " +
            "WHERE article.id = :id")
    public abstract Flowable<List<Article>> getArticleByIDAndCodeArticle(Integer id);

    @Query("SELECT * FROM Article  " +
            "WHERE name like :constraint COLLATE NOCASE OR productCode like :constraint")
    public abstract Flowable<List<Article>> getArticleByCodeBarreOrLibelle(String constraint);

    @Query("SELECT * FROM Article INNER JOIN Stock ON  article.productCode = stock.mCodeArticle " +
            "WHERE article.eanCode = :code LIMIT 1")
    public abstract Flowable<List<Article>> getArticleByStockAndCodeBarre(String code);

    @Query("SELECT * FROM Article INNER JOIN Stock ON  article.productCode = stock.mCodeArticle ")
    public abstract Flowable<List<Article>> getArticleByStock();

    @Query("SELECT * FROM Article WHERE eanCode = :code ")
    public abstract Flowable<List<Article>> getArticleByStock(String code);

    @Query("DELETE FROM Article")
    public abstract void deleteArtiles();


    @Transaction
    public void deleteAndInsertArticles(List<Article> articles) {
        deleteArtiles();
        insertAllProducts(articles);
    }






}
