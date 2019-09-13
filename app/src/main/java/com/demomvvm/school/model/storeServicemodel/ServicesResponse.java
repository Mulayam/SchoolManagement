package com.demomvvm.school.model.storeServicemodel;



import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServicesResponse implements Parcelable
{


    @SerializedName("service_id")
    @Expose
    private String id;
    @SerializedName("service_store_id")
    @Expose
    private String storeId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("dt_added")
    @Expose
    private String dtAdded;
    @SerializedName("dt_updated")
    @Expose
    private String dtUpdated;

    private String isChecked = "false";


    public ServicesResponse(Parcel in) {
        id = in.readString();
        storeId = in.readString();
        name = in.readString();
        price = in.readString();
        time = in.readString();
        status = in.readString();
        dtAdded = in.readString();
        dtUpdated = in.readString();

    }

    public static final Creator<ServicesResponse> CREATOR = new Creator<ServicesResponse>() {
        @Override
        public ServicesResponse createFromParcel(Parcel in) {
            return new ServicesResponse(in);
        }

        @Override
        public ServicesResponse[] newArray(int size) {
            return new ServicesResponse[size];
        }
    };

    public ServicesResponse() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDtAdded() {
        return dtAdded;
    }

    public void setDtAdded(String dtAdded) {
        this.dtAdded = dtAdded;
    }

    public String getDtUpdated() {
        return dtUpdated;
    }

    public void setDtUpdated(String dtUpdated) {
        this.dtUpdated = dtUpdated;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(storeId);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(time);
        dest.writeString(status);
        dest.writeString(dtAdded);
        dest.writeString(dtUpdated);

    }
}
