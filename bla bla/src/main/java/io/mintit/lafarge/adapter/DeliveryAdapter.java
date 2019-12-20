package io.mintit.lafarge.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mintit.lafarge.R;
import io.mintit.lafarge.model.Customer;
import io.mintit.lafarge.model.Purchase;
import io.mintit.lafarge.ui.activity.MainActivity;

/**
 * Created by mint on 17/04/17.
 */

public class DeliveryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Purchase> inventoryList = new ArrayList<>();
    Context context;
    OnItemClickListener onItemClickListener;
    private MainActivity activity;

    public DeliveryAdapter(ArrayList<Purchase> inventoryList, Context context, MainActivity activity) {
        this.inventoryList.addAll(inventoryList);
        this.context = context;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder mViewHolder = (ViewHolder) holder;
        mViewHolder.linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Unser construction", Toast.LENGTH_SHORT).show();
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
        @BindView(R.id.textview_quantity_remaining)
        TextView textviewQuantityRemaining;
        @BindView(R.id.textview_quantity)
        TextView textviewQuantity;
        @BindView(R.id.textview_date)
        TextView textviewDate;
        @BindView(R.id.linearLayout_container)
        LinearLayout linearLayoutContainer;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
