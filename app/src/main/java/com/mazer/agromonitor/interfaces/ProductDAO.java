package com.mazer.agromonitor.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mazer.agromonitor.entity.Product;

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
