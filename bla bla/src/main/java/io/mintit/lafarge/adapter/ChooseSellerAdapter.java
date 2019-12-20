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

public class ChooseSellerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    List<Seller> sellersList = new ArrayList<>();
    Context context;
    OnItemClickListener onItemClickListener;
    private ChooseSellerActivity activity;
    private Seller selectedSeller;
    private ItemFilter mFilter = new ItemFilter();

    public ChooseSellerAdapter(List<Seller> sellersList, Context context, ChooseSellerActivity activity) {
        this.sellersList.addAll(sellersList);
        this.context = context;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seller_choose, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder mViewHolder = (ViewHolder) holder;
        final Seller seller = sellersList.get(position);
        mViewHolder.textviewName.setText(seller.getFirstName());
        mViewHolder.linearLayoutContainer.setBackgroundColor(seller.isSelected()
                ? context.getResources().getColor(R.color.colorPrimaryTransparent)
                : context.getResources().getColor(R.color.white));
        sellersList.get(position).setSelected(false);
        mViewHolder.viewSeparator.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
        mViewHolder.textviewIdentifiant.setText(seller.getLastName());
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
            //System.out.println("AAAAAAA__filterString : "+filterString);

            FilterResults results = new FilterResults();

            final List<Seller> list = new ArrayList<>();
            list.addAll(sellersList);
            //System.out.println("AAAAAAA__list count : "+list.size());
            //System.out.println("AAAAAAA__sellersList count : "+sellersList.size());

            int count = list.size();
            final List<Seller> nlist = new ArrayList<>(count);
            //System.out.println("AAAAAAA__nlist count : "+nlist.size());

            //String filterableString;

            for (int i = 0; i < count; i++) {
                //System.out.println("inside FOR list : "+list.get(i).getPrenom().toLowerCase());
                //System.out.println("inside FOR nlist : "+nlist.get(i).getPrenom().toLowerCase());
                if(list.get(i).getLastName() != null && list.get(i).getFirstName() == null){
                    if (list.get(i).getLastName().toLowerCase().contains(filterString)) {
                        nlist.add(list.get(i));
                    }
                } else if (list.get(i).getFirstName() != null && list.get(i).getLastName() == null){
                    if (list.get(i).getFirstName().toLowerCase().contains(filterString) ) {
                        nlist.add(list.get(i));
                    }
                } else {
                    if (list.get(i).getLastName().toLowerCase().contains(filterString) || list.get(i).getFirstName().toLowerCase().contains(filterString) ) {
                        nlist.add(list.get(i));
                    }
                }

            }
            //System.out.println("BBBBBBB__list count : "+list.size());
            //System.out.println("BBBBBBB__nlist count : "+nlist.size());

            results.values = nlist;
            results.count = nlist.size();

            //System.out.println("BBBBBBB__results.values : "+results.values);
            //System.out.println("BBBBBBB__results.count count : "+results.count);

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            System.out.println("COUUUUUUUUNT4 : " + sellersList());
            sellersList.clear();
            System.out.println("COUUUUUUUUUUUUUUUUNT3 : " + results.values);
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

        @BindView(R.id.linearLayout_container)
        LinearLayout linearLayoutContainer;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
