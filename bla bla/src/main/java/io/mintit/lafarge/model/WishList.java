package io.mintit.lafarge.model;


import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class WishList implements Parcelable {


    public static final Parcelable.Creator<WishList> CREATOR = new Parcelable.Creator<WishList>() {
        @Override
        public WishList createFromParcel(Parcel source) {
            return new WishList(source);
        }

        @Override
        public WishList[] newArray(int size) {
            return new WishList[size];
        }
    };

    public WishList() {
    }

    protected WishList(Parcel in) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
