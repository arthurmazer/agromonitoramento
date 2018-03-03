package com.greenlab.agromonitor.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.greenlab.agromonitor.HomeActivity;
import com.greenlab.agromonitor.NewProjectActivity;
import com.greenlab.agromonitor.R;
import com.greenlab.agromonitor.adapters.ProjectListAdapter;
import com.greenlab.agromonitor.entity.Project;
import com.greenlab.agromonitor.entity.User;
import com.greenlab.agromonitor.utils.Constants;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private Button btnNewProject;
    private static int REQUEST_CODE_ITEMS = 3000;
    User user;
    ProjectListAdapter projectListAdapter;
    HomeActivity mActivity;
    List<Project> projectList;
    RecyclerView recyclerProjects;

    public static Fragment newInstance(){
        Fragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    public HomeFragment() {
        projectList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.empty_home, container, false);


        mActivity = (HomeActivity)getActivity();
        recyclerProjects = view.findViewById(R.id.recycler_projects);
        btnNewProject = view.findViewById(R.id.btn_new_project);
        btnNewProject.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

             Intent it = new Intent(getActivity(), NewProjectActivity.class);
             getActivity().startActivityForResult(it, Constants.REQUEST_CODE_NEW_PROJECT);
            }
        });

        projectListAdapter = new ProjectListAdapter(projectList, mActivity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        recyclerProjects.setLayoutManager(mLayoutManager);
        recyclerProjects.setItemAnimator(new DefaultItemAnimator());
        recyclerProjects.setAdapter(projectListAdapter);


        user = mActivity.getSessionUser();
        projectList.clear();

        int idProject = mActivity.getOpenedProject();
        for (Project project : mActivity.getProjectList()){
            if (project.getId()==idProject){
                project.setOpened(true);
                projectList.add(0,project);
            }else{
                project.setOpened(false);
                projectList.add(project);
            }

        }

        //projectList.addAll(mActivity.getProjectList());
        projectListAdapter.notifyDataSetChanged();

        return view;

    }




}
