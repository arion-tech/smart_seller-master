package io.mintit.lafarge.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class Commande implements Parcelable {

    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("basket")
    @Expose
    private Object basket;
    @SerializedName("basket_Id")
    @Expose
    private Integer basketId;
    @SerializedName("article_Id")
    @Expose
    private String articleId;
    @SerializedName("origin")
    @Expose
    private String origin;
    @SerializedName("taxExcludedNetUnitPrice")
    @Expose
    private Integer taxExcludedNetUnitPrice;
    @SerializedName("taxExcludedUnitPrice")
    @Expose
    private Integer taxExcludedUnitPrice;
    @SerializedName("taxIncludedNetUnitPrice")
    @Expose
    private Integer taxIncludedNetUnitPrice;
    @SerializedName("taxIncludedUnitPrice")
    @Expose
    private Integer taxIncludedUnitPrice;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("article")
    @Expose
    private Article article;

    public Commande() {
    }


    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Object getBasket() {
        return basket;
    }

    public void setBasket(Object basket) {
        this.basket = basket;
    }

    public Integer getBasketId() {
        return basketId;
    }

    public void setBasketId(Integer basketId) {
        this.basketId = basketId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Integer getTaxExcludedNetUnitPrice() {
        return taxExcludedNetUnitPrice;
    }

    public void setTaxExcludedNetUnitPrice(Integer taxExcludedNetUnitPrice) {
        this.taxExcludedNetUnitPrice = taxExcludedNetUnitPrice;
    }

    public Integer getTaxExcludedUnitPrice() {
        return taxExcludedUnitPrice;
    }

    public void setTaxExcludedUnitPrice(Integer taxExcludedUnitPrice) {
        this.taxExcludedUnitPrice = taxExcludedUnitPrice;
    }

    public Integer getTaxIncludedNetUnitPrice() {
        return taxIncludedNetUnitPrice;
    }

    public void setTaxIncludedNetUnitPrice(Integer taxIncludedNetUnitPrice) {
        this.taxIncludedNetUnitPrice = taxIncludedNetUnitPrice;
    }

    public Integer getTaxIncludedUnitPrice() {
        return taxIncludedUnitPrice;
    }

    public void setTaxIncludedUnitPrice(Integer taxIncludedUnitPrice) {
        this.taxIncludedUnitPrice = taxIncludedUnitPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.reference);
        dest.writeValue(this.quantity);
        dest.writeValue(this.basketId);
        dest.writeValue(this.articleId);
        dest.writeString(this.origin);
        dest.writeValue(this.taxExcludedNetUnitPrice);
        dest.writeValue(this.taxExcludedUnitPrice);
        dest.writeValue(this.taxIncludedNetUnitPrice);
        dest.writeValue(this.taxIncludedUnitPrice);
        dest.writeValue(this.id);
    }

    protected Commande(Parcel in) {
        this.reference = in.readString();
        this.quantity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.basket = in.readParcelable(Object.class.getClassLoader());
        this.basketId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.articleId = (String) in.readValue(String.class.getClassLoader());
        this.origin = in.readString();
        this.taxExcludedNetUnitPrice = (Integer) in.readValue(Integer.class.getClassLoader());
        this.taxExcludedUnitPrice = (Integer) in.readValue(Integer.class.getClassLoader());
        this.taxIncludedNetUnitPrice = (Integer) in.readValue(Integer.class.getClassLoader());
        this.taxIncludedUnitPrice = (Integer) in.readValue(Integer.class.getClassLoader());
        this.id = (String) in.readValue(String.class.getClassLoader());
    }

    public static final Creator<Commande> CREATOR = new Creator<Commande>() {
        @Override
        public Commande createFromParcel(Parcel source) {
            return new Commande(source);
        }

        @Override
        public Commande[] newArray(int size) {
            return new Commande[size];
        }
    };
}