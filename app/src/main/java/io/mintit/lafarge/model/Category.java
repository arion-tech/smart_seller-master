package io.mintit.lafarge.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.mintit.lafarge.model.typeConverters.ListCategoryConverter;


@TypeConverters({ListCategoryConverter.class})
@Entity
public class Category implements Parcelable {

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @SerializedName("code")
    private String mCode;
    @SerializedName("libelle")
    private String mLibelle;
    @SerializedName("level")
    private Long mLevel;
    @SerializedName("rank")
    private int rank;
    @SerializedName("children")
    @TypeConverters(ListCategoryConverter.class)
    private ArrayList<Category> mChildren = new ArrayList<>();
    @SerializedName("codeParent")
    private String mCodeParent;
    @SerializedName("id")
    @PrimaryKey
    private Long mId;

    @SerializedName("articles")
    @Ignore
    private List<Product> mProducts;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("families")
    @Ignore
    private List<Family> mFamilies;
    @SerializedName("parent")
    @Ignore
    private Parent mParent;
    private boolean selected;

    public Category() {
    }

    protected Category(Parcel in) {
        this.mProducts = in.createTypedArrayList(Product.CREATOR);
        this.mChildren = new ArrayList<Category>();
        in.readList(this.mChildren, Child.class.getClassLoader());
        this.mCode = in.readString();
        this.mCodeParent = in.readString();
        this.mDescription = in.readString();
        this.mFamilies = new ArrayList<Family>();
        in.readList(this.mFamilies, Family.class.getClassLoader());
        this.mId = (Long) in.readValue(Long.class.getClassLoader());
        this.mLevel = (Long) in.readValue(Long.class.getClassLoader());
        this.mLibelle = in.readString();
        this.mParent = in.readParcelable(Parent.class.getClassLoader());
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public List<Product> getArticles() {
        return mProducts;
    }

    public void setArticles(List<Product> products) {
        mProducts = products;
    }
    @TypeConverters(ListCategoryConverter.class)
    public ArrayList<Category> getChildren() {
        return mChildren;
    }
    @TypeConverters(ListCategoryConverter.class)
    public void setChildren(ArrayList<Category> children) {
        mChildren = children;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getCodeParent() {
        return mCodeParent;
    }

    public void setCodeParent(String codeParent) {
        mCodeParent = codeParent;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public List<Family> getFamilies() {
        return mFamilies;
    }

    public void setFamilies(List<Family> families) {
        mFamilies = families;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Long getLevel() {
        return mLevel;
    }

    public void setLevel(Long level) {
        mLevel = level;
    }

    public String getLibelle() {
        return mLibelle;
    }

    public void setLibelle(String libelle) {
        mLibelle = libelle;
    }

    public Parent getParent() {
        return mParent;
    }

    public void setParent(Parent parent) {
        mParent = parent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.mProducts);
        dest.writeList(this.mChildren);
        dest.writeString(this.mCode);
        dest.writeString(this.mCodeParent);
        dest.writeString(this.mDescription);
        dest.writeList(this.mFamilies);
        dest.writeValue(this.mId);
        dest.writeValue(this.mLevel);
        dest.writeString(this.mLibelle);
        dest.writeParcelable(this.mParent, flags);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
