package io.mintit.lafarge.ui.activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.mintit.lafarge.DBRoom.DBLafarge;
import io.mintit.lafarge.R;
import io.mintit.lafarge.app.LafargeApp;
import io.mintit.lafarge.events.ReleaseScreenEvent;
import io.mintit.lafarge.model.AppAuthentication;
import io.mintit.lafarge.model.AppConfig;
import io.mintit.lafarge.model.Cart;
import io.mintit.lafarge.model.Daily;
import io.mintit.lafarge.model.Etablissement;
import io.mintit.lafarge.model.Seller;
import io.mintit.lafarge.model.User;
import io.mintit.lafarge.services.ActionRequestsManager;
import io.mintit.lafarge.services.CategorieService;
import io.mintit.lafarge.ui.fragment.BaseFragment;
import io.mintit.lafarge.ui.fragment.CartFragment;
import io.mintit.lafarge.ui.fragment.MenuDrawerFragment;
import io.mintit.lafarge.ui.widget.FullDrawerLayout;
import io.mintit.lafarge.utils.Prefs;
import io.mintit.lafarge.utils.Utils;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String INDIGO = "indigo";
    public static final String PINK = "pink";
    public static final String DEFAULT = "default";

    @BindView(R.id.drawer_layout)
    FullDrawerLayout drawerLayout;
    @BindView(R.id.frame_container)
    FrameLayout frameContainer;
    @BindView(R.id.relativeLayout_toolbar)
    RelativeLayout relativeLayoutToolbar;
    @BindView(R.id.imageview_back)
    ImageView imageviewBack;
    @BindView(R.id.imageview_menu)
    ImageView imageviewMenu;
    @BindView(R.id.linearLayout_progress)
    RelativeLayout linearLayoutProgress;
    @BindView(R.id.toolbar_logo)
    ImageView toolbarLogo;

    ActivityManager am;
    ImageView imageInToolbar;

    private Cart lastOpenedCart;
    private Daily daily;
    private User user;

    private Etablissement etablissement;
    LafargeApp lafargeApp;
    private Gson gson = new Gson();
    AppConfig appConfig;
    private Seller sellerLogin = new Seller();

    public Etablissement getEtablissement() {
        if (etablissement == null) {
            return new Etablissement();
        }
        return etablissement;
    }


    public DBLafarge getLafargeDatabase() {
        return lafargeApp.getDataBaseInstance();
    }

    public Daily getDaily() {
        return daily;
    }

    public void setDaily(Daily daily) {
        this.daily = daily;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Seller getSellerLogin() {
        return sellerLogin;
    }

    public void setSellerLogin(Seller sellerLogin) {
        this.sellerLogin = sellerLogin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // main activity //
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_container, new CartFragment());
        ft.commit();
        //startService(new Intent(MainActivity.this, CategorieService.class));
        // setRequestedOrientation(Utils.isTablet(this) ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        lafargeApp = (LafargeApp) getApplication();
        //set theme
        setTheme(getSavedTheme());
        getSupportActionBar().hide();
        /*appConfig = new AppConfig();
        appConfig = gson.fromJson(Prefs.getPref(Prefs.APP_CONFIG, this), AppConfig.class);
        System.out.println("AP COOOOOOOOOOOOOOOOOOOONFIG : " + appConfig);*/
        //---------
        setInitLanguage(Prefs.getPref(Prefs.SELECTED_LANGUAGE, this).toLowerCase());
        lafargeApp.setLocale();
        //---------
        setContentView(R.layout.activity_main);
        //---------switch icone photo----------//
        //File imgFile = new  File(Prefs.getPref(Prefs.LOGO_URL, this));
        //if(imgFile.exists()){
         //   Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
         //   Toolbar mToolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.myToolbar);
           // ImageView imageInToolbar = (ImageView) mToolbar.findViewById(R.id.toolbar_logo);
           // imageInToolbar.setImageBitmap(myBitmap);
            //toolbarLogo.setImageBitmap(myBitmap);
        //}
        Toolbar mToolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.myToolbar);
        imageInToolbar = (ImageView) mToolbar.findViewById(R.id.toolbar_logo);
        String imagestring = Prefs.getPref(Prefs.ICONE_IMAGE,getApplication());
        byte[] imageBytes = Base64.decode(imagestring, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        imageInToolbar.setImageBitmap(decodedImage);

        //---------
        user = new Gson().fromJson(Prefs.getPref(Prefs.USER_INFO, this), User.class);
        sellerLogin = new Gson().fromJson(Prefs.getPref(Prefs.SELLER_LOGIN, this), Seller.class);

        etablissement = new Gson().fromJson(Prefs.getPref(Prefs.ETABLISSEMENT, this), Etablissement.class);
        System.out.println("Etaaaaaaab from PREFS : "+etablissement.getId());
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initDrawerContent();
        //checkLastModifiedCart();
        //checkDaily();
        getSupportFragmentManager().addOnBackStackChangedListener(getListener());

        System.out.println("THEEEEEEEEEME : "+ this.getResources().getIdentifier("indigoDark", "color", this.getPackageName()));
    }

    private void setInitLanguage(String languageToLoad) {
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_main);//setContentView should be here so we can refresh view language
    }

    private int getSavedTheme() {
        String theme = Prefs.getPref(Prefs.THEME, this);
        if(theme == null){
            theme = "default";
        }
        switch (theme) {
            case PINK:
                return R.style.PinkTheme;
            case INDIGO:
                return R.style.IndigoTheme;
            case DEFAULT:
                return R.style.AppTheme;
            default:
                return R.style.AppTheme;
        }
    }


    @SuppressLint("CheckResult")
    private void checkLastModifiedCart() {
        setLastOpenedCart(null);
        getLafargeDatabase().cartDao().getLastModifiedCart(getUser().getIdUtilisateur())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Cart>>() {
                    @Override
                    public void accept(List<Cart> carts) {
                        if (carts.size() > 0) {
                            lastOpenedCart = carts.get(0);
                        }
                    }
                });
    }


    private void initDrawerContent() {
        Fragment menuDrawerFragment = new MenuDrawerFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_drawer, menuDrawerFragment).commit();
    }


    @SuppressLint("CheckResult")
    private void checkDaily() {
        getLafargeDatabase().dailyDao().getDaily(user.getIdUtilisateur())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Daily>>() {
                    @Override
                    public void accept(List<Daily> dailies) {
                        if (dailies.size() > 0) {
                            daily = dailies.get(0);
                        }
                        //TODO: check current screen
                        addFragment(CartFragment.newInstance(null, false, daily != null), "", false);

                    }
                });
    }

    public void addFragment(Fragment fragment, String title, boolean add_to_back_stack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (add_to_back_stack) {
            transaction.add(R.id.frame_container, fragment);
            transaction.addToBackStack("");
        } else {
            //TODO fix navigation issue --> add fragment to root
            getSupportFragmentManager().popBackStackImmediate();
            transaction.replace(R.id.frame_container, fragment, "my_tag");

        }
        transaction.commitAllowingStateLoss();
        closeDrawers();
    }

    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {

                FragmentManager manager = getSupportFragmentManager();

                if (manager != null) {
                    Utils.hideSoftKeyboard(MainActivity.this);
                    if (manager.getBackStackEntryCount() >= 0) {
                        BaseFragment currentFragment = (BaseFragment) manager.findFragmentById(R.id.frame_container);
                        if (currentFragment != null) {
                            currentFragment.onFragResume();
                        }
                    }
                    if (manager.getBackStackEntryCount() == 0) {
                        //setupToolbarTitle(current_title);
                    }
                  /*  linearLayoutSidActions.setVisibility(manager.getBackStackEntryCount() == 0 ? View.VISIBLE : View.GONE);
                    linearLayoutToolbarActions.setVisibility(manager.getBackStackEntryCount() == 0 ? View.VISIBLE : View.GONE);*/
                    imageviewBack.setVisibility(manager.getBackStackEntryCount() > 0 ? View.VISIBLE : View.GONE);
                    imageviewMenu.setVisibility(manager.getBackStackEntryCount() == 0 ? View.VISIBLE : View.GONE);
                }
            }
        };

        return result;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCustomerSelectedEvent(ReleaseScreenEvent event) {
        showProgressBar(false);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();
        // start lock task mode if it's not already active
        am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        // ActivityManager.getLockTaskModeState api is not available in pre-M.
        if (am != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // For SDK version 23 and above.
                if (am.getLockTaskModeState() != ActivityManager.LOCK_TASK_MODE_NONE) {
                    //mLockTask();
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // When SDK version >= 21. This API is deprecated in 23.
                if (!am.isInLockTaskMode()) {
                    //mLockTask();
                }
            }
        }
    }

    private void mLockTask() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                try {
                    startLockTask();
                } catch (Exception exception) {
                    Log.v("MainActivity", "startLockTask - Invalid task, not in foreground");
                }
            }
        }, 1500);
    }

    public void showProgressBar(final boolean show) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                linearLayoutProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @OnClick({R.id.imageview_menu, R.id.imageview_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageview_back:
                onBackPressed();
                break;
            case R.id.imageview_menu:
                drawerLayout.openDrawer(Gravity.RIGHT);
                break;
        }
    }

    public void updateDayStatus(boolean open) {
        if (open) {
            daily = new Daily();
            daily.setId(Utils.generateTimestamp());
            daily.setReference(UUID.randomUUID().toString());
            try {
                daily.setStartTime(Utils.getCurrentFullDate());
                daily.setEndTime(Utils.getCurrentFullDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            daily.setUserID(user.getIdUtilisateur());
            daily.setOpened(true);
            insertDaily(daily);

            if (Utils.isNetworkAvailable(this)) {
                //new ActionRequestsManager(this).startSynchronisation();
                Prefs.setBooleanPref(Prefs.IS_SYNCHRONIZED, false, this);
                Intent intent = new Intent(this, SplashActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "No internet available", Toast.LENGTH_SHORT).show();
            }

        } else {
            try {
                daily.setEndTime(Utils.getCurrentFullDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            daily.setOpened(false);

            //dbTableHelper.updateDataInDB(new JSONObject(new Gson().toJson(daily)), Constants.TBL_DAILY, "id", false, null);
            updateDaily(daily);
            if (Utils.isNetworkAvailable(this)) {
                new ActionRequestsManager(this).startSynchronisation();
            }


            daily = null;

        }
        //TODO check day model in DB
        Toast.makeText(getApplicationContext(), open ? getString(R.string.toast_day_open) : getString(R.string.toast_day_close), Toast.LENGTH_LONG).show();
        checkDaily();
    }

    private void insertDaily(final Daily daily) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                getLafargeDatabase().dailyDao().insertDaily(daily);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Snackbar.make(frameContainer, "Daily added", Snackbar.LENGTH_SHORT);
                        Log.d("SUCCESS ROOM", "INSERT daily");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ERROR ROOM insert daily", e.getMessage());
                    }
                });
    }

    private void updateDaily(final Daily daily) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                getLafargeDatabase().dailyDao().update(daily);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Snackbar.make(frameContainer, "Daily updated", Snackbar.LENGTH_SHORT);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ERROR ROOM update daily", e.getMessage());
                    }
                });
    }


    @Override
    public void onBackPressed() {
        linearLayoutProgress.setVisibility(View.GONE);
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            //if (linearLayoutHomeActions.getVisibility() == View.GONE) {
            Toast.makeText(getApplicationContext(), R.string.exit_app_forbidden, Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

    public void closeDrawers() {
        drawerLayout.closeDrawers();
    }

    public Cart getLastOpenedCart() {
        return lastOpenedCart;
    }

    public void setLastOpenedCart(Cart lastOpenedCart) {
        this.lastOpenedCart = lastOpenedCart;
    }
}
