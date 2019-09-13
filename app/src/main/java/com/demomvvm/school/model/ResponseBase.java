package com.demomvvm.school.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseBase {

    @SerializedName("responsedata")
    @Expose
    private Responsedata responsedata;

    public Responsedata getResponsedata() {
        return responsedata;
    }

    public void setResponsedata(Responsedata responsedata) {
        this.responsedata = responsedata;
    }

}
