package io.mintit.lafarge.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.mintit.lafarge.R;
import io.mintit.lafarge.adapter.InventoryProductsAdapter;
import io.mintit.lafarge.adapter.ProductsAdapter;
import io.mintit.lafarge.events.InventoryEvent;
import io.mintit.lafarge.events.ProductSelectedEvent;
import io.mintit.lafarge.global.Constants;
import io.mintit.lafarge.model.Product;
import io.mintit.lafarge.model.Inventory;
import io.mintit.lafarge.model.InventoryArticle;
import io.mintit.lafarge.services.ActionRequestsManager;
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
 * Use the {@link InventoryProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InventoryProductsFragment extends BaseFragment implements ProductsAdapter.OnItemClickListener {
    private static final String ARG_PARAM_IVENTORY = "ARG_PARAM_IVENTORY";
    @BindView(R.id.textVIew_inventory_name)
    TextView textVIewInventoryName;
    @BindView(R.id.linearLayout_scan_code)
    LinearLayout linearLayoutScanCode;
    @BindView(R.id.textview_product_name)
    TextView textviewProductName;
    @BindView(R.id.textview_product_code)
    TextView textviewProductCode;
    @BindView(R.id.linearLayout_container)
    LinearLayout linearLayoutContainer;
    @BindView(R.id.recyclerView_products)
    RecyclerView recyclerViewProducts;
    @BindView(R.id.relativeLayout_submit)
    RelativeLayout relativeLayoutSubmit;
    @BindView(R.id.no_article_tv)
    TextView noArticleTv;
    ArrayList<InventoryArticle> listProducts = new ArrayList<>();
    private MainActivity activity;
    private InventoryProductsAdapter productsAdapter;
    private Inventory inventory;




    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param inventory
     * @return A new instance of fragment ProductsFragment.
     */
    public static InventoryProductsFragment newInstance(Inventory inventory) {
        InventoryProductsFragment fragment = new InventoryProductsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_IVENTORY, inventory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            inventory = (Inventory) getArguments().getParcelable(ARG_PARAM_IVENTORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products_inventory, container, false);
        ButterKnife.bind(this, view);
        activity = (MainActivity) getActivity();
        initViews();
        loadProducts();
        return view;
    }

    private void initViews() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerViewProducts.setLayoutManager(layoutManager);
        productsAdapter = new InventoryProductsAdapter(listProducts, activity, activity, inventory);
        recyclerViewProducts.setAdapter(productsAdapter);
        relativeLayoutSubmit.setVisibility(!inventory.getIsValidated() ? View.VISIBLE : View.GONE);
        linearLayoutScanCode.setVisibility(!inventory.getIsValidated() ? View.VISIBLE : View.GONE);
    }


    @SuppressLint("CheckResult")
    private void loadProducts() {
        activity.showProgressBar(true);
        activity.getLafargeDatabase().inventoryDao().getInventoryArticle(inventory.getCodeliste())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<InventoryArticle>>() {
                    @Override
                    public void accept(List<InventoryArticle> inventories) {
                        if (inventories.size() > 0) {
                            noArticleTv.setVisibility(View.INVISIBLE);
                            listProducts.addAll(inventories);
                            if (isAdded()) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        productsAdapter.addAll(listProducts);
                                    }
                                });
                            }
                        }else{
                            noArticleTv.setVisibility(View.VISIBLE);
                        }
                        activity.showProgressBar(false);
                    }
                });
    }


    @Override
    public void onFragResume() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onItemClick(Product product) {
    }

    @Override
    public void onItemAdd(Product product, int pickedNumber, boolean fromDetail) {
        EventBus.getDefault().post(new ProductSelectedEvent(product, pickedNumber,false));
    }

    @Override
    public void onItemUpdate(Product product) {
    }

    @Override
    public void onItemRemove(Product product) {
    }

    @OnClick({R.id.linearLayout_scan_code, R.id.relativeLayout_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.relativeLayout_submit:
                saveChanges();
                break;
            case R.id.linearLayout_scan_code:
                startScan();
                break;

        }
    }



    public void saveChanges() {
        if(listProducts.size()>0) {
            activity.showProgressBar(true);
            for (int i = 0; i < productsAdapter.getItemCount(); i++) {
                String physicalQty = productsAdapter.getListProducts().get(i).getStockPhysique();
                if (TextUtils.isEmpty(physicalQty)) {
                    Utils.showAlert(getString(R.string.alert_verify_empty_qty), activity, null, false, true);
                    return;
                } else {
                    listProducts.get(i).setStockPhysique(physicalQty);
                }
            }
            updateArticle();
            inventory.setIsValidated(true);
            updateInventory(inventory);


            new ActionRequestsManager(activity).createNewActionRequest(inventory, "Update", Constants.ACTION_REQUEST_INVENTORY);


            for (int i = 0; i < listProducts.size(); i++) {
                // new ActionRequestsManager(activity).createNewActionRequest(listProducts.get(i), "Update",  Constants.ACTION_REQUEST_INVENTORY_TRANSACTION);
                if (i == listProducts.size() - 1) {
                    activity.showProgressBar(false);
                }
            }
            inventory.setProducts(listProducts);
            EventBus.getDefault().post(new InventoryEvent(inventory));
            activity.onBackPressed();
        }else {
            Utils.showAlert(getString(R.string.empty_inventory), activity, null, false, true);
        }
    }

    private void updateArticle() {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                activity.getLafargeDatabase().inventoryDao().insertAllInventoryArticles(listProducts);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d("Success ROOM", "edit article");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ERROR ROOM edit article", e.getMessage());
                    }
                });
    }

    private void updateInventory(final Inventory inventory) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                activity.getLafargeDatabase().inventoryDao().updateInventory(inventory);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ERROR ROOM update cart", e.getMessage());
                    }
                });
    }

    @Override
    @SuppressLint("CheckResult")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        DebugLog.d("onActivityResult");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents()!=null) {
            activity.getLafargeDatabase().articleDao().getArticleByStock(result.getContents()/*"3102279312284"*/)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Product>>() {
                        @Override
                        public void accept(List<Product> products) {
                            String codeArticle = "";
                            if (products.size() > 0) {
                                codeArticle = products.get(0).getProductCode();
                            }
                            for (int i = 0; i < listProducts.size(); i++) {
                                InventoryArticle article = listProducts.get(i);
                                if (codeArticle.equals(article.getArticle())) {
                                    article.setStockPhysique((Integer.parseInt(article.getStockPhysique())+1)+"");
                                    productsAdapter.updateItem(article);
                                    break;
                                }
                            }
                            startScan();
                        }
                    });
        }

    }


    public void startScan() {
        IntentIntegrator.forSupportFragment(this).initiateScan();
    }


}
