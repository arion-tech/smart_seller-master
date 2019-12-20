package io.mintit.lafarge.ui.fragment;


import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
import io.mintit.lafarge.adapter.ProductsAdapter;
import io.mintit.lafarge.adapter.SalesAdapter;
import io.mintit.lafarge.events.CustomerSelectedEvent;
import io.mintit.lafarge.events.UpdateCustomerInfoEvent;
import io.mintit.lafarge.model.Article;
import io.mintit.lafarge.model.Cart;
import io.mintit.lafarge.model.Category;
import io.mintit.lafarge.model.Customer;
import io.mintit.lafarge.model.Reservation;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.utils.DebugLog;
import io.mintit.lafarge.utils.Utils;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerProfileFragment extends BaseFragment implements SalesAdapter.OnItemClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_CUSTOMER = "param_customer";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM_SELECT = "select_mode";
    @BindView(R.id.linearlayout_associate_to_cart)
    LinearLayout linearlayoutAssociateToCart;
    @BindView(R.id.linearLayout_actions)
    LinearLayout linearLayoutActions;
    @BindView(R.id.linearLayout_edit)
    LinearLayout linearLayoutEdit;
    @BindView(R.id.textView_name)
    TextView textViewName;
    @BindView(R.id.textView_email)
    TextView textViewEmail;
    @BindView(R.id.textView_phone)
    TextView textViewPhone;
    @BindView(R.id.textView_address)
    TextView textViewAddress;
    @BindView(R.id.radioButton_history)
    RadioButton radioButtonHistory;
    @BindView(R.id.radioButton_whishlist)
    RadioButton radioButtonWhishlist;
    @BindView(R.id.radioButton_recommendation)
    RadioButton radioButtonRecommendation;
    @BindView(R.id.recyclerView_whishlist)
    RecyclerView recyclerViewWhishlist;
    @BindView(R.id.recyclerView_recommendation)
    RecyclerView recyclerViewRecommendation;
    @BindView(R.id.linearLayout_whishlist)
    LinearLayout linearLayoutWhishlist;
    @BindView(R.id.linearLayout_recommendation)
    LinearLayout linearLayoutRecommendation;
    @BindView(R.id.radioButton_orders)
    RadioButton radioButtonOrders;
    @BindView(R.id.radioButton_products)
    RadioButton radioButtonProducts;
    @BindView(R.id.recyclerView_history)
    RecyclerView recyclerViewHistory;
    @BindView(R.id.linearLayout_history)
    LinearLayout linearLayoutHistory;
    Unbinder unbinder;
    @BindView(R.id.linearLayout_container_cart)
    LinearLayout linearLayoutContainerCart;
    @BindView(R.id.linearLayout_container_products)
    LinearLayout linearLayoutContainerCartProducts;

    private Customer mCustomer;
    private String mParam2;
    private MainActivity activity;
    private ProductsAdapter productsAdapter;
    private ProductsAdapter productsAdapterWhihshlist;
    private ProductsAdapter productsAdapterRecommendation;
    private ArrayList<Article> listProducts = new ArrayList<>();
    private ArrayList<Article> listProductsWhishlist = new ArrayList<>();
    private ArrayList<Article> listProductsRecommendation = new ArrayList<>();
    private ArrayList<Cart> listCarts = new ArrayList<>();
    private SalesAdapter salesAdapter;
    private boolean selectMode;
    private Cart cart;
    private Reservation res;

    private Cart c1 = new Cart();
    private Cart c2 = new Cart();
    ArrayList<Cart> myListCarts = new ArrayList<>();

    Article p1 = new Article();
    Article p2 = new Article();
    Article p3 = new Article();
    Category cat1 = new Category();


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1         Parameter 1.
     * @param param2         Parameter 2.
     * @param selectCustomer
     * @return A new instance of fragment CustomerProfileFragment.
     */
    public static CustomerProfileFragment newInstance(Customer param1, String param2, boolean selectCustomer) {
        CustomerProfileFragment fragment = new CustomerProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_CUSTOMER, param1);
        args.putString(ARG_PARAM2, param2);
        args.putBoolean(ARG_PARAM_SELECT, selectCustomer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCustomer = getArguments().getParcelable(ARG_PARAM_CUSTOMER);
            mParam2 = getArguments().getString(ARG_PARAM2);
            selectMode = getArguments().getBoolean(ARG_PARAM_SELECT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        activity = (MainActivity) getActivity();
        EventBus.getDefault().register(this);

        initViews();
        initInfos();
        importHistory();
        System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW : " + listProductsWhishlist.size());
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCustomerSelectedEvent(UpdateCustomerInfoEvent event) {
        if (event.getCustomer() != null) {
            event.getCustomer().setCustomerId(mCustomer.getCustomerId());
            mCustomer = event.getCustomer();
            initInfos();
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initInfos() {
        textViewName.setText(mCustomer.getFirstName() + " " + mCustomer.getLastName());
        textViewEmail.setText(mCustomer.getEmail());
        textViewPhone.setText(mCustomer.getOfficePhoneNumber());
        textViewAddress.setText(mCustomer.getAddressLine1());
        // linearLayoutEdit.setVisibility(canEditCustomer() ? View.VISIBLE : View.GONE);
    }


    private void importCarts(ArrayList<Cart> carts){
        //c1.set
        cat1.setId((long) 11);
        cat1.setLibelle("Chaussures");
        cat1.setLevel((long) 3);

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
        p2.setCategory(cat1);
        p2.setEanCode("1111");

        p3.setId(3);
        p3.setName("Shorts KK");
        p3.setPrice(55.9);
        p3.setStock(66);
        p3.setProductCode("SR8584");
        p3.setCategory(cat1);
        p3.setEanCode("2222");

        ArrayList<Article> listProductForCart = new ArrayList<>();
        listProductForCart.add(p1);
        listProductForCart.add(p2);
        ArrayList<Article> listProductForCart2 = new ArrayList<>();
        listProductForCart2.add(p3);
        //------------
        c1.setAgentID(String.valueOf(activity.getSellerLogin().getId()));
        c1.setAgentName(activity.getSellerLogin().getLibelle());
        c1.setCurrencyId(activity.getEtablissement().getCurrencyId());
        c1.setCompany(mCustomer.getIsCompany());
        //c1.setCustomer(mCustomer.getId()+"");
        c1.setCustomer("1234");
        c1.setCustomerFirstName(mCustomer.getFirstName());
        c1.setCustomerLastName(mCustomer.getLastName());
        c1.setProductList(listProductForCart);
        c1.setTotal(660.6);
        try {
            c1.setDate(Utils.getCurrentDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            c1.setTime(Utils.getCurrentTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c1.setSellerLibelle(activity.getSellerLogin().getLibelle());
        c1.setPaid(true);


        c2.setAgentID(String.valueOf(activity.getSellerLogin().getId()));
        c2.setAgentName(activity.getSellerLogin().getLibelle());
        c2.setCurrencyId(activity.getEtablissement().getCurrencyId());
        c2.setCompany(mCustomer.getIsCompany());
        //c2.setCustomer(mCustomer.getId()+"");
        c2.setCustomer("1234");
        c2.setCustomerFirstName(mCustomer.getFirstName());
        c2.setCustomerLastName(mCustomer.getLastName());
        c2.setTotal(226.0);
        try {
            c2.setDate(Utils.getCurrentDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            c2.setTime(Utils.getCurrentTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c2.setSellerLibelle(activity.getSellerLogin().getLibelle());
        c2.setPaid(true);

        c2.setProductList(listProductForCart2);

        ArrayList<Cart> importedCarts = new ArrayList<>();
        importedCarts.add(c1);
        importedCarts.add(c2);

        for (Cart c : importedCarts){
            if(c.getCustomer().equals(String.valueOf(mCustomer.getId()))){
                carts.add(c);
            }
        }
    }

    //Import customers Carts+Products for History
    @SuppressLint("CheckResult")
    private void importHistory() {
        activity.showProgressBar(true);
        //----------
        activity.showProgressBar(false);

        ArrayList<Cart> carts = new ArrayList<>();
        importCarts(carts);

        if (carts.size() > 0) {
            listCarts.addAll(carts);

            for (int i = 0; i < carts.size(); i++) {
                cart = carts.get(i);
                for (int j = 0; j < cart.getProductList().size(); j++) {
                    // TODO: 24/10/18 fix this when getarticle fixed
                    Article article = cart.getProductList().get(j);
                    article.setDate(cart.getDate());
                    listProducts.add(article);
                    listProductsWhishlist.add(article);
                    listProductsRecommendation.add(article);
                }
                initData();
            }
        }
        //----------
        //activity.getLafargeDatabase().cartDao().getCartByCustomer(mCustomer.getId() + "")
    }

    private void initData() {
        if (isAdded()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerViewHistory.setAdapter(radioButtonOrders.isChecked() ? salesAdapter : productsAdapter);
                    System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCC Carts : " + listCarts.size());
                    salesAdapter.addAll(listCarts);
                    productsAdapter.addAll(listProducts);
                    productsAdapterWhihshlist.addAll(listProductsWhishlist);
                    //just to test
                    listProductsRecommendation.remove(listProductsRecommendation.size()-1);
                    productsAdapterRecommendation.addAll(listProductsRecommendation);
                }
            });
        }
    }

    /*private boolean exists(Article article) {
        for (int i = 0; i < listProducts.size(); i++) {
            DebugLog.d(listProducts.get(i).getCodeBarre() + " --> " + (article.getCodeBarre()));
            if (listProducts.get(i).getCodeBarre().equals(article.getCodeBarre())) {
                listProducts.get(i).setQuantity(listProducts.get(i).getQuantity() + article.getQuantity());
                return true;
            }
        }
        return false;
    }*/

    private void initViews() {
        linearLayoutEdit.setVisibility(selectMode ? View.GONE : View.VISIBLE);
        radioButtonHistory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radioButtonHistory.isChecked()) {
                    radioButtonWhishlist.setChecked(false);
                    radioButtonRecommendation.setChecked(false);
                    radioButtonHistory.setChecked(true);

                    linearLayoutWhishlist.setVisibility(View.GONE);
                    linearLayoutRecommendation.setVisibility(View.GONE);
                    linearLayoutHistory.setVisibility(View.VISIBLE);

                    radioButtonHistory.setTextColor(getResources().getColor(R.color.white));
                    radioButtonWhishlist.setTextColor(getResources().getColor(R.color.dark_gray));
                    radioButtonRecommendation.setTextColor(getResources().getColor(R.color.dark_gray));

                    recyclerViewWhishlist.setAdapter(null);
                    recyclerViewRecommendation.setAdapter(null);
                }
            }
        });
        radioButtonWhishlist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radioButtonWhishlist.isChecked()) {
                    radioButtonHistory.setChecked(false);
                    radioButtonRecommendation.setChecked(false);
                    radioButtonWhishlist.setChecked(true);

                    linearLayoutWhishlist.setVisibility(View.VISIBLE);
                    linearLayoutHistory.setVisibility(View.GONE);
                    linearLayoutRecommendation.setVisibility(View.GONE);

                    radioButtonHistory.setTextColor(getResources().getColor(R.color.dark_gray));
                    radioButtonRecommendation.setTextColor(getResources().getColor(R.color.dark_gray));
                    radioButtonWhishlist.setTextColor(getResources().getColor(R.color.white));

                    recyclerViewWhishlist.setAdapter(productsAdapterWhihshlist);
                    recyclerViewRecommendation.setAdapter(null);
                }
            }
        });
        radioButtonRecommendation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radioButtonRecommendation.isChecked()) {

                    linearLayoutWhishlist.setVisibility(View.GONE);
                    linearLayoutHistory.setVisibility(View.GONE);
                    linearLayoutRecommendation.setVisibility(View.VISIBLE);

                    radioButtonHistory.setTextColor(getResources().getColor(R.color.dark_gray));
                    radioButtonWhishlist.setTextColor(getResources().getColor(R.color.dark_gray));
                    radioButtonRecommendation.setTextColor(getResources().getColor(R.color.white));

                    radioButtonHistory.setChecked(false);
                    radioButtonWhishlist.setChecked(false);
                    radioButtonRecommendation.setChecked(true);

                    recyclerViewRecommendation.setAdapter(productsAdapterRecommendation);
                    recyclerViewWhishlist.setAdapter(null);
                }
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        LinearLayoutManager layoutManagerWhishlist = new LinearLayoutManager(activity);
        LinearLayoutManager layoutManagerRecommendation = new LinearLayoutManager(activity);
        recyclerViewHistory.setLayoutManager(layoutManager);
        recyclerViewWhishlist.setLayoutManager(layoutManagerWhishlist);
        recyclerViewRecommendation.setLayoutManager(layoutManagerRecommendation);
        //-----Whishlist+recommendation recyclerview config
        productsAdapterWhihshlist = new ProductsAdapter(listProductsWhishlist, activity, activity, false, false);
        productsAdapterWhihshlist.setProfileList(true);

        productsAdapterRecommendation = new ProductsAdapter(listProductsRecommendation, activity, activity, false, false);
        productsAdapterRecommendation.setProfileList(true);
        //-----
        //Removed the Adapters Instanciation here
        productsAdapter = new ProductsAdapter(listProducts, activity, activity, false, false);
        productsAdapter.setProfileList(true);
        salesAdapter = new SalesAdapter(listCarts, activity, activity);
        salesAdapter.setOnItemClickListener(this);
        salesAdapter.setFromProfile(true);
        //-----
        radioButtonOrders.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
                    // on a large screen device ...
                    linearLayoutContainerCart.setVisibility(radioButtonOrders.isChecked() ? View.VISIBLE : View.GONE);
                }
                if (radioButtonOrders.isChecked()) {
                    radioButtonProducts.setChecked(false);
                    recyclerViewHistory.setAdapter(null);
                    recyclerViewHistory.setAdapter(salesAdapter);
                }
            }
        });
        radioButtonProducts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radioButtonProducts.isChecked()) {
                    radioButtonOrders.setChecked(false);
                    recyclerViewHistory.setAdapter(null);
                    recyclerViewHistory.setAdapter(productsAdapter);
                }
                linearLayoutContainerCartProducts.setVisibility(radioButtonProducts.isChecked() ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void onFragResume() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.linearlayout_associate_to_cart, R.id.linearlayout_associate_to_res, R.id.linearLayout_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linearlayout_associate_to_cart:
                if (selectMode) {
                    EventBus.getDefault().post(new CustomerSelectedEvent(mCustomer));
                    activity.onBackPressed();
                    activity.onBackPressed();
                /*else if (activity.getLastOpenedCart() != null &&
                        activity.getLastOpenedCart().isCompany() == mCustomer.getIsCompany()) {

                    activity.getLastOpenedCart().setCompany(mCustomer.getIsCompany());
                    activity.getLastOpenedCart().setCustomer(mCustomer.getCustomerId());
                    activity.getLastOpenedCart().setCustomerFirstName(mCustomer.getFirstName());
                    activity.getLastOpenedCart().setCustomerLastName(mCustomer.getLastName());

                }*/
                } else {
                    initCart();
                    //saveNewCart();
                    activity.addFragment(CartFragment.newInstance(cart, false, activity.getDaily() != null), "", true);
                }
                break;
            case R.id.linearlayout_associate_to_res:
                if (selectMode) {
                    EventBus.getDefault().post(new CustomerSelectedEvent(mCustomer));
                    activity.onBackPressed();
                    activity.onBackPressed();
                } else {
                    initRes();
                    //saveNewCart();
                    activity.addFragment(ReservationFragment.newInstance(res), "", true);
                }
                break;
            case R.id.linearLayout_edit:
                activity.addFragment(CustomerFragment.newInstance(mCustomer, "", false, false, false), "", true);
                break;
        }

    }


    private void saveNewCart() {
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

    private void initCart() {
        cart = new Cart();
        cart.setId(Utils.generateTimestamp());
        //cart.setDailyID(activity.getDaily().getId());
        cart.setClosed(false);
        try {
            cart.setDate(Utils.getCurrentDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cart.setAgentID(String.valueOf(activity.getSellerLogin().getId()));
        cart.setAgentName(activity.getSellerLogin().getLibelle());
        cart.setCurrencyId(activity.getEtablissement().getCurrencyId());
        cart.setCompany(mCustomer.getIsCompany());
        cart.setCustomer(mCustomer.getId()+"");
        cart.setCustomerFirstName(mCustomer.getFirstName());
        cart.setCustomerLastName(mCustomer.getLastName());
        cart.setLastModification(Utils.generateTimestamp());
    }

    private void initRes() {
        res = new Reservation();
        res.setId(Utils.generateTimestamp());
        //cart.setDailyID(activity.getDaily().getId());
        res.setClosed(false);
        try {
            res.setDate(Utils.getCurrentDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        res.setAgentID(String.valueOf(activity.getSellerLogin().getId()));
        res.setAgentName(activity.getSellerLogin().getLibelle());
        res.setCurrencyId(activity.getEtablissement().getCurrencyId());
        res.setCompany(mCustomer.getIsCompany());
        res.setCustomer(mCustomer.getId()+"");
        res.setCustomerFirstName(mCustomer.getFirstName());
        res.setCustomerLastName(mCustomer.getLastName());
        res.setLastModification(Utils.generateTimestamp());
    }

    @Override
    public void onItemClick(Cart cart) {
        activity.addFragment(CartFragment.newInstance(cart, false, activity.getDaily() != null), "", true);
    }

    @Override
    public void onItemDelete(Cart cart) {

    }
}
