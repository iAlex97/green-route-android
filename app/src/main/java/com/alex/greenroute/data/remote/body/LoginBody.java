package com.alex.greenroute.data.remote.body;

/**
 * Created by Alex on 12/11/2015.
 */
public class LoginBody {
    private String email;
    private String password;

    public LoginBody() {}

    public LoginBody(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
