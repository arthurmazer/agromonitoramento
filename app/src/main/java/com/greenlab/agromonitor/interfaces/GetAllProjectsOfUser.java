package com.greenlab.agromonitor.interfaces;

import com.greenlab.agromonitor.entity.Project;

import java.util.List;

/**
 * Created by monitorapc on 28-Feb-18.
 */

public interface GetAllProjectsOfUser {
    void onSuccessGettingProjects(List<Project> projectList);
}
