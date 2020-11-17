package com.mazer.agromonitor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.mazer.agromonitor.charts.DayAxisValueFormatter;
import com.mazer.agromonitor.charts.MyAxisValueFormatter;
import com.mazer.agromonitor.charts.XYMarkerView;
import com.mazer.agromonitor.entity.Project;
import com.mazer.agromonitor.entity.SpreadsheetValues;
import com.mazer.agromonitor.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarChartActivity extends BaseActivity implements
        OnChartValueSelectedListener {

    protected BarChart mChart;
    private ArrayList<Float> listOfValues;
    private Project currentProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        listOfValues = new ArrayList<>();
        mChart = (BarChart) findViewById(R.id.bar_chart);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);


        loadValuesFromProject();

        // setting data

        // mChart.setDrawLegend(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mChart.performClick();
    }

    @SuppressLint("StaticFieldLeak")
    public void loadValuesFromProject(){
        int idProject = getOpenedProject();

        final Project project = new Project();
        project.setId(idProject);
        new AsyncTask<Void, Void, List<SpreadsheetValues>>() {

            @Override
            protected List<SpreadsheetValues> doInBackground(Void... voids) {
                return project.getSpreadSheetValuesNotNull(getApplicationContext());
            }
            @Override
            protected void onPostExecute(List<SpreadsheetValues> spreadsheetValuesList) {
                loadMyCurrentProject(spreadsheetValuesList);
            }
        }.execute();

    }

    @SuppressLint("StaticFieldLeak")
    public void loadMyCurrentProject(List<SpreadsheetValues> spreadsheetValuesList){
        int idProject = getOpenedProject();

        final Project mProject = new Project();
        mProject.setId(idProject);
        new AsyncTask<Void, Void, Project>() {
            @Override
            protected Project doInBackground(Void... voids) {
                return mProject.getActualProject(getApplicationContext());
            }
            @Override
            protected void onPostExecute(Project proj) {
                setData(spreadsheetValuesList, proj);
            }
        }.execute();

    }

    private float getBarValue(ArrayList<Float> barValues, Project proj){
        float sum = 0f;
        float areaAmostral = proj.getAreaAmostral();
        for (float value : barValues){
            sum += value;
        }
        if (proj.getMeasureUnity() == Constants.KILO){
            return getValueCorrigidoUmidade(sum * 10000f / areaAmostral, proj);
        }else{
            return getValueCorrigidoUmidade((sum * 10000f / areaAmostral)/1000, proj);
        }
    }

    private float getValueCorrigidoUmidade(float value, Project proj){
        float umidade = proj.getUmidade();
        float umidadeCoop = proj.getUmidadeCoop();

        return value * ((100-umidade)/(100-umidadeCoop));
    }

    private void setData(List<SpreadsheetValues> spreadsheetValuesList, Project proj) {

        if ( spreadsheetValuesList.isEmpty()){
            Intent returnIntent = new Intent();
            setResult(Constants.RESULT_NO_DATA, returnIntent);
            finish();
        }

        HashMap<String, ArrayList<Float>> hashValues = new HashMap<>();

        hashValues = getHashValuesSpreadsheet(spreadsheetValuesList);

        ArrayList<String> arrayProductName = new ArrayList<>();
        ArrayList<Float> barValues = new ArrayList<>();


        for(Map.Entry<String, ArrayList<Float>> entry : hashValues.entrySet()) {
            String key = entry.getKey();
            ArrayList<Float> listValues = entry.getValue();


            float barVal = getBarValue(listValues, proj);
            barValues.add((float)barVal);

            arrayProductName.add(key);
        }


        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart,arrayProductName);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        float start = 1f;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = (int) start; i < barValues.size()+1; i++) {
            float val2 = barValues.get(i-1);

                yVals1.add(new BarEntry(i, val2));

        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Variáveis");

            set1.setDrawIcons(false);

            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);

            mChart.setData(data);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        }
    }
    public HashMap<String,ArrayList<Float>> getHashValuesSpreadsheet(List<SpreadsheetValues> spreadsheetValuesList){
        String currentId = "";
        HashMap<String,ArrayList<Float>> hashValues = new HashMap<>();
        listOfValues.clear();
        for ( int i = 0; i < spreadsheetValuesList.size(); i++){
            SpreadsheetValues spValue = spreadsheetValuesList.get(i);

            if ( i == 0){
                currentId = spValue.getProduct();
            }

            if (!currentId.equals(spValue.getProduct())){
                hashValues.put(currentId,(ArrayList<Float>) listOfValues.clone());
                currentId = spValue.getProduct();
                listOfValues.clear();
            }

            listOfValues.add(spValue.getValue());

            if ( i == (spreadsheetValuesList.size()-1)){
                hashValues.put(currentId,(ArrayList<Float>)listOfValues.clone());
            }

        }

        return hashValues;

    }


    protected RectF mOnValueSelectedRectF = new RectF();

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;

        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = mChart.getPosition(e, AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + mChart.getLowestVisibleX() + ", high: "
                        + mChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() { }
}
