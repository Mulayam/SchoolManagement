package com.demomvvm.school.model.loginModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("responsedata")
    @Expose
    private LoginResponseList responsedata;

    public LoginResponseList getResponsedata() {
        return responsedata;
    }

    public void setResponsedata(LoginResponseList responsedata) {
        this.responsedata = responsedata;
    }

}