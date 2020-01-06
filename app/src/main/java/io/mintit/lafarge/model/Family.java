package io.mintit.lafarge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class Family implements Parcelable {

    public static final Parcelable.Creator<Family> CREATOR = new Parcelable.Creator<Family>() {
        @Override
        public Family createFromParcel(Parcel source) {
            return new Family(source);
        }

        @Override
        public Family[] newArray(int size) {
            return new Family[size];
        }
    };
    @SerializedName("articles")
    private List<Product> mProducts;
    @SerializedName("categories")
    private List<Category> mCategories;
    @SerializedName("code")
    private String mCode;
    @SerializedName("codeArticle")
    private String mCodeArticle;
    @SerializedName("id")
    private Long mId;
    @SerializedName("libelle")
    private String mLibelle;
    @SerializedName("promotion")
    private Promotion mPromotion;
    @SerializedName("promotions")
    private List<Promotion> mPromotions;
    @SerializedName("tarif")
    private Tarif mTarif;
    @SerializedName("tarifs")
    private List<Tarif> mTarifs;

    public Family() {
    }

    protected Family(Parcel in) {
        this.mProducts = in.createTypedArrayList(Product.CREATOR);
        this.mCategories = in.createTypedArrayList(Category.CREATOR);
        this.mCode = in.readString();
        this.mCodeArticle = in.readString();
        this.mId = (Long) in.readValue(Long.class.getClassLoader());
        this.mLibelle = in.readString();
        this.mPromotion = in.readParcelable(Promotion.class.getClassLoader());
        this.mPromotions = new ArrayList<Promotion>();
        in.readList(this.mPromotions, Promotion.class.getClassLoader());
        this.mTarif = in.readParcelable(Tarif.class.getClassLoader());
        this.mTarifs = new ArrayList<Tarif>();
        in.readList(this.mTarifs, Tarif.class.getClassLoader());
    }

    public List<Product> getArticles() {
        return mProducts;
    }

    public void setArticles(List<Product> products) {
        mProducts = products;
    }

    public List<Category> getCategories() {
        return mCategories;
    }

    public void setCategories(List<Category> categories) {
        mCategories = categories;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getCodeArticle() {
        return mCodeArticle;
    }

    public void setCodeArticle(String codeArticle) {
        mCodeArticle = codeArticle;
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

    public Promotion getPromotion() {
        return mPromotion;
    }

    public void setPromotion(Promotion promotion) {
        mPromotion = promotion;
    }

    public List<Promotion> getPromotions() {
        return mPromotions;
    }

    public void setPromotions(List<Promotion> promotions) {
        mPromotions = promotions;
    }

    public Tarif getTarif() {
        return mTarif;
    }

    public void setTarif(Tarif tarif) {
        mTarif = tarif;
    }

    public List<Tarif> getTarifs() {
        return mTarifs;
    }

    public void setTarifs(List<Tarif> tarifs) {
        mTarifs = tarifs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.mProducts);
        dest.writeTypedList(this.mCategories);
        dest.writeString(this.mCode);
        dest.writeString(this.mCodeArticle);
        dest.writeValue(this.mId);
        dest.writeString(this.mLibelle);
        dest.writeParcelable(this.mPromotion, flags);
        dest.writeList(this.mPromotions);
        dest.writeParcelable(this.mTarif, flags);
        dest.writeList(this.mTarifs);
    }
}
