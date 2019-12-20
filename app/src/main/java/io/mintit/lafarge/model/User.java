package io.mintit.lafarge.model;

/**
 * Created by mint on 29/03/18.
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity(indices = {@Index(value = {"idUtilisateur"},unique = true)})
public class User implements Parcelable {

    public final static Parcelable.Creator<User> CREATOR = new Creator<User>() {


        @SuppressWarnings({
                "unchecked"
        })
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return (new User[size]);
        }

    };

    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("idUtilisateur")
    @Expose
    private String idUtilisateur;
    @SerializedName("libelle")
    @Expose
    private String libelle;
    @SerializedName("abrege")
    @Expose
    private String abrege;
    @SerializedName("controleur")
    @Expose
    private String controleur;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("present")
    @Expose
    private String present;
    @SerializedName("dateConnexion")
    @Expose
    private String dateConnexion;
    @SerializedName("groupe")
    @Expose
    private String groupe;
    @SerializedName("superviseur")
    @Expose
    private String superviseur;
    @SerializedName("suiviLog")
    @Expose
    private String suiviLog;
    @SerializedName("qrCouleur")
    @Expose
    private String qrCouleur;
    @SerializedName("crs")
    @Expose
    private int crs;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("etablissement")
    @Expose
    private String etablissement;
    @SerializedName("depot")
    @Expose
    private String depot;

    protected User(Parcel in) {
        this.idUtilisateur = ((String) in.readValue((String.class.getClassLoader())));
        this.libelle = ((String) in.readValue((String.class.getClassLoader())));
        this.abrege = ((String) in.readValue((String.class.getClassLoader())));
        this.controleur = ((String) in.readValue((String.class.getClassLoader())));
        this.password = ((String) in.readValue((String.class.getClassLoader())));
        this.present = ((String) in.readValue((String.class.getClassLoader())));
        this.dateConnexion = ((String) in.readValue((String.class.getClassLoader())));
        this.groupe = ((String) in.readValue((String.class.getClassLoader())));
        this.superviseur = ((String) in.readValue((String.class.getClassLoader())));
        this.suiviLog = ((String) in.readValue((String.class.getClassLoader())));
        this.qrCouleur = ((String) in.readValue((String.class.getClassLoader())));
        this.crs = ((int) in.readValue((int.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.etablissement = ((String) in.readValue((String.class.getClassLoader())));
        this.depot = ((String) in.readValue((String.class.getClassLoader())));
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(String idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getAbrege() {
        return abrege;
    }

    public void setAbrege(String abrege) {
        this.abrege = abrege;
    }

    public String getControleur() {
        return controleur;
    }

    public void setControleur(String controleur) {
        this.controleur = controleur;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getDateConnexion() {
        return dateConnexion;
    }

    public void setDateConnexion(String dateConnexion) {
        this.dateConnexion = dateConnexion;
    }

    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    public String getSuperviseur() {
        return superviseur;
    }

    public void setSuperviseur(String superviseur) {
        this.superviseur = superviseur;
    }

    public String getSuiviLog() {
        return suiviLog;
    }

    public void setSuiviLog(String suiviLog) {
        this.suiviLog = suiviLog;
    }

    public String getQrCouleur() {
        return qrCouleur;
    }

    public void setQrCouleur(String qrCouleur) {
        this.qrCouleur = qrCouleur;
    }

    public int getCrs() {
        return crs;
    }

    public void setCrs(int crs) {
        this.crs = crs;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(idUtilisateur);
        dest.writeValue(libelle);
        dest.writeValue(abrege);
        dest.writeValue(controleur);
        dest.writeValue(password);
        dest.writeValue(present);
        dest.writeValue(dateConnexion);
        dest.writeValue(groupe);
        dest.writeValue(superviseur);
        dest.writeValue(suiviLog);
        dest.writeValue(qrCouleur);
        dest.writeValue(crs);
        dest.writeValue(email);
        dest.writeValue(etablissement);
        dest.writeValue(depot);
    }

    public int describeContents() {
        return 0;
    }

}