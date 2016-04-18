package com.firerox.oplungiphone.api;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by TRUNGGUNNERs on 12/04/2016.
 */
public interface SendOrderAPI {
    @FormUrlEncoded
    @POST("/api/add_order.php/")

    void insertOrder(
        @Field("customer_name") String customer_name,
        @Field("customer_email") String customer_email,
        @Field("customer_phone") String customer_phone,
        @Field("customer_address") String customer_address,
        @Field("customer_info") String customer_info,
        @Field("detail") String detail,
        @Field("date_order") String date_order,

        Callback<Response> callback);


}
