package io.mintit.lafarge.model;

/**
 * Created by mint on 23/03/18.
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity
public class Product implements Parcelable {

    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NonNull
    private String id;

    @SerializedName("storeCode")
    @Expose
    private String storeCode;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("productCode")
    @Expose
    private String productCode;

    @SerializedName("colorCode")
    @Expose
    private String colorCode;

    @SerializedName("sizeCode")
    @Expose
    private String sizeCode;

    @SerializedName("eanCode")
    @Expose
    private String eanCode;

    @SerializedName("countryShortCode")
    @Expose
    private String countryShortCode;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("qty")
    @Expose
    private Integer qty;

    @SerializedName("price")
    @Expose
    private Double price;

    @SerializedName("promotion")
    @Expose
    private String promotion;

    @SerializedName("currencyCode")
    @Expose
    private String currencyCode;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("bin")
    @Expose
    private String bin;

    @SerializedName("imageProduct")
    @Expose
    private String imageProduct;

    @SerializedName("category")
    @Expose
    @Ignore
    private Object category;

    private transient boolean selected;
    private int stock;
    private transient String date;

    public Product() {
    }


    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }

    public String getEanCode() {
        return eanCode;
    }

    public void setEanCode(String eanCode) {
        this.eanCode = eanCode;
    }

    public String getCountryShortCode() {
        return countryShortCode;
    }

    public void setCountryShortCode(String countryShortCode) {
        this.countryShortCode = countryShortCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageProduct() {
        return imageProduct;
    }

    public void setImageProduct(String imageProduct) {
        this.imageProduct = imageProduct;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.storeCode);
        dest.writeString(this.description);
        dest.writeString(this.sizeCode);
        dest.writeString(this.colorCode);
        dest.writeString(this.description);
        dest.writeString(this.promotion);
        dest.writeString(this.productCode);
        dest.writeString(this.eanCode);
        dest.writeString(this.countryShortCode);
        dest.writeString(this.name);
        dest.writeString(this.currencyCode);
        dest.writeString(this.code);
        dest.writeString(this.bin);
        dest.writeString(this.imageProduct);
        dest.writeValue(this.category);
        dest.writeInt(this.qty);
        dest.writeDouble(this.price);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.stock);
        dest.writeString(this.date);
    }

    protected Product(Parcel in) {
        this.storeCode = in.readString();
        this.id = (String) in.readValue(String.class.getClassLoader());
        this.code = in.readString();
        this.bin = in.readString();
        this.imageProduct = in.readString();
        this.category = in.readParcelable(Object.class.getClassLoader());
        this.name = in.readString();
        this.productCode = in.readString();
        this.eanCode = in.readString();
        this.countryShortCode = in.readString();
        this.currencyCode = in.readString();
        this.price = (Double) in.readValue(Double.class.getClassLoader());
        this.sizeCode = in.readString();
        this.colorCode = in.readString();
        this.qty = (Integer) in.readValue(Integer.class.getClassLoader());
        this.description = in.readString();
        this.promotion = in.readString();
        this.selected = in.readByte() != 0;
        this.stock = in.readInt();
        this.date = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", storeCode='" + storeCode + '\'' +
                ", description='" + description + '\'' +
                ", productCode='" + productCode + '\'' +
                ", colorCode='" + colorCode + '\'' +
                ", sizeCode='" + sizeCode + '\'' +
                ", eanCode='" + eanCode + '\'' +
                ", countryShortCode='" + countryShortCode + '\'' +
                ", name='" + name + '\'' +
                ", qty=" + qty +
                ", price=" + price +
                ", promotion='" + promotion + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", code='" + code + '\'' +
                ", bin='" + bin + '\'' +
                ", imageProduct='" + imageProduct + '\'' +
                ", category=" + category +
                ", selected=" + selected +
                ", stock=" + stock +
                ", date='" + date + '\'' +
                '}';
    }
}
