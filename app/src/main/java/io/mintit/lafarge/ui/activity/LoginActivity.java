package io.mintit.lafarge.ui.activity;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mintit.lafarge.DBRoom.DBLafarge;
import io.mintit.lafarge.R;
import io.mintit.lafarge.app.LafargeApp;
import io.mintit.lafarge.model.User;
import io.mintit.lafarge.utils.Prefs;
import io.mintit.lafarge.utils.Utils;
import io.mintit.lafarge.utils.WSRequestsManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static io.mintit.lafarge.global.Constants.DATABASE_FILENAME;


public class LoginActivity extends AppCompatActivity {
    EditText editTextEmail;
    EditText editTextPassword;
    @BindView(R.id.radioButton_FR)
    RadioButton radioButtonFR;
    @BindView(R.id.radioButton_EN)
    RadioButton radioButtonEN;
    @BindView(R.id.linearLayout_progress)
    RelativeLayout linearLayoutProgress;
    LafargeApp lafargeApp;
    private String message;
    private String selectedLang = null;
    Gson gson = new Gson();
    private DBLafarge lafargeDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setRequestedOrientation(Utils.isTablet(this) ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        lafargeApp = (LafargeApp) getApplication();

        lafargeDatabase = lafargeApp.getDataBaseInstance();
    }

    public void onRadioFR(View view) {
        selectedLang = "fr";
        setInitLanguage("fr");
        radioButtonFR.setChecked(true);
        radioButtonEN.setChecked(false);
    }

    public void onRadioEN(View view) {
        selectedLang = "en";
        setInitLanguage("en");
        radioButtonFR.setChecked(false);
        radioButtonEN.setChecked(true);
    }

    private void setInitLanguage(String languageToLoad) {
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.activity_login);//setContentView should be here so we can refresh view language
    }
    public void login(View view) {
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        if (verifyFields()) {
            initLogin(editTextEmail.getText().toString(), editTextPassword.getText().toString());

        } else {
            Utils.showAlert(message, LoginActivity.this, null, false, true);
        }
    }




    private boolean verifyFields() {
        if (TextUtils.isEmpty(editTextEmail.getText().toString())) {
            message = getString(R.string.error_message_email);
            return false;
        }

        if (TextUtils.isEmpty(editTextPassword.getText().toString())||
                editTextPassword.getText().toString().length() < 6) {
            message = getString(R.string.error_message_password);
            return false;
        }

        return true;
    }


    @SuppressLint("CheckResult")
    private void initLogin(String email, String mdp) {
        Utils.hideSoftKeyboard(LoginActivity.this);
        showProgressBar(true);
        lafargeDatabase.userDao().getUserByEmailAndPswd(email, mdp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<User>>() {
                    @Override
                    public void accept(List<User> users) {
                        if (users.size() > 0) {

                            Prefs.setPref(Prefs.SELECTED_LANGUAGE, selectedLang, LoginActivity.this);
                            lafargeApp.setLocale();
                            Prefs.setBooleanPref(Prefs.IS_CONNECTED, true, LoginActivity.this);

                            Prefs.setPref(Prefs.USER_INFO, gson.toJson(users.get(0)), LoginActivity.this);
                            goToNextScreen();

                        } else {
                            showProgressBar(false);
                            Utils.showAlert(getString(R.string.wrong_credentials), LoginActivity.this, null, false, true);

                        }
                    }
                });
    }

    private void goToNextScreen() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgressBar(false);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                //startActivity(new Intent(LoginActivity.this, TestActivity.class));
                finishAffinity();
            }
        });
    }

    public void showProgressBar(boolean show) {
        linearLayoutProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }



}

