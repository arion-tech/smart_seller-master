package io.mintit.lafarge.ui.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.mintit.lafarge.R;
import io.mintit.lafarge.adapter.OperationsAdapter;
import io.mintit.lafarge.global.Constants;
import io.mintit.lafarge.model.ActionRequest;
import io.mintit.lafarge.services.ActionRequestsManager;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class OperationsFragment extends BaseFragment {
    @BindView(R.id.recyclerView_operations)
    RecyclerView recyclerViewOperations;
    @BindView(R.id.textview_empty_operations)
    TextView textviewEmptyOperations;
    Unbinder unbinder;
    private MainActivity activity;
    private ArrayList<ActionRequest> listOperations = new ArrayList<>();
    private boolean end_reached;
    private boolean loading;
    private OperationsAdapter operationsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_actions, container, false);
        unbinder = ButterKnife.bind(this, view);
        activity = (MainActivity) getActivity();
        initViews();
        loadOperations();
        return view;
    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        operationsAdapter = new OperationsAdapter(listOperations, activity, activity);
        recyclerViewOperations.setLayoutManager(layoutManager);
        recyclerViewOperations.setAdapter(operationsAdapter);
    }

    @Override
    public void onFragResume() {
    }

    @OnClick(R.id.resend_operations)
    void resendOperations(){
        boolean isAllsended = true;
        if(listOperations.size()>0){
            for(int i=0; i<listOperations.size(); i++){
                if(!listOperations.get(i).isTransmited()){
                    isAllsended = false;
                    if(Utils.isNetworkAvailable(getActivity())) {
                        new ActionRequestsManager(activity).getActionRequest(listOperations.get(i).getReference());
                    }else {
                        Toast.makeText(getActivity(),R.string.info_message_error_network,Toast.LENGTH_SHORT).show();
                    }
                }
            }
            if(isAllsended){
                Toast.makeText(getActivity(),"all operations are successfully sent",Toast.LENGTH_SHORT).show();
            }else {
                operationsAdapter.clear();
                loadOperations();
            }
        }else {
            Toast.makeText(getActivity(),"list operations is empty",Toast.LENGTH_SHORT).show();
        }
    }



    @SuppressLint("CheckResult")
    private void loadOperations(){
        activity.getLafargeDatabase().actionRequestDao().getAllActionRequests()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ActionRequest>>() {
                    @Override
                    public void accept(List<ActionRequest> actionRequests) {
                        if(actionRequests.size()>0){
                            listOperations.addAll(actionRequests);
                            activity.showProgressBar(false);
                            operationsAdapter.notifyItemRangeInserted(operationsAdapter.getItemCount(), actionRequests.size());
                            end_reached = actionRequests.size() < 10;
                            loading = false;
                        }else {
                            activity.showProgressBar(false);
                            textviewEmptyOperations.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
