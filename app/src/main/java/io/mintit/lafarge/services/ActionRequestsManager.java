package io.mintit.lafarge.services;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import io.mintit.lafarge.DBRoom.DBLafarge;
import io.mintit.lafarge.R;
import io.mintit.lafarge.app.LafargeApp;
import io.mintit.lafarge.events.UpdateCartEvent;
import io.mintit.lafarge.events.UpdateCustomerInfoEvent;
import io.mintit.lafarge.global.Constants;
import io.mintit.lafarge.global.ConstantsWS;
import io.mintit.lafarge.model.ActionRequest;
import io.mintit.lafarge.model.Customer;
import io.mintit.lafarge.model.SalesDocument;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.ui.fragment.CustomerDirectoryFragment;
import io.mintit.lafarge.utils.DebugLog;
import io.mintit.lafarge.utils.Prefs;
import io.mintit.lafarge.utils.Utils;
import io.mintit.lafarge.utils.WSRequestsManager;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static io.mintit.lafarge.global.Constants.DATABASE_FILENAME;

public class ActionRequestsManager {

    Gson gson = new GsonBuilder().serializeNulls().create();
    private Context context;
    boolean customersOk, salesOk, inventoriesOk, inventoryProductsOk;
    private JSONArray jsonArrayCustomers = new JSONArray();
    private JSONArray jsonArrayInventories = new JSONArray();
    private JSONArray jsonArrayInventoriesTransaction = new JSONArray();
    private JSONArray jsonArraySales = new JSONArray();
    private DBLafarge dbLafarge;
    private JSONArray jsonArray = new JSONArray();


    public ActionRequestsManager(Context context) {
        this.context = context;


        dbLafarge = Room.databaseBuilder(context,
                DBLafarge.class, DATABASE_FILENAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public void createNewActionRequest(Object object, String actionType, String objectType) {
        ActionRequest actionRequest = new ActionRequest();
        actionRequest.setObject(gson.toJson(object));
        actionRequest.setActionType(actionType);
        actionRequest.setObjectType(objectType);
        actionRequest.setReference(UUID.randomUUID().toString());
        actionRequest.setTransmited(false);
        try {
            actionRequest.setCreationDate(Utils.getCurrentFullDate());
            saveActionRequest(actionRequest);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    private void saveActionRequest(final ActionRequest request) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                dbLafarge.actionRequestDao().insertActionRequest(request);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d("SUCCESS ROOM", "Success Add ActionRequest");
                        getActionRequest(request.getReference());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ERROR ROOM addActionreq", e.getMessage());
                    }
                });
    }


