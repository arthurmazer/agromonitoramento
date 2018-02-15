package com.greenlab.agromonitor.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greenlab.agromonitor.R;
import java.util.ArrayList;

/**
 * Created by mazer on 2/15/2018.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private ArrayList<String> listProducts;

    public ProductListAdapter(ArrayList<String> listProducts){
        this.listProducts = listProducts;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_product ,parent,false);
        ViewHolder  myViewHolder = new ViewHolder(view);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String product = listProducts.get(position);
        holder.productName.setText(product);
    }

    @Override
    public int getItemCount() {
        return listProducts == null ? 0 : listProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        public ViewHolder(View itemView){
            super(itemView);
            productName = itemView.findViewById(R.id.textProductList);
        }
    }
}
