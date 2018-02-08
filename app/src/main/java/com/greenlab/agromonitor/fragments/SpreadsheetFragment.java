package com.greenlab.agromonitor.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greenlab.agromonitor.HomeActivity;
import com.greenlab.agromonitor.R;
import com.greenlab.agromonitor.adapters.SpreadsheetAdapter;
import com.greenlab.agromonitor.entity.Product;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpreadsheetFragment extends Fragment {

    private HomeActivity mActivity;
    private ArrayList<Object> spreadsheetList = new ArrayList<Object>();
    SpreadsheetAdapter spreadsheetAdapter;
    RecyclerView recyclerSpreadsheet;


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

        View mView = inflater.inflate(R.layout.fragment_spreadsheet, container, false);
        final int PRODUCT_VALUE = 0, CATEGORY = 1;

        recyclerSpreadsheet = (RecyclerView) mView.findViewById(R.id.recycler_spreadsheet);
        spreadsheetAdapter = new SpreadsheetAdapter(spreadsheetList);
        recyclerSpreadsheet.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(this.mActivity, 6);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Log.d("carai", "> " + spreadsheetAdapter.getItemViewType(position));

                switch(spreadsheetAdapter.getItemViewType(position)){
                    case PRODUCT_VALUE:
                        return 1;
                    case CATEGORY:
                        return 6;
                    default:
                        return 1;
                }
                }


        });




        recyclerSpreadsheet.setLayoutManager(manager);

       // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.mActivity.getApplicationContext());
        //recyclerSpreadsheet.setLayoutManager(mLayoutManager);
        recyclerSpreadsheet.setItemAnimator(new DefaultItemAnimator());
        recyclerSpreadsheet.setAdapter(spreadsheetAdapter);


        for (int i = 0; i < 40; i++){
            if ( i == 0){
                spreadsheetList.add("PT");
            }else{
                if ( i%15 == 0){
                    spreadsheetList.add("XXX");
                }else{
                    Product p = new Product(1, "title", Float.parseFloat("1.677"));
                    spreadsheetList.add(p);
                }
            }


        }
        spreadsheetAdapter.notifyDataSetChanged();

        //annalsManager.getAnais(AnnalsFragment.this);



        //mActivity.displayErrorMessage();
        return mView;
    }

}
