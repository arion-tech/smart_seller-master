package io.mintit.lafarge.model.typeConverters;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.mintit.lafarge.model.Product;

public class ListArticleConverter {

    @TypeConverter
    public String fromArticle(ArrayList<Product> products) {
        if (products == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Product>>(){}.getType();
        return gson.toJson(products, type);
    }

    @TypeConverter
    public ArrayList<Product> toArticle(String article) {
        if (article == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Product>>(){}.getType();
        return gson.fromJson(article, type);
    }
}
