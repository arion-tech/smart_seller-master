package io.mintit.lafarge.app;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;
import io.mintit.lafarge.DBRoom.DBLafarge;
import io.mintit.lafarge.utils.Prefs;

import static io.mintit.lafarge.global.Constants.DATABASE_FILENAME;

/**
 * Created by mint on 14/03/18.
 */

public class LafargeApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
      //  Fabric.with(this, new Crashlytics());
        setLocale();
    }

    public void setLocale() {
        if (Prefs.getPref(Prefs.SELECTED_LANGUAGE, this) != null) {
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.setLocale(new Locale(Prefs.getPref(Prefs.SELECTED_LANGUAGE, this).toLowerCase())); // API 17+ only.
            res.updateConfiguration(conf, dm);
        }
    }


    public DBLafarge getDataBaseInstance(){
        return Room.databaseBuilder(getApplicationContext(), DBLafarge.class, DATABASE_FILENAME).fallbackToDestructiveMigration().build();
    }

    static final Migration MIGRATION_22_23 = new Migration(22, 23) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLocale();
    }

}
