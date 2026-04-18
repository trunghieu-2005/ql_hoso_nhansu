package controller;

import javax.swing.JOptionPane;

import view.admin_view;
import view.hoso_view;
import view.khoa_view;

public class admin_controller {
    private final admin_view view;

    public admin_controller(admin_view view) {
        this.view = view;
    }

    public void onMoQuanLyHoSo() {
        hoso_view hosoView = new hoso_view();
        hoso_controller hosoController = new hoso_controller(hosoView);
        hosoController.loadData();
    }

    public void onMoQuanLyKhoa() {
        new khoa_view();
    }

    public void onThoatClicked() {
        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Bạn có chắc muốn thoát màn hình admin?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            view.dispose();
        }
    }

    public void start() {
        view.setController(this);
        view.setVisible(true);
    }
}
