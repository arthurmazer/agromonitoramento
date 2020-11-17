package com.mazer.agromonitor.fragments;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mazer.agromonitor.BarChartActivity;
import com.mazer.agromonitor.HomeActivity;
import com.mazer.agromonitor.PieChartActivity;
import com.mazer.agromonitor.R;
import com.mazer.agromonitor.dialog.DialogChooseVariable;
import com.mazer.agromonitor.dialog.SummaryDialog;
import com.mazer.agromonitor.entity.Product;
import com.mazer.agromonitor.entity.Project;
import com.mazer.agromonitor.utils.Constants;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {

    private HomeActivity mActivity;
    private Button btnProximo;

    CardView cardLine;
    CardView cardBar;
    CardView cardPie;
    CardView cardResumo;

    public static Fragment newInstance(){
        Fragment reportFragment = new ReportFragment();
        return reportFragment;
    }

    public ReportFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mView = inflater.inflate(R.layout.fragment_report, container, false);
        mActivity = (HomeActivity)getActivity();

        cardLine = mView.findViewById(R.id.card_line_chart);
        cardBar = mView.findViewById(R.id.card_bar_chart);
        cardPie = mView.findViewById(R.id.card_pie_chart);
        cardResumo = mView.findViewById(R.id.card_resumo);


        cardLine.setOnClickListener(view -> openVariablesDialog());


        cardBar.setOnClickListener(view -> {
            Intent it = new Intent(mActivity.getApplicationContext(), BarChartActivity.class);
            mActivity.startActivityForResult(it, Constants.OPEN_CHART);
        });

        cardPie.setOnClickListener(view -> {
            Intent it = new Intent(mActivity.getApplicationContext(), PieChartActivity.class);
            mActivity.startActivityForResult(it, Constants.OPEN_CHART);
        });

        cardResumo.setOnClickListener(view -> {
            openSummaryDialog();
        });

        return mView;
    }


    @SuppressLint("StaticFieldLeak")
    public void openSummaryDialog(){

        final Project project = new Project();
        project.setId(mActivity.getOpenedProject());


        new AsyncTask<Void, Void, List<Product>>() {
            @Override
            protected List<Product> doInBackground(Void... voids) {
                return project.getVariablesOfProject(mActivity.getApplicationContext());
            }
            @Override
            protected void onPostExecute(List<Product> variablesList) {
                SummaryDialog summaryDialog;

                for (int i = 0; i < variablesList.size(); i++){
                    Product prod = variablesList.get(i);
                    if (prod.product.toLowerCase().equals("pt")){
                        summaryDialog = new SummaryDialog(mActivity, prod.getId());
                        summaryDialog.show();
                    }
                }

            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void openVariablesDialog(){

        final Project project = new Project();
        project.setId(mActivity.getOpenedProject());


        new AsyncTask<Void, Void, List<Product>>() {
            @Override
            protected List<Product> doInBackground(Void... voids) {
                return project.getVariablesOfProject(mActivity.getApplicationContext());
            }
            @Override
            protected void onPostExecute(List<Product> variablesList) {
                DialogChooseVariable dialogChooseVariable;
                dialogChooseVariable = new DialogChooseVariable(mActivity,variablesList, mActivity);
                dialogChooseVariable.show();
            }
        }.execute();
    }

}
