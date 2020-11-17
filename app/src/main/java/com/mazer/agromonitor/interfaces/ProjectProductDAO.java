package com.mazer.agromonitor.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mazer.agromonitor.entity.ProjectProduct;
import com.mazer.agromonitor.entity.SpreadsheetValues;

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

        @Query("SELECT a.*, b.value, b.timestamp, b.id as idProjectProduct from product as a LEFT JOIN project_product as b on a.id = b.idProduct WHERE a.idProject = :idProject order by a.id, idProjectProduct")
        List<SpreadsheetValues> getAllProductsValuesFromProject(int idProject);

        @Query("SELECT a.*, b.value, b.timestamp, b.id as idProjectProduct from product as a LEFT JOIN project_product as b on a.id = b.idProduct WHERE a.idProject = :idProject AND b.value IS NOT NULL Order by a.id")
        List<SpreadsheetValues> getAllProductsValuesNotNullFromProject(int idProject);

        @Query("SELECT a.*, b.value, b.timestamp,  b.id as idProjectProduct from product as a LEFT JOIN project_product as b on a.id = b.idProduct WHERE a.idProject = :idProject AND b.idProduct = :idProduct AND b.value IS NOT NULL Order by  a.id, idProjectProduct")
        List<SpreadsheetValues> getProductValuesNotNullFromProject(int idProject, int idProduct);


}
