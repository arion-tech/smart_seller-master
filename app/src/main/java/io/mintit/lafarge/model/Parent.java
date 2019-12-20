package io.mintit.lafarge.model;


import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class Parent implements Parcelable {


    public static final Parcelable.Creator<Parent> CREATOR = new Parcelable.Creator<Parent>() {
        @Override
        public Parent createFromParcel(Parcel source) {
            return new Parent(source);
        }

        @Override
        public Parent[] newArray(int size) {
            return new Parent[size];
        }
    };

    public Parent() {
    }

    protected Parent(Parcel in) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
