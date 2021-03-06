package com.mazer.agromonitor.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mazer.agromonitor.DbManager;
import com.mazer.agromonitor.entity.Product;
import com.mazer.agromonitor.entity.Project;
import com.mazer.agromonitor.entity.ProjectProduct;
import com.mazer.agromonitor.entity.SpreadsheetValues;
import com.mazer.agromonitor.entity.User;
import com.mazer.agromonitor.entity.Variables;
import com.mazer.agromonitor.interfaces.GetAllProjectsOfUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arthu on 09/02/2018.
 */

public class UserManager {

    private final DatabaseReference reference;
    DbManager dbManager;

    public UserManager(Context ctx){
        this.dbManager = DbManager.getInstance(ctx);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("users/");
    }

    public User login(User user){
        return this.dbManager.userDAO().login(user.login,user.password);
    }

    public void getListOfProjects(int idUser, GetAllProjectsOfUser getAllProjectsOfUser){
        LoadProjects loadProjects = new LoadProjects(idUser,getAllProjectsOfUser);
        loadProjects.execute((Void) null);
    }

    public long saveProject(Project project, ArrayList<Variables> listOfProducts){
        long idProject = this.dbManager.projectDAO().insertProject(project);

        if (idProject != -1){
            for(Variables product: listOfProducts){
                Product prod = new Product();
                prod.setIdProject((int)idProject);
                prod.setProduct(product.getVarName());
                this.dbManager.productDAO().insertProduct(prod);
            }
        }

        return idProject;
    }

    public List<Product> getVariablesFromProject(int idProject){
        return dbManager.productDAO().getAllProductsHeadersFromProject(idProject);
    }

    public List<SpreadsheetValues> getSpreadsheetValues(final int idProject){
        return dbManager.projectProductDAO().getAllProductsValuesFromProject(idProject);

}
    public List<SpreadsheetValues> getSpreadsheetValuesNotNull(final int idProject){
        return dbManager.projectProductDAO().getAllProductsValuesNotNullFromProject(idProject);

    }

    public List<SpreadsheetValues> getProductValuesNotNullFromProject(final int idProject, final int idProduct){
        return dbManager.projectProductDAO().getProductValuesNotNullFromProject(idProject, idProduct);
    }

    public Project getProjectById(final int idProject){
        return dbManager.projectDAO().getProjectById(idProject);
    }





    public void findUser(String userLogin, final OnFindUser onFindUser) {
        DatabaseReference userReference = reference.child(userLogin);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onFindUser.onFindUserSuccess(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onFindUser.onFindUserFailed(databaseError.toString());
            }
        });

    }


    @SuppressLint("StaticFieldLeak")
    public void insertProjectProduct(final ProjectProduct projectProduct){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                dbManager.projectProductDAO().insertProjectProduct(projectProduct);
                return null;
            }
        }.execute();

    }

    @SuppressLint("StaticFieldLeak")
    public void removeProjectProduct(int idProduct){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                dbManager.projectProductDAO().removeProjectProduct(idProduct);
                return null;
            }
        }.execute();

    }

    @SuppressLint("StaticFieldLeak")
    public void updateProjectProduct(final ProjectProduct projectProduct){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                dbManager.projectProductDAO().updateProjectProduct(projectProduct);
                return null;
            }
        }.execute();

    }

    @SuppressLint("StaticFieldLeak")
    public void updateProjectAreaAndUnity(final int idProject,final int areaAmostral,final int unity){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                dbManager.projectDAO().updateProjectAreaAndUnity(idProject,areaAmostral,unity);
                return null;
            }
        }.execute();

    }

    @SuppressLint("StaticFieldLeak")
    public void updateProjectUmidade(final int idProject,final float umidade){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                dbManager.projectDAO().updateUmidade(idProject,umidade);
                return null;
            }
        }.execute();

    }

    @SuppressLint("StaticFieldLeak")
    public void updateProjectUmidadeCoop(final int idProject,final float umidade){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                dbManager.projectDAO().updateUmidadeCoop(idProject,umidade);
                return null;
            }
        }.execute();

    }




    @SuppressLint("StaticFieldLeak")
    public void update(final User user){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                dbManager.userDAO().updateUser(user);
                return null;
            }
        }.execute();
    }


    public class LoadProjects extends AsyncTask<Void, Void, List<Project>> {

        int idUser;
        GetAllProjectsOfUser getAllProjectsOfUser;

        LoadProjects(int idUser,GetAllProjectsOfUser getAllProjectsOfUser) {
            this.idUser = idUser;
            this.getAllProjectsOfUser = getAllProjectsOfUser;
        }

        @Override
        protected List<Project> doInBackground(Void... params) {
            List<Project> listOfProject = dbManager.projectDAO().selectgetAllProjectsFromUser();
            for(int i = 0; i < listOfProject.size(); i++){
                int idProject =  listOfProject.get(i).getId();
                listOfProject.get(i).setListOfProducts(dbManager.productDAO().getAllProductsHeadersFromProject(idProject));
            }
            return listOfProject;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(List<Project> projectList) {
            getAllProjectsOfUser.onSuccessGettingProjects(projectList);
        }

    }

    public interface OnFindUser {
        void onFindUserSuccess(User user);

        void onFindUserFailed(String e);
    }
}
