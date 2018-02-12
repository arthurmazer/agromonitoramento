package com.greenlab.agromonitor;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.greenlab.agromonitor.entity.Project;
import com.greenlab.agromonitor.entity.User;
import com.greenlab.agromonitor.interfaces.UserDAO;
import com.greenlab.agromonitor.utils.Constants;

/**
 * Created by arthu on 09/02/2018.
 */

@Database(entities = {User.class, Project.class}, version = 1)
public abstract class DbManager extends RoomDatabase{

    public static DbManager instance;
    public abstract UserDAO userDAO();

    public static DbManager getInstance(Context ctx){
        if ( instance == null ){
            instance = Room.databaseBuilder(ctx, DbManager.class, Constants.DB_NAME).build();
            instance.populateInitialData();
        }
        return instance;
    }

    public static void destroyInstance(){
        instance = null;
    }

    public void populateInitialData(){
        User user = new User();
        user.setLogin("admin@agro.com");
        user.setPassword("passpass14");
        Log.d("aqui", "sucesso");
        beginTransaction();
        try{
            userDAO().insertUser(user);
            setTransactionSuccessful();
            Log.d("aqui", "inseriu");
        }finally {
            endTransaction();
        }
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
