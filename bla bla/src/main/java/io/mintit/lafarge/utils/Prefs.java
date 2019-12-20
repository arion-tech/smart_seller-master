package io.mintit.lafarge.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Field;

import io.mintit.lafarge.R;


public class Prefs {

    public static final String IS_CONNECTED = "isConnected";
    public static final String GET_NOTIF = "getNotifications";
    public static final String USER_ID = "user_id";
    public static final String USER_INFO = "user_info";
    public static final String PROFILE_POPUP = "profile_popup";
    public static final String EVENT_POPUP = "event_popup";
    public static final String PREFIX_NOTIF_COUNT = "notif_count_";
    public static final String PHONE_POP = "false";
    public static final String USER_TYPE = "user_type";
    public static final String AUTHORIZATION = "Authorization";
    public static final String USER_EMAIL = "user_email";
    public static final String COVER_FIRST_START_TIME = "elapsed_time";
    public static final String COVER_FIRST_START_TIME_USER_ID = "COVER_FIRST_START_TIME_USER_ID";
    public static final String FCM_TOKEN = "fcm_token";
    public static final String CURRENT_TRIP = "current_trip";
    public static final String IS_PASSENGER = "is_passenger";
    public static final String USER_PASSWORD = "user_password";
    public static final String REMMEMBER_ME = "remmember_me";
    public static final String USER_PASSWORD_REMMEMBER = "USER_PASSWORD_REMMEMBER";
    public static final String USER_EMAIL_REMMEMBER = "USER_EMAIL_REMMEMBER";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ONE_SIGNAl_PLAYER_ID = "ONE_SIGNAl_PLAYER_ID";
    public static final String SELECTED_LANGUAGE = "SELECTED_LANGUAGE";
    public static final String IS_SYNCHRONIZED = "IS_SYNCHRONIZED";
    public static final String TOKEN = "token";
    public static final String HAS_PENDING_DAILY = "HAS_PENDING_DAILY";
    private static final String TAG = Prefs.class.getSimpleName();
    public static final String ETABLISSEMENT = "ETABLISSEMENT";

    public static final String CLIENT_ID = "CLIENT_ID";
    public static final String THEME = "theme";
    public static final String LOGO_URL = "LOGO_URL";
    public static final String APP_CONFIG = "APP_CONFIG";
    public static final String SELLER_LOGIN = "SELLER_LOGIN";

    public static void setBooleanPref(String key, boolean value, Context ctx) {
        DebugLog.d("setPref[" + key + "]  " + value);
        SharedPreferences.Editor edit = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).edit();
        edit.putBoolean(key, value).commit();
    }

    public static boolean getBooleanPref(String key, Context ctx, boolean defaultValue) {
        boolean value = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).getBoolean(key, defaultValue);
        DebugLog.d("getPref[" + key + "]  " + value);
        return value;
    }


    public static void setPref(String key, String value, Context ctx) {

        DebugLog.d("setPref[" + key + "]  " + value);
        if (value != null) {
            SharedPreferences.Editor edit = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).edit();
            edit.putString(key, value).commit();
        }
    }

    public static void setIntPref(String key, int value, Context ctx) {

        DebugLog.d("setPref[" + key + "]  " + value);

        SharedPreferences.Editor edit = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).edit();
        edit.putInt(key, value).commit();

    }

    public static int getIntPref(String key, Context ctx) {
        int value = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).getInt(key, 0);
        DebugLog.d("getPref[" + key + "]  " + value);
        return value;
    }

    public static String getPref(String key, Context ctx) {
        String value = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).getString(key, null);
        DebugLog.d("getPref[" + key + "]  " + value);
        return value;
    }

    public static void setPrefObject(Object obj, Context ctx) {

        DebugLog.d("setPref[" + obj.getClass().getSimpleName() + "]");
        for (Field f : obj.getClass().getFields()) {
            try {

                if (f.getType() == String.class) {
                    if (f.get(obj) != null) {
                        String value = f.get(obj) + "";
                        String name = f.getName();
                        String className = obj.getClass().getSimpleName();
                        SharedPreferences.Editor edit = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).edit();
                        edit.putString(className + "_" + name, value).commit();
                        DebugLog.d("set [" + className + "_" + name + "]" + f.get(obj));
                    }
                } else if (f.getType() == double.class) {
                    if (f.get(obj) != null) {
                        String value = String.valueOf(f.get(obj));
                        String name = f.getName();
                        String className = obj.getClass().getSimpleName();
                        SharedPreferences.Editor edit = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).edit();
                        edit.putString(className + "_" + name, value).commit();
                        DebugLog.d("set [" + className + "_" + name + "]" + f.get(obj));
                    }
                } else if (f.getType() == int.class) {
                    if (f.get(obj) != null) {
                        String value = String.valueOf(f.get(obj));
                        String name = f.getName();
                        String className = obj.getClass().getSimpleName();
                        SharedPreferences.Editor edit = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).edit();
                        edit.putString(className + "_" + name, value).commit();
                        DebugLog.d("set [" + className + "_" + name + "]" + f.get(obj));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void clear(Context ctx) {

        ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).edit().clear().commit();

    }


    public static Object getPrefObject(Class<?> cl, Context ctx) {

        try {
            String className = cl.getSimpleName();
            DebugLog.d("getPref[" + className + "]");
            Object obj = cl.newInstance();
            for (Field f : cl.getFields()) {
                String value = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE).getString(className + "_" + f.getName(), null);
                if ((value != null) && (value.length() > 0) && (f.getType() == String.class)) {

                    f.set(obj, value.replace("null", ""));
                    DebugLog.d("get [" + className + "_" + f.getName() + "]" + value);
                } else if ((value != null) && (value.length() > 0) && (f.getType() == double.class)) {

                    f.set(obj, Double.valueOf(value.replace("null", "")));
                    DebugLog.d("get [" + className + "_" + f.getName() + "]" + Double.valueOf(value.replace("null", "")));
                } else if ((value != null) && (value.length() > 0) && (f.getType() == int.class)) {

                    f.set(obj, Integer.parseInt(value.replace("null", "")));
                    DebugLog.d("get [" + className + "_" + f.getName() + "]" + Integer.parseInt(value.replace("null", "")));
                }
            }

            return obj;
        } catch (Exception e) {
            DebugLog.d("Error[" + cl + "]:" + e.getLocalizedMessage() + " ctx:" + ctx);
        }

        try {
            return cl.newInstance();
        } catch (Exception e) {
        }

        return null;
    }

    public static void clearPref(Context ctx, String key) {

        ctx.getSharedPreferences(TAG, Context.MODE_PRIVATE).edit().remove(key).commit();

    }
}