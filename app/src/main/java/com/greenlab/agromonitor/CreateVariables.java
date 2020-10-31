package com.greenlab.agromonitor;

import android.annotation.SuppressLint;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.greenlab.agromonitor.adapters.ProductListAdapter;
import com.greenlab.agromonitor.entity.Project;
import com.greenlab.agromonitor.entity.User;
import com.greenlab.agromonitor.entity.Variables;
import com.greenlab.agromonitor.managers.UserManager;
import com.greenlab.agromonitor.utils.Constants;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateVariables extends BaseActivity {

    Project project;

    RecyclerView recyclerProducs;
    RelativeLayout rootLayout;
    ProductListAdapter productListAdapter;
    UserManager userManager;
    ArrayList<Variables> listProducts;
    ImageView btnAddProduct;
    EditText textVariaveis;
    TextView btnBackToStep3;
    TextView btnFinish;
    CheckBox ctPT;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_variables);
        setTitle(getResources().getString(R.string.title_step_variables));

        context = getApplicationContext();
        textVariaveis = findViewById(R.id.text_variaveis);
        recyclerProducs = findViewById(R.id.recycler_products);
        btnAddProduct = findViewById(R.id.btn_add_variable);
        btnBackToStep3 = findViewById(R.id.btn_back_to_step3);
        btnFinish = findViewById(R.id.btn_finish_project);
        rootLayout = findViewById(R.id.root_layout);
        ctPT = findViewById(R.id.cbPT);
        listProducts = new ArrayList<>();
        userManager =  new UserManager(getApplicationContext());

        textVariaveis.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        productListAdapter = new ProductListAdapter(listProducts);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerProducs.setLayoutManager(mLayoutManager);
        recyclerProducs.setItemAnimator(new DefaultItemAnimator());
        recyclerProducs.setAdapter(productListAdapter);

        btnAddProduct.setOnClickListener(view -> {
            String product = textVariaveis.getText().toString();

            if (!product.isEmpty()){

                if (!containsVar(listProducts, product)) {
                    Variables var = new Variables();
                    var.setVarName(product);
                    listProducts.add(var);
                    productListAdapter.notifyDataSetChanged();
                    textVariaveis.setText("");
                }else{
                    showToast(getResources().getString(R.string.duplicated_variable));
                }
            }

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(textVariaveis.getWindowToken(), 0);
        });

        ctPT.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                Variables var = new Variables();
                var.setPerdasTotais(true);
                var.setVarName("PT");
                listProducts.add(var);
                productListAdapter.notifyDataSetChanged();
            }else{
                removePTFromList();
            }
        });

        Bundle extras = getIntent().getExtras();
        project = new Project();

        if (extras != null) {
            project = extras.getParcelable(Constants.REGISTRATION_EXTRA_PROJECT);
            setView();
        }


        btnBackToStep3.setOnClickListener(view -> backToStep3());

        btnFinish.setOnClickListener(view -> {

            getDataFromStepVar();
            if (checkObligatoryFields()){
                //default values
                if ( project.getCultureType() == Constants.PROJECT_TYPE_CANA_DE_ACUCAR)
                    project.setAreaAmostral(10);
                else
                    project.setAreaAmostral(2);
                saveProject(project);
            }

        });

    }

    private void removePTFromList() {
        int index = -1;
        for (int i = 0; i < listProducts.size(); i++){
            if (listProducts.get(i).isPerdasTotais()){
                index = i;
            }
        }
        if (index != -1) {
            listProducts.remove(index);
            productListAdapter.notifyDataSetChanged();
        }
    }

    public boolean containsVar(final List<Variables> list, String vars){
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).getVarName().equals(vars)){
                return true;
            }
        }
        return false;
    }

    public void getDataFromStepVar(){
        project.setListOfStringProducts(this.listProducts);
    }

    public void setView(){
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
    @SuppressLint("StaticFieldLeak")
    public class ProjectSave extends AsyncTask<Void, Void, Long> {

        User user;
        Project project;
        ArrayList<Variables> listOfProducts;


        ProjectSave(User user, Project project, ArrayList<Variables> listOfProducts) {
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
                data.putExtra("culture", project.getCultureType());
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
