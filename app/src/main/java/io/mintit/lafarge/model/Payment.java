package io.mintit.lafarge.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Payment implements Parcelable {

    @SerializedName("amount")
    private Double mAmount;
     @SerializedName("paymentId")
    private int paymentId;
    @SerializedName("currencyId")
    private String mCurrencyId;
    @SerializedName("dueDate")
    private String mDueDate;

    @SerializedName("isReceivedPayment")
    private Boolean mIsReceivedPayment;
    @SerializedName("methodId")
    private String mMethodId;


    public Payment() {
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public Double getAmount() {
        return mAmount;
    }

    public void setAmount(double amount) {
        mAmount = amount;
    }



    public String getCurrencyId() {
        return mCurrencyId;
    }

    public void setCurrencyId(String currencyId) {
        mCurrencyId = currencyId;
    }

    public String getDueDate() {
        return mDueDate;
    }

    public void setDueDate(String dueDate) {
        mDueDate = dueDate;
    }


    public Boolean getIsReceivedPayment() {
        return mIsReceivedPayment;
    }

    public void setIsReceivedPayment(Boolean isReceivedPayment) {
        mIsReceivedPayment = isReceivedPayment;
    }

    public String getMethodId() {
        return mMethodId;
    }

    public void setMethodId(String methodId) {
        mMethodId = methodId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mAmount);
        dest.writeInt(this.paymentId);

        dest.writeString(this.mCurrencyId);
        dest.writeString(this.mDueDate);
        dest.writeValue(this.mIsReceivedPayment);
        dest.writeString(this.mMethodId);
    }

    protected Payment(Parcel in) {
        this.mAmount = (Double) in.readValue(Double.class.getClassLoader());
        this.paymentId = in.readInt();

        this.mCurrencyId = in.readString();
        this.mDueDate = in.readString();
        this.mIsReceivedPayment = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.mMethodId = in.readString();
    }

    public static final Creator<Payment> CREATOR = new Creator<Payment>() {
        @Override
        public Payment createFromParcel(Parcel source) {
            return new Payment(source);
        }

        @Override
        public Payment[] newArray(int size) {
            return new Payment[size];
        }
    };
}
