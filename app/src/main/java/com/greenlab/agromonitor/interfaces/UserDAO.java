package com.greenlab.agromonitor.interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.greenlab.agromonitor.entity.User;

/**
 * Created by arthu on 09/02/2018.
 */

@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Query("SELECT * FROM user WHERE login = :login AND password = :password  LIMIT 1")
    User login(String login, String password);

}
