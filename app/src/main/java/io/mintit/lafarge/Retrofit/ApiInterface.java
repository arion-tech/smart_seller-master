package io.mintit.lafarge.Retrofit;

import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.List;

import io.mintit.lafarge.model.Article;
import io.mintit.lafarge.model.Category;
import io.mintit.lafarge.model.Customer;
import io.mintit.lafarge.model.Family;
import io.mintit.lafarge.model.Seller;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @Headers("Content-Type: application/json")
    @POST("/api/v1/applications/login")
    Call<ResponseBody> getToken(@Body JsonObject body);

    @GET("/api/v1/families/findall")
    Call<List<Family>> getFamilies();

    @Headers("Content-Type: application/json")
    @POST("/api/v1/seller/findbycriteria")
    Call<ArrayList<Seller>> getSellersByCriteria(@Body JsonObject storeId);


    @Headers("Content-Type: application/json")
    @POST("/api/v1/products/findByEtablissement")
    Call<ArrayList<Article>> getarticle(@Query("etablissement") String etablissement);


    @GET("/api/v1/categorie/findAll")
    Call<ArrayList<Category>> getcategories();



}
