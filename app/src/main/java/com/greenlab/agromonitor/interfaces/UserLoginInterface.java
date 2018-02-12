package com.greenlab.agromonitor.interfaces;

import com.greenlab.agromonitor.entity.User;

/**
 * Created by arthu on 09/02/2018.
 */

public interface UserLoginInterface {
    void onUserLoginSuccess(User user);
    void onUserLoginFailed();
}
