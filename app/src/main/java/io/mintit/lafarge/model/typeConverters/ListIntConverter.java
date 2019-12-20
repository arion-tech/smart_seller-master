package io.mintit.lafarge.model.typeConverters;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ListIntConverter {
    @TypeConverter
    public String fromInteger(List<Integer> integers) {
        if (integers == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>(){}.getType();
        return gson.toJson(integers, type);
    }

    @TypeConverter
    public List<Integer> toInteger(String integers) {
        if (integers == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>(){}.getType();
        return gson.fromJson(integers, type);
    }
}
