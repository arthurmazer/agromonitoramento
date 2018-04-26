package com.greenlab.agromonitor.fragments;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.greenlab.agromonitor.HomeActivity;
import com.greenlab.agromonitor.R;
import com.greenlab.agromonitor.adapters.SpreadsheetAdapter;
import com.greenlab.agromonitor.entity.Product;
import com.greenlab.agromonitor.entity.Project;
import com.greenlab.agromonitor.entity.SpreadsheetValues;
import com.greenlab.agromonitor.interfaces.GetSpreadsheetValues;
import com.greenlab.agromonitor.managers.SessionManager;

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
    EditText areaAmostral;


    public static Fragment newInstance(){
        Fragment spreadsheetFragment = new SpreadsheetFragment();
        return spreadsheetFragment;
    }

    public SpreadsheetFragment() {
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.mActivity = (HomeActivity)getActivity();
        this.mActivity.setTitle("Planilha - " + this.mActivity.getNameProjectOpened());

        final View mView = inflater.inflate(R.layout.fragment_spreadsheet, container, false);
        final int PRODUCT_VALUE = 0, CATEGORY = 1;

        Spinner spinnerUnity = mView.findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.mActivity,
                R.array.unity_choice, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnity.setAdapter(adapter);

        areaAmostral = mView.findViewById(R.id.area_amostral);
        recyclerSpreadsheet = mView.findViewById(R.id.recycler_spreadsheet);
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

        int areaAm = mActivity.getAreaAmostralProject();
        areaAmostral.setText(""+areaAm);

        areaAmostral.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                final String num = editable.toString();
                if (!num.isEmpty()) {
                    int areaAmostral = Integer.valueOf(num);
                    SessionManager sessionManager = new SessionManager(mView.getContext());
                    sessionManager.setProjectAreaAmostral(areaAmostral);
                }
            }
        });

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
        spreadsheetAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessGettingSpreadsheet(List<SpreadsheetValues> spreadsheetValuesList) {

    }
}
