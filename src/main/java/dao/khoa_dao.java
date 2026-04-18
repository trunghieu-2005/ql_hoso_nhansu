package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database.myconnection;
import model.khoa_model;

public class khoa_dao {

	public List<khoa_model> findAll() {
		String sql = "SELECT id, ten_khoa, mo_ta FROM khoa ORDER BY id";
		List<khoa_model> result = new ArrayList<>();

		try (Connection conn = myconnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				khoa_model item = new khoa_model(
						rs.getInt("id"),
						rs.getString("ten_khoa"),
						rs.getString("mo_ta")
				);
				result.add(item);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Lỗi khi lấy danh sách khoa", e);
		}

		return result;
	}

	public khoa_model findById(int id) {
		String sql = "SELECT id, ten_khoa, mo_ta FROM khoa WHERE id = ?";

		try (Connection conn = myconnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, id);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new khoa_model(
							rs.getInt("id"),
							rs.getString("ten_khoa"),
							rs.getString("mo_ta")
					);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Lỗi khi tìm khoa theo ID", e);
		}

		return null;
	}

	public List<khoa_model> search(String keyword) {
		List<khoa_model> result = new ArrayList<>();
		String sql = "SELECT id, ten_khoa,mo_ta FROM khoa WHERE ten_khoa LIKE ?";
		try (PreparedStatement ps = myconnection.getConnection().prepareStatement(sql)) {
			ps.setString(1, "%" + keyword + "%");
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					khoa_model item = new khoa_model(
							rs.getInt("id"),
							rs.getString("ten_khoa"),
							rs.getString("mo_ta")
					);
					result.add(item);
				}
			}
			
		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi tìm kiếm khoa", e);
		}
		return result;
	}

	public int insert(khoa_model khoa) {
		String sql = "INSERT INTO khoa(ten_khoa, mo_ta) VALUES (?, ?)";

		try (Connection conn = myconnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, khoa.getTenKhoa());
			ps.setString(2, khoa.getMoTa());

			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				return 0;
			}

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					int newId = rs.getInt(1);
					khoa.setId(newId);
					return newId;
				}
			}
			return 0;
		} catch (SQLException e) {
			throw new RuntimeException("Lỗi khi thêm khoa", e);
		}
	}

	public boolean update(khoa_model khoa) {
		String sql = "UPDATE khoa SET ten_khoa = ?, mo_ta = ? WHERE id = ?";

		try (Connection conn = myconnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, khoa.getTenKhoa());
			ps.setString(2, khoa.getMoTa());
			ps.setInt(3, khoa.getId());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new RuntimeException("Lỗi khi cập nhật khoa", e);
		}
	}

	public boolean delete(int id) {
		String sql = "DELETE FROM khoa WHERE id = ?";

		try (Connection conn = myconnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new RuntimeException("Lỗi khi xóa khoa", e);
		}
	}
	public boolean KiemTraTenKhoaTrung(String tenKhoa) {
		String sql = "SELECT COUNT(*) From khoa WHERE ten_khoa = ?";
		try(PreparedStatement pstmt = myconnection.getConnection().prepareStatement(sql)) {
			pstmt.setString(1, tenKhoa);
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
