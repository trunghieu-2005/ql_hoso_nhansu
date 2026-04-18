package app;

import model.user_model;

/**
 * Quản lý phiên đăng nhập toàn cục (Singleton)
 */
public class SessionManager {

    private static SessionManager instance;
    private user_model currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(user_model user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public user_model getCurrentUser() {
        return currentUser;
    }

    // ───── Kiểm tra quyền ─────

    public boolean isAdmin() {
        return isLoggedIn() && "admin".equals(currentUser.getRole());
    }

    public boolean isCanBoNhanSu() {
        return isLoggedIn() && "can_bo_nhan_su".equals(currentUser.getRole());
    }

    public boolean isCanBoNhanVien() {
        return isLoggedIn() && "can_bo_nhan_vien".equals(currentUser.getRole());
    }

    /** Admin hoặc cán bộ nhân sự được quản lý Khoa/Ngành/Hồ sơ */
    public boolean canManageKhoaNganh() {
        return isAdmin() || isCanBoNhanSu();
    }

    /** Admin hoặc cán bộ nhân sự được quản lý hồ sơ */
    public boolean canManageHoSo() {
        return isCanBoNhanSu();
    }

    /** Chỉ Admin được quản lý người dùng */
    public boolean canManageUsers() {
        return isAdmin();
    }

    /** Tất cả người dùng đăng nhập đều xem được hồ sơ cá nhân */
    public boolean canViewHoSo() {
        return isLoggedIn();
    }
}