package controller;

import app.SessionManager;
import dao.user_dao;
import model.user_model;
import view.user_view;

import java.util.List;

import javax.swing.JFrame;

public class user_controller {

    private final user_dao userDao = new user_dao();
    private final SessionManager session = SessionManager.getInstance();

    // ─── Hiển thị màn hình quản lý user ──────────────────────────
    public void showUserManagement() {
        if (!session.canManageUsers()) {
            showAccessDenied();
            return;
        }
        JFrame frame = new JFrame("Quản lý tài khoản");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(980, 620);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new user_view(this));
        frame.setVisible(true);
    }

    // ─── Lấy danh sách tất cả user ───────────────────────────────
    public List<user_model> getAllUsers() {
        return userDao.getAll();
    }

    // ─── Thêm tài khoản mới ──────────────────────────────────────
    public String addUser(String username, String password, String confirmPassword, String role) {
        String usernameTrim = username == null ? "" : username.trim();
        if (!session.canManageUsers()) return "Bạn không có quyền thực hiện thao tác này!";
        if (usernameTrim.isEmpty()) return "Tên đăng nhập không được để trống!";
        if (password == null || password.length() < 6) return "Mật khẩu phải có ít nhất 6 ký tự!";
        if (!password.equals(confirmPassword)) return "Xác nhận mật khẩu không khớp!";
        if (role == null || role.trim().isEmpty()) return "Vui lòng chọn vai trò!";
        if (userDao.usernameExists(usernameTrim)) return "Tên đăng nhập đã tồn tại!";

        user_model u = new user_model();
        u.setUsername(usernameTrim);
        u.setPassword(password);          // TODO: hash password khi triển khai thực tế
        u.setRole(role);

        return userDao.add(u) ? null : "Thêm tài khoản thất bại, vui lòng thử lại!";
    }

    // ─── Cập nhật tài khoản ──────────────────────────────────────
    public String updateUser(int id, String username, String password, String role) {
        String usernameTrim = username == null ? "" : username.trim();
        if (!session.canManageUsers()) return "Bạn không có quyền thực hiện thao tác này!";
        if (usernameTrim.isEmpty()) return "Tên đăng nhập không được để trống!";
        if (role == null || role.trim().isEmpty()) return "Vui lòng chọn vai trò!";

        user_model existing = userDao.getById(id);
        if (existing == null) {
            return "Không tìm thấy tài khoản cần cập nhật!";
        }

        if (userDao.usernameExistsExceptId(usernameTrim, id)) {
            return "Tên đăng nhập đã tồn tại!";
        }

        if (password != null && !password.isBlank() && password.length() < 6) {
            return "Mật khẩu mới phải có ít nhất 6 ký tự!";
        }

        // Không cho sửa tài khoản admin đang đăng nhập nếu đổi role
        user_model current = session.getCurrentUser();
        if (current != null && current.getId() == id && !role.equals(current.getRole())) {
            return "Không thể tự thay đổi vai trò của chính mình!";
        }

        user_model u = new user_model();
        u.setId(id);
        u.setUsername(usernameTrim);
        u.setPassword(password != null && !password.isBlank() ? password : existing.getPassword());
        u.setRole(role);

        return userDao.update(u) ? null : "Cập nhật thất bại, vui lòng thử lại!";
    }

    // ─── Xóa tài khoản ───────────────────────────────────────────
    public String deleteUser(int id) {
        if (!session.canManageUsers()) return "Bạn không có quyền thực hiện thao tác này!";

        user_model current = session.getCurrentUser();
        if (current.getId() == id) return "Không thể xóa tài khoản đang đăng nhập!";

        return userDao.delete(id) ? null : "Xóa tài khoản thất bại!";
    }

    // ─── Phân quyền ──────────────────────────────────────────────
    public String assignRole(int userId, String newRole) {
        if (!session.canManageUsers()) return "Bạn không có quyền thực hiện thao tác này!";
        if (newRole == null || newRole.trim().isEmpty()) return "Vui lòng chọn vai trò!";

        user_model current = session.getCurrentUser();
        if (current.getId() == userId) return "Không thể tự phân quyền cho chính mình!";

        return userDao.updateRole(userId, newRole) ? null : "Phân quyền thất bại!";
    }

    // ─── Đổi mật khẩu (dùng cho tất cả user) ────────────────────
    public String changePassword(int userId, String oldPassword, String newPassword, String confirmPassword) {
        user_model u = userDao.getById(userId);
        if (u == null) return "Không tìm thấy tài khoản!";
        if (!u.getPassword().equals(oldPassword)) return "Mật khẩu hiện tại không đúng!";
        if (newPassword == null || newPassword.length() < 6) return "Mật khẩu mới phải có ít nhất 6 ký tự!";
        if (!newPassword.equals(confirmPassword)) return "Xác nhận mật khẩu không khớp!";

        return userDao.changePassword(userId, newPassword) ? null : "Đổi mật khẩu thất bại!";
    }

    // ─── Helper ──────────────────────────────────────────────────
    private void showAccessDenied() {
        javax.swing.JOptionPane.showMessageDialog(null,
            "Bạn không có quyền truy cập chức năng này!",
            "Từ chối truy cập", javax.swing.JOptionPane.WARNING_MESSAGE);
    }
}