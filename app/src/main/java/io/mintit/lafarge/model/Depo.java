package io.mintit.lafarge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class Depo implements Parcelable {

    public static final Parcelable.Creator<Depo> CREATOR = new Parcelable.Creator<Depo>() {
        @Override
        public Depo createFromParcel(Parcel source) {
            return new Depo(source);
        }

        @Override
        public Depo[] newArray(int size) {
            return new Depo[size];
        }
    };
    @SerializedName("abrege")
    private String mAbrege;
    @SerializedName("address1")
    private String mAddress1;
    @SerializedName("address2")
    private String mAddress2;
    @SerializedName("address3")
    private String mAddress3;
    @SerializedName("city")
    private String mCity;
    @SerializedName("closed")
    private String mClosed;
    @SerializedName("codeDepo")
    private String mCodeDepo;
    @SerializedName("codeEtablissement")
    private String mCodeEtablissement;
    @SerializedName("country")
    private Country mCountry;
    @SerializedName("countryCode")
    private String mCountryCode;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("etablissement")
    private Etablissement mEtablissement;
    @SerializedName("faxNumber")
    private String mFaxNumber;
    @SerializedName("id")
    private Long mId;
    @SerializedName("libelle")
    private String mLibelle;
    @SerializedName("phoneNumber")
    private String mPhoneNumber;
    @SerializedName("postalCode")
    private String mPostalCode;
    @SerializedName("serialNumber")
    private String mSerialNumber;
    @SerializedName("stocks")
    private List<Stock> mStocks;

    public Depo() {
    }

    protected Depo(Parcel in) {
        this.mAbrege = in.readString();
        this.mAddress1 = in.readString();
        this.mAddress2 = in.readString();
        this.mAddress3 = in.readString();
        this.mCity = in.readString();
        this.mClosed = in.readString();
        this.mCodeDepo = in.readString();
        this.mCodeEtablissement = in.readString();
        this.mCountry = in.readParcelable(Country.class.getClassLoader());
        this.mCountryCode = in.readString();
        this.mEmail = in.readString();
        this.mEtablissement = in.readParcelable(Etablissement.class.getClassLoader());
        this.mFaxNumber = in.readString();
        this.mId = (Long) in.readValue(Long.class.getClassLoader());
        this.mLibelle = in.readString();
        this.mPhoneNumber = in.readString();
        this.mPostalCode = in.readString();
        this.mSerialNumber = in.readString();
        this.mStocks = new ArrayList<Stock>();
        in.readList(this.mStocks, Stock.class.getClassLoader());
    }

    public String getAbrege() {
        return mAbrege;
    }

    public void setAbrege(String abrege) {
        mAbrege = abrege;
    }

    public String getAddress1() {
        return mAddress1;
    }

    public void setAddress1(String address1) {
        mAddress1 = address1;
    }

    public String getAddress2() {
        return mAddress2;
    }

    public void setAddress2(String address2) {
        mAddress2 = address2;
    }

    public String getAddress3() {
        return mAddress3;
    }

    public void setAddress3(String address3) {
        mAddress3 = address3;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getClosed() {
        return mClosed;
    }

    public void setClosed(String closed) {
        mClosed = closed;
    }

    public String getCodeDepo() {
        return mCodeDepo;
    }

    public void setCodeDepo(String codeDepo) {
        mCodeDepo = codeDepo;
    }

    public String getCodeEtablissement() {
        return mCodeEtablissement;
    }

    public void setCodeEtablissement(String codeEtablissement) {
        mCodeEtablissement = codeEtablissement;
    }

    public Country getCountry() {
        return mCountry;
    }

    public void setCountry(Country country) {
        mCountry = country;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String countryCode) {
        mCountryCode = countryCode;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public Etablissement getEtablissement() {
        return mEtablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        mEtablissement = etablissement;
    }

    public String getFaxNumber() {
        return mFaxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        mFaxNumber = faxNumber;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getLibelle() {
        return mLibelle;
    }

    public void setLibelle(String libelle) {
        mLibelle = libelle;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public String getPostalCode() {
        return mPostalCode;
    }

    public void setPostalCode(String postalCode) {
        mPostalCode = postalCode;
    }

    public String getSerialNumber() {
        return mSerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        mSerialNumber = serialNumber;
    }

    public List<Stock> getStocks() {
        return mStocks;
    }

    public void setStocks(List<Stock> stocks) {
        mStocks = stocks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mAbrege);
        dest.writeString(this.mAddress1);
        dest.writeString(this.mAddress2);
        dest.writeString(this.mAddress3);
        dest.writeString(this.mCity);
        dest.writeString(this.mClosed);
        dest.writeString(this.mCodeDepo);
        dest.writeString(this.mCodeEtablissement);
        dest.writeParcelable(this.mCountry, flags);
        dest.writeString(this.mCountryCode);
        dest.writeString(this.mEmail);
        dest.writeParcelable(this.mEtablissement, flags);
        dest.writeString(this.mFaxNumber);
        dest.writeValue(this.mId);
        dest.writeString(this.mLibelle);
        dest.writeString(this.mPhoneNumber);
        dest.writeString(this.mPostalCode);
        dest.writeString(this.mSerialNumber);
        dest.writeList(this.mStocks);
    }
}
