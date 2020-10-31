package com.greenlab.agromonitor.interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.greenlab.agromonitor.entity.ProjectProduct;
import com.greenlab.agromonitor.entity.SpreadsheetValues;

import java.util.List;

/**
 * Created by monitorapc on 27-Feb-18.
 */

@Dao
public interface ProjectProductDAO {

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        void insertProjectProduct(ProjectProduct projectProduct);


        @Query("DELETE FROM project_product where idProduct = :idProduct")
        void removeProjectProduct(int idProduct);

        @Update
        void updateProjectProduct(ProjectProduct projectProduct);

        @Query("SELECT a.*, b.value, b.id as idProjectProduct from product as a LEFT JOIN project_product as b on a.id = b.idProduct WHERE a.idProject = :idProject order by a.id, idProjectProduct")
        List<SpreadsheetValues> getAllProductsValuesFromProject(int idProject);

        @Query("SELECT a.*, b.value,  b.id as idProjectProduct from product as a LEFT JOIN project_product as b on a.id = b.idProduct WHERE a.idProject = :idProject AND b.value IS NOT NULL Order by a.id")
        List<SpreadsheetValues> getAllProductsValuesNotNullFromProject(int idProject);

        @Query("SELECT a.*, b.value,  b.id as idProjectProduct from product as a LEFT JOIN project_product as b on a.id = b.idProduct WHERE a.idProject = :idProject AND b.idProduct = :idProduct AND b.value IS NOT NULL Order by  a.id, idProjectProduct")
        List<SpreadsheetValues> getProductValuesNotNullFromProject(int idProject, int idProduct);


}
