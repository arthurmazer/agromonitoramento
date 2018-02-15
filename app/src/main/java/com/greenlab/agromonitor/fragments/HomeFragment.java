package com.greenlab.agromonitor.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.greenlab.agromonitor.HomeActivity;
import com.greenlab.agromonitor.NewProjectActivity;
import com.greenlab.agromonitor.R;
import com.greenlab.agromonitor.utils.Constants;


public class HomeFragment extends Fragment {


    private Activity mActivity;
    private Button btnNewProject;
    private static int REQUEST_CODE_ITEMS = 3000;

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

        return view;

    }


}
