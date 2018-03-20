package com.greenlab.agromonitor;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.PointerIcon;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.greenlab.agromonitor.adapters.ProductListAdapter;
import com.greenlab.agromonitor.entity.Project;
import com.greenlab.agromonitor.entity.User;
import com.greenlab.agromonitor.managers.SessionManager;
import com.greenlab.agromonitor.managers.UserManager;
import com.greenlab.agromonitor.utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class NewProjectActivity extends BaseActivity {

    UserManager userManager;
    ArrayList<String> listProducts;
    RadioButton radioCana;
    RadioButton radioSoja;
    RecyclerView recyclerProducs;
    ProductListAdapter productListAdapter;
    ImageView btnAddProduct;
    Button btnSaveProject;
    EditText productLabelText;
    EditText nameProject;
    EditText farmName;
    EditText talhaoName;
    EditText frenteColheita;
    EditText machineId;
    EditText operatorsName;
    EditText measurerName;
    RadioButton radioDiurno;
    RadioButton radioNoturno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_project_layout);

        radioCana = findViewById(R.id.radio_cana_de_acucar);
        radioSoja = findViewById(R.id.radio_soja);
        recyclerProducs = findViewById(R.id.recycler_products);
        btnAddProduct = findViewById(R.id.btn_add_products);
        btnSaveProject = findViewById(R.id.btnSaveNewProject);
        productLabelText = findViewById(R.id.add_product_label);
        nameProject = findViewById(R.id.name_project);
        farmName = findViewById(R.id.name_farm);
        talhaoName = findViewById(R.id.name_talhao);
        frenteColheita = findViewById(R.id.frente_colheita);
        machineId = findViewById(R.id.machine_id);
        operatorsName = findViewById(R.id.operators_name);
        measurerName = findViewById(R.id.measurer_name);
        radioDiurno = findViewById(R.id.turno_diurno);
        radioNoturno = findViewById(R.id.turno_noturno);

        listProducts = new ArrayList<>();
        userManager =  new UserManager(getApplicationContext());

        productListAdapter = new ProductListAdapter(listProducts);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerProducs.setLayoutManager(mLayoutManager);
        recyclerProducs.setItemAnimator(new DefaultItemAnimator());
        recyclerProducs.setAdapter(productListAdapter);

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String product = productLabelText.getText().toString();

                if (!product.isEmpty()){
                    listProducts.add(product);
                    productListAdapter.notifyDataSetChanged();
                    productLabelText.setText("");
                }

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(productLabelText.getWindowToken(), 0);
            }
        });

        btnSaveProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkObligatoryFields())
                    saveProject();
            }
        });

        populateFields();

    }

    public void populateFields(){
        this.listProducts.clear();
        this.radioCana.setChecked(true);
        this.radioDiurno.setChecked(true);
    }

    public boolean checkObligatoryFields(){
        if ( listProducts.isEmpty() ){
            String error = getResources().getString(R.string.error_no_products);
            showToast(error);
            return false;
        }
        return true;
    }

    public void saveProject(){
        User user = getSessionUser(); //get user with id and login and list of projects -- password isn't necessary
        Project project = new Project();
        if(nameProject.getText().toString().isEmpty())
            project.setProjectName("Novo Projeto");
        else
            project.setProjectName(nameProject.getText().toString());

        Log.d("save-project-user","> " + user.getId());
        if ( this.radioCana.isChecked())
            project.setCultureType(Constants.PROJECT_TYPE_CANA_DE_ACUCAR);
        else
            project.setCultureType(Constants.PROJECT_TYPE_SOJA);

        if ( this.radioDiurno.isChecked())
            project.setTurn(Constants.TURNO_DIURNO);
        else
            project.setTurn(Constants.TURNO_NOTURNO);

        project.setFarmName(farmName.getText().toString());
        project.setTalhao(talhaoName.getText().toString());
        project.setFrenteColheita(frenteColheita.getText().toString());
        project.setMachineID(machineId.getText().toString());
        project.setOperatorsName(operatorsName.getText().toString());
        project.setMeasurersName(measurerName.getText().toString());

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String dateNow = dateFormat.format(date);

        project.setCreationDate(dateNow);
        project.setIdUser(user.getId());

        ProjectSave projectSave = new ProjectSave(user,project,this.listProducts);
        projectSave.execute((Void) null);

    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class ProjectSave extends AsyncTask<Void, Void, Long> {

        User user;
        Project project;
        ArrayList<String> listOfProducts;


        ProjectSave(User user, Project project, ArrayList<String> listOfProducts) {
            this.user = user;
            this.project = project;
            this.listOfProducts = listOfProducts;
        }

        @Override
        protected Long doInBackground(Void... params) {
            Log.d("save-project", "doing on background");
            return user.saveProject(getApplicationContext(),project,listOfProducts);

        }

        @Override
        protected void onPreExecute() {
            Log.d("save-project", "pre-executing...");
        }

        @Override
        protected void onPostExecute(Long idProject) {
            //mAuthTask = null;
            //showProgress(false);
            Log.d("save-project", "finished " + idProject);
            if ( idProject != -1){
                Intent data = new Intent();
                data.putExtra("success", true);
                data.putExtra("id_project", idProject);
                data.putExtra("name_project", project.getProjectName());
                setResult(Constants.RESULT_NEW_PROJECT, data);
                finish();
            }else{
                Intent data = new Intent();
                data.putExtra("success", false);
                setResult(Constants.RESULT_NEW_PROJECT, data);
                finish();
            }
        }

    }
}
