package com.mazer.agromonitor.interfaces;

import com.mazer.agromonitor.entity.SpreadsheetValues;

import java.util.List;

/**
 * Created by arthu on 02/03/2018.
 */

public interface GetSpreadsheetValues {
    void onSuccessGettingSpreadsheet(List<SpreadsheetValues> spreadsheetValuesList);
}
