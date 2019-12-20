package io.mintit.lafarge.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mintit.lafarge.R;
import io.mintit.lafarge.global.Constants;
import io.mintit.lafarge.model.ActionRequest;
import io.mintit.lafarge.ui.activity.MainActivity;

/**
 * Created by mint on 17/04/17.
 */

public class OperationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;

    private MainActivity activity;
    ArrayList<ActionRequest> actionRequests = new ArrayList<>();

    public OperationsAdapter(ArrayList<ActionRequest> actionRequests, Context context, MainActivity activity) {
        this.actionRequests = actionRequests;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_operation, parent, false);
        return new ViewHolder(v);
    }

    public void clear(){
        actionRequests.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder mViewHolder = (ViewHolder) holder;
        final ActionRequest actionRequest = actionRequests.get(position);
        mViewHolder.radioButtonStatus.setChecked(!actionRequest.isTransmited());
        mViewHolder.textViewActionType.setText(actionRequest.getActionType());
        mViewHolder.textviewOperation.setText(actionRequest.getObjectType());
        mViewHolder.textviewDate.setText(actionRequest.getCreationDate());
        switch (actionRequest.getObjectType()) {
            case Constants.ACTION_REQUEST_SALES_DOCUMENT:
                mViewHolder.imageviewType.setImageDrawable(context.getResources().getDrawable(R.drawable.cart_icon));
                break;
            case Constants.ACTION_REQUEST_WHISHLIST:
                mViewHolder.imageviewType.setImageDrawable(context.getResources().getDrawable(R.drawable.inventory_icon));
                break;
            case Constants.ACTION_REQUEST_CUSTOMER:
                mViewHolder.imageviewType.setImageDrawable(context.getResources().getDrawable(R.drawable.customer_icon));
                break;
            /*case Constants.ACTION_REQUEST_PURCHASE:
                mViewHolder.imageviewType.setImageDrawable(context.getResources().getDrawable(R.drawable.purchase));
                break;*/
            default:
                break;
        }

        mViewHolder.linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actionRequests.get(position).isTransmited()){
                    Toast.makeText(context,"transmitted successfully",Toast.LENGTH_SHORT).show();
                }else {
                    String errorMsg = actionRequests.get(position).getErrorMessage();
                    if(errorMsg != null && !errorMsg.isEmpty()){
                        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "no message provided", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return actionRequests.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageview_type)
        ImageView imageviewType;
        @BindView(R.id.textview_operation)
        TextView textviewOperation;
        @BindView(R.id.textview_date)
        TextView textviewDate;
        @BindView(R.id.textView_reference)
        TextView textViewReference;
        @BindView(R.id.textView_action_type)
        TextView textViewActionType;
        @BindView(R.id.radioButton_status)
        RadioButton radioButtonStatus;
        @BindView(R.id.linearLayout_container)
        LinearLayout linearLayoutContainer;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
