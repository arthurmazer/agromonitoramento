package com.greenlab.agromonitor.interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.greenlab.agromonitor.entity.Product;

import java.util.List;

/**
 * Created by monitorapc on 27-Feb-18.
 */

@Dao
public interface ProductDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertProduct(Product product);

    @Update
    void updateProject(Product product);

    @Query("SELECT * FROM product WHERE idProject = :idProject")
    List<Product> getAllProductsHeadersFromProject(int idProject);
}
