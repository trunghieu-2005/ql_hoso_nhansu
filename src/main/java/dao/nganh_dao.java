package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database.myconnection;
import model.nganh_model;

public class nganh_dao {

    // Lấy tất cả ngành (JOIN với khoa để lấy tên khoa)
    public List<nganh_model> findAll() {
        String sql = "SELECT n.id, n.ten_nganh, n.khoa_id, k.ten_khoa, n.mo_ta " +
                     "FROM nganh n LEFT JOIN khoa k ON n.khoa_id = k.id " +
                     "ORDER BY n.id";
        List<nganh_model> result = new ArrayList<>();

        try (Connection conn = myconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                nganh_model item = new nganh_model(
                        rs.getInt("id"),
                        rs.getString("ten_nganh"),
                        rs.getInt("khoa_id"),
                        rs.getString("ten_khoa"),
                        rs.getString("mo_ta")
                );
                result.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách ngành", e);
        }

        return result;
    }

    // Tìm ngành theo ID
    public nganh_model findById(int id) {
        String sql = "SELECT n.id, n.ten_nganh, n.khoa_id, k.ten_khoa, n.mo_ta " +
                     "FROM nganh n LEFT JOIN khoa k ON n.khoa_id = k.id " +
                     "WHERE n.id = ?";

        try (Connection conn = myconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new nganh_model(
                            rs.getInt("id"),
                            rs.getString("ten_nganh"),
                            rs.getInt("khoa_id"),
                            rs.getString("ten_khoa"),
                            rs.getString("mo_ta")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm ngành theo ID", e);
        }

        return null;
    }

    // Tìm kiếm ngành theo tên
    public List<nganh_model> search(String keyword) {
        List<nganh_model> result = new ArrayList<>();
        String sql = "SELECT n.id, n.ten_nganh, n.khoa_id, k.ten_khoa, n.mo_ta " +
                     "FROM nganh n LEFT JOIN khoa k ON n.khoa_id = k.id " +
                     "WHERE n.ten_nganh LIKE ?";

        try (Connection conn = myconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    nganh_model item = new nganh_model(
                            rs.getInt("id"),
                            rs.getString("ten_nganh"),
                            rs.getInt("khoa_id"),
                            rs.getString("ten_khoa"),
                            rs.getString("mo_ta")
                    );
                    result.add(item);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm kiếm ngành", e);
        }

        return result;
    }

    // Thêm ngành mới
    public int insert(nganh_model nganh) {
        String sql = "INSERT INTO nganh(ten_nganh, khoa_id, mo_ta) VALUES (?, ?, ?)";

        try (Connection conn = myconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nganh.getTenNganh());
            ps.setInt(2, nganh.getKhoaId());
            ps.setString(3, nganh.getMoTa());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                return 0;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int newId = rs.getInt(1);
                    nganh.setId(newId);
                    return newId;
                }
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thêm ngành", e);
        }
    }

    // Cập nhật ngành
    public boolean update(nganh_model nganh) {
        String sql = "UPDATE nganh SET ten_nganh = ?, khoa_id = ?, mo_ta = ? WHERE id = ?";

        try (Connection conn = myconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nganh.getTenNganh());
            ps.setInt(2, nganh.getKhoaId());
            ps.setString(3, nganh.getMoTa());
            ps.setInt(4, nganh.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật ngành", e);
        }
    }

    // Xóa ngành
    public boolean delete(int id) {
        String sql = "DELETE FROM nganh WHERE id = ?";

        try (Connection conn = myconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi xóa ngành", e);
        }
    }

    // Kiểm tra tên ngành trùng khi THÊM
    public boolean kiemTraTenNganhTrung(String tenNganh) {
        String sql = "SELECT COUNT(*) FROM nganh WHERE ten_nganh = ?";

        try (Connection conn = myconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tenNganh);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Kiểm tra tên ngành trùng khi SỬA (bỏ qua chính nó)
    public boolean kiemTraTenNganhTrungKhiSua(String tenNganh, int id) {
        String sql = "SELECT COUNT(*) FROM nganh WHERE ten_nganh = ? AND id != ?";

        try (Connection conn = myconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tenNganh);
            ps.setInt(2, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}