package io.mintit.lafarge.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mintit.lafarge.R;
import io.mintit.lafarge.model.Seller;
import io.mintit.lafarge.ui.activity.ChooseSellerActivity;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.utils.DebugLog;

/**
 * Created by mint on 17/04/17.
 */

public class SellersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    ArrayList<Seller> sellersList = new ArrayList<>();
    Context context;
    OnItemClickListener onItemClickListener;
    private boolean history = false;
    private MainActivity activity;
    private boolean selectCustomer;
    private Seller selectedSeller;
    private ItemFilter mFilter = new ItemFilter();

    public SellersAdapter(List<Seller> sellersList, Context context, MainActivity activity) {
        this.sellersList.addAll(sellersList);
        this.context = context;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seller, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder mViewHolder = (ViewHolder) holder;
        final Seller seller = sellersList.get(position);
        mViewHolder.textviewName.setText(seller.getLastName());
        mViewHolder.linearLayoutContainer.setBackgroundColor(seller.isSelected()
                ? context.getResources().getColor(R.color.colorPrimaryTransparent)
                : context.getResources().getColor(R.color.white));
        sellersList.get(position).setSelected(false);
        mViewHolder.viewSeparator.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
        mViewHolder.textviewIdentifiant.setText(seller.getFirstName());
        mViewHolder.linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedSeller = seller;
                sellersList.get(position).setSelected(true);
                for (int i = 0; i < sellersList.size(); i++) {
                    if (i != position) {
                        sellersList.get(i).setSelected(false);
                    }
                }
                notifyDataSetChanged();
                onItemClickListener.onItemClick(seller);
            }
        });


    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void updateItem(int i, Seller seller) {
        sellersList.remove(i);
        sellersList.add(i, seller);
        notifyItemChanged(i);
    }

    public void clear() {
        sellersList.clear();
        notifyDataSetChanged();
    }

    public void insertItems(ArrayList<Seller> listCustomerTemp) {
        sellersList.addAll(listCustomerTemp);
        notifyItemRangeInserted(sellersList.size() - listCustomerTemp.size(), listCustomerTemp.size());
    }

    public Seller getSelectedSeller() {
        return selectedSeller;
    }

    @Override
    public int getItemCount() {
        return sellersList.size();
    }

    public void addAll(ArrayList<Seller> sellers) {
        sellersList.clear();
        sellersList.addAll(sellers);
        notifyDataSetChanged();
    }

    public void removeItem(int pos) {
        sellersList.remove(pos);
        notifyItemRemoved(pos);
    }

    public void reset(List<Seller> sellers) {
        this.sellersList.clear();
        this.sellersList.addAll(sellers);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public interface OnItemClickListener {
        public void onItemClick(Seller customer);
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final ArrayList<Seller> list = new ArrayList<>();
            list.addAll(sellersList);

            int count = list.size();
            final ArrayList<Seller> nlist = new ArrayList<Seller>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                if (list.get(i).getLastName().toLowerCase().contains(constraint.toString().toLowerCase()) || list.get(i).getFirstName().toLowerCase().contains(constraint.toString().toLowerCase()) || list.get(i).getCommercial().toLowerCase().contains(constraint.toString().toLowerCase())) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            sellersList.clear();
            if (results.values != null) {
                DebugLog.d("" + (results.values != null));
                sellersList.addAll((ArrayList<Seller>) results.values);
                DebugLog.d("" + (sellersList.size()));
            }
            notifyDataSetChanged();
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_name)
        TextView textviewName;
        @BindView(R.id.view_separator)
        View viewSeparator;

        @BindView(R.id.textview_identifiant)
        TextView textviewIdentifiant;
        @BindView(R.id.textview_mission)
        TextView textviewMission;
        @BindView(R.id.linearLayout_container)
        LinearLayout linearLayoutContainer;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
