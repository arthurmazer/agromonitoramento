package com.mazer.agromonitor;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mazer.agromonitor.entity.Project;
import com.mazer.agromonitor.utils.Constants;

public class NewProjectActivityStep3 extends BaseActivity {

    Project project;

    EditText talhaoName;
    EditText machineID;
    EditText frenteColheita;

    TextView frenteColheitaLabel;
    TextView btnBackToStep2;
    TextView btnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project_step3);
        setTitle(getResources().getString(R.string.title_step3));

        talhaoName = findViewById(R.id.text_talhao_name);
        machineID = findViewById(R.id.text_machine_id);
        frenteColheita = findViewById(R.id.text_frente_colheita);
        frenteColheitaLabel = findViewById(R.id.informe_frente_name_label);
        btnBackToStep2 = findViewById(R.id.btn_back_to_step2);
        btnNext = findViewById(R.id.btn_next_to_step4);

        talhaoName.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        frenteColheita.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        Bundle extras = getIntent().getExtras();
        project = new Project();

        if (extras != null) {
            project = extras.getParcelable(Constants.REGISTRATION_EXTRA_PROJECT);
            setView();
        }

        btnBackToStep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToStep2();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataFromStep3();
                Intent it = new Intent(getApplicationContext() , CreateVariables.class);
                it.putExtra(Constants.REGISTRATION_EXTRA_PROJECT, project);

                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(it);
                overridePendingTransition(R.anim.slide_in_foward, R.anim.slide_out_forward);
            }
        });


    }

    public void getDataFromStep3(){
        project.setTalhao(talhaoName.getText().toString());
        project.setMachineID(machineID.getText().toString());
        project.setFrenteColheita(frenteColheita.getText().toString());
    }

    @Override
    public void onBackPressed() {
        backToStep2();
    }

    public void backToStep2(){
        getDataFromStep3();
        Intent it = new Intent(getApplicationContext() , NewProjectActivityStep2.class);
        it.putExtra(Constants.REGISTRATION_EXTRA_PROJECT, project);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
        overridePendingTransition(R.anim.slide_in_backward, R.anim.slide_out_backward);
    }

    public void setView(){
        if (project.getTalhao() != null && !project.getTalhao().isEmpty()){
            talhaoName.setText(project.getTalhao());
        }
        if (project.getMachineID() != null && !project.getMachineID().isEmpty()){
            machineID.setText(project.getMachineID());
        }

        if (project.getCultureType() == Constants.PROJECT_TYPE_SOJA){
            frenteColheitaLabel.setVisibility(View.GONE);
            frenteColheita.setVisibility(View.GONE);
        }else{
            if (project.getFrenteColheita() != null && !project.getFrenteColheita().isEmpty()){
                frenteColheita.setText(project.getFrenteColheita());
            }
        }
    }
}
