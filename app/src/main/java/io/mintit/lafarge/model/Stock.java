package io.mintit.lafarge.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


@Entity(indices = {@Index(value = {"mId"},unique = true)})
public class Stock {

    @SerializedName("article")
    @Ignore
    private Article mArticle;
    @SerializedName("codeArticle")
    private String mCodeArticle;
    @SerializedName("codeDepo")
    private String mCodeDepo;
    @SerializedName("codeStock")
    private String mCodeStock;
    @SerializedName("depo")
    @Ignore
    private Depo mDepo;
    @SerializedName("id")
    @PrimaryKey
    private Long mId;
    @SerializedName("quantity")
    private int mQuantity;

    public Article getArticle() {
        return mArticle;
    }

    public void setArticle(Article article) {
        mArticle = article;
    }

    public String getCodeArticle() {
        return mCodeArticle;
    }

    public void setCodeArticle(String codeArticle) {
        mCodeArticle = codeArticle;
    }

    public String getCodeDepo() {
        return mCodeDepo;
    }

    public void setCodeDepo(String codeDepo) {
        mCodeDepo = codeDepo;
    }

    public String getCodeStock() {
        return mCodeStock;
    }

    public void setCodeStock(String codeStock) {
        mCodeStock = codeStock;
    }

    public Depo getDepo() {
        return mDepo;
    }

    public void setDepo(Depo depo) {
        mDepo = depo;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

}
