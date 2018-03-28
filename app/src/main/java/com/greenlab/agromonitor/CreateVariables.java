package com.greenlab.agromonitor;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.greenlab.agromonitor.adapters.ProductListAdapter;
import com.greenlab.agromonitor.entity.Project;
import com.greenlab.agromonitor.entity.User;
import com.greenlab.agromonitor.managers.UserManager;
import com.greenlab.agromonitor.utils.Constants;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CreateVariables extends BaseActivity {

    Project project;

    RecyclerView recyclerProducs;
    ProductListAdapter productListAdapter;
    UserManager userManager;
    ArrayList<String> listProducts;
    ImageView btnAddProduct;
    EditText textVariaveis;
    TextView btnBackToStep3;
    TextView btnFinish;
    Spinner spinnerUnity;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_variables);
        setTitle(getResources().getString(R.string.title_step_variables));

        context = getApplicationContext();
        textVariaveis = findViewById(R.id.text_variaveis);
        recyclerProducs = findViewById(R.id.recycler_products);
        spinnerUnity = findViewById(R.id.unity_spinner);
        btnAddProduct = findViewById(R.id.btn_add_variable);
        btnBackToStep3 = findViewById(R.id.btn_back_to_step3);
        btnFinish = findViewById(R.id.btn_finish_project);
        listProducts = new ArrayList<>();
        userManager =  new UserManager(getApplicationContext());


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.unity_choice, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnity.setAdapter(adapter);

        textVariaveis.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        productListAdapter = new ProductListAdapter(listProducts);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerProducs.setLayoutManager(mLayoutManager);
        recyclerProducs.setItemAnimator(new DefaultItemAnimator());
        recyclerProducs.setAdapter(productListAdapter);

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String product = textVariaveis.getText().toString();

                if (!product.isEmpty()){
                    if ( !listProducts.contains(product)) {
                        listProducts.add(product);
                        productListAdapter.notifyDataSetChanged();
                        textVariaveis.setText("");
                    }else{
                        showToast(getResources().getString(R.string.duplicated_variable));
                    }
                }

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textVariaveis.getWindowToken(), 0);
            }
        });

        Bundle extras = getIntent().getExtras();
        project = new Project();

        if (extras != null) {
            project = extras.getParcelable(Constants.REGISTRATION_EXTRA_PROJECT);
            setView();
        }


        btnBackToStep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToStep3();
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDataFromStepVar();
                if (checkObligatoryFields()){
                    saveProject(project);
                }

            }
        });

    }

    public void getDataFromStepVar(){
        if (spinnerUnity.getSelectedItem().toString().equals("Kg/ha"))
            project.setMeasureUnity(Constants.KILO_HECTARE);
        else if(spinnerUnity.getSelectedItem().toString().equals("t/ha"))
            project.setMeasureUnity((Constants.TONELADA_HECTARE));

        project.setListOfStringProducts(this.listProducts);
    }

    public void setView(){
        if ( project.getMeasureUnity() == Constants.KILO_HECTARE )
            spinnerUnity.setSelection(Constants.KILO_HECTARE);
        else if ( project.getMeasureUnity() == Constants.TONELADA_HECTARE)
            spinnerUnity.setSelection(Constants.TONELADA_HECTARE);

        if (project.getListOfStringProducts() != null && !project.getListOfStringProducts().isEmpty()){
            listProducts.clear();
            listProducts.addAll(project.getListOfStringProducts());
            productListAdapter.notifyDataSetChanged();
        }
    }

    public boolean checkObligatoryFields(){
        if ( listProducts.isEmpty() ){
            String error = getResources().getString(R.string.error_no_products);
            showToast(error);
            return false;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        backToStep3();
    }

    public void backToStep3(){
        getDataFromStepVar();
        Intent it = new Intent(getApplicationContext() , NewProjectActivityStep3.class);
        it.putExtra(Constants.REGISTRATION_EXTRA_PROJECT, project);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
        overridePendingTransition(R.anim.slide_in_backward, R.anim.slide_out_backward);
    }

    public void saveProject(Project project){
        User user = getSessionUser(); //get user with id and login and list of projects -- password isn't necessary

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String dateNow = dateFormat.format(date);

        project.setCreationDate(dateNow);
      //  project.setIdUser(user.getId());

        CreateVariables.ProjectSave projectSave = new CreateVariables.ProjectSave(user,project,this.listProducts);
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
                Intent data = new Intent(context, HomeActivity.class);
                data.putExtra("success", true);
                data.putExtra("id_project", idProject);
                data.putExtra("name_project", project.getProjectName());
                data.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                setResult(Constants.RESULT_NEW_PROJECT, data);
                startActivity(data);
            }else{
                Intent data = new Intent(context, HomeActivity.class);
                data.putExtra("success", false);
                setResult(Constants.RESULT_NEW_PROJECT, data);
                startActivity(data);
            }
        }

    }

}
