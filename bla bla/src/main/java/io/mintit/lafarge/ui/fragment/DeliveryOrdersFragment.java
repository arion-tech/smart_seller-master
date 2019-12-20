package io.mintit.lafarge.ui.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.mintit.lafarge.R;
import io.mintit.lafarge.adapter.DeliveryAdapter;
import io.mintit.lafarge.data.Data;
import io.mintit.lafarge.model.Purchase;
import io.mintit.lafarge.ui.activity.MainActivity;


public class DeliveryOrdersFragment extends BaseFragment {
    @BindView(R.id.edittext_supplier)
    EditText editTextSupplier;
    @BindView(R.id.recyclerView_delivery_orders)
    RecyclerView recyclerViewDeliveryOrders;
    Unbinder unbinder;
    private MainActivity activity;
    private DeliveryAdapter purchaseAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delivery_orders, container, false);
        activity = (MainActivity) getActivity();
        unbinder = ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerViewDeliveryOrders.setLayoutManager(layoutManager);
        purchaseAdapter = new DeliveryAdapter(Data.getInstance().getPurchaseList(), activity, activity);
        recyclerViewDeliveryOrders.setAdapter(purchaseAdapter);

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
                        if(Data.getInstance().getPurchaseList().get(i).getReference().equals(editTextSupplier.getText().toString())){
                            purchasesFiltred.add(Data.getInstance().getPurchaseList().get(i));
                        }
                    }
                    purchaseAdapter.reset(purchasesFiltred);

                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
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
}
