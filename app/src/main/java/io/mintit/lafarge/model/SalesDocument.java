package io.mintit.lafarge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;


@SuppressWarnings("unused")
public class SalesDocument implements Parcelable {

    @SerializedName("active")
    private Boolean mActive;
    @SerializedName("billingStatus")
    private String mBillingStatus;
    @SerializedName("commandes")
    private List<Commande> mCommandes;
    @SerializedName("currencyId")
    private String mCurrencyId;
    @SerializedName("customerId")
    private String customerId;
    @SerializedName("address")
    private Address address;
    @SerializedName("date")
    private String mDate;
    @SerializedName("deliveryStoreId")
    private String mDeliveryStoreId;
    @SerializedName("deliveryType")
    private String mDeliveryType;
    @SerializedName("followUpStatus")
    private String mFollowUpStatus;
    @SerializedName("internalReference")
    private String mInternalReference;
    @SerializedName("linesUnmodifiable")
    private Boolean mLinesUnmodifiable;
    @SerializedName("origin")
    private String mOrigin;
    @SerializedName("paymentStatus")
    private String mPaymentStatus;
    @SerializedName("payments")
    private List<Payment> mPayments;


    @SerializedName("returnStatus")
    private String mReturnStatus;
    @SerializedName("shippingStatus")
    private String mShippingStatus;

    @SerializedName("storeId")
    private String mStoreId;


    @SerializedName("type")
    private String mType;
    @SerializedName("custmerReference")
    private String custmerReference;

    public SalesDocument() {
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Boolean getActive() {
        return mActive;
    }

    public void setActive(Boolean active) {
        mActive = active;
    }

    public String getBillingStatus() {
        return mBillingStatus;
    }

    public void setBillingStatus(String billingStatus) {
        mBillingStatus = billingStatus;
    }

    public List<Commande> getCommandes() {
        return mCommandes;
    }

    public void setCommandes(List<Commande> commandes) {
        mCommandes = commandes;
    }

    public String getCurrencyId() {
        return mCurrencyId;
    }

    public void setCurrencyId(String currencyId) {
        mCurrencyId = currencyId;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getDeliveryStoreId() {
        return mDeliveryStoreId;
    }

    public void setDeliveryStoreId(String deliveryStoreId) {
        mDeliveryStoreId = deliveryStoreId;
    }

    public String getDeliveryType() {
        return mDeliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        mDeliveryType = deliveryType;
    }


    public String getFollowUpStatus() {
        return mFollowUpStatus;
    }

    public void setFollowUpStatus(String followUpStatus) {
        mFollowUpStatus = followUpStatus;
    }

    public String getInternalReference() {
        return mInternalReference;
    }

    public void setInternalReference(String internalReference) {
        mInternalReference = internalReference;
    }



    public Boolean getLinesUnmodifiable() {
        return mLinesUnmodifiable;
    }

    public void setLinesUnmodifiable(Boolean linesUnmodifiable) {
        mLinesUnmodifiable = linesUnmodifiable;
    }



    public String getOrigin() {
        return mOrigin;
    }

    public void setOrigin(String origin) {
        mOrigin = origin;
    }

    public String getPaymentStatus() {
        return mPaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        mPaymentStatus = paymentStatus;
    }

    public List<Payment> getPayments() {
        return mPayments;
    }

    public void setPayments(List<Payment> payments) {
        mPayments = payments;
    }



    public String getReturnStatus() {
        return mReturnStatus;
    }

    public void setReturnStatus(String returnStatus) {
        mReturnStatus = returnStatus;
    }

    public String getShippingStatus() {
        return mShippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        mShippingStatus = shippingStatus;
    }



    public String getStoreId() {
        return mStoreId;
    }

    public void setStoreId(String storeId) {
        mStoreId = storeId;
    }


    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mActive);
        dest.writeString(this.mBillingStatus);
        dest.writeTypedList(this.mCommandes);
        dest.writeString(this.mCurrencyId);
        dest.writeString(this.customerId);
        dest.writeParcelable(this.address, flags);
        dest.writeString(this.mDate);
        dest.writeString(this.mDeliveryStoreId);
        dest.writeString(this.mDeliveryType);
        dest.writeString(this.mFollowUpStatus);
        dest.writeString(this.mInternalReference);
        dest.writeValue(this.mLinesUnmodifiable);
        dest.writeString(this.mOrigin);
        dest.writeString(this.mPaymentStatus);
        dest.writeTypedList(this.mPayments);
        dest.writeString(this.mReturnStatus);
        dest.writeString(this.mShippingStatus);
        dest.writeString(this.mStoreId);
        dest.writeString(this.mType);
    }

    protected SalesDocument(Parcel in) {
        this.mActive = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.mBillingStatus = in.readString();
        this.mCommandes = in.createTypedArrayList(Commande.CREATOR);
        this.mCurrencyId = in.readString();
        this.customerId = in.readString();
        this.address = in.readParcelable(Address.class.getClassLoader());
        this.mDate = in.readString();
        this.mDeliveryStoreId = in.readString();
        this.mDeliveryType = in.readString();
        this.mFollowUpStatus = in.readString();
        this.mInternalReference = in.readString();
        this.mLinesUnmodifiable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.mOrigin = in.readString();
        this.mPaymentStatus = in.readString();
        this.mPayments = in.createTypedArrayList(Payment.CREATOR);
        this.mReturnStatus = in.readString();
        this.mShippingStatus = in.readString();
        this.mStoreId = in.readString();
        this.mType = in.readString();
    }

    public static final Creator<SalesDocument> CREATOR = new Creator<SalesDocument>() {
        @Override
        public SalesDocument createFromParcel(Parcel source) {
            return new SalesDocument(source);
        }

        @Override
        public SalesDocument[] newArray(int size) {
            return new SalesDocument[size];
        }
    };

    public void setCustmerReference(String custmerReference) {
        this.custmerReference = custmerReference;
    }

    public String getCustmerReference() {
        return custmerReference;
    }
}
