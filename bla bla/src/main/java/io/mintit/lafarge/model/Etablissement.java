package io.mintit.lafarge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;



public class Etablissement implements Parcelable {

    public static final Parcelable.Creator<Etablissement> CREATOR = new Parcelable.Creator<Etablissement>() {
        @Override
        public Etablissement createFromParcel(Parcel source) {
            return new Etablissement(source);
        }

        @Override
        public Etablissement[] newArray(int size) {
            return new Etablissement[size];
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
    @SerializedName("codeEtablissement")
    private String mCodeEtablissement;
    @SerializedName("countryCode")
    private String mCountryCode;
    @SerializedName("currencyId")
    private String mCurrencyId;
    @SerializedName("customers")
    private List<Customer> mCustomers;
    @SerializedName("depos")
    private List<Depo> mDepos;
    @SerializedName("id")
    private Long mId;
    @SerializedName("libelle")
    private String mLibelle;
    @SerializedName("postalCode")
    private String mPostalCode;
    @SerializedName("promotions")
    private List<Promotion> mPromotions;
    @SerializedName("tarifs")
    private List<Tarif> mTarifs;
    @SerializedName("typeTarifTtc")
    private String mTypeTarifTtc;

    public Etablissement() {
    }

    protected Etablissement(Parcel in) {
        this.mAbrege = in.readString();
        this.mAddress1 = in.readString();
        this.mAddress2 = in.readString();
        this.mAddress3 = in.readString();
        this.mCity = in.readString();
        this.mCodeEtablissement = in.readString();
        this.mCountryCode = in.readString();
        this.mCurrencyId = in.readString();
        this.mCustomers = in.createTypedArrayList(Customer.CREATOR);
        this.mDepos = in.createTypedArrayList(Depo.CREATOR);
        this.mId = (Long) in.readValue(Long.class.getClassLoader());
        this.mLibelle = in.readString();
        this.mPostalCode = in.readString();
        this.mPromotions = new ArrayList<Promotion>();
        in.readList(this.mPromotions, Promotion.class.getClassLoader());
        this.mTarifs = new ArrayList<Tarif>();
        in.readList(this.mTarifs, Tarif.class.getClassLoader());
        this.mTypeTarifTtc = in.readString();
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

    public String getCodeEtablissement() {
        return mCodeEtablissement;
    }

    public void setCodeEtablissement(String codeEtablissement) {
        mCodeEtablissement = codeEtablissement;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String countryCode) {
        mCountryCode = countryCode;
    }

    public String getCurrencyId() {
        return mCurrencyId;
    }

    public void setCurrencyId(String currencyId) {
        mCurrencyId = currencyId;
    }

    public List<Customer> getCustomers() {
        return mCustomers;
    }

    public void setCustomers(List<Customer> customers) {
        mCustomers = customers;
    }

    public List<Depo> getDepos() {
        return mDepos;
    }

    public void setDepos(List<Depo> depos) {
        mDepos = depos;
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

    public String getPostalCode() {
        return mPostalCode;
    }

    public void setPostalCode(String postalCode) {
        mPostalCode = postalCode;
    }

    public List<Promotion> getPromotions() {
        return mPromotions;
    }

    public void setPromotions(List<Promotion> promotions) {
        mPromotions = promotions;
    }

    public List<Tarif> getTarifs() {
        return mTarifs;
    }

    public void setTarifs(List<Tarif> tarifs) {
        mTarifs = tarifs;
    }

    public String getTypeTarifTtc() {
        return mTypeTarifTtc;
    }

    public void setTypeTarifTtc(String typeTarifTtc) {
        mTypeTarifTtc = typeTarifTtc;
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
        dest.writeString(this.mCodeEtablissement);
        dest.writeString(this.mCountryCode);
        dest.writeString(this.mCurrencyId);
        dest.writeTypedList(this.mCustomers);
        dest.writeTypedList(this.mDepos);
        dest.writeValue(this.mId);
        dest.writeString(this.mLibelle);
        dest.writeString(this.mPostalCode);
        dest.writeList(this.mPromotions);
        dest.writeList(this.mTarifs);
        dest.writeString(this.mTypeTarifTtc);
    }
}
