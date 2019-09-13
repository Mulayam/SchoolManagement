package com.demomvvm.school.model.storeListmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on stra3/stra2/2017.
 */

public class StoreImages implements Parcelable {

    protected StoreImages(Parcel in) {
        image = in.readString();
    }

    public static final Creator<StoreImages> CREATOR = new Creator<StoreImages>() {
        @Override
        public StoreImages createFromParcel(Parcel in) {
            return new StoreImages(in);
        }

        @Override
        public StoreImages[] newArray(int size) {
            return new StoreImages[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @SerializedName("image")
    @Expose
    private String image;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
    }
}
