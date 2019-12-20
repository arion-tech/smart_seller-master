package io.mintit.lafarge.DBRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import java.util.List;
import io.mintit.lafarge.model.Dimension;


@Dao
public interface DimensionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDimension(List<Dimension> dimensions);
}
