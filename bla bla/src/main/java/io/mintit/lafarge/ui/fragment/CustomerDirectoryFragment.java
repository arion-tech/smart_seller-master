package io.mintit.lafarge.ui.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
import io.mintit.lafarge.adapter.CustomersAdapter;
import io.mintit.lafarge.data.Data;
import io.mintit.lafarge.events.CustomerSelectedEvent;
import io.mintit.lafarge.events.UpdateCustomerInfoEvent;
import io.mintit.lafarge.model.Article;
import io.mintit.lafarge.model.Cart;
import io.mintit.lafarge.model.Customer;
import io.mintit.lafarge.model.Reservation;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerDirectoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerDirectoryFragment extends BaseFragment implements CustomersAdapter.OnItemClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM_SELECT = "slect";
    private static final String ARG_PARAM_CART = "ARG_PARAM_CART";
    private static final String ARG_PARAM_RES = "ARG_PARAM_RES";
    private static final String ARG_PARAM_IS_COMPANY = "IS_COMPANY";

    @BindView(R.id.edittext_filter)
    EditText edittextFilter;
    @BindView(R.id.recyclerView_customers)
    RecyclerView recyclerViewCustomers;
    Unbinder unbinder;
    @BindView(R.id.linearLayout_back)
    LinearLayout linearLayoutBack;
    @BindView(R.id.linearLayout_new_customer)
    LinearLayout linearLayoutNewCustomer;
    @BindView(R.id.relativeLayout_add_to_cart)
    RelativeLayout relativeLayoutAddToCart;

    private MainActivity activity;
    private ArrayList<Customer> listCustomerFiltred = new ArrayList<>();
    private ArrayList<Customer> listCustomer = new ArrayList<>();
    private CustomersAdapter customersAdapter;
    private boolean selectCustomer;
    private Cart cart;
    private Reservation res;
    private boolean isCompany;
    private int pageindex;
    private String constraint = "";
    //private int visibleItemCount;
    //private int totalItemCount;
    //private int pastVisiblesItems;
    //private boolean end_reached = false;
    private boolean loading;

    private ArrayList<Customer> searchedCustomersList = new ArrayList<>();

    //-------
    Customer c1 = new Customer();
    Customer c2 = new Customer();
    Customer c3= new Customer();
    Customer c4 = new Customer();
    Customer c5 = new Customer();
    //-------


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param cart
     * @param isCompany
     * @return A new instance of fragment CustomerDire  ctoryFragment.
     */
    public static CustomerDirectoryFragment newInstance( boolean selectCustomer, Cart cart, boolean isCompany) {
        CustomerDirectoryFragment fragment = new CustomerDirectoryFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM_SELECT, selectCustomer);
        args.putBoolean(ARG_PARAM_IS_COMPANY, isCompany);
        args.putParcelable(ARG_PARAM_CART, cart);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param res
     * @param isCompany
     * @return A new instance of fragment CustomerDirectoryFragment.
     */
    public static CustomerDirectoryFragment newInstanceForReservation(boolean selectCustomer, Reservation res, boolean isCompany) {
        CustomerDirectoryFragment fragment = new CustomerDirectoryFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM_SELECT, selectCustomer);
        args.putBoolean(ARG_PARAM_IS_COMPANY, isCompany);
        args.putParcelable(ARG_PARAM_RES, res);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectCustomer = getArguments().getBoolean(ARG_PARAM_SELECT);
            isCompany = getArguments().getBoolean(ARG_PARAM_IS_COMPANY);
            cart = (Cart) getArguments().getParcelable(ARG_PARAM_CART);
            res = (Reservation) getArguments().getParcelable(ARG_PARAM_RES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_directory, container, false);
        unbinder = ButterKnife.bind(this, view);
        activity = (MainActivity) getActivity();
        EventBus.getDefault().register(this);
        initViews();
        importData();
        //-------
        System.out.println("LIIIIIIIIIIIIIIIIIIIIIIIIIIIISTE - listCustomer : " + listCustomer.size());
        System.out.println("LIIIIIIIIIIIIIIIIIIIIIIIIIIIISTE - listCustomerFiltred : " + listCustomerFiltred.size());
        //-------
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCustomerSelectedEvent(UpdateCustomerInfoEvent event) {
        if (event.getCustomer() != null) {
            for (int i = 0; i < listCustomer.size(); i++) {
                if (listCustomer.get(i).getCustomerId().equals(event.getCustomer().getCustomerId())) {
                    listCustomer.remove(i);
                    listCustomer.add(i, event.getCustomer());
                    Data.getInstance().getListCustomers().remove(i);
                    Data.getInstance().getListCustomers().add(i, event.getCustomer());
                    customersAdapter.updateItem(i, event.getCustomer());

                }
            }

        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initViews() {
        relativeLayoutAddToCart.setVisibility(selectCustomer ? View.INVISIBLE : View.GONE);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerViewCustomers.setLayoutManager(layoutManager);
        /*recyclerViewCustomers.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
              if (!end_reached) {
                    if (((visibleItemCount + pastVisiblesItems) >= totalItemCount) && !loading) {
                        pageindex++;
                        loading = true;
                        loadCustomers(constraint);
                    }
                }

            }
        });*/
        customersAdapter = new CustomersAdapter(listCustomer, activity, activity);
        customersAdapter.setOnItemClickListener(this);
        customersAdapter.setSelectCustomer(selectCustomer);
        recyclerViewCustomers.setAdapter(customersAdapter);

        edittextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchedCustomersList.clear();

                //---------------
                if (edittextFilter.getText().length() == 0 && listCustomer != null) {
                    customersAdapter.reset(listCustomer);
                } else {
                    //pageindex = 0;
                    customersAdapter.clear();
                    constraint = edittextFilter.getText().toString();
                    //loadCustomers(edittextFilter.getText().toString());
                    //end_reached = true;
                    for (int i = 0; i < listCustomer.size(); i++) {
                        if (listCustomer.get(i).getFirstName().toLowerCase().startsWith(edittextFilter.getText().toString().toLowerCase()) ||
                                listCustomer.get(i).getLastName().toLowerCase().startsWith(edittextFilter.getText().toString().toLowerCase()) ||
                                listCustomer.get(i).getFirstName().toLowerCase().contains(edittextFilter.getText().toString().toLowerCase()) ||
                                listCustomer.get(i).getLastName().toLowerCase().contains(edittextFilter.getText().toString().toLowerCase()) ) {
                            searchedCustomersList.add(listCustomer.get(i));
                        }
                    }
                    setSearchedCustomers(searchedCustomersList);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
    }

    private void setSearchedCustomers(ArrayList<Customer> searchedCustomersList) {
        if (searchedCustomersList.size() > 0) {
            customersAdapter.reset(searchedCustomersList);
        } else {
            customersAdapter.clear();
        }
        System.out.println("setSearchedArticles method productAdapter " + customersAdapter.getItemCount());
    }

    private void importData() {
        activity.showProgressBar(true);
        loadCustomers(constraint);
    }


    @SuppressLint("CheckResult")
    private void loadCustomers(final String constraint){
        List<Customer> customers = new ArrayList<>();

        c1.setFirstName("Abdennadher");
        c1.setLastName("Achraf");
        c1.setSex("Homme");
        c1.setEmail("aaa@aa.com");
        c1.setAddressLine1("la petite ariana, raoued, ariana, tunisie");
        c1.setOfficePhoneNumber("55254854");
        c1.setBirthDateDay(10);
        c1.setBirthDateMonth(11);
        c1.setBirthDateYear(1994);
        c1.setId(1234);

        c2.setFirstName("Cherif");
        c2.setLastName("Firas");
        c2.setSex("Homme");
        c2.setEmail("bbb@bb.com");
        c2.setAddressLine1("Ghazela, raoued, ariana, tunisie");
        c2.setOfficePhoneNumber("99630002");
        c2.setBirthDateDay(10);
        c2.setBirthDateMonth(10);
        c2.setBirthDateYear(1993);
        c2.setId(1265);

        c3.setFirstName("Jallouli");
        c3.setLastName("Hedi");
        c3.setSex("Homme");
        c3.setEmail("ccc@cc.com");
        c3.setAddressLine1("L'aouina, ariana, tunisie");
        c3.setOfficePhoneNumber("53666251");
        c3.setBirthDateDay(23);
        c3.setBirthDateMonth(6);
        c3.setBirthDateYear(1995);
        c3.setId(1874);

        c4.setFirstName("Chelli");
        c4.setLastName("Hanen");
        c4.setSex("Femme");
        c4.setEmail("ddd@dd.com");
        c4.setAddressLine1("Sakiet ezzit, sfax, tunisie");
        c4.setOfficePhoneNumber("23321129");
        c4.setBirthDateDay(3);
        c4.setBirthDateMonth(12);
        c4.setBirthDateYear(1990);
        c4.setId(2234);

        c5.setFirstName("Hichri");
        c5.setLastName("Amal");
        c5.setSex("Femme");
        c5.setEmail("eee@ee.com");
        c5.setAddressLine1("la petite ariana, raoued, ariana, tunisie");
        c5.setOfficePhoneNumber("90805080");
        c5.setBirthDateDay(19);
        c5.setBirthDateMonth(5);
        c5.setBirthDateYear(1989);
        c5.setId(6520);

        customers.add(c1);
        customers.add(c2);
        customers.add(c3);
        customers.add(c4);
        customers.add(c5);

        if(customers.size()>0){
            if(TextUtils.isEmpty(constraint)) {
                listCustomer.clear();
                listCustomer.addAll(customers);
                //Data.getInstance().setListCustomers(listCustomer);//!constraint
                initData(listCustomer);
            }else {
                listCustomerFiltred.clear();
                listCustomerFiltred.addAll(customers);
                initData(listCustomerFiltred);

            }
            loading = false;
            //end_reached = customers.size() < 10;

        }else{
            activity.showProgressBar(false);
        }
    }




    private void initData(final ArrayList<Customer> listCustomerTemp) {
        if(cart != null) {
            if (cart.getCustomer() != null) {
                if (listCustomerTemp == null) {
                    for (int i = 0; i < listCustomer.size(); i++) {
                        if (String.valueOf(listCustomer.get(i).getId()).equals(cart.getCustomer())) {
                            listCustomer.get(i).setSelected(true);
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < listCustomerTemp.size(); i++) {
                        if (String.valueOf(listCustomerTemp.get(i).getId()).equals(cart.getCustomer())) {
                            listCustomerTemp.get(i).setSelected(true);
                            break;
                        }
                    }
                }
            }
        }
        if(res != null) {
            if (res.getCustomer() != null) {
                if (listCustomerTemp == null) {
                    for (int i = 0; i < listCustomer.size(); i++) {
                        if (String.valueOf(listCustomer.get(i).getId()).equals(res.getCustomer())) {
                            listCustomer.get(i).setSelected(true);
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < listCustomerTemp.size(); i++) {
                        if (String.valueOf(listCustomerTemp.get(i).getId()).equals(res.getCustomer()))  {
                            listCustomerTemp.get(i).setSelected(true);
                            break;
                        }
                    }
                }
            }
        }

        if (isAdded()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (listCustomerTemp == null) {
                        System.out.println("AAAAAAAAAAAAAAAAAAAAAAA");
                        customersAdapter.addAll(listCustomer);
                    } else {
                        customersAdapter.insertItems(listCustomerTemp);
                    }
                    activity.showProgressBar(false);
                }
            });
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
    public void onItemClick(Customer customer) {
        activity.addFragment(CustomerProfileFragment.newInstance(customer, "", selectCustomer), "", true);
    }

    @Override
    public void onItemSelected(Customer customer) {
        EventBus.getDefault().post(new CustomerSelectedEvent(customer));
        activity.onBackPressed();
    }

    @OnClick({R.id.linearLayout_back, R.id.linearLayout_new_customer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linearLayout_back:
                activity.onBackPressed();
                break;
            case R.id.linearLayout_new_customer:
                activity.addFragment(CustomerFragment.newInstance(null, "", selectCustomer, false, isCompany), "", true);
                break;
        }
    }

}
