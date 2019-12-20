package io.mintit.lafarge.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Login implements Parcelable {

    public static final Parcelable.Creator<Login> CREATOR = new Parcelable.Creator<Login>() {
        @Override
        public Login createFromParcel(Parcel source) {
            return new Login(source);
        }

        @Override
        public Login[] newArray(int size) {
            return new Login[size];
        }
    };
    @SerializedName("loginProvider")
    private String mLoginProvider;
    @SerializedName("providerKey")
    private String mProviderKey;
    @SerializedName("userId")
    private String mUserId;

    public Login() {
    }

    protected Login(Parcel in) {
        this.mLoginProvider = in.readString();
        this.mProviderKey = in.readString();
        this.mUserId = in.readString();
    }

    public String getLoginProvider() {
        return mLoginProvider;
    }

    public void setLoginProvider(String loginProvider) {
        mLoginProvider = loginProvider;
    }

    public String getProviderKey() {
        return mProviderKey;
    }

    public void setProviderKey(String providerKey) {
        mProviderKey = providerKey;
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
        dest.writeString(this.mLoginProvider);
        dest.writeString(this.mProviderKey);
        dest.writeString(this.mUserId);
    }
}
