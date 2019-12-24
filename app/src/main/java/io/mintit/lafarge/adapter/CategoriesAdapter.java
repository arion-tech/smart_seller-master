package io.mintit.lafarge.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mintit.lafarge.R;
import io.mintit.lafarge.model.Category;
import io.mintit.lafarge.ui.activity.MainActivity;
import io.mintit.lafarge.utils.DebugLog;

/**
 * Created by mint on 17/04/17.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Category> categoriesList = new ArrayList<>();
    ArrayList<Category> initialList = new ArrayList<>();
    Context context;
    OnItemClickListener onItemClickListener;
    private MainActivity activity;
    private boolean selectCustomer;
    private boolean fromProfile;
    private List<ArrayList<Category>> path = new ArrayList<>();

    public CategoriesAdapter(ArrayList<Category> categoriesList, Context context, MainActivity activity) {
        this.categoriesList.addAll(categoriesList);
        //  path.add(categoriesList);
        this.initialList.addAll(categoriesList);
        this.context = context;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder mViewHolder = (ViewHolder) holder;
        final Category category = categoriesList.get(position);
        mViewHolder.textviewCategoryName.setText(category.getLibelle());
        mViewHolder.textviewCategoryName.setTextColor(category.isSelected() ? context.getResources().getColor(R.color.colorAccent) : context.getResources().getColor(R.color.darkGray));
        mViewHolder.imageViewNext.setVisibility(category.getChildren() != null && category.getChildren().size() > 0 ? View.VISIBLE : View.INVISIBLE);
        mViewHolder.linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (category.getChildren() != null && category.getChildren().size() > 0) {
                    addAll(category.getChildren());
                } else {
                    for (int i = 0; i < categoriesList.size(); i++) {
                        categoriesList.get(i).setSelected(false);
                    }
                    categoriesList.get(position).setSelected(true);

                    notifyDataSetChanged();
                }
                DebugLog.d(String.valueOf(category.getId()));
                onItemClickListener.onItemClick(category);
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public boolean stepBack() {

        if (path.size() > 0) {
            categoriesList.clear();
            categoriesList.addAll(path.get(path.size() - 1));
            notifyDataSetChanged();
            path.remove(path.size() - 1);
        }
        return path.size() == 0;

    }

    public boolean isFromProfile() {
        return fromProfile;
    }

    public void setFromProfile(boolean fromProfile) {
        this.fromProfile = fromProfile;
    }

    public void updateItem(int i, Category cart) {
        categoriesList.remove(i);
        categoriesList.add(i, cart);
        notifyItemChanged(i);
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public void addAll(ArrayList<Category> customers) {
        // path.add(customers);
        categoriesList.clear();
        categoriesList.addAll(customers);
        notifyDataSetChanged();
    }

    public void removeItem(Category pos) {
        for (int i = 0; i < categoriesList.size(); i++) {
            if (categoriesList.get(i).getId().equals(pos.getId())) {
                categoriesList.remove(i);
                notifyItemRemoved(i);
            }
        }

    }

    public void reset(ArrayList<Category> customers) {
        this.categoriesList.clear();
        this.categoriesList.addAll(customers);
        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        public void onItemClick(Category cart);


    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_category_name)
        TextView textviewCategoryName;

        @BindView(R.id.imageView_next)
        ImageView imageViewNext;

        @BindView(R.id.linearLayout_container)
        LinearLayout linearLayoutContainer;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
