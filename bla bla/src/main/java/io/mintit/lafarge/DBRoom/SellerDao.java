package io.mintit.lafarge.DBRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.mintit.lafarge.model.Seller;
import io.reactivex.Flowable;

@Dao
public interface SellerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllSellers(List<Seller> sellers);

    @Query("SELECT * FROM Seller")
    Flowable<List<Seller>> getSellers();

    @Query("SELECT * FROM Seller where commercial = :libelle COLLATE NOCASE")
    Flowable<List<Seller>> getSellerByLibelle(String libelle);
}
