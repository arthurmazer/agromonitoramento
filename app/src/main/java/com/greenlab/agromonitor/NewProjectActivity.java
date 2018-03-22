package com.greenlab.agromonitor;


import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.greenlab.agromonitor.entity.Project;
import com.greenlab.agromonitor.utils.Constants;

public class NewProjectActivity extends BaseActivity {

    RadioButton radioCana;
    RadioButton radioSoja;
    EditText nameProject;
    RadioButton radioDiurno;
    RadioButton radioNoturno;
    RadioGroup radioGroupCultura;
    TextView btnNext;
    Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_project_first_step);
        setTitle(getResources().getString(R.string.title_step1));

        project = new Project();

        nameProject = findViewById(R.id.name_project);
        radioCana = findViewById(R.id.radio_cana_de_acucar);
        radioSoja = findViewById(R.id.radio_soja);
        radioDiurno = findViewById(R.id.radio_turno_diurno);
        radioNoturno = findViewById(R.id.radio_turno_noturno);
        radioGroupCultura = findViewById(R.id.radiogroup_culture);
        btnNext = findViewById(R.id.btn_next_to_step2);

        nameProject.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        Bundle extras = getIntent().getExtras();
        project = new Project();

        if (extras != null) {
            project = extras.getParcelable(Constants.REGISTRATION_EXTRA_PROJECT);
            setView();
        }


        radioGroupCultura.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == radioCana.getId()){
                    radioNoturno.setEnabled(false);
                    radioDiurno.setChecked(true);
                }else{
                    if ( i == radioSoja.getId()){
                        radioNoturno.setEnabled(true);
                    }
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataFromStep1();
                Intent it = new Intent(getApplicationContext() , NewProjectActivityStep2.class);
                it.putExtra(Constants.REGISTRATION_EXTRA_PROJECT, project);
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(it);
                overridePendingTransition(R.anim.slide_in_foward, R.anim.slide_out_forward);
            }
        });

    }


    public void getDataFromStep1(){
        if (nameProject.getText().toString().isEmpty())
            project.setProjectName("Novo Projeto");
        else
            project.setProjectName(nameProject.getText().toString());
        if (radioCana.isChecked())
            project.setCultureType(Constants.PROJECT_TYPE_CANA_DE_ACUCAR);
        else
            project.setCultureType(Constants.PROJECT_TYPE_SOJA);

        if(radioDiurno.isChecked())
            project.setTurn(Constants.TURNO_DIURNO);
        else
            project.setTurn(Constants.TURNO_NOTURNO);

    }

    public void setView(){
        if (project.getProjectName()!= null && !project.getProjectName().isEmpty() ){
            nameProject.setText(project.getProjectName());
        }
        if ( project.getCultureType() == Constants.PROJECT_TYPE_CANA_DE_ACUCAR ){
            radioCana.setChecked(true);
            radioNoturno.setEnabled(false);
            radioDiurno.setChecked(true);
        }else if ( project.getCultureType() == Constants.PROJECT_TYPE_SOJA){
            radioSoja.setChecked(true);
            radioNoturno.setEnabled(true);
        }
        if ( project.getTurn() == Constants.TURNO_DIURNO ){
            radioDiurno.setChecked(true);
        }else if ( project.getCultureType() == Constants.TURNO_NOTURNO){
            radioNoturno.setEnabled(true);
            radioNoturno.setChecked(true);
        }

    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(getApplicationContext() , HomeActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
        super.onBackPressed();
    }



}
