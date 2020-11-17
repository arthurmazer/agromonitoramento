package com.mazer.agromonitor;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mazer.agromonitor.entity.Project;
import com.mazer.agromonitor.entity.SpreadsheetValues;
import com.mazer.agromonitor.utils.Constants;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

/**
 * Created by arthu on 19/03/2018.
 */

public class LineChartActivity extends BaseActivity  implements
        OnChartGestureListener, OnChartValueSelectedListener {

    private LineChart mChart;
    private ArrayList<Float> listOfValues;
    private String projectName;
    private YAxis leftAxis;
    private TextView textCountVarPoints;

    private FloatingTextButton fab;

    private LimitLine meanLL;
    private LimitLine floorLL;
    private LimitLine ceilLL;
    private LimitLine custom1;
    private LimitLine custom2;
    private float limit_start;
    private float limit_end;
    List<SpreadsheetValues> valuesProject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line_chart_layout);

        listOfValues = new ArrayList<>();
        fab = findViewById(R.id.add_limits_btn);

        Bundle extras = getIntent().getExtras();


        int idProduct = 0;
        if (extras != null) {
            projectName = extras.getString("project_name");
            // and get whatever type user account id is
            idProduct = extras.getInt("id_product");
        }

        textCountVarPoints = findViewById(R.id.text_count_variables);
        mChart = findViewById(R.id.chart1);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        loadValuesFromProject(idProduct);

        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines


                new MaterialDialog.Builder(LineChartActivity.this)
                        .customView(R.layout.layout_choose_limit, true)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                View v = dialog.getCustomView();
                                EditText limitStartEditText = v.findViewById(R.id.limit_start);
                                EditText limitEndEditText = v.findViewById(R.id.limit_end);
                                limit_start = Float.valueOf(limitStartEditText.getText().toString());
                                limit_end = Float.valueOf(limitEndEditText.getText().toString());


                                leftAxis.removeLimitLine(custom1);
                                leftAxis.removeLimitLine(custom2);


                                custom1 = new LimitLine(limit_start, "Lim 1");
                                custom1.setLineWidth(3f);
                                custom1.setLineColor(Color.GREEN);
                                custom1.enableDashedLine(10f, 10f, 0f);
                                custom1.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
                                custom1.setTextSize(10f);

                                custom2 = new LimitLine(limit_end, "Lim 2");
                                custom2.setLineWidth(3f);
                                custom2.enableDashedLine(10f, 10f, 0f);
                                custom2.setLineColor(Color.GREEN);
                                custom2.setTextStyle(Paint.Style.FILL_AND_STROKE);
                                custom2.setTextColor(R.color.blue);
                                custom2.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
                                custom2.setTextSize(10f);


                                leftAxis.addLimitLine(custom1);
                                leftAxis.addLimitLine(custom2);

                                mChart.getData().notifyDataChanged();
                                mChart.notifyDataSetChanged();

                                displayNumberPointsInsideLimit(limit_start,limit_end);
                                setVisibilitTextCountLimit(true);

                                mChart.invalidate();

                                fab.setTitle("Trocar Limites");


                            }
                        })
                        .negativeText("Cancelar")
                        .negativeColor(Color.RED)

                        .positiveText("OK")
                        .show();
            }
        });
    }



    public void setVisibilitTextCountLimit(boolean isVisible){
        if (isVisible)
            this.textCountVarPoints.setVisibility(View.VISIBLE);
        else
            this.textCountVarPoints.setVisibility(View.GONE);
    }

    public void displayNumberPointsInsideLimit(float limit1, float limit2){

        float limitMenor, limitMaior;
        int countPoints = 0;

        if ( limit1 > limit2){
            limitMenor = limit2;
            limitMaior = limit1;
        }else{
            limitMenor = limit1;
            limitMaior = limit2;
        }


        for (SpreadsheetValues sp : valuesProject ){

            if (sp.getValue() >= limitMenor && sp.getValue() <= limitMaior){
                countPoints++;
            }
        }

        float percentage = ((float)countPoints/(float)valuesProject.size())*100;
        updateLabel(countPoints, (int)percentage);

    }

    public void updateLabel(int count, int percentage){
        if ( count == 1 )
            this.textCountVarPoints.setText(count + " ponto dentro do limite inserido (" + percentage + "%)");
        else
            this.textCountVarPoints.setText(count + " pontos dentro do limite inserido (" + percentage + "%)");
    }

    @SuppressLint("StaticFieldLeak")
    public void loadValuesFromProject(final int idProduct){
        int idProject = getOpenedProject();

        final Project project = new Project();
        project.setId(idProject);
        new AsyncTask<Void, Void, List<SpreadsheetValues>>() {
            @Override
            protected List<SpreadsheetValues> doInBackground(Void... voids) {
                return project.getProductValuesNotNullFromProject(getApplicationContext(),idProduct);
            }
            @Override
            protected void onPostExecute(List<SpreadsheetValues> spreadsheetValuesList) {
                loadProjectAndSetData(spreadsheetValuesList);
            }
        }.execute();

    }


    @SuppressLint("StaticFieldLeak")
    public void loadProjectAndSetData(final List<SpreadsheetValues> spreadsheetValuesList){

        int idProject = getOpenedProject();

        final Project project = new Project();
        project.setId(idProject);

        new AsyncTask<Void, Void, Project>() {
            @Override
            protected Project doInBackground(Void... voids) {
                return project.getActualProject(getApplicationContext());
            }
            @Override
            protected void onPostExecute(Project mProject) {
                setData(mProject, spreadsheetValuesList);
            }
        }.execute();

    }


    public List<SpreadsheetValues> formatValues(Project mProject, List<SpreadsheetValues> spreadsheetValuesList){

        List<SpreadsheetValues> newSPList = new ArrayList<>();


        for (SpreadsheetValues spV : spreadsheetValuesList){

            if (mProject.getCultureType() == Constants.PROJECT_TYPE_CANA_DE_ACUCAR){
                float valorPorHectare = 0;

                //trocando para hectare
                Log.d("graficao", "multiplicando " + spV.getValue() + " * 10000 / " + mProject.getAreaAmostral());
                valorPorHectare = (spV.getValue()*10000)/(float)mProject.getAreaAmostral();
                if (mProject.getMeasureUnity() == Constants.KILO){
                    //cana coletada em Kilo, first, transforma em tonelada
                    Log.d("graficao", "divide por 1000");
                    valorPorHectare = valorPorHectare/1000;
                }else if (mProject.getMeasureUnity() == Constants.GRAMA){
                    Log.d("graficao", "divide por 1000000");
                    valorPorHectare = valorPorHectare/1000000;
                }


                SpreadsheetValues sp;
                sp = spV;
                sp.setValue(valorPorHectare);
                newSPList.add(sp);

            }else{

                float valorPorHectare = 0;
                //trocando para hectare
                Log.d("graficao", "multiplicando " + spV.getValue() + " * 10000 / " + mProject.getAreaAmostral());
                valorPorHectare = (spV.getValue()*10000)/(float)mProject.getAreaAmostral();
                if (mProject.getMeasureUnity() == Constants.GRAMA){
                    Log.d("graficao", "divide por 100");
                    valorPorHectare = valorPorHectare/1000;
                }

                SpreadsheetValues sp;
                sp = spV;
                sp.setValue(valorPorHectare);

                newSPList.add(sp);

            }



        }

        return newSPList;
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

    @SuppressLint("StaticFieldLeak")
    private void setData(Project project, List<SpreadsheetValues> spreadsheetValuesList) {


        valuesProject = formatValues(project,spreadsheetValuesList);
        spreadsheetValuesList = valuesProject;
        //valuesProject = spreadsheetValuesList;

        HashMap<String, ArrayList<Float>> hashValues = new HashMap<>();

        if (spreadsheetValuesList.size() <= 0) {
            Intent returnIntent = new Intent();
            setResult(Constants.RESULT_NO_DATA, returnIntent);
            finish();
        }else {

            DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();

            for (SpreadsheetValues spValues : spreadsheetValuesList) {
                Log.d("valor", "> " + spValues.getValue());
                listOfValues.add(spValues.getValue());
                descriptiveStatistics.addValue(spValues.getValue());
            }

            hashValues = getHashValuesSpreadsheet(spreadsheetValuesList);


            Double stdDeviation = descriptiveStatistics.getStandardDeviation();
            double mean = descriptiveStatistics.getMean();

            float ceilLimit = (float) (mean + 3 * stdDeviation);
            float floorLimit = (float) (mean - 3 * stdDeviation);
            float fMean = (float) mean;

            // x-axis limit line
            LimitLine llXAxis = new LimitLine(10f, "Index 10");
            llXAxis.setLineWidth(4f);
            llXAxis.enableDashedLine(10f, 10f, 0f);
            llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            llXAxis.setTextSize(10f);

            XAxis xAxis = mChart.getXAxis();
            xAxis.enableGridDashedLine(10f, 10f, 0f);
            //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
            //xAxis.addLimitLine(llXAxis); // add x-axis limit line

            //Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

            ceilLL = new LimitLine(ceilLimit, "Limite Superior");
            ceilLL.setLineWidth(4f);
            ceilLL.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ceilLL.setTextSize(10f);
            //ll1.setTypeface(tf);

            if ( floorLimit < 0)
                floorLimit = 0;
            floorLL = new LimitLine(floorLimit, "Limite Inferior");
            floorLL.setLineWidth(4f);
            floorLL.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            floorLL.setTextSize(10f);

            //ll2.setTypeface(tf);

            meanLL = new LimitLine(fMean, "Média");
            meanLL.setLineWidth(4f);
            meanLL.enableDashedLine(10f, 10f, 0f);
            meanLL.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            meanLL.setTextSize(10f);
            meanLL.setLineColor(Color.BLUE);





            leftAxis = mChart.getAxisLeft();
            leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
            leftAxis.addLimitLine(ceilLL);
            leftAxis.addLimitLine(floorLL);
            leftAxis.addLimitLine(meanLL);

            leftAxis.setAxisMaximum(ceilLimit + 10);
            leftAxis.setAxisMinimum(0);
            //leftAxis.setYOffset(20f);
            leftAxis.enableGridDashedLine(10f, 10f, 0f);
            leftAxis.setDrawZeroLine(false);

            // limit lines are drawn behind data (and not on top)
            leftAxis.setDrawLimitLinesBehindData(true);
            mChart.getAxisRight().setEnabled(false);
            mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            mChart.getXAxis().setLabelCount(spreadsheetValuesList.size());
            mChart.getXAxis().setGranularity(1);
            mChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return String.valueOf((int)value);
                }
            });
            mChart.animateX(2500);
            //mChart.invalidate();

            // get the legend (only possible after setting data)
            Legend l = mChart.getLegend();

            // modify the legend ...
            l.setForm(Legend.LegendForm.LINE);


            ArrayList<Entry> values = new ArrayList<Entry>();


            ArrayList<ArrayList<Entry>> arrayEntry = new ArrayList<>();
            ArrayList<String> arrayProductName = new ArrayList<>();
            for(Map.Entry<String, ArrayList<Float>> entry : hashValues.entrySet()) {
                String key = entry.getKey();
                ArrayList<Float> listValues = entry.getValue();

                ArrayList<Entry> yVals1 = new ArrayList<>();

                for (int i = 0; i < listValues.size(); i++) {
                    yVals1.add(new Entry(i, listValues.get(i)));
                }

                arrayEntry.add(yVals1);
                arrayProductName.add(key);
            }

            LineDataSet set1, set2 = null, set3 = null;

            if (mChart.getData() != null &&
                    mChart.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                set1.setValues(values);
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
            } else {
                // create a dataset and give it a type
                /**   if ( index == 0)
                 set1 = new LineDataSet(values, "Dados " + "1");
                 else{

                 set1 = new LineDataSet(values, "Dados " + "2");
                 }

                 set1.setDrawIcons(false);

                 // set the line to be drawn like this "- - - - - -"
                 set1.enableDashedLine(10f, 5f, 0f);
                 set1.enableDashedHighlightLine(10f, 5f, 0f);
                 if( index == 0) {
                 set1.setColor(Color.BLACK);
                 set1.setCircleColor(Color.BLACK);
                 }else{
                 set1.setColor(Color.BLUE);
                 set1.setCircleColor(Color.BLUE);
                 }
                 set1.setLineWidth(1f);
                 set1.setCircleRadius(3f);
                 set1.setDrawCircleHole(false);
                 set1.setValueTextSize(9f);
                 set1.setDrawFilled(true);
                 set1.setFormLineWidth(1f);
                 set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                 set1.setFormSize(15.f);




                 dataSets.add(set1); add the datasets**/

                if (arrayEntry.isEmpty()) {
                    showSnackBar("Nenhum valor adicionado a planilha");
                    finish();
                }

                set1 = new LineDataSet(arrayEntry.get(0), arrayProductName.get(0));

                set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                set1.setColor(Color.DKGRAY);
                set1.setCircleColor(Color.BLACK);
                set1.setLineWidth(2f);
                set1.setCircleRadius(3f);
                set1.setFillAlpha(65);
                set1.setFillColor(ColorTemplate.getHoloBlue());
                set1.setHighLightColor(Color.rgb(244, 117, 117));
                set1.setDrawCircleHole(false);
                //set1.setFillFormatter(new MyFillFormatter(0f));
                //set1.setDrawHorizontalHighlightIndicator(false);
                //set1.setVisible(false);
                //set1.setCircleHoleColor(Color.WHITE);

                // create a dataset and give it a type
                if (arrayEntry.size() > 1) {
                    set2 = new LineDataSet(arrayEntry.get(1), arrayProductName.get(1));
                    set2.setAxisDependency(YAxis.AxisDependency.LEFT);
                    set2.setColor(Color.RED);
                    set2.setCircleColor(Color.BLACK);
                    set2.setLineWidth(2f);
                    set2.setCircleRadius(3f);
                    set2.setFillAlpha(65);
                    set2.setFillColor(Color.RED);
                    set2.setDrawCircleHole(false);
                    set2.setHighLightColor(Color.rgb(244, 117, 117));
                    //set2.setFillFormatter(new MyFillFormatter(900f));
                }

                if ( arrayEntry.size() >  2) {
                    set3 = new LineDataSet(arrayEntry.get(2), arrayProductName.get(2));
                    set3.setAxisDependency(YAxis.AxisDependency.LEFT);
                    set3.setColor(Color.YELLOW);
                    set3.setCircleColor(Color.BLACK);
                    set3.setLineWidth(2f);
                    set3.setCircleRadius(3f);
                    set3.setFillAlpha(65);
                    set3.setFillColor(ColorTemplate.colorWithAlpha(Color.YELLOW, 200));
                    set3.setDrawCircleHole(false);
                    set3.setHighLightColor(Color.rgb(244, 117, 117));
                }
                // create a data object with the datasets
                LineData data;
                switch (arrayEntry.size()){
                    case 1:
                        data = new LineData(set1);
                        break;
                    case 2:
                        data = new LineData(set1, set2);
                        break;
                    case 3:
                        data = new LineData(set1, set2, set3);
                        break;
                    default:
                        data = new LineData(set1);
                        break;
                }

                data.setValueTextColor(Color.BLACK);
                data.setValueTextSize(12f);

                // set data
                mChart.setData(data);

            }


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


    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            mChart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
