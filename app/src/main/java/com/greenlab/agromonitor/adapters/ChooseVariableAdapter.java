package com.greenlab.agromonitor.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greenlab.agromonitor.LineChartActivity;
import com.greenlab.agromonitor.R;
import com.greenlab.agromonitor.entity.Product;
import com.greenlab.agromonitor.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazer on 2/15/2018.
 */

public class ChooseVariableAdapter extends RecyclerView.Adapter<ChooseVariableAdapter.ViewHolder> {

    private List<Product> listOfVariables;
    private Context context;
    private Activity activity;

    public ChooseVariableAdapter(List<Product> listOfVariables, Activity activity){
        this.listOfVariables = listOfVariables;
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_variable ,parent,false);
        this.context = parent.getContext();
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = listOfVariables.get(position);
        holder.variable.setText(product.getProduct());
        holder.idProduct = product.getId();
    }

    @Override
    public int getItemCount() {
        return listOfVariables == null ? 0 : listOfVariables.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView variable;
        RelativeLayout layoutVariable;
        int idProduct;

        ViewHolder(View itemView){
            super(itemView);
            variable = itemView.findViewById(R.id.variable);
            layoutVariable = itemView.findViewById(R.id.layout_variable);

            layoutVariable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //listOfVariables.remove(getAdapterPosition());
                    //notifyDataSetChanged();
                    Intent it = new Intent(activity, LineChartActivity.class);
                    it.putExtra("id_product", idProduct);
                    activity.startActivityForResult(it, Constants.OPEN_CHART);
                }
            });
        }
    }
}
