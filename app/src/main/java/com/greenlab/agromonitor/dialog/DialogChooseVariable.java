package com.greenlab.agromonitor.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Window;

import com.greenlab.agromonitor.R;
import com.greenlab.agromonitor.adapters.ChooseVariableAdapter;
import com.greenlab.agromonitor.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class DialogChooseVariable extends Dialog {

    private List<Product> variableList;
    private RecyclerView recyclerVaribles;
    private ChooseVariableAdapter chooseVariableAdapter;
    private Context ctx;
    private Activity activity;

    public DialogChooseVariable(@NonNull Context context, List<Product> variableList, Activity activity) {
        super(context);
        this.ctx = context;
        this.variableList = new ArrayList<>();
        this.variableList = variableList;
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_choose_variable);

        recyclerVaribles = findViewById(R.id.recycler_variables);


        chooseVariableAdapter = new ChooseVariableAdapter(variableList,activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ctx);
        recyclerVaribles.setLayoutManager(mLayoutManager);
        recyclerVaribles.setItemAnimator(new DefaultItemAnimator());
        recyclerVaribles.setAdapter(chooseVariableAdapter);

        chooseVariableAdapter.notifyDataSetChanged();

    }

    public void setAllVariables(List<Product> variableList){
        if ( this.variableList != null && variableList != null) {
            this.variableList.addAll(variableList);

        }
    }

}
