package com.alex.greenroute.data.remote.body;

/**
 * Created by Alex on 12/10/2015.
 */
public class RegisterBody {
    private String email;
    private String password;

    public RegisterBody() {}

    public RegisterBody(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
