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
import io.mintit.lafarge.model.Inventory;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.utils.Utils;

/**
 * Created by mint on 17/04/17.
 */

public class InventoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Inventory> inventoryList = new ArrayList<>();
    Context context;
    OnItemClickListener onItemClickListener;
    private MainActivity activity;

    public InventoryAdapter(ArrayList<Inventory> inventoryList, Context context, MainActivity activity) {
        this.inventoryList.addAll(inventoryList);
        this.context = context;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder mViewHolder = (ViewHolder) holder;
        final Inventory inventory = inventoryList.get(position);
        mViewHolder.textviewDate.setText(Utils.getDate(inventory.getDateCreation()));
        mViewHolder.textviewDateLast.setText(Utils.getDate(inventory.getDateModif()));
        mViewHolder.textviewDescription.setText(inventory.getLibelle());
        mViewHolder.radioButtonStatus.setChecked(!inventory.getIsValidated());
        mViewHolder.textviewStatus.setText(inventory.getIsValidated() ? context.getResources().getString(R.string.valid) : context.getResources().getString(R.string.pending));
        mViewHolder.relativeLyaoutUpdate.setVisibility(inventory.getIsValidated() ? View.GONE : View.VISIBLE);
        mViewHolder.relativeLyaoutShow.setVisibility(!inventory.getIsValidated() ? View.GONE : View.VISIBLE);
        mViewHolder.relativeLyaoutShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(inventory);
                }
            }
        });

        mViewHolder.relativeLyaoutUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(inventory);
                }
            }
        });

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public void updateItem(int i, Inventory inventory) {
        inventoryList.remove(i);
        inventoryList.add(i, inventory);
        notifyItemChanged(i);
    }

    @Override
    public int getItemCount() {
        return inventoryList.size();
    }

    public void addAll(ArrayList<Inventory> inventoryList) {
        this.inventoryList.clear();
        this.inventoryList.addAll(inventoryList);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
         void onItemClick(Inventory inventory);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_description)
        TextView textviewDescription;
        @BindView(R.id.textview_date)
        TextView textviewDate;
        @BindView(R.id.textview_date_last)
        TextView textviewDateLast;
        @BindView(R.id.radioButton_status)
        RadioButton radioButtonStatus;
        @BindView(R.id.textview_status)
        TextView textviewStatus;
        @BindView(R.id.textView_modify)
        TextView textViewModify;
        @BindView(R.id.relativeLyaout_update)
        RelativeLayout relativeLyaoutUpdate;
        @BindView(R.id.relativeLyaout_show)
        RelativeLayout relativeLyaoutShow;
        @BindView(R.id.linearlayout_actions)
        LinearLayout linearlayoutActions;
        @BindView(R.id.linearLayout_container)
        LinearLayout linearLayoutContainer;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
