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
import io.mintit.lafarge.model.Reservation;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.utils.DebugLog;
import io.mintit.lafarge.utils.Utils;

/**
 * Created by mint on 17/04/17.
 */

public class ReservationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Reservation> ressList = new ArrayList<>();
    Context context;
    OnItemClickListener onItemClickListener;

    private MainActivity activity;
    private boolean selectCustomer;
    private boolean fromProfile;

    public ReservationsAdapter(ArrayList<Reservation> ressList, Context context, MainActivity activity) {
        this.ressList.addAll(ressList);
        this.context = context;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder mViewHolder = (ViewHolder) holder;
        final Reservation res = ressList.get(position);
        // mViewHolder.textviewDate.setText(Utils.getDate(Long.parseLong(cart.getLastModification())));
        mViewHolder.textViewTotal.setText(res.getTotal() + " " + res.getCurrencyId());
        mViewHolder.textViewSeller.setText(res.getSellerLibelle());
        mViewHolder.textviewDate.setText(res.getDate());
        mViewHolder.textviewTime.setText(res.getTime());
        mViewHolder.textviewReservation.setText(res.getId());

        long diff = Long.parseLong(Utils.generateTimestamp()) - Long.parseLong(res.getLastModification());
        DebugLog.d("Days -->" + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        mViewHolder.radioButtonStatus.setChecked(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 4);
        mViewHolder.linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(res);
                }
            }
        });
        mViewHolder.radioButtonStatus.setVisibility(View.VISIBLE);
        mViewHolder.relativeLayoutDeleteRes.setVisibility(View.VISIBLE);
        String name = "";
        String lastName = "";
        if(res.getCustomerFirstName()!=null)
            name = res.getCustomerFirstName();
        if(res.getCustomerLastName()!=null)
            lastName = res.getCustomerLastName();
        mViewHolder.textviewCustomerName.setText(name + " " + lastName  );


        mViewHolder.relativeLayoutDeleteRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemDelete(res);
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

    public void updateItem(int i, Reservation res) {
        ressList.remove(i);
        ressList.add(i, res);
        notifyItemChanged(i);
    }

    public void addItem(Reservation res) {
        ressList.add(res);
        notifyItemInserted(getItemCount());
    }

    @Override
    public int getItemCount() {
        return ressList.size();
    }

    public void addAll(ArrayList<Reservation> ress) {
        if(ress != null) {
            ressList.clear();
            ressList.addAll(ress);
            notifyDataSetChanged();
        }
    }

    public void removeItem(Reservation pos) {
        for (int i = 0; i < ressList.size(); i++) {
            if (ressList.get(i).getId().equals(pos.getId())) {
                ressList.remove(i);
                notifyItemRemoved(i);
            }
        }

    }

    public void reset(ArrayList<Reservation> customers) {
        this.ressList.clear();
        this.ressList.addAll(customers);
        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        public void onItemClick(Reservation res);

        void onItemDelete(Reservation res);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_customer_name)
        TextView textviewCustomerName;
        @BindView(R.id.textview_reservation)
        TextView textviewReservation;
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
        @BindView(R.id.relativeLayout_delete_res)
        RelativeLayout relativeLayoutDeleteRes;
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
