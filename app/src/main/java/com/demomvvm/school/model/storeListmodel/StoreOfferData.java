package com.demomvvm.school.model.storeListmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on stra3/stra2/2017.
 */

public class StoreOfferData implements Parcelable {

    @SerializedName("offer_id")
    @Expose
    private String id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("offer_service_id")
    @Expose
    private String offer_service_id;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("actual_price")
    @Expose
    private String actualPrice;

    @SerializedName("discount_price")
    @Expose
    private String discountPrice;

    @SerializedName("discount_per")
    @Expose
    private String discountPer;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("start_date")
    @Expose
    private String start_date;

    @SerializedName("end_date")
    @Expose
    private String end_date;

    public String getOffer_service_id() {
        return offer_service_id;
    }

    public void setOffer_service_id(String offer_service_id) {
        this.offer_service_id = offer_service_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public static Creator<StoreOfferData> getCREATOR() {
        return CREATOR;
    }



    private StoreOfferData(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.actualPrice = in.readString();
        this.discountPrice = in.readString();
        this.discountPer = in.readString();
        this.image = in.readString();
        this.end_date = in.readString();
        this.start_date = in.readString();
        this.offer_service_id = in.readString();
    }


    public static final Creator<StoreOfferData> CREATOR = new Creator<StoreOfferData>() {

        @Override
        public StoreOfferData createFromParcel(Parcel source) {
            return new StoreOfferData(source);
        }

        @Override
        public StoreOfferData[] newArray(int size) {
            return new StoreOfferData[size];
        }
    };


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(String discountPer) {
        this.discountPer = discountPer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(actualPrice);
        dest.writeString(discountPrice);
        dest.writeString(discountPer);
        dest.writeString(image);
        dest.writeString(end_date);
        dest.writeString(start_date);
        dest.writeString(offer_service_id);



    }

}
