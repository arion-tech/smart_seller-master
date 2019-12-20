package io.mintit.lafarge.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Country implements Parcelable {

    public static final Parcelable.Creator<Country> CREATOR = new Parcelable.Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel source) {
            return new Country(source);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };
    @SerializedName("id")
    private Long mId;
    @SerializedName("pY_CODEBANCAIRE")
    private String mPYCODEBANCAIRE;
    @SerializedName("pY_CODEINSEE")
    private String mPYCODEINSEE;
    @SerializedName("pY_CODEISO2")
    private String mPYCODEISO2;
    @SerializedName("pY_LIBELLE")
    private String mPYLIBELLE;
    @SerializedName("pY_PAYS")
    private String mPYPAYS;

    public Country() {
    }

    protected Country(Parcel in) {
        this.mId = (Long) in.readValue(Long.class.getClassLoader());
        this.mPYCODEBANCAIRE = in.readString();
        this.mPYCODEINSEE = in.readString();
        this.mPYCODEISO2 = in.readString();
        this.mPYLIBELLE = in.readString();
        this.mPYPAYS = in.readString();
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getPYCODEBANCAIRE() {
        return mPYCODEBANCAIRE;
    }

    public void setPYCODEBANCAIRE(String pYCODEBANCAIRE) {
        mPYCODEBANCAIRE = pYCODEBANCAIRE;
    }

    public String getPYCODEINSEE() {
        return mPYCODEINSEE;
    }

    public void setPYCODEINSEE(String pYCODEINSEE) {
        mPYCODEINSEE = pYCODEINSEE;
    }

    public String getPYCODEISO2() {
        return mPYCODEISO2;
    }

    public void setPYCODEISO2(String pYCODEISO2) {
        mPYCODEISO2 = pYCODEISO2;
    }

    public String getPYLIBELLE() {
        return mPYLIBELLE;
    }

    public void setPYLIBELLE(String pYLIBELLE) {
        mPYLIBELLE = pYLIBELLE;
    }

    public String getPYPAYS() {
        return mPYPAYS;
    }

    public void setPYPAYS(String pYPAYS) {
        mPYPAYS = pYPAYS;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mId);
        dest.writeString(this.mPYCODEBANCAIRE);
        dest.writeString(this.mPYCODEINSEE);
        dest.writeString(this.mPYCODEISO2);
        dest.writeString(this.mPYLIBELLE);
        dest.writeString(this.mPYPAYS);
    }
}
