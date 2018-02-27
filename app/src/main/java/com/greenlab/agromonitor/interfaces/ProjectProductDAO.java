package com.greenlab.agromonitor.interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.greenlab.agromonitor.entity.ProjectProduct;

import java.util.List;

/**
 * Created by monitorapc on 27-Feb-18.
 */

@Dao
public interface ProjectProductDAO {

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        void insertProjectProduct(ProjectProduct projectProduct);

        @Update
        void updateProjectProduct(ProjectProduct projectProduct);

        @Query("SELECT * FROM project_product WHERE idProject = :idProject")
        List<ProjectProduct> getAllProductsValuesFromProject(int idProject);


}
