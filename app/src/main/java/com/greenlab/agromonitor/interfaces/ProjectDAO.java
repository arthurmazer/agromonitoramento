package com.greenlab.agromonitor.interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.greenlab.agromonitor.entity.Project;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by monitorapc on 22-Feb-18.
 */

@Dao
public interface ProjectDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertProject(Project project);

    @Update
    void updateProject(Project project);

    @Query("SELECT * FROM project WHERE idUser = :idUser")
    List<Project> selectgetAllProjectsFromUser(int idUser);
}
