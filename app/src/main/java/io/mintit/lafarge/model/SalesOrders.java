package io.mintit.lafarge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SalesOrders implements Parcelable {

    @SerializedName("transNum")
    private  int transNum;
    @SerializedName("storeCode")
    private String storeCode;
    @SerializedName("transType")
    private String transType;
    @SerializedName("operatorCode")
    private  String operatorCode;
    @SerializedName("customerId")
    private  String customerId;
    @SerializedName("salesItems")
    private List<SalesItems> salesItems;
    @SerializedName("globalAmount")
    private  String globalAmount;
    @SerializedName("currencyCode")
    private  String currencyCode;
    @SerializedName("Origin")
    private  String Origin;

    public SalesOrders() {

    }
    protected SalesOrders(Parcel in) {

        this.transNum = in.readInt();
        this.salesItems = in.createTypedArrayList(SalesItems.CREATOR);
        this.storeCode = in.readString();
        this.customerId = in.readString();
        this.operatorCode = in.readString();
        this.customerId = in.readString();
        this.globalAmount = in.readString();
        this.currencyCode = in.readString();
    }

    public static final Creator<SalesOrders> CREATOR = new Creator<SalesOrders>() {
        @Override
        public SalesOrders createFromParcel(Parcel in) {
            return new SalesOrders(in);
        }

        @Override
        public SalesOrders[] newArray(int size) {
            return new SalesOrders[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.transNum);
        dest.writeString(this.storeCode);
        dest.writeTypedList(this.salesItems);
        dest.writeString(this.operatorCode);
        dest.writeString(this.customerId);
        dest.writeString(this.globalAmount);
        dest.writeString(this.transType);
        dest.writeString(this.currencyCode);
    }

    public int getTransNum() {
        return transNum;
    }

    public void setTransNum(int transNum) {
        this.transNum = transNum;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<SalesItems> getSalesItems() {
        return salesItems;
    }

    public void setSalesItems(List<SalesItems> salesItems) {
        this.salesItems = salesItems;
    }

    public String getGlobalAmount() {
        return globalAmount;
    }

    public void setGlobalAmount(String globalAmount) {
        this.globalAmount = globalAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }



    public static Creator<SalesOrders> getCREATOR() {
        return CREATOR;
    }
}
