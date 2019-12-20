package io.mintit.lafarge.utils;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import io.mintit.lafarge.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WSRequestsManager {
    public static final int GET = 0;
    public static final int POST = 1;
    public static final int PUT = 2;
    public static final int DELETE = 3;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //public static final MediaType URL_ENCODED = MediaType.parse("application/x-www-form-urlencoded");
    private static final String LAFARGE_HOST = "http://197.13.7.115";
    private static final long TIMEOUT = 30 * 60 * 1000;


    JSONObject data = new JSONObject();
    JSONArray dataArray = new JSONArray();
    String raw = "";
    private Context ctx;
    private boolean hasFile;
    private boolean isJson = true;
    private boolean isUrlEncoded = false;

    public WSRequestsManager() {
    }

    public void sendRequest(String[] pathSegment, HashMap<String, String> queryParameter, HashMap<String, Object> bodyParams, JSONArray jsonArray, int method, final HashMap<String, String> headers, final OnRequestResponselistener onRequestResponselistener, final Context context, final Activity activity) {
        if (activity != null) {
            Utils.hideSoftKeyboard(activity);
        }
        ctx = context;
        final boolean isConnected = Prefs.getBooleanPref(Prefs.IS_CONNECTED, context, false);
        String[] separated = LAFARGE_HOST.split("/");


        OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder();
        // turn off timeouts (github.com/socketio/engine.io-client-java/issues/32)  clientBuilder.readTimeout(0, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(TIMEOUT, TimeUnit.SECONDS);
        clientBuilder.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
        clientBuilder.readTimeout(TIMEOUT, TimeUnit.SECONDS);
       /* if (isConnected) {
            clientBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", PrefUtils.getPref(PrefUtils.AUTHORIZATION, context))
                            .build();
                    return chain.proceed(newRequest);
                }
            });
        }*/
        //OkHttpClient okHttpClient = clientBuilder.build();
        HttpUrl.Builder url_builder = new HttpUrl.Builder();

        System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZ: "+separated[0]);
        url_builder.scheme(separated[0].replace(":", ""));

        url_builder.host(separated[2]);
        url_builder.port(5002); //old value : 5005

        System.out.println("YYYYYYYYYYYYYYYYYYYY: "+separated[2]);
        System.out.println("TTTTTTTTTTTTTTTTTTTT: "+separated[1]);

        if (separated.length > 3) {
            for (int i = 3; i < separated.length; i++) {
                if (!TextUtils.isEmpty(separated[i])) {
                    url_builder.addPathSegment(separated[i]);
                }

            }
        }

        if (pathSegment != null) {
            if (pathSegment.length > 0) {
                for (int i = 0; i < pathSegment.length; i++) {
                    url_builder.addPathSegment(pathSegment[i]);
                }
            }
        }

        if (queryParameter != null) {
            Iterator paramsIterator = queryParameter.keySet().iterator();
            while (paramsIterator.hasNext()) {
                String key = (String) paramsIterator.next();
                String value = (String) queryParameter.get(key);
                url_builder.addQueryParameter(key, value);
            }
        }
        if (isConnected) {
            //url_builder.addQueryParameter(Constants.API.ACCESS_TOKEN, PrefUtils.getPref(PrefUtils.ACCESS_TOKEN, context));
        }

        System.out.println("AAAAAAAAAAAAAAAAAAAAAA: "+url_builder.toString());


        final HttpUrl httpUrl = url_builder.build();
        DebugLog.d(httpUrl.url().toString());
        JSONObject jsonBody = new JSONObject();
        FormBody.Builder formBody = new FormBody.Builder();
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
        if (bodyParams != null) {
            if (!hasFile) {
                Iterator paramsIterator = bodyParams.keySet().iterator();
                while (paramsIterator.hasNext()) {
                    String key = (String) paramsIterator.next();
                    Object value = bodyParams.get(key);
                    if (isJson) {
                        try {
                            data.put(key, value);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (isUrlEncoded) {
                        formBody.add(key, value.toString());
                    } else {
                        if (!TextUtils.isEmpty(raw)) {
                            raw = raw + "&";
                        }
                        raw = raw + key + "=" + value;
                    }
                }
            } else {
                multipartBuilder.setType(MultipartBody.FORM);
                Iterator paramsIterator = bodyParams.keySet().iterator();
                final MediaType MEDIA_TYPE_PNG = MediaType.parse("*/*");

                while (paramsIterator.hasNext()) {
                    String key = (String) paramsIterator.next();
                    Object value = bodyParams.get(key);
                    if (value instanceof File) {
                        DebugLog.d("addFormDataPart_file_name " + key + " --> " + ((File) value).getName());

                        multipartBuilder.addFormDataPart(key, "", RequestBody.create(MEDIA_TYPE_PNG, (File) value));
                    } else if (false) {
                        try {
                            data.put(key, value);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        } else if (jsonArray != null) {
            dataArray = jsonArray;

        }
        final Request.Builder request = new Request.Builder();
        request.url(httpUrl);
        switch (method) {
            case GET:
                break;
            case POST:
                if (!hasFile) {
                    if (!isUrlEncoded) {
                        request.post(RequestBody.create(JSON, isJson ? (dataArray.length() > 0 ? dataArray.toString() : data.toString()) : raw));
                        DebugLog.d((dataArray.length() > 0 ? dataArray.toString() : data.toString()));
                    } else {
                        request.post(formBody.build())
                                .build();
                    }
                } else {
                    request.post(multipartBuilder.build());
                }
                break;
            case PUT:
                break;
            case DELETE:
                request.delete();
                break;
        }
        if (headers != null) {
            Iterator paramsIterator = headers.keySet().iterator();
            while (paramsIterator.hasNext()) {
                String key = (String) paramsIterator.next();
                String value = (String) headers.get(key);
                if (key != null && value != null) {
                    request.addHeader(key, value);
                    DebugLog.d(key + " --> " + value);
                }
            }
        }

        //  request.addHeader("Content-Type", "application/x-www-form-urlencoded");

        if (Utils.isNetworkAvailable(context)) {

            final OkHttpClient client = new OkHttpClient();
            Call call = client.newCall(request.build());

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    DebugLog.d("onFailure sendRequest " + e.getMessage());
                    onRequestResponselistener.OnRequestResponse("");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String mResponse = response.body().string().toString();
                    DebugLog.d("onResponse " + httpUrl);
                    DebugLog.d("onResponse " + mResponse);
                    onRequestResponselistener.OnRequestResponse(mResponse);

                }
            });
        } else {
            if (activity != null) {
                onRequestResponselistener.OnRequestResponse(context.getResources().getString(R.string.info_message_error_network));
            }
        }
    }

    public boolean isHasFile() {
        return hasFile;
    }

    public void setHasFile(boolean hasFile) {
        this.hasFile = hasFile;
    }

    public void setIsJson(boolean isJson) {
        this.isJson = isJson;
    }

    public void setUrlEncoded(boolean urlEncoded) {
        isUrlEncoded = urlEncoded;
    }

    public interface OnRequestResponselistener {
        void OnRequestResponse(String response);
    }

}
