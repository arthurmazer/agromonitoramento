package com.greenlab.agromonitor.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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
