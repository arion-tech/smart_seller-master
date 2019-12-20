package io.mintit.lafarge.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.mintit.lafarge.R;
import io.mintit.lafarge.app.LafargeApp;
import io.mintit.lafarge.model.Etablissement;
import io.mintit.lafarge.utils.Prefs;

public class ChooseStoreActivity extends AppCompatActivity {

    @BindView(R.id.relativeLayout_choose_store)
    RelativeLayout linearlayoutChooseStore;

    @BindView(R.id.edittext_store_code)
    EditText edittextStoreCode;

    Gson gson = new Gson();
    LafargeApp lafargeApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //System.out.println("LAAAAAAAAAAAAAAAAAAAAAAAAANG : "+Prefs.getPref(Prefs.SELECTED_LANGUAGE, this).toLowerCase());
        if(Prefs.getPref(Prefs.SELECTED_LANGUAGE, this) == null){
            Prefs.setPref(Prefs.SELECTED_LANGUAGE, "fr", this);
        }
        if(Prefs.getPref(Prefs.SELECTED_LANGUAGE, this).toLowerCase().equals("")){
            Prefs.setPref(Prefs.SELECTED_LANGUAGE, "fr", this);
        }
        lafargeApp = (LafargeApp) getApplication();
        setInitLanguage(Prefs.getPref(Prefs.SELECTED_LANGUAGE, this).toLowerCase());
        lafargeApp.setLocale();
        setContentView(R.layout.activity_choose_store);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.relativeLayout_choose_store)
    void chooseStore(){
        Prefs.setPref(Prefs.ETABLISSEMENT, edittextStoreCode.getText().toString(), this);
        if(edittextStoreCode.getText().toString().equals("300")){
            Etablissement et = new Etablissement();
            et.setId(Long.valueOf(edittextStoreCode.getText().toString()));
            et.setLibelle("Arion Tech.");
            et.setCurrencyId("TND");
            //et.setCodeEtablissement("300");
            Prefs.setPref(Prefs.ETABLISSEMENT, gson.toJson(et), ChooseStoreActivity.this);

            Intent i = new Intent(this, ChooseSellerActivity.class);
            i.putExtra("store_code", edittextStoreCode.getText().toString());
            startActivity(i);
        }
    }

    private void setInitLanguage(String languageToLoad) {
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_choose_store);//setContentView should be here so we can refresh view language
    }
}
