package io.mintit.lafarge.ui.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hendrix.pdfmyxml.PdfDocument;
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.mintit.lafarge.R;
import io.mintit.lafarge.adapter.ProductsAdapter;
import io.mintit.lafarge.events.UpdateCartEvent;
import io.mintit.lafarge.global.Constants;
import io.mintit.lafarge.model.Address;
import io.mintit.lafarge.model.Article;
import io.mintit.lafarge.model.Cart;
import io.mintit.lafarge.model.Commande;
import io.mintit.lafarge.model.Customer;
import io.mintit.lafarge.model.Payment;
import io.mintit.lafarge.model.SalesDocument;
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
 * Use the {@link CloseCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CloseCartFragment extends BaseFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_CART = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.textview_customer_name)
    TextView textviewCustomerName;
    @BindView(R.id.textview_address)
    TextView textviewAddress;
    @BindView(R.id.textview_email)
    TextView textviewEmail;
    @BindView(R.id.linearLayout_container)
    LinearLayout linearLayoutContainer;
    @BindView(R.id.radioButton_email)
    CheckBox radioButtonEmail;
    @BindView(R.id.radioButton_print)
    CheckBox radioButtonPrint;
    @BindView(R.id.radioButton_paiement)
    RadioButton radioButtonPaiement;
    @BindView(R.id.radioButton_command)
    RadioButton radioButtonCommand;
    @BindView(R.id.recyclerView_products)
    RecyclerView recyclerViewProducts;
    @BindView(R.id.linearLayout_command)
    LinearLayout linearLayoutCommand;
    @BindView(R.id.textView_remaining)
    TextView textViewRemaining;
    @BindView(R.id.relativelayout_cash)
    RelativeLayout relativelayoutCash;
    @BindView(R.id.relativelayout_check)
    RelativeLayout relativelayoutCheck;
    @BindView(R.id.relativelayout_cb)
    RelativeLayout relativelayoutCb;
    @BindView(R.id.linearLayout_paiement_detail)
    LinearLayout linearLayoutPaiementDetail;
    @BindView(R.id.linearLayout_finish_cart)
    LinearLayout linearLayoutFinishCart;
    Unbinder unbinder;
    @BindView(R.id.checkbox_cash)
    CheckBox checkboxCash;
    @BindView(R.id.checkbox_check)
    CheckBox checkboxCheck;
    @BindView(R.id.checkbox_cb)
    CheckBox checkboxCb;
    @BindView(R.id.edittext_cash)
    EditText edittextCash;
    @BindView(R.id.edittext_cb)
    EditText edittextCb;
    @BindView(R.id.edittext_check)
    EditText edittextCheck;
    @BindView(R.id.edittext_email)
    EditText edittextEmail;
    @BindView(R.id.relativeLayout_email)
    LinearLayout relativeLayoutEmail;
    @BindView(R.id.textview_product_name)
    TextView textviewProductName;
    @BindView(R.id.textview_product_code)
    TextView textviewProductCode;
    @BindView(R.id.textview_product_ref)
    TextView textviewProductRef;
    @BindView(R.id.textview_product_stock)
    TextView textviewProductStock;
    @BindView(R.id.textview_product_price)
    TextView textviewProductPrice;
    @BindView(R.id.linearLayout_email)
    LinearLayout linearLayoutEmail;
    @BindView(R.id.linearLayout_print)
    LinearLayout linearLayoutPrint;
    @BindView(R.id.textview_amount)
    TextView textviewAmount;
    @BindView(R.id.textView_seller)
    TextView textViewSeller;
    @BindView(R.id.linearLayout_seller)
    LinearLayout linearLayoutSeller;
    @BindView(R.id.checkbox_cash_icon)
    CheckBox checkboxCashIcon;
    @BindView(R.id.checkbox_check_icon)
    CheckBox checkboxCheckIcon;
    @BindView(R.id.checkbox_cb_icon)
    CheckBox checkboxCbIcon;
    Payment cash, creditCard, cheque;
    private Cart mCart;
    private String mParam2;
    private MainActivity activity;
    private ProductsAdapter productsAdapter;
    private ArrayList<Article> listProducts = new ArrayList<>();
    private Customer customer;
    private double remaining = 0;


    public CloseCartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CloseCartFragment.
     */
    public static CloseCartFragment newInstance(Cart param1, String param2) {
        CloseCartFragment fragment = new CloseCartFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_CART, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCart = getArguments().getParcelable(ARG_PARAM_CART);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_close_cart, container, false);
        activity = (MainActivity) getActivity();
        unbinder = ButterKnife.bind(this, view);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initViews();
        initData();
        //setupPdfRenderer();
        return view;
    }

    private void setupPdfRenderer() {
        AbstractViewRenderer page = new AbstractViewRenderer(activity, R.layout.fragment_close_cart) {
            @Override
            protected void initView(View view) {

            }
        };

// you can reuse the bitmap if you want
        page.setReuseBitmap(true);
        new PdfDocument.Builder(activity).addPage(page).orientation(PdfDocument.A4_MODE.LANDSCAPE)
                .progressMessage(R.string.gen_pdf_file).progressTitle(R.string.gen_please_wait)
                .renderWidth(2115).renderHeight(1500)
                .saveDirectory(activity.getExternalFilesDir(null))
                .filename("test")
                .listener(new PdfDocument.Callback() {
                    @Override
                    public void onComplete(File file) {
                        DebugLog.d(PdfDocument.TAG_PDF_MY_XML + "Complete " + file.getAbsolutePath());
                    }

                    @Override
                    public void onError(Exception e) {
                        DebugLog.d(PdfDocument.TAG_PDF_MY_XML + "Error");
                    }
                }).create().createPdf(activity);
    }

    private void initData() {
        if (mCart.getCustomer() != null) {
            textviewCustomerName.setText(mCart.getCustomerLastName() + " " + mCart.getCustomerFirstName());
            getCustomer();

        }
        listProducts = mCart.getProductList();
        productsAdapter.addAll(listProducts);
        DebugLog.d("getItemCount " + productsAdapter.getItemCount() + " " + (recyclerViewProducts.getVisibility() == View.VISIBLE) + " " + (linearLayoutCommand.getVisibility() == View.VISIBLE));


        textViewSeller.setText(" "+mCart.getSellerLibelle());
        System.out.println("NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN : " + mCart.getSellerLibelle());
        textViewRemaining.setText(mCart.getTotal() + " " + mCart.getCurrencyId());
        remaining = mCart.getTotal();
        textviewAmount.setText(mCart.getTotal() + " " + mCart.getCurrencyId());

    }

    @SuppressLint("CheckResult")
    private void getCustomer() {
        activity.getLafargeDatabase().customerDao().getCustomersByIdOrName(mCart.getCustomer(), mCart.getCustomerFirstName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Customer>>() {
                    @Override
                    public void accept(List<Customer> customers) {
                        if (customers.size() > 0) {
                            customer = customers.get(0);
                            textviewAddress.setText(customer.getAddressLine1());
                            textviewEmail.setText(customer.getEmail());
                            edittextEmail.setText(customer.getEmail());
                            initViews();
                        }
                    }
                });
    }

    private void initViews() {
        linearLayoutCommand.setVisibility(View.GONE);
        radioButtonEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radioButtonEmail.isChecked()) {
                    relativeLayoutEmail.setVisibility(View.VISIBLE);

                } else {
                    relativeLayoutEmail.setVisibility(View.GONE);
                }
            }
        });
        radioButtonPrint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
        radioButtonCommand.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radioButtonCommand.isChecked()) {
                    linearLayoutCommand.setVisibility(View.VISIBLE);
                    recyclerViewProducts.setVisibility(View.VISIBLE);
                    linearLayoutPaiementDetail.setVisibility(View.GONE);
                    radioButtonPaiement.setChecked(false);
                }
            }
        });
        radioButtonPaiement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radioButtonPaiement.isChecked()) {
                    linearLayoutCommand.setVisibility(View.GONE);
                    linearLayoutPaiementDetail.setVisibility(View.VISIBLE);
                    radioButtonCommand.setChecked(false);
                }
            }
        });
        checkboxCashIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkboxCash.performClick();
            }
        });

        checkboxCbIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkboxCb.performClick();
            }
        });

        checkboxCheckIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkboxCheck.performClick();
            }
        });

        checkboxCash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                relativelayoutCash.setVisibility(checkboxCash.isChecked() ? View.VISIBLE : View.GONE);
                checkboxCashIcon.setChecked(checkboxCash.isChecked());
                if (checkboxCash.isChecked()) {

                    double value = mCart.getTotal();
                    double minus = 0;
                    if (checkboxCb.isChecked()) {
                        minus += Double.parseDouble(edittextCb.getText().toString());
                    }
                    if (checkboxCheck.isChecked()) {
                        minus += Double.parseDouble(edittextCheck.getText().toString());
                    }
                    edittextCash.setText(String.valueOf(value - minus));

                    cash = new Payment();
                    cash.setAmount(value - minus);
                    cash.setCurrencyId(mCart.getCurrencyId());
                    cash.setMethodId("EEU");
                    cash.setIsReceivedPayment(false);
                    try {
                        cash.setDueDate(Utils.getCurrentFullDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    cash = null;
                    edittextCash.setText("0");
                }
            }
        });

        checkboxCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                relativelayoutCb.setVisibility(checkboxCb.isChecked() ? View.VISIBLE : View.GONE);
                checkboxCbIcon.setChecked(checkboxCb.isChecked());
                if (checkboxCb.isChecked()) {
                    double value = mCart.getTotal();
                    double minus = 0;
                    if (checkboxCash.isChecked()) {
                        minus += Double.parseDouble(edittextCash.getText().toString());
                    }
                    if (checkboxCheck.isChecked()) {
                        minus += Double.parseDouble(edittextCheck.getText().toString());
                    }
                    edittextCb.setText(String.valueOf(value - minus));

                    creditCard = new Payment();
                    creditCard.setAmount(value - minus);
                    creditCard.setCurrencyId(mCart.getCurrencyId());
                    creditCard.setMethodId("CBE");
                    creditCard.setIsReceivedPayment(false);
                    try {
                        creditCard.setDueDate(Utils.getCurrentFullDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    creditCard = null;
                    edittextCb.setText("0");
                }
            }
        });

        checkboxCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                relativelayoutCheck.setVisibility(checkboxCheck.isChecked() ? View.VISIBLE : View.GONE);
                checkboxCheckIcon.setChecked(checkboxCheck.isChecked());
                if (checkboxCheck.isChecked()) {
                    double value = mCart.getTotal();
                    double minus = 0;
                    if (checkboxCash.isChecked()) {
                        minus += Double.parseDouble(edittextCash.getText().toString());
                    }
                    if (checkboxCb.isChecked()) {
                        minus += Double.parseDouble(edittextCb.getText().toString());
                    }
                    edittextCheck.setText(String.valueOf(value - minus));
                    cheque = new Payment();
                    cheque.setAmount(value - minus);
                    cheque.setCurrencyId(mCart.getCurrencyId());
                    cheque.setMethodId("CQE");
                    cheque.setIsReceivedPayment(false);
                    try {
                        cheque.setDueDate(Utils.getCurrentFullDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    cheque = null;
                    edittextCheck.setText("0");

                }
            }
        });
        edittextCash.addTextChangedListener(new GenericTextWatcher(edittextCash));
        edittextCb.addTextChangedListener(new GenericTextWatcher(edittextCb));
        edittextCheck.addTextChangedListener(new GenericTextWatcher(edittextCheck));
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerViewProducts.setLayoutManager(layoutManager);
        productsAdapter = new ProductsAdapter(listProducts, activity, activity, false,false);
        recyclerViewProducts.setAdapter(productsAdapter);

    }

    @Override
    public void onFragResume() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.linearLayout_finish_cart)
    public void onViewClicked() {
        if (remaining > 0) {
            Utils.showAlert(getString(R.string.error_message_invalid_paiement), activity, null, false, true);
        } else if (radioButtonEmail.isChecked() && TextUtils.isEmpty(edittextEmail.getText().toString())) {
            Utils.showAlert(getString(R.string.error_message_setup_email), activity, null, false, true);
        } else {
            Utils.showAlert(getString(R.string.info_message_submit_paiement), activity, new Runnable() {
                @Override
                public void run() {
                    mCart.setClosed(true);
                    Toast.makeText(activity, "Paiement validé", Toast.LENGTH_SHORT).show();
                    activity.showProgressBar(true);
                    saveSalesDocument();

                    closeCart();
                }
            }, true, true);
        }
    }

    private void closeCart() {
        EventBus.getDefault().post(new UpdateCartEvent(mCart));
        activity.setLastOpenedCart(null);
        activity.addFragment(new PendingCartsFragment(), "", false);
        //activity.checkLastModifiedCart();

    }

    private void saveSalesDocument() {
        updateCartDB();
        SalesDocument salesDocument = new SalesDocument();
        //--- Begin standard fields ----
        salesDocument.setActive(true);
        try {
            salesDocument.setDate(Utils.getCurrentFullDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        salesDocument.setInternalReference(UUID.randomUUID().toString());
        salesDocument.setCurrencyId(mCart.getCurrencyId());
        salesDocument.setLinesUnmodifiable(true);
        //TODO extract constant fields
        salesDocument.setOrigin("ECommerce");
        salesDocument.setType("Receipt");
        salesDocument.setBillingStatus("Totally");
        salesDocument.setFollowUpStatus("ToBeProcessed");
        salesDocument.setReturnStatus("NotReturned");
        salesDocument.setShippingStatus("Pending");
        salesDocument.setDeliveryType("BookedInStore");
        salesDocument.setPaymentStatus("Totally");
        //--- End standard fields ----
        // TODO: 03/04/19 store id must be 300
       // salesDocument.setStoreId(activity.getEtablissement().getCodeEtablissement());
        salesDocument.setStoreId("999");
        salesDocument.setDeliveryStoreId(activity.getEtablissement().getCodeEtablissement());

        if (customer.getCustomerId()!=null && !TextUtils.isEmpty(customer.getCustomerId())) {
            salesDocument.setCustomerId(customer.getCustomerId());
        } else {
            salesDocument.setCustmerReference(customer.getReference());
        }
        Address address = new Address();
        address.setCity(customer.getCity());
        address.setCountryId(customer.getCountryId());
        address.setFirstName(customer.getFirstName());
        address.setLastName(customer.getLastName());
        address.setAddressLine1(customer.getAddressLine1());
        address.setAddressLine2(customer.getAddressLine2());
        address.setAddressLine3(customer.getAddressLine3());
        address.setTitleId(customer.getTitle());
        address.setZipCode(customer.getZipCode());
        String phone = "";
        if (customer.getCellularPhoneNumber() != null) {
            phone = customer.getCellularPhoneNumber();
        }
        if (customer.getHomePhoneNumber() != null) {
            phone = customer.getHomePhoneNumber();
        }
        if (customer.getOfficePhoneNumber() != null) {
            phone = customer.getOfficePhoneNumber();
        }
        if (customer.getAlternatePhoneNumber() != null) {
            phone = customer.getAlternatePhoneNumber();
        }
        address.setPhoneNumber(phone);
        salesDocument.setAddress(address);
        ArrayList<Commande> commandes = new ArrayList<>();
        for (int i = 0; i < mCart.getProductList().size(); i++) {
            Commande commande = new Commande();
            commande.setArticle(mCart.getProductList().get(i));
            commande.setReference(mCart.getProductList().get(i).getEanCode());
            commande.setQuantity(mCart.getProductList().get(i).getQty());
            commande.setBasketId(i);
            commande.setArticleId(" " + mCart.getProductList().get(i).getId());
            commande.setOrigin(salesDocument.getOrigin());
            //commande.setTaxIncludedUnitPrice(mCart.getProductList().get(i).getPrice());
            //commande.setTaxIncludedNetUnitPrice(mCart.getProductList().get(i).getPrice());
            commande.setTaxExcludedNetUnitPrice(0);
            commande.setTaxExcludedUnitPrice(0);
            commandes.add(commande);
            commande.setId(mCart.getId());
        }
        salesDocument.setCommandes(commandes);
        ArrayList<Payment> payments = new ArrayList<>();
        if (cash != null) {
            cash.setPaymentId(payments.size() + 1);
            payments.add(cash);
        }
        if (creditCard != null) {
            creditCard.setPaymentId(payments.size() + 1);
            payments.add(cash);
        }
        if (cheque != null) {
            cheque.setPaymentId(payments.size() + 1);
            payments.add(cheque);
        }

       // salesDocument.setPayments(payments);

        DebugLog.d(new Gson().toJson(salesDocument));


        new ActionRequestsManager(activity).createNewActionRequest(salesDocument, "Insert", Constants.ACTION_REQUEST_SALES_DOCUMENT);


    }

    private void updateCartDB() {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                activity.getLafargeDatabase().cartDao().updateBasketItem(mCart);
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

    @OnClick({R.id.linearLayout_email, R.id.linearLayout_print})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linearLayout_email:
                radioButtonEmail.performClick();
                break;
            case R.id.linearLayout_print:
                radioButtonPrint.performClick();
                break;
        }
    }

    private class GenericTextWatcher implements TextWatcher {

        private EditText view;

        private GenericTextWatcher(EditText view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            if (TextUtils.isEmpty(text)) {
                view.setText("0");

            }
            remaining = mCart.getTotal() - (Double.parseDouble(edittextCash.getText().toString()) + Double.parseDouble(edittextCb.getText().toString()) + Double.parseDouble(edittextCheck.getText().toString()));
            if (remaining < 0) {
                view.setText("0");
                view.setSelection(1);
                Toast.makeText(activity, R.string.error_message_invalid_amount, Toast.LENGTH_SHORT).show();
            } else {
                textViewRemaining.setText(String.valueOf(remaining) + " " + mCart.getCurrencyId());
            }

        }
    }
}
