package io.mintit.lafarge.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;


@Entity
public class Dimension implements Parcelable {

    public static final Parcelable.Creator<Dimension> CREATOR = new Parcelable.Creator<Dimension>() {
        @Override
        public Dimension createFromParcel(Parcel source) {
            return new Dimension(source);
        }

        @Override
        public Dimension[] newArray(int size) {
            return new Dimension[size];
        }
    };

    @SerializedName("abregeFamily")
    private String mAbregeFamily;
    @SerializedName("articles")
    @Ignore
    private List<Product> mProducts;
    @SerializedName("code")
    private String mCode;
    @SerializedName("codeFamily")
    private String mCodeFamily;
    @SerializedName("codeType")
    private String mCodeType;
    @SerializedName("id")
    @PrimaryKey
    private Long mId;
    @SerializedName("libelle")
    private String mLibelle;
    @SerializedName("libelleFamily")
    private String mLibelleFamily;
    @SerializedName("libelleType")
    private String mLibelleType;

    public Dimension() {
    }

    protected Dimension(Parcel in) {
        this.mAbregeFamily = in.readString();
        this.mProducts = in.createTypedArrayList(Product.CREATOR);
        this.mCode = in.readString();
        this.mCodeFamily = in.readString();
        this.mCodeType = in.readString();
        this.mId = (Long) in.readValue(Long.class.getClassLoader());
        this.mLibelle = in.readString();
        this.mLibelleFamily = in.readString();
        this.mLibelleType = in.readString();
    }

    public String getAbregeFamily() {
        return mAbregeFamily;
    }

    public void setAbregeFamily(String abregeFamily) {
        mAbregeFamily = abregeFamily;
    }

    public List<Product> getArticles() {
        return mProducts;
    }

    public void setArticles(List<Product> products) {
        mProducts = products;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getCodeFamily() {
        return mCodeFamily;
    }

    public void setCodeFamily(String codeFamily) {
        mCodeFamily = codeFamily;
    }

    public String getCodeType() {
        return mCodeType;
    }

    public void setCodeType(String codeType) {
        mCodeType = codeType;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getLibelle() {
        return mLibelle;
    }

    public void setLibelle(String libelle) {
        mLibelle = libelle;
    }

    public String getLibelleFamily() {
        return mLibelleFamily;
    }

    public void setLibelleFamily(String libelleFamily) {
        mLibelleFamily = libelleFamily;
    }

    public String getLibelleType() {
        return mLibelleType;
    }

    public void setLibelleType(String libelleType) {
        mLibelleType = libelleType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mAbregeFamily);
        dest.writeTypedList(this.mProducts);
        dest.writeString(this.mCode);
        dest.writeString(this.mCodeFamily);
        dest.writeString(this.mCodeType);
        dest.writeValue(this.mId);
        dest.writeString(this.mLibelle);
        dest.writeString(this.mLibelleFamily);
        dest.writeString(this.mLibelleType);
    }
}
