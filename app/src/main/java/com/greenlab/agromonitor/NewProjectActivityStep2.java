package com.greenlab.agromonitor;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.greenlab.agromonitor.entity.Project;
import com.greenlab.agromonitor.utils.Constants;

public class NewProjectActivityStep2 extends BaseActivity {

    Project project;

    TextView btnBackToStep1;
    TextView btnNextToStep3;
    EditText farmName;
    EditText operatorName;
    EditText measurer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project_step2);
        setTitle(getResources().getString(R.string.title_step2));

        btnBackToStep1 = findViewById(R.id.btn_back_to_step1);
        btnNextToStep3 = findViewById(R.id.btn_next_to_step3);
        farmName = findViewById(R.id.text_farm_name);
        operatorName = findViewById(R.id.text_operator_name);
        measurer = findViewById(R.id.text_measurer_name);


        farmName.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        operatorName.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        measurer.setFilters(new InputFilter[] {new InputFilter.AllCaps()});


        Bundle extras = getIntent().getExtras();
        project = new Project();

        if (extras != null) {
            project = extras.getParcelable(Constants.REGISTRATION_EXTRA_PROJECT);
            setView();
        }

        btnBackToStep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToStep1();
            }
        });

        btnNextToStep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataFromStep2();
                Intent it = new Intent(getApplicationContext() , NewProjectActivityStep3.class);
                it.putExtra(Constants.REGISTRATION_EXTRA_PROJECT, project);
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(it);
                overridePendingTransition(R.anim.slide_in_foward, R.anim.slide_out_forward);
            }
        });
    }

    public void getDataFromStep2(){
        project.setFarmName(farmName.getText().toString());
        project.setOperatorsName(operatorName.getText().toString());
        project.setMeasurersName(measurer.getText().toString());
    }


    @Override
    public void onBackPressed() {
        backToStep1();
    }

    public void backToStep1(){
        getDataFromStep2();
        Intent it = new Intent(getApplicationContext() , NewProjectActivity.class);
        it.putExtra(Constants.REGISTRATION_EXTRA_PROJECT, project);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
        overridePendingTransition(R.anim.slide_in_backward, R.anim.slide_out_backward);
    }

    public void setView(){
        if (project.getFarmName() != null && !project.getFarmName().isEmpty()){
            farmName.setText(project.getFarmName());

        }
        if (project.getOperatorsName() != null && !project.getOperatorsName().isEmpty()){
            operatorName.setText(project.getOperatorsName());

        }
        if (project.getMeasurersName() != null && !project.getMeasurersName().isEmpty()){
            measurer.setText(project.getMeasurersName());

        }
    }

}
