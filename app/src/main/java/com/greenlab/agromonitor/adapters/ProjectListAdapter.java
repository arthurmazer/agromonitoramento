package com.greenlab.agromonitor.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.greenlab.agromonitor.R;
import com.greenlab.agromonitor.entity.Project;

import java.util.ArrayList;

/**
 * Created by mazer on 2/15/2018.
 */

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {

    private ArrayList<Project> listProjects;

    public ProjectListAdapter(ArrayList<Project> listProjects){
        this.listProjects = listProjects;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project ,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Project project = listProjects.get(position);
        //holder.productName.setText(product);

        holder.pId.setText(project.getId());
        holder.pTitle.setText(project.getProjectName());
        //holder.pProducts.setText(project.);
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

        public ViewHolder(View itemView){
            super(itemView);
            pId = itemView.findViewById(R.id.project_id);
            pTitle = itemView.findViewById(R.id.project_title);
            pProducts = itemView.findViewById(R.id.list_products);
            pDate = itemView.findViewById(R.id.project_creation_date);

        }
    }
}
