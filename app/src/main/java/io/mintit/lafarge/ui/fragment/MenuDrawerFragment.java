package io.mintit.lafarge.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.mintit.lafarge.R;
import io.mintit.lafarge.model.AppConfig;
import io.mintit.lafarge.model.Reservation;
import io.mintit.lafarge.ui.activity.LoginActivity;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.ui.activity.SplashActivity;
import io.mintit.lafarge.utils.Prefs;
import io.mintit.lafarge.utils.Utils;


public class MenuDrawerFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    @BindView(R.id.imageView_close)
    ImageView imageViewClose;
    //@BindView(R.id.linearLayout_cart_pending)
    //LinearLayout linearLayoutCartPending;
    @BindView(R.id.linearLayout_cart_new)
    LinearLayout linearLayoutCartNew;
    @BindView(R.id.linearLayout_cart_list)
    LinearLayout linearLayoutCartList;
    @BindView(R.id.linearLayout_cart_container)
    ExpandableRelativeLayout linearLayoutCartContainer;
    @BindView(R.id.linearLayout_cart)
    LinearLayout linearLayoutCart;
    @BindView(R.id.linearLayout_list_products)
    LinearLayout linearLayoutListProducts;
    @BindView(R.id.linearLayout_products_container)
    ExpandableRelativeLayout linearLayoutProductsContainer;
    @BindView(R.id.linearLayout_products)
    LinearLayout linearLayoutProducts;
    @BindView(R.id.linearLayout_customer_new)
    LinearLayout linearLayoutCustomerNew;
    @BindView(R.id.linearLayout_customer_list)
    LinearLayout linearLayoutCustomerList;
    @BindView(R.id.linearLayout_customer_container)
    ExpandableRelativeLayout linearLayoutCustomerContainer;
    @BindView(R.id.linearLayout_customer)
    LinearLayout linearLayoutCustomer;
    //@BindView(R.id.linearLayout_inventory)
    //LinearLayout linearLayoutInventory;
    //@BindView(R.id.linearLayout_daily)
    //LinearLayout linearLayoutDaily;
    @BindView(R.id.linearLayout_settings)
    LinearLayout linearLayoutSettings;
    @BindView(R.id.linearLayout_logout)
    LinearLayout linearLayoutLogout;
    Unbinder unbinder;
    @BindView(R.id.drop_down_drawer_cart)
    ImageView dropDownDrawerCart;
    @BindView(R.id.drop_down_drawer_product)
    ImageView dropDownDrawerProduct;
    @BindView(R.id.drop_down_drawer_customer)
    ImageView dropDownDrawerCustomer;
    @BindView(R.id.drop_down_drawer_reservation)
    ImageView dropDownDrawerReservation;


    @BindView(R.id.linearLayout_reservation_list)
    LinearLayout linearLayoutReservationList;
    @BindView(R.id.linearLayout_reservation_new)
    LinearLayout linearLayoutReservationNew;
    @BindView(R.id.linearLayout_reservation_container)
    ExpandableRelativeLayout linearLayoutReservationContainer;

    private MainActivity activity;
    private Gson gson = new Gson();
    AppConfig appConfig;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_drawer, container, false);
        unbinder = ButterKnife.bind(this, view);
        activity = (MainActivity) getActivity();
        initViews();
        //---------------------
        appConfig = new AppConfig();
        appConfig = gson.fromJson(Prefs.getPref(Prefs.APP_CONFIG, getContext()), AppConfig.class);
        //System.out.println("AP COOOOOOOOOOOOOOOOOOOONFIG : " + appConfig);

        if(appConfig.getCatalogProduct() == 0){
            linearLayoutListProducts.setVisibility(View.GONE);
        } else {
            linearLayoutListProducts.setVisibility(View.VISIBLE);
        }
        //-----Disabling Customer Menu-----------------------
        if(appConfig.getNewCustomer() == 0){
            linearLayoutCustomerNew.setVisibility(View.GONE);
        } else {
            linearLayoutCustomerNew.setVisibility(View.VISIBLE);
        }
        if(appConfig.getListCustomer() == 0){
            linearLayoutCustomerList.setVisibility(View.GONE);
        } else {
            linearLayoutCustomerList.setVisibility(View.VISIBLE);
        }
        if((appConfig.getListCustomer() == 0) && (appConfig.getNewCustomer() == 0)){
            linearLayoutCustomer.setVisibility(View.GONE);
        } else {
            linearLayoutCustomer.setVisibility(View.VISIBLE);
        }
        //---------------------------------------------------

        //---------------------
        return view;
    }

    private void initViews() {
        linearLayoutCartContainer.collapse();
        linearLayoutProductsContainer.collapse();
        linearLayoutCustomerContainer.collapse();
        linearLayoutReservationContainer.collapse();


        /*if (linearLayoutCartContainer.isExpanded()) {
            linearLayoutCartContainer.collapse();
        }
        if (linearLayoutProductsContainer.isExpanded()) {
            linearLayoutProductsContainer.collapse();
        }
        if (linearLayoutCustomerContainer.isExpanded()) {
            linearLayoutCustomerContainer.collapse();
        }*/

        linearLayoutCartContainer.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onPreOpen() {
                if (linearLayoutCustomerContainer.isExpanded()) {
                    linearLayoutCustomerContainer.collapse();
                }
                if (linearLayoutProductsContainer.isExpanded()) {
                    linearLayoutProductsContainer.collapse();
                }
                if (linearLayoutReservationContainer.isExpanded()) {
                    linearLayoutReservationContainer.collapse();
                }
                rotate(dropDownDrawerCart, 0.0f, 180.0f);

            }

            @Override
            public void onPreClose() {

            }

            @Override
            public void onOpened() {

            }

            @Override
            public void onClosed() {
                rotate(dropDownDrawerCart, 180.0f, 0.0f);

            }
        });

        linearLayoutCustomerContainer.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onPreOpen() {
                if (linearLayoutCartContainer.isExpanded()) {
                    linearLayoutCartContainer.collapse();
                }
                if (linearLayoutProductsContainer.isExpanded()) {
                    linearLayoutProductsContainer.collapse();
                }
                if (linearLayoutReservationContainer.isExpanded()) {
                    linearLayoutReservationContainer.collapse();
                }
                rotate(dropDownDrawerCustomer, 0.0f, 180.0f);
            }

            @Override
            public void onPreClose() {

            }

            @Override
            public void onOpened() {

            }

            @Override
            public void onClosed() {
                rotate(dropDownDrawerCustomer, 180.0f, 0.0f);

            }
        });
        linearLayoutProductsContainer.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onPreOpen() {
                if (linearLayoutCustomerContainer.isExpanded()) {
                    linearLayoutCustomerContainer.collapse();
                }
                if (linearLayoutCartContainer.isExpanded()) {
                    linearLayoutCartContainer.collapse();
                }
                if (linearLayoutReservationContainer.isExpanded()) {
                    linearLayoutReservationContainer.collapse();
                }
                rotate(dropDownDrawerProduct, 0.0f, 180.0f);
            }

            @Override
            public void onPreClose() {

            }

            @Override
            public void onOpened() {

            }

            @Override
            public void onClosed() {
                rotate(dropDownDrawerProduct, 180.0f, 0.0f);

            }
        });
        linearLayoutReservationContainer.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onPreOpen() {
                if (linearLayoutCustomerContainer.isExpanded()) {
                    linearLayoutCustomerContainer.collapse();
                }
                if (linearLayoutCartContainer.isExpanded()) {
                    linearLayoutCartContainer.collapse();
                }
                if (linearLayoutProductsContainer.isExpanded()) {
                    linearLayoutProductsContainer.collapse();
                }
                rotate(dropDownDrawerReservation, 0.0f, 180.0f);
            }

            @Override
            public void onPreClose() {

            }

            @Override
            public void onOpened() {

            }

            @Override
            public void onClosed() {
                rotate(dropDownDrawerReservation, 180.0f, 0.0f);

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.linearLayout_reservation, R.id.linearLayout_reservation_new, R.id.linearLayout_reservation_list, R.id.imageView_close, R.id.linearLayout_logout, R.id.linearLayout_settings, R.id.linearLayout_cart_new, R.id.linearLayout_cart_list, R.id.linearLayout_cart, R.id.linearLayout_list_products, R.id.linearLayout_products, R.id.linearLayout_customer, R.id.linearLayout_customer_new, R.id.linearLayout_customer_list, R.id.linearLayout_action_requests})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageView_close:
                activity.closeDrawers();
                break;
            case R.id.linearLayout_logout:
                Prefs.setBooleanPref(Prefs.IS_CONNECTED, false, activity);
                Prefs.setPref(Prefs.TOKEN, "", activity);
                activity.startActivity(new Intent(activity, SplashActivity.class));
                activity.finishAffinity();
                break;
            case R.id.linearLayout_settings:
                 activity.addFragment(new SettingsFragment(), "", false);
                break;
            case R.id.linearLayout_cart_new:
                //if(activity.getDaily()!= null) {
                activity.addFragment(CartFragment.newInstance(null, false, activity.getDaily() != null), "", false);
                //}else {
                //   Toast.makeText(getActivity(),R.string.open_daily_toast,Toast.LENGTH_SHORT).show();
                //}
                break;
            case R.id.linearLayout_cart_list:
                activity.addFragment(new PendingCartsFragment(), "", false);
                break;

            case R.id.linearLayout_cart:
                linearLayoutCartContainer.toggle();
                break;
            case R.id.linearLayout_list_products:
                activity.addFragment(ProductsFragment.newInstance(false, null, false,true), "", false);
                break;

            case R.id.linearLayout_products:
                linearLayoutProductsContainer.toggle();
                break;
            case R.id.linearLayout_customer_new:
                activity.addFragment(CustomerFragment.newInstance(null, "", false, false, false), "", false);
                break;
            case R.id.linearLayout_customer:
                linearLayoutCustomerContainer.toggle();
                break;
            case R.id.linearLayout_customer_list:
                activity.addFragment(CustomerDirectoryFragment.newInstance(false, null, false), "", false);
                break;
            case R.id.linearLayout_reservation:
                linearLayoutReservationContainer.toggle();
                break;
            case R.id.linearLayout_reservation_list:
                //activity.addFragment(CustomerDirectoryFragment.newInstance(false, null, false), "", false);
                //Toast.makeText(activity,"Reservation List under construction",Toast.LENGTH_LONG).show();
                activity.addFragment(new ReservationListFragment(), "", false);
                break;
            case R.id.linearLayout_reservation_new:
                activity.addFragment(ReservationFragment.newInstance(null), "", false);
                //Toast.makeText(activity,"Reservation New under construction",Toast.LENGTH_LONG).show();
                break;
            case R.id.linearLayout_action_requests:
                activity.addFragment(new OperationsFragment(), "", false);

                break;
        }
    }

    private void manageDaily() {
        if(activity.getDaily() != null && activity.getLastOpenedCart() != null){
            Toast.makeText(activity, "Close the pending cart first", Toast.LENGTH_SHORT).show();
        }else {
            Utils.showAlert(activity.getDaily() == null ? getString(R.string.alert_message_open_journey) : getString(R.string.alert_message_close_journey), activity, new Runnable() {
                @Override
                public void run() {
                    activity.updateDayStatus(activity.getDaily() == null);

                }
            }, true, true);
        }
    }

    void rotate(ImageView arrowImageView, float from, float to) {
        //arrowImageView
        RotateAnimation rotateAnimation = new RotateAnimation(from, to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());
        rotateAnimation.setRepeatCount(0);
        rotateAnimation.setDuration(200);
        rotateAnimation.setFillAfter(true);
        arrowImageView.startAnimation(rotateAnimation);
    }
}
