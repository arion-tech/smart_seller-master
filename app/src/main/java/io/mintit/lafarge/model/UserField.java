package io.mintit.lafarge.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class UserField implements Parcelable {

    public static final Parcelable.Creator<UserField> CREATOR = new Parcelable.Creator<UserField>() {
        @Override
        public UserField createFromParcel(Parcel source) {
            return new UserField(source);
        }

        @Override
        public UserField[] newArray(int size) {
            return new UserField[size];
        }
    };
    @SerializedName("booleanValue")
    private Boolean mBooleanValue;
    @SerializedName("dateValue")
    private String mDateValue;
    @SerializedName("id")
    private Long mId;
    @SerializedName("listElementValue")
    private String mListElementValue;
    @SerializedName("numberValue")
    private Long mNumberValue;
    @SerializedName("textValue")
    private String mTextValue;
    @SerializedName("valueType")
    private Long mValueType;

    public UserField() {
    }

    protected UserField(Parcel in) {
        this.mBooleanValue = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.mDateValue = in.readString();
        this.mId = (Long) in.readValue(Long.class.getClassLoader());
        this.mListElementValue = in.readString();
        this.mNumberValue = (Long) in.readValue(Long.class.getClassLoader());
        this.mTextValue = in.readString();
        this.mValueType = (Long) in.readValue(Long.class.getClassLoader());
    }

    public Boolean getBooleanValue() {
        return mBooleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {
        mBooleanValue = booleanValue;
    }

    public String getDateValue() {
        return mDateValue;
    }

    public void setDateValue(String dateValue) {
        mDateValue = dateValue;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getListElementValue() {
        return mListElementValue;
    }

    public void setListElementValue(String listElementValue) {
        mListElementValue = listElementValue;
    }

    public Long getNumberValue() {
        return mNumberValue;
    }

    public void setNumberValue(Long numberValue) {
        mNumberValue = numberValue;
    }

    public String getTextValue() {
        return mTextValue;
    }

    public void setTextValue(String textValue) {
        mTextValue = textValue;
    }

    public Long getValueType() {
        return mValueType;
    }

    public void setValueType(Long valueType) {
        mValueType = valueType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mBooleanValue);
        dest.writeString(this.mDateValue);
        dest.writeValue(this.mId);
        dest.writeString(this.mListElementValue);
        dest.writeValue(this.mNumberValue);
        dest.writeString(this.mTextValue);
        dest.writeValue(this.mValueType);
    }
}
