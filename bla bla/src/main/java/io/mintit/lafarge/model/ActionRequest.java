package io.mintit.lafarge.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity
public class ActionRequest implements Parcelable {

    @SerializedName("errorMessage")
    @Expose
    public String errorMessage;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("error")
    @Expose
    boolean error;
    @SerializedName("actionType")
    @Expose
    String actionType;
    @SerializedName("objectType")
    @Expose
    String objectType;
    @SerializedName("transmited")
    @Expose
    boolean transmited = false;
    @SerializedName("creationDate")
    @Expose
    String creationDate;
    @SerializedName("requestNumber")
    @Expose
    int requestNumber;
    @SerializedName("reference")
    @Expose
    String reference;
    @SerializedName("object")
    @Expose
    String object;

    public ActionRequest() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public boolean isTransmited() {
        return transmited;
    }

    public void setTransmited(boolean transmited) {
        this.transmited = transmited;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public int getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(int requestNumber) {
        this.requestNumber = requestNumber;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.errorMessage);
        dest.writeByte(this.error ? (byte) 1 : (byte) 0);
        dest.writeString(this.actionType);
        dest.writeString(this.objectType);
        dest.writeByte(this.transmited ? (byte) 1 : (byte) 0);
        dest.writeString(this.creationDate);
        dest.writeInt(this.requestNumber);
        dest.writeString(this.reference);
    }

    protected ActionRequest(Parcel in) {
        this.errorMessage = in.readString();
        this.error = in.readByte() != 0;
        this.actionType = in.readString();
        this.objectType = in.readString();
        this.transmited = in.readByte() != 0;
        this.creationDate = in.readString();
        this.requestNumber = in.readInt();
        this.reference = in.readString();
        this.object = in.readParcelable(Object.class.getClassLoader());
    }

    public static final Creator<ActionRequest> CREATOR = new Creator<ActionRequest>() {
        @Override
        public ActionRequest createFromParcel(Parcel source) {
            return new ActionRequest(source);
        }

        @Override
        public ActionRequest[] newArray(int size) {
            return new ActionRequest[size];
        }
    };
}
