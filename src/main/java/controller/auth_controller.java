package controller;

import app.SessionManager;
import dao.user_dao;
import model.user_model;
import view.login_view;
import view.dashboard_view;

public class auth_controller {

    private final user_dao userDao = new user_dao();
    private final SessionManager session = SessionManager.getInstance();

    public void showLogin() {
        login_view view = new login_view(this);
        view.setVisible(true);
    }

    public boolean doLogin(String username, String password, login_view view) {
        if (username == null || username.trim().isEmpty()) {
            view.showError("Vui lòng nhập tên đăng nhập!");
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            view.showError("Vui lòng nhập mật khẩu!");
            return false;
        }

        user_model user = userDao.login(username.trim(), password.trim());
        if (user == null) {
            view.showError("Sai tài khoản hoặc mật khẩu!");
            return false;
        }

        session.login(user);
        view.dispose(); // đóng login
        openDashboard();
        return true;
    }

   private void openDashboard() {
    dashboard_view dash = new dashboard_view(this, session.getCurrentUser());
    dash.setVisible(true);
}
public void doLogout(dashboard_view dashView) {
    session.logout();       
    dashView.dispose();     

    showLogin();           
}
}