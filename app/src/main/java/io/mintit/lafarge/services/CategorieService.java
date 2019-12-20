package io.mintit.lafarge.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import io.mintit.lafarge.Retrofit.ApiInterface;
import io.mintit.lafarge.Retrofit.ApiManager;
import io.mintit.lafarge.model.Article;
import io.mintit.lafarge.utils.Prefs;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategorieService extends Service {

    ArrayList<Article> listProducts = new ArrayList<>();


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ApiInterface service = ApiManager.createService(ApiInterface.class, Prefs.getPref(Prefs.TOKEN,getApplicationContext()));
        Call<ArrayList<Article>> productCall = service.getarticle(Prefs.getPref(Prefs.STORE , getApplicationContext()));
        productCall.enqueue(new Callback<ArrayList<Article>>() {
            @Override
            public void onResponse(Call<ArrayList<Article>> call, Response<ArrayList<Article>> response) {
                ArrayList<Article> listArticle = response.body();
                System.out.println("service product : " + listArticle);
                for(Article s: listArticle){

                    System.out.println("service product : " + s.toString());
                    Article p = new Article();
                    p.setId(s.getId());
                    p.setPrice(s.getPrice());
                    p.setName(s.getName());
                    p.setStock(s.getStock());
                    p.setProductCode(s.getProductCode());
                    p.setCategory(s.getCategory());
                    p.setEanCode(s.getEanCode());
                    listProducts.add(p);
                }
         Prefs.setPrefList(listProducts,"listProducts",getApplicationContext());
            }

            @Override
            public void onFailure(Call<ArrayList<Article>> call, Throwable throwable){}
        });

        return super.onStartCommand(intent, flags, startId);





    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
