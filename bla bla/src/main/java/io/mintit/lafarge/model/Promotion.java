package io.mintit.lafarge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;


@SuppressWarnings("unused")
public class Promotion implements Parcelable {

    public static final Parcelable.Creator<Promotion> CREATOR = new Parcelable.Creator<Promotion>() {
        @Override
        public Promotion createFromParcel(Parcel source) {
            return new Promotion(source);
        }

        @Override
        public Promotion[] newArray(int size) {
            return new Promotion[size];
        }
    };
    @SerializedName("articles")
    private List<Article> mArticles;
    @SerializedName("calculRemise")
    private Long mCalculRemise;
    @SerializedName("codeArticle")
    private String mCodeArticle;
    @SerializedName("codeBarre")
    private String mCodeBarre;
    @SerializedName("codeFamily")
    private String mCodeFamily;
    @SerializedName("codePromotion")
    private String mCodePromotion;
    @SerializedName("endDate")
    private String mEndDate;
    @SerializedName("etablissements")
    private List<Etablissement> mEtablissements;
    @SerializedName("family")
    private Family mFamily;
    @SerializedName("id")
    private Long mId;
    @SerializedName("price")
    private Long mPrice;
    @SerializedName("remise")
    private Long mRemise;
    @SerializedName("startDate")
    private String mStartDate;

    public Promotion() {
    }

    protected Promotion(Parcel in) {
        this.mArticles = in.createTypedArrayList(Article.CREATOR);
        this.mCalculRemise = (Long) in.readValue(Long.class.getClassLoader());
        this.mCodeArticle = in.readString();
        this.mCodeBarre = in.readString();
        this.mCodeFamily = in.readString();
        this.mCodePromotion = in.readString();
        this.mEndDate = in.readString();
        this.mEtablissements = in.createTypedArrayList(Etablissement.CREATOR);
        this.mFamily = in.readParcelable(Family.class.getClassLoader());
        this.mId = (Long) in.readValue(Long.class.getClassLoader());
        this.mPrice = (Long) in.readValue(Long.class.getClassLoader());
        this.mRemise = (Long) in.readValue(Long.class.getClassLoader());
        this.mStartDate = in.readString();
    }

    public List<Article> getArticles() {
        return mArticles;
    }

    public void setArticles(List<Article> articles) {
        mArticles = articles;
    }

    public Long getCalculRemise() {
        return mCalculRemise;
    }

    public void setCalculRemise(Long calculRemise) {
        mCalculRemise = calculRemise;
    }

    public String getCodeArticle() {
        return mCodeArticle;
    }

    public void setCodeArticle(String codeArticle) {
        mCodeArticle = codeArticle;
    }

    public String getCodeBarre() {
        return mCodeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        mCodeBarre = codeBarre;
    }

    public String getCodeFamily() {
        return mCodeFamily;
    }

    public void setCodeFamily(String codeFamily) {
        mCodeFamily = codeFamily;
    }

    public String getCodePromotion() {
        return mCodePromotion;
    }

    public void setCodePromotion(String codePromotion) {
        mCodePromotion = codePromotion;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public void setEndDate(String endDate) {
        mEndDate = endDate;
    }

    public List<Etablissement> getEtablissements() {
        return mEtablissements;
    }

    public void setEtablissements(List<Etablissement> etablissements) {
        mEtablissements = etablissements;
    }

    public Family getFamily() {
        return mFamily;
    }

    public void setFamily(Family family) {
        mFamily = family;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Long getPrice() {
        return mPrice;
    }

    public void setPrice(Long price) {
        mPrice = price;
    }

    public Long getRemise() {
        return mRemise;
    }

    public void setRemise(Long remise) {
        mRemise = remise;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(String startDate) {
        mStartDate = startDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.mArticles);
        dest.writeValue(this.mCalculRemise);
        dest.writeString(this.mCodeArticle);
        dest.writeString(this.mCodeBarre);
        dest.writeString(this.mCodeFamily);
        dest.writeString(this.mCodePromotion);
        dest.writeString(this.mEndDate);
        dest.writeTypedList(this.mEtablissements);
        dest.writeParcelable(this.mFamily, flags);
        dest.writeValue(this.mId);
        dest.writeValue(this.mPrice);
        dest.writeValue(this.mRemise);
        dest.writeString(this.mStartDate);
    }
}
