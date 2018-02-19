package com.greenlab.agromonitor;

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
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.greenlab.agromonitor.adapters.ProductListAdapter;
import com.greenlab.agromonitor.entity.Project;
import com.greenlab.agromonitor.entity.User;
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
    Button btnAddProduct;
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
        btnSaveProject = findViewById(R.id.btn_new_project);
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
                }
            }
        });

        btnSaveProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProject();
            }
        });

        populateFields();

    }

    public void populateFields(){
        this.listProducts.clear();
        this.radioCana.setChecked(true);
    }

    public void saveProject(){
        Project project = new Project();
        project.setProjectName("Teste 1");

        if ( this.radioCana.isChecked())
            project.setCultureType(Constants.PROJECT_TYPE_CANA_DE_ACUCAR);
        else
            project.setCultureType(Constants.PROJECT_TYPE_SOJA);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String dateNow = dateFormat.format(date);

        project.setCreationDate(dateNow);
        String jsonProducts = new Gson().toJson(this.listProducts);
        project.setListOfProducts(jsonProducts);

        Log.d("aquijson1",jsonProducts);


        User user = getSessionUser(); //get user with id and login and list of projects -- password isn't necessary
        ArrayList<Project> listOfProjects = user.getListOfProjects();
        listOfProjects.add(project);
        user.setListOfProjects(listOfProjects); //set list of projects
        userManager.update(user);

    }
}
