package io.mintit.lafarge.DBRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.mintit.lafarge.model.Daily;
import io.reactivex.Flowable;
@Dao
public interface DailyDao {

    @Query("SELECT * FROM Daily WHERE userID LIKE :idUser AND opened = 1")
    Flowable<List<Daily>> getDaily(String idUser);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertDaily(Daily daily);

    @Update
    void update(Daily daily);
}
