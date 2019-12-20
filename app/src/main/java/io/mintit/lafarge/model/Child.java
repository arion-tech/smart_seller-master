package io.mintit.lafarge.model;


import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class Child implements Parcelable {


    public static final Parcelable.Creator<Child> CREATOR = new Parcelable.Creator<Child>() {
        @Override
        public Child createFromParcel(Parcel source) {
            return new Child(source);
        }

        @Override
        public Child[] newArray(int size) {
            return new Child[size];
        }
    };

    public Child() {
    }

    protected Child(Parcel in) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
