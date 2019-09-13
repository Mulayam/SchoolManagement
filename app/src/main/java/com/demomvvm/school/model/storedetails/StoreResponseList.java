package com.demomvvm.school.model.storedetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.demomvvm.school.model.storeListmodel.StoreResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kailash Patel
 */

public class StoreResponseList implements Parcelable {

    @SerializedName("success")
    @Expose
    private String success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private StoreResponse storeResponse = null;


    protected StoreResponseList(Parcel in) {
        success = in.readString();
        message = in.readString();
        storeResponse = in.readParcelable(StoreResponse.class.getClassLoader());
    }

    public static final Creator<StoreResponseList> CREATOR = new Creator<StoreResponseList>() {
        @Override
        public StoreResponseList createFromParcel(Parcel in) {
            return new StoreResponseList(in);
        }

        @Override
        public StoreResponseList[] newArray(int size) {
            return new StoreResponseList[size];
        }
    };

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StoreResponse getStoreResponse() {
        return storeResponse;
    }

    public void setStoreResponse(StoreResponse storeResponse) {
        this.storeResponse = storeResponse;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(success);
        dest.writeString(message);
        dest.writeParcelable(storeResponse, flags);
    }
}
