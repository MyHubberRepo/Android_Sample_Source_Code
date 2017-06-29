
package com.myhubber.myhubber.hubboards_directory.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by Midhun ek on 4/2/17,
 * Ideaprodigies,
 * Dubai, UAE.
 */


public class Feature {

    @SerializedName("Area")
    @Expose
    private String area;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

}
