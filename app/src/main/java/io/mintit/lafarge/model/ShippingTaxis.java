package io.mintit.lafarge.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class ShippingTaxis implements Parcelable {

    public static final Creator<ShippingTaxis> CREATOR = new Creator<ShippingTaxis>() {
        @Override
        public ShippingTaxis createFromParcel(Parcel source) {
            return new ShippingTaxis(source);
        }

        @Override
        public ShippingTaxis[] newArray(int size) {
            return new ShippingTaxis[size];
        }
    };
    @SerializedName("taxCode")
    private String mTaxCode;
    @SerializedName("taxExcludedAmount")
    private Long mTaxExcludedAmount;
    @SerializedName("taxIncludedAmount")
    private Long mTaxIncludedAmount;

    public ShippingTaxis() {
    }

    protected ShippingTaxis(Parcel in) {
        this.mTaxCode = in.readString();
        this.mTaxExcludedAmount = (Long) in.readValue(Long.class.getClassLoader());
        this.mTaxIncludedAmount = (Long) in.readValue(Long.class.getClassLoader());
    }

    public String getTaxCode() {
        return mTaxCode;
    }

    public void setTaxCode(String taxCode) {
        mTaxCode = taxCode;
    }

    public Long getTaxExcludedAmount() {
        return mTaxExcludedAmount;
    }

    public void setTaxExcludedAmount(Long taxExcludedAmount) {
        mTaxExcludedAmount = taxExcludedAmount;
    }

    public Long getTaxIncludedAmount() {
        return mTaxIncludedAmount;
    }

    public void setTaxIncludedAmount(Long taxIncludedAmount) {
        mTaxIncludedAmount = taxIncludedAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTaxCode);
        dest.writeValue(this.mTaxExcludedAmount);
        dest.writeValue(this.mTaxIncludedAmount);
    }
}
