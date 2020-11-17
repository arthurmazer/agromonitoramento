package com.mazer.agromonitor.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mazer.agromonitor.entity.Project;

import java.util.List;


/**
 * Created by monitorapc on 22-Feb-18.
 */

@Dao
public interface ProjectDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertProject(Project project);

    //@Update
    //void updateProject(Project project);

    @Query("SELECT * FROM project")
    List<Project> selectgetAllProjectsFromUser();

    @Query("SELECT * FROM project where id=:idProject")
    Project getProjectById(int idProject);

    @Query("UPDATE project SET areaAmostral=:areaAmostral, measureUnity=:measureUnity WHERE id=:idProject")
    void updateProjectAreaAndUnity(int idProject, int areaAmostral, int measureUnity);

    @Query("UPDATE project SET umidade=:umidade WHERE id=:idProject")
    void updateUmidade(int idProject, float umidade);


    @Query("UPDATE project SET umidadeCoop=:umidade WHERE id=:idProject")
    void updateUmidadeCoop(int idProject, float umidade);

}
