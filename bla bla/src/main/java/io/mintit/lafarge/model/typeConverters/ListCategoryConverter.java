package io.mintit.lafarge.model.typeConverters;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.mintit.lafarge.model.Category;

public class ListCategoryConverter {
    @TypeConverter
    public String fromCategory(ArrayList<Category> categories) {
        if (categories == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Category>>(){}.getType();
        return gson.toJson(categories, type);
    }

    @TypeConverter
    public ArrayList<Category> toCategory(String category) {
        if (category == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Category>>(){}.getType();
        return gson.fromJson(category, type);
    }
}
