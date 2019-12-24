package io.mintit.lafarge.ui.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.mintit.lafarge.R;
import io.mintit.lafarge.Retrofit.ApiInterface;
import io.mintit.lafarge.Retrofit.ApiManager;
import io.mintit.lafarge.adapter.ProductsAdapter;
import io.mintit.lafarge.adapter.SellersAdapter;
import io.mintit.lafarge.data.Data;
import io.mintit.lafarge.events.CustomerSelectedEvent;
import io.mintit.lafarge.events.ProductSelectedEvent;
import io.mintit.lafarge.model.Article;
import io.mintit.lafarge.model.Cart;
import io.mintit.lafarge.model.Category;
import io.mintit.lafarge.model.Seller;
import io.mintit.lafarge.model.Tarif;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.utils.DebugLog;
import io.mintit.lafarge.utils.Prefs;
import io.mintit.lafarge.utils.Utils;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends BaseFragment implements ProductsAdapter.OnItemClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_cart = "param1";
    private static final String ARG_CLOSED_CART = "param2";
    private static final String ARG_PARAM_ON_GOING_DAILY = "ARG_PARAM_ON_GOING_DAILY";

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
    @BindView(R.id.linearLayout_submit_cart)
    LinearLayout linearLayoutSubmitCart;
    @BindView(R.id.linearLayout_switch_cart)
    LinearLayout linearLayoutSwitchCart;
    @BindView(R.id.view_cover)
    View viewCover;
    @BindView(R.id.textView_open_day)
    TextView textViewOpenDay;
    Unbinder unbinder;
    @Nullable
    @BindView(R.id.scroll)
    ScrollView mScrollView;
    @BindView(R.id.frame_products)
    FrameLayout frameProducts;

    private boolean isClosedCart;
    private MainActivity activity;
    private ProductsAdapter productsAdapter;
    private Cart cart;
    private boolean newCart;
    private boolean onGoingDaily;
    private Gson gson = new Gson();
    private Seller selectedSeller;
    private String toast;
    ArrayList<Article> productsList = new ArrayList<>();
    ProductsFragment productsFragment;

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

    public static CartFragment newInstance(Cart mCart, boolean closedCard, boolean onGoingDaily) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_cart, mCart);
        args.putBoolean(ARG_CLOSED_CART, closedCard);
        args.putBoolean(ARG_PARAM_ON_GOING_DAILY, onGoingDaily);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cart = getArguments().getParcelable(ARG_PARAM_cart);
            isClosedCart = getArguments().getBoolean(ARG_CLOSED_CART);
            onGoingDaily = getArguments().getBoolean(ARG_PARAM_ON_GOING_DAILY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        activity = (MainActivity) getActivity();
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        if(cart == null)
            isCartNull = true;
        else
            isCartNull = false;

        initViews();
        /*if (onGoingDaily) {
            newCart = cart == null;
            if (newCart) {
                checkDefaultSeller();
            }
        }*/
        selectedSeller = new Gson().fromJson(Prefs.getPref(Prefs.SELLER_LOGIN, activity), Seller.class);
        System.out.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG : " + selectedSeller.toString());
        newCart = cart == null;
        if (newCart) {
            checkDefaultSeller();
        }

        if (!newCart) {
            initData();
        }

        //----------READ CARTS
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productsFragment = ProductsFragment.newInstance(true, cart, false, false);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_products, productsFragment).commit();
    }


    @SuppressLint("CheckResult")
    private void checkDefaultSeller() {
        selectedSeller = new Gson().fromJson(Prefs.getPref(Prefs.SELLER_LOGIN, activity), Seller.class);
        activity.showProgressBar(true);
        setupCartWithSeller();
    }


    private void showSellersDialog() {
        if (Data.getInstance().getListSellers() != null) {
            activity.showProgressBar(false);
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);

            dialog.setContentView(R.layout.dialog_sellers);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            RecyclerView recyclerViewSellers = (RecyclerView) dialog.findViewById(R.id.recyclerView_sellers);
            final EditText edittextFilter = (EditText) dialog.findViewById(R.id.edittext_filter);
            LinearLayout linearLayoutCancel = (LinearLayout) dialog.findViewById(R.id.linearLayout_cancel);
            LinearLayout linearLayoutSubmit = (LinearLayout) dialog.findViewById(R.id.linearLayout_submit);
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
            recyclerViewSellers.setLayoutManager(layoutManager);
            final SellersAdapter sellersAdapter = new SellersAdapter(Data.getInstance().getListSellers(), activity, activity);
            recyclerViewSellers.setAdapter(sellersAdapter);

            sellersAdapter.setOnItemClickListener(new SellersAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Seller seller) {
                }
            });
            edittextFilter.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (edittextFilter.getText().length() > 0) {
                        sellersAdapter.getFilter().filter(edittextFilter.getText().toString());
                    } else {
                        sellersAdapter.reset(Data.getInstance().getListSellers());
                    }
                }
            });
            linearLayoutCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cart == null) {
                        activity.onBackPressed();
                    }
                    dialog.dismiss();
                }
            });
            linearLayoutSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sellersAdapter.getSelectedSeller() != null) {
                        if (cart == null) {
                            selectedSeller = sellersAdapter.getSelectedSeller();
                            setupCartWithSeller();
                            dialog.dismiss();
                        } else {
                            cart.setSellerLibelle(sellersAdapter.getSelectedSeller().getUtilisateur());
                            cart.setSellerLibelle(sellersAdapter.getSelectedSeller().getLibelle());
                            dialog.dismiss();
                            //textViewSeller.setText(cart.getSellerLibelle());

                        }
                    } else {
                        Utils.showAlert(getString(R.string.alert_select_cashier), activity, null, false, true);
                    }
                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        } else {
            loadSellers();
        }

    }

    private void setupCartWithSeller() {
        activity.showProgressBar(false);
        //textViewSeller.setText(selectedSeller.getLibelle());
        initCart();
    }

    private void initCart() {
        cart = new Cart();
        linearLayoutSubmitCart.setEnabled(true);
        cart.setId(Utils.generateTimestamp());
        System.out.println("IIIIIIIIIIIIIIII CART ID : " + cart.getId());
        /*if (activity.getDaily() != null) {
            cart.setDailyID(activity.getDaily().getId());
        }*/
        cart.setClosed(false);
        try {
            cart.setDate(Utils.getCurrentDate());
            cart.setTime(Utils.getCurrentTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cart.setAgentID(activity.getSellerLogin().getId()+"");
        cart.setAgentName(activity.getSellerLogin().getLibelle());
        cart.setSellerLibelle(selectedSeller.getLibelle());
        cart.setSellerId(selectedSeller.getId()+"");
        cart.setCurrencyId(activity.getEtablissement().getCurrencyId());
        productsAdapter.clear();
        updateTotal();
        initWithWalkthroughCustomer();
    }


    @SuppressLint("CheckResult")
    private void loadSellers() {
        activity.showProgressBar(true);
        activity.getLafargeDatabase().sellerDao().getSellers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Seller>>() {
                    @Override
                    public void accept(List<Seller> sellers) {
                        activity.showProgressBar(false);
                        if (sellers.size() > 0) {
                            Data.getInstance().setListSellers(sellers);

                            if (isAdded()) {
                                //showSellersDialog();
                            }
                        }
                    }
                });
    }


    private void initData() {
        DebugLog.d(gson.toJson(cart));
        if (cart != null) {
            if (cart.getCustomer() != null) {
                //switchPriceMode.setChecked(cart.isCompany());
                //switchPriceMode.setEnabled(false);

            }
            if (cart.getClosed()) {
                linearLayoutSubmitCart.setVisibility(View.GONE);
                linearLayoutSwitchCart.setVisibility(View.GONE);
                frameProducts.setVisibility(View.GONE);
            }
            //textViewSeller.setText(cart.getSellerLibelle());
            productsAdapter.addAll(cart.getProductList());
            textviewFirstName.setText(cart.getCustomerFirstName() + " " + cart.getCustomerLastName());
            textViewTotal.setText(cart.getTotal() + " " + cart.getCurrencyId());
            viewCover.setVisibility(cart.getClosed() ? View.VISIBLE : View.GONE);
            //textViewOpenDay.setVisibility(!onGoingDaily && !cart.getClosed() ? View.VISIBLE : View.GONE);
        }
    }

    private void initViews() {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerViewProducts.setLayoutManager(layoutManager);
        productsAdapter = new ProductsAdapter(new ArrayList<Article>(), activity, activity, true, isClosedCart);
        recyclerViewProducts.setAdapter(productsAdapter);
        productsAdapter.setOnItemClickListener(this);
        productsAdapter.setCartList(true);

        loadProducts();

        //viewCover.setVisibility(onGoingDaily ? View.GONE : View.VISIBLE);
        //textViewOpenDay.setVisibility(onGoingDaily ? View.GONE : View.VISIBLE);
        textViewOpenDay.setVisibility(View.GONE);
        viewCover.setVisibility(View.GONE);

    }

    private void scrollListener() {
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (mScrollView != null) {

                    if(cart!= null && cart.getClosed()){
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
        if(cart != null) {
            cart.setCustomer("SC000004");
            cart.setCompany(true);
            cart.setCustomerFirstName("");
            cart.setCustomerLastName("CLIENT de PASSAGE");
            textviewFirstName.setText(cart.getCustomerLastName());
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
            //cart.setCompany(switchPriceMode.isChecked());
            cart.setCustomer(event.getCustomer().getId() + "");
            cart.setCustomerFirstName(event.getCustomer().getFirstName());
            cart.setCustomerLastName(event.getCustomer().getLastName());
            updateCart();
            textviewFirstName.setText(cart.getCustomerFirstName() + " " + cart.getCustomerLastName());
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProductSelectedEvent(ProductSelectedEvent event) {
        boolean found = false;
        if (event.getProduct() != null) {
            //TODO check price type: detail/gros/promo
            Article product = event.getProduct();
            DebugLog.d("onClick " + product.getQty());
            if (product.getQty() > 0) {
                for (int i = 0; i < cart.getProductList().size(); i++) {
                    if (cart.getProductList().get(i).getEanCode().equals(product.getEanCode())) {
                        if ((cart.getProductList().get(i).getQty() + 1) <= product.getStock()) {
                            cart.getProductList().get(i).setQty(cart.getProductList().get(i).getQty() + event.getQte());
                            productsAdapter.updateItem(cart.getProductList().get(i));

                            updateCart();
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
                    cart.getProductList().add(product);
                    productsAdapter.add(product);
                    recyclerViewProducts.smoothScrollToPosition(cart.getProductList().size() - 1);
                    updateCart();
                }
            } else {
                Toast.makeText(activity, R.string.error_message_product_not_added, Toast.LENGTH_SHORT).show();
            }
            updateTotal();
        }
    }


    @SuppressLint("CheckResult")
    private Double getTarification(Article article) {
        final Tarif[] tarif = new Tarif[1];
        //SELECT * FROM Tarif where mCodeArticle = :codeArticle and regimePrix = :regimePrix COLLATE NOCASE
        //activity.getLafargeDatabase().tarifDao().getTarifByCodeArticleAndRegimePrix(article.getCodeArticle(), switchPriceMode.isChecked() ? Constants.TTC : Constants.HT)
        ArrayList<Tarif> tarifs = new ArrayList<>();

        if (tarifs.size() > 0) {
            tarif[0] = tarifs.get(0);
            cart.setCurrencyId(tarifs.get(0).getmCurrencyId());
        }

        if (tarif[0] != null) {
            return tarif[0].getPrice();
        }
        return article.getPrice();
    }


    @SuppressLint("CheckResult")
    private void updateCart() {
        /*activity.getLafargeDatabase().cartDao().getCartById(cart.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Cart>>() {
                    @Override
                    public void accept(List<Cart> carts) {
                        setupLastOpenedCart();
                        if (carts.size() > 0) {
                            updateCartDB();
                        } else {
                            insertCart();
                        }
                    }
                });*/
    }

    private void setupLastOpenedCart() {
        cart.setLastModification(Utils.generateTimestamp());
        DebugLog.d(cart.getLastModification());
        activity.setLastOpenedCart(cart);
    }

    private void updateCartDB() {
        /*Completable.fromAction(new Action() {
            @Override
            public void run() {
                activity.getLafargeDatabase().cartDao().updateBasketItem(cart);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        EventBus.getDefault().post(new UpdateCartEvent(cart));
                        activity.showProgressBar(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ERROR ROOM update cart", e.getMessage());
                    }
                });*/
    }


    private void insertCart() {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                activity.getLafargeDatabase().cartDao().insertCart(cart);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d("*****ROOM SUCCESS*****", "*********insert cart**********");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ERROR ROOM insert cart", e.getMessage());
                    }
                });
    }

    private void deleteCartById(String id){
        System.out.println("Delete Cart from the CartFragment with ID = " + id);
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                activity.getLafargeDatabase().cartDao().deleteCartById(id);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d("*****ROOM SUCCESS*****", "*********delete cart**********");
                        insertCart();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ERROR ROOM insert cart", e.getMessage());
                    }
                });
    }

    private void updateCartById(){
        System.out.println("update Cart from the CartFragment");
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                activity.getLafargeDatabase().cartDao().updateBasketItem(cart);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d("*****ROOM SUCCESS*****", "*********updated cart**********");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ERROR ROOM update cart", e.getMessage());
                    }
                });
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

    @OnClick({R.id.linearLayout_scan_code, R.id.linearLayout_add_client, R.id.linearLayout_add_product, R.id.linearLayout_switch_cart, R.id.linearLayout_submit_cart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linearLayout_scan_code:
                startScan();
                break;
            /*case R.id.linearLayout_seller:
                if(cart != null && cart.getClosed())
                    break;
                else
                    showSellersDialog();
                break;*/
            case R.id.linearLayout_add_client:
                if(cart!=null && isClosedCart)
                    break;
                else
                    //activity.addFragment(CustomerDirectoryFragment.newInstance(true, cart, switchPriceMode.isChecked()), "", true);
                    activity.addFragment(CustomerDirectoryFragment.newInstance(true, cart, true), "", true);
                break;

            case R.id.linearLayout_add_product:
                if (cart != null) {
                    activity.addFragment(ProductsFragment.newInstance(true, cart, false, false), "", true);
                } else {
                    Toast.makeText(activity, R.string.error_message_set_client, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.linearLayout_switch_cart:
                activity.addFragment(new PendingCartsFragment(), "", false);
                break;

            case R.id.linearLayout_submit_cart:
                if (cart != null) {
                    cart.setSellerLibelle(activity.getSellerLogin().getFirstName() + " " + activity.getSellerLogin().getLastName());
                    if (cart.getCustomer() == null  || cart.getProductList().size() == 0) {
                        Utils.showAlert(getString(R.string.error_message_non_complete_cart), activity, null, false, true);
                    } else {
                        activity.addFragment(CloseCartFragment.newInstance(cart, ""), "", true);
                    }
                } else {
                    Utils.showAlert(getString(R.string.error_message_non_complete_cart), activity, null, false, true);
                }
                System.out.println("SUBMITEEEEEEEEEEEED CAAART  :  " + cart.toString());
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
        for (int i = 0; i < cart.getProductList().size(); i++) {
            if (cart.getProductList().get(i).getEanCode().equals(product.getEanCode())) {
                cart.getProductList().get(i).setQty(product.getQty());
                break;
            }
        }
        updateTotal();
    }

    private void updateTotal() {
        Double total = 0.0;
        for (int i = 0; i < cart.getProductList().size(); i++) {
            Double totalByProduct = Double.valueOf(cart.getProductList().get(i).getPrice()) * cart.getProductList().get(i).getQty();
            total += totalByProduct;
        }
        textViewTotal.setText(total + " " + cart.getCurrencyId());
        cart.setTotal(total);
        updateCart();
    }

    @Override
    public void onItemRemove(Article product) {
        cart.getProductList().remove(product);
        updateTotal();
        productsAdapter.removeItem(product);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        System.out.println("EXIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIT");
        cart.setSellerId(activity.getSellerLogin().getId()+"");
        cart.setLastModification(Utils.generateTimestamp());
        //deleteCartById(cart.getId());
        if(isCartNull)
            insertCart();
        else
            updateCartById();
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
        ApiInterface service = ApiManager.createService(ApiInterface.class, Prefs.getPref(Prefs.TOKEN, getContext()));
        Call<ArrayList<Article>> productCall = service.getarticle("101");
        productCall.enqueue(new Callback<ArrayList<Article>>() {
            @Override
            public void onResponse(Call<ArrayList<Article>> call, Response<ArrayList<Article>> response) {
                ArrayList<Article> listArticle = response.body();
                System.out.println("exeption : " + response.body());
                for(Article s: listArticle){
                    System.out.println("SEEEELLLLLEEEER : " + s.toString());
                    Article p = new Article();
                    p.setId(s.getId());
                    p.setPrice(s.getPrice());
                    p.setName(s.getName());
                    p.setStock(s.getStock());
                    p.setProductCode(s.getProductCode());
                    p.setCategory(s.getCategory());
                    p.setEanCode(s.getEanCode());
                    articleDbList.add(p);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Article>> call, Throwable throwable){

                System.out.println("Errorr :");
            }
        });
    }
}
