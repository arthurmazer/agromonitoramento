package com.greenlab.agromonitor.charts;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

/**
 * Created by philipp on 02/06/16.
 */
public class DayAxisValueFormatter implements IAxisValueFormatter
{


    private ArrayList<String> barLabels = new ArrayList<>();

    private BarLineChartBase<?> chart;

    public DayAxisValueFormatter(BarLineChartBase<?> chart, ArrayList<String> barLabels) {
        this.chart = chart;
        this.barLabels = barLabels;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int index = (int) value;
        String retStr="";
        if (index <= barLabels.size()){
            retStr = barLabels.get(index-1);
        }
        return retStr;
    }


}
