package io.mintit.lafarge.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.mintit.lafarge.R;
import io.mintit.lafarge.adapter.ProductsAdapter;
import io.mintit.lafarge.data.Data;
import io.mintit.lafarge.events.CustomerSelectedEvent;
import io.mintit.lafarge.events.ProductSelectedEvent;
import io.mintit.lafarge.model.Article;
import io.mintit.lafarge.model.Category;
import io.mintit.lafarge.model.Reservation;
import io.mintit.lafarge.model.Seller;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.utils.DebugLog;
import io.mintit.lafarge.utils.Prefs;
import io.mintit.lafarge.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReservationFragment extends BaseFragment implements ProductsAdapter.OnItemClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_RES = "param1";

    @BindView(R.id.textview_first_name)
    TextView textviewFirstName;
    @BindView(R.id.linearLayout_add_client)
    LinearLayout linearLayoutAddClient;
    @BindView(R.id.recyclerView_products)
    RecyclerView recyclerViewProducts;
    @BindView(R.id.linearLayout_add_product)
    LinearLayout linearLayoutAddProduct;
    @BindView(R.id.textView_Total)
    TextView textViewTotal;
    @BindView(R.id.linearLayout_submit_res)
    LinearLayout linearLayoutSubmitRes;
    @BindView(R.id.linearLayout_switch_res)
    LinearLayout linearLayoutSwitchRes;
    @BindView(R.id.view_cover)
    View viewCover;
    Unbinder unbinder;
    @Nullable
    @BindView(R.id.scroll)
    ScrollView mScrollView;
    @BindView(R.id.frame_products)
    FrameLayout frameProducts;

    private MainActivity activity;
    private ProductsAdapter productsAdapter;
    private Reservation res;
    private boolean newCart;
    private Gson gson = new Gson();
    private Seller selectedSeller;
    private String toast;
    ArrayList<Article> productsList = new ArrayList<>();
    ProductsReservationFragment productsFragment;

    private ArrayList<Category> categoriesList = new ArrayList<>();
    private ArrayList<Article> articleDbList = new ArrayList<>();
    Article p1 = new Article();
    Article p2 = new Article();
    Article p3 = new Article();
    Article p4 = new Article();
    Article p5 = new Article();
    Article p6 = new Article();
    Article p7 = new Article();
    Category c1 = new Category();
    Category c2 = new Category();
    Category c3 = new Category();

    private  boolean isCartNull;

    public static ReservationFragment newInstance(Reservation mCart) {
        ReservationFragment fragment = new ReservationFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_RES, mCart);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            res = getArguments().getParcelable(ARG_PARAM_RES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reservation, container, false);
        activity = (MainActivity) getActivity();
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        if(res == null)
            isCartNull = true;
        else
            isCartNull = false;

        initViews();
        selectedSeller = new Gson().fromJson(Prefs.getPref(Prefs.SELLER_LOGIN, activity), Seller.class);
        newCart = res == null;
        if (newCart) {
            checkDefaultSeller();
        }
        if (!newCart) {
            initData();
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productsFragment = ProductsReservationFragment.newInstance(true, res, false, false);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_products, productsFragment).commit();
    }


    @SuppressLint("CheckResult")
    private void checkDefaultSeller() {
        selectedSeller = new Gson().fromJson(Prefs.getPref(Prefs.SELLER_LOGIN, activity), Seller.class);
        activity.showProgressBar(true);
        setupCartWithSeller();
    }

    private void setupCartWithSeller() {
        activity.showProgressBar(false);
        //textViewSeller.setText(selectedSeller.getLibelle());
        initCart();
    }

    private void initCart() {
        res = new Reservation();
        linearLayoutSubmitRes.setEnabled(true);
        res.setId(Utils.generateTimestamp());
        System.out.println("IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII CART ID : " + res.getId());
        /*if (activity.getDaily() != null) {
            res.setDailyID(activity.getDaily().getId());
        }*/
        res.setClosed(false);
        try {
            res.setDate(Utils.getCurrentDate());
            res.setTime(Utils.getCurrentTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        res.setAgentID(activity.getSellerLogin().getId()+"");
        res.setAgentName(activity.getSellerLogin().getLibelle());
        res.setSellerLibelle(selectedSeller.getLibelle());
        res.setSellerId(selectedSeller.getId()+"");
        res.setCurrencyId(activity.getEtablissement().getCurrencyId());
        productsAdapter.clear();
        updateTotal();
        initWithWalkthroughCustomer();
    }


    private void initData() {
        DebugLog.d(gson.toJson(res));
        if (res != null) {
            if (res.getCustomer() != null) {
            }
            if (res.getClosed()) {
                linearLayoutSubmitRes.setVisibility(View.GONE);
                linearLayoutSwitchRes.setVisibility(View.GONE);
                frameProducts.setVisibility(View.GONE);
            }
            //textViewSeller.setText(res.getSellerLibelle());
            productsAdapter.addAll(res.getProductList());
            textviewFirstName.setText(res.getCustomerFirstName() + " " + res.getCustomerLastName());
            textViewTotal.setText(res.getTotal() + " " + res.getCurrencyId());
            viewCover.setVisibility(res.getClosed() ? View.VISIBLE : View.GONE);
            //textViewOpenDay.setVisibility(!onGoingDaily && !res.getClosed() ? View.VISIBLE : View.GONE);
        }
    }

    private void initViews() {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerViewProducts.setLayoutManager(layoutManager);
        productsAdapter = new ProductsAdapter(new ArrayList<Article>(), activity, activity, true, false);
        recyclerViewProducts.setAdapter(productsAdapter);
        productsAdapter.setOnItemClickListener(this);
        productsAdapter.setCartList(true);

        loadProducts();

        //viewCover.setVisibility(onGoingDaily ? View.GONE : View.VISIBLE);
        //textViewOpenDay.setVisibility(onGoingDaily ? View.GONE : View.VISIBLE);
        viewCover.setVisibility(View.GONE);

    }

    private void scrollListener() {
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (mScrollView != null) {

                    if(res!= null && res.getClosed()){
                    }
                    else {
                        View view = (View) mScrollView.getChildAt(mScrollView.getChildCount() - 1);
                        int diff = (view.getBottom() - (mScrollView.getHeight() + mScrollView.getScrollY()));
                        if (diff == 0) {
                            productsFragment.addProducts();
                        }
                    }
                }
            }
        });
    }


    @SuppressLint("CheckResult")
    private void loadProducts() {
        //activity.getLafargeDatabase().articleDao().getArticleByStock()
        reloadProducts();
        productsList.addAll(articleDbList);
        scrollListener();
    }


    @SuppressLint("CheckResult")
    private void initWithWalkthroughCustomer() {
        //initialize the customer with Walkthrough "Generic Customer"
        if(res != null) {
            res.setCustomer("SC000004");
            res.setCompany(true);
            res.setCustomerFirstName("");
            res.setCustomerLastName("CLIENT de PASSAGE");
            textviewFirstName.setText(res.getCustomerLastName());
        }
    }

    @Override
    public void onFragResume() {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCustomerSelectedEvent(CustomerSelectedEvent event) {
        if (event.getCustomer() != null) {
            //switchPriceMode.setEnabled(false);
            //res.setCompany(switchPriceMode.isChecked());
            res.setCustomer(event.getCustomer().getId() + "");
            res.setCustomerFirstName(event.getCustomer().getFirstName());
            res.setCustomerLastName(event.getCustomer().getLastName());
            textviewFirstName.setText(res.getCustomerFirstName() + " " + res.getCustomerLastName());
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProductSelectedEvent(ProductSelectedEvent event) {
        boolean found = false;
        if (event.getProduct() != null) {
            //TODO check price type: detail/gros/promo
            Article product = event.getProduct();
            DebugLog.d("onClick " + product.getQty());
            if (product.getStock() > 0) {
                for (int i = 0; i < res.getProductList().size(); i++) {
                    if (res.getProductList().get(i).getEanCode().equals(product.getEanCode())) {
                        if ((res.getProductList().get(i).getQty() + 1) <= product.getStock()) {
                            res.getProductList().get(i).setQty(res.getProductList().get(i).getQty() + event.getQte());
                            productsAdapter.updateItem(res.getProductList().get(i));
                        } else {
                            Toast.makeText(activity, R.string.error_message_product_not_added, Toast.LENGTH_SHORT).show();
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    product.setQty(event.getQte());
                    // product.setTarifPrice(String.valueOf(getTarification(product)));
                    res.getProductList().add(product);
                    productsAdapter.add(product);
                    recyclerViewProducts.smoothScrollToPosition(res.getProductList().size() - 1);
                }
            } else {
                Toast.makeText(activity, R.string.error_message_product_not_added, Toast.LENGTH_SHORT).show();
            }
            updateTotal();
        }
    }

    private void setupLastOpenedCart() {
        res.setLastModification(Utils.generateTimestamp());
        DebugLog.d(res.getLastModification());
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void startScan() {
        IntentIntegrator.forSupportFragment(this).initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("SCAAAAAAAAAAAAAAAAAAAAAAAAAAN from CART FRAGMENT !!!!!!!!!!");
        DebugLog.d("onActivityResult");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                // toast = "Cancelled from fragment";
            } else {
                for (int i = 0; i < Data.getInstance().getListProducts().size(); i++) {
                    if (Data.getInstance().getListProducts().get(i).getEanCode().equals(result.getContents())) {
                        EventBus.getDefault().post(new ProductSelectedEvent(Data.getInstance().getListProducts().get(i), 1,false));
                        toast = "Scanned product: " + result.getContents();
                        break;
                    }
                }
                startScan();
            }
            // At this point we may or may not have a reference to the activity
            displayToast();
        }

    }

    private void displayToast() {
        if (getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
    }

    @OnClick({R.id.linearLayout_scan_code, R.id.linearLayout_add_client, R.id.linearLayout_add_product, R.id.linearLayout_switch_res, R.id.linearLayout_submit_res})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linearLayout_scan_code:
                startScan();
                break;
            /*case R.id.linearLayout_seller:
                if(res != null && res.getClosed())
                    break;
                else
                    showSellersDialog();
                break;*/
            case R.id.linearLayout_add_client:
                System.out.println("Clicked HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEERE");
                activity.addFragment(CustomerDirectoryFragment.newInstanceForReservation(true, res, true), "", true);
                break;

            case R.id.linearLayout_add_product:
                if (res != null) {
                    activity.addFragment(ProductsReservationFragment.newInstance(true, res, false, false), "", true);
                } else {
                    Toast.makeText(activity, R.string.error_message_set_client, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.linearLayout_switch_res:
                activity.addFragment(new ReservationListFragment(), "", false);
                break;

            case R.id.linearLayout_submit_res:
                if (res != null) {
                    res.setSellerLibelle(activity.getSellerLogin().getFirstName() + " " + activity.getSellerLogin().getLastName());
                    if (res.getCustomer() == null  || res.getProductList().size() == 0) {
                        Utils.showAlert(getString(R.string.error_message_non_complete_cart), activity, null, false, true);
                    } else {
                        //activity.addFragment(CloseCartFragment.newInstance(cart, ""), "", true);
                    }
                } else {
                    Utils.showAlert(getString(R.string.error_message_non_complete_cart), activity, null, false, true);
                }
                break;
        }
    }

    @Override
    public void onItemClick(Article product) {
    }

    @Override
    public void onItemAdd(Article product, int pickedNumber, boolean fromdetail) {
        updateTotal();
    }


    @Override
    public void onItemUpdate(Article product) {
        for (int i = 0; i < res.getProductList().size(); i++) {
            if (res.getProductList().get(i).getEanCode().equals(product.getEanCode())) {
                res.getProductList().get(i).setQty(product.getQty());
                break;
            }
        }
        updateTotal();
    }

    private void updateTotal() {
        Double total = 0.0;
        for (int i = 0; i < res.getProductList().size(); i++) {
            Double totalByProduct = Double.valueOf(res.getProductList().get(i).getPrice()) * res.getProductList().get(i).getQty();
            total += totalByProduct;
        }
        textViewTotal.setText(total + " " + res.getCurrencyId());
        res.setTotal(total);
    }

    @Override
    public void onItemRemove(Article product) {
        res.getProductList().remove(product);
        updateTotal();
        productsAdapter.removeItem(product);
    }

    //----------------------
    private void reloadCateg(){
        c1.setId((long) 11);
        c1.setLibelle("Chaussures");
        c1.setLevel((long) 3);
        c2.setId((long) 22);
        c2.setLibelle("Shirts");
        c2.setLevel((long) 3);
        c3.setId((long) 33);
        c3.setLibelle("Trousers");
        c3.setLevel((long) 3);
        categoriesList.add(c1);
        categoriesList.add(c2);
        categoriesList.add(c3);
    }

    private void reloadProducts(){
        p1.setId(1);
        p1.setName("Chaussure X");
        p1.setPrice(18.5);
        p1.setStock(45);
        p1.setProductCode("CN5268");
        p1.setEanCode("84165787");
        p1.setCategory(c1);
        p1.setImageProduct("https://images-na.ssl-images-amazon.com/images/I/71VTSkvMa8L._UY395_.jpg");
        p1.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec massa orci, dignissim a scelerisque nec, suscipit et diam. Sed non tristique nunc, sed mollis nibh.");

        p2.setId(2);
        p2.setName("Pantalon GHF");
        p2.setPrice(66.8);
        p2.setStock(124);
        p2.setProductCode("PL1110");
        p2.setCategory(c3);
        p2.setEanCode("1111");

        p3.setId(3);
        p3.setName("Shorts KK");
        p3.setPrice(55.9);
        p3.setStock(66);
        p3.setProductCode("SR8584");
        p3.setCategory(c3);
        p3.setEanCode("2222");

        p4.setId(4);
        p4.setName("Chaussure POP");
        p4.setPrice(190.4);
        p4.setStock(5);
        p4.setProductCode("CH9939");
        p4.setCategory(c1);
        p4.setEanCode("3333");

        p5.setId(5);
        p5.setName("Chaussure H");
        p5.setPrice(100.0);
        p5.setStock(26);
        p5.setProductCode("CH5545");
        p5.setCategory(c1);
        p5.setEanCode("4444");

        p6.setId(6);
        p6.setName("Jupe M");
        p6.setPrice(80.0);
        p6.setStock(200);
        p6.setProductCode("JP7857");
        p6.setCategory(c3);
        p6.setEanCode("5555");

        p7.setId(7);
        p7.setName("Polo");
        p7.setPrice(70.5);
        p7.setStock(10);
        p7.setProductCode("PO9514");
        p7.setCategory(c2);
        p7.setEanCode("6666");

        articleDbList.add(p1);
        articleDbList.add(p2);
        articleDbList.add(p3);
        articleDbList.add(p4);
        articleDbList.add(p5);
        articleDbList.add(p6);
        articleDbList.add(p7);
    }

}
