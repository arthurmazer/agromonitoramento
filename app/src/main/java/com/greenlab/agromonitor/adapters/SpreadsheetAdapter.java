package com.greenlab.agromonitor.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
    private Context mContext;

    private static final int REQUEST = 112;
    public final int PRODUCT_VALUE = 0, CATEGORY = 1;


    public SpreadsheetAdapter(ArrayList<Object> spreadsheetList) {
        this.spreadsheetList = spreadsheetList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case PRODUCT_VALUE:
                View v1 = inflater.from(parent.getContext()).inflate(R.layout.item_product_value,parent,false);
                viewHolder = new ViewHolder(v1);
                break;

            case CATEGORY:
                View v2 = inflater.from(parent.getContext()).inflate(R.layout.item_category_header,parent,false);
                viewHolder = new ViewHolderCategoria(v2);
                break;
            default:
                break;
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        switch (holder.getItemViewType()) {
            case PRODUCT_VALUE:
                ViewHolder productHolder = (ViewHolder) holder;
                Product product = (Product) this.spreadsheetList.get(position);
                productHolder.productValue.setText(String.valueOf(product.getValue()));
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


        public ViewHolder(View itemView) {
            super(itemView);
            productValue = (TextView) itemView.findViewById(R.id.product_value);
        }

        @Override
        public void onClick(View v) {

        }
    }

    class ViewHolderCategoria extends RecyclerView.ViewHolder {

        public TextView titleProduct;
        public TextView quantProduct;


        public ViewHolderCategoria(View itemView) {
            super(itemView);
            titleProduct = itemView.findViewById(R.id.title_product);
        }
    }
}
