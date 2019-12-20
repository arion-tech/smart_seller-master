package io.mintit.lafarge.DBRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.mintit.lafarge.model.Supplier;
import io.reactivex.Flowable;

@Dao
public interface SupplierDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSupplier(List<Supplier> suppliers);

    @Query("SELECT * FROM Supplier")
    Flowable<List<Supplier>> getAllSupplier();
}
