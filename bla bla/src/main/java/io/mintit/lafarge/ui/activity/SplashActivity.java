package io.mintit.lafarge.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.mintit.lafarge.DBRoom.DBLafarge;
import io.mintit.lafarge.R;
import io.mintit.lafarge.Retrofit.ApiInterface;
import io.mintit.lafarge.Retrofit.ApiManager;
import io.mintit.lafarge.app.LafargeApp;
import io.mintit.lafarge.model.AppAuthentication;
import io.mintit.lafarge.model.AppConfig;
import io.mintit.lafarge.model.Family;
import io.mintit.lafarge.utils.Prefs;
import io.mintit.lafarge.utils.ToggleButtonLayout;
import io.mintit.lafarge.utils.Utils;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.imageViewConfiguration)
    ImageView imageViewConfiguration;
    EditText editTextClientId;
    private View popupInputDialogView;

    @BindView(R.id.progressBar_authentication)
    ProgressBar progressBarAuthentication;
    @BindView(R.id.linearLayout_body)
    LinearLayout linearLayoutBody;

    /*@BindView(R.id.progress_users)
    ToggleButtonLayout progressBarUsers;
    @BindView(R.id.progress_clients)
    ToggleButtonLayout progressClients;
    @BindView(R.id.progress_products)
    ToggleButtonLayout progressProducts;
    @BindView(R.id.progress_categories)
    ToggleButtonLayout progressCategories;
    @BindView(R.id.progress_seller)
    ToggleButtonLayout progressSeller;*/

    @BindView(R.id.retry_rl)
    RelativeLayout retryRL;

    @BindView(R.id.splash_logo)
    ImageView imageViewLogo;

    @BindView(R.id.buttonLogin)
    Button loginButton;

    private Gson gson = new Gson();

    private AppAuthentication appAuthentication;
    private int indexInventory = 0;
    private JSONArray inventoryResponse;
    private DBLafarge lafargeDatabase;
    private LafargeApp lafargeApp;

    private List<Family> familiesList = new ArrayList<>();

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //private boolean isDone = false;
    private String logo_url = "https://media.licdn.com/dms/image/C560BAQGGHe5vIxFepw/company-logo_200_200/0?e=2159024400&v=beta&t=ifSDFOdHb0Tf45DSk7-4udCr3MHjsbAo5jrXjIyF2pM";
    //ImageLoader imageLoader;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(Utils.isTablet(this) ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //------------------------
        Prefs.setPref(Prefs.TOKEN, "", SplashActivity.this);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        lafargeApp = (LafargeApp)getApplication();
        lafargeDatabase = lafargeApp.getDataBaseInstance();
        verifyStoragePermissions(this);
        //getSupportActionBar().hide();
        deleteFiles();
        //if (Prefs.getBooleanPref(Prefs.IS_SYNCHRONIZED, this, false)) {
        System.out.println("PREF CLIENT ID : "+Prefs.getPref(Prefs.CLIENT_ID, SplashActivity.this));
    }

    @OnClick(R.id.buttonLogin)
    void onLogin(){
        if(Prefs.getPref(Prefs.CLIENT_ID, SplashActivity.this) != null) {
            if (Prefs.getPref(Prefs.TOKEN, SplashActivity.this) != null) {
                if (!Prefs.getPref(Prefs.TOKEN, SplashActivity.this).equals("")) {
                    Picasso.get().load(logo_url).into(new Target() {
                          @Override
                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                              try{
                                  //isDone = true;
                                  File sdCard = Environment.getExternalStorageDirectory();
                                  @SuppressLint("DefaultLocale") String fileName = String.format("%d.jpg", System.currentTimeMillis());
                                  File dir = new File(sdCard.getAbsolutePath() + "/sscache");
                                  dir.mkdirs();
                                  final File myImageFile = new File(dir, fileName); // Create image file
                                  FileOutputStream fos = null;
                                  try {
                                      fos = new FileOutputStream(myImageFile);
                                      //System.out.println("PAAAAAAAAAAAAAAAAAAAAAATH : " + myImageFile.toString());
                                      //Bitmap bitmap = Picasso.get().load(MyUrl).get();
                                      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                      imageViewLogo.setImageBitmap(bitmap);
                                      Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                      intent.setData(Uri.fromFile(myImageFile));
                                      getApplicationContext().sendBroadcast(intent);
                                      //------
                                      Prefs.setPref(Prefs.LOGO_URL, myImageFile.toString(), SplashActivity.this);
                                      //-----Check for restrictions
                                      AppConfig appConfig = new AppConfig();
                                      appConfig.setIsActif(true);
                                      appConfig.setNewCustomer(1);
                                      appConfig.setListCustomer(1);
                                      appConfig.setCatalogProduct(1);
                                      appConfig.setListCart(1);
                                      Prefs.setPref(Prefs.APP_CONFIG, gson.toJson(appConfig), SplashActivity.this);
                                      if(appConfig.getIsActif()){
                                          goToNextScreen();
                                      } else {
                                          Toast.makeText(SplashActivity.this,"You are not authorized to use this application",Toast.LENGTH_LONG).show();
                                          //showProgressBar(false);
                                          progressBarAuthentication.setVisibility(View.GONE);
                                      }
                                      //---------------------------

                                  } catch (IOException e) {
                                      e.printStackTrace();
                                  } finally {
                                      try {
                                          fos.close();
                                      } catch (IOException e) {
                                          e.printStackTrace();
                                      }
                                  }
                              }catch (Exception e){
                              }
                          }

                          @Override
                          public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                          }

                          @Override
                          public void onPrepareLoad(Drawable placeHolderDrawable) {
                          }
                      }
                    );

                    //goToNextScreen();
                } else {
                    if (Utils.isNetworkAvailable(this)) {
                        //subscribeDevice();
                        System.out.println("HEEEEEEEEEEEEEEEEEEEEREEEEE");
                        subscribeDevice2();
                        //goToNextScreen();
                    } else {
                        progressBarAuthentication.setVisibility(View.GONE);
                        Toast.makeText(this, "check internet", Toast.LENGTH_SHORT).show();
                        retryRL.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                Prefs.setPref(Prefs.TOKEN, "", SplashActivity.this);
                retry();
            }
        } else {

        }
    }

    public void deleteFiles(){
        File dir = new File(Environment.getExternalStorageDirectory()+"/sscache");
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block this thread waiting for the user's response! After the user sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an app-defined int constant. The callback method gets the result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the contacts-related task you need to do.
                    progressBarAuthentication.setVisibility(View.VISIBLE);
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other // permissions this app might request
        }
    }


    @OnClick(R.id.retry_img)
    void retry(){
        if(Utils.isNetworkAvailable(this)) {
            retryRL.setVisibility(View.GONE);
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(this,"check internet",Toast.LENGTH_SHORT).show();
        }

    }

    //---------Configuration Action
    @OnClick(R.id.imageViewConfiguration)
    void configureApp(){
        // Create a AlertDialog Builder.
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashActivity.this);
        // Set title, icon, can not cancel properties.
        alertDialogBuilder.setTitle("Configuration");
        alertDialogBuilder.setIcon(R.drawable.settings_icon2);
        alertDialogBuilder.setCancelable(false);

        // Init popup dialog view and it's ui controls.
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(SplashActivity.this);
        // Inflate the popup dialog from a layout xml file.
        popupInputDialogView = layoutInflater.inflate(R.layout.dialog_conf_app, null);
        editTextClientId = (EditText) popupInputDialogView.findViewById(R.id.edittext_clientid);
        LinearLayout linearLayoutCancel = (LinearLayout) popupInputDialogView.findViewById(R.id.linearLayout_cancel);
        LinearLayout linearLayoutSubmit = (LinearLayout) popupInputDialogView.findViewById(R.id.linearLayout_submit);

        // Set the inflated layout view object to the AlertDialog builder.
        alertDialogBuilder.setView(popupInputDialogView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        editTextClientId.setText(Prefs.getPref(Prefs.CLIENT_ID, SplashActivity.this));
        //----------------
        linearLayoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        linearLayoutSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.setPref(Prefs.CLIENT_ID, editTextClientId.getText().toString(), SplashActivity.this);
                //System.out.println("Client id : "+editTextClientId.getText().toString());
                alertDialog.dismiss();
                retry();
            }
        });
        // Create AlertDialog and show.

        alertDialog.show();
    }

    //-----------------------------
    private void subscribeDevice2(){
        if(!Prefs.getPref(Prefs.CLIENT_ID, SplashActivity.this).equals("") || Prefs.getPref(Prefs.CLIENT_ID, SplashActivity.this) != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //linearLayoutProgress.setVisibility(View.VISIBLE);
                    progressBarAuthentication.setVisibility(View.VISIBLE);
                }
            });
            ApiInterface service = ApiManager.createService(ApiInterface.class);
            //Call<String> callSync = service.getToken(Prefs.getPref(Prefs.CLIENT_ID, SplashActivity.this));
            //Call<String> callAsync = service.getToken(Prefs.getPref(Prefs.CLIENT_ID, SplashActivity.this));
            JsonObject paramObject = new JsonObject();
            paramObject.addProperty("appKey", Prefs.getPref(Prefs.CLIENT_ID, SplashActivity.this));
            Call<ResponseBody> loginCall = service.getToken(paramObject);

            loginCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    //System.out.println("REQUESST CAAAAAAAAAALL : " + bodyToString(call.request()));
                    //System.out.println("REPOOONSE BOODYYY : " + response.body());
                    //System.out.println("REPOOONSE JSON PRETTY : " + new GsonBuilder().setPrettyPrinting().create().toJson(response));
                /*try {
                    System.out.println("REPOOONSE BOODYYY : " + response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                    String token = null;
                    try {
                        token = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    JsonObject x = gson.fromJson(token, JsonObject.class);
                    String dirtyToken = x.get("access_token").toString();
                    dirtyToken = dirtyToken.substring(1, dirtyToken.length() - 1);
                    String bearerToken = "Bearer " + dirtyToken;
                    Prefs.setPref(Prefs.TOKEN, bearerToken, SplashActivity.this);
                    appAuthentication = gson.fromJson(token, AppAuthentication.class);

                    //System.out.println("UUUUUUUUUUUUUUU appAuthentication : "+appAuthentication.getExpires() + "------" + appAuthentication.getAudiance()+"-------------"+appAuthentication.getTokenType()+"-------"+appAuthentication.getExpiresIn()+"---------"+appAuthentication.getRefreshToken());
                    Prefs.setPref(Prefs.AUTHORIZATION, appAuthentication.getTokenType() + " " + appAuthentication.getAccessToken(), SplashActivity.this);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //linearLayoutProgress.setVisibility(View.VISIBLE);
                            progressBarAuthentication.setVisibility(View.GONE);
                        }
                    });

                    getEtablissement2();
                    //getCashiers();
                    goToNextScreen();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //linearLayoutProgress.setVisibility(View.VISIBLE);
                            progressBarAuthentication.setVisibility(View.GONE);
                        }
                    });
                }
            });
        }
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

    private void getEtablissement2() {
        ApiInterface service = ApiManager.createService(ApiInterface.class, Prefs.getPref(Prefs.TOKEN, SplashActivity.this));
        Call<List<Family>> familiesCall = service.getFamilies();

        familiesCall.enqueue(new Callback<List<Family>>() {
            @Override
            public void onResponse(Call<List<Family>> call, Response<List<Family>> response) {
                //System.out.println("BBBBBBBBBBBBBBBBBBBB : " + bodyToString(call.request()));
                //System.out.println("REPOOONSE BOODYYY : " + response.body().size());
                for(Family f: response.body()){
                    familiesList.add(f);
                    System.out.println("AAA : " + f.getLibelle());
                }
            }

            @Override
            public void onFailure(Call<List<Family>> call, Throwable throwable) {
                //System.out.println(throwable);
                //System.out.println("BBBBBBBBBBBBBBBBBBBB : " + bodyToString(call.request()));
            }
        });
    }

    //-----------------------------
    private void handleErrorSubscribeDevice(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBarAuthentication.setVisibility(View.GONE);
                retryRL.setVisibility(View.VISIBLE);
                Toast.makeText(SplashActivity.this,"error Sync",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    private void subscribeDevice() {
        WSRequestsManager wsRequestsManager = new WSRequestsManager();
        String[] path = new String[1];
        path[0] = ConstantsWS.PATH_TOKEN;
        HashMap<String, Object> bodyParams = new HashMap<>();
        //bodyParams.put("grant_type", "client_credentials");
        //bodyParams.put("client_id", "c806ba37-2bbe-4df4-9cf5-476b6eef407a");
        //bodyParams.put("client_secret", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJlMWQwOTBmYy0zNDBlLTRkNTQtYTYwNS1kZGUzYTM3YmIzNjkiLCJDVVNUT01fQ0xBSU1TX0NsaWVudElkIjoiYzgwNmJhMzctMmJiZS00ZGY0LTljZjUtNDc2YjZlZWY0MDdhIiwiQ1VTVE9NX0NMQUlNU19Db21wYW55TmFtZSI6IkFyaW9uLVRlY2giLCJDVVNUT01fQ0xBSU1TX0NvbXBhbnlJZCI6ImNmYmYxYjNhLWUyZDItNGI3NS04ZDM3LThiZTZkODBmZjEzYiIsImV4cCI6MTU2MjY3Njk3NiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo1MDc3Ni8iLCJhdWQiOiJodHRwOi8vbG9jYWxob3N0OjUwNzc2LyJ9.-9RhQQmOF87UnYRv_wPr3BQuRsm2g1x7DzW-xzNXkTU");
        //bodyParams.put("client_id", "09b5b7eb-d107-4487-89ce-b7fb73ab3a58");
        //bodyParams.put("client_secret", "mv80kAtDIn9ie78TFNAepSY1ppAFTY6oBiDJw9mgRIFtj0G1eaLtbLXHDJhtTo2C3eFEW+HQzta0Fdt60l62EKPsauCFARQYi15CW2zOn81LoWT++4jVBQYmf/0KoQagG8QcjL1rmM8ekvZ7BhcaWQCMZnVt0fEIOGlfUPB7BayNlruNP0wi+mW3mXqeONmkL0p0ilwBHQFCmVq543DtUuXqowzXGET6/Vzo5J1wz8x2dMI856xfQs3yAamRkuvUVqkzSrCNmrFYjbarE0khKY+JVpczDETC8BN7i/hUr6JBGWPrBDtHBUKcwX3e05w9uaZ2LZ4f8g/l6U9n6nTO4A==");
        bodyParams.put("appKey", Prefs.getPref(Prefs.CLIENT_ID, SplashActivity.this));

        wsRequestsManager.setIsJson(true);
        wsRequestsManager.setUrlEncoded(false);
        wsRequestsManager.sendRequest(path, null, bodyParams, null, WSRequestsManager.POST, null, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                //DebugLog.d(response);
                System.out.println("LOGIN RESPONSE : "+response);
                if (response != null) {
                    //TODO parse response & save etablissement/depots
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        if (responseObject.has("access_token")) {
                            //Prefs.setPref(Prefs.TOKEN, response, SplashActivity.this);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    linearLayoutProgress.setVisibility(View.VISIBLE);
                                    progressBarAuthentication.setVisibility(View.GONE);
                                }
                            });
                            //synchronize();
                            appAuthentication = gson.fromJson(Prefs.getPref(Prefs.TOKEN, SplashActivity.this), AppAuthentication.class);
                            Prefs.setPref(Prefs.AUTHORIZATION, appAuthentication.getTokenType() + " " + appAuthentication.getAccessToken(), SplashActivity.this);
                            getEtablissement();
                            getCashiers();
                        }else {
                            handleErrorSubscribeDevice();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                       handleErrorSubscribeDevice();
                    }

                }
            }
        }, SplashActivity.this, SplashActivity.this);
    }
    */

    /*
    private void synchronize() {
        appAuthentication = gson.fromJson(Prefs.getPref(Prefs.TOKEN, SplashActivity.this), AppAuthentication.class);
        Prefs.setPref(Prefs.AUTHORIZATION, appAuthentication.getTokenType() + " " + appAuthentication.getAccessToken(), SplashActivity.this);
        *//*getProducts();//OK
        getEtablissement();//Shared Pref
        getInventory();//OK*//*
        getCashiers();//user OK
        *//*getCustomers();//empty API
        getSellers();//OK
        getPrices();//empty API
        getIdCategoriesByIdArticle();//OK
        getStock();//OK
        getDimensions();//OK
        getCategories();//OK
        getSuppliers();//OK*//*
    }
    */

    /*
    private void getProducts() {
        WSRequestsManager wsRequestsManager = new WSRequestsManager();
        String[] path = new String[3];
        path[0] = ConstantsWS.PATH_API;
        path[1] = ConstantsWS.PATH_PRODUCT;
        path[2] = ConstantsWS.PATH_ALL_PRODUCTS;
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", appAuthentication.getTokenType() + " " + appAuthentication.getAccessToken());
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("codeEtablissement", "300");
        wsRequestsManager.sendRequest(path, queryParams, null, null, WSRequestsManager.GET, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                if (response != null) {
                    try {
                        JSONArray jsonArrayResponse = new JSONArray(response);
                        List<Article> articles = new ArrayList<>();
                        if (jsonArrayResponse.length() > 0) {
                            for (int i = 0; i < jsonArrayResponse.length(); i++) {
                                final Article article = gson.fromJson(jsonArrayResponse.getJSONObject(i).toString(), Article.class);

                                articles.add(article);
                            }
                            addArticleDB(articles);
                        }else{
                            //getInventory();
                        }
                        setProgressState(progressProducts, 1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        setProgressState(progressProducts, 2);
                    }
                } else {
                    setProgressState(progressProducts, 2);
                }


            }
        }, SplashActivity.this, SplashActivity.this);
    }
    */

    /*
    private void addArticleDB(final List<Article> articles) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                lafargeDatabase.articleDao().insertAllProducts(articles);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d("*****ROOM SUCCESS*****", "**********articles*********");
                        //getInventory();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("*ERROR ROOM article*", e.getMessage());
                    }
                });
    }
    */

    /*
    private void getEtablissement() {
        //GET /api/etablissement/getEtablissementByCode
        WSRequestsManager wsRequestsManager = new WSRequestsManager();
        String[] path = new String[3];
        path[0] = ConstantsWS.PATH_API;
        path[1] = ConstantsWS.PATH_ETABLISSEMENT;
        path[2] = ConstantsWS.PATH_ETABLISSEMENT_BY_CODE;
        HashMap<String, String> headers = new HashMap<>();
        //TODO setup etablissement code
        headers.put("Authorization", appAuthentication.getTokenType() + " " + appAuthentication.getAccessToken());
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("codeEtablissement", "300");
        wsRequestsManager.sendRequest(path, queryParams, null, null, WSRequestsManager.GET, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                if (response != null) {
                    try {
                        JSONArray jsonArrayResponse = new JSONArray(response);

                        if (jsonArrayResponse != null && jsonArrayResponse.length() > 0) {
                            Prefs.setPref(Prefs.ETABLISSEMENT, jsonArrayResponse.getJSONObject(0).toString(), SplashActivity.this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, SplashActivity.this, SplashActivity.this);
    }
    */

    /*
    private void getCashiers() {
        //GET /api/cashier/getListCashierByCodeEtabCodeDepot
        WSRequestsManager wsRequestsManager = new WSRequestsManager();
        String[] path = new String[3];
        path[0] = ConstantsWS.PATH_API;
        path[1] = ConstantsWS.PATH_CASHIER;
        path[2] = ConstantsWS.PATH_CASHIER_BY_ETAB_USERS;
        HashMap<String, String> headers = new HashMap<>();
        //TODO setup etablissement code
        headers.put("Authorization", appAuthentication.getTokenType() + " " + appAuthentication.getAccessToken());
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("codeEtablissement", "300");

        //queryParams.put("codeDepot", "300");
        wsRequestsManager.sendRequest(path, queryParams, null, null, WSRequestsManager.GET, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                if (response != null) {
                    try {
                        JSONArray jsonArrayResponse = new JSONArray(response);
                        List<User> users = new ArrayList<>();
                        if (jsonArrayResponse.length() > 0) {
                            for (int i = 0; i < jsonArrayResponse.length(); i++) {
                                users.add(gson.fromJson(jsonArrayResponse.getJSONObject(i).toString(), User.class));
                            }
                            addCashiers(users);
                        }

                        setProgressState(progressBarUsers, 1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        setProgressState(progressBarUsers, 2);
                    }
                } else {
                    setProgressState(progressBarUsers, 2);
                }


            }
        }, SplashActivity.this, SplashActivity.this);
    }
    */

    /*
    private void addCashiers(final List<User> users) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                lafargeDatabase.userDao().insertAllUsers(users);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d("*****ROOM SUCCESS*****", "********Cashiers***********");
                        getSellers();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("**ERROR ROOM Cashier**", e.getMessage());
                    }
                });
    }
    */

    /*
    private void getInventory() {
        //GET /api/Inventory/getInventories
        WSRequestsManager wsRequestsManager = new WSRequestsManager();
        String[] path = new String[3];
        path[0] = ConstantsWS.PATH_API;
        path[1] = ConstantsWS.PATH_INVENTORY;
        path[2] = ConstantsWS.PATH_GET_INVENTORIES;
        HashMap<String, String> headers = new HashMap<>();
        //TODO setup etablissement code
        headers.put("Authorization", appAuthentication.getTokenType() + " " + appAuthentication.getAccessToken());
        wsRequestsManager.sendRequest(path, null, null, null, WSRequestsManager.GET, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                if (response != null) {
                    try {
                        JSONArray jsonArrayResponse = new JSONArray(response);

                        if (jsonArrayResponse.length() > 0) {
                            inventoryResponse = jsonArrayResponse;

                            List<Inventory> inventories = new ArrayList<>();
                            for (int i = 0; i < jsonArrayResponse.length(); i++) {
                                inventories.add(gson.fromJson(jsonArrayResponse.getJSONObject(i).toString(), Inventory.class));
                            }
                            addInventories(inventories);
                           // inventoryOk = true;

                            setProgressState(progressInventory, 1);
                            getInventoryProducts();
                        }else {
                            getPrices();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        setProgressState(progressInventory, 2);
                    }
                } else {
                    setProgressState(progressInventory, 2);
                }
            }
        }, SplashActivity.this, SplashActivity.this);
    }
    */

    /*
    private void addInventories(final List<Inventory> inventories) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                lafargeDatabase.inventoryDao().insertAllInventories(inventories);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {

                        Log.d("*****ROOM SUCCESS*****", "*******************");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("*****ERROR ROOM******", e.getMessage());
                    }
                });
    }
    */

    /*
    private void getInventoryProducts() throws JSONException {
        //GET /api/Inventory/getInventoryTransactionsByInventoryCode
        WSRequestsManager wsRequestsManager = new WSRequestsManager();
        String[] path = new String[3];
        path[0] = ConstantsWS.PATH_API;
        path[1] = ConstantsWS.PATH_INVENTORY;
        path[2] = ConstantsWS.PATH_GET_INVENTORY_TRANSACTIONS;
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", appAuthentication.getTokenType() + " " + appAuthentication.getAccessToken());
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("inventoryCode", inventoryResponse.getJSONObject(indexInventory).getString("codeliste"));
        wsRequestsManager.sendRequest(path, queryParams, null, null, WSRequestsManager.GET, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                if (response != null) {
                    try {
                        JSONArray jsonArrayResponse = new JSONArray(response);
                        List<InventoryArticle> inventories = new ArrayList<>();
                        for (int i = 0; i < jsonArrayResponse.length(); i++) {
                            inventories.add(gson.fromJson(jsonArrayResponse.getJSONObject(i).toString(), InventoryArticle.class));
                        }
                        addInventoriesByProduct(inventories);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, SplashActivity.this, SplashActivity.this);

    }
    */

    /*
    private void addInventoriesByProduct(final List<InventoryArticle> inventories) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                lafargeDatabase.inventoryDao().insertAllInventoriesByArticle(inventories);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {

                        Log.d("*****ROOM SUCCESS*****", "*********InventoryArticle**********");
                        getPrices();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ERROR ROOM InventoryArt", e.getMessage());
                    }
                });
    }
    */

    /*
    private void getCustomers() {
        WSRequestsManager wsRequestsManager = new WSRequestsManager();
        String[] path = new String[3];
        path[0] = ConstantsWS.PATH_API;
        path[1] = ConstantsWS.PATH_CUSTOMER;
        path[2] = ConstantsWS.PATH_ALL_CUSTOMERS;
        HashMap<String, String> headers = new HashMap<>();
        //TODO setup etablissement code
        headers.put("Authorization", appAuthentication.getTokenType() + " " + appAuthentication.getAccessToken());
        HashMap<String, String> queryParams = new HashMap<>();
        //queryParams.put("codeEtablissement", "300");
        wsRequestsManager.sendRequest(path, queryParams, null, null, WSRequestsManager.GET, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                //DebugLog.d("getCustomers --> " + response);
                if (response != null) {
                    try {
                        JSONArray jsonArrayResponse = new JSONArray(response);
                        if (jsonArrayResponse.length() > 0) {
                            List<Customer> customers = new ArrayList<>();

                            for (int i = 0; i < jsonArrayResponse.length(); i++) {
                                customers.add(gson.fromJson(jsonArrayResponse.getJSONObject(i).toString(), Customer.class));
                            }
                            addCustomers(customers);
                        }else{
                            getProducts();
                        }

                        setProgressState(progressClients, 1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        setProgressState(progressClients, 2);
                    }
                } else {
                    setProgressState(progressClients, 2);
                }
            }
        }, SplashActivity.this, SplashActivity.this);
    }
    */

    /*
    private void addCustomers(final List<Customer> customers) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                lafargeDatabase.customerDao().deleteAndInsertCustomers(customers);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {

                        Log.d("*****ROOM SUCCESS*****", "*********Customers**********");
                        getProducts();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ERROR ROOM Customers", e.getMessage());
                    }
                });
    }
    */

    /*
    private void getSellers() {
        //GET /api/seller/getListSellerByCodeEtablissement
        setProgressState(progressSeller, 0);
        WSRequestsManager wsRequestsManager = new WSRequestsManager();
        String[] path = new String[3];
        path[0] = ConstantsWS.PATH_API;
        path[1] = ConstantsWS.PATH_SELLER;
        path[2] = ConstantsWS.PATH_SELLER_BY_ETAB;
        HashMap<String, String> headers = new HashMap<>();
        //TODO setup etablissement code
        headers.put("Authorization", appAuthentication.getTokenType() + " " + appAuthentication.getAccessToken());
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("codeEtablissement", "300");
        wsRequestsManager.sendRequest(path, queryParams, null, null, WSRequestsManager.GET, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                //DebugLog.d(response);
                if (response != null) {
                    try {
                        JSONArray jsonArrayResponse = new JSONArray(response);
                        List<Seller> sellers = new ArrayList<>();
                        if (jsonArrayResponse.length() > 0) {
                            //      dbTableHelper.addTableToDb(Constants.TBL_SELLERS, jsonArrayResponse.getJSONObject(0), "");
                            for (int i = 0; i < jsonArrayResponse.length(); i++) {
                                sellers.add(gson.fromJson(jsonArrayResponse.getJSONObject(i).toString(), Seller.class));
                            }
                            addSellers(sellers);
                        }
                        setProgressState(progressSeller, 1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        setProgressState(progressClients, 2);
                    }
                } else {
                    setProgressState(progressClients, 2);
                }
            }
        }, SplashActivity.this, SplashActivity.this);
    }
    */

    /*
    private void addSellers(final List<Seller> sellers) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                lafargeDatabase.sellerDao().insertAllSellers(sellers);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d("*****ROOM SUCCESS*****", "*******************");
                        getCustomers();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("*****ERROR ROOM******", e.getMessage());
                    }
                });
    }
    */

    /*
    private void getPrices() {
        WSRequestsManager wsRequestsManager = new WSRequestsManager();
        String[] path = new String[3];
        path[0] = ConstantsWS.PATH_API;
        path[1] = ConstantsWS.PATH_TARIF;
        path[2] = ConstantsWS.PATH_GET;
        HashMap<String, String> headers = new HashMap<>();
        //TODO setup token & etablissement code
        headers.put("Authorization", appAuthentication.getTokenType() + " " + appAuthentication.getAccessToken());
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("codeEtablissement", "300");
        wsRequestsManager.sendRequest(path, queryParams, null, null, WSRequestsManager.GET, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                if (response != null) {
                    try {
                        JSONArray jsonArrayResponse = new JSONArray(response);
                        List<Tarif> tarifs = new ArrayList<>();

                        if (jsonArrayResponse.length() > 0) {
                            for (int i = 0; i < jsonArrayResponse.length(); i++) {

                                tarifs.add(gson.fromJson(jsonArrayResponse.getJSONObject(i).toString(), Tarif.class));
                            }
                            addTarifsDB(tarifs);
                        }else {
                            getStock();
                        }

                        setProgressState(progressPrices, 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        setProgressState(progressPrices, 2);
                    }
                } else {
                    setProgressState(progressPrices, 2);
                }

            }
        }, SplashActivity.this, SplashActivity.this);
    }
    */

    /*
    private void addTarifsDB(final List<Tarif> tarifs) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                lafargeDatabase.tarifDao().insertAllTrifs(tarifs);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d("*****ROOM SUCCESS*****", "**********Tarif*********");
                        getStock();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("***ERROR ROOM Tarif***", e.getMessage());
                    }
                });
    }
    */

    /*
    private void getIdCategoriesByIdArticle() {
        WSRequestsManager wsRequestsManager = new WSRequestsManager();
        //GET /api/article/getIdCategoriesByIdArticle
        String[] path = new String[3];
        path[0] = ConstantsWS.PATH_API;
        path[1] = ConstantsWS.PATH_ARTICLE;
        path[2] = ConstantsWS.PATH_GET_ID_CATEGORY_BY_ID_PRODUCT;
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", appAuthentication.getTokenType() + " " + appAuthentication.getAccessToken());

        wsRequestsManager.sendRequest(path, null, null, null, WSRequestsManager.GET, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                //DebugLog.d(response);
                if (response != null) {
                    try {
                        JSONArray jsonArrayResponse = new JSONArray(response);
                        List<CategoryByArticle> categoryByArticles = new ArrayList<>();
                        if (jsonArrayResponse.length() > 0) {
                            for (int i = 0; i < jsonArrayResponse.length(); i++) {
                                categoryByArticles.add(gson.fromJson(jsonArrayResponse.getJSONObject(i).toString(), CategoryByArticle.class));
                            }
                            addCatByArticleDB(categoryByArticles);
                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Prefs.setBooleanPref(Prefs.IS_SYNCHRONIZED, true, SplashActivity.this);
                                    goToNextScreen();

                                }
                            });
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }


            }
        }, SplashActivity.this, SplashActivity.this);
    }
    */

    /*
    private void addCatByArticleDB(final List<CategoryByArticle> categoryByArticles) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                lafargeDatabase.categoryDao().insertCategoryByArticle(categoryByArticles);
            }
        }).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new CompletableObserver() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onComplete() {
                    Log.d("*****ROOM SUCCESS*****", "**********Tarif*********");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Prefs.setBooleanPref(Prefs.IS_SYNCHRONIZED, true, SplashActivity.this);
                            goToNextScreen();

                        }
                    });
                }

                @Override
                public void onError(Throwable e) {
                    Log.d("***ERROR ROOM Tarif***", e.getMessage());
                }
            });
    }
    */

    /*
    private void getStock() {
        setProgressState(progressStock, 0);
        WSRequestsManager wsRequestsManager = new WSRequestsManager();
        String[] path = new String[3];
        path[0] = ConstantsWS.PATH_API;
        path[1] = ConstantsWS.PATH_STOCK;
        path[2] = ConstantsWS.PATH_ALL_STOCK;
        HashMap<String, String> headers = new HashMap<>();
        //TODO setup depot & etablissement code
        headers.put("Authorization", appAuthentication.getTokenType() + " " + appAuthentication.getAccessToken());
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("codeEtablissement", "300");
        queryParams.put("codeDepot", "300");
        wsRequestsManager.sendRequest(path, queryParams, null, null, WSRequestsManager.GET, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                //DebugLog.d(response);
                if (response != null) {
                    try {
                        JSONArray jsonArrayResponse = new JSONArray(response);
                        List<Stock> stocks = new ArrayList<>();

                        if (jsonArrayResponse.length() > 0) {
                            for (int i = 0; i < jsonArrayResponse.length(); i++) {
                                stocks.add(gson.fromJson(jsonArrayResponse.getJSONObject(i).toString(), Stock.class));
                            }
                            addStockDB(stocks);
                        }else {
                            getDimensions();
                        }

                        setProgressState(progressStock, 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        setProgressState(progressStock, 2);
                    }
                } else {
                    setProgressState(progressStock, 2);
                }

            }
        }, SplashActivity.this, SplashActivity.this);
    }
    */

    /*
    private void addStockDB(final List<Stock> stocks) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                lafargeDatabase.stockDao().insertStock(stocks);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d("*****ROOM SUCCESS*****", "**********Stock*********");
                        getDimensions();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("***ERROR ROOM Stock***", e.getMessage());
                    }
                });
    }
    */

    /*
    private void getDimensions() {
        WSRequestsManager wsRequestsManager = new WSRequestsManager();
        String[] path = new String[3];
        path[0] = ConstantsWS.PATH_API;
        path[1] = ConstantsWS.PATH_DIMENSION;
        path[2] = ConstantsWS.PATH_ALL;
        HashMap<String, String> headers = new HashMap<>();
        //TODO setup  etablissement code
        headers.put("Authorization", appAuthentication.getTokenType() + " " + appAuthentication.getAccessToken());

        wsRequestsManager.sendRequest(path, null, null, null, WSRequestsManager.GET, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                if (response != null) {
                    try {
                        JSONArray jsonArrayResponse = new JSONArray(response);
                        List<Dimension> dimensions = new ArrayList<>();
                        if (jsonArrayResponse.length() > 0) {
                            for (int i = 0; i < jsonArrayResponse.length(); i++) {
                                dimensions.add(gson.fromJson(jsonArrayResponse.getJSONObject(i).toString(), Dimension.class));
                            }
                            addDimensionDB(dimensions);
                        }else {
                            getCategories();
                        }


                        setProgressState(progressDimensions, 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        setProgressState(progressDimensions, 2);
                    }
                } else {
                    setProgressState(progressDimensions, 2);
                }


            }
        }, SplashActivity.this, SplashActivity.this);
    }
    */

    /*
    private void addDimensionDB(final List<Dimension> dimensions) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                lafargeDatabase.dimensionDao().insertDimension(dimensions);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d("*****ROOM SUCCESS*****", "**********Dimensions*********");
                        getCategories();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("*ERROR ROOM Dimensions*", e.getMessage());
                    }
                });
    }
    */

    /*
    private void getSuppliers() {
        // GET /api/Supplier/GetSupplierList
        WSRequestsManager wsRequestsManager = new WSRequestsManager();
        String[] path = new String[3];
        path[0] = ConstantsWS.PATH_API;
        path[1] = ConstantsWS.PATH_SUPPLIER;
        path[2] = ConstantsWS.PATH_SUPPLIER_LIST;
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", appAuthentication.getTokenType() + " " + appAuthentication.getAccessToken());

        wsRequestsManager.sendRequest(path, null, null, null, WSRequestsManager.GET, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                if (response != null) {
                    try {
                        JSONArray jsonArrayResponse = new JSONArray(response);
                        List<Supplier> suppliers = new ArrayList<>();
                        if (jsonArrayResponse.length() > 0) {
                            for (int i = 0; i < jsonArrayResponse.length(); i++) {
                                suppliers.add(gson.fromJson(jsonArrayResponse.getJSONObject(i).toString(), Supplier.class));
                            }
                            addSupplierDB(suppliers);
                        }else {
                            getIdCategoriesByIdArticle();
                        }


                        setProgressState(progressSuppliers, 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        setProgressState(progressSuppliers, 2);
                    }
                } else {
                    setProgressState(progressSuppliers, 2);
                }


            }
        }, SplashActivity.this, SplashActivity.this);
    }
    */

    /*
    private void addSupplierDB(final List<Supplier> suppliers) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                lafargeDatabase.supplierDao().insertSupplier(suppliers);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d("*****ROOM SUCCESS*****", "**********Supplier*********");
                      getIdCategoriesByIdArticle();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("*ERROR ROOM Supplier*", e.getMessage());
                    }
                });
    }
    */

    /*
    private void getCategories() {
        //GET /api/article/level/all
        WSRequestsManager wsRequestsManager = new WSRequestsManager();
        String[] path = new String[4];
        path[0] = ConstantsWS.PATH_API;
        path[1] = ConstantsWS.PATH_ARTICLE;
        path[2] = ConstantsWS.PATH_LEVEL;
        path[3] = ConstantsWS.PATH_ALL;
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", appAuthentication.getTokenType() + " " + appAuthentication.getAccessToken());

        wsRequestsManager.sendRequest(path, null, null, null, WSRequestsManager.GET, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                //DebugLog.d(response);
                if (response != null) {
                    try {
                        JSONArray jsonArrayResponse = new JSONArray(response);
                        List<Category> categories = new ArrayList<>();
                        if (jsonArrayResponse.length() > 0) {
                            for (int i = 0; i < jsonArrayResponse.length(); i++) {
                                //         dbTableHelper.saveDataToBD(jsonArrayResponse.getJSONObject(i), Constants.TBL_CATEGORY, null);
                                categories.add(gson.fromJson(jsonArrayResponse.getJSONObject(i).toString(), Category.class));
                            }
                            addCategoryDB(categories);
                        }else {
                            //getSuppliers();
                        }

                        setProgressState(progressCategories, 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        setProgressState(progressCategories, 2);
                    }
                } else {
                    setProgressState(progressCategories, 2);
                }


            }
        }, SplashActivity.this, SplashActivity.this);
    }
    */

    /*
    private void addCategoryDB(final List<Category> categories) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                lafargeDatabase.categoryDao().insertCategory(categories);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d("*****ROOM SUCCESS*****", "**********Categoty*********");
                        //getSuppliers();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("*ERROR ROOM Category*", e.getMessage());
                    }
                });
    }
    */



    private void goToNextScreen() {
        //startActivity(new Intent(SplashActivity.this, Prefs.getBooleanPref(Prefs.IS_CONNECTED, SplashActivity.this, false) ? MainActivity.class : LoginActivity.class));
        startActivity(new Intent(SplashActivity.this, Prefs.getBooleanPref(Prefs.IS_CONNECTED, SplashActivity.this, false) ? MainActivity.class : ChooseStoreActivity.class));
        //startActivity(new Intent(SplashActivity.this, TestActivity.class));
        finish();
    }

    private void setProgressState(final ToggleButtonLayout progressbar, final int state) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(state == 2){
                    retryRL.setVisibility(View.VISIBLE);
                }
                progressbar.updateView(state);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, R.string.exit_app_forbidden, Toast.LENGTH_SHORT).show();
    }

}
