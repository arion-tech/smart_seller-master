package io.mintit.lafarge.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import io.mintit.lafarge.ui.fragment.DeliveryOrdersFragment;
import io.mintit.lafarge.ui.fragment.PurchasesListFragment;

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    public FragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PurchasesListFragment();

            case 1:
                return new DeliveryOrdersFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
