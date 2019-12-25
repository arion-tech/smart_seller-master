package io.mintit.lafarge.services;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.mintit.lafarge.DBRoom.DBLafarge;
import io.mintit.lafarge.global.ConstantsWS;
import io.mintit.lafarge.model.AppAuthentication;
import io.mintit.lafarge.model.CategoryByArticle;
import io.mintit.lafarge.model.Customer;
import io.mintit.lafarge.model.Inventory;
import io.mintit.lafarge.model.InventoryArticle;
import io.mintit.lafarge.model.Stock;
import io.mintit.lafarge.model.Tarif;
import io.mintit.lafarge.utils.DebugLog;
import io.mintit.lafarge.utils.Prefs;
import io.mintit.lafarge.utils.WSRequestsManager;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

import static io.mintit.lafarge.global.Constants.DATABASE_FILENAME;

public class SynchronisationManager {
    Context context;
    Activity activity;
    private AppAuthentication appAuthentication;
    private Gson gson = new Gson();
    private boolean stocksOk;
    private boolean customersOk;
    private boolean inventoryOk;
    private boolean productsOK;
    private boolean pricesOk;
    private JSONArray inventoryResponse;
    private int indexInventory;
    private boolean idCategoriesOk;
    private DBLafarge lafargeDatabase;

    public SynchronisationManager(Context context, Activity activity) {
        this.context = context;

        this.activity = activity;
        lafargeDatabase = Room.databaseBuilder(context,
                DBLafarge.class, DATABASE_FILENAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public void subscribeDevice() {
        WSRequestsManager wsRequestsManager = new WSRequestsManager();
        String[] path = new String[1];
        path[0] = ConstantsWS.PATH_TOKEN;
        HashMap<String, String> headers = new HashMap<>();
        // headers.put("content-type", "application/x-www-form-urlencoded");
        HashMap<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("grant_type", "client_credentials");
        bodyParams.put("client_id", "4e7a0567-f7aa-45fa-884a-24cf4d1f1feb");
        //   bodyParams.put("client_id", "09b5b7eb-d107-4487-89ce-b7fb73ab3a58");
        bodyParams.put("client_secret", "mv80kAtDIn9ie78TFNAepSY1ppAFTY6oBiDJw9mgRIFtj0G1eaLtbLXHDJhtTo2C3eFEW+HQzta0Fdt60l62EKPsauCFARQYi15CW2zOn81LoWT++4jVBQYmf/0KoQagG8QcjL1rmM8ekvZ7BhcaWQCMZnVt0fEIOGlfUPB7BayNlruNP0wi+mW3mXqeONmkL0p0ilwBHQFCmVq543DtUuXqowzXGET6/Vzo5J1wz8x2dMI856xfQs3yAamRkuvUVqkzSrCNmrFYjbarE0khKY+JVpczDETC8BN7i/hUr6JBGWPrBDtHBUKcwX3e05w9uaZ2LZ4f8g/l6U9n6nTO4A==");
        wsRequestsManager.setIsJson(false);
        wsRequestsManager.sendRequest(path, null, bodyParams, null, WSRequestsManager.POST, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                DebugLog.d(response);
                if (response != null) {
                    //TODO parse response & save etablissement/depots
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        if (responseObject.has("access_token")) {
                            Prefs.setPref(Prefs.TOKEN, response, context);

                            synchronize();
                        } else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, context, activity);

    }

    private void getStock() {
        WSRequestsManager wsRequestsManager = new WSRequestsManager();
        String[] path = new String[3];
        path[0] = ConstantsWS.PATH_API;
        path[1] = ConstantsWS.PATH_STOCK;
        path[2] = ConstantsWS.PATH_ALL_STOCK;
        HashMap<String, String> headers = new HashMap<>();
        //TODO setup depot & etablissement code
        headers.put("Authorization", appAuthentication.getTokenType() + " " + appAuthentication.getAccessToken());
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("codeEtablissement", "200");
        queryParams.put("codeDepot", "200");
        wsRequestsManager.sendRequest(path, queryParams, null, null, WSRequestsManager.GET, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                DebugLog.d(response);
                if (response != null) {
                    try {
                        JSONArray jsonArrayResponse = new JSONArray(response);

                        List<Stock> stocks = new ArrayList<>();

                        if (jsonArrayResponse.length() > 0) {
                            for (int i = 0; i < jsonArrayResponse.length(); i++) {
                                stocks.add(gson.fromJson(jsonArrayResponse.getJSONObject(i).toString(), Stock.class));
                            }
                            deleteAndInsertStockDB(stocks);
                        }

                            stocksOk = true;
                            checkNext();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, context, activity);
    }

    private void deleteAndInsertStockDB(final List<Stock> stocks) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                lafargeDatabase.stockDao().deleteAndInsertStock(stocks);
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
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("***ERROR ROOM Stock***", e.getMessage());
                    }
                });
    }


    private void checkNext() {
        if (idCategoriesOk && inventoryOk && productsOK && stocksOk && customersOk && pricesOk) {
            // EventBus.getDefault().post(new ReleaseScreenEvent());
        }
    }


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
        queryParams.put("codeEtablissement", "101");
        wsRequestsManager.sendRequest(path, queryParams, null, null, WSRequestsManager.GET, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                //DebugLog.d("getCustomers --> " + response);
                if (response != null) {
                    if (response != null) {
                        try {
                            JSONArray jsonArrayResponse = new JSONArray(response);


                                if (jsonArrayResponse.length() > 0) {
                                    List<Customer> customers = new ArrayList<>();

                                    for (int i = 0; i < jsonArrayResponse.length(); i++) {
                                        customers.add(gson.fromJson(jsonArrayResponse.getJSONObject(i).toString(),Customer.class));
                                    }
                                    deleteAndInsertCustomers(customers);
                                }

                                customersOk = true;
                                checkNext();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }, context, null);
    }

    private void deleteAndInsertCustomers(final List<Customer> customers) {
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

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ERROR ROOM Customers", e.getMessage());
                    }
                });
    }


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
                //DebugLog.d("getInventory " + response);
                if (response != null) {
                    try {
                        JSONArray jsonArrayResponse = new JSONArray(response);

                        if (jsonArrayResponse != null) {
                            if (jsonArrayResponse.length() > 0) {

                                List<Inventory> inventories = new ArrayList<>();
                                for (int i = 0; i < jsonArrayResponse.length(); i++) {
                                    inventories.add(gson.fromJson(jsonArrayResponse.getJSONObject(i).toString(), Inventory.class));
                                }
                                addInventories(inventories);

                                inventoryResponse = jsonArrayResponse;
                                if (inventoryResponse.length() > 0) {
                                    getInventoryProducts();
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, context, null);
    }


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

    JSONArray inventoryProductsList = new JSONArray();

    private void getInventoryProducts() throws JSONException {
        //DebugLog.d(inventoryResponse.length() + "");

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
        }, context, null);
    }

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
                        inventoryOk = true;
                        checkNext();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ERROR ROOM InventoryArt", e.getMessage());
                    }
                });
    }

