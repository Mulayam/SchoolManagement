package com.demomvvm.school.webservice;


import com.demomvvm.school.model.ResponseBase;
import com.demomvvm.school.model.loginModel.LoginResponse;
import com.demomvvm.school.model.storeListmodel.StoreResponseData;
import com.demomvvm.school.model.storeServicemodel.ServicesList;
import com.demomvvm.school.model.storedetails.StoreDetailsMainModel;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Kailash Patel
 */

public interface UsersService {

    @FormUrlEncoded
    @POST("store_list")
    Observable<StoreResponseData> doGetLocation(@Field("location_id") String location_id,
                                                @Field("offset") String offset);

    @FormUrlEncoded
    @POST("store_list")
    Observable<StoreResponseData> doGetLocationWithLatLng(@Field("latitude") String latitude,
                                                          @Field("longitude") String longitude,
                                                          @Field("offset") String offset);

    @FormUrlEncoded
    @POST("user_login")
    Observable<LoginResponse> doLogin(@Field("email") String email, @Field("password") String password, @Field("login_type") String login_type);

    @FormUrlEncoded
    @POST("add_device")
    Observable<ResponseBase> addDeviceToken(@Field("user_id") String user_id, @Field("device_id") String device_id, @Field("device_token") String device_token, @Field("device_type") String device_type);


    @FormUrlEncoded
    @POST("user_login")
    Observable<LoginResponse> doFBSocialLogin(@Field("fname") String fname, @Field("lname") String lname, @Field("email") String email, @Field("phone") String strPhoneNo,
                                              @Field("login_type") String login_type, @Field("facebook_token_id") String facebook_token_id);

    @FormUrlEncoded
    @POST("user_login")
    Observable<LoginResponse> doGoogleSocialLogin(@Field("fname") String fname, @Field("lname") String lname, @Field("email") String email, @Field("phone") String strPhoneNo,
                                                  @Field("login_type") String login_type, @Field("gmail_token_id") String gmail_token_id);

    @FormUrlEncoded
    @POST("user_forgot_password")
    Observable<ResponseBase> doForgotPasswordDetails(@Field("email") String email);

    @FormUrlEncoded
    @POST("user_register")
    Observable<ResponseBase> doRegister(@Field("fname") String fname, @Field("lname") String lname, @Field("email") String email, @Field("password") String password, @Field("phone") String phone, @Field("login_type") String login_type);


    @FormUrlEncoded
    @POST("store_service_list")
    Observable<ServicesList> getServiceList(@Field("store_id") String storeId,@Field("category_id") String category_id);


    @FormUrlEncoded
    @POST("book_appointment")
    Observable<ResponseBase> doBookAppointment(
            @Field("store_id") String storeId,
            @Field("user_id") String userId,
            @Field("date") String date,
            @Field("time") String time,
            @Field("staff_id") String staff_id,
            @Field("service_id") String serviceid,
            @Field("transaction_id") String transaction_id,
            @Field("offer_id") String offferID,
            @Field("total_amount") String totalAmount);


    @FormUrlEncoded
    @POST("cancel_booking")
    Observable<ResponseBase> doCancelBooking(@Field("booking_id") String bookingID);

    @FormUrlEncoded
    @POST("review_store")
    Observable<ResponseBase> doStoreReview(@Field("user_id") String user_id, @Field("store_id") String store_id, @Field("rating") String rating, @Field("review") String review);

    @FormUrlEncoded
    @POST("profile_update")
    Observable<ResponseBase> getUserProfileDetailsResponse(@Field("fname") String fname, @Field("lname") String lname,
                                                           @Field("email") String email, @Field("phone") String phone);

    @FormUrlEncoded
    @POST("user_feedback")
    Observable<ResponseBase> doFeedback(@Field("user_id") String uId,@Field("rating") String ratting,@Field("feedback") String feedback);

    @FormUrlEncoded
    @POST("user_change_password")
    Observable<ResponseBase> doGetChangePasswordDetails(@Field("email") String email, @Field("old_password") String old_password, @Field("new_password") String new_password);


    @FormUrlEncoded
    @POST("store_details")
    Observable<StoreDetailsMainModel> getStoreDetails(@Field("store_id") String storeId);




}
