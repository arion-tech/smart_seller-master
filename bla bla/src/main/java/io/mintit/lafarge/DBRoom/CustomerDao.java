package io.mintit.lafarge.DBRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;
import android.text.TextUtils;

import java.util.List;

import io.mintit.lafarge.model.Customer;
import io.reactivex.Flowable;

@Dao
public abstract class CustomerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insertCustomers(List<Customer> customers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insertCustomer(Customer customer);

    @Query("SELECT * FROM Customer where customerId = 'SC000004' ")
    abstract public Flowable<List<Customer>> getCustomersWalkThrough();

    @Query("SELECT * FROM Customer where id like :id")
    abstract public Flowable<List<Customer>> getCustomersById(String id);

    @Query("SELECT * FROM Customer where firstName like :name")
    abstract public Flowable<List<Customer>> getCustomersByName(String name);

     public Flowable<List<Customer>> getCustomersByIdOrName(String id, String name){
        if(id.equals("0")){
            return getCustomersByName(name);
        }else {
            return getCustomersById(id);
        }
    }

    @Query("SELECT * FROM Customer")
    abstract public Flowable<List<Customer>> getCustomers();

    @Query("SELECT * FROM Customer where isCompany = :isCompany")
    abstract public Flowable<List<Customer>> getCustomersCompany(boolean isCompany);

    @Query("SELECT * FROM Customer where " +
            "    firstName like :constraint COLLATE NOCASE" +
            " or lastName like :constraint COLLATE NOCASE" +
            " or email like :constraint COLLATE NOCASE " +

            " or cellularPhoneNumber like :constraint " +
            " or homePhoneNumber like :constraint" +
            " or officePhoneNumber like :constraint")
    abstract public Flowable<List<Customer>> getCustomersConstraint(String constraint);


    @Query("SELECT * FROM Customer where " +
            "(firstName like :constraint1 COLLATE NOCASE" +
            " and lastName like :constraint2 COLLATE NOCASE )" +

            "or (firstName like :constraint2 COLLATE NOCASE " +
            "and lastName like :constraint1 COLLATE NOCASE )" +

            "or (firstName like :constraint COLLATE NOCASE " +
            "or lastName like :constraint COLLATE NOCASE) ")
    abstract public Flowable<List<Customer>> getCustomersConstraint(String constraint, String constraint1, String constraint2);


    @Query("SELECT * FROM Customer where isCompany = :isCompany" +
            " and (firstName like :constraint COLLATE NOCASE " +
            " or lastName like :constraint COLLATE NOCASE " +
            " or email like :constraint COLLATE NOCASE " +

            " or homePhoneNumber like :constraint" +
            " or officePhoneNumber like :constraint"+
            " or cellularPhoneNumber like :constraint)")
    abstract public Flowable<List<Customer>> getCustomersCompanyConstraint(boolean isCompany, String constraint);

    @Query("SELECT * FROM Customer where isCompany = :isCompany and" +
            "((firstName like :constraint1 COLLATE NOCASE" +
            " and lastName like :constraint2 COLLATE NOCASE )" +

            "or (firstName like :constraint2 COLLATE NOCASE " +
            "and lastName like :constraint1 COLLATE NOCASE )" +

            "or (firstName like :constraint COLLATE NOCASE " +
            "or lastName like :constraint COLLATE NOCASE))")
    abstract public Flowable<List<Customer>> getCustomersCompanyConstraint(boolean isCompany, String constraint, String constraint1, String constraint2);


    public Flowable<List<Customer>> getCustomers(boolean selectCustomer, boolean isCompany, String constraint){
        if(selectCustomer && !TextUtils.isEmpty(constraint)){
            if(!constraint.contains(" ")) return getCustomersCompanyConstraint(isCompany,constraint);
            else {
                return getCustomersCompanyConstraint(isCompany, constraint, constraint.substring(0,constraint.indexOf(" "))+"%", "%"+constraint.substring(constraint.indexOf(" ")).trim());
            }
        }else if(selectCustomer){
            return getCustomersCompany(isCompany);
        }else if(!TextUtils.isEmpty(constraint)){
            if(!constraint.contains(" ")) return getCustomersConstraint(constraint);
            else{
                return getCustomersConstraint(constraint, constraint.substring(0,constraint.indexOf(" "))+"%", "%"+constraint.substring(constraint.indexOf(" ")).trim());
            }

        }
        return getCustomers();
    }

    @Update
    abstract public void updateCustomer(Customer customer);

    @Query("DELETE FROM Customer")
    abstract public void deleteCustomer();

    @Transaction
    public void deleteAndInsertCustomers(List<Customer> customers) {
        deleteCustomer();
       insertCustomers(customers);
    }
}
