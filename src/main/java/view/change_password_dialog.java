package view;

import controller.user_controller;
import model.user_model;
import view.ui.UiKit;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class change_password_dialog extends JDialog {

    private final user_model user;
    private final user_controller controller;

    private JPasswordField txtOld, txtNew, txtConfirm;

    public change_password_dialog(Frame parent, user_model user, user_controller controller) {
        super(parent, "Đổi mật khẩu", true);
        this.user = user;
        this.controller = controller;
        initUI();
    }

    private void initUI() {
        setSize(430, 330);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(new EmptyBorder(15, 20, 10, 20));
        getContentPane().setBackground(UiKit.CARD_BG);

        // ── Tiêu đề ──
        JLabel lblTitle = new JLabel("Đổi mật khẩu");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(UiKit.PRIMARY_DARK);

        // ── Form ──
        JPanel form = new JPanel(new GridLayout(3, 2, 8, 12));
        form.setOpaque(false);

        txtOld     = new JPasswordField();
        txtNew     = new JPasswordField();
        txtConfirm = new JPasswordField();

        UiKit.styleTextField(txtOld);
        UiKit.styleTextField(txtNew);
        UiKit.styleTextField(txtConfirm);

        form.add(new JLabel("Mật khẩu hiện tại *:")); form.add(txtOld);
        form.add(new JLabel("Mật khẩu mới *:"));       form.add(txtNew);
        form.add(new JLabel("Xác nhận mật khẩu *:"));  form.add(txtConfirm);

        // ── Nút ──
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);

        JButton btnSave  = makeBtn("Lưu", UiKit.SUCCESS);
        JButton btnClose = makeBtn("Đóng", new Color(116, 130, 147));

        btnSave.addActionListener(e -> doChange());
        btnClose.addActionListener(e -> dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnClose);

        add(lblTitle, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void doChange() {
        String err = controller.changePassword(
            user.getId(),
            new String(txtOld.getPassword()),
            new String(txtNew.getPassword()),
            new String(txtConfirm.getPassword())
        );
        if (err != null) {
            JOptionPane.showMessageDialog(this, err, "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
            dispose();
        }
    }

    private JButton makeBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        UiKit.styleButton(btn, bg);
        return btn;
    }
}