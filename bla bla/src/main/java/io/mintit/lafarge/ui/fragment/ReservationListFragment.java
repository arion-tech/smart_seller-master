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

import java.text.ParseException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.mintit.lafarge.R;
import io.mintit.lafarge.adapter.ReservationsAdapter;
import io.mintit.lafarge.events.UpdateReservationEvent;
import io.mintit.lafarge.model.Article;
import io.mintit.lafarge.model.Category;
import io.mintit.lafarge.model.Reservation;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.utils.Utils;


public class ReservationListFragment extends BaseFragment implements ReservationsAdapter.OnItemClickListener {

    @BindView(R.id.recyclerView_reservations)
    RecyclerView recyclerViewReservations;
    Unbinder unbinder;
    private MainActivity activity;
    private ArrayList<Reservation> listReservations = new ArrayList<>();
    private ReservationsAdapter reservationsAdapter;
    private Reservation c1 = new Reservation();
    private Reservation c2 = new Reservation();
    Article p1 = new Article();
    Article p2 = new Article();
    Article p3 = new Article();
    Category cat1 = new Category();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reservation_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        activity = (MainActivity) getActivity();
        EventBus.getDefault().register(this);
        initViews();
        importData();
        return view;
    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerViewReservations.setLayoutManager(layoutManager);
        reservationsAdapter = new ReservationsAdapter(listReservations, activity, activity);
        reservationsAdapter.setOnItemClickListener(this);
        recyclerViewReservations.setAdapter(reservationsAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCustomerSelectedEvent(UpdateReservationEvent event) {
        if (event.getReservation() != null) {
            for (int i = 0; i < listReservations.size(); i++) {
                if (listReservations.get(i).getId().equals(event.getReservation().getId())) {
                    listReservations.remove(i);
                    listReservations.add(i, event.getReservation());
                    reservationsAdapter.updateItem(i, event.getReservation());
                    break;
                }
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

    @Override
    public void onItemClick(Reservation res) {
        activity.addFragment(ReservationFragment.newInstance(res), "", true);
    }

    @Override
    public void onItemDelete(final Reservation res) {
        listReservations.remove(res);
        reservationsAdapter.removeItem(res);
    }

    private void importCarts(ArrayList<Reservation> ress){
        //c1.set
        cat1.setId((long) 11);
        cat1.setLibelle("Chaussures");
        cat1.setLevel((long) 3);

        p1.setId(1);
        p1.setName("Chaussure X");
        p1.setPrice(18.5);
        p1.setStock(45);
        p1.setProductCode("CN5268");
        p1.setEanCode("84165787");
        p1.setCategory(cat1);
        p1.setImageProduct("https://images-na.ssl-images-amazon.com/images/I/71VTSkvMa8L._UY395_.jpg");
        p1.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec massa orci, dignissim a scelerisque nec, suscipit et diam. Sed non tristique nunc, sed mollis nibh.");
        p1.setQty(5);

        p2.setId(2);
        p2.setName("Pantalon GHF");
        p2.setPrice(66.8);
        p2.setStock(124);
        p2.setProductCode("PL1110");
        p2.setCategory(cat1);
        p2.setEanCode("1111");
        p2.setQty(3);

        p3.setId(3);
        p3.setName("Shorts KK");
        p3.setPrice(55.9);
        p3.setStock(66);
        p3.setProductCode("SR8584");
        p3.setCategory(cat1);
        p3.setEanCode("2222");
        p3.setQty(4);

        ArrayList<Article> listProductForCart = new ArrayList<>();
        listProductForCart.add(p1);
        listProductForCart.add(p2);
        ArrayList<Article> listProductForCart2 = new ArrayList<>();
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
        //c1.setTotal(660.6);
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
        //c2.setTotal(226.0);
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

        ress.add(c1);
        ress.add(c2);
    }

    @SuppressLint("CheckResult")
    private void importData(){
        //Get Carts by Seller/User
        ArrayList<Reservation> importedRess = new ArrayList<>();
        importCarts(importedRess);
        //On Complete fetching data from API
        if(importedRess.size()>0){
            /*for (int i=0; i<importedRess.size(); i++){
                long diff = Long.parseLong(Utils.generateTimestamp()) - Long.parseLong(importedCarts.get(i).getLastModification());

                if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) <= 4) {
                    if (importedCarts.get(i).getClosed()) {
                        listCartsClosed.add(importedCarts.get(i));
                    } else {
                        listCarts.add(importedCarts.get(i));
                    }
                }
                listReservations.add(importedRess.get(i));
            }*/
            listReservations.addAll(importedRess);
        }
        initData();
    }

    private void initData() {
        if (isAdded()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    reservationsAdapter.addAll(listReservations);
                    activity.showProgressBar(false);
                }
            });
        }
    }

    @OnClick(R.id.linearLayout_create_reservation)
    public void onViewClicked() {
        activity.addFragment(ReservationFragment.newInstance(null), "", false);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}