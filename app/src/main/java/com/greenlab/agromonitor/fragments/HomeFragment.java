package com.greenlab.agromonitor.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.greenlab.agromonitor.BaseActivity;
import com.greenlab.agromonitor.HomeActivity;
import com.greenlab.agromonitor.NewProjectActivity;
import com.greenlab.agromonitor.R;
import com.greenlab.agromonitor.entity.Project;
import com.greenlab.agromonitor.entity.User;
import com.greenlab.agromonitor.utils.Constants;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private Button btnNewProject;
    private static int REQUEST_CODE_ITEMS = 3000;
    User user;
    HomeActivity mActivity;

    public static Fragment newInstance(){
        Fragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.empty_home, container, false);

        mActivity = (HomeActivity)getActivity();
        btnNewProject = view.findViewById(R.id.btn_new_project);
        btnNewProject.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

             Intent it = new Intent(getActivity(), NewProjectActivity.class);
             getActivity().startActivityForResult(it, Constants.REQUEST_CODE_NEW_PROJECT);
            }
        });
        User user = mActivity.getSessionUser();


        LoadProjects loadProjects = new LoadProjects(user);
        loadProjects.execute((Void) null);
        Log.d("aqui", "chamo");
        TapTargetView.showFor(mActivity,                 // `this` is an Activity
                TapTarget.forView(view.findViewById(R.id.btn_new_project), getResources().getString(R.string.title_onboard_no_project), getResources().getString(R.string.text_onboard_no_project))
                        // All options below are optional
                        .outerCircleAlpha(0.42f)            // Specify the alpha amount for the outer circle
                        .targetCircleColor(R.color.white)   // Specify a color for the target circle
                        .titleTextSize(22)                  // Specify the size (in sp) of the title text
                        .titleTextColor(R.color.white)      // Specify the color of the title text
                        .descriptionTextSize(12)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(R.color.light_grey)  // Specify the color of the description text
                        .textColor(R.color.black)            // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
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
                        btnNewProject.performClick();

                    }
                });



        return view;

    }

    public class LoadProjects extends AsyncTask<Void, Void, List<Project>>{

        User user;


        LoadProjects(User user) {
            this.user = user;
        }

        @Override
        protected List<Project> doInBackground(Void... params) {
           // Log.d("save-project", "doing on background");
            return user.getListOfProjects(mActivity.getApplicationContext());

        }

        @Override
        protected void onPreExecute() {
            //Log.d("save-project", "pre-executing...");
        }

        @Override
        protected void onPostExecute(List<Project> projectList) {
            //mAuthTask = null;
            //showProgress(false);
            //Log.d("save-project", "finished " + idProject);
            for(Project proj: projectList){
                Log.d("aqui-project: ", proj.getProjectName());
            }
        }

    }


}
