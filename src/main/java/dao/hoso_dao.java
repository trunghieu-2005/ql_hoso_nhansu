package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import database.myconnection;
import model.hoso_model;

public class hoso_dao {

    public List<hoso_model> findAll() {
        String sql = "SELECT id, ho_ten, ngay_sinh, gioi_tinh, so_dien_thoai, email, dia_chi, khoa_id, nganh_id, trinh_do, chuc_vu FROM hosonhansu ORDER BY id";
        List<hoso_model> result = new ArrayList<>();

        try (Connection conn = myconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(new hoso_model(
                        rs.getInt("id"),
                        rs.getString("ho_ten"),
                        rs.getString("ngay_sinh"),
                        rs.getString("gioi_tinh"),
                        rs.getString("so_dien_thoai"),
                        rs.getString("email"),
                        rs.getString("dia_chi"),
                        getNullableInt(rs, "khoa_id"),
                        getNullableInt(rs, "nganh_id"),
                        rs.getString("trinh_do"),
                        rs.getString("chuc_vu")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Loi khi lay danh sach ho so", e);
        }

        return result;
    }

    public int insert(hoso_model model) {
        String sql = "INSERT INTO hosonhansu(ho_ten, ngay_sinh, gioi_tinh, so_dien_thoai, email, dia_chi, khoa_id, nganh_id, trinh_do, chuc_vu) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = myconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, model.getHoTen());
            setNullableString(ps, 2, model.getNgaySinh());
            setNullableString(ps, 3, model.getGioiTinh());
            setNullableString(ps, 4, model.getSoDienThoai());
            setNullableString(ps, 5, model.getEmail());
            setNullableString(ps, 6, model.getDiaChi());
            setNullableInt(ps, 7, model.getKhoaId());
            setNullableInt(ps, 8, model.getNganhId());
            setNullableString(ps, 9, model.getTrinhDo());
            setNullableString(ps, 10, model.getChucVu());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                return 0;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Loi khi them ho so", e);
        }
    }

    public boolean update(hoso_model model) {
        String sql = "UPDATE hosonhansu SET ho_ten = ?, ngay_sinh = ?, gioi_tinh = ?, so_dien_thoai = ?, email = ?, dia_chi = ?, khoa_id = ?, nganh_id = ?, trinh_do = ?, chuc_vu = ? WHERE id = ?";

        try (Connection conn = myconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, model.getHoTen());
            setNullableString(ps, 2, model.getNgaySinh());
            setNullableString(ps, 3, model.getGioiTinh());
            setNullableString(ps, 4, model.getSoDienThoai());
            setNullableString(ps, 5, model.getEmail());
            setNullableString(ps, 6, model.getDiaChi());
            setNullableInt(ps, 7, model.getKhoaId());
            setNullableInt(ps, 8, model.getNganhId());
            setNullableString(ps, 9, model.getTrinhDo());
            setNullableString(ps, 10, model.getChucVu());
            ps.setInt(11, model.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Loi khi cap nhat ho so", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM hosonhansu WHERE id = ?";

        try (Connection conn = myconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Loi khi xoa ho so", e);
        }
    }

    public List<hoso_model> searchAdvanced(String keyword, Integer khoaId, Integer nganhId,
                                           String gioiTinh, String trinhDo, String chucVu) {
        StringBuilder sql = new StringBuilder(
                "SELECT id, ho_ten, ngay_sinh, gioi_tinh, so_dien_thoai, email, dia_chi, khoa_id, nganh_id, trinh_do, chuc_vu " +
                "FROM hosonhansu WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (hasText(keyword)) {
            String pattern = "%" + keyword.trim() + "%";
            sql.append(" AND (")
               .append("CAST(id AS CHAR) LIKE ? OR ")
               .append("ho_ten LIKE ? OR ")
               .append("ngay_sinh LIKE ? OR ")
               .append("gioi_tinh LIKE ? OR ")
               .append("so_dien_thoai LIKE ? OR ")
               .append("email LIKE ? OR ")
               .append("dia_chi LIKE ? OR ")
               .append("CAST(khoa_id AS CHAR) LIKE ? OR ")
               .append("CAST(nganh_id AS CHAR) LIKE ? OR ")
               .append("trinh_do LIKE ? OR ")
               .append("chuc_vu LIKE ?")
               .append(")");

            for (int i = 0; i < 11; i++) {
                params.add(pattern);
            }
        }

        if (khoaId != null) {
            sql.append(" AND khoa_id = ?");
            params.add(khoaId);
        }

        if (nganhId != null) {
            sql.append(" AND nganh_id = ?");
            params.add(nganhId);
        }

        if (hasText(gioiTinh)) {
            sql.append(" AND gioi_tinh LIKE ?");
            params.add("%" + gioiTinh.trim() + "%");
        }

        if (hasText(trinhDo)) {
            sql.append(" AND trinh_do LIKE ?");
            params.add("%" + trinhDo.trim() + "%");
        }

        if (hasText(chucVu)) {
            sql.append(" AND chuc_vu LIKE ?");
            params.add("%" + chucVu.trim() + "%");
        }

        sql.append(" ORDER BY id");

        List<hoso_model> result = new ArrayList<>();
        try (Connection conn = myconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            for (Object param : params) {
                if (param instanceof Integer intValue) {
                    ps.setInt(index++, intValue);
                } else {
                    ps.setString(index++, String.valueOf(param));
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new hoso_model(
                            rs.getInt("id"),
                            rs.getString("ho_ten"),
                            rs.getString("ngay_sinh"),
                            rs.getString("gioi_tinh"),
                            rs.getString("so_dien_thoai"),
                            rs.getString("email"),
                            rs.getString("dia_chi"),
                            getNullableInt(rs, "khoa_id"),
                            getNullableInt(rs, "nganh_id"),
                            rs.getString("trinh_do"),
                            rs.getString("chuc_vu")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Loi khi tim kiem ho so", e);
        }

        return result;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private Integer getNullableInt(ResultSet rs, String column) throws SQLException {
        int value = rs.getInt(column);
        return rs.wasNull() ? null : value;
    }

    private void setNullableString(PreparedStatement ps, int index, String value) throws SQLException {
        if (value == null || value.trim().isEmpty()) {
            ps.setNull(index, Types.VARCHAR);
        } else {
            ps.setString(index, value.trim());
        }
    }

    private void setNullableInt(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.INTEGER);
        } else {
            ps.setInt(index, value);
        }
    }
}