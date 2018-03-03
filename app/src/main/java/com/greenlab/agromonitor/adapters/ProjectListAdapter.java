package com.greenlab.agromonitor.adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.greenlab.agromonitor.HomeActivity;
import com.greenlab.agromonitor.R;
import com.greenlab.agromonitor.entity.Product;
import com.greenlab.agromonitor.entity.Project;
import com.greenlab.agromonitor.utils.Constants;

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
        String culture = "";
        switch(project.getCultureType()){
            case Constants.PROJECT_TYPE_CANA_DE_ACUCAR:
                culture = "Cana";
                break;
            case Constants.PROJECT_TYPE_SOJA:
                culture = "Soja";
                break;
        }
        holder.pCulture.setText(culture);

        if ( project.isOpened ){
            int codeColor = ContextCompat.getColor(this.mActivity, R.color.gold);
            holder.openImage.setImageResource(R.drawable.project_opened);
            holder.pId.setTextColor(codeColor);
            holder.pDate.setTextColor(codeColor);
            holder.pTitle.setTextColor(codeColor);
            holder.pCulture.setTextColor(codeColor);
            holder.pProducts.setTextColor(codeColor);
            holder.labelOpen.setText("Aberto");
            holder.labelOpen.setTextColor(codeColor);
            holder.labelProducts.setTextColor(codeColor);
        }
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
        public TextView pCulture;
        public TextView labelOpen;
        public TextView labelProducts;
        ImageView openImage;

        public ViewHolder(View itemView){
            super(itemView);
            pId = itemView.findViewById(R.id.project_id);
            pTitle = itemView.findViewById(R.id.project_title);
            pProducts = itemView.findViewById(R.id.list_products);
            pDate = itemView.findViewById(R.id.project_creation_date);
            openImage = itemView.findViewById(R.id.imageView);
            pCulture = itemView.findViewById(R.id.label_culture);
            labelOpen = itemView.findViewById(R.id.label_open);
            labelProducts = itemView.findViewById(R.id.label_products);

            openImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int idProject = Integer.valueOf(pId.getText().toString().replace("#","").trim());
                    mActivity.setProjectOpened(idProject, pTitle.getText().toString());
                    mActivity.changeToSpreadsheetScreen();
                }
            });
        }
    }
}
