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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.mintit.lafarge.R;
import io.mintit.lafarge.adapter.CategoriesAdapter;
import io.mintit.lafarge.adapter.ProductsAdapter;
import io.mintit.lafarge.events.ChangeTarifEvent;
import io.mintit.lafarge.events.ProductSelectedEvent;
import io.mintit.lafarge.global.Constants;
import io.mintit.lafarge.model.Article;
import io.mintit.lafarge.model.Cart;
import io.mintit.lafarge.model.Category;
import io.mintit.lafarge.model.Reservation;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.utils.DebugLog;


public class ProductsReservationFragment extends BaseFragment implements ProductsAdapter.OnItemClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_SELECT = "SELECT";
    private static final String ARG_PARAM_RES = "ARG_PARAM_CART";
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
    ArrayList<Article> listProducts = new ArrayList<>();
    boolean selectItem ;
    @BindView(R.id.cardView_container)
    CardView cardViewContainer;
    @BindView(R.id.progress)
    ProgressBar progressBar;


    private MainActivity activity;
    private ProductsAdapter productsAdapter;
    private Reservation res;
    private ArrayList<Category> categoriesList = new ArrayList<>();
    private Category selectedCategory;
    private String constraint;
    private String tarif = Constants.TTC;
    private boolean fromProduct;
    private boolean fromOrder;

    ArrayList<Article> listProductsTemp = new ArrayList<>();

    ArrayList<Article> listArticlesFilteredByCat = new ArrayList<>();
    ArrayList<Article> searchedArticlesList = new ArrayList<>();

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


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param selectItem
     * @param res
     * @param fromOrder
     * @return A new instance of fragment ProductsFragment.
     */
    public static ProductsReservationFragment newInstance(boolean selectItem, Reservation res, boolean fromOrder, boolean fromProducts) {
        ProductsReservationFragment fragment = new ProductsReservationFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM_SELECT, selectItem);
        args.putBoolean(ARG_PARAM_FROM_PRODUCT, fromProducts);
        args.putBoolean(ARG_PARAM_FROM_ORDER, fromOrder);
        args.putParcelable(ARG_PARAM_RES, res);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectItem = getArguments().getBoolean(ARG_PARAM_SELECT);
            res = (Reservation) getArguments().getParcelable(ARG_PARAM_RES);
            fromProduct = getArguments().getBoolean(ARG_PARAM_FROM_PRODUCT);
            fromOrder = getArguments().getBoolean(ARG_PARAM_FROM_ORDER);
        }
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
            reloadProducts();
            productsAdapter.clear();
            productsAdapter.addAll(listProducts);
        } else {
            //listProducts = Data.getInstance().getListProducts();
            reloadProducts();
            initProducts();
        }
        if (!fromProduct && !fromOrder) {
            productsAdapter.clear();
        }
        return view;
    }

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

        listProducts.add(p1);
        listProducts.add(p2);
        listProducts.add(p3);
        listProducts.add(p4);
        listProducts.add(p5);
        listProducts.add(p6);
        listProducts.add(p7);
    }


    private void initViews() {
        if (res != null) {
            cardViewContainer.setCardBackgroundColor(Color.TRANSPARENT);
            cardViewContainer.setCardElevation(0);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) cardViewContainer.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 0);
            cardViewContainer.requestLayout();
        }
        //relativeLayoutAddToCart.setVisibility(selectItem ? View.INVISIBLE : View.GONE);
        relativeLayoutAddToCart.setVisibility(selectItem ? View.VISIBLE : View.GONE);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerViewProducts.setLayoutManager(layoutManager);

        boolean isCartClosed;

        if(res == null || res.getClosed() == null){
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
            }
        });

        System.out.println("InitViews method productAdapter " + productsAdapter.productsList.size());
    }

    private void setSearchedArticles(ArrayList<Article> searchedArticlesList) {
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
        List<Article> articles = new ArrayList<>();
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
    public void onItemClick(Article product) {
    }

    @Override
    public void onItemAdd(Article product, int pickedNumber, boolean fromDetail) {
        EventBus.getDefault().post(new ProductSelectedEvent(product, pickedNumber, fromDetail));
    }

    @Override
    public void onItemUpdate(Article product) {
    }

    @Override
    public void onItemRemove(Article product) {
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
                } else {
                    Toast.makeText(getActivity(), "Select Category", Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    @SuppressLint("CheckResult")
    private void filterProductsList(String selectedCategoryFromDialog) {
        productsAdapter.clear();
        for(Article art: listProducts){
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
                Article product_inter = new Article();
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
        activity.showProgressBar(false);
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
