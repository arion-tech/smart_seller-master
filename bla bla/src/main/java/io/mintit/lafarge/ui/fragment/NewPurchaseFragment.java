package io.mintit.lafarge.ui.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.mintit.lafarge.R;
import io.mintit.lafarge.adapter.ProductsAdapter;
import io.mintit.lafarge.adapter.SuppliersAdapter;
import io.mintit.lafarge.data.Data;
import io.mintit.lafarge.events.ProductSelectedEvent;
import io.mintit.lafarge.events.PurchaseOrderEvent;
import io.mintit.lafarge.model.Article;
import io.mintit.lafarge.model.Cart;
import io.mintit.lafarge.model.Purchase;
import io.mintit.lafarge.model.Supplier;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.utils.Utils;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class NewPurchaseFragment extends BaseFragment implements ProductsAdapter.OnItemClickListener {
    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.frame_products)
    FrameLayout frameProducts;
    @BindView(R.id.relativeLayout_cover)
    RelativeLayout relativeLayoutCover;
    @BindView(R.id.textView_supplier)
    TextView textViewSupplier;
    @BindView(R.id.linearLayout_supplier)
    LinearLayout linearLayoutSupplier;
    @BindView(R.id.recyclerView_products)
    RecyclerView recyclerViewProducts;
    @BindView(R.id.textView_empty_list)
    TextView textViewEmptyList;
    @BindView(R.id.linearLayout_submit_po)
    LinearLayout linearLayoutSubmitPo;
    Unbinder unbinder;

    private Purchase mPurchase;
    private MainActivity activity;
    private ProductsAdapter productsAdapter;
    private Purchase purchase = new Purchase();
    private Supplier selectedSupplier = null;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment NewPurchaseFragment.
     */
    public static NewPurchaseFragment newInstance(Purchase param1) {
        NewPurchaseFragment fragment = new NewPurchaseFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPurchase = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_purchase, container, false);
        activity = (MainActivity) getActivity();
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, view);
        if (mPurchase == null) {
            purchase.setId(Utils.generateTimestamp());
        } else {
            purchase = mPurchase;
        }
        initViews();
        return view;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initViews() {
        Fragment productsFragment = ProductsFragment.newInstance(true, new Cart(), true, false);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_products, productsFragment).commit();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerViewProducts.setLayoutManager(layoutManager);
        productsAdapter = new ProductsAdapter(mPurchase != null ? purchase.getProductList() : new ArrayList<Article>(), activity, activity, true,false);
        recyclerViewProducts.setAdapter(productsAdapter);
        productsAdapter.setOnItemClickListener(this);
        productsAdapter.setCartList(true);
        if (mPurchase != null) {
            textViewSupplier.setText(purchase.getSupplier());
            relativeLayoutCover.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProductSelectedEvent(ProductSelectedEvent event) {
        boolean found = false;
        if (event.getProduct() != null) {
            Article product = event.getProduct();
            for (int i = 0; i < purchase.getProductList().size(); i++) {
                if (purchase.getProductList().get(i).getEanCode().equals(product.getProductCode())) {
                    purchase.getProductList().get(i).setQty(purchase.getProductList().get(i).getQty() + event.getQte());
                    productsAdapter.updateItem(purchase.getProductList().get(i));
                    found = true;
                    break;
                }
            }
            if (!found) {
                product.setQty(event.getQte());
                purchase.getProductList().add(product);
                productsAdapter.add(product);
                recyclerViewProducts.smoothScrollToPosition(purchase.getProductList().size() - 1);
            }

        }
    }

    @Override
    public void onFragResume() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.linearLayout_supplier, R.id.linearLayout_submit_po})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linearLayout_supplier:
                showSellersDialog();
                break;
            case R.id.linearLayout_submit_po:
                if (selectedSupplier == null && mPurchase == null) {

                    Utils.showAlert(getString(R.string.error_message_empty_po), activity, null, false, true);

                } else {
                    int position = 0;
                    if (mPurchase == null) {
                        addPurchaseDB(purchase);
                        Data.getInstance().getPurchaseList().add(purchase);
                    } else {
                        for (int i = 0; i < Data.getInstance().getPurchaseList().size(); i++) {
                            if (Data.getInstance().getPurchaseList().get(i).getId().equals(mPurchase.getId())) {
                                Data.getInstance().getPurchaseList().remove(i);
                                Data.getInstance().getPurchaseList().add(i, purchase);
                                position = i;
                                break;
                            }
                        }
                       updatePurchase(purchase);
                    }

                    EventBus.getDefault().post(new PurchaseOrderEvent(purchase, mPurchase != null, position));
                    activity.onBackPressed();
                }
                break;
        }
    }

    private void updatePurchase(final Purchase purchase) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                activity.getLafargeDatabase().purchaseDao().updatePurchase(purchase);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d("SUCCES ROOM ", "update purchase");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ERROR ROOM update cart", e.getMessage());
                    }
                });
    }

    private void addPurchaseDB(final Purchase purchase){
            Completable.fromAction(new Action() {
            @Override
            public void run() {
                activity.getLafargeDatabase().purchaseDao().insertPurchase(purchase);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d("*****ROOM SUCCESS*****", "**********add Purchase*********");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("*ERROR ROOM Purchase*", e.getMessage());
                    }
                });
    }

    @Override
    public void onItemClick(Article product) {

    }

    @Override
    public void onItemAdd(Article product, int pickedNumber, boolean fromDetail) {
        product.getProductCode();
    }

    @Override
    public void onItemUpdate(Article product) {

    }

    @Override
    public void onItemRemove(Article product) {
        purchase.getProductList().remove(product);
        productsAdapter.removeItem(product);
    }

    private void showSellersDialog() {
        if (Data.getInstance().getSuppliers() != null) {
            activity.showProgressBar(false);
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_suppliers);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            RecyclerView recyclerViewSellers = (RecyclerView) dialog.findViewById(R.id.recyclerView_sellers);
            final EditText edittextFilter = (EditText) dialog.findViewById(R.id.edittext_filter);
            LinearLayout linearLayoutCancel = (LinearLayout) dialog.findViewById(R.id.linearLayout_cancel);
            LinearLayout linearLayoutSubmit = (LinearLayout) dialog.findViewById(R.id.linearLayout_submit);
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
            recyclerViewSellers.setLayoutManager(layoutManager);
            final SuppliersAdapter suppliersAdapter = new SuppliersAdapter(Data.getInstance().getSuppliers(), activity, activity);
            recyclerViewSellers.setAdapter(suppliersAdapter);

            suppliersAdapter.setOnItemClickListener(new SuppliersAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Supplier seller) {
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
                        suppliersAdapter.getFilter().filter(edittextFilter.getText().toString());
                    } else {
                        suppliersAdapter.reset(Data.getInstance().getSuppliers());
                    }
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
                    selectedSupplier = suppliersAdapter.getSelectedSupplier();
                    setupCartWithSeller();
                    dialog.dismiss();
                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        } else {
            loadSuppliers();
        }

    }



    @SuppressLint("CheckResult")
    private void loadSuppliers(){
        activity.getLafargeDatabase().supplierDao().getAllSupplier()
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Supplier>>() {
                    @Override
                    public void accept(List<Supplier> suppliers) {
                        if(suppliers.size()>0){
                            Data.getInstance().setSuppliers(new ArrayList<>(suppliers));
                            showSellersDialog();
                        }
                    }
                });

    }

    private void setupCartWithSeller() {
        if (isAdded() && selectedSupplier != null) {
            textViewSupplier.setText(selectedSupplier.getLibelle());
            relativeLayoutCover.setVisibility(View.GONE);
            purchase.setSupplier(selectedSupplier.getLibelle());
        }
    }
}
