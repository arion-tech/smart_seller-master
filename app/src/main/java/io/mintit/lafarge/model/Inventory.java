
package io.mintit.lafarge.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
@Entity(indices = {@Index(value = {"codeliste"},unique = true)})
public class Inventory implements Parcelable {
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    @SerializedName("codeliste")
    @Expose
    private String codeliste;
    @SerializedName("libelle")
    @Expose
    private String libelle;
    @SerializedName("etablissement")
    @Expose
    private String etablissement;
    @SerializedName("depot")
    @Expose
    private String depot;
    @SerializedName("dateInventaire")
    @Expose
    private String dateInventaire;
    @SerializedName("creatorSalerCode")
    @Expose
    private String creatorSalerCode;
    @SerializedName("validatorSalerCode")
    @Expose
    private String validatorSalerCode;
    @SerializedName("dateCreation")
    @Expose
    private String dateCreation;
    @SerializedName("dateModif")
    @Expose
    private String dateModif;
    @SerializedName("creatorUser")
    @Expose
    private String creatorUser;
    @SerializedName("updatorUser")
    @Expose
    private String updatorUser;
    @SerializedName("isValidated")
    @Expose
    private Boolean isValidated = false;
    @SerializedName("nature")
    @Expose
    private String nature;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("stockPhysique")
    @Expose
    private String stockPhysique;
    @SerializedName("stockTheorique")
    @Expose
    private String stockTheorique;
    @SerializedName("totalDiffrence")
    @Expose
    private String totalDiffrence;
    @SerializedName("numberOfDifferentLines")
    @Expose
    private String numberOfDifferentLines;
    @SerializedName("souche")
    @Expose
    private String souche;
    @SerializedName("depotsList")
    @Expose
    private String depotsList;
    @SerializedName("validationDate")
    @Expose
    private String validationDate;
    @SerializedName("negativePieceNumber")
    @Expose
    private String negativePieceNumber;
    @SerializedName("transactions")
    @Expose
    private String transactions;
    @Ignore
    private ArrayList<InventoryArticle> products=new ArrayList<>();

    public Inventory() {
    }

    public String getCodeliste() {
        return codeliste;
    }

    public void setCodeliste(String codeliste) {
        this.codeliste = codeliste;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(String etablissement) {
        this.etablissement = etablissement;
    }

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }

    public String getDateInventaire() {
        return dateInventaire;
    }

    public void setDateInventaire(String dateInventaire) {
        this.dateInventaire = dateInventaire;
    }

    public String getCreatorSalerCode() {
        return creatorSalerCode;
    }

    public void setCreatorSalerCode(String creatorSalerCode) {
        this.creatorSalerCode = creatorSalerCode;
    }

    public String getValidatorSalerCode() {
        return validatorSalerCode;
    }

    public void setValidatorSalerCode(String validatorSalerCode) {
        this.validatorSalerCode = validatorSalerCode;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getDateModif() {
        return dateModif;
    }

    public void setDateModif(String dateModif) {
        this.dateModif = dateModif;
    }

    public String getCreatorUser() {
        return creatorUser;
    }

    public void setCreatorUser(String creatorUser) {
        this.creatorUser = creatorUser;
    }

    public String getUpdatorUser() {
        return updatorUser;
    }

    public void setUpdatorUser(String updatorUser) {
        this.updatorUser = updatorUser;
    }

    public Boolean getIsValidated() {
        return isValidated;
    }

    public void setIsValidated(Boolean isValidated) {
        this.isValidated = isValidated;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStockPhysique() {
        return stockPhysique;
    }

    public void setStockPhysique(String stockPhysique) {
        this.stockPhysique = stockPhysique;
    }

    public String getStockTheorique() {
        return stockTheorique;
    }

    public void setStockTheorique(String stockTheorique) {
        this.stockTheorique = stockTheorique;
    }

    public String getTotalDiffrence() {
        return totalDiffrence;
    }

    public void setTotalDiffrence(String totalDiffrence) {
        this.totalDiffrence = totalDiffrence;
    }

    public String getNumberOfDifferentLines() {
        return numberOfDifferentLines;
    }

    public void setNumberOfDifferentLines(String numberOfDifferentLines) {
        this.numberOfDifferentLines = numberOfDifferentLines;
    }

    public String getSouche() {
        return souche;
    }

    public void setSouche(String souche) {
        this.souche = souche;
    }

    public String getDepotsList() {
        return depotsList;
    }

    public void setDepotsList(String depotsList) {
        this.depotsList = depotsList;
    }

    public String getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(String validationDate) {
        this.validationDate = validationDate;
    }

    public String getNegativePieceNumber() {
        return negativePieceNumber;
    }

    public void setNegativePieceNumber(String negativePieceNumber) {
        this.negativePieceNumber = negativePieceNumber;
    }

    public String getTransactions() {
        return transactions;
    }

    public void setTransactions(String transactions) {
        this.transactions = transactions;
    }

    @NonNull
    public int getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.codeliste);
        dest.writeString(this.libelle);
        dest.writeString(this.etablissement);
        dest.writeString(this.depot);
        dest.writeString(this.dateInventaire);
        dest.writeString(this.creatorSalerCode);
        dest.writeString(this.validatorSalerCode);
        dest.writeString(this.dateCreation);
        dest.writeString(this.dateModif);
        dest.writeString(this.creatorUser);
        dest.writeString(this.updatorUser);
        dest.writeValue(this.isValidated);
        dest.writeString(this.nature);
        dest.writeString(this.number);
        dest.writeString(this.currency);
        dest.writeString(this.stockPhysique);
        dest.writeString(this.stockTheorique);
        dest.writeString(this.totalDiffrence);
        dest.writeString(this.numberOfDifferentLines);
        dest.writeString(this.souche);
        dest.writeString(this.depotsList);
        dest.writeString(this.validationDate);
        dest.writeString(this.negativePieceNumber);
        dest.writeString(this.transactions);
    }

    protected Inventory(Parcel in) {
        this.codeliste = in.readString();
        this.libelle = in.readString();
        this.etablissement = in.readString();
        this.depot = in.readString();
        this.dateInventaire = in.readString();
        this.creatorSalerCode = in.readString();
        this.validatorSalerCode = in.readString();
        this.dateCreation = in.readString();
        this.dateModif = in.readString();
        this.creatorUser = in.readString();
        this.updatorUser = in.readString();
        this.isValidated = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.nature = in.readString();
        this.number = in.readString();
        this.currency = in.readString();
        this.stockPhysique = in.readString();
        this.stockTheorique = in.readString();
        this.totalDiffrence = in.readString();
        this.numberOfDifferentLines = in.readString();
        this.souche = in.readString();
        this.depotsList = in.readString();
        this.validationDate = in.readString();
        this.negativePieceNumber = in.readString();
        this.transactions = in.readString();
    }

    public static final Creator<Inventory> CREATOR = new Creator<Inventory>() {
        @Override
        public Inventory createFromParcel(Parcel source) {
            return new Inventory(source);
        }

        @Override
        public Inventory[] newArray(int size) {
            return new Inventory[size];
        }
    };

    public void setProducts(ArrayList<InventoryArticle> products) {
        this.products = products;
    }

    public ArrayList<InventoryArticle> getProducts() {
        return products;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }
}