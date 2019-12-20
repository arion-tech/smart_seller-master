package io.mintit.lafarge.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(indices = {@Index(value = {"libelle", "lastName", "storeCode", "datenaissance"},unique = true)})
public class Seller implements Parcelable {

    public final static Parcelable.Creator<Seller> CREATOR = new Creator<Seller>() {

        @SuppressWarnings({
                "unchecked"
        })
        public Seller createFromParcel(Parcel in) {
            return new Seller(in);
        }

        public Seller[] newArray(int size) {
            return (new Seller[size]);
        }

    };
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("storeCode")
    @Expose
    private String storeCode;

    @SerializedName("lastName")
    @Expose
    private String lastName;

    @SerializedName("firstName")
    @Expose
    private String firstName;

    @SerializedName("libelle")
    @Expose
    private String libelle;

    @SerializedName("commercial")
    @Expose
    private String commercial;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("commission")
    @Expose
    private Float commission;

    @SerializedName("zonecom")
    @Expose
    private String zonecom;

    @SerializedName("typecom")
    @Expose
    private String typecom;

    @SerializedName("naturepieceg")
    @Expose
    private String naturepieceg;

    @SerializedName("salarie")
    @Expose
    private String salarie;

    @SerializedName("utilassocie")
    @Expose
    private String utilassocie;

    @SerializedName("prefixetiers")
    @Expose
    private String prefixetiers;

    @SerializedName("datesupp")
    @Expose
    private String datesupp;

    @SerializedName("idpavetactile")
    @Expose
    private Integer idpavetactile;

    @SerializedName("dateidpave")
    @Expose
    private String dateidpave;

    @SerializedName("vallibre1")
    @Expose
    private Integer vallibre1;

    @SerializedName("vallibre2")
    @Expose
    private Integer vallibre2;

    @SerializedName("vallibre3")
    @Expose
    private Integer vallibre3;

    @SerializedName("datelibre1")
    @Expose
    private String datelibre1;

    @SerializedName("datelibre2")
    @Expose
    private String datelibre2;

    @SerializedName("datelibre3")
    @Expose
    private String datelibre3;

    @SerializedName("datecreation")
    @Expose
    private String datecreation;

    @SerializedName("datemodif")
    @Expose
    private String datemodif;

    @SerializedName("dateintegr")
    @Expose
    private String dateintegr;

    @SerializedName("createur")
    @Expose
    private String createur;

    @SerializedName("tiers")
    @Expose
    private String tiers;

    @SerializedName("vendeur")
    @Expose
    private String vendeur;

    @SerializedName("caissier")
    @Expose
    private String caissier;

    @SerializedName("fonctioncom")
    @Expose
    private String fonctioncom;

    @SerializedName("numeross")
    @Expose
    private String numeross;

    @SerializedName("adresse1")
    @Expose
    private String adresse1;

    @SerializedName("adresse2")
    @Expose
    private String adresse2;

    @SerializedName("adresse3")
    @Expose
    private String adresse3;

    @SerializedName("codepostal")
    @Expose
    private String codepostal;

    @SerializedName("ville")
    @Expose
    private String ville;

    @SerializedName("pays")
    @Expose
    private String pays;

    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;

    @SerializedName("datenaissance")
    @Expose
    private String datenaissance;

    @SerializedName("dateentree")
    @Expose
    private String dateentree;

    @SerializedName("datesortie")
    @Expose
    private String datesortie;

    @SerializedName("horhebdo")
    @Expose
    private Integer horhebdo;

    @SerializedName("maxhorhebdo")
    @Expose
    private Integer maxhorhebdo;

    @SerializedName("tempspartiel")
    @Expose
    private String tempspartiel;

    @SerializedName("tcontrat")
    @Expose
    private String tcontrat;

    @SerializedName("listeetab")
    @Expose
    private String listeetab;

    @SerializedName("tauxmh")
    @Expose
    private Integer tauxmh;

