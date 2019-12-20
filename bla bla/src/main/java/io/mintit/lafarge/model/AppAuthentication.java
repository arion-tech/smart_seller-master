package io.mintit.lafarge.model;

/**
 * Created by mint on 23/03/18.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppAuthentication implements Parcelable {

    public final static Parcelable.Creator<AppAuthentication> CREATOR = new Creator<AppAuthentication>() {


        @SuppressWarnings({
                "unchecked"
        })
        public AppAuthentication createFromParcel(Parcel in) {
            return new AppAuthentication(in);
        }

        public AppAuthentication[] newArray(int size) {
            return (new AppAuthentication[size]);
        }

    };
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("token_type")
    @Expose
    private String tokenType;
    @SerializedName("expires_in")
    @Expose
    private Integer expiresIn;
    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;
    @SerializedName("audiance")
    @Expose
    private String audiance;
    @SerializedName("expires")
    @Expose
    private String expires;

    protected AppAuthentication(Parcel in) {
        this.accessToken = ((String) in.readValue((String.class.getClassLoader())));
        this.tokenType = ((String) in.readValue((String.class.getClassLoader())));
        this.expiresIn = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.refreshToken = ((String) in.readValue((String.class.getClassLoader())));
        this.audiance = ((String) in.readValue((String.class.getClassLoader())));
        this.expires = ((String) in.readValue((String.class.getClassLoader())));
    }

    public AppAuthentication() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAudiance() {
        return audiance;
    }

    public void setAudiance(String audiance) {
        this.audiance = audiance;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(accessToken);
        dest.writeValue(tokenType);
        dest.writeValue(expiresIn);
        dest.writeValue(refreshToken);
        dest.writeValue(audiance);
        dest.writeValue(expires);
    }

    public int describeContents() {
        return 0;
    }

}