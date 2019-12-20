package io.mintit.lafarge.DBRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import io.mintit.lafarge.model.Category;
import io.mintit.lafarge.model.CategoryByArticle;
import io.mintit.lafarge.model.Stock;
import io.reactivex.Flowable;

@Dao
public abstract class CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insertCategory(List<Category> categories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insertCategoryByArticle(List<CategoryByArticle> categories);

    @Query("SELECT * FROM Category")
    abstract public Flowable<List<Category>> getCategories();

    @Query("SELECT * FROM CategoryByArticle where idCategorie = :id")
    abstract public Flowable<List<CategoryByArticle>> getCategoryById(long id);

    @Query("DELETE FROM Category")
    abstract public void deleteCategory();

    @Transaction
    public void deleteAndInsertCategories(List<CategoryByArticle> categories) {
        deleteCategory();
        insertCategoryByArticle(categories);
    }

}
