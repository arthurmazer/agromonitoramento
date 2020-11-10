package com.greenlab.agromonitor;

import android.annotation.SuppressLint;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.greenlab.agromonitor.entity.Product;
import com.greenlab.agromonitor.entity.Project;
import com.greenlab.agromonitor.entity.ProjectProduct;
import com.greenlab.agromonitor.entity.User;
import com.greenlab.agromonitor.interfaces.ProductDAO;
import com.greenlab.agromonitor.interfaces.ProjectDAO;
import com.greenlab.agromonitor.interfaces.ProjectProductDAO;
import com.greenlab.agromonitor.interfaces.UserDAO;
import com.greenlab.agromonitor.utils.Constants;

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