    @SuppressLint("CheckResult")
    public void getActionRequest(final String ref){
        dbLafarge.actionRequestDao().getAllActionRequestsByRef(ref)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ActionRequest>>() {
                    @Override
                    public void accept(List<ActionRequest> actionRequests) throws JSONException {
                        if(actionRequests.size()>0 && !actionRequests.get(0).isTransmited()){
                            actionRequests.get(0).setRequestNumber(actionRequests.get(0).getId());
                            jsonArray.put(new JSONObject(gson.toJson(actionRequests.get(0))));
                            pushActionRequest();
                        }
                    }
                });
    }

    private boolean checkReadyToPush(ActionRequest request) {
        if (!Utils.isNetworkAvailable(context)) {
            return false;
        }
        if (request.getObjectType().equals(Constants.ACTION_REQUEST_SALES_DOCUMENT)) {
            SalesDocument requestJson = gson.fromJson(gson.toJson(request), SalesDocument.class);
            if (TextUtils.isEmpty(requestJson.getCustomerId())) {
                return false;
            }
        }
        return true;
    }


    public void startSynchronisation() {
        loadActionRequests(Constants.ACTION_REQUEST_CUSTOMER);
        loadActionRequests(Constants.ACTION_REQUEST_INVENTORY);
        loadActionRequests(Constants.ACTION_REQUEST_INVENTORY_TRANSACTION);

    }

    @SuppressLint("CheckResult")
    private void loadActionRequests(final String objectType){
        dbLafarge.actionRequestDao().getAllActionRequestsByObjectType(objectType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ActionRequest>>() {
                    @Override
                    public void accept(List<ActionRequest> actionRequests) throws JSONException {
                        if(actionRequests.size()>0){
                            JSONArray jsonArray = new JSONArray();
                            for(int i=0; i<actionRequests.size(); i++) {
                                jsonArray.put(new JSONObject().put("", new Gson().toJson(actionRequests.get(i))));
                            }
                            switch (objectType) {
                                case Constants.ACTION_REQUEST_CUSTOMER:
                                    jsonArrayCustomers = jsonArray;
                                    pushCustomers();
                                    break;
                                case Constants.ACTION_REQUEST_INVENTORY:
                                    jsonArrayInventories = jsonArray;
                                    pushInventories();
                                    break;
                                case Constants.ACTION_REQUEST_INVENTORY_TRANSACTION:
                                    jsonArrayInventoriesTransaction = jsonArray;
                                    pushInventoryTransaction();
                                    break;
                                case Constants.ACTION_REQUEST_SALES_DOCUMENT:
                                    jsonArraySales = jsonArray;
                                    break;
                            }

                        }
                    }
                });
    }





    private void pushInventoryTransaction() throws JSONException {
        DebugLog.d(jsonArrayInventories.toString());
        //TODO send list of requests
        //TODO setup push API
        //POST /api/sync/actionRequest/list

        WSRequestsManager wsRequestsManager = new WSRequestsManager();
        String[] path = new String[4];
        path[0] = ConstantsWS.PATH_API;
        path[1] = ConstantsWS.PATH_SYNC;
        path[2] = ConstantsWS.PATH_ACTIONREQUEST;
        path[3] = ConstantsWS.PATH_LIST;

        HashMap<String, String> headers = new HashMap<>();
        //TODO setup etablissement code
        headers.put("Authorization", Prefs.getPref(Prefs.AUTHORIZATION, context));
        String array = jsonArrayInventories.toString();
        array = array.replaceAll("\"null\"", "null");
        jsonArrayInventories = new JSONArray(array);
        DebugLog.d("pushSynchronisation " + jsonArrayInventories.toString());
        wsRequestsManager.sendRequest(path, null, null, jsonArrayInventories, WSRequestsManager.POST, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                DebugLog.d("pushSynchronisation " + response);
                if (response != null) {
                    try {
                        JSONArray respArray = new JSONArray(response);
                        if (respArray.length() > 0) {

                            handleResponse(respArray, jsonArrayInventories);
                            inventoryProductsOk = true;
                            if (Utils.isNetworkAvailable(context) && customersOk && salesOk && inventoriesOk && inventoryProductsOk) {
                                //TODO update sent actions in local db transmitted status
                                new SynchronisationManager(context, null).subscribeDevice();
                            }
                            generateErrorReport();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, context, null);
    }

    private void pushInventories() throws JSONException {
        DebugLog.d(jsonArrayInventoriesTransaction.toString());
        //TODO send list of requests
        //TODO setup push API
        //POST /api/sync/actionRequest/list

        WSRequestsManager wsRequestsManager = new WSRequestsManager();
        String[] path = new String[4];
        path[0] = ConstantsWS.PATH_API;
        path[1] = ConstantsWS.PATH_SYNC;
        path[2] = ConstantsWS.PATH_ACTIONREQUEST;
        path[3] = ConstantsWS.PATH_LIST;

        HashMap<String, String> headers = new HashMap<>();
        //TODO setup etablissement code
        headers.put("Authorization", Prefs.getPref(Prefs.AUTHORIZATION, context));
        String array = jsonArrayInventories.toString();
        array = array.replaceAll("\"null\"", "null");
        jsonArrayInventories = new JSONArray(array);
        DebugLog.d("pushSynchronisation " + jsonArrayInventories.toString());
        wsRequestsManager.sendRequest(path, null, null, jsonArrayInventories, WSRequestsManager.POST, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                DebugLog.d("pushSynchronisation " + response);
                if (response != null) {
                    try {
                        JSONArray respArray = new JSONArray(response);
                        if (respArray.length() > 0) {

                            handleResponse(respArray, jsonArrayInventories);
                            inventoriesOk = true;
                            if (Utils.isNetworkAvailable(context) && customersOk && salesOk && inventoriesOk && inventoryProductsOk) {
                                //TODO update sent actions in local db transmitted status
                                new SynchronisationManager(context, null).subscribeDevice();
                            }
                            generateErrorReport();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        }, context, null);
    }

    private void pushCustomers() throws JSONException {
        DebugLog.d(jsonArrayCustomers.toString());
        //TODO send list of requests
        //TODO setup push API
        //POST /api/sync/actionRequest/list

        WSRequestsManager wsRequestsManager = new WSRequestsManager();
        String[] path = new String[4];
        path[0] = ConstantsWS.PATH_API;
        path[1] = ConstantsWS.PATH_SYNC;
        path[2] = ConstantsWS.PATH_ACTIONREQUEST;
        path[3] = ConstantsWS.PATH_LIST;

        HashMap<String, String> headers = new HashMap<>();
        //TODO setup etablissement code
        headers.put("Authorization", Prefs.getPref(Prefs.AUTHORIZATION, context));
        String array = jsonArrayCustomers.toString();
        array = array.replaceAll("\"null\"", "null");
        jsonArrayCustomers = new JSONArray(array);
        DebugLog.d("pushSynchronisation " + jsonArrayCustomers.toString());
        wsRequestsManager.sendRequest(path, null, null, jsonArrayCustomers, WSRequestsManager.POST, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                DebugLog.d("pushSynchronisation " + response);
                if (response != null) {
                    try {
                        JSONArray respArray = new JSONArray(response);
                        if (respArray.length() > 0) {
                            //TODO Update local salesdocumets having temporary customer refence

                            loadActionRequests(Constants.ACTION_REQUEST_SALES_DOCUMENT);

                            for (int i = 0; i < jsonArraySales.length(); i++) {
                                if (TextUtils.isEmpty(jsonArraySales.getJSONObject(i).getString("customerId"))) {
                                    for (int j = 0; j < respArray.length(); j++) {
                                        if (respArray.getJSONObject(j).getString("reference").equals(jsonArraySales.getJSONObject(i).getString("customerReference")) && !TextUtils.isEmpty(respArray.getJSONObject(j).getString("customerId"))) {
                                            jsonArraySales.getJSONObject(i).put("customerId", respArray.getJSONObject(j).getString("customerId"));
                                         //   dbTableHelper.updateDataInDB(jsonArraySales.getJSONObject(i), Constants.TBL_ACTION_REQUESTS, "requestNumber", false, "");
                                            break;
                                        }
                                    }
                                }
                            }

                            pushSales();
                            customersOk = true;
                            handleResponse(respArray, jsonArrayCustomers);
                            if (Utils.isNetworkAvailable(context) && customersOk && salesOk && inventoriesOk && inventoryProductsOk) {
                                //TODO update sent actions in local db transmitted status
                                new SynchronisationManager(context, null).subscribeDevice();
                            }
                            generateErrorReport();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        }, context, null);
    }

    private void pushSales() throws JSONException {
        DebugLog.d(jsonArraySales.toString());
        //TODO send list of requests
        //TODO setup push API
        //POST /api/sync/actionRequest/list
        JSONArray salesTemp = new JSONArray();
        for (int i = 0; i < jsonArraySales.length(); i++) {
            ActionRequest request = gson.fromJson(jsonArraySales.getJSONObject(i).toString(), ActionRequest.class);
            if (checkReadyToPush(request)) {
                salesTemp.put(new JSONObject(gson.toJson(request)));
            }
        }

        jsonArraySales = salesTemp;
        WSRequestsManager wsRequestsManager = new WSRequestsManager();
        String[] path = new String[4];
        path[0] = ConstantsWS.PATH_API;
        path[1] = ConstantsWS.PATH_SYNC;
        path[2] = ConstantsWS.PATH_ACTIONREQUEST;
        path[3] = ConstantsWS.PATH_LIST;

        HashMap<String, String> headers = new HashMap<>();
        //TODO setup etablissement code
        headers.put("Authorization", Prefs.getPref(Prefs.AUTHORIZATION, context));
        String array = jsonArraySales.toString();
        array = array.replaceAll("\"null\"", "null");
        jsonArraySales = new JSONArray(array);
        DebugLog.d("pushSynchronisation " + jsonArraySales.toString());
        wsRequestsManager.sendRequest(path, null, null, jsonArraySales, WSRequestsManager.POST, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                DebugLog.d("pushSynchronisation " + response);
                if (response != null) {
                    try {
                        JSONArray respArray = new JSONArray(response);
                        if (respArray.length() > 0) {
                            //TODO Update local salesdocumets having temporary customer refence


                            salesOk = true;
                            handleResponse(respArray, jsonArraySales);
                            if (Utils.isNetworkAvailable(context) && customersOk && salesOk && inventoriesOk && inventoryProductsOk) {
                                //TODO update sent actions in local db transmitted status
                                new SynchronisationManager(context, null).subscribeDevice();
                            }
                            generateErrorReport();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        }, context, null);

    }

    private void pushActionRequest()  {
        DebugLog.d(jsonArray.toString());
        //TODO send list of requests
        //TODO setup push API
        //POST /api/sync/actionRequest/list
        final MainActivity activity = (MainActivity)context;
        WSRequestsManager wsRequestsManager = new WSRequestsManager();
        String[] path = new String[4];
        path[0] = ConstantsWS.PATH_API;
        path[1] = ConstantsWS.PATH_SYNC;
        path[2] = ConstantsWS.PATH_ACTIONREQUEST;
        path[3] = ConstantsWS.PATH_LIST;

        HashMap<String, String> headers = new HashMap<>();
        //TODO setup etablissement code
        headers.put("Authorization", Prefs.getPref(Prefs.AUTHORIZATION, context));
        String array = jsonArray.toString();
        array = array.replaceAll("\"null\"", "null");
        try {
            jsonArray = new JSONArray(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DebugLog.d("pushSynchronisation " + jsonArray.toString());
        wsRequestsManager.sendRequest(path, null, null, jsonArray, WSRequestsManager.POST, headers, new WSRequestsManager.OnRequestResponselistener() {
            @Override
            public void OnRequestResponse(String response) {
                DebugLog.d("pushSynchronisation " + response);
                if (response != null) {
                    if(response.equals(context.getString(R.string.info_message_error_network))){
                        try {
                            int transmited = 0;
                            int error = 1;
                            String errorMsg = "network error";
                            String ref = jsonArray.getJSONObject(0).getString("reference");
                            updateActionRequest(ref, transmited, error, errorMsg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            JSONObject jsonObject ;
                            if(response.startsWith("[")){
                                JSONArray respArray = new JSONArray(response);
                                jsonObject = respArray.getJSONObject(0);
                            }else {
                                jsonObject = new JSONObject(response);
                            }
                                //   handleResponse(respArray, jsonArray);
                                int transmited = 0;
                                if (jsonObject.getBoolean("transmited")) {
                                    transmited = 1;
                                }
                                String ref = jsonObject.getString("reference");
                                int error = 0;
                                if (jsonObject.getBoolean("error")) {
                                    error = 1;
                                }
                                String errorMsg = "";
                                if (jsonObject.has("errorMessage")) {
                                    errorMsg = jsonObject.getString("errorMessage");
                                }
                                updateActionRequest(ref, transmited, error, errorMsg);
                                generateErrorReport();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, context, activity);
    }


    private void handleResponse(JSONArray array, JSONArray respArray) throws JSONException {
        for (int i = 0; i < respArray.length(); i++) {
            for (int j = 0; j < array.length(); j++) {
                if (array.getJSONObject(j).getString("reference").equals(respArray.getJSONObject(i).getString("reference"))) {
                    //TODO update sent actions in local db transmitted status
                    // EventBus.getDefault().post(new ReleaseScreenEvent());
                    JSONObject jsonObject = array.getJSONObject(j);
                    jsonObject.put("transmited", true);
                    jsonObject.put("error", respArray.getJSONObject(i).getBoolean("error"));
                    if (respArray.getJSONObject(i).has("errorMessage")) {
                        jsonObject.put("errorMessage", respArray.getJSONObject(i).getString("errorMessage"));
                    }
                    jsonObject.length();
                    break;
                }
            }
        }
    }



    private void updateActionRequest(final String ref, final int transmited, final int error, final String errorMsg) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                dbLafarge.actionRequestDao().updateActionRequest(ref, transmited, error, errorMsg);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d("SUCCESS ROOM", "Success update ActionRequest");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ERROR ROOM up Actionreq", e.getMessage());
                    }
                });
    }

    private void generateErrorReport() {
       // JSONArray jsonArrayReport = dbTableHelper.getResults(Constants.TBL_ACTION_REQUESTS, "SELECT * FROM  " + Constants.TBL_ACTION_REQUESTS + " WHERE " + Constants.TBL_ACTION_REQUESTS + ".error LIKE 'true'", 0, 0);
        //DebugLog.d(jsonArrayReport.toString());
    }


}
