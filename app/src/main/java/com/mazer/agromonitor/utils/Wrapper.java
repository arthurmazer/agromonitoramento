package com.mazer.agromonitor.utils;

import com.mazer.agromonitor.entity.Project;
import com.mazer.agromonitor.entity.SpreadsheetValues;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazer on 4/28/2018.
 */

public class Wrapper {

    List<SpreadsheetValues> spreadsheetValuesList;
    Project project;

    public Wrapper(){
        spreadsheetValuesList = new ArrayList<>();
        project = new Project();
    }

    public List<SpreadsheetValues> getSpreadsheetValuesList() {
        return spreadsheetValuesList;
    }

    public void setSpreadsheetValuesList(List<SpreadsheetValues> spreadsheetValuesList) {
        this.spreadsheetValuesList = spreadsheetValuesList;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
