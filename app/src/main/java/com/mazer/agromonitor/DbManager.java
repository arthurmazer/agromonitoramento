package com.mazer.agromonitor;

import android.annotation.SuppressLint;

import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Context;
import android.os.AsyncTask;

import com.mazer.agromonitor.entity.Product;
import com.mazer.agromonitor.entity.Project;
import com.mazer.agromonitor.entity.ProjectProduct;
import com.mazer.agromonitor.entity.User;
import com.mazer.agromonitor.interfaces.ProductDAO;
import com.mazer.agromonitor.interfaces.ProjectDAO;
import com.mazer.agromonitor.interfaces.ProjectProductDAO;
import com.mazer.agromonitor.interfaces.UserDAO;
import com.mazer.agromonitor.utils.Constants;

/**
 * Created by arthu on 09/02/2018.
 */

@Database(entities = {User.class,Project.class, ProjectProduct.class, Product.class}, version = Constants.DATABASE_VERSION)
public abstract class DbManager extends RoomDatabase{

    public static DbManager instance;
    public abstract UserDAO userDAO();
    public abstract ProjectDAO projectDAO();
    public abstract ProductDAO productDAO();
    public abstract ProjectProductDAO projectProductDAO();

    public static DbManager getInstance(Context ctx){
        if ( instance == null ){
            instance = Room.databaseBuilder(ctx, DbManager.class, Constants.DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
            instance.populateInitialData();
        }



        return instance;
    }

    @SuppressLint("StaticFieldLeak")
    public void populateInitialData(){
        final User user = new User();
        user.setLogin("admin@agro.com");
        user.setPassword("passpass14");

        new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    userDAO().insertUser(user);
                    return null;
                }
            }.execute();


    }

    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }
}
