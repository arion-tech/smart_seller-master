
package io.mintit.lafarge.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity
public class InventoryArticle extends Product implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int idInventoryArticle;
    @SerializedName("codeliste")
    @Expose
    private String codeliste;
    @SerializedName("article")
    @Expose
    private String article;
    @SerializedName("lot")
    @Expose
    private String lot;
    @SerializedName("depot")
    @Expose
    private String depot;
    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;
    @SerializedName("inventoriedArticle")
    @Expose
    private String inventoriedArticle;
    @SerializedName("stockPhysique")
    @Expose
    private String stockPhysique;
    @SerializedName("stockTheorique")
    @Expose
    private String stockTheorique;
    @SerializedName("lastDepotPurchasePrice")
    @Expose
    private String lastDepotPurchasePrice;
    @SerializedName("averagePurchasePrice")
    @Expose
    private String averagePurchasePrice;
    @SerializedName("lastDepotCostPrice")
    @Expose
    private String lastDepotCostPrice;
    @SerializedName("averageDepotCostPrice")
    @Expose
    private String averageDepotCostPrice;
    @SerializedName("lastItemPurchasePrice")
    @Expose
    private String lastItemPurchasePrice;
    @SerializedName("averageItemPurchasePrice")
    @Expose
    private String averageItemPurchasePrice;
    @SerializedName("lastItemCostPrice")
    @Expose
    private String lastItemCostPrice;
    @SerializedName("averageItemCostPrice")
    @Expose
    private String averageItemCostPrice;
    @SerializedName("dateModif")
    @Expose
    private String dateModif;
    @SerializedName("tarifPurchasePrice")
    @Expose
    private String tarifPurchasePrice;
    @SerializedName("dutyFreeItemPurchasePrice")
    @Expose
    private String dutyFreeItemPurchasePrice;
    @SerializedName("dutyFreeItemCostPrice")
    @Expose
    private String dutyFreeItemCostPrice;
    @SerializedName("cumulativeStockTransferAnnouncement")
    @Expose
    private String cumulativeStockTransferAnnouncement;
    public final static Creator<InventoryArticle> CREATOR = new Creator<InventoryArticle>() {


        @SuppressWarnings({
                "unchecked"
        })
        public InventoryArticle createFromParcel(Parcel in) {
            return new InventoryArticle(in);
        }

        public InventoryArticle[] newArray(int size) {
            return (new InventoryArticle[size]);
        }

    };

    protected InventoryArticle(Parcel in) {
        this.codeliste = ((String) in.readValue((String.class.getClassLoader())));
        this.article = ((String) in.readValue((String.class.getClassLoader())));
        this.lot = ((String) in.readValue((String.class.getClassLoader())));
        this.depot = ((String) in.readValue((String.class.getClassLoader())));
        this.serialNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.inventoriedArticle = ((String) in.readValue((String.class.getClassLoader())));
        this.stockPhysique = ((String) in.readValue((String.class.getClassLoader())));
        this.stockTheorique = ((String) in.readValue((String.class.getClassLoader())));
        this.lastDepotPurchasePrice = ((String) in.readValue((String.class.getClassLoader())));
        this.averagePurchasePrice = ((String) in.readValue((String.class.getClassLoader())));
        this.lastDepotCostPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.averageDepotCostPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.lastItemPurchasePrice = ((String) in.readValue((String.class.getClassLoader())));
        this.averageItemPurchasePrice = ((String) in.readValue((String.class.getClassLoader())));
        this.lastItemCostPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.averageItemCostPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.dateModif = ((String) in.readValue((String.class.getClassLoader())));
        this.tarifPurchasePrice = ((String) in.readValue((String.class.getClassLoader())));
        this.dutyFreeItemPurchasePrice = ((String) in.readValue((String.class.getClassLoader())));
        this.dutyFreeItemCostPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.cumulativeStockTransferAnnouncement = ((String) in.readValue((String.class.getClassLoader())));
    }

    public InventoryArticle() {
    }


    public int getIdInventoryArticle() {
        return idInventoryArticle;
    }

    public void setIdInventoryArticle(int idInventoryArticle) {
        this.idInventoryArticle = idInventoryArticle;
    }

    public String getCodeliste() {
        return codeliste;
    }

    public void setCodeliste(String codeliste) {
        this.codeliste = codeliste;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getInventoriedArticle() {
        return inventoriedArticle;
    }

    public void setInventoriedArticle(String inventoriedArticle) {
        this.inventoriedArticle = inventoriedArticle;
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

    public String getLastDepotPurchasePrice() {
        return lastDepotPurchasePrice;
    }

    public void setLastDepotPurchasePrice(String lastDepotPurchasePrice) {
        this.lastDepotPurchasePrice = lastDepotPurchasePrice;
    }

    public String getAveragePurchasePrice() {
        return averagePurchasePrice;
    }

    public void setAveragePurchasePrice(String averagePurchasePrice) {
        this.averagePurchasePrice = averagePurchasePrice;
    }

    public String getLastDepotCostPrice() {
        return lastDepotCostPrice;
    }

    public void setLastDepotCostPrice(String lastDepotCostPrice) {
        this.lastDepotCostPrice = lastDepotCostPrice;
    }

    public String getAverageDepotCostPrice() {
        return averageDepotCostPrice;
    }

    public void setAverageDepotCostPrice(String averageDepotCostPrice) {
        this.averageDepotCostPrice = averageDepotCostPrice;
    }

    public String getLastItemPurchasePrice() {
        return lastItemPurchasePrice;
    }

    public void setLastItemPurchasePrice(String lastItemPurchasePrice) {
        this.lastItemPurchasePrice = lastItemPurchasePrice;
    }

    public String getAverageItemPurchasePrice() {
        return averageItemPurchasePrice;
    }

    public void setAverageItemPurchasePrice(String averageItemPurchasePrice) {
        this.averageItemPurchasePrice = averageItemPurchasePrice;
    }

    public String getLastItemCostPrice() {
        return lastItemCostPrice;
    }

    public void setLastItemCostPrice(String lastItemCostPrice) {
        this.lastItemCostPrice = lastItemCostPrice;
    }

    public String getAverageItemCostPrice() {
        return averageItemCostPrice;
    }

    public void setAverageItemCostPrice(String averageItemCostPrice) {
        this.averageItemCostPrice = averageItemCostPrice;
    }

    public String getDateModif() {
        return dateModif;
    }

    public void setDateModif(String dateModif) {
        this.dateModif = dateModif;
    }

    public String getTarifPurchasePrice() {
        return tarifPurchasePrice;
    }

    public void setTarifPurchasePrice(String tarifPurchasePrice) {
        this.tarifPurchasePrice = tarifPurchasePrice;
    }

    public String getDutyFreeItemPurchasePrice() {
        return dutyFreeItemPurchasePrice;
    }

    public void setDutyFreeItemPurchasePrice(String dutyFreeItemPurchasePrice) {
        this.dutyFreeItemPurchasePrice = dutyFreeItemPurchasePrice;
    }

    public String getDutyFreeItemCostPrice() {
        return dutyFreeItemCostPrice;
    }

    public void setDutyFreeItemCostPrice(String dutyFreeItemCostPrice) {
        this.dutyFreeItemCostPrice = dutyFreeItemCostPrice;
    }

    public String getCumulativeStockTransferAnnouncement() {
        return cumulativeStockTransferAnnouncement;
    }

    public void setCumulativeStockTransferAnnouncement(String cumulativeStockTransferAnnouncement) {
        this.cumulativeStockTransferAnnouncement = cumulativeStockTransferAnnouncement;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(codeliste);
        dest.writeValue(article);
        dest.writeValue(lot);
        dest.writeValue(depot);
        dest.writeValue(serialNumber);
        dest.writeValue(inventoriedArticle);
        dest.writeValue(stockPhysique);
        dest.writeValue(stockTheorique);
        dest.writeValue(lastDepotPurchasePrice);
        dest.writeValue(averagePurchasePrice);
        dest.writeValue(lastDepotCostPrice);
        dest.writeValue(averageDepotCostPrice);
        dest.writeValue(lastItemPurchasePrice);
        dest.writeValue(averageItemPurchasePrice);
        dest.writeValue(lastItemCostPrice);
        dest.writeValue(averageItemCostPrice);
        dest.writeValue(dateModif);
        dest.writeValue(tarifPurchasePrice);
        dest.writeValue(dutyFreeItemPurchasePrice);
        dest.writeValue(dutyFreeItemCostPrice);
        dest.writeValue(cumulativeStockTransferAnnouncement);
    }

    public int describeContents() {
        return 0;
    }

}
