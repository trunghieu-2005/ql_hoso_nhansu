package dao;

import database.myconnection;
import model.user_model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class user_dao {

    // ─── Đăng nhập ───────────────────────────────────────────────
    public user_model login(String username, String password) {
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
        try (Connection conn = myconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);          // nếu dùng hash thì đổi ở đây
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("[user_dao.login] " + e.getMessage());
        }
        return null;
    }

    // ─── Lấy tất cả user ─────────────────────────────────────────
    public List<user_model> getAll() {
        List<user_model> list = new ArrayList<>();
        String sql = "SELECT * FROM user ORDER BY id";
        try (Connection conn = myconnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[user_dao.getAll] " + e.getMessage());
        }
        return list;
    }

    // ─── Lấy user theo id ────────────────────────────────────────
    public user_model getById(int id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        try (Connection conn = myconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[user_dao.getById] " + e.getMessage());
        }
        return null;
    }

    // ─── Thêm user ───────────────────────────────────────────────
    public boolean add(user_model u) {
        String sql = "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = myconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRole());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[user_dao.add] " + e.getMessage());
        }
        return false;
    }

    // ─── Cập nhật user ───────────────────────────────────────────
    public boolean update(user_model u) {
        String sql = "UPDATE user SET username = ?, password = ?, role = ? WHERE id = ?";
        try (Connection conn = myconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRole());
            ps.setInt(4, u.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[user_dao.update] " + e.getMessage());
        }
        return false;
    }

    // ─── Đổi mật khẩu ────────────────────────────────────────────
    public boolean changePassword(int userId, String newPassword) {
        String sql = "UPDATE user SET password = ? WHERE id = ?";
        try (Connection conn = myconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[user_dao.changePassword] " + e.getMessage());
        }
        return false;
    }

    // ─── Phân quyền ──────────────────────────────────────────────
    public boolean updateRole(int userId, String newRole) {
        String sql = "UPDATE user SET role = ? WHERE id = ?";
        try (Connection conn = myconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newRole);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[user_dao.updateRole] " + e.getMessage());
        }
        return false;
    }

    // ─── Xóa user ────────────────────────────────────────────────
    public boolean delete(int id) {
        String sql = "DELETE FROM user WHERE id = ?";
        try (Connection conn = myconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[user_dao.delete] " + e.getMessage());
        }
        return false;
    }

    // ─── Kiểm tra username đã tồn tại ────────────────────────────
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM user WHERE username = ?";
        try (Connection conn = myconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("[user_dao.usernameExists] " + e.getMessage());
        }
        return false;
    }

    public boolean usernameExistsExceptId(String username, int excludedId) {
        String sql = "SELECT COUNT(*) FROM user WHERE username = ? AND id <> ?";
        try (Connection conn = myconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setInt(2, excludedId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[user_dao.usernameExistsExceptId] " + e.getMessage());
        }
        return false;
    }

    // ─── Helper ──────────────────────────────────────────────────
    private user_model mapRow(ResultSet rs) throws SQLException {
        user_model u = new user_model();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setRole(rs.getString("role"));
        return u;
    }
}