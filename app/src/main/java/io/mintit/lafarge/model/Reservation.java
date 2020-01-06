package io.mintit.lafarge.model;

/**
 * Created by mint on 05/04/18.
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.mintit.lafarge.model.typeConverters.ListArticleConverter;

@TypeConverters(ListArticleConverter.class)
@Entity
public class Reservation implements Parcelable {

    @SerializedName("customer")
    @Expose
    private String customer;
    @SerializedName("basePrice")
    @Expose
    private Integer basePrice;
    @SerializedName("product_list")
    @Expose
    @TypeConverters(ListArticleConverter.class)
    private ArrayList<Product> productList = new ArrayList<>();
    @SerializedName("price_type")
    @Expose
    private Boolean priceType;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("total")
    @Expose
    private Double total = 0.0;
    @SerializedName("closed")
    @Expose
    private Boolean closed;
    @SerializedName("customerFirstName")
    @Expose
    private String customerFirstName;
    @SerializedName("customerLastName")
    @Expose
    private String customerLastName;
    @SerializedName("customerAddress")
    @Expose
    private String customerAddress;
    @SerializedName("agentName")
    @Expose
    private String agentName;
    @SerializedName("dailyID")
    @Expose
    private String dailyID;
    @SerializedName("isCompany")
    @Expose
    private boolean isCompany;
    @SerializedName("lastModification")
    @Expose
    private String lastModification;
    private String sellerLibelle;
    private String sellerId;
    @SerializedName("agentID")
    @Expose
    private String agentID;
    @SerializedName("paid")
    @Expose
    private Boolean paid;
    @NonNull
    @PrimaryKey
    private String id;
    @SerializedName("currencyId")
    private String currencyId;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Reservation() {
    }

    public String getAgentID() {
        return agentID;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Integer getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Integer basePrice) {
        this.basePrice = basePrice;
    }

    @TypeConverters(ListArticleConverter.class)
    public ArrayList<Product> getProductList() {
        return productList;
    }
    @TypeConverters(ListArticleConverter.class)
    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }

    public Boolean getPriceType() {
        return priceType;
    }

    public void setPriceType(Boolean priceType) {
        this.priceType = priceType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getTotal() {
        if (total == null) {
            return 0.000;
        }
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getDailyID() {
        return dailyID;
    }

    public void setDailyID(String dailyID) {
        this.dailyID = dailyID;
    }

    public boolean isCompany() {
        return isCompany;
    }

    public void setCompany(boolean isCompany) {
        this.isCompany = isCompany;
    }

    public String getLastModification() {
        if (TextUtils.isEmpty(lastModification)) return "0";
        return lastModification;
    }

    public void setLastModification(String lastModification) {
        this.lastModification = lastModification;
    }

    public String getSellerLibelle() {
        return sellerLibelle;
    }

    public void setSellerLibelle(String sellerLibelle) {
        this.sellerLibelle = sellerLibelle;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.customer);
        dest.writeValue(this.basePrice);
        dest.writeTypedList(this.productList);
        dest.writeValue(this.priceType);
        dest.writeString(this.date);
        dest.writeString(this.time);
        dest.writeValue(this.total);
        dest.writeValue(this.closed);
        dest.writeString(this.customerFirstName);
        dest.writeString(this.customerLastName);
        dest.writeString(this.customerAddress);
        dest.writeString(this.agentName);
        dest.writeString(this.dailyID);
        dest.writeByte(this.isCompany ? (byte) 1 : (byte) 0);
        dest.writeString(this.lastModification);
        dest.writeString(this.sellerLibelle);
        dest.writeString(this.sellerId);
        dest.writeString(this.agentID);
        dest.writeValue(this.paid);
        dest.writeString(this.id);
        dest.writeString(this.currencyId);
    }

    protected Reservation(Parcel in) {
        this.customer = in.readString();
        this.basePrice = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productList = in.createTypedArrayList(Product.CREATOR);
        this.priceType = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.date = in.readString();
        this.time = in.readString();
        this.total = (Double) in.readValue(Double.class.getClassLoader());
        this.closed = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.customerFirstName = in.readString();
        this.customerLastName = in.readString();
        this.customerAddress = in.readString();
        this.agentName = in.readString();
        this.dailyID = in.readString();
        this.isCompany = in.readByte() != 0;
        this.lastModification = in.readString();
        this.sellerLibelle = in.readString();
        this.sellerId = in.readString();
        this.agentID = in.readString();
        this.paid = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.id = in.readString();
        this.currencyId = in.readString();
    }

    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel source) {
            return new Cart(source);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

    @Override
    public String toString() {
        return "Reservation{" +
                "customer='" + customer + '\'' +
                ", basePrice=" + basePrice +
                ", productList=" + productList +
                ", priceType=" + priceType +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", total=" + total +
                ", closed=" + closed +
                ", customerFirstName='" + customerFirstName + '\'' +
                ", customerLastName='" + customerLastName + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", agentName='" + agentName + '\'' +
                ", dailyID='" + dailyID + '\'' +
                ", isCompany=" + isCompany +
                ", lastModification='" + lastModification + '\'' +
                ", sellerLibelle='" + sellerLibelle + '\'' +
                ", sellerId='" + sellerId + '\'' +
                ", agentID='" + agentID + '\'' +
                ", paid=" + paid +
                ", id='" + id + '\'' +
                ", currencyId='" + currencyId + '\'' +
                '}';
    }
}