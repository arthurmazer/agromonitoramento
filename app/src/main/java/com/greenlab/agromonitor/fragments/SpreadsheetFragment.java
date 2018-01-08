package com.greenlab.agromonitor.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greenlab.agromonitor.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpreadsheetFragment extends Fragment {


    public SpreadsheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spreadsheet, container, false);
    }

}
