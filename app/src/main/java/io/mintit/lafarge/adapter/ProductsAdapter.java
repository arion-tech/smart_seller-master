package io.mintit.lafarge.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mintit.lafarge.R;
import io.mintit.lafarge.model.Product;
import io.mintit.lafarge.model.CategoryByArticle;
import io.mintit.lafarge.model.Stock;
import io.mintit.lafarge.model.Tarif;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.ui.widget.CustomNumberPicker;
import io.mintit.lafarge.utils.DebugLog;
import io.mintit.lafarge.utils.Prefs;
import io.mintit.lafarge.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mint on 17/04/17.
 */

public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private final boolean associateToCart;
    public ArrayList<Product> productsList = new ArrayList<>();
    ArrayList<Product> initialProductsList = new ArrayList<>();
    Context context;
    OnItemClickListener onItemClickListener;
    //private boolean history = false;
    private MainActivity activity;
    private boolean detailPrice = false;
    private boolean cartList;
    private boolean selectProduct;
    private ItemFilter mFilter = new ItemFilter();
    ArrayList<Product> listProductsTemp = new ArrayList<>();
    private int cartQte = 1;
    private boolean isClosed;



    public void setProfileList(boolean profileList) {
        this.profileList = profileList;
    }

    private boolean profileList = false;

    public ProductsAdapter(ArrayList<Product> productsList, Context context, MainActivity activity, boolean associateToCart, boolean isClosed) {
        this.productsList.addAll(productsList);
        this.initialProductsList.addAll(productsList);
        this.context = context;
        this.activity = activity;
        this.associateToCart = associateToCart;
        this.isClosed = isClosed;
    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        if (cartList) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_cart, parent, false);
        }
        if (profileList) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_profile, parent, false);
        }
        return new ViewHolder(v);
    }

    public void addAll(ArrayList<Product> products) {
        if(products != null) {
            productsList.clear();
            productsList.addAll(products);
            initialProductsList.clear();
            initialProductsList.addAll(products);
            notifyDataSetChanged();
        }
    }

    public void insertAll(ArrayList<Product> products) {
        productsList.addAll(products);
        initialProductsList.addAll(products);
        notifyItemRangeInserted(productsList.size() - products.size(), products.size());
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder mViewHolder = (ViewHolder) holder;
        if (productsList.size() > 0) {
            final Product product = productsList.get(position);
            mViewHolder.textviewProductName.setText(product.getName());
            mViewHolder.textviewProductRef.setText(product.getProductCode());
            mViewHolder.editTextProductStock.setText(String.valueOf(product.getQty()));
            //DebugLog.d(new Gson().toJson(product));
            //mViewHolder.textviewProductPrice.setText(product.getTarifPrice() + " " + activity.getEtablissement().getCurrencyId());
            mViewHolder.textviewProductPrice.setText(product.getPrice() + "");
            if (profileList) {
                mViewHolder.textview_productCode.setText(product.getDate());
            }

            if (isCartList()) {
                mViewHolder.linearlayoutActions.setVisibility(View.VISIBLE);
                mViewHolder.relativeLyaoutRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemRemove(product);
                        }
                    }
                });

                if (isClosed) {
                    mViewHolder.relativeLayoutAddToCart.setVisibility(View.GONE);
                    mViewHolder.linearlayoutActions.setVisibility(View.GONE);
                }

                mViewHolder.editTextProductStock.setFocusableInTouchMode(false);
                mViewHolder.relativeLyaoutUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onItemClickListener != null) {
                            boolean isFocusable = mViewHolder.editTextProductStock.isFocusableInTouchMode();
                            if (!isFocusable) {
                                mViewHolder.editTextProductStock.setFocusableInTouchMode(true);
                                mViewHolder.textViewModify.setText(R.string.valider);
                                mViewHolder.editTextProductStock.requestFocusFromTouch();
                                mViewHolder.editTextProductStock.setSelection(mViewHolder.editTextProductStock.getText().length());
                                Utils.showKeyboard(mViewHolder.editTextProductStock, activity);
                            } else {
                                if (validCount(mViewHolder.editTextProductStock.getText().toString(), product.getStock())) {
                                    mViewHolder.editTextProductStock.setFocusableInTouchMode(false);
                                    mViewHolder.editTextProductStock.clearFocus();
                                    Utils.hideSoftKeyboard(activity);
                                    mViewHolder.editTextProductStock.clearFocus();
                                    product.setQty(Integer.valueOf(mViewHolder.editTextProductStock.getText().toString()));
                                    setCartQte(Integer.valueOf(mViewHolder.editTextProductStock.getText().toString()));
                                    onItemClickListener.onItemUpdate(product);
                                    mViewHolder.textViewModify.setText(R.string.modifier);
                                }
                            }
                        }
                    }
                });
            }
            // mViewHolder.textviewProductPrice.setVisibility(selectProduct ? View.GONE : View.VISIBLE);
            if (isSelectProduct()) {
                mViewHolder.relativeLayoutAddToCart.setVisibility(isClosed ? View.GONE : View.VISIBLE);
                mViewHolder.relativeLayoutAddToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemAdd(product, 1, false);
                            // mViewHolder.relativeLayoutAddToCart.setVisibility(View.GONE);
                        }
                    }
                });
            }

            if (!isSelectProduct()) {
                mViewHolder.relativeLayoutAddToCart.setVisibility(View.GONE);
            }else{
                mViewHolder.relativeLayoutAddToCart.setVisibility(View.VISIBLE);
            }

            if (cartList) {
                mViewHolder.textview_productCode.setVisibility(View.GONE);
                mViewHolder.textviewProductRef.setVisibility(View.GONE);
                mViewHolder.editTextProductStock.setText(String.valueOf(product.getQty()));
            }

            mViewHolder.linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isCartList() && !isClosed)
                    showProductDetailsDialog(product, associateToCart);
                }
            });
        }
    }

    public int getCartQte() {
        return cartQte;
    }

    public void setCartQte(int cartQte) {
        this.cartQte = cartQte;
    }

    private void showProductDetailsDialog(final Product product, boolean associateToCart) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        dialog.setContentView(R.layout.dialog_produuct_detail);
        ImageView imageviewClose = (ImageView) dialog.findViewById(R.id.imageview_close);
        ImageView imageviewProduct = (ImageView) dialog.findViewById(R.id.product_image);
        ImageView imageviewColor = (ImageView) dialog.findViewById(R.id.color_Article);
        TextView textviewPproductName = (TextView) dialog.findViewById(R.id.textview_product_name);
        TextView textviewProductRef = (TextView) dialog.findViewById(R.id.textview_product_ref);
        TextView textviewProductStock = (TextView) dialog.findViewById(R.id.textview_product_stock);
        TextView textviewProductPrice = (TextView) dialog.findViewById(R.id.textview_product_price);
        TextView textviewDescription = (TextView) dialog.findViewById(R.id.textview_description);

        TextView textviewtaille = (TextView) dialog.findViewById(R.id.taille_article);
        final CustomNumberPicker numberPicker = (CustomNumberPicker) dialog.findViewById(R.id.numberPicker);
        LinearLayout linearlayoutAssociateToCart = (LinearLayout) dialog.findViewById(R.id.linearlayout_associate_to_cart);
        LinearLayout linearlayoutAssociateToCartContainer = (LinearLayout) dialog.findViewById(R.id.linearlayout_associate_to_cart_container);
        linearlayoutAssociateToCartContainer.setVisibility(associateToCart ? View.VISIBLE : View.GONE);
        textviewPproductName.setText(product.getName());
        textviewProductRef.setText(" " + product.getProductCode());
        textviewProductStock.setText(" " + product.getQty());
        textviewProductPrice.setText(context.getResources().getString(R.string.prix) + " " + product.getPrice() + " " + activity.getEtablissement().getCurrencyId());
        textviewDescription.setText(product.getDescription());
        textviewtaille.setText(product.getSizeCode());
        Picasso.get().load("http://197.13.7.115:5016/botte.jpg").into(imageviewProduct);

        linearlayoutAssociateToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null && !TextUtils.isEmpty(numberPicker.getPickedNumber())) {
                    try {
                        product.setQty(Integer.valueOf(numberPicker.getPickedNumber()));
                        onItemClickListener.onItemAdd(product, Integer.parseInt(numberPicker.getPickedNumber()),true);
                        DebugLog.d(String.valueOf(product.getQty()));
                        dialog.dismiss();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        numberPicker.setMax(product.getStock());

        imageviewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    private boolean validCount(String qte, int stock) {

        if (TextUtils.isEmpty(qte)) {
            Toast.makeText(context, R.string.error_message_set_quantity, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (qte.length() < 18 && Long.parseLong(qte) == 0) {
            Toast.makeText(context, R.string.error_message_set_quantity, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (qte.length() > 18 || Long.parseLong(qte) > stock) {
            Toast.makeText(context, R.string.error_message_invalid_stock, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }



    public void add(Product product) {
        productsList.add(product);
        notifyItemInserted(productsList.size() - 1);
    }

    public void filterByCategory(final CategoryByArticle idCategories, final String tarif) {
        productsList.clear();
        initialProductsList.clear();
        if (idCategories.getIdArticles() != null) {
            loadStock(idCategories.getIdArticles(), tarif);
            initialProductsList.addAll(productsList);
            activity.showProgressBar(false);
        }
    }


    @SuppressLint("CheckResult")
    public void loadStock(final List<Integer> idArticlesList, final String tarif) {
        activity.getLafargeDatabase().stockDao().getAllStock()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Stock>>() {
                    @Override
                    public void accept(List<Stock> stocks) {
                        ArrayList<String> stockCodeArticle = new ArrayList<>();
                        if(stocks.size()>0) {
                            for (int i = 0; i < stocks.size(); i++) {
                                stockCodeArticle.add(stocks.get(i).getCodeArticle());
                            }
                        }
                        getTarification(idArticlesList, tarif, stocks, stockCodeArticle);

                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getTarification(final List<Integer> idArticlesList, final String tarif, final List<Stock> stocks, final ArrayList<String> stockCodeArticle) {
        activity.getLafargeDatabase().tarifDao().getAllTarifs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Tarif>>() {
                    @Override
                    public void accept(List<Tarif> tarifs) {
                        ArrayList<String> tarifsCodeArticle = new ArrayList<>();
                        if (tarifs.size() > 0){
                            for(int i=0; i<tarifs.size(); i++){
                                tarifsCodeArticle.add(tarifs.get(i).getCodeArticle());
                            }
                        }
                       getProductFromList(idArticlesList,tarif, stocks, stockCodeArticle, tarifs, tarifsCodeArticle);

                    }
                });
    }


    @SuppressLint("CheckResult")
    private void getProductFromList(final List<Integer> idArticlesList, final String tarif,final List<Stock> stocks, final ArrayList<String> stockCodeArticle,
                                    final List<Tarif> tarifs, final ArrayList<String> tarifsCodeArticle) {
        activity.showProgressBar(true);
        activity.getLafargeDatabase().articleDao().getArticleByStock()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Product>>() {
                    @Override
                    public void accept(List<Product> products) {
                        if (products.size() > 0) {
                            for (int i = 0; i < products.size(); i++) {
                                if (idArticlesList.contains(products.get(i).getId())) {
                                    if (stockCodeArticle.contains(products.get(i).getProductCode())) {
                                        products.get(i).setStock(stocks.get(stockCodeArticle.indexOf(products.get(i).getProductCode())).getQuantity());
                                    }

                                    if (tarifsCodeArticle.contains(products.get(i).getProductCode())) {
                                        products.get(i).setPrice(tarifs.get(tarifsCodeArticle.indexOf(products.get(i).getProductCode())).getPrice());
                                    }

                                    productsList.add(products.get(i));
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            notifyDataSetChanged();
                                        }
                                    });
                                }
                            }
                            Gson gson = new Gson();
                            String json = gson.toJson(productsList);
                            Prefs.clearPref(context, "articlesFiltredByCat");
                            Prefs.setPref("articlesFiltredByCat", json, context);
                            //filtredArticleList.getArticlesFilredByCat(productsList);

                            activity.showProgressBar(false);
                        }else {
                            clear();
                        }
                    }
                });
    }



    @SuppressLint("CheckResult")
    private Double getTarification(Product product, final String tarif) {
        final double[] price = new double[1];
        price[0] = product.getPrice();
        activity.getLafargeDatabase().tarifDao().getTarifByCodeArticleAndRegimePrix(product.getProductCode(), tarif)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Tarif>>() {
                    @Override
                    public void accept(List<Tarif> tarifs) {
                        if (tarifs.size() > 0)
                            price[0] = tarifs.get(0).getPrice();

                    }
                });

        return price[0];
    }


    public void clear() {
        listProductsTemp.clear();
        productsList.clear();
        notifyDataSetChanged();
    }

    public void addProducts(ArrayList<Product> productsList) {
        if (productsList.size() > 0) {
            listProductsTemp.clear();
            int initSize = getItemCount();
            if(productsList.size()>initSize+10) {
                for (int i = initSize; i < initSize + 10; i++) {
                    listProductsTemp.add(productsList.get(i));
                }
            }else {
                for (int i = initSize; i < productsList.size() ; i++) {
                    listProductsTemp.add(productsList.get(i));
                }
            }
            insertAll(listProductsTemp);
        }
    }


    public void setDetailPrice(boolean detailPrice) {
        this.detailPrice = detailPrice;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        DebugLog.d("getItemCounttest " + productsList.size());

        return productsList.size();

    }

    public void updateItem(Product pos) {
        for (int i = 0; i < productsList.size(); i++) {
            if (productsList.get(i).getId().equals(pos.getId())) {
                productsList.remove(i);
                productsList.add(i, pos);
                notifyItemChanged(i);
                break;
            }
        }

    }

    public void removeItem(Product pos) {
        DebugLog.d("" + (productsList.size()));
        for (int i = 0; i < productsList.size(); i++) {
            if (productsList.get(i).getId().equals(pos.getId())) {
                productsList.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public void reset(ArrayList<Product> listProducts) {
        this.productsList.clear();
        this.productsList.addAll(initialProductsList);
        notifyDataSetChanged();
    }

    public boolean isSelectProduct() {
        return selectProduct;
    }

    public void setSelectProduct(boolean selectProduct) {
        this.selectProduct = selectProduct;
    }

    public boolean isCartList() {
        return cartList;
    }

    public void setCartList(boolean cartList) {
        this.cartList = cartList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);

        void onItemAdd(Product product, int pickedNumber, boolean fromdetail);

        void onItemUpdate(Product product);

        void onItemRemove(Product product);

    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final ArrayList<Product> list = new ArrayList<>();
            list.addAll(productsList);

            int count = list.size();
            final ArrayList<Product> nlist = new ArrayList<Product>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getName();
                if ((list.get(i).getName().toLowerCase().contains(filterString)) || (list.get(i).getEanCode().toLowerCase().contains(filterString))) {
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
            productsList.clear();
            if (results.values != null) {
                DebugLog.d("" + (results.values != null));
                productsList.addAll((ArrayList<Product>) results.values);
                DebugLog.d("" + (productsList.size()));
            }
            notifyDataSetChanged();
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_product_name)
        TextView textviewProductName;
        @BindView(R.id.textview_product_code)
        TextView textview_productCode;
        @BindView(R.id.textview_product_ref)
        TextView textviewProductRef;
        @BindView(R.id.textview_product_stock)
        EditText editTextProductStock;
        @BindView(R.id.textView_modify)
        TextView textViewModify;
        @BindView(R.id.textview_product_price)
        TextView textviewProductPrice;
        @BindView(R.id.linearlayout_actions)
        LinearLayout linearlayoutActions;
        @BindView(R.id.linearLayout_container)
        LinearLayout linearLayoutContainer;
        @BindView(R.id.relativeLyaout_update)
        RelativeLayout relativeLyaoutUpdate;
        @BindView(R.id.relativeLyaout_remove)
        RelativeLayout relativeLyaoutRemove;
        @BindView(R.id.relativeLayout_add_to_cart)
        RelativeLayout relativeLayoutAddToCart;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
