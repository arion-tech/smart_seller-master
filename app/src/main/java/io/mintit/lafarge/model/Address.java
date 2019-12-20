package io.mintit.lafarge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address implements Parcelable
{

    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("countryId")
    @Expose
    private String countryId;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("addressLine1")
    @Expose
    private String addressLine1;
    @SerializedName("addressLine2")
    @Expose
    private String addressLine2;
    @SerializedName("addressLine3")
    @Expose
    private String addressLine3;
    @SerializedName("titleId")
    @Expose
    private String titleId;
    @SerializedName("zipCode")
    @Expose
    private String zipCode;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    public final static Creator<Address> CREATOR = new Creator<Address>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        public Address[] newArray(int size) {
            return (new Address[size]);
        }

    }
            ;

    protected Address(Parcel in) {
        this.city = ((String) in.readValue((String.class.getClassLoader())));
        this.countryId = ((String) in.readValue((String.class.getClassLoader())));
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.addressLine1 = ((String) in.readValue((String.class.getClassLoader())));
        this.addressLine2 = ((String) in.readValue((String.class.getClassLoader())));
        this.addressLine3 = ((String) in.readValue((String.class.getClassLoader())));
        this.titleId = ((String) in.readValue((String.class.getClassLoader())));
        this.zipCode = ((String) in.readValue((String.class.getClassLoader())));
        this.phoneNumber = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Address() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(city);
        dest.writeValue(countryId);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(addressLine1);
        dest.writeValue(addressLine2);
        dest.writeValue(addressLine3);
        dest.writeValue(titleId);
        dest.writeValue(zipCode);
        dest.writeValue(phoneNumber);
    }

    public int describeContents() {
        return 0;
    }

}