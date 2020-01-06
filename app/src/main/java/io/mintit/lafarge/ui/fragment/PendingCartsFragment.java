package io.mintit.lafarge.ui.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.mintit.lafarge.R;
import io.mintit.lafarge.adapter.SalesAdapter;
import io.mintit.lafarge.events.UpdateCartEvent;
import io.mintit.lafarge.model.Product;
import io.mintit.lafarge.model.Cart;
import io.mintit.lafarge.model.Category;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.utils.Utils;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class PendingCartsFragment extends BaseFragment implements SalesAdapter.OnItemClickListener {

    @BindView(R.id.recyclerView_carts)
    RecyclerView recyclerViewCarts;
    Unbinder unbinder;
    @BindView(R.id.recyclerView_carts_closed)
    RecyclerView recyclerViewCartsClosed;
    private MainActivity activity;
    private ArrayList<Cart> listCarts = new ArrayList<>();
    private ArrayList<Cart> listCartsClosed = new ArrayList<>();
    private SalesAdapter salesAdapter;
    private SalesAdapter salesAdapterClosed;

    private Cart c1 = new Cart();
    private Cart c2 = new Cart();

    Product p1 = new Product();
    Product p2 = new Product();
    Product p3 = new Product();
    Category cat1 = new Category();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_carts, container, false);
        unbinder = ButterKnife.bind(this, view);
        activity = (MainActivity) getActivity();
        EventBus.getDefault().register(this);
        System.out.println("Exectued onCreateView");
        importCartsFromDB();
        initViews();
        importData();
        //deleteAllCartsDB();
        return view;
    }

    private void deleteAllCartsDB(){
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                activity.getLafargeDatabase().cartDao().emptyCarts();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d("**ROOM SUCCESS DELETE**", "**********Carts*********");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("***ERROR ROOM Stock***", e.getMessage());
                    }
                });
    }



    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(activity);
        recyclerViewCarts.setLayoutManager(layoutManager);
        recyclerViewCartsClosed.setLayoutManager(layoutManager2);

        salesAdapter = new SalesAdapter(listCarts, activity, activity);
        salesAdapter.setOnItemClickListener(this);
        recyclerViewCarts.setAdapter(salesAdapter);

        salesAdapterClosed = new SalesAdapter(listCartsClosed, activity, activity);
        salesAdapterClosed.setOnItemClickListener(this);
        recyclerViewCartsClosed.setAdapter(salesAdapterClosed);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCustomerSelectedEvent(UpdateCartEvent event) {
        if (event.getCart() != null) {
            for (int i = 0; i < listCarts.size(); i++) {
                if (listCarts.get(i).getId().equals(event.getCart().getId())) {
                    if (!event.getCart().getClosed()) {
                        listCarts.remove(i);
                        listCarts.add(i, event.getCart());
                        salesAdapter.updateItem(i, event.getCart());
                    } else {
                        listCarts.remove(i);
                        listCartsClosed.add(event.getCart());
                        salesAdapterClosed.addItem(event.getCart());
                    }
                    break;
                }
            }

        }
    }


    @Override
    public void onFragResume() {
        //System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
        //importCartsFromDB();
        initViews();
        System.out.println("Exectued onFragResume");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(Cart cart) {
        activity.addFragment(CartFragment.newInstance(cart, cart.getClosed(), activity.getDaily() != null), "", true);
    }

    @Override
    public void onItemDelete(final Cart cart) {
        listCarts.remove(cart);
        salesAdapter.removeItem(cart);
        deleteCartById(cart.getId());
        if (salesAdapter.getItemCount() == 0) {
            activity.setLastOpenedCart(null);
        }
    }

    private void deleteCartById(String id){
        System.out.println("Delete Cart from the list with ID = " + id);
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                activity.getLafargeDatabase().cartDao().deleteCartById(id);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d("*****ROOM SUCCESS*****", "*********delete cart**********");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ERROR ROOM delete cart", e.getMessage());
                    }
                });
    }


    private void importCarts(ArrayList<Cart> carts){
        //c1.set
        cat1.setId((long) 11);
        cat1.setLibelle("Chaussures");
        cat1.setLevel((long) 3);

        p1.setId("1");
        p1.setName("Chaussure X");
        p1.setPrice(18.5);
        p1.setStock(45);
        p1.setProductCode("CN5268");
        p1.setEanCode("84165787");
        p1.setCategory(cat1);
        p1.setImageProduct("https://images-na.ssl-images-amazon.com/images/I/71VTSkvMa8L._UY395_.jpg");
        p1.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec massa orci, dignissim a scelerisque nec, suscipit et diam. Sed non tristique nunc, sed mollis nibh.");

        p2.setId("2");
        p2.setName("Pantalon GHF");
        p2.setPrice(66.8);
        p2.setStock(124);
        p2.setProductCode("PL1110");
        p2.setCategory(cat1);
        p2.setEanCode("1111");

        p3.setId("3");
        p3.setName("Shorts KK");
        p3.setPrice(55.9);
        p3.setStock(66);
        p3.setProductCode("SR8584");
        p3.setCategory(cat1);
        p3.setEanCode("2222");

        ArrayList<Product> listProductForCart = new ArrayList<>();
        listProductForCart.add(p1);
        listProductForCart.add(p2);
        ArrayList<Product> listProductForCart2 = new ArrayList<>();
        listProductForCart2.add(p3);
        //------------
        c1.setAgentID(String.valueOf(activity.getSellerLogin().getId()));
        c1.setAgentName(activity.getSellerLogin().getLibelle());
        c1.setCurrencyId(activity.getEtablissement().getCurrencyId());
        c1.setCompany(false);
        //c1.setCustomer(mCustomer.getId()+"");
        c1.setCustomer("1234");
        c1.setCustomerFirstName("Achraf");
        c1.setCustomerLastName("Abdennadher");
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
        c1.setPaid(false);
        c1.setClosed(false);
        c1.setId("1111");
        c1.setSellerId(activity.getSellerLogin().getId()+"");


        c2.setAgentID(String.valueOf(activity.getSellerLogin().getId()));
        c2.setAgentName(activity.getSellerLogin().getLibelle());
        c2.setCurrencyId(activity.getEtablissement().getCurrencyId());
        c2.setCompany(false);
        //c2.setCustomer(mCustomer.getId()+"");
        c2.setCustomer("1234");
        c2.setCustomerFirstName("Achraf");
        c2.setCustomerLastName("Abdennadher");
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
        c2.setClosed(true);
        c2.setId("2222");
        c2.setSellerId(activity.getSellerLogin().getId()+"");

        carts.add(c1);
        carts.add(c2);
    }

    @SuppressLint("CheckResult")
    private void importData(){
        //Get Carts by Seller/User
        ArrayList<Cart> importedCarts = new ArrayList<>();
        importCarts(importedCarts);

        //On Complete fetching data from API
        if(importedCarts.size()>0){
            for (int i=0; i<importedCarts.size(); i++){
                /*long diff = Long.parseLong(Utils.generateTimestamp()) - Long.parseLong(importedCarts.get(i).getLastModification());

                if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) <= 4) {
                    if (importedCarts.get(i).getClosed()) {
                        listCartsClosed.add(importedCarts.get(i));
                    } else {
                        listCarts.add(importedCarts.get(i));
                    }
                }*/
                if (importedCarts.get(i).getClosed()) {
                    listCartsClosed.add(importedCarts.get(i));
                } else {
                    listCarts.add(importedCarts.get(i));
                }
            }
        }
        initData();
    }

    @SuppressLint("CheckResult")
    private void importCartsFromDB(){
        System.out.println("Importing Carts from ROOM DB");
        activity.getLafargeDatabase().cartDao().getCartByUser(activity.getSellerLogin().getId()+"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Cart>>() {
                    @Override
                    public void accept(List<Cart> carts) {
                        if(carts.size()>0){
                            for (int i=0; i<carts.size(); i++){
                                System.out.println("REAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADED CARTS" + carts.get(i).toString());
                                long diff = Long.parseLong(Utils.generateTimestamp()) - Long.parseLong(carts.get(i).getLastModification());
                                if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) <= 4) {
                                    if (carts.get(i).getClosed()) {
                                        listCartsClosed.add(carts.get(i));
                                        //salesAdapterClosed.notifyItemInserted(salesAdapterClosed.getItemCount());
                                        //salesAdapterClosed.notifyItemChanged(salesAdapterClosed.getItemCount());
                                    } else {
                                        listCarts.add(carts.get(i));
                                        //salesAdapter.notifyItemInserted(salesAdapterClosed.getItemCount());
                                        //salesAdapter.notifyItemChanged(salesAdapterClosed.getItemCount());
                                    }
                                }
                            }
                        }
                        //---
                        initDataDB();
                        salesAdapterClosed.notifyDataSetChanged();
                        salesAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void initData() {
        if (isAdded()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    salesAdapter.addAll(listCarts);
                    salesAdapterClosed.addAll(listCartsClosed);
                    activity.showProgressBar(false);
                }
            });
        }
    }

    private void initDataDB() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                salesAdapter.addAll(listCarts);
                salesAdapterClosed.addAll(listCartsClosed);
                activity.showProgressBar(false);
            }
        });
    }

    @OnClick(R.id.linearLayout_create_car)
    public void onViewClicked() {
        activity.addFragment(CartFragment.newInstance(null, false, activity.getDaily() != null), "", false);
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
