package com.demomvvm.school.model.storeServicemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServicesList {

    @SerializedName("responsedata")
    @Expose
    private ServiceResponsedata serviceResponsedata;

    public ServiceResponsedata getServiceResponsedata() {
        return serviceResponsedata;
    }

    public void setServiceResponsedata(ServiceResponsedata serviceResponsedata) {
        this.serviceResponsedata = serviceResponsedata;
    }

}
