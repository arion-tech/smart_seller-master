package io.mintit.lafarge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class IdCategories implements Parcelable {

    public final static Parcelable.Creator<IdCategories> CREATOR = new Creator<IdCategories>() {


        @SuppressWarnings({
                "unchecked"
        })
        public IdCategories createFromParcel(Parcel in) {
            return new IdCategories(in);
        }

        public IdCategories[] newArray(int size) {
            return (new IdCategories[size]);
        }

    };
    @SerializedName("idCategorie")
    @Expose
    private Integer idCategorie;
    @SerializedName("idArticles")
    @Expose
    private ArrayList<Integer> idArticles = null;

    protected IdCategories(Parcel in) {
        this.idCategorie = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.idArticles, (java.lang.Integer.class.getClassLoader()));
    }

    public IdCategories() {
    }

    public Integer getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(Integer idCategorie) {
        this.idCategorie = idCategorie;
    }

    public List<Integer> getIdArticles() {
        return idArticles;
    }

    public void setIdArticles(ArrayList<Integer> idArticles) {
        this.idArticles = idArticles;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(idCategorie);
        dest.writeList(idArticles);
    }

    public int describeContents() {
        return 0;
    }

}