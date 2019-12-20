package io.mintit.lafarge.DBRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import io.mintit.lafarge.model.Tarif;
import io.reactivex.Flowable;

@Dao
public abstract class TarifDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAllTrifs(List<Tarif> tarifs);

    @Query("SELECT * FROM Tarif")
    public abstract Flowable<List<Tarif>> getAllTarifs();

    @Query("SELECT * FROM Tarif where mCodeArticle = :codeArticle and regimePrix = :regimePrix COLLATE NOCASE")
    public abstract Flowable<List<Tarif>> getTarifByCodeArticleAndRegimePrix(String codeArticle, String regimePrix);

    @Query("DELETE FROM Tarif")
    public abstract void deleteTarif();

    @Transaction
    public void deleteAndInsertTarif(List<Tarif> tarifs) {
       deleteTarif();
       insertAllTrifs(tarifs);
    }
}
