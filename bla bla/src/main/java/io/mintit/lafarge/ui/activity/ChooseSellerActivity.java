package io.mintit.lafarge.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.mintit.lafarge.R;
import io.mintit.lafarge.Retrofit.ApiInterface;
import io.mintit.lafarge.Retrofit.ApiManager;
import io.mintit.lafarge.adapter.ChooseSellerAdapter;
import io.mintit.lafarge.model.Seller;
import io.mintit.lafarge.model.User;
import io.mintit.lafarge.utils.Prefs;
import okhttp3.Request;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseSellerActivity extends AppCompatActivity {

    @BindView(R.id.linearLayout_progress)
    RelativeLayout linearLayoutProgress;

    Gson gson = new Gson();

    String store_code;
    ArrayList<Seller> listSellers = new ArrayList<>();

    ChooseSellerAdapter chooseSellerAdapter;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_seller);
        ButterKnife.bind(this);

        Intent i = getIntent();
        store_code = i.getStringExtra("store_code") + "";
    }

    @OnClick(R.id.linearLayout_seller)
    void onSelectSellerClicked() {
        showSellersDialog();
    }

    private void getSellers(){
        ApiInterface service = ApiManager.createService(ApiInterface.class, Prefs.getPref(Prefs.TOKEN, ChooseSellerActivity.this));
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("storeCode", store_code);
        Call<ArrayList<Seller>> sellerCall = service.getSellersByCriteria(paramObject);

        sellerCall.enqueue(new Callback<ArrayList<Seller>>() {
            @Override
            public void onResponse(Call<ArrayList<Seller>> call, Response<ArrayList<Seller>> response) {
                for(Seller s: response.body()){
                    listSellers.add(s);
                    System.out.println("SEEEELLLLLEEEER : " + s.toString());
                }
                //------------
                //this.showProgressBar(false);
                dialog = new Dialog(ChooseSellerActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_sellers_choose);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                RecyclerView recyclerViewSellers = (RecyclerView) dialog.findViewById(R.id.recyclerView_sellers);
                final EditText edittextFilter = (EditText) dialog.findViewById(R.id.edittext_filter);
                LinearLayout linearLayoutCancel = (LinearLayout) dialog.findViewById(R.id.linearLayout_cancel);
                LinearLayout linearLayoutSubmit = (LinearLayout) dialog.findViewById(R.id.linearLayout_submit);
                LinearLayoutManager layoutManager = new LinearLayoutManager(ChooseSellerActivity.this);
                recyclerViewSellers.setLayoutManager(layoutManager);

                chooseSellerAdapter = new ChooseSellerAdapter(listSellers, ChooseSellerActivity.this, ChooseSellerActivity.this);
                recyclerViewSellers.setAdapter(chooseSellerAdapter);

                chooseSellerAdapter.setOnItemClickListener(new ChooseSellerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Seller seller) {
                        showProgressBar(true);
                        Prefs.setBooleanPref(Prefs.IS_CONNECTED, true, ChooseSellerActivity.this);
                        User sellerUser = new User();
                        sellerUser.setIdUtilisateur(String.valueOf(seller.getId()));
                        Prefs.setPref(Prefs.USER_INFO, gson.toJson(sellerUser), ChooseSellerActivity.this);
                        Prefs.setPref(Prefs.SELLER_LOGIN, gson.toJson(seller), ChooseSellerActivity.this);
                        goToNextScreen();
                    }
                });

                edittextFilter.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (edittextFilter.getText().length() > 0) {
                            chooseSellerAdapter.getFilter().filter(edittextFilter.getText().toString());
                            //System.out.println("AAAAAAAAAAAA: "+edittextFilter.getText().toString());
                        } else {
                            chooseSellerAdapter.reset(listSellers);
                        }
                    }
                });
                linearLayoutCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                linearLayoutSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //selectedSupplier = suppliersAdapter.getSelectedSupplier();
                        //setupCartWithSeller();
                        dialog.dismiss();
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }

            @Override
            public void onFailure(Call<ArrayList<Seller>> call, Throwable throwable) {
                System.err.println("ERRRRRRRRRRRRRRRRRRRRRR" + throwable);
                System.out.println("BBBBBBBBBBBBBBBBBBBB : " + bodyToString(call.request()));
            }
        });
        //return DBSellerList;
    }


    private static String bodyToString(final Request request){

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    private void showSellersDialog() {
        getSellers();


    }


        public void showProgressBar(final boolean show){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    linearLayoutProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }

    private void goToNextScreen() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgressBar(false);
                startActivity(new Intent(ChooseSellerActivity.this, MainActivity.class));
                finishAffinity();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
