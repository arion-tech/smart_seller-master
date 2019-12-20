package io.mintit.lafarge.Retrofit;

import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.List;

import io.mintit.lafarge.model.Family;
import io.mintit.lafarge.model.Seller;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    @Headers("Content-Type: application/json")
    @POST("/api/v1/applications/login")
    Call<ResponseBody> getToken(@Body JsonObject body);

    @GET("/api/v1/families/findall")
    Call<List<Family>> getFamilies();

    @Headers("Content-Type: application/json")
    @POST("/api/v1/seller/findbycriteria")
    Call<ArrayList<Seller>> getSellersByCriteria(@Body JsonObject storeId);

}