    @SerializedName("tablibre1")
    @Expose
    private String tablibre1;

    @SerializedName("tablibre2")
    @Expose
    private String tablibre2;

    @SerializedName("tablibre3")
    @Expose
    private String tablibre3;

    @SerializedName("tablibre4")
    @Expose
    private String tablibre4;

    @SerializedName("tablibre5")
    @Expose
    private String tablibre5;

    @SerializedName("tablibre6")
    @Expose
    private String tablibre6;

    @SerializedName("tablibre7")
    @Expose
    private String tablibre7;

    @SerializedName("tablibre8")
    @Expose
    private String tablibre8;

    @SerializedName("tablibre9")
    @Expose
    private String tablibre9;

    @SerializedName("tablibrea")
    @Expose
    private String tablibrea;

    @SerializedName("textlibre1")
    @Expose
    private String textlibre1;

    @SerializedName("textlibre2")
    @Expose
    private String textlibre2;

    @SerializedName("textlibre3")
    @Expose
    private String textlibre3;

    @SerializedName("boollibre1")
    @Expose
    private String boollibre1;

    @SerializedName("boollibre2")
    @Expose
    private String boollibre2;

    @SerializedName("boollibre3")
    @Expose
    private String boollibre3;

    @SerializedName("utilisateur")
    @Expose
    private String utilisateur;

    private boolean selected;

