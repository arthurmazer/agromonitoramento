package com.mazer.agromonitor.interfaces;

import com.mazer.agromonitor.entity.User;

/**
 * Created by arthu on 09/02/2018.
 */

public interface UserLoginInterface {
    void onUserLoginSuccess(User user);
    void onUserLoginFailed();
}
