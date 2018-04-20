package com.greenlab.agromonitor.fragments;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.greenlab.agromonitor.BarChartActivity;
import com.greenlab.agromonitor.HomeActivity;
import com.greenlab.agromonitor.LineChartActivity;
import com.greenlab.agromonitor.PieChartActivity;
import com.greenlab.agromonitor.R;
import com.greenlab.agromonitor.dialog.DialogChooseVariable;
import com.greenlab.agromonitor.entity.Product;
import com.greenlab.agromonitor.entity.Project;
import com.greenlab.agromonitor.entity.SpreadsheetValues;
import com.greenlab.agromonitor.utils.Constants;

import org.apache.commons.math3.analysis.function.Constant;

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


        cardLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVariablesDialog();
                //overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_right_out_activity);
            }
        });


        cardBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mActivity.getApplicationContext(), BarChartActivity.class);
                mActivity.startActivityForResult(it, Constants.OPEN_CHART);
                //overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_right_out_activity);
            }
        });

        cardPie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mActivity.getApplicationContext(), PieChartActivity.class);
                mActivity.startActivityForResult(it, Constants.OPEN_CHART);
                //overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_right_out_activity);
            }
        });

        return mView;
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
