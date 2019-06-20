package edu.skku.HyeonYeong_Jeong.SWPPA2;

import com.google.firebase.database.IgnoreExtraProperties;


public class User {

    public String user_id;
    public String user_password;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String input_user_id, String input_user_passwrod){

        user_id = input_user_id;
        user_password = input_user_passwrod;

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }
}

