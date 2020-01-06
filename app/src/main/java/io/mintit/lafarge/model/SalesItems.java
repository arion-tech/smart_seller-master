package io.mintit.lafarge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalesItems implements Parcelable {

    @SerializedName("qty")
    private  int qty;
    @SerializedName("staffCode")
    private String staffCode;
    @SerializedName("soldPrice")
    private double soldPrice;
    @SerializedName("currencyCode")
    private  String currencyCode;
    @SerializedName("product")
    @Expose
    private Product product;

    public SalesItems() {

    }

    protected SalesItems(Parcel in) {
        qty = in.readInt();
        staffCode = in.readString();
        soldPrice = in.readDouble();
        currencyCode = in.readString();
        product = in.readParcelable(Product.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(qty);
        dest.writeString(staffCode);
        dest.writeDouble(soldPrice);
        dest.writeString(currencyCode);
        dest.writeParcelable(product, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SalesItems> CREATOR = new Creator<SalesItems>() {
        @Override
        public SalesItems createFromParcel(Parcel in) {
            return new SalesItems(in);
        }

        @Override
        public SalesItems[] newArray(int size) {
            return new SalesItems[size];
        }
    };

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public double getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(double soldPrice) {
        this.soldPrice = soldPrice;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public static Creator<SalesItems> getCREATOR() {
        return CREATOR;
    }
}
