package com.greenlab.agromonitor;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.greenlab.agromonitor.entity.Project;
import com.greenlab.agromonitor.entity.User;
import com.greenlab.agromonitor.fragments.HomeFragment;
import com.greenlab.agromonitor.fragments.ReportFragment;
import com.greenlab.agromonitor.fragments.SpreadsheetFragment;
import com.greenlab.agromonitor.interfaces.GetAllProjectsOfUser;
import com.greenlab.agromonitor.managers.SessionManager;
import com.greenlab.agromonitor.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements GetAllProjectsOfUser {

    private BottomNavigationView bottomNavigationView;
    private User user;
    private List<Project> projectList;
    Button btnNewProject;
    public boolean isNewProject = false;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setTitle(R.string.title_home);
                    pushFragment(HomeFragment.newInstance());
                    return true;
                case R.id.navigation_spreadsheet:
                    setTitle(R.string.title_spreadsheet);
                    pushFragment(SpreadsheetFragment.newInstance());
                    return true;
                case R.id.navigation_reports:
                    setTitle(R.string.title_reports);
                    pushFragment(ReportFragment.newInstance());
                    return true;

            }
            return false;
        }
    };

    /**
     * Method to push any fragment into given id.
     *
     * @param fragment An instance of Fragment to show into the given id.
     */
    protected void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.root_layout, fragment);
                ft.commit();
            }
        }
    }

    public void displayErrorMessage(){
        AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
        alertDialog.setTitle("Alerta");
        alertDialog.setMessage("Você ainda não tem um projeto em aberto");
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                    }
                });
        alertDialog.show();
    }

    public void changeToSpreadsheetScreen(){
        bottomNavigationView.setSelectedItemId(R.id.navigation_spreadsheet);
    }

    public void changeToHomeScreen(){
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        setTitle(R.string.title_home);
        pushFragment(HomeFragment.newInstance());
    }

    public void changeToReportScreen(){
        bottomNavigationView.setSelectedItemId(R.id.navigation_reports);
        setTitle(R.string.title_reports);
        pushFragment(ReportFragment.newInstance());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle(R.string.title_home);
        projectList = new ArrayList<>();
        btnNewProject = findViewById(R.id.btn_new_project_home);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            Boolean isSaved = extras.getBoolean("success", false);
            long idProject = extras.getLong("id_project", 0);
            String nameProject = extras.getString("name_project");
            if (isSaved) {
                showSnackBar(getResources().getString(R.string.project_save_success));
                setProjectOpened((int)idProject,nameProject);
                isNewProject = true;
            }
            else {
                showSnackBar(getResources().getString(R.string.project_save_error));
            }
        }


        if ( isNewProject )
            changeToSpreadsheetScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //pushFragment(HomeFragment.newInstance());
        user = getSessionUser();
        user.getListOfProjects(getApplicationContext(), HomeActivity.this);
        
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSuccessGettingProjects(List<Project> projectList) {
        this.projectList = projectList;

        if ( !projectList.isEmpty() ){
            btnNewProject.setVisibility(View.GONE);
            if (!isNewProject)
                pushFragment(HomeFragment.newInstance());
        }else{
            btnNewProject.setVisibility(View.VISIBLE);
            TapTargetView.showFor(this,
                    TapTarget.forView(findViewById(R.id.btn_new_project_home),  //the view
                            getResources().getString(R.string.title_onboard_no_project), "") //title and description
                            // All options below are optional
                            .outerCircleAlpha(0.42f)            // Specify the alpha amount for the outer circle
                            .targetCircleColor(R.color.white)   // Specify a color for the target circle
                            .titleTextSize(18)                  // Specify the size (in sp) of the title text
                            .titleTextColor(R.color.white)      // Specify the color of the title text
                            .descriptionTextSize(12)            // Specify the size (in sp) of the description text
                            .descriptionTextColor(R.color.light_grey)  // Specify the color of the description text
                            .textColor(R.color.white)            // Specify a color for both the title and description text
                            .textTypeface(Typeface.SERIF)  // Specify a typeface for the text
                            .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                            .drawShadow(true)                   // Whether to draw a drop shadow or not
                            .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                            .tintTarget(true)                   // Whether to tint the target view's color
                            .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)// Specify a custom drawable to draw as the target
                            .targetRadius(60),                  // Specify the target radius (in dp)
                    new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);
                            //btnNewProject.performClick();
                            Intent it = new Intent(getApplicationContext(), NewProjectActivity.class);
                            startActivityForResult(it, Constants.REQUEST_CODE_NEW_PROJECT);
                        }
                    });
        }

        if (bottomNavigationView.getSelectedItemId() == R.id.navigation_reports)
            changeToReportScreen();
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    @Override
    public void onBackPressed() {
        if (bottomNavigationView.getSelectedItemId() != R.id.navigation_home)
            changeToHomeScreen();
        else
            super.onBackPressed();
    }

    public void setProjectOpened(int idProject, String nameProject){
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.setCurrentProject(idProject,nameProject);
    }

    public String getNameProjectOpened(){
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        return sessionManager.getNameCurrentProject();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        // Check which request we're responding to
        if (requestCode == Constants.OPEN_CHART) {
            // Make sure the request was successful
            if (resultCode == Constants.RESULT_NO_DATA) {
                showSnackBar("Não há dados inserido na planilha");
            }
        }
    }

}
