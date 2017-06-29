
package com.myhubber.myhubber.voucher_wallet_directory.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * Created by Midhun ek on 1/18/17,
 * Ideaprodigies,
 * Dubai, UAE.
 * Class Name   : Voucherdetail
 * Description  : Voucherdetail Models
 *
 */

public class Voucherdetail implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_description")
    @Expose
    private String categoryDescription;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("color")
    @Expose
    private String color;



    @SerializedName("type")
    @Expose
    private String type;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
    @SerializedName("merchant_id")
    @Expose
    private String merchantId;
    @SerializedName("merchant_logo")
    @Expose
    private String merchantLogo;
    @SerializedName("merchant_tradename")
    @Expose
    private String merchantTradename;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantLogo() {
        return merchantLogo;
    }

    public void setMerchantLogo(String merchantLogo) {
        this.merchantLogo = merchantLogo;
    }

    public String getMerchantTradename() {
        return merchantTradename;
    }

    public void setMerchantTradename(String merchantTradename) {
        this.merchantTradename = merchantTradename;
    }


    @SerializedName("branch_id")
    @Expose
    private String branchId;
    @SerializedName("branch_name")
    @Expose
    private String branchName;
    @SerializedName("branch_pin")
    @Expose
    private String branchPin;
    @SerializedName("branch_location")
    @Expose
    private String branchLocation;
    @SerializedName("branch_phone")
    @Expose
    private String branchPhone;
    @SerializedName("branch_lat")
    @Expose
    private String branchLat;
    @SerializedName("branch_lng")
    @Expose
    private String branchLng;


    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchPin() {
        return branchPin;
    }

    public void setBranchPin(String branchPin) {
        this.branchPin = branchPin;
    }

    public String getBranchLocation() {
        return branchLocation;
    }

    public void setBranchLocation(String branchLocation) {
        this.branchLocation = branchLocation;
    }

    public String getBranchPhone() {
        return branchPhone;
    }

    public void setBranchPhone(String branchPhone) {
        this.branchPhone = branchPhone;
    }

    public String getBranchLat() {
        return branchLat;
    }

    public void setBranchLat(String branchLat) {
        this.branchLat = branchLat;
    }

    public String getBranchLng() {
        return branchLng;
    }

    public void setBranchLng(String branchLng) {
        this.branchLng = branchLng;
    }



    @SerializedName("voucher_id")
    @Expose
    private String voucherId;
    @SerializedName("voucher_name")
    @Expose
    private String voucherName;
    @SerializedName("voucher_artwork")
    @Expose
    private String voucherArtwork;
    @SerializedName("voucher_androidartwork")
    @Expose
    private String voucherAndroidartwork;
    @SerializedName("voucher_description")
    @Expose
    private String voucherDescription;
    @SerializedName("voucher_merchant")
    @Expose
    private String voucherMerchant;
    @SerializedName("voucher_terms")
    @Expose
    private String voucherTerms;
    @SerializedName("voucherpath")
    @Expose
    private String voucherpath;
//    @SerializedName("merchant_logo")
//    @Expose
//    private String merchantLogo;
//    @SerializedName("merchant_tradename")
//    @Expose
//    private String merchantTradename;
    @SerializedName("daysleft")
    @Expose
    private String daysleft;
//    @SerializedName("branch_id")
//    @Expose
//    private String branchId;
//    @SerializedName("branch_name")
//    @Expose
//    private String branchName;
//    @SerializedName("branch_pin")
//    @Expose
//    private String branchPin;
//    @SerializedName("branch_location")
//    @Expose
//    private String branchLocation;
//    @SerializedName("branch_phone")
//    @Expose
//    private String branchPhone;
//    @SerializedName("branch_lat")
//    @Expose
//    private String branchLat;
//    @SerializedName("branch_lng")
//    @Expose
//    private String branchLng;
    @SerializedName("branches")
    @Expose
    private String branches;

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public String getVoucherArtwork() {
        return voucherArtwork;
    }

    public void setVoucherArtwork(String voucherArtwork) {
        this.voucherArtwork = voucherArtwork;
    }

    public String getVoucherAndroidartwork() {
        return voucherAndroidartwork;
    }

    public void setVoucherAndroidartwork(String voucherAndroidartwork) {
        this.voucherAndroidartwork = voucherAndroidartwork;
    }

    public String getVoucherDescription() {
        return voucherDescription;
    }

    public void setVoucherDescription(String voucherDescription) {
        this.voucherDescription = voucherDescription;
    }

    public String getVoucherMerchant() {
        return voucherMerchant;
    }

    public void setVoucherMerchant(String voucherMerchant) {
        this.voucherMerchant = voucherMerchant;
    }

    public String getVoucherTerms() {
        return voucherTerms;
    }

    public void setVoucherTerms(String voucherTerms) {
        this.voucherTerms = voucherTerms;
    }

    public String getVoucherpath() {
        return voucherpath;
    }

    public void setVoucherpath(String voucherpath) {
        this.voucherpath = voucherpath;
    }



    public String getDaysleft() {
        return daysleft;
    }

    public void setDaysleft(String daysleft) {
        this.daysleft = daysleft;
    }

    public String getBranches() {
        return branches;
    }

    public void setBranches(String branches) {
        this.branches = branches;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
