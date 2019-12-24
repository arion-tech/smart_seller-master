package io.mintit.lafarge.model;

/**
 * Created by mint on 26/03/18.
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity
public class Customer implements Parcelable {
    public final static Parcelable.Creator<Customer> CREATOR = new Creator<Customer>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        public Customer[] newArray(int size) {
            return (new Customer[size]);
        }

    };
    @SerializedName("id")
    @Expose
    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("customerId")
    @Expose
    private String customerId;
    @SerializedName("addressLine1")
    @Expose
    private String addressLine1;
    @SerializedName("addressLine2")
    @Expose
    private String addressLine2;
    @SerializedName("addressLine3")
    @Expose
    private String addressLine3;
    @SerializedName("brandName")
    @Expose
    @Ignore
    private Object brandName;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("countryId")
    @Expose
    private String countryId;
    @SerializedName("zipCode")
    @Expose
    private String zipCode;
    @SerializedName("alternateFirstName")
    @Expose
    private String alternateFirstName;
    @SerializedName("alternateLastName")
    @Expose
    private String alternateLastName;
    @SerializedName("alternateEmail")
    @Expose
    @Ignore
    private Object alternateEmail;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("emailingAccepted")
    @Expose
    private Boolean emailingAccepted;
    @SerializedName("sendReceiptByMail")
    @Expose
    @Ignore
    private Object sendReceiptByMail;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("isCompany")
    @Expose
    private Boolean isCompany = false;
    @SerializedName("isWalkThrough")
    @Expose
    private Boolean isWalkThrough = false;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("alternatePhoneNumber")
    @Expose
    private String alternatePhoneNumber;
    @SerializedName("cellularPhoneNumber")
    @Expose
    private String cellularPhoneNumber;
    @SerializedName("homePhoneNumber")
    @Expose
    private String homePhoneNumber;
    @SerializedName("officePhoneNumber")
    @Expose
    private String officePhoneNumber;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("userField")
    @Expose
    @Ignore
    private Object userField;
    @SerializedName("usualStoreId")
    @Expose
    private String usualStoreId;
    @SerializedName("activityCode")
    @Expose
    @Ignore
    private Object activityCode;
    @SerializedName("barCode")
    @Expose
    private String barCode;
    @SerializedName("birthDateDay")
    @Expose
    private Integer birthDateDay;
    @SerializedName("birthDateMonth")
    @Expose
    private Integer birthDateMonth;
    @SerializedName("birthDateYear")
    @Expose
    private Integer birthDateYear;
    @SerializedName("companyIdNumber")
    @Expose
    private String companyIdNumber;
    @SerializedName("companyTitleId")
    @Expose
    @Ignore
    private Object companyTitleId;
    @SerializedName("currencyId")
    @Expose
    private String currencyId;
    @SerializedName("fiscallId")
    @Expose
    private String fiscallId;
    @SerializedName("languageId")
    @Expose
    private String languageId;
    @SerializedName("nationalityId")
    @Expose
    private String nationalityId;
    @SerializedName("passportNumber")
    @Expose
    private String passportNumber;
    @SerializedName("sex")
    @Expose
    private String sex;
    @SerializedName("creationStoreId")
    @Expose
    @Ignore
    private Object creationStoreId;
    @SerializedName("internetPassword")
    @Expose
    private String internetPassword;
    private transient boolean selected;
    private String reference;

    protected Customer(Parcel in) {
        this.customerId = ((String) in.readValue((String.class.getClassLoader())));
        this.addressLine1 = ((String) in.readValue((String.class.getClassLoader())));
        this.addressLine2 = ((String) in.readValue((String.class.getClassLoader())));
        this.addressLine3 = ((String) in.readValue((String.class.getClassLoader())));
        this.brandName = ((Object) in.readValue((Object.class.getClassLoader())));
        this.city = ((String) in.readValue((String.class.getClassLoader())));
        this.countryId = ((String) in.readValue((String.class.getClassLoader())));
        this.zipCode = ((String) in.readValue((String.class.getClassLoader())));
        this.alternateFirstName = ((String) in.readValue((String.class.getClassLoader())));
        this.alternateLastName = ((String) in.readValue((String.class.getClassLoader())));
        this.alternateEmail = ((Object) in.readValue((Object.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.emailingAccepted = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.sendReceiptByMail = ((Object) in.readValue((Object.class.getClassLoader())));
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.isCompany = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.alternatePhoneNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.cellularPhoneNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.homePhoneNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.officePhoneNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.userField = ((Object) in.readValue((Object.class.getClassLoader())));
        this.usualStoreId = ((String) in.readValue((String.class.getClassLoader())));
        this.activityCode = ((Object) in.readValue((Object.class.getClassLoader())));
        this.barCode = ((String) in.readValue((String.class.getClassLoader())));
        this.birthDateDay = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.birthDateMonth = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.birthDateYear = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.companyIdNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.companyTitleId = ((Object) in.readValue((Object.class.getClassLoader())));
        this.currencyId = ((String) in.readValue((String.class.getClassLoader())));
        this.fiscallId = ((String) in.readValue((String.class.getClassLoader())));
        this.languageId = ((String) in.readValue((String.class.getClassLoader())));
        this.nationalityId = ((String) in.readValue((String.class.getClassLoader())));
        this.passportNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.sex = ((String) in.readValue((String.class.getClassLoader())));
        this.creationStoreId = ((Object) in.readValue((Object.class.getClassLoader())));
        this.internetPassword = ((String) in.readValue((String.class.getClassLoader())));
    }
    public Customer() {
    }

    public Boolean getWalkThrough() {
        return isWalkThrough;
    }

    public void setWalkThrough(Boolean walkThrough) {
        isWalkThrough = walkThrough;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public Object getBrandName() {
        return brandName;
    }

    public void setBrandName(Object brandName) {
        this.brandName = brandName;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAlternateFirstName() {
        return alternateFirstName;
    }

    public void setAlternateFirstName(String alternateFirstName) {
        this.alternateFirstName = alternateFirstName;
    }

    public String getAlternateLastName() {
        return alternateLastName;
    }

    public void setAlternateLastName(String alternateLastName) {
        this.alternateLastName = alternateLastName;
    }

    public Object getAlternateEmail() {
        return alternateEmail;
    }

    public void setAlternateEmail(Object alternateEmail) {
        this.alternateEmail = alternateEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEmailingAccepted() {
        return emailingAccepted;
    }

    public void setEmailingAccepted(Boolean emailingAccepted) {
        this.emailingAccepted = emailingAccepted;
    }

    public Object getSendReceiptByMail() {
        return sendReceiptByMail;
    }

    public void setSendReceiptByMail(Object sendReceiptByMail) {
        this.sendReceiptByMail = sendReceiptByMail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Boolean getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(Boolean isCompany) {
        this.isCompany = isCompany;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAlternatePhoneNumber() {
        return alternatePhoneNumber;
    }

    public void setAlternatePhoneNumber(String alternatePhoneNumber) {
        this.alternatePhoneNumber = alternatePhoneNumber;
    }

    public String getCellularPhoneNumber() {
        return cellularPhoneNumber;
    }

    public void setCellularPhoneNumber(String cellularPhoneNumber) {
        this.cellularPhoneNumber = cellularPhoneNumber;
    }

    public String getHomePhoneNumber() {
        return homePhoneNumber;
    }

    public void setHomePhoneNumber(String homePhoneNumber) {
        this.homePhoneNumber = homePhoneNumber;
    }

    public String getOfficePhoneNumber() {
        return officePhoneNumber;
    }

    public void setOfficePhoneNumber(String officePhoneNumber) {
        this.officePhoneNumber = officePhoneNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getUserField() {
        return userField;
    }

    public void setUserField(Object userField) {
        this.userField = userField;
    }

    public String getUsualStoreId() {
        return usualStoreId;
    }

    public void setUsualStoreId(String usualStoreId) {
        this.usualStoreId = usualStoreId;
    }

    public Object getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(Object activityCode) {
        this.activityCode = activityCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Integer getBirthDateDay() {
        return birthDateDay;
    }

    public void setBirthDateDay(Integer birthDateDay) {
        this.birthDateDay = birthDateDay;
    }

    public Integer getBirthDateMonth() {
        return birthDateMonth;
    }

    public void setBirthDateMonth(Integer birthDateMonth) {
        this.birthDateMonth = birthDateMonth;
    }

    public Integer getBirthDateYear() {
        return birthDateYear;
    }

    public void setBirthDateYear(Integer birthDateYear) {
        this.birthDateYear = birthDateYear;
    }

    public String getCompanyIdNumber() {
        return companyIdNumber;
    }

    public void setCompanyIdNumber(String companyIdNumber) {
        this.companyIdNumber = companyIdNumber;
    }

    public Object getCompanyTitleId() {
        return companyTitleId;
    }

    public void setCompanyTitleId(Object companyTitleId) {
        this.companyTitleId = companyTitleId;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getFiscallId() {
        return fiscallId;
    }

    public void setFiscallId(String fiscallId) {
        this.fiscallId = fiscallId;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(String nationalityId) {
        this.nationalityId = nationalityId;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Object getCreationStoreId() {
        return creationStoreId;
    }

    public void setCreationStoreId(Object creationStoreId) {
        this.creationStoreId = creationStoreId;
    }

    public String getInternetPassword() {
        return internetPassword;
    }

    public void setInternetPassword(String internetPassword) {
        this.internetPassword = internetPassword;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(customerId);
        dest.writeValue(addressLine1);
        dest.writeValue(addressLine2);
        dest.writeValue(addressLine3);
        dest.writeValue(brandName);
        dest.writeValue(city);
        dest.writeValue(countryId);
        dest.writeValue(zipCode);
        dest.writeValue(alternateFirstName);
        dest.writeValue(alternateLastName);
        dest.writeValue(alternateEmail);
        dest.writeValue(email);
        dest.writeValue(emailingAccepted);
        dest.writeValue(sendReceiptByMail);
        dest.writeValue(firstName);
        dest.writeValue(isCompany);
        dest.writeValue(lastName);
        dest.writeValue(alternatePhoneNumber);
        dest.writeValue(cellularPhoneNumber);
        dest.writeValue(homePhoneNumber);
        dest.writeValue(officePhoneNumber);
        dest.writeValue(title);
        dest.writeValue(userField);
        dest.writeValue(usualStoreId);
        dest.writeValue(activityCode);
        dest.writeValue(barCode);
        dest.writeValue(birthDateDay);
        dest.writeValue(birthDateMonth);
        dest.writeValue(birthDateYear);
        dest.writeValue(companyIdNumber);
        dest.writeValue(companyTitleId);
        dest.writeValue(currencyId);
        dest.writeValue(fiscallId);
        dest.writeValue(languageId);
        dest.writeValue(nationalityId);
        dest.writeValue(passportNumber);
        dest.writeValue(sex);
        dest.writeValue(creationStoreId);
        dest.writeValue(internetPassword);
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

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    @Override
    public String toString() {
        return "{"+
                "customerId='" + customerId + '\'' +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", addressLine3='" + addressLine3 + '\'' +
                ", brandName=" + brandName +
                ", city='" + city + '\'' +
                ", countryId='" + countryId + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", alternateFirstName='" + alternateFirstName + '\'' +
                ", alternateLastName='" + alternateLastName + '\'' +
                ", alternateEmail=" + alternateEmail +
                ", email='" + email + '\'' +
                ", emailingAccepted=" + emailingAccepted +
                ", sendReceiptByMail=" + sendReceiptByMail +
                ", firstName='" + firstName + '\'' +
                ", isCompany=" + isCompany +
                ", isWalkThrough=" + isWalkThrough +
                ", lastName='" + lastName + '\'' +
                ", alternatePhoneNumber='" + alternatePhoneNumber + '\'' +
                ", cellularPhoneNumber='" + cellularPhoneNumber + '\'' +
                ", homePhoneNumber='" + homePhoneNumber + '\'' +
                ", officePhoneNumber='" + officePhoneNumber + '\'' +
                ", title='" + title + '\'' +
                ", userField=" + userField +
                ", usualStoreId='" + usualStoreId + '\'' +
                ", activityCode=" + activityCode +
                ", barCode='" + barCode + '\'' +
                ", birthDateDay=" + birthDateDay +
                ", birthDateMonth=" + birthDateMonth +
                ", birthDateYear=" + birthDateYear +
                ", companyIdNumber='" + companyIdNumber + '\'' +
                ", companyTitleId=" + companyTitleId +
                ", currencyId='" + currencyId + '\'' +
                ", fiscallId='" + fiscallId + '\'' +
                ", languageId='" + languageId + '\'' +
                ", nationalityId='" + nationalityId + '\'' +
                ", passportNumber='" + passportNumber + '\'' +
                ", sex='" + sex + '\'' +
                ", creationStoreId=" + creationStoreId +
                ", internetPassword='" + internetPassword + '\'' +
                ", selected=" + selected +
                ", reference='" + reference + '\'' +
                '}';
    }
}
