package controller;

import java.util.List;

import javax.swing.JOptionPane;

import dao.khoa_dao;
import dao.nganh_dao;
import model.khoa_model;
import model.nganh_model;
import view.nganh_view;

public class nganh_controller {
    private final nganh_view view;
    private final nganh_dao dao;
    private final khoa_dao khoaDao;

    public nganh_controller(nganh_view view) {
        this(view, new nganh_dao(), new khoa_dao());
    }

    public nganh_controller(nganh_view view, nganh_dao dao, khoa_dao khoaDao) {
        this.view = view;
        this.dao = dao;
        this.khoaDao = khoaDao;
    }

    private void loadDataToTable() {
        List<nganh_model> list = dao.findAll();
        view.setTableData(list);
    }

    private void loadKhoaToComboBox() {
        List<khoa_model> list = khoaDao.findAll();
        view.setKhoaComboBox(list);
    }

    private void refreshTableByKeyword() {
        String keyword = view.getTuKhoaTimKiem();
        if (keyword.isBlank()) {
            loadDataToTable();
            return;
        }

        List<nganh_model> list = dao.search(keyword);
        view.setTableData(list);
    }

    private boolean validateInput() {
        String tenNganh = view.getTenNganhText();
        String moTa = view.getMoTaText();
        int khoaId = view.getSelectedKhoaId();

        if (tenNganh.isBlank()) {
            JOptionPane.showMessageDialog(view, "Tên ngành không được để trống.");
            view.focusTenNganh();
            return false;
        }

        if (tenNganh.length() > 100) {
            JOptionPane.showMessageDialog(view, "Tên ngành tối đa 100 ký tự.");
            view.focusTenNganh();
            return false;
        }

        if (khoaId == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn khoa.");
            return false;
        }

        if (moTa.length() > 255) {
            JOptionPane.showMessageDialog(view, "Mô tả tối đa 255 ký tự.");
            return false;
        }

        return true;
    }

    public boolean kiemTraTrungNganh(String tenNganh) {
        if (dao.kiemTraTenNganhTrung(tenNganh)) {
            JOptionPane.showMessageDialog(view, "Tên ngành đã tồn tại. Vui lòng nhập lại.");
            view.focusTenNganh();
            return false;
        }
        return true;
    }

    public boolean kiemTraTrungNganhKhiSua(String tenNganh, int id) {
        if (dao.kiemTraTenNganhTrungKhiSua(tenNganh, id)) {
            JOptionPane.showMessageDialog(view, "Tên ngành đã tồn tại. Vui lòng nhập lại.");
            view.focusTenNganh();
            return false;
        }
        return true;
    }

    public void onThemClicked() {
        if (!validateInput()) {
            return;
        }

        if (!kiemTraTrungNganh(view.getTenNganhText())) {
            return;
        }

        nganh_model model = new nganh_model(
                0,
                view.getTenNganhText(),
                view.getSelectedKhoaId(),
                view.getMoTaText()
        );

        int newId = dao.insert(model);

        if (newId > 0) {
            JOptionPane.showMessageDialog(view, "Thêm ngành thành công.");
            refreshTableByKeyword();
            view.clearForm();
        } else {
            JOptionPane.showMessageDialog(view, "Thêm ngành thất bại.");
        }
    }

    public void onSuaClicked() {
        String idText = view.getIdText();
        if (idText.isBlank()) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn ngành cần sửa.");
            return;
        }

        if (!validateInput()) {
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "ID không hợp lệ.");
            return;
        }

        // Dùng hàm kiểm tra trùng khi sửa (bỏ qua chính nó)
        if (!kiemTraTrungNganhKhiSua(view.getTenNganhText(), id)) {
            return;
        }

        nganh_model model = new nganh_model(
                id,
                view.getTenNganhText(),
                view.getSelectedKhoaId(),
                view.getMoTaText()
        );

        boolean success = dao.update(model);

        if (success) {
            JOptionPane.showMessageDialog(view, "Cập nhật ngành thành công.");
            refreshTableByKeyword();
            view.clearForm();
        } else {
            JOptionPane.showMessageDialog(view, "Cập nhật ngành thất bại.");
        }
    }

    public void onXoaClicked() {
        String idText = view.getIdText();
        if (idText.isBlank()) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn ngành cần xóa.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Bạn có chắc chắn muốn xóa ngành này?",
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

        try {
            boolean success = dao.delete(id);
            if (success) {
                JOptionPane.showMessageDialog(view, "Xóa ngành thành công.");
                refreshTableByKeyword();
                view.clearForm();
            } else {
                JOptionPane.showMessageDialog(view, "Xóa ngành thất bại.");
            }
        } catch (RuntimeException ex) {
            // Bắt lỗi FK constraint khi ngành đang có hồ sơ nhân sự
            JOptionPane.showMessageDialog(view,
                    "Không thể xóa! Ngành này đang có hồ sơ nhân sự liên kết.");
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
        loadKhoaToComboBox(); // Load combobox khoa trước
        loadDataToTable();    // Sau đó load bảng ngành
    }
}