package io.mintit.lafarge.DBRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.mintit.lafarge.model.ActionRequest;
import io.reactivex.Flowable;
@Dao
public interface ActionRequestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertActionRequest(ActionRequest actionRequest);

    @Query("SELECT * FROM ActionRequest")
    Flowable<List<ActionRequest>> getAllActionRequests();

    @Query("SELECT * FROM ActionRequest where objectType = :objectType")
    Flowable<List<ActionRequest>> getAllActionRequestsByObjectType(String objectType);

    @Query("SELECT * FROM ActionRequest where reference = :ref")
    Flowable<List<ActionRequest>> getAllActionRequestsByRef(String ref);

    @Query("UPDATE ActionRequest SET transmited = :transmeted , error = :error, errorMessage = :errorMsg  where reference = :ref")
    void updateActionRequest(String ref, int  transmeted, int error, String errorMsg);
}
