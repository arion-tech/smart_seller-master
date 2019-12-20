package io.mintit.lafarge.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class CreditCardInfo implements Parcelable {

    public static final Parcelable.Creator<CreditCardInfo> CREATOR = new Parcelable.Creator<CreditCardInfo>() {
        @Override
        public CreditCardInfo createFromParcel(Parcel source) {
            return new CreditCardInfo(source);
        }

        @Override
        public CreditCardInfo[] newArray(int size) {
            return new CreditCardInfo[size];
        }
    };
    @SerializedName("authorizationNumber")
    private String mAuthorizationNumber;
    @SerializedName("transactionNumber")
    private String mTransactionNumber;

    public CreditCardInfo() {
    }

    protected CreditCardInfo(Parcel in) {
        this.mAuthorizationNumber = in.readString();
        this.mTransactionNumber = in.readString();
    }

    public String getAuthorizationNumber() {
        return mAuthorizationNumber;
    }

    public void setAuthorizationNumber(String authorizationNumber) {
        mAuthorizationNumber = authorizationNumber;
    }

    public String getTransactionNumber() {
        return mTransactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        mTransactionNumber = transactionNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mAuthorizationNumber);
        dest.writeString(this.mTransactionNumber);
    }
}
