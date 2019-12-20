package io.mintit.lafarge.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import io.mintit.lafarge.ui.activity.SplashActivity;
import io.mintit.lafarge.utils.NetworkUtil;
import io.mintit.lafarge.utils.Prefs;
import io.mintit.lafarge.utils.Utils;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if(isAppRunning(context)) {
            Log.d("BroadcastReceiverU", Utils.isNetworkAvailable(context) + "");
            int status = NetworkUtil.getConnectivityStatusString(context);
            Log.d("BroadcastReceiverS", status + "");
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction()) ||
                    "android.net.wifi.WIFI_STATE_CHANGED".equals(intent.getAction())) {
                if (status == NetworkUtil.NETWORK_STATUS_WIFI ||
                        status == NetworkUtil.NETWORK_STATUS_MOBILE) {
                    if (!Prefs.getBooleanPref("IS_SYNCHRONIZED", context, false)) {
                        Intent intent1 = new Intent(context, SplashActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent1);
                    }
                } else {
                    if (!Prefs.getBooleanPref("IS_SYNCHRONIZED", context, false)) {
                        Toast.makeText(context, "check internet", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
    }


    protected boolean isAppRunning(Context context){
        String activity = SplashActivity.class.getName();
        ActivityManager activityManager = (ActivityManager)context.
                getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> tasks = activityManager.
                getRunningTasks(Integer.MAX_VALUE);

        for(ActivityManager.RunningTaskInfo task : tasks){
            if(activity.equals(task.baseActivity.getClassName())){
                return true;
            }
        }
        return false;
    }



}