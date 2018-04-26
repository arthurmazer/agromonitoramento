package com.greenlab.agromonitor;

import android.annotation.SuppressLint;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
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
            Log.d("aqui", "instancia nulla");
            instance = Room.databaseBuilder(ctx, DbManager.class, Constants.DB_NAME)
                    .addMigrations(MIGRATION_5_6,MIGRATION_6_7,MIGRATION_8_9)
                    .build();
            instance.populateInitialData();
        }



        return instance;
    }

    public static void destroyInstance(){
        instance = null;
    }


    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Aumento os campos da tabela projeto
            database.execSQL("ALTER TABLE project "
                    + " ADD COLUMN turn INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE project "
                    + " ADD COLUMN farmName TEXT");
            database.execSQL("ALTER TABLE project "
                    + " ADD COLUMN talhao TEXT");
            database.execSQL("ALTER TABLE project "
                    + " ADD COLUMN frenteColheita TEXT");
            database.execSQL("ALTER TABLE project "
                    + " ADD COLUMN machineID TEXT");
            database.execSQL("ALTER TABLE project "
                    + " ADD COLUMN operatorsName TEXT");
            database.execSQL("ALTER TABLE project "
                    + " ADD COLUMN measurersName TEXT");

        }
    };

    static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Aumento os campos da tabela projeto
            database.execSQL("ALTER TABLE project "
                    + " ADD COLUMN measureUnity INTEGER NOT NULL DEFAULT 0");
        }
    };

    static final Migration MIGRATION_8_9 = new Migration(8, 9) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE project "
                    + " ADD COLUMN areaAmostral INTEGER NOT NULL DEFAULT 10");

        }
    };

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
