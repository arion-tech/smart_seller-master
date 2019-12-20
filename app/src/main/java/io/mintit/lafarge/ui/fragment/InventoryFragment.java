package io.mintit.lafarge.ui.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.mintit.lafarge.R;
import io.mintit.lafarge.adapter.InventoryAdapter;
import io.mintit.lafarge.events.InventoryEvent;
import io.mintit.lafarge.model.Inventory;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class InventoryFragment extends BaseFragment implements InventoryAdapter.OnItemClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    @BindView(R.id.recyclerView_inventory)
    RecyclerView recyclerViewInventory;
    @BindView(R.id.recyclerView_inventory_closed)
    RecyclerView recyclerViewInventoryClosed;
    Unbinder unbinder;
    private MainActivity activity;
    private InventoryAdapter inventoryAdapter;
    private InventoryAdapter inventoryAdapterClosed;
    private ArrayList<Inventory> listInventory = new ArrayList<Inventory>();
    private ArrayList<Inventory> listInventoryClosed = new ArrayList<Inventory>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);
        unbinder = ButterKnife.bind(this, view);
        activity = (MainActivity) getActivity();
        loadInventories();
        EventBus.getDefault().register(this);
        return view;
    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerViewInventory.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(activity);
        recyclerViewInventoryClosed.setLayoutManager(layoutManager2);

        inventoryAdapter = new InventoryAdapter(listInventory, activity, activity);
        inventoryAdapterClosed = new InventoryAdapter(listInventoryClosed, activity, activity);
        inventoryAdapter.setOnItemClickListener(this);
        inventoryAdapterClosed.setOnItemClickListener(this);
        recyclerViewInventory.setAdapter(inventoryAdapter);
        recyclerViewInventoryClosed.setAdapter(inventoryAdapterClosed);
    }

    @Override
    public void onFragResume() {

    }


    @SuppressLint("CheckResult")
    private void loadInventories(){
        activity.showProgressBar(true);
        activity.getLafargeDatabase().inventoryDao().getAllInventory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Inventory>>() {
                    @Override
                    public void accept(List<Inventory> inventories){
                        if(inventories.size()>0){
                            for(int i=0;i<inventories.size();i++){
                                if (inventories.get(i).getIsValidated()) {
                                    listInventoryClosed.add(inventories.get(i));
                                } else {
                                    listInventory.add(inventories.get(i));
                                }
                            }
                        }
                        if (isAdded()) {
                           activity.showProgressBar(false);
                            initViews();
                        }
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInventoryEvent(InventoryEvent event) {
        if (event.getInventory() != null) {
            if (!event.getInventory().getIsValidated()) {
                for (int i = 0; i < listInventory.size(); i++) {
                    if (listInventory.get(i).getCodeliste().equals(event.getInventory().getCodeliste())) {
                        inventoryAdapter.updateItem(i, event.getInventory());
                        break;
                    }
                }
            } else {
                listInventoryClosed.add(event.getInventory());
                inventoryAdapterClosed.notifyItemInserted(inventoryAdapterClosed.getItemCount());
                for (int i = 0; i < listInventory.size(); i++) {
                    if (listInventory.get(i).getCodeliste().equals(event.getInventory().getCodeliste())) {
                        listInventory.remove(i);
                        inventoryAdapter.notifyItemRemoved(i);
                        break;
                    }
                }

            }
            initViews();
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(Inventory inventory) {
        activity.addFragment(InventoryProductsFragment.newInstance(inventory), "", true);
    }
}
