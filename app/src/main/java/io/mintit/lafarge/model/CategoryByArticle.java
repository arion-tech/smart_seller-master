package io.mintit.lafarge.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import java.util.List;
import io.mintit.lafarge.model.typeConverters.ListIntConverter;

@TypeConverters({ListIntConverter.class})
@Entity
public class CategoryByArticle {

    @PrimaryKey
    private int idCategorie;

    @TypeConverters(ListIntConverter.class)
    private List<Integer> idArticles;

    public int getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    @TypeConverters(ListIntConverter.class)
    public List<Integer> getIdArticles() {
        return idArticles;
    }

    @TypeConverters(ListIntConverter.class)
    public void setIdArticles(List<Integer> idArticles) {
        this.idArticles = idArticles;
    }
}
