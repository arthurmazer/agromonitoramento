package com.greenlab.agromonitor.utils;

/**
 * Created by arthu on 09/02/2018.
 */

public class Constants {

    public static final String DB_NAME = "agromonitor_database";
    public static final int DATABASE_VERSION = 11;

    public static final String SHARED_PREF = "agromonitor_sharedPref";
    public static final String SP_IS_LOGGED_IN = "sp_isloggedin";
    public static final String SP_USER_LOGIN = "sp_login";
    public static final String SP_USER_ID = "sp_id";
    public static final String SP_PROJECT_OPEN = "sp_project_open";
    public static final String SP_NAME_PROJECT_OPEN = "sp_name_project_open";
    public static final String SP_PROJECT_INDEX = "sp_project_open_index";
    public static final String SP_USER_LIST_PROJECTS = "sp_list_projects";
    public static final String SP_TYPE_PROJECT = "sp_type_project";
    public static final String SP_AREA_AMOSTRAL_PROJECT = "sp_area_amostral";
    public static final String SP_CULTURE_TYPE = "sp_culture_type";
    public static final String SP_USER_EMAIL = "sp_user_email";
    public static final String SP_UNITY_PROJECT = "sp_user_email";

    public static final String REGISTRATION_EXTRA_PROJECT = "project";

    public static final int REQUEST_CODE_NEW_PROJECT = 100;
    public static final int RESULT_NEW_PROJECT = 101;
    public static final int RESULT_NO_DATA = 404;
    public static final int OPEN_CHART = 102;

    public static final int PROJECT_TYPE_CANA_DE_ACUCAR = 0;
    public static final int PROJECT_TYPE_SOJA = 1;
    public static final int PROJECT_TYPE_MILHO = 2;
    public static final int PROJECT_TYPE_CAFE = 3;
    public static final int PROJECT_TYPE_AMENDOIM = 4;
    public static final int PROJECT_TYPE_ALGODAO = 5;


    public static final int KILO = 0;
    public static final int GRAMA = 1;

    public static final int TURNO_DIURNO = 0;
    public static final int TURNO_NOTURNO = 1;


}
