package io.mintit.lafarge.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mintit.lafarge.R;
import io.mintit.lafarge.model.Article;
import io.mintit.lafarge.model.Inventory;
import io.mintit.lafarge.model.InventoryArticle;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.utils.DebugLog;

/**
 * Created by mint on 17/04/17.
 */

public class InventoryProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Inventory inventory;
    ArrayList<InventoryArticle> productsList = new ArrayList<>();
    ArrayList<InventoryArticle> initialProductsList = new ArrayList<>();
    Context context;
    OnItemClickListener onItemClickListener;
    private MainActivity activity;
    private boolean selectProduct;

    public InventoryProductsAdapter(ArrayList<InventoryArticle> productsList, Context context, MainActivity activity, Inventory inventory) {
        this.productsList.addAll(productsList);
        this.initialProductsList.addAll(productsList);
        this.inventory = inventory;
        this.context = context;
        this.activity = activity;
    }

    public void setSelectProduct(boolean selectProduct) {
        this.selectProduct = selectProduct;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_inventory, parent, false);

        return new ViewHolder(v);
    }

    public void addAll(ArrayList<InventoryArticle> products) {
        productsList.clear();
        productsList.addAll(products);
        initialProductsList.clear();
        initialProductsList.addAll(products);
        notifyDataSetChanged();
    }

    public void insertAll(ArrayList<InventoryArticle> products) {
        productsList.addAll(products);
        initialProductsList.addAll(products);
        notifyItemRangeInserted(productsList.size() - products.size(), products.size());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder mViewHolder = (ViewHolder) holder;
        if (productsList.size() > 0) {
            final InventoryArticle product = productsList.get(position);
            mViewHolder.textviewProductName.setText(product.getName());
            mViewHolder.textviewProductCode.setText(product.getEanCode());
            mViewHolder.textviewProductStock.setText(product.getStockTheorique() + "");
            mViewHolder.textviewProductStock.setFocusableInTouchMode(false);
            mViewHolder.textviewProductPhysical.setText(product.getStockPhysique() + "");
            mViewHolder.textviewProductPhysical.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    productsList.get(position).setStockPhysique(mViewHolder.textviewProductPhysical.getText().toString());

                }
            });
            if (inventory.getIsValidated()) {
                mViewHolder.textviewProductPhysical.setFocusableInTouchMode(false);
            }
            DebugLog.d(new Gson().toJson(product));
        }

    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void add(InventoryArticle product) {
        productsList.add(product);
        notifyItemInserted(productsList.size() - 1);
    }


    public void clear() {
        productsList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public void updateItem(InventoryArticle pos) {
        for (int i = 0; i < productsList.size(); i++) {
            if (productsList.get(i).getId().equals(pos.getId())) {
                productsList.remove(i);
                productsList.add(i, pos);
                notifyItemChanged(i);
                break;
            }
        }

    }

    public ArrayList<InventoryArticle> getListProducts() {
        return productsList;
    }


    public interface OnItemClickListener {
        void onItemClick(Article product);

        void onItemAdd(Article product);

        void onItemUpdate(Article product);

        void onItemRemove(Article product);

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_product_name)
        TextView textviewProductName;
        @BindView(R.id.textview_product_code)
        TextView textviewProductCode;
        @BindView(R.id.textview_product_stock)
        EditText textviewProductStock;
        @BindView(R.id.textview_product_physical)
        EditText textviewProductPhysical;
        @BindView(R.id.linearLayout_container)
        LinearLayout linearLayoutContainer;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
