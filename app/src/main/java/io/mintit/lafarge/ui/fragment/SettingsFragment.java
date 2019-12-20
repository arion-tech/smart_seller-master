package io.mintit.lafarge.ui.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.mintit.lafarge.R;
import io.mintit.lafarge.app.LafargeApp;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.ui.activity.SplashActivity;
import io.mintit.lafarge.utils.Prefs;
import io.mintit.lafarge.utils.Utils;

import static android.app.Activity.RESULT_OK;


public class SettingsFragment extends BaseFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    Unbinder unbinder;
    private MainActivity activity;
    private static final int PICK_FROM_GALLERY = 1;
    @BindView(R.id.general_settings)
    RelativeLayout relativeLayoutGeneralSettings;
    @BindView(R.id.cgu_settings)
    RelativeLayout relativeLayoutCgu;
    @BindView(R.id.selected_setting_arrow_cgu)
    ImageView cguArrow;
    @BindView(R.id.selected_setting_arrow_generalSettings)
    ImageView generalArrow;

    @BindView(R.id.cgu_displayer)
    LinearLayout linearLayoutCguDisplayer;
    @BindView(R.id.general_settings_displayer)
    LinearLayout linearLayoutGeneralDisplayer;

    @BindView(R.id.theme_default)
    Button default_theme;
    @BindView(R.id.theme_indigo)
    Button indigo_theme;
    @BindView(R.id.theme_pink)
    Button pink_theme;

    @BindView(R.id.radioButtons_FR)
    RadioButton radioButtonFR;
    @BindView(R.id.radioButtons_EN)
    RadioButton radioButtonEN;
    @BindView(R.id.change_logo)
    Button changebutton;
    @BindView(R.id.image_icone)
    ImageView icone;




    LafargeApp lafargeApp;
    private String selectedLang = null;

    public static final String INDIGO = "indigo";
    public static final String PINK = "pink";
    public static final String DEFAULT = "default";





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);


        unbinder = ButterKnife.bind(this, view);
        activity = (MainActivity) getActivity();
        //lafargeApp = (LafargeApp) activity.getApplication();
        String imagestring = Prefs.getPref(Prefs.ICONE_IMAGE,getContext());
        byte[] imageBytes = Base64.decode(imagestring, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        icone.setImageBitmap(decodedImage);
        return view;
    }

    @Override
    public void onFragResume() {

    }

    public void startSynchro(){
        Utils.showAlert(getString(R.string.sync_verification), activity, new Runnable() {
            @Override
            public void run() {
                if(Utils.isNetworkAvailable(getActivity())) {
                    Prefs.setBooleanPref(Prefs.IS_SYNCHRONIZED, false, activity);
                    Intent intent = new Intent(getActivity(), SplashActivity.class);
                    startActivity(intent);
                    activity.finish();
                }else {
                    Toast.makeText(getActivity(), "No internet available", Toast.LENGTH_SHORT).show();
                }

            }
        }, true, true);

    }

    @OnClick(R.id.general_settings)
    public void generalSettings(){
        relativeLayoutGeneralSettings.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangle_accent));
        relativeLayoutCgu.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangle_white_outline_round));
        cguArrow.setVisibility(View.GONE);
        generalArrow.setVisibility(View.VISIBLE);
        linearLayoutCguDisplayer.setVisibility(View.GONE);
        linearLayoutGeneralDisplayer.setVisibility(View.VISIBLE);
    }
  @OnClick(R.id.change_logo)
  public void changeimage()
  {
      Intent intent = new Intent(Intent.ACTION_PICK, null);
      intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
      startActivityForResult(intent, PICK_FROM_GALLERY);
  }

    @OnClick(R.id.cgu_settings)
    public void cguSettings(){
        relativeLayoutGeneralSettings.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangle_white_outline_round));
        relativeLayoutCgu.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangle_accent));
        cguArrow.setVisibility(View.VISIBLE);
        generalArrow.setVisibility(View.GONE);
        linearLayoutCguDisplayer.setVisibility(View.VISIBLE);
        linearLayoutGeneralDisplayer.setVisibility(View.GONE);
    }

    @OnClick({R.id.theme_default, R.id.theme_indigo, R.id.theme_pink})
    void themeButtonsClicked(View view){
        switch (view.getId()) {
            case R.id.theme_default:
                saveTheme(DEFAULT);
                break;
            case R.id.theme_indigo:
                saveTheme(INDIGO);
                break;
            case R.id.theme_pink:
                saveTheme(PINK);
                break;
        }
    }

    private void saveTheme(String value) {
        Prefs.setPref(Prefs.THEME, value, activity);
        //getActivity().recreate();
        activity.finish();
        activity.overridePendingTransition( 0, 0);
        activity.startActivity(activity.getIntent());
        activity.overridePendingTransition( 0, 0);
    }

    @OnClick(R.id.radioButtons_FR)
    public void onRadiosFR() {
        System.out.println("radio FR");
        selectedLang = "fr";
        setInitLanguage("fr");
        radioButtonFR.setChecked(true);
        radioButtonEN.setChecked(false);
        Prefs.setPref(Prefs.SELECTED_LANGUAGE, selectedLang, activity);
        //lafargeApp.setLocale();
        //getActivity().recreate();
        activity.finish();
        activity.overridePendingTransition( 0, 0);
        activity.startActivity(activity.getIntent());
        activity.overridePendingTransition( 0, 0);
    }

    @OnClick(R.id.radioButtons_EN)
    public void onRadiosEN(View view) {
        System.out.println("radio EN");
        selectedLang = "en";
        setInitLanguage("en");
        radioButtonFR.setChecked(false);
        radioButtonEN.setChecked(true);
        Prefs.setPref(Prefs.SELECTED_LANGUAGE, selectedLang, activity);
        //lafargeApp.setLocale();
        //getActivity().recreate();
        activity.finish();
        activity.overridePendingTransition( 0, 0);
        activity.startActivity(activity.getIntent());
        activity.overridePendingTransition( 0, 0);
    }

    private void setInitLanguage(String languageToLoad) {
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config, activity.getBaseContext().getResources().getDisplayMetrics());
        activity.setContentView(R.layout.activity_main);//setContentView should be here so we can refresh view language
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            icone.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            Bitmap bitmap = ((BitmapDrawable) icone.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageInByte = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageInByte, Base64.DEFAULT);
            Toolbar mToolbar = activity.findViewById(R.id.myToolbar);
            ImageView imageInToolbar = (ImageView) mToolbar.findViewById(R.id.toolbar_logo);
            byte[] imageBytes = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageInToolbar.setImageBitmap(decodedImage);
            Prefs.setPref(Prefs.ICONE_IMAGE,encodedImage,getContext());

        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
