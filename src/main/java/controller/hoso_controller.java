package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import dao.khoa_dao;
import dao.nganh_dao;
import model.hoso_model;
import model.nganh_model;
import service.hoso_service;
import view.hoso_view;

public class hoso_controller {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0\\d{8,10}$");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    private final hoso_view view;
    private final hoso_service service;
    private final khoa_dao khoaDao;
    private final nganh_dao nganhDao;

    public hoso_controller(hoso_view view) {
        this(view, new hoso_service(), new khoa_dao(), new nganh_dao());
    }

    public hoso_controller(hoso_view view, hoso_service service, khoa_dao khoaDao, nganh_dao nganhDao) {
        this.view = view;
        this.service = service;
        this.khoaDao = khoaDao;
        this.nganhDao = nganhDao;
    }

    public void loadData() {
        loadLookupData();
        refreshTable();
    }

    private void loadLookupData() {
        view.setKhoaOptions(khoaDao.findAll());
        view.setNganhOptions(nganhDao.findAll());
    }

    public void onThemClicked() {
        if (!view.getIdText().isBlank()) {
            showMessage("ID được tạo tự động. Vui lòng làm mới form trước khi thêm mới.");
            return;
        }

        if (!validateInput(false)) {
            return;
        }

        hoso_model model = readFormData(null);
        try {
            int newId = service.add(model);
            refreshTable();
            if (newId > 0) {
                int index = service.findIndexById(newId);
                if (index >= 0) {
                    view.selectRow(index);
                }
            }
            showMessage("Thêm hồ sơ thành công.");
        } catch (RuntimeException ex) {
            showMessage("Thêm hồ sơ thất bại: " + getErrorMessage(ex));
        }
    }

    public void onSuaClicked() {
        int row = view.getSelectedRowIndex();
        if (row < 0) {
            showMessage("Vui lòng chọn hồ sơ cần sửa.");
            return;
        }

        Integer id = parseInteger(view.getIdText());
        if (id == null || id <= 0) {
            showMessage("ID hồ sơ không hợp lệ.");
            return;
        }

        if (!validateInput(true)) {
            return;
        }

        hoso_model model = readFormData(id);
        try {
            boolean success = service.update(model);
            if (success) {
                refreshTable();
                int newIndex = service.findIndexById(model.getId());
                if (newIndex >= 0) {
                    view.selectRow(newIndex);
                }
                showMessage("Cập nhật hồ sơ thành công.");
            } else {
                showMessage("Không thể cập nhật hồ sơ.");
            }
        } catch (RuntimeException ex) {
            showMessage("Cập nhật hồ sơ thất bại: " + getErrorMessage(ex));
        }
    }

