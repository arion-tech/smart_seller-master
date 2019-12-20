package io.mintit.lafarge.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity
public class Supplier implements Parcelable
{
    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("tiersCode")
    @Expose
    private String tiersCode;
    @SerializedName("libelle")
    @Expose
    private String libelle;
    @SerializedName("adresse1")
    @Expose
    private String adresse1;
    @SerializedName("adresse2")
    @Expose
    private String adresse2;
    @SerializedName("adresse3")
    @Expose
    private String adresse3;
    @SerializedName("postalCode")
    @Expose
    private String postalCode;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("tvaRegime")
    @Expose
    private String tvaRegime;
    public final static Parcelable.Creator<Supplier> CREATOR = new Creator<Supplier>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Supplier createFromParcel(Parcel in) {
            return new Supplier(in);
        }

        public Supplier[] newArray(int size) {
            return (new Supplier[size]);
        }

    }
            ;
    private boolean selected;

    protected Supplier(Parcel in) {
        this.tiersCode = ((String) in.readValue((String.class.getClassLoader())));
        this.libelle = ((String) in.readValue((String.class.getClassLoader())));
        this.adresse1 = ((String) in.readValue((String.class.getClassLoader())));
        this.adresse2 = ((String) in.readValue((String.class.getClassLoader())));
        this.adresse3 = ((String) in.readValue((String.class.getClassLoader())));
        this.postalCode = ((String) in.readValue((String.class.getClassLoader())));
        this.city = ((String) in.readValue((String.class.getClassLoader())));
        this.country = ((String) in.readValue((String.class.getClassLoader())));
        this.language = ((String) in.readValue((String.class.getClassLoader())));
        this.currency = ((String) in.readValue((String.class.getClassLoader())));
        this.tvaRegime = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Supplier() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTiersCode() {
        return tiersCode;
    }

    public void setTiersCode(String tiersCode) {
        this.tiersCode = tiersCode;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getAdresse1() {
        return adresse1;
    }

    public void setAdresse1(String adresse1) {
        this.adresse1 = adresse1;
    }

    public String getAdresse2() {
        return adresse2;
    }

    public void setAdresse2(String adresse2) {
        this.adresse2 = adresse2;
    }

    public String getAdresse3() {
        return adresse3;
    }

    public void setAdresse3(String adresse3) {
        this.adresse3 = adresse3;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTvaRegime() {
        return tvaRegime;
    }

    public void setTvaRegime(String tvaRegime) {
        this.tvaRegime = tvaRegime;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(tiersCode);
        dest.writeValue(libelle);
        dest.writeValue(adresse1);
        dest.writeValue(adresse2);
        dest.writeValue(adresse3);
        dest.writeValue(postalCode);
        dest.writeValue(city);
        dest.writeValue(country);
        dest.writeValue(language);
        dest.writeValue(currency);
        dest.writeValue(tvaRegime);
    }

    public int describeContents() {
        return 0;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}