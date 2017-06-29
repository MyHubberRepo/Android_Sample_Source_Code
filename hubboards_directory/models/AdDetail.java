
package com.myhubber.myhubber.hubboards_directory.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by Midhun ek on 4/2/17,
 * Ideaprodigies,
 * Dubai, UAE.
 */

public class AdDetail {

    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("adID")
    @Expose
    private String adID;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("streetAddress")
    @Expose
    private String streetAddress;
    @SerializedName("subLocality")
    @Expose
    private String subLocality;
    @SerializedName("locality")
    @Expose
    private String locality;
    @SerializedName("cls_status")
    @Expose
    private String clsStatus;
    @SerializedName("clsstatustxt")
    @Expose
    private String clsstatustxt;
    @SerializedName("cls_additionalcontactinfo")
    @Expose
    private String clsAdditionalcontactinfo;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("posteddate")
    @Expose
    private String posteddate;
    @SerializedName("media")
    @Expose
    private String media;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("imageurl")
    @Expose
    private String imageurl;
    @SerializedName("videothumb")
    @Expose
    private String videothumb;
    @SerializedName("mediastatus")
    @Expose
    private String mediastatus;
    @SerializedName("feature")
    @Expose
    private Feature feature;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAdID() {
        return adID;
    }

    public void setAdID(String adID) {
        this.adID = adID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getSubLocality() {
        return subLocality;
    }

    public void setSubLocality(String subLocality) {
        this.subLocality = subLocality;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getClsStatus() {
        return clsStatus;
    }

    public void setClsStatus(String clsStatus) {
        this.clsStatus = clsStatus;
    }

    public String getClsstatustxt() {
        return clsstatustxt;
    }

    public void setClsstatustxt(String clsstatustxt) {
        this.clsstatustxt = clsstatustxt;
    }

    public String getClsAdditionalcontactinfo() {
        return clsAdditionalcontactinfo;
    }

    public void setClsAdditionalcontactinfo(String clsAdditionalcontactinfo) {
        this.clsAdditionalcontactinfo = clsAdditionalcontactinfo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPosteddate() {
        return posteddate;
    }

    public void setPosteddate(String posteddate) {
        this.posteddate = posteddate;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getVideothumb() {
        return videothumb;
    }

    public void setVideothumb(String videothumb) {
        this.videothumb = videothumb;
    }

    public String getMediastatus() {
        return mediastatus;
    }

    public void setMediastatus(String mediastatus) {
        this.mediastatus = mediastatus;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

}
