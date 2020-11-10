package com.greenlab.agromonitor.adapters;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.greenlab.agromonitor.R;
import com.greenlab.agromonitor.entity.Variables;

import java.util.ArrayList;

/**
 * Created by mazer on 2/15/2018.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private ArrayList<Variables> listProducts;
    private Context context;

    public ProductListAdapter(ArrayList<Variables> listProducts){
        this.listProducts = listProducts;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_product ,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Variables var = listProducts.get(position);
        holder.productName.setText(var.getVarName());

        if (var.isPerdasTotais){
            holder.productName.setBackgroundResource(R.drawable.shape_list_pt);
            holder.removeIcon.setVisibility(View.GONE);
        }else{
            holder.productName.setBackgroundResource(R.drawable.shape_list_product);
            holder.removeIcon.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return listProducts == null ? 0 : listProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        public ImageView removeIcon;

        public ViewHolder(View itemView){
            super(itemView);
            productName = itemView.findViewById(R.id.textProductList);
            removeIcon = itemView.findViewById(R.id.remove_product);

            removeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listProducts.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }
    }
}
