package io.mintit.lafarge.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;


@Entity(indices = {@Index(value = {"mId"},unique = true)})
public class Tarif implements Parcelable {

    @SerializedName("articles")
    @Ignore
    private List<Article> mArticles;
    @SerializedName("codeArticle")
    private String mCodeArticle;
    @SerializedName("codeBarre")
    private String mCodeBarre;
    @SerializedName("codeFamily")
    private String mCodeFamily;
    @SerializedName("regimePrix")
    private String regimePrix;
    @SerializedName("codeTarif")
    private String mCodeTarif;
    @SerializedName("endDate")
    private String mEndDate;
    @SerializedName("etablissements")
    @Ignore
    private List<Etablissement> mEtablissements;
    @SerializedName("family")
    @Ignore
    private Family mFamily;
    @PrimaryKey
    @SerializedName("id")
    private Long mId;
    @SerializedName("price")
    private Double mPrice;
    @SerializedName("startDate")
    private String mStartDate;

    public String getmCurrencyId() {
        return mCurrencyId;
    }

    public void setmCurrencyId(String mCurrencyId) {
        this.mCurrencyId = mCurrencyId;
    }

    @SerializedName("currencyId")
    @Ignore
    private String mCurrencyId;

    public Tarif() {
    }

    public String getRegimePrix() {
        return regimePrix;
    }

    public void setRegimePrix(String regimePrix) {
        this.regimePrix = regimePrix;
    }

    public List<Article> getArticles() {
        return mArticles;
    }

    public void setArticles(List<Article> articles) {
        mArticles = articles;
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

    public String getCodeTarif() {
        return mCodeTarif;
    }

    public void setCodeTarif(String codeTarif) {
        mCodeTarif = codeTarif;
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

    public Double getPrice() {
        return mPrice;
    }

    public void setPrice(Double price) {
        mPrice = price;
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
        dest.writeString(this.mCodeArticle);
        dest.writeString(this.mCodeBarre);
        dest.writeString(this.mCodeFamily);
        dest.writeString(this.regimePrix);
        dest.writeString(this.mCodeTarif);
        dest.writeString(this.mEndDate);
        dest.writeTypedList(this.mEtablissements);
        dest.writeParcelable(this.mFamily, flags);
        dest.writeValue(this.mId);
        dest.writeValue(this.mPrice);
        dest.writeString(this.mStartDate);
        dest.writeString(this.mCurrencyId);
    }

    protected Tarif(Parcel in) {
        this.mArticles = in.createTypedArrayList(Article.CREATOR);
        this.mCodeArticle = in.readString();
        this.mCodeBarre = in.readString();
        this.mCodeFamily = in.readString();
        this.regimePrix = in.readString();
        this.mCodeTarif = in.readString();
        this.mEndDate = in.readString();
        this.mEtablissements = in.createTypedArrayList(Etablissement.CREATOR);
        this.mFamily = in.readParcelable(Family.class.getClassLoader());
        this.mId = (Long) in.readValue(Long.class.getClassLoader());
        this.mPrice = (Double) in.readValue(Double.class.getClassLoader());
        this.mStartDate = in.readString();
        this.mCurrencyId = in.readString();
    }

    public static final Creator<Tarif> CREATOR = new Creator<Tarif>() {
        @Override
        public Tarif createFromParcel(Parcel source) {
            return new Tarif(source);
        }

        @Override
        public Tarif[] newArray(int size) {
            return new Tarif[size];
        }
    };
}
