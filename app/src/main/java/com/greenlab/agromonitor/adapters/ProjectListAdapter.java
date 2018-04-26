package com.greenlab.agromonitor.adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.greenlab.agromonitor.HomeActivity;
import com.greenlab.agromonitor.R;
import com.greenlab.agromonitor.entity.Product;
import com.greenlab.agromonitor.entity.Project;
import com.greenlab.agromonitor.utils.Constants;

import org.w3c.dom.Text;

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
        setHasStableIds(true);
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
                culture = "Cana-de-açucar";
                break;
            case Constants.PROJECT_TYPE_SOJA:
                culture = "Soja";
                break;
            case Constants.PROJECT_TYPE_ALGODAO:
                culture = "Algodão";
                break;
            case Constants.PROJECT_TYPE_AMENDOIM:
                culture = "Amendoim";
                break;
            case Constants.PROJECT_TYPE_MILHO:
                culture = "Milho";
                break;
            case Constants.PROJECT_TYPE_CAFE:
                culture = "Café";
                break;
        }
        holder.culture = project.getCultureType();
        holder.pCulture.setText(culture);
        if (!project.getFarmName().isEmpty()){
            holder.farmText.setText(project.getFarmName());
        }else{
            holder.farmLayout.setVisibility(View.GONE);
        }

        if (!project.getMachineID().isEmpty()){
            holder.idMachineText.setText(project.getMachineID());
        }else{
            holder.idMachineLayout.setVisibility(View.GONE);
        }

        if (!project.getTalhao().isEmpty()){
            holder.talhaoText.setText(project.getTalhao());
        }else{
            holder.talhaoLayout.setVisibility(View.GONE);
        }

        String turno = "";
        switch(project.getTurn()){
            case Constants.TURNO_DIURNO:
                turno = "Diurno";
                break;
            case Constants.TURNO_NOTURNO:
                turno = "Noturno";
                break;
        }
        holder.turnoText.setText(turno);

        if (!project.getOperatorsName().isEmpty()){
            holder.operatorText.setText(project.getOperatorsName());
        }else{
            holder.operatorLayout.setVisibility(View.GONE);
        }

        if (!project.getMeasurersName().isEmpty()){
            holder.measurerText.setText(project.getMeasurersName());
        }else{
            holder.measurerLayout.setVisibility(View.GONE);
        }
        if (!project.getFrenteColheita().isEmpty()){
            holder.frenteText.setText(project.getFrenteColheita());
        }else{
            holder.frenteLayout.setVisibility(View.GONE);
        }



        if (isMoreDetailsNecessary(holder)){
            holder.moreDetailsButton.setVisibility(View.VISIBLE);
        }else{
            holder.moreDetailsButton.setVisibility(View.INVISIBLE);
        }


        if ( project.isOpened ){
            int goldColor = ContextCompat.getColor(this.mActivity, R.color.gold);
            holder.openImage.setImageResource(R.drawable.project_opened);
            holder.pId.setTextColor(goldColor);
            holder.pDate.setTextColor(goldColor);
            holder.pTitle.setTextColor(goldColor);
            holder.pCulture.setTextColor(goldColor);
            holder.labelCulture.setTextColor(goldColor);
            holder.pProducts.setTextColor(goldColor);
            holder.labelOpen.setText("Aberto");
            holder.labelOpen.setTextColor(goldColor);
            holder.labelProducts.setTextColor(goldColor);

            holder.labelFarm.setTextColor(goldColor);
            holder.farmText.setTextColor(goldColor);
            holder.labelIdMachine.setTextColor(goldColor);
            holder.idMachineText.setTextColor(goldColor);
            holder.labelTalhao.setTextColor(goldColor);
            holder.talhaoText.setTextColor(goldColor);
            holder.labelTurno.setTextColor(goldColor);
            holder.turnoText.setTextColor(goldColor);
            holder.labelOperatorName.setTextColor(goldColor);
            holder.operatorText.setTextColor(goldColor);
            holder.labelMeasurer.setTextColor(goldColor);
            holder.measurerText.setTextColor(goldColor);
            holder.labelFrente.setTextColor(goldColor);
            holder.frenteText.setTextColor(goldColor);

        }
    }

    public boolean isMoreDetailsNecessary(ViewHolder holder){

        if ( holder.farmLayout.getVisibility() == View.VISIBLE  ||
                holder.idMachineLayout.getVisibility() == View.VISIBLE ||
                holder.talhaoLayout.getVisibility() == View.VISIBLE ||
                holder.operatorLayout.getVisibility() == View.VISIBLE ||
                holder.measurerLayout.getVisibility() == View.VISIBLE ||
                holder.frenteLayout.getVisibility() == View.VISIBLE)
            return true;
        else
            return false;
    }

    @Override
    public int getItemCount() {
        return listProjects == null ? 0 : listProjects.size();
    }

    @Override
    public long getItemId(int position){
        return listProjects.get(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView pId;
        private TextView pTitle;
        private TextView pProducts;
        private TextView pDate;
        private TextView labelCulture;
        private TextView pCulture;
        private TextView labelOpen;
        private TextView labelProducts;

        private LinearLayout farmLayout;
        private TextView labelFarm;
        private TextView farmText;
        private LinearLayout idMachineLayout;
        private TextView labelIdMachine;
        private TextView idMachineText;
        private LinearLayout talhaoLayout;
        private TextView labelTalhao;
        private TextView talhaoText;
        private LinearLayout turnoLayout;
        private TextView labelTurno;
        private TextView turnoText;
        private LinearLayout operatorLayout;
        private TextView labelOperatorName;
        private TextView operatorText;
        private LinearLayout measurerLayout;
        private TextView labelMeasurer;
        private TextView measurerText;
        private LinearLayout frenteLayout;
        private TextView labelFrente;
        private TextView frenteText;
        private int culture;


        private RelativeLayout layoutProject;
        private LinearLayout layoutDescription;
        private RelativeLayout rightPanel;
        private LinearLayout moreDetailsButton;
        private LinearLayout moreDetailsHidden;
        public TextView moreOrLessDetailsText;
        ImageView openImage;
        ImageView arrowImage;

        public ViewHolder(View itemView){
            super(itemView);
            pId = itemView.findViewById(R.id.project_id);
            pTitle = itemView.findViewById(R.id.project_title);
            pProducts = itemView.findViewById(R.id.list_products);
            pDate = itemView.findViewById(R.id.project_creation_date);
            openImage = itemView.findViewById(R.id.imageView);
            pCulture = itemView.findViewById(R.id.text_culture);
            labelCulture = itemView.findViewById(R.id.label_culture);
            labelOpen = itemView.findViewById(R.id.label_open);
            labelProducts = itemView.findViewById(R.id.label_products);
            layoutProject = itemView.findViewById(R.id.relative_id_culture);
            layoutDescription = itemView.findViewById(R.id.layout_description);
            rightPanel = itemView.findViewById(R.id.right_side_layout);
            moreDetailsButton = itemView.findViewById(R.id.more_details_button);
            moreDetailsHidden = itemView.findViewById(R.id.more_details_hidden);
            arrowImage = itemView.findViewById(R.id.arrow_image);
            moreOrLessDetailsText = itemView.findViewById(R.id.more_or_less_details);

            labelFarm = itemView.findViewById(R.id.farm_label);
            farmText = itemView.findViewById(R.id.farm);
            farmLayout = itemView.findViewById(R.id.layout_farm);
            idMachineLayout = itemView.findViewById(R.id.layout_id_machine);
            labelIdMachine = itemView.findViewById(R.id.id_machine_label);
            idMachineText = itemView.findViewById(R.id.id_machine);
            talhaoLayout = itemView.findViewById(R.id.layout_talhao);
            labelTalhao = itemView.findViewById(R.id.label_talhao);
            talhaoText = itemView.findViewById(R.id.talhao);
            turnoLayout = itemView.findViewById(R.id.layout_turno);
            labelTurno = itemView.findViewById(R.id.label_turno);
            turnoText = itemView.findViewById(R.id.turno);
            operatorLayout = itemView.findViewById(R.id.layout_operator_name);
            labelOperatorName = itemView.findViewById(R.id.label_nome_operador);
            operatorText = itemView.findViewById(R.id.operator);
            measurerLayout = itemView.findViewById(R.id.layout_avaliador);
            labelMeasurer = itemView.findViewById(R.id.label_avaliador);
            measurerText = itemView.findViewById(R.id.avaliador);
            frenteLayout = itemView.findViewById(R.id.layout_frente);
            labelFrente = itemView.findViewById(R.id.label_frente);
            frenteText = itemView.findViewById(R.id.frente);

            layoutProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openProject();
                }
            });
            layoutDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openProject();
                }
            });
            rightPanel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openProject();
                }
            });
            moreDetailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (moreDetailsHidden.getVisibility() == View.VISIBLE){
                        moreDetailsHidden.setVisibility(View.GONE);
                        arrowImage.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                        moreOrLessDetailsText.setText("MAIS DETALHES");
                    }else{
                        moreDetailsHidden.setVisibility(View.VISIBLE);
                        arrowImage.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                        moreOrLessDetailsText.setText("MENOS DETALHES");
                    }
                }
            });
        }


        public void openProject(){
            int idProject = Integer.valueOf(pId.getText().toString().replace("#","").trim());
            mActivity.setProjectOpened(idProject, pTitle.getText().toString());
            mActivity.changeToSpreadsheetScreen();
        }
    }
}
