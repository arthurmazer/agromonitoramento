package com.greenlab.agromonitor.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greenlab.agromonitor.R;

import com.greenlab.agromonitor.entity.Product;

import java.util.ArrayList;

/**
 * Created by arthu on 2/5/2018.
 */

public class SpreadsheetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<Object> spreadsheetList;
    private Context context;
    private Activity mActivity;

    private static final int REQUEST = 112;
    private final int PRODUCT_VALUE = 0, CATEGORY = 1;


    public SpreadsheetAdapter(ArrayList<Object> spreadsheetList, Context context, Activity annalsActivity) {
        this.spreadsheetList = spreadsheetList;
        this.context = context;
        this.mActivity = annalsActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        switch (holder.getItemViewType()) {
            case PRODUCT_VALUE:
                ViewHolder productHolder = (ViewHolder) holder;
                Product product = (Product) this.spreadsheetList.get(position);
                productHolder.productValue.setText(String.valueOf(product.getProductValue()));
                break;
            case CATEGORY:
                ViewHolderCategoria headerCategory = (ViewHolderCategoria) holder;
                String categoria = (String) this.spreadsheetList.get(position);
                headerCategory.titleProduct.setText(categoria);
                break;

        }


    }

    @Override
    public int getItemCount() {
        return spreadsheetList == null ? 0 : spreadsheetList.size();

    }


    @Override
    public int getItemViewType(int position) {
        if (spreadsheetList.get(position) instanceof Product) {
            return PRODUCT_VALUE;
        } else if (spreadsheetList.get(position) instanceof String) {
            return CATEGORY;
        }
        return -1;
    }


    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView productValue;
        public Context context;
        public Activity activity;


        public ViewHolder(View itemView, Context context, Activity activity) {
            super(itemView);
            productValue = (TextView) itemView.findViewById(R.id.product_value);
            this.activity = activity;
            this.context = context;

        }

        @Override
        public void onClick(View v) {
            // if (v.getId() == itemLayout.getId()){

        }
    }

    class ViewHolderCategoria extends RecyclerView.ViewHolder {

        public TextView titleProduct;
        public TextView quantProduct;


        public ViewHolderCategoria(View itemView) {
            super(itemView);
            titleProduct = (TextView) itemView.findViewById(R.id.title_product);
        }
    }
}
