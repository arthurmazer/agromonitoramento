package com.greenlab.agromonitor.interfaces;

import com.greenlab.agromonitor.entity.SpreadsheetValues;

import java.util.List;

/**
 * Created by arthu on 02/03/2018.
 */

public interface GetSpreadsheetValues {
    void onSuccessGettingSpreadsheet(List<SpreadsheetValues> spreadsheetValuesList);
}
