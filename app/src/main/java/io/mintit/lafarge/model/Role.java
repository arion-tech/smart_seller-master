package io.mintit.lafarge.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Role implements Parcelable {

    public static final Parcelable.Creator<Role> CREATOR = new Parcelable.Creator<Role>() {
        @Override
        public Role createFromParcel(Parcel source) {
            return new Role(source);
        }

        @Override
        public Role[] newArray(int size) {
            return new Role[size];
        }
    };
    @SerializedName("roleId")
    private String mRoleId;
    @SerializedName("userId")
    private String mUserId;

    public Role() {
    }

    protected Role(Parcel in) {
        this.mRoleId = in.readString();
        this.mUserId = in.readString();
    }

    public String getRoleId() {
        return mRoleId;
    }

    public void setRoleId(String roleId) {
        mRoleId = roleId;
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
        dest.writeString(this.mRoleId);
        dest.writeString(this.mUserId);
    }
}
