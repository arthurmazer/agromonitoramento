package com.greenlab.agromonitor.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.greenlab.agromonitor.HomeActivity;
import com.greenlab.agromonitor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {

    private HomeActivity mActivity;
    public int chatVisible = 1;
    private Button btnProximo;

    public static Fragment newInstance(){
        Fragment reportFragment = new ReportFragment();
        return reportFragment;
    }

    public ReportFragment() {
        // Required empty public constructor
        chatVisible = 1;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mView = inflater.inflate(R.layout.fragment_report, container, false);
        mActivity = (HomeActivity)getActivity();


        btnProximo = mView.findViewById(R.id.btn_proximo);
        final BarChart chart = (BarChart) mView.findViewById(R.id.chart1);
        final LineChart lineChart = (LineChart) mView.findViewById(R.id.chart12);
        final PieChart pieChart = (PieChart) mView.findViewById(R.id.chart3);

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 30f));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 60f));
        entries.add(new BarEntry(3f, 50f));
        // gap of 2f
        entries.add(new BarEntry(5f, 70f));
        entries.add(new BarEntry(6f, 60f));

        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        set.setColors(ColorTemplate.JOYFUL_COLORS);
        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh


        List<Entry> valsComp1 = new ArrayList<Entry>();
        List<Entry> valsComp2 = new ArrayList<Entry>();

        Entry c1e1 = new Entry(0f, 100000f); // 0 == quarter 1
        valsComp1.add(c1e1);
        Entry c1e2 = new Entry(1f, 140000f); // 1 == quarter 2 ...
        valsComp1.add(c1e2);
        // and so on ...

        Entry c2e1 = new Entry(0f, 130000f); // 0 == quarter 1
        valsComp2.add(c2e1);
        Entry c2e2 = new Entry(1f, 115000f); // 1 == quarter 2 ...
        valsComp2.add(c2e2);

        LineDataSet setComp1 = new LineDataSet(valsComp1, "Company 1");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineDataSet setComp2 = new LineDataSet(valsComp2, "Company 2");
        setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp1.setColors(ColorTemplate.LIBERTY_COLORS);
        setComp2.setColors(ColorTemplate.MATERIAL_COLORS);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);
        dataSets.add(setComp2);

        LineData data2 = new LineData(dataSets);
        lineChart.setData(data2);
        lineChart.invalidate(); // refresh

        List<PieEntry> entries2 = new ArrayList<>();

        entries2.add(new PieEntry(18.5f, "Green"));
        entries2.add(new PieEntry(26.7f, "Yellow"));
        entries2.add(new PieEntry(24.0f, "Red"));
        entries2.add(new PieEntry(30.8f, "Blue"));

        PieDataSet set2 = new PieDataSet(entries2, "Election Results");
        set2.setColors(ColorTemplate.VORDIPLOM_COLORS);
        PieData data3 = new PieData(set2);
        pieChart.setData(data3);
        pieChart.invalidate(); // refresh


        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (chatVisible){
                    case 1:
                        chart.setVisibility(View.GONE);
                        lineChart.setVisibility(View.VISIBLE);
                        pieChart.setVisibility(View.GONE);
                        chatVisible = 2;
                        break;
                    case 2:
                        chart.setVisibility(View.GONE);
                        lineChart.setVisibility(View.GONE);
                        pieChart.setVisibility(View.VISIBLE);
                        chatVisible = 3;
                        break;
                    case 3:
                        chart.setVisibility(View.VISIBLE);
                        pieChart.setVisibility(View.GONE);
                        lineChart.setVisibility(View.GONE);
                        chatVisible = 1;
                        break;
                }
            }
        });

        return mView;
    }

}
