package com.greenlab.agromonitor;

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
        project.setProjectName("Teste 1");
        Log.d("save-project-user","> " + user.getId());

        if ( this.radioCana.isChecked())
            project.setCultureType(Constants.PROJECT_TYPE_CANA_DE_ACUCAR);
        else
            project.setCultureType(Constants.PROJECT_TYPE_SOJA);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String dateNow = dateFormat.format(date);

        project.setCreationDate(dateNow);
        project.setIdUser(user.getId());

        ProjectSave projectSave = new ProjectSave(user,project);
        projectSave.execute((Void) null);

    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class ProjectSave extends AsyncTask<Void, Void, Long> {

        User user;
        Project project;


        ProjectSave(User user, Project project) {
            this.user = user;
            this.project = project;
        }

        @Override
        protected Long doInBackground(Void... params) {
            Log.d("save-project", "doing on background");
            return user.saveProject(getApplicationContext(),project);

        }

        @Override
        protected void onPreExecute() {
            Log.d("save-project", "pre-executing...");
        }

        @Override
        protected void onPostExecute(Long status) {
            //mAuthTask = null;
            //showProgress(false);
            Log.d("save-project", "finished");
            if ( status == 1){
                Intent data = new Intent();
                data.putExtra("success", true);
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
