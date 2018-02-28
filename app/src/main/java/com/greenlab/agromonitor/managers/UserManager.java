package com.greenlab.agromonitor.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.greenlab.agromonitor.DbManager;
import com.greenlab.agromonitor.entity.Product;
import com.greenlab.agromonitor.entity.Project;
import com.greenlab.agromonitor.entity.User;
import com.greenlab.agromonitor.interfaces.GetAllProjectsOfUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arthu on 09/02/2018.
 */

public class UserManager {

    DbManager dbManager;

    public UserManager(Context ctx){
        this.dbManager = DbManager.getInstance(ctx);
    }

    public User login(User user){
        return this.dbManager.userDAO().login(user.login,user.password);
    }

    public void getListOfProjects(int idUser, GetAllProjectsOfUser getAllProjectsOfUser){
        LoadProjects loadProjects = new LoadProjects(idUser,getAllProjectsOfUser);
        loadProjects.execute((Void) null);
    }

    public long saveProject(Project project, ArrayList<String> listOfProducts){
        long idProject = this.dbManager.projectDAO().insertProject(project);

        if (idProject != -1){
            for(String product: listOfProducts){
                Product prod = new Product();
                prod.setIdProject((int)idProject);
                prod.setProduct(product);
                this.dbManager.productDAO().insertProduct(prod);
            }
        }

        return idProject;
    }

    public void createInitialProductsHeader(){

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
        //this.dbManager.userDAO().updateUser(user);
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
            return dbManager.projectDAO().selectgetAllProjectsFromUser(idUser);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(List<Project> projectList) {
            getAllProjectsOfUser.onSuccess(projectList);
        }

    }
}
