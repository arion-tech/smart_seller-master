package io.mintit.lafarge.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.mintit.lafarge.R;
import io.mintit.lafarge.adapter.FragmentPagerAdapter;
import io.mintit.lafarge.ui.widget.CustomViewPager;


public class PurchaseOrdersFragment extends BaseFragment {
    @BindView(R.id.pagerPurchase)
    CustomViewPager pagerPurchase;
    Unbinder unbinder;
    @BindView(R.id.radioButton_purchase)
    RadioButton radioButtonPurchase;
    @BindView(R.id.radioButton_delivery)
    RadioButton radioButtonDelivery;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_purchase_orders, container, false);
        unbinder = ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        radioButtonPurchase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (radioButtonPurchase.isChecked()) {
                    radioButtonDelivery.setChecked(false);
                    pagerPurchase.setCurrentItem(0, false);
                }
            }
        });
        radioButtonDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (radioButtonDelivery.isChecked()) {
                    radioButtonPurchase.setChecked(false);
                    pagerPurchase.setCurrentItem(1, false);
                }
            }
        });

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager());
        pagerPurchase.setAdapter(fragmentPagerAdapter);

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
