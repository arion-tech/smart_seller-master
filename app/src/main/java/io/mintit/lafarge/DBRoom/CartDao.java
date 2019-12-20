package io.mintit.lafarge.DBRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
import io.mintit.lafarge.model.Cart;
import io.mintit.lafarge.model.Customer;
import io.reactivex.Flowable;

@Dao
public interface CartDao {

    @Query("SELECT * FROM Cart WHERE agentID LIKE :idUser ORDER BY lastModification DESC LIMIT 1")
    Flowable<List<Cart>> getLastModifiedCart(String idUser);

    @Query("SELECT * FROM Cart WHERE id = :id ")
    Flowable<List<Cart>> getCartById(String id);

    @Query("SELECT * FROM Cart WHERE sellerId = :id ")
    Flowable<List<Cart>> getCartByUser(String id);

    @Query("SELECT * FROM Cart WHERE customer = :id ")
    Flowable<List<Cart>> getCartByCustomer(String id);

    @Query("SELECT * FROM Cart WHERE closed = :state")
    Flowable<List<Cart>> getCartByState(boolean state);

    @Update
    void updateBasketItem(Cart cart);

    @Insert
    void insertCart(Cart cart);

    @Query("DELETE FROM Cart WHERE id = :id")
    void deleteCartById(String id);

    @Query("DELETE FROM Cart")
    void emptyCarts();
}

