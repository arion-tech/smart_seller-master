package io.mintit.lafarge.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatDrawableManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import io.mintit.lafarge.R;


/**
 * Created by mint on 27/03/17.
 */

public class Utils {
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    public static DateFormat formatterFr = new SimpleDateFormat("dd/MM/yyyy");
    public static DateFormat formatterEng = new SimpleDateFormat("yyyy/MM/dd");
    public static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat formatterDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static DateFormat formatterDateTime2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat caldateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static boolean isTablet(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        DebugLog.d("" + (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE));
        return manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE;
    }

    public static String readJSONFromAsset(String filename, Context context) {
        String json = "";
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static void showAlert(final String message_id, final Activity activity, final Runnable runnable, final boolean showCancel, final boolean showOk) {
        if (!activity.isFinishing()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AppCompatDialog alertDialog = null;
                    try {
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity, R.style.MyAlertDialogStyle);
                        alertDialog = null;
                        LayoutInflater inflater = activity.getLayoutInflater();
                        View dialoglayout = inflater.inflate(R.layout.dialog_info, null);
                        TextView dialogMessage = (TextView) dialoglayout.findViewById(R.id.textView_message);
                        dialogMessage.setText(message_id);

                        alertDialogBuilder.setCancelable(false);
                        if (showOk) {
                            alertDialogBuilder.setPositiveButton(activity.getString(R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            if (runnable != null) {
                                                activity.runOnUiThread(runnable);
                                            }
                                            dialog.dismiss();
                                        }
                                    });
                        }
                        if (showCancel) {
                            alertDialogBuilder.setNegativeButton(activity.getString(R.string.annuler),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                        }
                        alertDialogBuilder.setView(dialoglayout);
                        alertDialog = alertDialogBuilder.create();


                        alertDialog.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });
        }


    }

    public static boolean isComingHour(String time, String date) {
        /* Set the alarm to start at 10:30 AM */
        try {

            final Calendar c = Calendar.getInstance();
            c.setTime(Utils.caldateFormat.parse(date));
            Calendar calendar = Calendar.getInstance();
            DebugLog.d("now___ " + "   " + calendar.getTime().toString());
            Calendar now = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            //  if (setDate) {
            calendar.set(Calendar.YEAR, c.get(Calendar.YEAR));
            calendar.set(Calendar.MONTH, c.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH));
            //  }
            Date mTime = timeFormat.parse(time);
            calendar.set(Calendar.HOUR_OF_DAY, mTime.getHours());
            calendar.set(Calendar.MINUTE, mTime.getMinutes());
            calendar.set(Calendar.SECOND, mTime.getSeconds());
            calendar.set(Calendar.MILLISECOND, 0);
            DebugLog.d("now___ " + now.getTime().toString() + "   " + calendar.getTime().toString());
            return calendar.after(now);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static Bitmap getRoundCornerBitmap(Bitmap bitmap, int radius) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        final RectF rectF = new RectF(0, 0, w, h);

        canvas.drawRoundRect(rectF, radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, null, rectF, paint);

        /**
         * here to define your corners, this is for left bottom and right bottom corners
         */
        final Rect clipRect = new Rect(w, 0, h - radius, 0);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        canvas.drawRect(clipRect, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rectF, paint);

        bitmap.recycle();

        return output;
    }

    public static Bitmap decodeFile(File f) throws IOException {
        Bitmap b = null;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = new FileInputStream(f);
        BitmapFactory.decodeStream(fis, null, o);
        fis.close();

        int scale = 1;
        if (o.outHeight > 500 || o.outWidth > 500) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(500 /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        fis = new FileInputStream(f);
        b = BitmapFactory.decodeStream(fis, null, o2);
        fis.close();

        return b;
    }

    public static void showKeyboard(final EditText editText, final Context context) {
        InputMethodManager keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(editText, 0);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    @SuppressWarnings("RestrictedApi")
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static boolean isNetworkAvailable(Context activity) {
        if (activity != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) activity
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager
                    .getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                return true;
            } else {
                // showAlert(R.string.info_message_error_network, activity, null, false, false, 0);
                return false;
            }
        } else {
            return true;
        }

    }

    public static void openAppRating(Context context) {
        // you can also use BuildConfig.APPLICATION_ID
        String appId = context.getPackageName();
        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appId));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager()
                .queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp : otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName
                    .equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                // make sure it does NOT open in the stack of your activity
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // task reparenting if needed
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                // if the Google Play was already open in a search result
                //  this make sure it still go to the app page you requested
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // this make sure only the Google Play app is allowed to
                // intercept the intent
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appId));
            context.startActivity(webIntent);
        }
    }


    public static HashMap<String, Object> toMap(JSONObject object) throws JSONException {
        HashMap<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);
           /*
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
           */
            map.put(key, value);
        }
        return map;
    }
   public static HashMap<String, Object> toMap(JSONArray  myArray) throws JSONException {
       HashMap<String, Object> pairs = new HashMap<>();
       for (int i = 0; i < myArray.length(); i++) {
           JSONObject j = myArray.optJSONObject(i);
           Iterator<String>  it = j.keys();
           while (it.hasNext()) {
               String n = it.next();
               pairs.put(n, j.get(n));
           }
       }
        return pairs;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }


    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null
                    && activity.getCurrentFocus() != null
                    && activity.getCurrentFocus().getWindowToken() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity
                        .getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static String getCurrentFullDate() throws ParseException {
        //formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(new Date());
    }

    public static String getCurrentDate() throws ParseException {
        //formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        return caldateFormat.format(new Date());
    }

    public static String getCurrentDateTimeWith15minutes(boolean add15) throws ParseException {
        Calendar cal = Calendar.getInstance(); //current date and time
        if (add15) {
            cal.add(Calendar.MINUTE, 15);
        }
        return formatterDateTime.format(cal.getTime());
    }

    public static String getCurrentDateTimePlus1Minute() throws ParseException {
        Calendar cal = Calendar.getInstance(); //current date and time
        cal.add(Calendar.MINUTE, 1);
        return formatterDateTime.format(cal.getTime());
    }

    public static String getCurrentDateTime() throws ParseException {
        return formatterDateTime.format(new Date());
    }

     public static String getCurrentTime() throws ParseException {
        return timeFormat.format(new Date());
    }

    public static String getTomorrowDate() throws ParseException {
        Calendar cal = Calendar.getInstance(); //current date and time
        cal.add(Calendar.DAY_OF_MONTH, 1); //add a day
        return formatter.format(cal.getTime());
    }

    public static File bitmapToPng(Context context, Bitmap resource) throws IOException {
        //create a file to write bitmap data
        File f = new File(context.getCacheDir(), "MyFbImage.png");
        f.createNewFile();
//Convert bitmap to byte array
        Bitmap bitmap = resource;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        return f;
    }

    public static String getDate(String timeStamp) {
//2016-01-13T00:00:00
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date netDate = sdf.parse(timeStamp);
            return sdf2.format(netDate);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "xx";
        }
    }

    public static Date getDateFromTimestamp(long timeStamp) {
        try {
            Date netDate = (new Date(timeStamp));
            return netDate;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getTime(long timeStamp) {

        try {
            DateFormat sdf = new SimpleDateFormat("HH:mm");
            Date netDate = (new Date(timeStamp * 1000));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static String generateTimestamp() {
        Long tsLong = System.currentTimeMillis();
        String ts = tsLong.toString();
        return ts;
    }

    public static String capitalize(String prenom) {
        String[] words = prenom.split(" ");
        StringBuilder sb = new StringBuilder();
        if (words.length > 0) {
            if (words[0].length() > 0) {
                sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString().toLowerCase());
                for (int i = 1; i < words.length; i++) {
                    sb.append(" ");
                    if (words[i].length() > 0) {
                        sb.append(Character.toUpperCase(words[i].charAt(0)) + words[i].subSequence(1, words[i].length()).toString().toLowerCase());
                    }
                }
            }
            String titleCaseValue = sb.toString();
            return titleCaseValue;
        } else {
            return prenom;
        }

    }


    public static long getRreaminingTime(Date coverDateTime) throws ParseException {
        long diff = formatterDateTime.parse(getCurrentDateTime()).getTime() - coverDateTime.getTime();
        long seconds = diff / 1000;
        return seconds;
    }

    public static String getTripPeriod(String tripDateStart, String tripDateEnd) {
        long secs = (Long.parseLong(tripDateEnd) - Long.parseLong((tripDateStart)));
        int hours = (int) (secs / 3600);
        secs = secs % 3600;
        int mins = (int) (secs / 60);
        secs = secs % 60;
        DebugLog.d(hours + " h " + mins + " mn" + secs + "");
        return (hours > 0 ? hours + " h " : "") + (mins > 0 ? mins + " mn" : "") + (secs > 0 ? secs + " s" : "");
    }

    public static float getTripDistance(String addLatitude, String addLongitude, String addLatitude1, String addLongitude1) {
        Location locationStart = new Location("loc_start");
        locationStart.setLatitude(Double.parseDouble(addLatitude));
        locationStart.setLongitude(Double.parseDouble(addLongitude));
        Location locationEnd = new Location("loc_end");
        locationEnd.setLatitude(Double.parseDouble(addLatitude1));
        locationEnd.setLongitude(Double.parseDouble(addLongitude1));
        float distance = locationStart.distanceTo(locationEnd);
        return distance;
    }

    public static String formatBirth(String usrBirthdate) throws ParseException {
        Date date = null;
        if (usrBirthdate.equals("0000-00-00")) {
            //  usrBirthdate = "";
        } else if (usrBirthdate.contains("-")) {
            date = formatter.parse(usrBirthdate);
        } else if (usrBirthdate.contains("/")) {
            String[] splists = usrBirthdate.split("/");
            if (splists[0].length() == 4) {
                date = formatterEng.parse(usrBirthdate);
            }
        }
        if (date != null) {
            usrBirthdate = formatterFr.format(date);
        } else {
            usrBirthdate = "";
        }
        return usrBirthdate;
    }

    public static Bitmap getScaledBitmap(int driver_car, Activity context) {
        int height = (int) convertPixelsToDp(100, context);
        int width = (int) convertPixelsToDp(100, context);
        BitmapDrawable bitmapdraw = (BitmapDrawable) context.getResources().getDrawable(driver_car);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        return smallMarker;
    }
}
