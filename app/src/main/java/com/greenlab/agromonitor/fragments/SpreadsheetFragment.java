package com.greenlab.agromonitor.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greenlab.agromonitor.HomeActivity;
import com.greenlab.agromonitor.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpreadsheetFragment extends Fragment {

    private HomeActivity mActivity;

    public static Fragment newInstance(){
        Fragment spreadsheetFragment = new SpreadsheetFragment();
        return spreadsheetFragment;
    }

    public SpreadsheetFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.mActivity = (HomeActivity)getActivity();

        mActivity.displayErrorMessage();
        return inflater.inflate(R.layout.fragment_spreadsheet, container, false);
    }

}
