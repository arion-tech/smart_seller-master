package io.mintit.lafarge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class Wisher implements Parcelable {

    public static final Parcelable.Creator<Wisher> CREATOR = new Parcelable.Creator<Wisher>() {
        @Override
        public Wisher createFromParcel(Parcel source) {
            return new Wisher(source);
        }

        @Override
        public Wisher[] newArray(int size) {
            return new Wisher[size];
        }
    };
    @SerializedName("accessFailedCount")
    private Long mAccessFailedCount;
    @SerializedName("claims")
    private List<Claim> mClaims;
    @SerializedName("customerId")
    private String mCustomerId;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("emailConfirmed")
    private Boolean mEmailConfirmed;
    @SerializedName("id")
    private String mId;
    @SerializedName("lockoutEnabled")
    private Boolean mLockoutEnabled;
    @SerializedName("lockoutEndDateUtc")
    private String mLockoutEndDateUtc;
    @SerializedName("logins")
    private List<Login> mLogins;
    @SerializedName("passwordHash")
    private String mPasswordHash;
    @SerializedName("phoneNumber")
    private String mPhoneNumber;
    @SerializedName("phoneNumberConfirmed")
    private Boolean mPhoneNumberConfirmed;
    @SerializedName("roles")
    private List<Role> mRoles;
    @SerializedName("securityStamp")
    private String mSecurityStamp;
    @SerializedName("twoFactorEnabled")
    private Boolean mTwoFactorEnabled;
    @SerializedName("userName")
    private String mUserName;
    @SerializedName("wishList")
    private List<WishList> mWishList;

    public Wisher() {
    }

    protected Wisher(Parcel in) {
        this.mAccessFailedCount = (Long) in.readValue(Long.class.getClassLoader());
        this.mClaims = in.createTypedArrayList(Claim.CREATOR);
        this.mCustomerId = in.readString();
        this.mEmail = in.readString();
        this.mEmailConfirmed = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.mId = in.readString();
        this.mLockoutEnabled = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.mLockoutEndDateUtc = in.readString();
        this.mLogins = in.createTypedArrayList(Login.CREATOR);
        this.mPasswordHash = in.readString();
        this.mPhoneNumber = in.readString();
        this.mPhoneNumberConfirmed = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.mRoles = in.createTypedArrayList(Role.CREATOR);
        this.mSecurityStamp = in.readString();
        this.mTwoFactorEnabled = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.mUserName = in.readString();
        this.mWishList = new ArrayList<WishList>();
        in.readList(this.mWishList, WishList.class.getClassLoader());
    }

    public Long getAccessFailedCount() {
        return mAccessFailedCount;
    }

    public void setAccessFailedCount(Long accessFailedCount) {
        mAccessFailedCount = accessFailedCount;
    }

    public List<Claim> getClaims() {
        return mClaims;
    }

    public void setClaims(List<Claim> claims) {
        mClaims = claims;
    }

    public String getCustomerId() {
        return mCustomerId;
    }

    public void setCustomerId(String customerId) {
        mCustomerId = customerId;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public Boolean getEmailConfirmed() {
        return mEmailConfirmed;
    }

    public void setEmailConfirmed(Boolean emailConfirmed) {
        mEmailConfirmed = emailConfirmed;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Boolean getLockoutEnabled() {
        return mLockoutEnabled;
    }

    public void setLockoutEnabled(Boolean lockoutEnabled) {
        mLockoutEnabled = lockoutEnabled;
    }

    public String getLockoutEndDateUtc() {
        return mLockoutEndDateUtc;
    }

    public void setLockoutEndDateUtc(String lockoutEndDateUtc) {
        mLockoutEndDateUtc = lockoutEndDateUtc;
    }

    public List<Login> getLogins() {
        return mLogins;
    }

    public void setLogins(List<Login> logins) {
        mLogins = logins;
    }

    public String getPasswordHash() {
        return mPasswordHash;
    }

    public void setPasswordHash(String passwordHash) {
        mPasswordHash = passwordHash;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public Boolean getPhoneNumberConfirmed() {
        return mPhoneNumberConfirmed;
    }

    public void setPhoneNumberConfirmed(Boolean phoneNumberConfirmed) {
        mPhoneNumberConfirmed = phoneNumberConfirmed;
    }

    public List<Role> getRoles() {
        return mRoles;
    }

    public void setRoles(List<Role> roles) {
        mRoles = roles;
    }

    public String getSecurityStamp() {
        return mSecurityStamp;
    }

    public void setSecurityStamp(String securityStamp) {
        mSecurityStamp = securityStamp;
    }

    public Boolean getTwoFactorEnabled() {
        return mTwoFactorEnabled;
    }

    public void setTwoFactorEnabled(Boolean twoFactorEnabled) {
        mTwoFactorEnabled = twoFactorEnabled;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public List<WishList> getWishList() {
        return mWishList;
    }

    public void setWishList(List<WishList> wishList) {
        mWishList = wishList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mAccessFailedCount);
        dest.writeTypedList(this.mClaims);
        dest.writeString(this.mCustomerId);
        dest.writeString(this.mEmail);
        dest.writeValue(this.mEmailConfirmed);
        dest.writeString(this.mId);
        dest.writeValue(this.mLockoutEnabled);
        dest.writeString(this.mLockoutEndDateUtc);
        dest.writeTypedList(this.mLogins);
        dest.writeString(this.mPasswordHash);
        dest.writeString(this.mPhoneNumber);
        dest.writeValue(this.mPhoneNumberConfirmed);
        dest.writeTypedList(this.mRoles);
        dest.writeString(this.mSecurityStamp);
        dest.writeValue(this.mTwoFactorEnabled);
        dest.writeString(this.mUserName);
        dest.writeList(this.mWishList);
    }
}
