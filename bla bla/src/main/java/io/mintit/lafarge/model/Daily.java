package io.mintit.lafarge.model;

/**
 * Created by mint on 19/04/18.
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Daily implements Parcelable {
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("startDate")
    @Expose
    private String startTime;
    @SerializedName("endDate")
    @Expose
    private String endTime;
    @SerializedName("opened")
    @Expose
    private Boolean opened;
    @SerializedName("userID")
    @Expose
    private String userID;
    @Ignore
    private SynchronisationObject synchronisationObject;

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }


    public Daily() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Boolean getOpened() {
        return opened;
    }

    public void setOpened(Boolean opened) {
        this.opened = opened;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.reference);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeValue(this.opened);
        dest.writeString(this.userID);
    }

    protected Daily(Parcel in) {
        this.id = in.readString();
        this.reference = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.opened = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.userID = in.readString();
    }

    public static final Creator<Daily> CREATOR = new Creator<Daily>() {
        @Override
        public Daily createFromParcel(Parcel source) {
            return new Daily(source);
        }

        @Override
        public Daily[] newArray(int size) {
            return new Daily[size];
        }
    };



}