package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.user_controller;
import model.user_model;
import view.ui.UiKit;

public class user_view extends JPanel {

    private final user_controller controller;

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;

    private static final Color C_PRIMARY = UiKit.PRIMARY_DARK;
    private static final Color C_BTN_ADD = UiKit.SUCCESS;
    private static final Color C_BTN_DEL = UiKit.DANGER;
    private static final Color C_BTN_ROLE= UiKit.WARNING;

    public user_view(user_controller controller) {
        this.controller = controller;
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UiKit.APP_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // ── Tiêu đề ──
        JLabel lblTitle = new JLabel("Quản lý Tài khoản Người dùng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(C_PRIMARY);

        // ── Thanh công cụ ──
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setOpaque(false);

        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm kiếm username...");
        UiKit.styleTextField(txtSearch);

        JButton btnSearch = makeBtn("Tìm kiếm", UiKit.INFO);
        JButton btnAdd    = makeBtn("Thêm tài khoản", C_BTN_ADD);
        JButton btnEdit   = makeBtn("Sửa", UiKit.INFO);
        JButton btnRole   = makeBtn("Phân quyền", C_BTN_ROLE);
        JButton btnDel    = makeBtn("Xóa", C_BTN_DEL);
        JButton btnRefresh= makeBtn("Làm mới", new Color(116, 130, 147));

        toolbar.add(txtSearch);
        toolbar.add(btnSearch);
        toolbar.add(new JSeparator(SwingConstants.VERTICAL));
        toolbar.add(btnAdd);
        toolbar.add(btnEdit);
        toolbar.add(btnRole);
        toolbar.add(btnDel);
        toolbar.add(btnRefresh);

        JPanel top = new JPanel(new BorderLayout(0, 8));
        top.setOpaque(false);
        top.add(lblTitle, BorderLayout.NORTH);
        top.add(toolbar, BorderLayout.SOUTH);

        // ── Bảng ──
        String[] columns = {"ID", "Tên đăng nhập", "Vai trò", "Mô tả quyền"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        UiKit.styleTable(table);

        // Ẩn cột ID
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);
        table.getColumnModel().getColumn(3).setPreferredWidth(350);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UiKit.BORDER));

        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // ── Sự kiện ──
        btnAdd.addActionListener(e -> showAddDialog());
        btnEdit.addActionListener(e -> showEditDialog());
        btnRole.addActionListener(e -> showRoleDialog());
        btnDel.addActionListener(e -> deleteSelected());
        btnRefresh.addActionListener(e -> loadData());
        btnSearch.addActionListener(e -> filterTable(txtSearch.getText()));
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { filterTable(txtSearch.getText()); }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) showEditDialog();
            }
        });
    }

    // ─── Load dữ liệu ────────────────────────────────────────────
    private void loadData() {
        tableModel.setRowCount(0);
        List<user_model> list = controller.getAllUsers();
        for (user_model u : list) {
            tableModel.addRow(new Object[]{
                u.getId(),
                u.getUsername(),
                u.getRoleDisplay(),
                getRoleDescription(u.getRole())
            });
        }
    }

    private void filterTable(String keyword) {
        tableModel.setRowCount(0);
        List<user_model> list = controller.getAllUsers();
        for (user_model u : list) {
            if (keyword.isEmpty() || u.getUsername().toLowerCase().contains(keyword.toLowerCase())) {
                tableModel.addRow(new Object[]{
                    u.getId(), u.getUsername(), u.getRoleDisplay(), getRoleDescription(u.getRole())
                });
            }
        }
    }

    // ─── Dialog Thêm tài khoản ───────────────────────────────────
    private void showAddDialog() {
        JDialog dialog = createFormDialog("Thêm tài khoản mới");
        JTextField txtUser  = new JTextField(20);
        JPasswordField txtPass = new JPasswordField(20);
        JPasswordField txtConfirm = new JPasswordField(20);
        JComboBox<String> cboRole = createRoleCombo();
        UiKit.styleTextField(txtUser);
        UiKit.styleTextField(txtPass);
        UiKit.styleTextField(txtConfirm);
        UiKit.styleComboBox(cboRole);

        JPanel form = buildFormPanel(new String[]{"Tên đăng nhập *", "Mật khẩu *", "Xác nhận mật khẩu *", "Vai trò *"},
                new JComponent[]{txtUser, txtPass, txtConfirm, cboRole});

        JButton btnSave = makeBtn("Lưu", C_BTN_ADD);
        btnSave.addActionListener(e -> {
            String role = getRoleKey((String) cboRole.getSelectedItem());
            String err = controller.addUser(txtUser.getText(),
                    new String(txtPass.getPassword()),
                    new String(txtConfirm.getPassword()), role);
            if (err != null) { JOptionPane.showMessageDialog(dialog, err, "Lỗi", JOptionPane.ERROR_MESSAGE); }
            else { JOptionPane.showMessageDialog(dialog, "Thêm tài khoản thành công!"); dialog.dispose(); loadData(); }
        });

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(wrapBtns(btnSave, makeCloseBtn(dialog)), BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ─── Dialog Sửa tài khoản ────────────────────────────────────
    private void showEditDialog() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần sửa!"); return; }

        int id = (int) tableModel.getValueAt(row, 0);
        String curUsername = (String) tableModel.getValueAt(row, 1);
        String curRole = getRoleKeyByDisplay((String) tableModel.getValueAt(row, 2));

        JDialog dialog = createFormDialog("Sửa tài khoản");
        JTextField txtUser  = new JTextField(curUsername, 20);
        JPasswordField txtPass = new JPasswordField(20);
        JComboBox<String> cboRole = createRoleCombo();
        cboRole.setSelectedItem(getRoleDisplay(curRole));
        UiKit.styleTextField(txtUser);
        UiKit.styleTextField(txtPass);
        UiKit.styleComboBox(cboRole);

        JPanel note = new JPanel(new FlowLayout(FlowLayout.LEFT));
        note.setOpaque(false);
        JLabel lblNote = new JLabel("* Để trống mật khẩu nếu không muốn thay đổi");
        lblNote.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblNote.setForeground(Color.GRAY);
        note.add(lblNote);

        JPanel form = buildFormPanel(new String[]{"Tên đăng nhập *", "Mật khẩu mới", "Vai trò *"},
                new JComponent[]{txtUser, txtPass, cboRole});

        JButton btnSave = makeBtn("Cập nhật", new Color(60, 120, 200));
        btnSave.addActionListener(e -> {
            String role = getRoleKey((String) cboRole.getSelectedItem());
            String pass = new String(txtPass.getPassword());
            String err = controller.updateUser(id, txtUser.getText(), pass.isEmpty() ? null : pass, role);
            if (err != null) { JOptionPane.showMessageDialog(dialog, err, "Lỗi", JOptionPane.ERROR_MESSAGE); }
            else { JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!"); dialog.dispose(); loadData(); }
        });

        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.setOpaque(false);
        center.add(form, BorderLayout.CENTER);
        center.add(note, BorderLayout.SOUTH);

        dialog.add(center, BorderLayout.CENTER);
        dialog.add(wrapBtns(btnSave, makeCloseBtn(dialog)), BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ─── Dialog Phân quyền ───────────────────────────────────────
    private void showRoleDialog() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần phân quyền!"); return; }

        int id = (int) tableModel.getValueAt(row, 0);
        String curRole = getRoleKeyByDisplay((String) tableModel.getValueAt(row, 2));

        JDialog dialog = createFormDialog("Phân quyền tài khoản");
        dialog.setSize(380, 220);

        JComboBox<String> cboRole = createRoleCombo();
        cboRole.setSelectedItem(getRoleDisplay(curRole));
        UiKit.styleComboBox(cboRole);

        JPanel form = buildFormPanel(new String[]{"Chọn vai trò mới *"},
                new JComponent[]{cboRole});

        JButton btnSave = makeBtn("Phân quyền", C_BTN_ROLE);
        btnSave.addActionListener(e -> {
            String role = getRoleKey((String) cboRole.getSelectedItem());
            int confirm = JOptionPane.showConfirmDialog(dialog,
                "Xác nhận phân quyền \"" + getRoleDisplay(role) + "\" cho tài khoản này?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;
            String err = controller.assignRole(id, role);
            if (err != null) { JOptionPane.showMessageDialog(dialog, err, "Lỗi", JOptionPane.ERROR_MESSAGE); }
            else { JOptionPane.showMessageDialog(dialog, "Phân quyền thành công!"); dialog.dispose(); loadData(); }
        });

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(wrapBtns(btnSave, makeCloseBtn(dialog)), BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ─── Xóa tài khoản ───────────────────────────────────────────
    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần xóa!"); return; }

        int id = (int) tableModel.getValueAt(row, 0);
        String username = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn xóa tài khoản \"" + username + "\"?",
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        String err = controller.deleteUser(id);
        if (err != null) JOptionPane.showMessageDialog(this, err, "Lỗi", JOptionPane.ERROR_MESSAGE);
        else { JOptionPane.showMessageDialog(this, "Đã xóa tài khoản!"); loadData(); }
    }

    // ─── UI Helpers ──────────────────────────────────────────────
    private JDialog createFormDialog(String title) {
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        d.setSize(420, 380);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout(10, 10));
        d.getRootPane().setBorder(new EmptyBorder(15, 15, 10, 15));
        return d;
    }

    private JPanel buildFormPanel(String[] labels, JComponent[] fields) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(UiKit.CARD_BG);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 4, 6, 4);
        gc.anchor = GridBagConstraints.WEST;
        for (int i = 0; i < labels.length; i++) {
            gc.gridx = 0; gc.gridy = i; gc.weightx = 0;
            p.add(new JLabel(labels[i]), gc);
            gc.gridx = 1; gc.weightx = 1; gc.fill = GridBagConstraints.HORIZONTAL;
            p.add(fields[i], gc);
            gc.fill = GridBagConstraints.NONE;
        }
        return p;
    }

    private JPanel wrapBtns(JButton... btns) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        p.setBackground(UiKit.APP_BG);
        for (JButton b : btns) p.add(b);
        return p;
    }

    private JButton makeCloseBtn(JDialog d) {
        JButton b = makeBtn("Đóng", Color.GRAY);
        b.addActionListener(e -> d.dispose());
        return b;
    }

    private JButton makeBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        UiKit.styleButton(btn, bg);
        return btn;
    }

    private JComboBox<String> createRoleCombo() {
        return new JComboBox<>(new String[]{"Quản trị viên", "Cán bộ nhân sự"});
    }

    private String getRoleKey(String display) {
        switch (display) {
            case "Quản trị viên":   return "admin";
            case "Cán bộ nhân sự":  return "can_bo_nhan_su";
            default:                return "can_bo_nhan_vien";
        }
    }

    private String getRoleKeyByDisplay(String display) {
        return getRoleKey(display);
    }

    private String getRoleDisplay(String key) {
        switch (key) {
            case "admin":            return "Quản trị viên";
            case "can_bo_nhan_su":   return "Cán bộ nhân sự";
            default:                 return "Cán bộ nhân viên";
        }
    }

    private String getRoleDescription(String role) {
        switch (role) {
            case "admin":           return "Quản lý user, Khoa, Ngành";
            case "can_bo_nhan_su":  return "Quản lý Khoa, Ngành,Xem hồ sơ cá nhân";
            case "can_bo_nhan_vien":return "Chỉ xem hồ sơ cá nhân";
            default:                return "Không xác định";
        }
    }
}