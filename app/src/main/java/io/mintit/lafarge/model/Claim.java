package io.mintit.lafarge.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Claim implements Parcelable {

    public static final Parcelable.Creator<Claim> CREATOR = new Parcelable.Creator<Claim>() {
        @Override
        public Claim createFromParcel(Parcel source) {
            return new Claim(source);
        }

        @Override
        public Claim[] newArray(int size) {
            return new Claim[size];
        }
    };
    @SerializedName("claimType")
    private String mClaimType;
    @SerializedName("claimValue")
    private String mClaimValue;
    @SerializedName("id")
    private Long mId;
    @SerializedName("userId")
    private String mUserId;

    public Claim() {
    }

    protected Claim(Parcel in) {
        this.mClaimType = in.readString();
        this.mClaimValue = in.readString();
        this.mId = (Long) in.readValue(Long.class.getClassLoader());
        this.mUserId = in.readString();
    }

    public String getClaimType() {
        return mClaimType;
    }

    public void setClaimType(String claimType) {
        mClaimType = claimType;
    }

    public String getClaimValue() {
        return mClaimValue;
    }

    public void setClaimValue(String claimValue) {
        mClaimValue = claimValue;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mClaimType);
        dest.writeString(this.mClaimValue);
        dest.writeValue(this.mId);
        dest.writeString(this.mUserId);
    }
}
