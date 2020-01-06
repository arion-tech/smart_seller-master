package io.mintit.lafarge.ui.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.mintit.lafarge.R;
import io.mintit.lafarge.Retrofit.ApiInterface;
import io.mintit.lafarge.Retrofit.ApiManager;
import io.mintit.lafarge.adapter.CategoriesAdapter;
import io.mintit.lafarge.adapter.ProductsAdapter;
import io.mintit.lafarge.events.ChangeTarifEvent;
import io.mintit.lafarge.events.ProductSelectedEvent;
import io.mintit.lafarge.global.Constants;
import io.mintit.lafarge.model.Product;
import io.mintit.lafarge.model.Cart;
import io.mintit.lafarge.model.Category;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.utils.DebugLog;
import io.mintit.lafarge.utils.Prefs;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductsFragment extends BaseFragment implements ProductsAdapter.OnItemClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_SELECT = "SELECT";
    private static final String ARG_PARAM_CART = "ARG_PARAM_CART";
    private static final String ARG_PARAM_FROM_PRODUCT = "ARG_PARAM_PRODUCT";
    private static final String ARG_PARAM_FROM_ORDER = "ARG_PARAM_INVENTORY";


    @BindView(R.id.edittext_code)
    EditText edittextCode;
    @BindView(R.id.linearLayout_scan_code)
    LinearLayout linearlayoutScanCode;
    @BindView(R.id.recyclerView_products)
    RecyclerView recyclerViewProducts;
    Unbinder unbinder;
    @BindView(R.id.linearLayout_back)
    LinearLayout linearLayoutBack;
    @BindView(R.id.relativeLayout_add_to_cart)
    RelativeLayout relativeLayoutAddToCart;
    @BindView(R.id.textview_selected_category)
    TextView textviewSelectedCategory;
    @BindView(R.id.textview_product_price)
    TextView textviewProductPrice;
    @BindView(R.id.textview_reset)
    TextView textviewReset;
    @BindView(R.id.linearLayout_selected_category)
    LinearLayout linearLayoutSelectedCategory;
    ArrayList<Product> listProducts = new ArrayList<>();
    boolean selectItem ;
    @BindView(R.id.cardView_container)
    CardView cardViewContainer;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    private MainActivity activity;
    private ProductsAdapter productsAdapter;
    private Cart cart;
    private ArrayList<Category> categoriesList = new ArrayList<>();
    private Category selectedCategory;
    private String constraint;
    private String tarif = Constants.TTC;
    private boolean fromProduct;
    private boolean fromOrder;
    //private boolean scrool = true;
    ArrayList<Product> listProductsTemp = new ArrayList<>();
    //ArrayList<String> stockCodeArticle = new ArrayList<>();
    ArrayList<Product> listArticlesFilteredByCat = new ArrayList<>();
    ArrayList<Product> searchedArticlesList = new ArrayList<>();
    ApiInterface ApiInterface;

    Product p1 = new Product();
    Product p2 = new Product();
    Product p3 = new Product();
    Product p4 = new Product();
    Product p5 = new Product();
    Product p6 = new Product();
    Product p7 = new Product();
    Category c0 = new Category();
    Category c1 = new Category();
    Category c2 = new Category();
    Category c3 = new Category();
    Category c4 = new Category();
    Category c5 = new Category();


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param selectItem
     * @param cart
     * @param fromOrder
     * @return A new instance of fragment ProductsFragment.
     */

    public static ProductsFragment newInstance(boolean selectItem, Cart cart, boolean fromOrder, boolean fromProducts) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM_SELECT, selectItem);
        args.putBoolean(ARG_PARAM_FROM_PRODUCT, fromProducts);
        args.putBoolean(ARG_PARAM_FROM_ORDER, fromOrder);
        args.putParcelable(ARG_PARAM_CART, cart);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            selectItem = getArguments().getBoolean(ARG_PARAM_SELECT);
            cart = (Cart) getArguments().getParcelable(ARG_PARAM_CART);
            fromProduct = getArguments().getBoolean(ARG_PARAM_FROM_PRODUCT);
            fromOrder = getArguments().getBoolean(ARG_PARAM_FROM_ORDER);
        }

        final boolean isConnected = Prefs.getBooleanPref(Prefs.IS_CONNECTED, getContext(), false);
        if(isConnected == false)
        {Toast.makeText(getContext(), "No internet available", Toast.LENGTH_SHORT).show();}


    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        unbinder = ButterKnife.bind(this, view);
        activity = (MainActivity) getActivity();
        initViews();
        EventBus.getDefault().register(this);

        if (fromProduct || fromOrder) {
            productsAdapter.clear();
            reloadProducts();
            productsAdapter.addAll(listProducts);
        } else {
            //listProducts = Data.getInstance().getListProducts();
            reloadProducts();
            initProducts();

        }
        if (!fromProduct && !fromOrder) {
            productsAdapter.clear();
        }
        reloadProducts();
        return view;
    }

    private void reloadCateg(){

        //categoriesList.add(c1);
        //categoriesList.add(c2);
        //categoriesList.add(c3);
        ApiInterface service = ApiManager.createService(ApiInterface.class, Prefs.getPref(Prefs.TOKEN, getContext()));
        Call<ArrayList<Category>> categorieCall = service.getcategories();
        ArrayList<Category> children = new ArrayList<>();
        categorieCall.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                ArrayList<Category> listCategory = response.body();
                long i = 0;
                for(Category s: listCategory){
                    System.out.println("AAAAAAAARRRRTICLE : " + s.getLibelle());
                    System.out.println("AAAAAAAARRRRTICLE : " + i++);
                    Category c = new Category();
                    c.setId(i++);
                    c.setLibelle(s.getLibelle());
                    c.setLevel((long) 2);
                    children.add(c);
                }

                Set<Category> mySet = new HashSet<Category>(children);

                // Créer une Nouvelle ArrayList à partir de Set
                ArrayList<Category> children = new ArrayList<Category>(mySet);

                c5.setChildren(children);

            }
            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable throwable){}
        });

        c1.setId((long) 11);
        c1.setLibelle("Chaussures");
        c1.setLevel((long) 2);

        c2.setId((long) 22);
        c2.setLibelle("Shirts");
        c2.setLevel((long) 2);

        c3.setId((long) 33);
        c3.setLibelle("Trousers");
        c3.setLevel((long) 2);

        c0.setId((long) 44);
        c0.setLibelle("Vetement");
        c0.setLevel((long) 1);


        c4.setId((long) 55);
        c4.setLibelle("homme");
        c4.setLevel((long) 1);

        c5.setId((long) 66);
        c5.setLibelle("femme");
        c5.setLevel((long) 1);

        ArrayList<Category> catNiv2 = new ArrayList<>();
        catNiv2.add(c1);
        catNiv2.add(c2);
        catNiv2.add(c3);

        c0.setChildren(catNiv2);
        c4.setChildren(catNiv2);
        categoriesList.add(c0);
        categoriesList.add(c4);
        categoriesList.add(c5);
    }

    private void reloadProducts(){
        //listProducts = Prefs.getPrefList("listProducts", getContext());
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface service = ApiManager.createService(ApiInterface.class, Prefs.getPref(Prefs.TOKEN,getContext()));
        Call<ArrayList<Product>> productCall = service.getarticle(Prefs.getPref(Prefs.STORE , getContext()));
        productCall.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                ArrayList<Product> listProduct = response.body();
                int i = 0;
                for(Product s: listProduct){
                    System.out.println("service product : " + s.toString());
                    Product p = new Product();
                    p.setId(s.getId());
                    p.setPrice(s.getPrice());
                    p.setName(s.getName());
                    p.setQty(s.getQty());
                    p.setProductCode(s.getProductCode());
                    p.setCategory(s.getCategory());
                    p.setEanCode(s.getEanCode());
                    p.setSizeCode(s.getSizeCode());
                    listProducts.add(p);
                }

                progressBar.setVisibility(View.GONE);
                productsAdapter.addProducts(listProducts);

            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable throwable){}
        });




    }


    private void initViews() {
        if (cart != null) {
            cardViewContainer.setCardBackgroundColor(Color.TRANSPARENT);
            cardViewContainer.setCardElevation(0);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) cardViewContainer.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 0);
            cardViewContainer.requestLayout();
        }
        //relativeLayoutAddToCart.setVisibility(selectItem ? View.INVISIBLE : View.GONE);
        //relativeLayoutAddToCart.setVisibility(selectItem ? View.VISIBLE : View.GONE);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerViewProducts.setLayoutManager(layoutManager);

        boolean isCartClosed;



        if(cart == null || cart.getClosed() == null){
            isCartClosed = false;
        }else{
            isCartClosed = true;
        }
        productsAdapter = new ProductsAdapter(listProducts, activity, activity, selectItem, isCartClosed);
        productsAdapter.setSelectProduct(selectItem);
        productsAdapter.setOnItemClickListener(this);
        recyclerViewProducts.setAdapter(productsAdapter);


        //linearlayoutScanCode.setVisibility(selectItem ? View.VISIBLE : View.GONE);
        //textviewProductPrice.setVisibility(selectItem ? View.GONE : View.VISIBLE);
        edittextCode.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                System.out.println("TEST FROM NEW CAAAAAAAAAAAAAAAAAAAART");
                searchedArticlesList.clear();
                if (edittextCode.getText().length() == 0) {
                    if(!fromProduct && !fromOrder && selectedCategory == null){
                        productsAdapter.clear();
                    } else {
                        if (selectedCategory == null) {
                            initProducts();
                        } else {
                            setSearchedArticles(listArticlesFilteredByCat);
                        }
                    }
                } else {
                    productsAdapter.clear();
                    if (selectedCategory == null) {
                        for (int i = 0; i < listProducts.size(); i++) {
                            if (listProducts.get(i).getProductCode().contains(edittextCode.getText().toString()) ||
                                listProducts.get(i).getName().toLowerCase().startsWith(edittextCode.getText().toString().toLowerCase())) {
                                searchedArticlesList.add(listProducts.get(i));
                            }
                        }
                    } else {
                        for (int i = 0; i < listArticlesFilteredByCat.size(); i++) {
                            if (listArticlesFilteredByCat.get(i).getProductCode().contains(edittextCode.getText().toString()) ||
                                listArticlesFilteredByCat.get(i).getName().toLowerCase().startsWith(edittextCode.getText().toString().toLowerCase())) {
                                searchedArticlesList.add(listArticlesFilteredByCat.get(i));
                            }
                        }
                    }
                    setSearchedArticles(searchedArticlesList);
                }
                System.out.println("InitViews/afterTextChanged method productAdapter " + productsAdapter.productsList.size());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                productsAdapter.addProducts(listProducts);
            }
        });

        System.out.println("InitViews method productAdapter " + productsAdapter.productsList.size());
    }

    private void setSearchedArticles(ArrayList<Product> searchedArticlesList) {
        if (searchedArticlesList.size() > 0) {
            productsAdapter.clear();
            listProductsTemp.clear();
            //if (selectedCategory == null && selectItem) {
            if (selectedCategory == null && !selectItem) {
                productsAdapter.addProducts(listProducts);
            } else {
                productsAdapter.addAll(searchedArticlesList);
            }
        } else {
            productsAdapter.clear();
        }
        System.out.println("setSearchedArticles method productAdapter " + productsAdapter.productsList.size());
    }

    /*@SuppressLint("CheckResult")
    private void loadSearchedProducts(final String constraint) {
        this.constraint = constraint;
        //reloadProducts();
        List<Product> articles = new ArrayList<>();
        articles.addAll(listProducts);
        if (articles.size() > 0) {
            productsAdapter.clear();
            if (articles.size() > 10) {
                for (int i = 0; i < 10; i++) {
                    listProductsTemp.add(articles.get(i));
                }
            } else {
                listProductsTemp.addAll(articles);
            }
            productsAdapter.addAll(listProductsTemp);
        } else {
            productsAdapter.clear();
        }
    }*/

    private void initProducts() {
        listProductsTemp.addAll(listProducts);
        productsAdapter.addAll(listProducts);
        System.out.println("initProducts method productAdapter " + productsAdapter.productsList.size());
    }

    public void addProducts() {
        if(selectedCategory == null ) {
            //showLoaderArticles(true);
            productsAdapter.addProducts(searchedArticlesList.size()>0 ? searchedArticlesList : listProducts);
        }

    }

    public void showLoaderArticles(final boolean show){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
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

    @Override
    public void onItemClick(Product product) {
    }

    @Override
    public void onItemAdd(Product product, int pickedNumber, boolean fromDetail) {
        EventBus.getDefault().post(new ProductSelectedEvent(product, pickedNumber, fromDetail));
    }

    @Override
    public void onItemUpdate(Product product) {
    }

    @Override
    public void onItemRemove(Product product) {
    }

    @OnClick({R.id.textview_reset, R.id.linearLayout_back, R.id.linearLayout_scan_code, R.id.linearLayout_category})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linearLayout_back:
                activity.onBackPressed();
                break;
            case R.id.linearLayout_scan_code:
                startScan();
                break;
            case R.id.linearLayout_category:
                loadData();
                break;
            case R.id.textview_reset:
                resetCategFilter();
                break;
        }
    }

    private void resetCategFilter(){
        selectedCategory = null;
        linearLayoutSelectedCategory.setVisibility(View.GONE);
        textviewSelectedCategory.setText("");
        edittextCode.setText("");
        //listProducts = Data.getInstance().getListProducts();
        if (fromProduct || fromOrder)
            productsAdapter.addAll(listProducts);
        else {
            searchedArticlesList.clear();
            listArticlesFilteredByCat.clear();
            listProductsTemp.clear();
            initProducts();
        }
        if(!fromProduct && !fromOrder ){
            productsAdapter.clear();
        }
        System.out.println("resetCategFilter method productAdapter " + productsAdapter.productsList.size());
    }

    private void loadData() {
        categoriesList.clear();
        reloadCateg();
        showCategoriesDialog();
        System.out.println("loadData method productAdapter " + productsAdapter.productsList.size());
    }

    private void showCategoriesDialog() {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_categories);

        RecyclerView recyclerViewCategories = (RecyclerView) dialog.findViewById(R.id.recyclerView_categories);
        LinearLayout linearLayoutCancel = (LinearLayout) dialog.findViewById(R.id.linearLayout_cancel);
        LinearLayout linearLayoutSubmit = (LinearLayout) dialog.findViewById(R.id.linearLayout_submit);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerViewCategories.setLayoutManager(layoutManager);

        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(categoriesList, activity, activity);
        recyclerViewCategories.setAdapter(categoriesAdapter);

        categoriesAdapter.setOnItemClickListener(new CategoriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category category) {
                selectedCategory = category;
            }
        });

        linearLayoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        linearLayoutSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedCategory != null && selectedCategory.getLevel() == 3) {
                    listArticlesFilteredByCat.clear();
                    linearLayoutSelectedCategory.setVisibility(View.VISIBLE);
                    textviewSelectedCategory.setText(selectedCategory.getLibelle());
                    filterProductsList(selectedCategory.getLibelle());
                    dialog.dismiss();
                    System.out.println("showCategoriesDialog/OnClick method productAdapter " + productsAdapter.productsList.size());
                    dialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Select Category", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    @SuppressLint("CheckResult")
    private void filterProductsList(String selectedCategoryFromDialog) {
        productsAdapter.clear();
        for(Product art: listProducts){
            Category x = (Category) art.getCategory();
            if(selectedCategoryFromDialog.equals(x.getLibelle())){
                productsAdapter.add(art);
                listArticlesFilteredByCat.add(art);
            }
        }
        System.out.println("filterProductsList method productAdapter " + productsAdapter.productsList.size());
    }

    @SuppressLint("CheckResult")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //DebugLog.d("onActivityResult");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() != null) {
                Product product_inter = new Product();
                if(selectedCategory == null) {
                    productsAdapter.clear();
                    System.out.println("After CLEAAAAAAAAAAAAAAAAAAAAAAR" + productsAdapter.productsList.size());
                    if (listProducts.size() > 0) {
                        for (int i = 0; i < listProducts.size(); i++) {
                            if (listProducts.get(i).getEanCode().equals(result.getContents())) {
                                edittextCode.setText(listProducts.get(i).getName());
                                productsAdapter.add(listProducts.get(i));
                                product_inter = listProducts.get(i);
                                //EventBus.getDefault().post(new ProductSelectedEvent(product, 1,false));
                            }
                        }
                    }
                } else {
                    productsAdapter.clear();
                    if (listArticlesFilteredByCat.size() > 0) {
                        for (int i = 0; i < listArticlesFilteredByCat.size(); i++) {
                            if (listArticlesFilteredByCat.get(i).getEanCode().equals(result.getContents())) {
                                edittextCode.setText(listArticlesFilteredByCat.get(i).getName());
                                productsAdapter.add(listArticlesFilteredByCat.get(i));
                                product_inter = listArticlesFilteredByCat.get(i);
                                //EventBus.getDefault().post(new ProductSelectedEvent(product, 1,false));
                            }
                        }
                    }
                }
                System.out.println("onActivityResult method productAdapter " + productsAdapter.productsList.size());
                //Remove the double variable
                productsAdapter.removeItem(product_inter);
            } else {
                Toast.makeText(getActivity(), "Code a bar n'existe pas", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "Code a bar n'existe pas", Toast.LENGTH_LONG).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeTarifEvent(ChangeTarifEvent event) {
        DebugLog.d(event.getTarif());
        tarif = event.getTarif();
        activity.showProgressBar(true);
        if (listProducts.size() > 0) {
            for (int i = 0; i < listProducts.size(); i++) {
                //listProducts.get(i).setTarifPrice(String.valueOf(getTarification(listProducts.get(i))));
            }
            productsAdapter.addAll(listProducts);
        }
        activity.showProgressBar(false);
    }

    public void startScan() {
        IntentIntegrator.forSupportFragment(this).initiateScan();
    }
}