    protected Seller(Parcel in) {
        this.storeCode = ((String) in.readValue((String.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.libelle = ((String) in.readValue((String.class.getClassLoader())));
        this.commercial = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.commission = ((float) in.readValue((float.class.getClassLoader())));
        this.zonecom = ((String) in.readValue((String.class.getClassLoader())));
        this.typecom = ((String) in.readValue((String.class.getClassLoader())));
        this.naturepieceg = ((String) in.readValue((String.class.getClassLoader())));
        this.salarie = ((String) in.readValue((String.class.getClassLoader())));
        this.utilassocie = ((String) in.readValue((String.class.getClassLoader())));
        this.prefixetiers = ((String) in.readValue((String.class.getClassLoader())));
        this.datesupp = ((String) in.readValue((String.class.getClassLoader())));
        this.idpavetactile = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.dateidpave = ((String) in.readValue((String.class.getClassLoader())));
        this.vallibre1 = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.vallibre2 = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.vallibre3 = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.datelibre1 = ((String) in.readValue((String.class.getClassLoader())));
        this.datelibre2 = ((String) in.readValue((String.class.getClassLoader())));
        this.datelibre3 = ((String) in.readValue((String.class.getClassLoader())));
        this.datecreation = ((String) in.readValue((String.class.getClassLoader())));
        this.datemodif = ((String) in.readValue((String.class.getClassLoader())));
        this.dateintegr = ((String) in.readValue((String.class.getClassLoader())));
        this.createur = ((String) in.readValue((String.class.getClassLoader())));
        this.tiers = ((String) in.readValue((String.class.getClassLoader())));
        this.vendeur = ((String) in.readValue((String.class.getClassLoader())));
        this.caissier = ((String) in.readValue((String.class.getClassLoader())));
        this.fonctioncom = ((String) in.readValue((String.class.getClassLoader())));
        this.numeross = ((String) in.readValue((String.class.getClassLoader())));
        this.adresse1 = ((String) in.readValue((String.class.getClassLoader())));
        this.adresse2 = ((String) in.readValue((String.class.getClassLoader())));
        this.adresse3 = ((String) in.readValue((String.class.getClassLoader())));
        this.codepostal = ((String) in.readValue((String.class.getClassLoader())));
        this.ville = ((String) in.readValue((String.class.getClassLoader())));
        this.pays = ((String) in.readValue((String.class.getClassLoader())));
        this.phoneNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.datenaissance = ((String) in.readValue((String.class.getClassLoader())));
        this.dateentree = ((String) in.readValue((String.class.getClassLoader())));
        this.datesortie = ((String) in.readValue((String.class.getClassLoader())));
        this.horhebdo = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.maxhorhebdo = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.tempspartiel = ((String) in.readValue((String.class.getClassLoader())));
        this.tcontrat = ((String) in.readValue((String.class.getClassLoader())));
        this.listeetab = ((String) in.readValue((String.class.getClassLoader())));
        this.tauxmh = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.tablibre1 = ((String) in.readValue((String.class.getClassLoader())));
        this.tablibre2 = ((String) in.readValue((String.class.getClassLoader())));
        this.tablibre3 = ((String) in.readValue((String.class.getClassLoader())));
        this.tablibre4 = ((String) in.readValue((String.class.getClassLoader())));
        this.tablibre5 = ((String) in.readValue((String.class.getClassLoader())));
        this.tablibre6 = ((String) in.readValue((String.class.getClassLoader())));
        this.tablibre7 = ((String) in.readValue((String.class.getClassLoader())));
        this.tablibre8 = ((String) in.readValue((String.class.getClassLoader())));
        this.tablibre9 = ((String) in.readValue((String.class.getClassLoader())));
        this.tablibrea = ((String) in.readValue((String.class.getClassLoader())));
        this.textlibre1 = ((String) in.readValue((String.class.getClassLoader())));
        this.textlibre2 = ((String) in.readValue((String.class.getClassLoader())));
        this.textlibre3 = ((String) in.readValue((String.class.getClassLoader())));
        this.boollibre1 = ((String) in.readValue((String.class.getClassLoader())));
        this.boollibre2 = ((String) in.readValue((String.class.getClassLoader())));
        this.boollibre3 = ((String) in.readValue((String.class.getClassLoader())));
        this.utilisateur = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Seller() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCommercial() {
        return commercial;
    }

    public void setCommercial(String commercial) {
        this.commercial = commercial;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public Float getCommission() {
        return commission;
    }

    public void setCommission(Float commission) {
        this.commission = commission;
    }

    public String getZonecom() {
        return zonecom;
    }

    public void setZonecom(String zonecom) {
        this.zonecom = zonecom;
    }

    public String getTypecom() {
        return typecom;
    }

    public void setTypecom(String typecom) {
        this.typecom = typecom;
    }

    public String getNaturepieceg() {
        return naturepieceg;
    }

    public void setNaturepieceg(String naturepieceg) {
        this.naturepieceg = naturepieceg;
    }

    public String getSalarie() {
        return salarie;
    }

    public void setSalarie(String salarie) {
        this.salarie = salarie;
    }

    public String getUtilassocie() {
        return utilassocie;
    }

    public void setUtilassocie(String utilassocie) {
        this.utilassocie = utilassocie;
    }

    public String getPrefixetiers() {
        return prefixetiers;
    }

    public void setPrefixetiers(String prefixetiers) {
        this.prefixetiers = prefixetiers;
    }

    public String getDatesupp() {
        return datesupp;
    }

    public void setDatesupp(String datesupp) {
        this.datesupp = datesupp;
    }

    public Integer getIdpavetactile() {
        return idpavetactile;
    }

    public void setIdpavetactile(Integer idpavetactile) {
        this.idpavetactile = idpavetactile;
    }

    public String getDateidpave() {
        return dateidpave;
    }

    public void setDateidpave(String dateidpave) {
        this.dateidpave = dateidpave;
    }

    public Integer getVallibre1() {
        return vallibre1;
    }

    public void setVallibre1(Integer vallibre1) {
        this.vallibre1 = vallibre1;
    }

    public Integer getVallibre2() {
        return vallibre2;
    }

    public void setVallibre2(Integer vallibre2) {
        this.vallibre2 = vallibre2;
    }

    public Integer getVallibre3() {
        return vallibre3;
    }

    public void setVallibre3(Integer vallibre3) {
        this.vallibre3 = vallibre3;
    }

    public String getDatelibre1() {
        return datelibre1;
    }

    public void setDatelibre1(String datelibre1) {
        this.datelibre1 = datelibre1;
    }

    public String getDatelibre2() {
        return datelibre2;
    }

    public void setDatelibre2(String datelibre2) {
        this.datelibre2 = datelibre2;
    }

    public String getDatelibre3() {
        return datelibre3;
    }

    public void setDatelibre3(String datelibre3) {
        this.datelibre3 = datelibre3;
    }

    public String getDatecreation() {
        return datecreation;
    }

    public void setDatecreation(String datecreation) {
        this.datecreation = datecreation;
    }

    public String getDatemodif() {
        return datemodif;
    }

    public void setDatemodif(String datemodif) {
        this.datemodif = datemodif;
    }

    public String getDateintegr() {
        return dateintegr;
    }

    public void setDateintegr(String dateintegr) {
        this.dateintegr = dateintegr;
    }

    public String getCreateur() {
        return createur;
    }

    public void setCreateur(String createur) {
        this.createur = createur;
    }

    public String getTiers() {
        return tiers;
    }

    public void setTiers(String tiers) {
        this.tiers = tiers;
    }

    public String getVendeur() {
        return vendeur;
    }

    public void setVendeur(String vendeur) {
        this.vendeur = vendeur;
    }

    public String getCaissier() {
        return caissier;
    }

    public void setCaissier(String caissier) {
        this.caissier = caissier;
    }

    public String getFonctioncom() {
        return fonctioncom;
    }

    public void setFonctioncom(String fonctioncom) {
        this.fonctioncom = fonctioncom;
    }

    public String getNumeross() {
        return numeross;
    }

    public void setNumeross(String numeross) {
        this.numeross = numeross;
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

    public String getCodepostal() {
        return codepostal;
    }

    public void setCodepostal(String codepostal) {
        this.codepostal = codepostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDatenaissance() {
        return datenaissance;
    }

    public void setDatenaissance(String datenaissance) {
        this.datenaissance = datenaissance;
    }

    public String getDateentree() {
        return dateentree;
    }

    public void setDateentree(String dateentree) {
        this.dateentree = dateentree;
    }

    public String getDatesortie() {
        return datesortie;
    }

    public void setDatesortie(String datesortie) {
        this.datesortie = datesortie;
    }

    public Integer getHorhebdo() {
        return horhebdo;
    }

    public void setHorhebdo(Integer horhebdo) {
        this.horhebdo = horhebdo;
    }

    public Integer getMaxhorhebdo() {
        return maxhorhebdo;
    }

    public void setMaxhorhebdo(Integer maxhorhebdo) {
        this.maxhorhebdo = maxhorhebdo;
    }

    public String getTempspartiel() {
        return tempspartiel;
    }

    public void setTempspartiel(String tempspartiel) {
        this.tempspartiel = tempspartiel;
    }

    public String getTcontrat() {
        return tcontrat;
    }

    public void setTcontrat(String tcontrat) {
        this.tcontrat = tcontrat;
    }

    public String getListeetab() {
        return listeetab;
    }

    public void setListeetab(String listeetab) {
        this.listeetab = listeetab;
    }

    public Integer getTauxmh() {
        return tauxmh;
    }

    public void setTauxmh(Integer tauxmh) {
        this.tauxmh = tauxmh;
    }

    public String getTablibre1() {
        return tablibre1;
    }

    public void setTablibre1(String tablibre1) {
        this.tablibre1 = tablibre1;
    }

    public String getTablibre2() {
        return tablibre2;
    }

    public void setTablibre2(String tablibre2) {
        this.tablibre2 = tablibre2;
    }

    public String getTablibre3() {
        return tablibre3;
    }

    public void setTablibre3(String tablibre3) {
        this.tablibre3 = tablibre3;
    }

    public String getTablibre4() {
        return tablibre4;
    }

    public void setTablibre4(String tablibre4) {
        this.tablibre4 = tablibre4;
    }

    public String getTablibre5() {
        return tablibre5;
    }

    public void setTablibre5(String tablibre5) {
        this.tablibre5 = tablibre5;
    }

    public String getTablibre6() {
        return tablibre6;
    }

    public void setTablibre6(String tablibre6) {
        this.tablibre6 = tablibre6;
    }

    public String getTablibre7() {
        return tablibre7;
    }

    public void setTablibre7(String tablibre7) {
        this.tablibre7 = tablibre7;
    }

    public String getTablibre8() {
        return tablibre8;
    }

    public void setTablibre8(String tablibre8) {
        this.tablibre8 = tablibre8;
    }

    public String getTablibre9() {
        return tablibre9;
    }

    public void setTablibre9(String tablibre9) {
        this.tablibre9 = tablibre9;
    }

    public String getTablibrea() {
        return tablibrea;
    }

    public void setTablibrea(String tablibrea) {
        this.tablibrea = tablibrea;
    }

    public String getTextlibre1() {
        return textlibre1;
    }

    public void setTextlibre1(String textlibre1) {
        this.textlibre1 = textlibre1;
    }

    public String getTextlibre2() {
        return textlibre2;
    }

    public void setTextlibre2(String textlibre2) {
        this.textlibre2 = textlibre2;
    }

    public String getTextlibre3() {
        return textlibre3;
    }

    public void setTextlibre3(String textlibre3) {
        this.textlibre3 = textlibre3;
    }

    public String getBoollibre1() {
        return boollibre1;
    }

    public void setBoollibre1(String boollibre1) {
        this.boollibre1 = boollibre1;
    }

    public String getBoollibre2() {
        return boollibre2;
    }

    public void setBoollibre2(String boollibre2) {
        this.boollibre2 = boollibre2;
    }

    public String getBoollibre3() {
        return boollibre3;
    }

    public void setBoollibre3(String boollibre3) {
        this.boollibre3 = boollibre3;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(storeCode);
        dest.writeValue(lastName);
        dest.writeValue(firstName);
        dest.writeValue(commercial);
        dest.writeValue(libelle);
        dest.writeValue(email);
        dest.writeValue(commission);
        dest.writeValue(zonecom);
        dest.writeValue(typecom);
        dest.writeValue(naturepieceg);
        dest.writeValue(salarie);
        dest.writeValue(utilassocie);
        dest.writeValue(prefixetiers);
        dest.writeValue(datesupp);
        dest.writeValue(idpavetactile);
        dest.writeValue(dateidpave);
        dest.writeValue(vallibre1);
        dest.writeValue(vallibre2);
        dest.writeValue(vallibre3);
        dest.writeValue(datelibre1);
        dest.writeValue(datelibre2);
        dest.writeValue(datelibre3);
        dest.writeValue(datecreation);
        dest.writeValue(datemodif);
        dest.writeValue(dateintegr);
        dest.writeValue(createur);
        dest.writeValue(tiers);
        dest.writeValue(vendeur);
        dest.writeValue(caissier);
        dest.writeValue(fonctioncom);
        dest.writeValue(numeross);
        dest.writeValue(adresse1);
        dest.writeValue(adresse2);
        dest.writeValue(adresse3);
        dest.writeValue(codepostal);
        dest.writeValue(ville);
        dest.writeValue(pays);
        dest.writeValue(phoneNumber);
        dest.writeValue(datenaissance);
        dest.writeValue(dateentree);
        dest.writeValue(datesortie);
        dest.writeValue(horhebdo);
        dest.writeValue(maxhorhebdo);
        dest.writeValue(tempspartiel);
        dest.writeValue(tcontrat);
        dest.writeValue(listeetab);
        dest.writeValue(tauxmh);
        dest.writeValue(tablibre1);
        dest.writeValue(tablibre2);
        dest.writeValue(tablibre3);
        dest.writeValue(tablibre4);
        dest.writeValue(tablibre5);
        dest.writeValue(tablibre6);
        dest.writeValue(tablibre7);
        dest.writeValue(tablibre8);
        dest.writeValue(tablibre9);
        dest.writeValue(tablibrea);
        dest.writeValue(textlibre1);
        dest.writeValue(textlibre2);
        dest.writeValue(textlibre3);
        dest.writeValue(boollibre1);
        dest.writeValue(boollibre2);
        dest.writeValue(boollibre3);
        dest.writeValue(utilisateur);
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

    @Override
    public String toString() {
        return "Seller{" +
                "id=" + id +
                ", storeCode='" + storeCode + '\'' +
                ", lastname='" + lastName + '\'' +
                ", firstname='" + firstName + '\'' +
                ", libelle='" + libelle + '\'' +
                ", commercial='" + commercial + '\'' +
                ", email='" + email + '\'' +
                ", commission=" + commission +
                ", zonecom='" + zonecom + '\'' +
                ", typecom='" + typecom + '\'' +
                ", naturepieceg='" + naturepieceg + '\'' +
                ", salarie='" + salarie + '\'' +
                ", utilassocie='" + utilassocie + '\'' +
                ", prefixetiers='" + prefixetiers + '\'' +
                ", datesupp='" + datesupp + '\'' +
                ", idpavetactile=" + idpavetactile +
                ", dateidpave='" + dateidpave + '\'' +
                ", vallibre1=" + vallibre1 +
                ", vallibre2=" + vallibre2 +
                ", vallibre3=" + vallibre3 +
                ", datelibre1='" + datelibre1 + '\'' +
                ", datelibre2='" + datelibre2 + '\'' +
                ", datelibre3='" + datelibre3 + '\'' +
                ", datecreation='" + datecreation + '\'' +
                ", datemodif='" + datemodif + '\'' +
                ", dateintegr='" + dateintegr + '\'' +
                ", createur='" + createur + '\'' +
                ", tiers='" + tiers + '\'' +
                ", vendeur='" + vendeur + '\'' +
                ", caissier='" + caissier + '\'' +
                ", fonctioncom='" + fonctioncom + '\'' +
                ", numeross='" + numeross + '\'' +
                ", adresse1='" + adresse1 + '\'' +
                ", adresse2='" + adresse2 + '\'' +
                ", adresse3='" + adresse3 + '\'' +
                ", codepostal='" + codepostal + '\'' +
                ", ville='" + ville + '\'' +
                ", pays='" + pays + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", datenaissance='" + datenaissance + '\'' +
                ", dateentree='" + dateentree + '\'' +
                ", datesortie='" + datesortie + '\'' +
                ", horhebdo=" + horhebdo +
                ", maxhorhebdo=" + maxhorhebdo +
                ", tempspartiel='" + tempspartiel + '\'' +
                ", tcontrat='" + tcontrat + '\'' +
                ", listeetab='" + listeetab + '\'' +
                ", tauxmh=" + tauxmh +
                ", tablibre1='" + tablibre1 + '\'' +
                ", tablibre2='" + tablibre2 + '\'' +
                ", tablibre3='" + tablibre3 + '\'' +
                ", tablibre4='" + tablibre4 + '\'' +
                ", tablibre5='" + tablibre5 + '\'' +
                ", tablibre6='" + tablibre6 + '\'' +
                ", tablibre7='" + tablibre7 + '\'' +
                ", tablibre8='" + tablibre8 + '\'' +
                ", tablibre9='" + tablibre9 + '\'' +
                ", tablibrea='" + tablibrea + '\'' +
                ", textlibre1='" + textlibre1 + '\'' +
                ", textlibre2='" + textlibre2 + '\'' +
                ", textlibre3='" + textlibre3 + '\'' +
                ", boollibre1='" + boollibre1 + '\'' +
                ", boollibre2='" + boollibre2 + '\'' +
                ", boollibre3='" + boollibre3 + '\'' +
                ", utilisateur='" + utilisateur + '\'' +
                ", selected=" + selected +
                '}';
    }
}