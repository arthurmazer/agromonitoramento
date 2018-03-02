package com.greenlab.agromonitor.adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.greenlab.agromonitor.HomeActivity;
import com.greenlab.agromonitor.R;
import com.greenlab.agromonitor.entity.Product;
import com.greenlab.agromonitor.entity.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazer on 2/15/2018.
 */

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {

    private List<Project> listProjects;
    HomeActivity mActivity;

    public ProjectListAdapter(List<Project> listProjects, HomeActivity mActivity){
        this.listProjects = listProjects;
        this.mActivity = mActivity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_projects ,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Project project = listProjects.get(position);
        //holder.productName.setText(product);

        String id = "#" + String.valueOf(project.getId());
        holder.pId.setText(id);
        holder.pTitle.setText(project.getProjectName());
        List<Product> productsList = project.getListOfProducts();
        StringBuilder products = new StringBuilder();

        for(int i = 0; i < productsList.size(); i++){
            Product product = productsList.get(i);
            if (i != 0)
                products.append(", ");
            products.append(product.getProduct());
        }
        holder.pProducts.setText(products.toString());
        holder.pDate.setText(project.getCreationDate());
    }

    @Override
    public int getItemCount() {
        return listProjects == null ? 0 : listProjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pId;
        public TextView pTitle;
        public TextView pProducts;
        public TextView pDate;
        ImageView openImage;

        public ViewHolder(View itemView){
            super(itemView);
            pId = itemView.findViewById(R.id.project_id);
            pTitle = itemView.findViewById(R.id.project_title);
            pProducts = itemView.findViewById(R.id.list_products);
            pDate = itemView.findViewById(R.id.project_creation_date);
            openImage = itemView.findViewById(R.id.imageView);

            openImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int idProject = Integer.valueOf(pId.getText().toString().replace("#","").trim());
                    Log.d("aqui PROJETO : ", String.valueOf(idProject));
                    mActivity.setProjectOpened(idProject);
                    mActivity.changeToSpreadsheetScreen();
                }
            });
        }
    }
}