/*

    private void getProducts() {
        WSRequestsManager wsRequestsManager = new WSRequestsManager();
        String[] path = new String[3];
        path[0] = ConstantsWS.PATH_API;
        path[1] = ConstantsWS.PATH_PRODUCT;
        path[2] = ConstantsWS.PATH_ALL_PRODUCTS;
        HashMap<String, String> headers = new HashMap<>();
        //TODO setup etablissement & depots codes
        headers.put("Authorization", appAuthentication.getTokenType() + " " + appAuthentication.getAccessToken());
        HashMap<String, Object> bodyParams = new HashMap<>();
        JSONArray listDepots = new JSONArray();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", "0");
            jsonObject.put("codeDepot", "200");
            listDepots.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bodyParams.put("codeEtablissement", "200");
        bodyParams.put("Depots", listDepots);
        wsRequestsManager.sendRequest(path, null, bodyParams, null, WSRequestsManager.POST, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                //DebugLog.d(response);
                if (response != null) {

                    try {
                        JSONArray jsonArrayResponse = new JSONArray(response);

                        if (jsonArrayResponse != null) {
                            if (jsonArrayResponse.length() > 0) {
                                dbTableHelper.dropTable(Constants.TBL_PRODUCT);
                                dbTableHelper.addTableToDb(Constants.TBL_PRODUCT, jsonArrayResponse.getJSONObject(0), "");
                                for (int i = 0; i < jsonArrayResponse.length(); i++) {
                                    dbTableHelper.saveDataToBD(jsonArrayResponse.getJSONObject(i), Constants.TBL_PRODUCT, null);
                                }
                                productsOK = true;
                            }

                            checkNext();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, context, null);
    }
*/

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
        queryParams.put("codeEtablissement", "200");
        wsRequestsManager.sendRequest(path, queryParams, null, null, WSRequestsManager.GET, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                //DebugLog.d(response);
                if (response != null) {
                    try {
                        JSONArray jsonArrayResponse = new JSONArray(response);
                        List<Tarif> tarifs = new ArrayList<>();
                        if (jsonArrayResponse.length() > 0) {
                            for (int i = 0; i < jsonArrayResponse.length(); i++) {
                                tarifs.add(gson.fromJson(jsonArrayResponse.getJSONObject(i).toString(), Tarif.class));
                            }
                            deleteAndInsertTarifsDB(tarifs);
                        }
                        pricesOk = true;
                        checkNext();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, context, null);
    }


    private void deleteAndInsertTarifsDB(final List<Tarif> tarifs) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                lafargeDatabase.tarifDao().deleteAndInsertTarif(tarifs);
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
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("***ERROR ROOM Tarif***", e.getMessage());
                    }
                });
    }

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
                        }

                        idCategoriesOk = true;
                        checkNext();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }
        }, context, null);
    }
    private void addCatByArticleDB(final List<CategoryByArticle> categoryByArticles) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                lafargeDatabase.categoryDao().deleteAndInsertCategories(categoryByArticles);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d("*****ROOM SUCCESS*****", "**********cat*********");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("***ERROR ROOM cat***", e.getMessage());
                    }
                });
    }

    private void synchronize() {
        appAuthentication = gson.fromJson(Prefs.getPref(Prefs.TOKEN, context), AppAuthentication.class);
        Prefs.setPref(Prefs.AUTHORIZATION, appAuthentication.getTokenType() + " " + appAuthentication.getAccessToken(), context);
        //getProducts(); todo fix after get product fixed
        getInventory();
        getCustomers();
        getPrices();
        getIdCategoriesByIdArticle();
        getStock();
    }
}