    public void onXoaClicked() {
        int row = view.getSelectedRowIndex();
        if (row < 0) {
            showMessage("Vui lòng chọn hồ sơ cần xóa.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Bạn có chắc muốn xóa hồ sơ này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Integer id = parseInteger(view.getIdText());
        if (id == null || id <= 0) {
            showMessage("ID hồ sơ không hợp lệ.");
            return;
        }

        try {
            boolean success = service.deleteById(id);
            if (success) {
                refreshTable();
                view.clearForm();
                showMessage("Đã xóa hồ sơ.");
            } else {
                showMessage("Không thể xóa hồ sơ.");
            }
        } catch (RuntimeException ex) {
            showMessage("Xóa hồ sơ thất bại: " + getErrorMessage(ex));
        }
    }

    public void onLamMoiClicked() {
        view.clearForm();
        view.clearTimKiem();
        refreshTable();
    }

    public void onTimKiemClicked() {
        String keyword = view.getTuKhoaTimKiem();
        Integer khoaId = view.getTimKhoaId();
        Integer nganhId = view.getTimNganhId();
        String gioiTinh = view.getTimGioiTinh();
        String trinhDo = view.getTimTrinhDo();
        String chucVu = view.getTimChucVu();

        if (isSearchEmpty(keyword, khoaId, nganhId, gioiTinh, trinhDo, chucVu)) {
            refreshTable();
            return;
        }

        List<hoso_model> result = service.searchAdvanced(keyword, khoaId, nganhId, gioiTinh, trinhDo, chucVu);
        view.setTableData(result);
        if (result.isEmpty()) {
            showMessage("Không tìm thấy hồ sơ phù hợp.");
        }
    }

    public void onXoaLocTimKiemClicked() {
        view.clearTimKiem();
        refreshTable();
    }

    private boolean isSearchEmpty(String keyword, Integer khoaId, Integer nganhId, String gioiTinh, String trinhDo, String chucVu) {
        return (keyword == null || keyword.isBlank())
                && khoaId == null
                && nganhId == null
                && (gioiTinh == null || gioiTinh.isBlank())
                && (trinhDo == null || trinhDo.isBlank())
                && (chucVu == null || chucVu.isBlank());
    }

    private hoso_model readFormData(Integer id) {
        return new hoso_model(
                id,
                view.getHoTenText(),
                view.getNgaySinhText(),
                view.getGioiTinhText(),
                view.getSoDienThoaiText(),
                view.getEmailText(),
                view.getDiaChiText(),
                view.getSelectedKhoaId(),
                view.getSelectedNganhId(),
                view.getTrinhDoText(),
                view.getChucVuText()
        );
    }

    private boolean validateInput(boolean isEditing) {
        String idText = view.getIdText();
        String hoTen = view.getHoTenText();
        String ngaySinh = view.getNgaySinhText();
        String gioiTinh = view.getGioiTinhText();
        String soDienThoai = view.getSoDienThoaiText();
        String email = view.getEmailText();
        String diaChi = view.getDiaChiText();
        Integer khoaId = view.getSelectedKhoaId();
        Integer nganhId = view.getSelectedNganhId();
        String trinhDo = view.getTrinhDoText();
        String chucVu = view.getChucVuText();

        if (isEditing) {
            Integer id = parseInteger(idText);
            if (id == null || id <= 0) {
                showMessage("ID hồ sơ không hợp lệ.");
                return false;
            }
        } else if (!idText.isBlank()) {
            showMessage("ID được tạo tự động, không cho phép nhập tay.");
            return false;
        }

        if (hoTen.isBlank()) {
            showMessage("Họ tên không được để trống.");
            return false;
        }

        if (hoTen.length() > 100) {
            showMessage("Họ tên tối đa 100 ký tự.");
            return false;
        }

        if (!ngaySinh.isBlank() && !isValidDate(ngaySinh)) {
            showMessage("Ngày sinh không hợp lệ. Định dạng đúng: yyyy-MM-dd.");
            return false;
        }

        if (!gioiTinh.isBlank() && gioiTinh.length() > 10) {
            showMessage("Giới tính tối đa 10 ký tự.");
            return false;
        }

        if (!soDienThoai.isBlank() && !PHONE_PATTERN.matcher(soDienThoai).matches()) {
            showMessage("Số điện thoại không hợp lệ. Ví dụ: 0901234567.");
            return false;
        }

        if (!email.isBlank() && !EMAIL_PATTERN.matcher(email).matches()) {
            showMessage("Email không hợp lệ. Ví dụ: ten@domain.com.");
            return false;
        }

        if (!diaChi.isBlank() && diaChi.length() > 255) {
            showMessage("Địa chỉ tối đa 255 ký tự.");
            return false;
        }

        if (khoaId == null || khoaId <= 0) {
            showMessage("Vui lòng chọn khoa.");
            return false;
        }

        if (nganhId == null || nganhId <= 0) {
            showMessage("Vui lòng chọn ngành.");
            return false;
        }

        if (!isNganhBelongToKhoa(khoaId, nganhId)) {
            showMessage("Ngành được chọn không thuộc khoa đã chọn.");
            return false;
        }

        if (!trinhDo.isBlank() && trinhDo.length() > 100) {
            showMessage("Trình độ tối đa 100 ký tự.");
            return false;
        }

        if (!chucVu.isBlank() && chucVu.length() > 100) {
            showMessage("Chức vụ tối đa 100 ký tự.");
            return false;
        }

        return true;
    }

    private boolean isNganhBelongToKhoa(Integer khoaId, Integer nganhId) {
        nganh_model nganh = nganhDao.findById(nganhId);
        return nganh != null && nganh.getKhoaId() == khoaId;
    }

    private boolean isValidDate(String value) {
        try {
            LocalDate.parse(value, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    private void refreshTable() {
        List<hoso_model> list = service.findAll();
        view.setTableData(list);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(view, message);
    }

    private Integer parseInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String getErrorMessage(Throwable ex) {
        String message = ex.getMessage();
        if (message == null || message.isBlank()) {
            return "Loi khong xac dinh";
        }
        return message;
    }
}
