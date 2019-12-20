package io.mintit.lafarge.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mintit.lafarge.R;
import io.mintit.lafarge.model.Supplier;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.utils.DebugLog;

/**
 * Created by mint on 17/04/17.
 */

public class SuppliersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    ArrayList<Supplier> suppliersList = new ArrayList<>();
    Context context;
    OnItemClickListener onItemClickListener;
    private boolean history = false;
    private MainActivity activity;
    private boolean selectCustomer;
    private Supplier selectedSupplier;
    private ItemFilter mFilter = new ItemFilter();

    public SuppliersAdapter(ArrayList<Supplier> sellersList, Context context, MainActivity activity) {
        this.suppliersList.addAll(sellersList);
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
        final Supplier seller = suppliersList.get(position);
        mViewHolder.textviewName.setText(seller.getLibelle());
        mViewHolder.linearLayoutContainer.setBackgroundColor(seller.isSelected() ? context.getResources().getColor(R.color.colorPrimaryTransparent) : context.getResources().getColor(R.color.white));
        mViewHolder.viewSeparator.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
        mViewHolder.textviewIdentifiant.setText(seller.getLibelle());
        mViewHolder.textviewMission.setVisibility(View.GONE);
        mViewHolder.linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedSupplier = seller;
                suppliersList.get(position).setSelected(true);
                for (int i = 0; i < suppliersList.size(); i++) {
                    if (i != position) {
                        suppliersList.get(i).setSelected(false);
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

    public void updateItem(int i, Supplier seller) {
        suppliersList.remove(i);
        suppliersList.add(i, seller);
        notifyItemChanged(i);
    }

    public void clear() {
        suppliersList.clear();
        notifyDataSetChanged();
    }

    public void insertItems(ArrayList<Supplier> listCustomerTemp) {
        suppliersList.addAll(listCustomerTemp);
        notifyItemRangeInserted(suppliersList.size() - listCustomerTemp.size(), listCustomerTemp.size());
    }

    public Supplier getSelectedSupplier() {
        return selectedSupplier;
    }

    @Override
    public int getItemCount() {
        return suppliersList.size();
    }

    public void addAll(ArrayList<Supplier> suppliers) {
        suppliersList.clear();
        suppliersList.addAll(suppliers);
        notifyDataSetChanged();
    }

    public void removeItem(int pos) {
        suppliersList.remove(pos);
        notifyItemRemoved(pos);
    }

    public void reset(ArrayList<Supplier> suppliers) {
        this.suppliersList.clear();
        this.suppliersList.addAll(suppliers);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public interface OnItemClickListener {
        public void onItemClick(Supplier customer);
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final ArrayList<Supplier> list = new ArrayList<>();
            list.addAll(suppliersList);

            int count = list.size();
            final ArrayList<Supplier> nlist = new ArrayList<Supplier>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                if (list.get(i).getLibelle().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            suppliersList.clear();
            if (results.values != null) {
                DebugLog.d("" + (results.values != null));
                suppliersList.addAll((ArrayList<Supplier>) results.values);
                DebugLog.d("" + (suppliersList.size()));
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
