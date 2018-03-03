package com.greenlab.agromonitor.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
                viewHolder = new ViewHolderCategoria(v2, this.mContext);
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
                productHolder.productValue.setText(String.valueOf(product.getProduct()));
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
        public ImageView action;


        public ViewHolder(View itemView) {
            super(itemView);
            productValue = (TextView) itemView.findViewById(R.id.product_value);
            action = itemView.findViewById(R.id.action_list);
        }


        @Override
        public void onClick(View view) {

        }
    }

    class ViewHolderCategoria extends RecyclerView.ViewHolder implements View.OnClickListener  {

        public TextView titleProduct;
        LinearLayout layoutAddProduct;
        Context ctx;


        public ViewHolderCategoria(View itemView, final Context ctx) {
            super(itemView);
            titleProduct = itemView.findViewById(R.id.title_product);
            layoutAddProduct = itemView.findViewById(R.id.layout_add_product_value);

            layoutAddProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new MaterialDialog.Builder(ctx)
                            .title(titleProduct.getText())
                            .content("Insira um valor")
                            .inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL)
                            .negativeText("Cancelar")
                            .positiveText("Inserir")
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .input("0.5", "", new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(MaterialDialog dialog, CharSequence input) {

                                        //spreadsheetList.add("ok");
                                    insertValueOnList(input.toString());
                                }
                            }).show();
                }
            });
            this.ctx = ctx;
        }

        public void insertValueOnList(String value){
            int index = 0;
            boolean founded = false;
            String currentProduct = "", oldProduct = "";



            for (int i = 0; i < spreadsheetList.size(); i++){
                currentProduct = titleProduct.getText().toString();
                if (spreadsheetList.get(i).toString().equals(currentProduct)){
                    index = i;
                    Log.d("indice -aqui ", "> " + index);
                }
                Log.d("indice-current", currentProduct);
                Log.d("indice,", spreadsheetList.get(i).toString());
            }
            Float fValue = Float.valueOf(value);
            Product product = new Product();
            product.setProduct(value);
            spreadsheetList.add(index+1,product);
            notifyDataSetChanged();

        }

        @Override
        public void onClick(View view) {
            Log.d("aqui", "aushdusad");

        }
    }
}
