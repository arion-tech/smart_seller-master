package io.mintit.lafarge.model.typeConverters;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.mintit.lafarge.model.Article;

public class ListArticleConverter {

    @TypeConverter
    public String fromArticle(ArrayList<Article> articles) {
        if (articles == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Article>>(){}.getType();
        return gson.toJson(articles, type);
    }

    @TypeConverter
    public ArrayList<Article> toArticle(String article) {
        if (article == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Article>>(){}.getType();
        return gson.fromJson(article, type);
    }
}
