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
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mintit.lafarge.R;
import io.mintit.lafarge.model.Cart;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.utils.DebugLog;
import io.mintit.lafarge.utils.Utils;

/**
 * Created by mint on 17/04/17.
 */

public class SalesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Cart> cartsList = new ArrayList<>();
    Context context;
    OnItemClickListener onItemClickListener;

    private MainActivity activity;
    private boolean selectCustomer;
    private boolean fromProfile;

    public SalesAdapter(ArrayList<Cart> cartsList, Context context, MainActivity activity) {
        this.cartsList.addAll(cartsList);
        this.context = context;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        if (isFromProfile()) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_profile, parent, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder mViewHolder = (ViewHolder) holder;
        final Cart cart = cartsList.get(position);
        // mViewHolder.textviewDate.setText(Utils.getDate(Long.parseLong(cart.getLastModification())));
        mViewHolder.textViewTotal.setText(cart.getTotal() + " " + cart.getCurrencyId());
        mViewHolder.textViewSeller.setText(cart.getSellerLibelle());
        mViewHolder.textviewDate.setText(cart.getDate());
        mViewHolder.textviewTime.setText(cart.getTime());
        mViewHolder.textviewOrder.setText(cart.getId());

        long diff = Long.parseLong(Utils.generateTimestamp()) - Long.parseLong(cart.getLastModification());
        DebugLog.d("Days -->" + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        mViewHolder.radioButtonStatus.setChecked(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 4);
        mViewHolder.linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(cart);
                }
            }
        });
        mViewHolder.radioButtonStatus.setVisibility((isFromProfile() || cart.getClosed()) ? View.GONE : View.VISIBLE);
        mViewHolder.relativeLayoutDeleteCart.setVisibility((isFromProfile() || cart.getClosed()) ? View.GONE : View.VISIBLE);
        String name = "";
        String lastName = "";
        if(cart.getCustomerFirstName()!=null)
            name = cart.getCustomerFirstName();
        if(cart.getCustomerLastName()!=null)
            lastName = cart.getCustomerLastName();
        mViewHolder.textviewCustomerName.setText(name + " " + lastName  );



        mViewHolder.relativeLayoutDeleteCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemDelete(cart);
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public boolean isFromProfile() {
        return fromProfile;
    }

    public void setFromProfile(boolean fromProfile) {
        this.fromProfile = fromProfile;
    }

    public void updateItem(int i, Cart cart) {
        cartsList.remove(i);
        cartsList.add(i, cart);
        notifyItemChanged(i);
    }

    public void addItem(Cart cart) {
        cartsList.add(cart);
        notifyItemInserted(getItemCount());
    }

    @Override
    public int getItemCount() {
        return cartsList.size();
    }

    public void addAll(ArrayList<Cart> carts) {
        if(carts != null) {
            cartsList.clear();
            cartsList.addAll(carts);
            notifyDataSetChanged();
        }
    }

    public void removeItem(Cart pos) {
        for (int i = 0; i < cartsList.size(); i++) {
            if (cartsList.get(i).getId().equals(pos.getId())) {
                cartsList.remove(i);
                notifyItemRemoved(i);
            }
        }

    }

    public void reset(ArrayList<Cart> customers) {
        this.cartsList.clear();
        this.cartsList.addAll(customers);
        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        public void onItemClick(Cart cart);

        void onItemDelete(Cart cart);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_customer_name)
        TextView textviewCustomerName;
        @BindView(R.id.textview_order)
        TextView textviewOrder;
        @BindView(R.id.textview_date)
        TextView textviewDate;
        @BindView(R.id.textview_time)
        TextView textviewTime;
        @BindView(R.id.textView_seller)
        TextView textViewSeller;
        @BindView(R.id.textView_Total)
        TextView textViewTotal;
        @BindView(R.id.radioButton_status)
        RadioButton radioButtonStatus;
        @BindView(R.id.relativeLayout_delete_cart)
        RelativeLayout relativeLayoutDeleteCart;
        @BindView(R.id.linearLayout_container)
        LinearLayout linearLayoutContainer;
        @BindView(R.id.view_separator)
        View viewSeparator;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
