package fr.odyssee.application.login;

import fr.theshark34.openlauncherlib.util.Saver;

import javax.swing.*;

public interface LoginListener {
    boolean login(JFrame frame, String username, String password, Saver saver);

    void onSuccessLogin(String username, String password, Saver saver);

    void onFailedLogin();

    void onCancel();
}
