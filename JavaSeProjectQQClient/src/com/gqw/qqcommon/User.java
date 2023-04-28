package com.gqw.qqcommon;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author VitamineG
 */
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    //用户账号
    private String userId;
    //用户密码
    private String passwd;


    public User() {
    }

    public User(String userId, String passwd) {
        this.userId = userId;
        this.passwd = passwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
