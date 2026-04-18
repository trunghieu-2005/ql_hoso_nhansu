package controller;

import java.util.List;

import javax.swing.JOptionPane;

import dao.khoa_dao;
import model.khoa_model;
import view.khoa_view;

public class khoa_controller {
	private final khoa_view view;
	private final khoa_dao dao;

	public khoa_controller(khoa_view view) {
		this(view, new khoa_dao());
	}

	public khoa_controller(khoa_view view, khoa_dao dao) {
		this.view = view;
		this.dao = dao;
	}

	private void loadDataToTable() {
		List<khoa_model> list = dao.findAll();
		view.setTableData(list);
	}

	private void refreshTableByKeyword() {
		String keyword = view.getTuKhoaTimKiem();
		if (keyword.isBlank()) {
			loadDataToTable();
			return;
		}

		List<khoa_model> list = dao.search(keyword);
		view.setTableData(list);
	}

	private boolean validateInput() {
		String tenKhoa = view.getTenKhoaText();
		String moTa = view.getMoTaText();

		if (tenKhoa.isBlank()) {
			JOptionPane.showMessageDialog(view, "Tên khoa không được để trống.");
			view.focusTenKhoa();
			return false;
		}

		if (tenKhoa.length() > 100) {
			JOptionPane.showMessageDialog(view, "Tên khoa tối đa 100 ký tự.");
			view.focusTenKhoa();
			return false;
		}

		if (moTa.length() > 255) {
			JOptionPane.showMessageDialog(view, "Mô tả tối đa 255 ký tự.");
			view.focusMoTa();
			return false;
		}

		return true;
	}

	public boolean kiemTraTrungKhoa(String tenKhoa) {
		if (dao.KiemTraTenKhoaTrung(tenKhoa)) {
			JOptionPane.showMessageDialog(view, "Tên khoa đã tồn tại. Vui lòng nhập lại.");
			view.focusTenKhoa();
			return false;
		}
		return true;
	}

	public void onThemClicked() {
		if (!validateInput()) {
			return;
		}
		
		if (!kiemTraTrungKhoa(view.getTenKhoaText())) {
			return;
		}

		khoa_model model = new khoa_model(view.getTenKhoaText(), view.getMoTaText());
		int newId = dao.insert(model);

		if (newId > 0) {
			JOptionPane.showMessageDialog(view, "Thêm khoa thành công.");
			refreshTableByKeyword();
			view.clearForm();
		} else {
			JOptionPane.showMessageDialog(view, "Thêm khoa thất bại.");
		}
	}

	public void onSuaClicked() {
		String idText = view.getIdText();
		if (idText.isBlank()) {
			JOptionPane.showMessageDialog(view, "Vui lòng chọn khoa cần sửa.");
			return;
		}

		if (!validateInput()) {
			return;
		}

		if (!kiemTraTrungKhoa(view.getTenKhoaText())) {
			return;
		} 

		int id;
		try {
			id = Integer.parseInt(idText);
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(view, "ID không hợp lệ.");
			return;
		}

		khoa_model model = new khoa_model(id, view.getTenKhoaText(), view.getMoTaText());
		boolean success = dao.update(model);

		if (success) {
			JOptionPane.showMessageDialog(view, "Cập nhật khoa thành công.");
			refreshTableByKeyword();
			view.clearForm();
		} else {
			JOptionPane.showMessageDialog(view, "Cập nhật khoa thất bại.");
		}
	}

	public void onXoaClicked() {
		String idText = view.getIdText();
		if (idText.isBlank()) {
			JOptionPane.showMessageDialog(view, "Vui lòng chọn khoa cần xóa.");
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(
				view,
				"Bạn có chắc chắn muốn xóa khoa này?",
				"Xác nhận xóa",
				JOptionPane.YES_NO_OPTION
		);
		if (confirm != JOptionPane.YES_OPTION) {
			return;
		}

		int id;
		try {
			id = Integer.parseInt(idText);
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(view, "ID không hợp lệ.");
			return;
		}

		boolean success = dao.delete(id);
		if (success) {
			JOptionPane.showMessageDialog(view, "Xóa khoa thành công.");
			refreshTableByKeyword();
			view.clearForm();
		} else {
			JOptionPane.showMessageDialog(view, "Xóa khoa thất bại.");
		}
	}

	public void onTimKiemClicked() {
		refreshTableByKeyword();
	}

	public void onLamMoiClicked() {
		view.clearForm();
		view.clearTuKhoaTimKiem();
		loadDataToTable();
	}

	public void loadData() {
    List<khoa_model> list = dao.findAll();
    view.setTableData(list);
}
}
