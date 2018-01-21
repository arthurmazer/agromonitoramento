package com.greenlab.agromonitor.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.greenlab.agromonitor.HomeActivity;
import com.greenlab.agromonitor.R;


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

                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle(R.string.choose_an_culture);
                builder.setItems(R.array.culture_choice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        });

        return view;

    }


}
