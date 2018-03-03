package com.greenlab.agromonitor.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
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
import com.greenlab.agromonitor.entity.Project;
import com.greenlab.agromonitor.entity.SpreadsheetValues;
import com.greenlab.agromonitor.interfaces.GetSpreadsheetValues;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpreadsheetFragment extends Fragment implements GetSpreadsheetValues {

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
        recyclerSpreadsheet.setItemAnimator(new DefaultItemAnimator());
        recyclerSpreadsheet.setAdapter(spreadsheetAdapter);


        int idProject = mActivity.getOpenedProject();

        final Project project = new Project();
        project.setId(idProject);
        new AsyncTask<Void, Void, List<SpreadsheetValues>>() {
            @Override
            protected List<SpreadsheetValues> doInBackground(Void... voids) {
                return project.getSpreadSheetValues(mActivity.getApplicationContext() );
            }
            @Override
            protected void onPostExecute(List<SpreadsheetValues> spreadsheetValuesList) {
                onSuccessGettingValues(spreadsheetValuesList);
            }
        }.execute();

        return mView;
    }

    private void onSuccessGettingValues(List<SpreadsheetValues> spreadsheetValuesList) {
        String currentProduct = "";
        spreadsheetList.clear();
         for ( SpreadsheetValues spreadsheetValues: spreadsheetValuesList){
             if (!spreadsheetValues.getProduct().equals(currentProduct)){
                 currentProduct = spreadsheetValues.getProduct();
                 Product product = new Product();
                 product.setId(spreadsheetValues.getId());
                 product.setIdProject(spreadsheetValues.getIdProject());
                 product.setProduct(spreadsheetValues.getProduct());
                 spreadsheetList.add(product);
             }
             if ( spreadsheetValues.getValue() > 0)
                spreadsheetList.add(spreadsheetValues);
        }
        if ( spreadsheetList.isEmpty() ){
            int indexProject = mActivity.getIndexOpenedProject();
            List<Product> productList = this.mActivity.getProjectList().get(indexProject).getListOfProducts();
            spreadsheetList.addAll(productList);
        }
        spreadsheetAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessGettingSpreadsheet(List<SpreadsheetValues> spreadsheetValuesList) {

    }
}
