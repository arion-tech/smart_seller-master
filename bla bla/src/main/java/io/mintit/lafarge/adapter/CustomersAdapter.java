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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mintit.lafarge.R;
import io.mintit.lafarge.model.Customer;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.utils.DebugLog;

/**
 * Created by mint on 17/04/17.
 */

public class CustomersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    ArrayList<Customer> customersList = new ArrayList<>();
    Context context;
    OnItemClickListener onItemClickListener;
    private boolean history = false;
    private MainActivity activity;
    private boolean selectCustomer;
    private ItemFilter mFilter = new ItemFilter();


    public CustomersAdapter(ArrayList<Customer> customersList, Context context, MainActivity activity) {
        this.customersList.addAll(customersList);
        this.context = context;
        this.activity = activity;
    }

    public boolean isSelectCustomer() {
        return selectCustomer;
    }

    public void setSelectCustomer(boolean selectCustomer) {
        this.selectCustomer = selectCustomer;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_client, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder mViewHolder = (ViewHolder) holder;
        final Customer customer = customersList.get(position);
        mViewHolder.textviewCustomerName.setText(customer.getFirstName() + " " + customer.getLastName());
        mViewHolder.textviewAddress.setText(customer.getAddressLine1());
        mViewHolder.textviewEmail.setText(customer.getEmail());
        mViewHolder.textviewPhone.setText(customer.getOfficePhoneNumber());
        mViewHolder.linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(customer);
            }
        });
        if (selectCustomer) {
            mViewHolder.relativeLayoutAddToCart.setVisibility(View.VISIBLE);
            mViewHolder.relativeLayoutAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemSelected(customer);
                }
            });
        }
        if (customer.isSelected()) {
            mViewHolder.relativeLayoutAddToCart.setVisibility(View.GONE);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void updateItem(int i, Customer customer) {
        customersList.remove(i);
        customersList.add(i, customer);
        notifyItemChanged(i);
    }

    public void clear() {
        customersList.clear();
        notifyDataSetChanged();
    }

    public void insertItems(ArrayList<Customer> listCustomerTemp) {
        customersList.addAll(listCustomerTemp);
        notifyItemRangeInserted(customersList.size() - listCustomerTemp.size(), listCustomerTemp.size());
    }

    @Override
    public int getItemCount() {
        return customersList.size();
    }

    public void addAll(ArrayList<Customer> customers) {
        customersList.clear();
        customersList.addAll(customers);
        notifyDataSetChanged();
    }

    public void removeItem(int pos) {
        customersList.remove(pos);
        notifyItemRemoved(pos);
    }

    public void reset(ArrayList<Customer> customers) {
        this.customersList.clear();
        this.customersList.addAll(customers);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public interface OnItemClickListener {
        public void onItemClick(Customer customer);

        void onItemSelected(Customer customer);
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final ArrayList<Customer> list = new ArrayList<>();
            list.addAll(customersList);

            int count = list.size();
            final ArrayList<Customer> nlist = new ArrayList<Customer>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                if (list.get(i).getFirstName().toLowerCase().contains(constraint.toString().toLowerCase()) || list.get(i).getFirstName().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            customersList.clear();
            if (results.values != null) {
                DebugLog.d("" + (results.values != null));
                customersList.addAll((ArrayList<Customer>) results.values);
                DebugLog.d("" + (customersList.size()));
            }
            notifyDataSetChanged();
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_customer_name)
        TextView textviewCustomerName;
        @BindView(R.id.textview_address)
        TextView textviewAddress;
        @BindView(R.id.textview_phone)
        TextView textviewPhone;
        @BindView(R.id.textview_email)
        TextView textviewEmail;

        @BindView(R.id.linearLayout_container)
        LinearLayout linearLayoutContainer;

        @BindView(R.id.relativeLayout_add_to_cart)
        RelativeLayout relativeLayoutAddToCart;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
