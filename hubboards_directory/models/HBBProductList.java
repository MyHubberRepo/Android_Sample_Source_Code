
package com.myhubber.myhubber.hubboards_directory.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created by Midhun ek on 4/2/17,
 * Ideaprodigies,
 * Dubai, UAE.
 */

public class HBBProductList {

    @SerializedName("adDetails")
    @Expose
    private List<AdDetail> adDetails = null;
    @SerializedName("is_success")
    @Expose
    private String isSuccess;
    @SerializedName("message")
    @Expose
    private String message;

    public List<AdDetail> getAdDetails() {
        return adDetails;
    }

    public void setAdDetails(List<AdDetail> adDetails) {
        this.adDetails = adDetails;
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
