
package com.myhubber.myhubber.voucher_wallet_directory.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created by Midhun ek on 1/18/17,
 * Ideaprodigies,
 * Dubai, UAE.
 */

public class Merchant {

    @SerializedName("is_success")
    @Expose
    private String isSuccess;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("voucherdetails")
    @Expose
    private List<Voucherdetail> voucherdetails = null;

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

    public List<Voucherdetail> getVoucherdetails() {
        return voucherdetails;
    }

    public void setVoucherdetails(List<Voucherdetail> voucherdetails) {
        this.voucherdetails = voucherdetails;
    }

}
