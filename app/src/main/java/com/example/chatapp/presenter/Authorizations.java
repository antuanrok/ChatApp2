package com.example.chatapp.presenter;

public interface Authorizations {
    void showActivity (boolean res, String user_name);
   // void regGoogle();

    void getOutGoogle(String text);
    void showError(String mess);

    void showMess (String mess);

}
