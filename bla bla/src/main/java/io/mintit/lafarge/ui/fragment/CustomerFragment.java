package io.mintit.lafarge.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.mintit.lafarge.R;
import io.mintit.lafarge.events.CustomerSelectedEvent;
import io.mintit.lafarge.events.UpdateCustomerInfoEvent;
import io.mintit.lafarge.global.Constants;
import io.mintit.lafarge.model.Customer;
import io.mintit.lafarge.services.ActionRequestsManager;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.utils.Utils;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM_SELECT = "slect";
    private static final String ARG_PARAM_VIEW = "view_detail";
    private static final String ARG_PARAM_IS_COMPANY = "ARG_PARAM_IS_COMPANY";

    @BindView(R.id.edittext_firstname)
    EditText edittextFirstname;
    @BindView(R.id.edittext_address)
    EditText edittextAddress;
    @BindView(R.id.edittext_email)
    EditText edittextEmail;
    @BindView(R.id.edittext_lastname)
    EditText edittextLastname;
    @BindView(R.id.edittext_zipcode)
    EditText edittextZipcode;
    @BindView(R.id.editttext_phone)
    EditText editTextPhone;
    @BindView(R.id.edittext_date_birth)
    EditText edittextDateBirth;
    @BindView(R.id.edttext_city)
    EditText editTextCity;
    @BindView(R.id.linearLayout_reset)
    LinearLayout linearLayoutReset;
    @BindView(R.id.linearlayout_submit_client)
    LinearLayout linearlayoutSubmitClient;
    @BindView(R.id.linearLayout_actions)
    LinearLayout linearLayoutActions;
    Unbinder unbinder;
    @BindView(R.id.edittext_comment)
    EditText edittextComment;
    @BindView(R.id.switch_customer_type)
    Switch switchCustomerType;
    int year;
    int monthOfYear;
    int dayOfMonth;
    private Customer mCustomer;
    private String mParam2;
    private MainActivity activity;
    private String message;
    private boolean selectCustomer;
    private boolean viewOnly;
    private boolean isCompany;
    Date date = new Date();
    Date chosenDate;

    public CustomerFragment()  {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1         Parameter 1.
     * @param param2         Parameter 2.
     * @param selectCustomer
     * @param viewOnly
     * @param isCompany
     * @return A new instance of fragment CustomerFragment.
     */
    public static CustomerFragment newInstance(Customer param1, String param2, boolean selectCustomer, boolean viewOnly, boolean isCompany) {
        CustomerFragment fragment = new CustomerFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putBoolean(ARG_PARAM_SELECT, selectCustomer);
        args.putBoolean(ARG_PARAM_VIEW, viewOnly);
        args.putBoolean(ARG_PARAM_IS_COMPANY, isCompany);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCustomer = (Customer) getArguments().getParcelable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            selectCustomer = getArguments().getBoolean(ARG_PARAM_SELECT);
            viewOnly = getArguments().getBoolean(ARG_PARAM_VIEW);
            isCompany = getArguments().getBoolean(ARG_PARAM_IS_COMPANY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer, container, false);
        unbinder = ButterKnife.bind(this, view);
        activity = (MainActivity) getActivity();


        edittextDateBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (edittextDateBirth.hasFocus()) {
                    edittextDateBirth.performClick();
                }
            }
        });
        if (viewOnly) {
            linearLayoutActions.setVisibility(View.GONE);
        }
        if (selectCustomer) {
            switchCustomerType.setChecked(isCompany);
     //       switchCustomerType.setClickable(false);
        }
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if (mCustomer != null) {
            initData();
          //  switchCustomerType.setClickable(false);

        }
        return view;
    }

    private void initData() {
        // radioButtonMiss.setChecked(mCustomer.getCivility().equals(radioButtonMiss.getText().toString()));
        //  radioButtonMister.setChecked(mCustomer.getCivility().equals(radioButtonMister.getText().toString()));
        edittextFirstname.setText(mCustomer.getFirstName());
        edittextAddress.setText(mCustomer.getAddressLine1());
        edittextEmail.setText(mCustomer.getEmail());
        edittextLastname.setText(mCustomer.getLastName());
        edittextZipcode.setText(mCustomer.getZipCode());
        editTextPhone.setText(mCustomer.getOfficePhoneNumber());
        edittextDateBirth.setText(String.format("%02d", mCustomer.getBirthDateDay()) + "/" + String.format("%02d", mCustomer.getBirthDateMonth()) + "/" + String.format("%02d", mCustomer.getBirthDateYear()));
        editTextCity.setText(mCustomer.getCity());
        dayOfMonth = mCustomer.getBirthDateDay();
        monthOfYear = mCustomer.getBirthDateMonth();
        year = mCustomer.getBirthDateYear();
        switchCustomerType.setChecked(mCustomer.getIsCompany());
    }

    @Override
    public void onFragResume() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.edittext_date_birth, R.id.linearLayout_reset, R.id.linearlayout_submit_client})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edittext_date_birth:
                edittextDateBirth.clearFocus();
                showDatePicker();
                break;
            case R.id.linearLayout_reset:
                activity.onBackPressed();
                break;
            case R.id.linearlayout_submit_client:
                Utils.hideSoftKeyboard(activity);

                if (checkFields()) {
                    activity.showProgressBar(true);
                    if (mCustomer == null) {
                        saveCustomer(collectInfos());
                    } else {
                        updateCustomer(collectInfos());
                    }
                } else {
                    Utils.showAlert(message, activity, null, false, true);
                }
                break;
        }
    }


    private void saveCustomer(final Customer customer) {
        activity.showProgressBar(true);
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                activity.getLafargeDatabase().customerDao().insertCustomer(customer);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        new ActionRequestsManager(activity).createNewActionRequest(customer, "Insert", Constants.ACTION_REQUEST_CUSTOMER);
                        EventBus.getDefault().post(new CustomerSelectedEvent(customer));
                        Utils.showAlert(getString(R.string.info_message_customer_add_success), activity, new Runnable() {
                            @Override
                            public void run() {
                                activity.showProgressBar(false);
                                clearFields();
                                if(selectCustomer){
                                    activity.onBackPressed();
                                    activity.onBackPressed();
                                }else {
                                    activity.addFragment(new CustomerDirectoryFragment(),"",false);
                                }

                            }
                        }, false, true);
                          }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ERROR ROOM add customer", e.getMessage());
                    }
                });
    }


    private void updateCustomer(final Customer customer) {
        activity.showProgressBar(true);
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                activity.getLafargeDatabase().customerDao().updateCustomer(customer);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        new ActionRequestsManager(activity).createNewActionRequest(customer, "Update", Constants.ACTION_REQUEST_CUSTOMER);
                        EventBus.getDefault().post(new UpdateCustomerInfoEvent(customer));
                        Utils.showAlert(getString(R.string.info_message_customer_update_success), activity, new Runnable() {
                            @Override
                            public void run() {
                                activity.showProgressBar(false);
                                activity.addFragment(new CustomerDirectoryFragment(),"",false);
                                activity.onBackPressed();
                                activity.onBackPressed();
                            }
                        }, false, true);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ERROR ROOM editCustomer", e.getMessage());
                    }
                });
    }



    private Customer collectInfos() {
        if (mCustomer == null) {
            mCustomer = new Customer();
        }
        mCustomer.setAddressLine1(edittextAddress.getText().toString());
        mCustomer.setFirstName(edittextFirstname.getText().toString());
        mCustomer.setLastName(edittextLastname.getText().toString());
        mCustomer.setOfficePhoneNumber(editTextPhone.getText().toString());
        mCustomer.setEmail(edittextEmail.getText().toString());
        mCustomer.setCity(editTextCity.getText().toString());
        mCustomer.setZipCode(edittextZipcode.getText().toString());
        mCustomer.setBirthDateDay(dayOfMonth);
        mCustomer.setBirthDateMonth(monthOfYear);
        mCustomer.setBirthDateYear(year);
        mCustomer.setIsCompany(switchCustomerType.isChecked());
        //mCustomer.setCustomerId("");
        //mCustomer.setReference(UUID.randomUUID().toString());
        return mCustomer;
    }

    private void showDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setAccentColor(getResources().getColor(R.color.colorPrimary));
        if (isAdded()) {
            dpd.show(activity.getFragmentManager(), "");
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = String.format("%02d", dayOfMonth) + "/" + String.format("%02d", monthOfYear + 1) + "/" + year;
        edittextDateBirth.setText(date);
        this.dayOfMonth = dayOfMonth;
        this.monthOfYear = monthOfYear + 1;
        this.year = year;



    }

    private boolean checkFields() {
        if (TextUtils.isEmpty(edittextLastname.getText().toString())) {
            message = getString(R.string.error_message_set_lastname);
            return false;
        }
        if (TextUtils.isEmpty(edittextFirstname.getText().toString())) {
            message = getString(R.string.error_message_set_firstname);
            return false;
        }
        if (!TextUtils.isEmpty(edittextEmail.getText().toString()) && !Utils.isEmailValid(edittextEmail.getText().toString())) {
            message = getString(R.string.error_message_set_valid_email);
            return false;
        }

        /*if (TextUtils.isEmpty(edittextEmail.getText().toString())) {
            message = getString(R.string.error_message_set_email);
            return false;
        }

        if (TextUtils.isEmpty(editTextPhone.getText().toString())) {
            message = getString(R.string.error_message_set_phone);
            return false;
        }
        if (TextUtils.isEmpty(edittextDateBirth.getText().toString())) {
            message = getString(R.string.error_message_set_birth);
            return false;
        }
        if (TextUtils.isEmpty(edittextAddress.getText().toString())) {
            message = getString(R.string.error_message_set_address);
            return false;
        }
        if (TextUtils.isEmpty(editTextCity.getText().toString())) {
            message = getString(R.string.error_message_set_city);
            return false;
        }
        if (TextUtils.isEmpty(edittextZipcode.getText().toString())) {
            message = getString(R.string.error_message_set_zipcode);
            return false;
        }*/


        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            chosenDate= sdf.parse(year+"-"+monthOfYear+"-"+dayOfMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(chosenDate!=null && chosenDate.after(date)){
            message = getString(R.string.error_message_wrong_birth);
            return false;
        }



        return true;
    }



    private void clearFields() {
        edittextFirstname.getText().clear();
        edittextAddress.getText().clear();
        edittextEmail.getText().clear();
        edittextLastname.getText().clear();
        edittextZipcode.getText().clear();
        editTextPhone.getText().clear();
        edittextDateBirth.getText().clear();
        editTextCity.getText().clear();
        edittextComment.getText().clear();
        switchCustomerType.setChecked(false);
    }
}
