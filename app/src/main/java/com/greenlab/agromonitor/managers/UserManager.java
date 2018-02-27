package com.greenlab.agromonitor.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.greenlab.agromonitor.DbManager;
import com.greenlab.agromonitor.entity.Product;
import com.greenlab.agromonitor.entity.Project;
import com.greenlab.agromonitor.entity.User;

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

    public List<Project> getListOfProjects(int idUser){
        return this.dbManager.projectDAO().getAllProjectsFromUser(idUser);
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

    public interface OnUserProjectInserted{
        void onProjectSavedSuccess();
        void onProjectSavedFailed();
    }
}
