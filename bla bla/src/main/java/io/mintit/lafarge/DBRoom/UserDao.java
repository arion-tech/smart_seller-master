package io.mintit.lafarge.DBRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.mintit.lafarge.model.User;
import io.reactivex.Flowable;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllUsers(List<User> users);

    @Query("SELECT * FROM User WHERE idUtilisateur =  :email  AND password =  :password ")
    Flowable<List<User>> getUserByEmailAndPswd(String email, String password);
}
