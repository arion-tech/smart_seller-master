package io.mintit.lafarge.ui.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

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
import io.mintit.lafarge.adapter.PurchaseAdapter;
import io.mintit.lafarge.data.Data;
import io.mintit.lafarge.events.PurchaseOrderEvent;
import io.mintit.lafarge.model.Purchase;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PurchasesListFragment extends BaseFragment implements PurchaseAdapter.OnItemClickListener {
    @BindView(R.id.edittext_supplier)
    EditText editTextSupplier;
    @BindView(R.id.recyclerView_purchase_orders)
    RecyclerView recyclerViewPurchaseOrders;
    @BindView(R.id.linearLayout_create_po)
    LinearLayout linearLayoutCreatePo;
    Unbinder unbinder;
    private MainActivity activity;
    private PurchaseAdapter purchaseAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_purchases_list, container, false);
        activity = (MainActivity) getActivity();
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        loadPurchases();
        return view;
    }



    @SuppressLint("CheckResult")
    private void loadPurchases(){
        activity.getLafargeDatabase().purchaseDao().getAllPurchases()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Purchase>>() {
                    @Override
                    public void accept(List<Purchase> purchases) {
                        if(purchases.size()>0){
                            Data.getInstance().setPurchaseList(new ArrayList<Purchase>(purchases));
                        }
                        activity.showProgressBar(false);
                        initViews();
                    }
                });

    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerViewPurchaseOrders.setLayoutManager(layoutManager);
        purchaseAdapter = new PurchaseAdapter(Data.getInstance().getPurchaseList(), activity, activity);
        recyclerViewPurchaseOrders.setAdapter(purchaseAdapter);
        purchaseAdapter.setOnItemClickListener(this);

        editTextSupplier.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editTextSupplier.getText().length() == 0) {
                    purchaseAdapter.reset(Data.getInstance().getPurchaseList());
                } else {
                    ArrayList<Purchase> purchasesFiltred = new ArrayList<>();
                    for(int i=0; i<Data.getInstance().getPurchaseList().size(); i++){
                        if(Data.getInstance().getPurchaseList().get(i).getSupplier().toLowerCase().contains(editTextSupplier.getText().toString().toLowerCase())){
                            purchasesFiltred.add(Data.getInstance().getPurchaseList().get(i));
                        }
                    }
                    purchaseAdapter.addAll(purchasesFiltred);

                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
        });

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPurchaseOrderEvent(PurchaseOrderEvent event) {
        if (event.getPurchase() != null) {
            if (event.isUpdate()) {
                purchaseAdapter.updateItem(event.getPosition(), event.getPurchase());
            } else {
                purchaseAdapter.add(event.getPurchase());
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

    @OnClick(R.id.linearLayout_create_po)
    public void onViewClicked() {
        activity.addFragment(NewPurchaseFragment.newInstance(null), "", true);
    }

    @Override
    public void onItemClick(Purchase purchase) {
        activity.addFragment(NewPurchaseFragment.newInstance(purchase), "", true);
    }
}
