package io.mintit.lafarge.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mintit.lafarge.R;
import io.mintit.lafarge.model.Customer;
import io.mintit.lafarge.model.Purchase;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.utils.Utils;

/**
 * Created by mint on 17/04/17.
 */

public class PurchaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Purchase> inventoryList = new ArrayList<>();
    Context context;
    OnItemClickListener onItemClickListener;
    private MainActivity activity;

    public PurchaseAdapter(ArrayList<Purchase> inventoryList, Context context, MainActivity activity) {
        this.inventoryList.addAll(inventoryList);
        this.context = context;
        this.activity = activity;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder mViewHolder = (ViewHolder) holder;
        final Purchase purchase = inventoryList.get(position);
       // mViewHolder.textviewDate.setText(Utils.getDate(Long.parseLong(purchase.getId())));
        mViewHolder.textviewDescription.setText(purchase.getReference());
        mViewHolder.textviewQuantity.setText(purchase.getTotalQte() + "");
        mViewHolder.textViewSupplier.setText(purchase.getSupplier());
        mViewHolder.radioButtonStatus.setChecked(!purchase.isValidated());
        mViewHolder.textviewStatus.setText(purchase.isValidated() ? context.getResources().getString(R.string.valid) : context.getResources().getString(R.string.pending));
        mViewHolder.relativeLyaoutShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(purchase);
                }
            }
        });


    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public void updateItem(int i, Purchase inventory) {
        inventoryList.remove(i);
        inventoryList.add(i, inventory);
        notifyItemChanged(i);
    }

    public void add(Purchase purchase) {
        inventoryList.add(purchase);
        notifyItemInserted(inventoryList.size() - 1);
    }

    public void reset(ArrayList<Purchase> purchases) {
        inventoryList.clear();
        inventoryList.addAll(purchases);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Purchase> inventoryList) {
        this.inventoryList.clear();
        this.inventoryList.addAll(inventoryList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return inventoryList.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(Purchase inventory);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_description)
        TextView textviewDescription;
        @BindView(R.id.textView_supplier)
        TextView textViewSupplier;
        @BindView(R.id.textview_quantity)
        TextView textviewQuantity;
        @BindView(R.id.textview_date)
        TextView textviewDate;
        @BindView(R.id.radioButton_status)
        RadioButton radioButtonStatus;
        @BindView(R.id.textview_status)
        TextView textviewStatus;
        @BindView(R.id.relativeLyaout_show)
        RelativeLayout relativeLyaoutShow;
        @BindView(R.id.linearlayout_actions)
        LinearLayout linearlayoutActions;



        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
