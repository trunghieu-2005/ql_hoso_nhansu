package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class myconnection {

	public static Connection getConnection() {
		Connection conn = null;

		try {
			// Đăng ký MySQL Driver vào DriverManager
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Thông tin kết nối đến CSDL
			String url = "jdbc:mysql://localhost:3307/quanlyhoso";
			String username = "root";
			String password = "";

			// Tạo kết nối
			conn = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException | SQLException e) {
			throw new RuntimeException("Không thể kết nối database quanlyhoso", e);
		}

		return conn;
	}

	// Ngừng kết nối
	public static void closeConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException("Không thể đóng kết nối database", e);
		}
	}
}
