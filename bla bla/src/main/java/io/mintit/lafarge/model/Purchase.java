package io.mintit.lafarge.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.mintit.lafarge.model.typeConverters.ListArticleConverter;

@Entity
@TypeConverters(ListArticleConverter.class)
public class Purchase implements Parcelable {
    public static final Creator<Purchase> CREATOR = new Creator<Purchase>() {
        @Override
        public Purchase createFromParcel(Parcel source) {
            return new Purchase(source);
        }

        @Override
        public Purchase[] newArray(int size) {
            return new Purchase[size];
        }
    };
    @SerializedName("supplier")
    @Expose
    private String supplier;
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NonNull
    private String id;
    @SerializedName("product_list")
    @Expose
    @TypeConverters(ListArticleConverter.class)
    private ArrayList<Article> productList = new ArrayList<>();
    @SerializedName("date")
    @Expose
    private long date;
    @SerializedName("validated")
    @Expose
    private boolean validated;
    @SerializedName("totalQte")
    @Expose
    private int totalQte;
    @SerializedName("reference")
    @Expose
    private String reference;

    public Purchase() {
    }

    protected Purchase(Parcel in) {
        this.supplier = in.readString();
        this.id = in.readString();
        this.productList = in.createTypedArrayList(Article.CREATOR);
        this.date = in.readLong();
        this.validated = in.readByte() != 0;
        this.totalQte = in.readInt();
        this.reference = in.readString();
    }

    public static Creator<Purchase> getCREATOR() {
        return CREATOR;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    @TypeConverters(ListArticleConverter.class)
    public ArrayList<Article> getProductList() {
        return productList;
    }
    @TypeConverters(ListArticleConverter.class)
    public void setProductList(ArrayList<Article> productList) {
        this.productList = productList;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public int getTotalQte() {
        totalQte = 0;
        for (int i = 0; i < productList.size(); i++) {
            totalQte += productList.get(i).getQty();
        }
        return totalQte;
    }

    public void setTotalQte(int totalQte) {
        this.totalQte = totalQte;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.supplier);
        dest.writeString(this.id);
        dest.writeTypedList(this.productList);
        dest.writeLong(this.date);
        dest.writeByte(this.validated ? (byte) 1 : (byte) 0);
        dest.writeInt(this.totalQte);
        dest.writeString(this.reference);
    }
}
