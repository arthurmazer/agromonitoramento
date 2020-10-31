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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.greenlab.agromonitor.R;

import com.greenlab.agromonitor.entity.Product;
import com.greenlab.agromonitor.entity.Project;
import com.greenlab.agromonitor.entity.ProjectProduct;
import com.greenlab.agromonitor.entity.SpreadsheetValues;
import com.greenlab.agromonitor.utils.SpreedsheatUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
                viewHolder = new ViewHolder(v1, this.mContext);
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
                SpreadsheetValues spreadsheetValues = (SpreadsheetValues) this.spreadsheetList.get(position);
                productHolder.productValue.setText(String.valueOf(spreadsheetValues.getValue()));
                productHolder.idProject = spreadsheetValues.getIdProject();
                productHolder.idProduct = spreadsheetValues.getId();
                break;

            case CATEGORY:
                ViewHolderCategoria headerCategory = (ViewHolderCategoria) holder;
                Product prod = (Product) this.spreadsheetList.get(position);
                headerCategory.titleProduct.setText(prod.getProduct());
                headerCategory.idProject = prod.getIdProject();
                headerCategory.idProduct = prod.getId();

                if (prod.getProduct().toLowerCase().equals("pt")){
                    headerCategory.layoutAddProduct.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void getSumPT(){
        HashMap<Integer, ArrayList<Float>> myHash;
        myHash = SpreedsheatUtils.getSubArrays(spreadsheetList);
        updatePT(myHash);
    }

    public void updatePT(HashMap<Integer, ArrayList<Float>> hashList){
        removeAllFromCategory("pt");
        for (HashMap.Entry<Integer, ArrayList<Float>> entry : hashList.entrySet()) {
            ArrayList<Float> myList;
            myList = entry.getValue();
            float sum = 0f;
            for (int i = 0; i < myList.size(); i++){
                sum += myList.get(i);
            }
            insertValueOnList(sum, "pt");
        }
    }

    public Product getCategory(String category){
        for (int i = 0; i < spreadsheetList.size(); i++){
            if (spreadsheetList.get(i) instanceof Product){
                Product product = (Product) spreadsheetList.get(i);
                if (product.getProduct().toLowerCase().equals(category)) {
                    return product;
                }
            }
        }
        return null;
    }

    public int getLastPositionOfCategory(String category){
        boolean flagFounded = false;
        for (int i = 0; i < spreadsheetList.size(); i++){
            if (spreadsheetList.get(i) instanceof Product){
                Product product = (Product) spreadsheetList.get(i);

                if (flagFounded)
                    return i;

                if (product.getProduct().equals(category)) {
                    flagFounded = true;
                }
            }
        }
        return spreadsheetList.size();
    }

    public void insertValueOnList(Float value, String category){
        int index = getLastPositionOfCategory(category);
        SpreadsheetValues spreadsheetValues = new SpreadsheetValues();

        Product prod = getCategory("pt");

        if(prod != null) {
            spreadsheetValues.setValue(value);
            spreadsheetValues.setId(prod.getId());
            spreadsheetValues.setIdProject(prod.getIdProject());

            Project project = new Project();
            ProjectProduct projectProduct = new ProjectProduct();
            projectProduct.setIdProduct(prod.getId());
            projectProduct.setIdProject(prod.getIdProject());
            projectProduct.setValue(value);
            project.insertProjectProduct( this.mContext, projectProduct);

            spreadsheetList.add(index, spreadsheetValues);
            notifyDataSetChanged();
        }

    }

    public void removeAllFromCategory(String category){
        boolean flagFounded = false;
        ArrayList<Integer> arrayIndexes = new ArrayList<>();
        Product prodPt  = new Product();
        for (int i = 0; i < spreadsheetList.size(); i++){
            if (spreadsheetList.get(i) instanceof Product){
                Product product = (Product) spreadsheetList.get(i);

                if (flagFounded)
                    break;

                if (product.getProduct().toLowerCase().equals(category)) {
                    flagFounded = true;
                    prodPt = product;
                }
            }else{
                if (flagFounded){
                    arrayIndexes.add(i);
                }
            }
        }
        if (arrayIndexes.size() > 0) {
            spreadsheetList.subList(arrayIndexes.get(0), arrayIndexes.get(arrayIndexes.size()-1)+1).clear();
            notifyItemRangeRemoved(arrayIndexes.get(0), arrayIndexes.size());
            Project proj = new Project();
            proj.removeProjectProduct(this.mContext, prodPt.getId());
        }
    }

    @Override
    public int getItemCount() {
        return spreadsheetList == null ? 0 : spreadsheetList.size();

    }


    @Override
    public int getItemViewType(int position) {
        if (spreadsheetList.get(position) instanceof SpreadsheetValues) {
            return PRODUCT_VALUE;
        } else if (spreadsheetList.get(position) instanceof Product) {
            return CATEGORY;
        }
        return -1;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView productValue;
        public ImageView action;
        public RelativeLayout layoutProduct;
        Context ctx;
        String category;
        int idProject;
        int idProduct;

        public ViewHolder(View itemView, final Context ctx) {
            super(itemView);
            productValue = (TextView) itemView.findViewById(R.id.product_value);
            layoutProduct = itemView.findViewById(R.id.layout_item_variable);
            action = itemView.findViewById(R.id.action_list);

            this.ctx = ctx;

            layoutProduct.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new MaterialDialog.Builder(ctx)
                            .title("Editar valor: " + productValue.getText().toString())
                            .content("Novo valor")
                            .inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL)
                            .negativeText("Cancelar")
                            .neutralText("Deletar")
                            .neutralColor(ctx.getResources().getColor(R.color.red))
                            .positiveText("Inserir")
                            .onNegative((dialog, which) -> dialog.dismiss())
                            .input("", productValue.getText().toString(), new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(MaterialDialog dialog, CharSequence input) {
                                    if (!input.toString().isEmpty())
                                        updateValueOnList(input.toString(), getAdapterPosition());
                                }
                            }).show();
                    return true;
                }
            });
        }


        public void updateValueOnList(String newValue, int position){

            Project project = new Project();
            project.setId(idProject);
            ProjectProduct projectProduct = new ProjectProduct();
            projectProduct.setIdProduct(idProduct);
            projectProduct.setIdProject(idProject);
            projectProduct.setValue(Float.valueOf(newValue));
            project.updateProjectProduct(this.ctx, projectProduct);

            ((SpreadsheetValues) spreadsheetList.get(position)).setValue(Float.valueOf(newValue));
            notifyItemChanged(position);

        }


        @Override
        public void onClick(View view) {

        }
    }

    class ViewHolderCategoria extends RecyclerView.ViewHolder  {

        public TextView titleProduct;
        LinearLayout layoutAddProduct;
        Context ctx;
        int idProject;
        int idProduct;

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
                            .input("Ex: 0.89", "", new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(MaterialDialog dialog, CharSequence input) {
                                    if (!input.toString().isEmpty()) {
                                        insertValueOnList(input.toString());
                                        getSumPT();
                                    }
                                }
                            }).show();
                }
            });
            this.ctx = ctx;
        }

        public void insertValueOnList(String value){
            String currentProduct = titleProduct.getText().toString();
            int index = getLastPositionOfCategory(currentProduct);
            SpreadsheetValues spreadsheetValues = new SpreadsheetValues();

            spreadsheetValues.setValue(Float.valueOf(value));
            spreadsheetValues.setId(idProduct);
            spreadsheetValues.setIdProject(idProject);

            Project project = new Project();
            ProjectProduct projectProduct = new ProjectProduct();
            projectProduct.setIdProduct(idProduct);
            projectProduct.setIdProject(idProject);
            projectProduct.setValue(Float.valueOf(value));
            project.insertProjectProduct(this.ctx, projectProduct);

            spreadsheetList.add(index,spreadsheetValues);
            notifyDataSetChanged();

        }

        public int getLastPositionOfCategory(String category){
            boolean flagFounded = false;
            for (int i = 0; i < spreadsheetList.size(); i++){
                if (spreadsheetList.get(i) instanceof Product){
                    Product product = (Product) spreadsheetList.get(i);

                    if (flagFounded)
                        return i;

                    if (product.getProduct().equals(category)) {
                        flagFounded = true;
                    }
                }
            }
            return spreadsheetList.size();
        }
    }
}
