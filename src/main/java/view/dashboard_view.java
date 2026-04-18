package view;

import app.SessionManager;
import controller.auth_controller;
import controller.user_controller;
import model.user_model;
import view.ui.UiKit;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class dashboard_view extends JFrame {

    private final auth_controller authCtrl;
    private final user_model currentUser;
    private final SessionManager session = SessionManager.getInstance();

  
    private static final Color C_SIDEBAR   = new Color(15, 53, 92);
    private static final Color C_SIDEBAR_H = new Color(24, 77, 130);
    private static final Color C_CONTENT   = UiKit.APP_BG;
    private static final Color C_HEADER    = UiKit.PRIMARY;
    private static final Color C_WHITE     = Color.WHITE;

    private JPanel contentArea;

    public dashboard_view(auth_controller authCtrl, user_model currentUser) {
        this.authCtrl    = authCtrl;
        this.currentUser = currentUser;
        initUI();
    }

    private void initUI() {
        setTitle("Quản lý Hồ sơ Nhân sự - " + currentUser.getRoleDisplay());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1260, 760);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { confirmLogout(); }
        });

        JPanel root = new JPanel(new BorderLayout());

        // ── Header ──────────────────────────────────────────────
        root.add(buildHeader(), BorderLayout.NORTH);

        // ── Sidebar + Content ────────────────────────────────────
        JPanel center = new JPanel(new BorderLayout());
        center.add(buildSidebar(), BorderLayout.WEST);

        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(C_CONTENT);
        showWelcome();
        center.add(contentArea, BorderLayout.CENTER);

        root.add(center, BorderLayout.CENTER);
        add(root);
    }

    // ─── Header ──────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(C_HEADER);
        h.setPreferredSize(new Dimension(1100, 72));
        h.setBorder(new EmptyBorder(8, 20, 8, 20));

        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ NHÂN SỰ ĐẠI HỌC");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(C_WHITE);

        JLabel lblSub = new JLabel("Quản trị dữ liệu hồ sơ, khoa, ngành và tài khoản tập trung");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(207, 224, 245));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(lblTitle);
        left.add(Box.createVerticalStrut(2));
        left.add(lblSub);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        right.setOpaque(false);

        JLabel lblUser = new JLabel("  " + currentUser.getUsername()
                + "  |  " + currentUser.getRoleDisplay());
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setOpaque(true);
        lblUser.setBorder(new EmptyBorder(6, 10, 6, 10));
        lblUser.setBackground(new Color(32, 93, 154));
        lblUser.setForeground(new Color(227, 239, 255));

        JButton btnLogout = makeHeaderBtn("Đăng xuất ");
        btnLogout.addActionListener(e -> confirmLogout());

        right.add(lblUser);
        right.add(btnLogout);

        h.add(left, BorderLayout.WEST);
        h.add(right, BorderLayout.EAST);
        return h;
    }

    // ─── Sidebar ─────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(C_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(new EmptyBorder(14, 0, 14, 0));

        // ── Nhóm: Trang chủ ──
        sidebar.add(makeSidebarItem(" Trang chủ", e -> showWelcome()));
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(makeSeparator("CHỨC NĂNG"));

        // ── Quản lý người dùng (chỉ Admin) ──
        if (session.canManageUsers()) {
            sidebar.add(makeSidebarItem("Quản lý tài khoản", e -> openUserManagement()));
        }

        // ── Quản lý Khoa (Admin + CB Nhân sự) ──
        if (session.canManageKhoaNganh()) {
            sidebar.add(makeSidebarItem("Quản lý Khoa", e -> openKhoaManagement()));
            sidebar.add(makeSidebarItem("Quản lý Ngành", e -> openNganhManagement()));
        }

        // ── Quản lý Hồ sơ (Admin + CB Nhân sự) ──
        if (session.canManageHoSo()) {
            sidebar.add(makeSidebarItem("Quản lý Hồ sơ", e -> openHosoManagement()));
           
        }

        // ── Xem hồ sơ cá nhân (tất cả) ──
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(makeSeparator("CÁ NHÂN"));
        sidebar.add(makeSidebarItem("Đổi mật khẩu", e -> openChangePassword()));

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(makeSidebarItem("Đăng xuất", e -> confirmLogout()));

        return sidebar;
    }

    // ─── Trang chào mừng ─────────────────────────────────────────
    private void showWelcome() {
        contentArea.removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(C_CONTENT);

        JPanel card = new JPanel(new GridLayout(0, 1, 0, 10));
        card.setBackground(C_WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(UiKit.BORDER, 1, true),
            new EmptyBorder(28, 36, 28, 36)
        ));

        JLabel ico  = new JLabel("Trung tâm điều phối", SwingConstants.CENTER);
        ico.setOpaque(true);
        ico.setBorder(new EmptyBorder(10, 12, 10, 12));
        ico.setBackground(new Color(235, 244, 254));
        ico.setForeground(UiKit.PRIMARY_DARK);
        ico.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel lbl1 = new JLabel("Chào mừng, " + currentUser.getUsername() + "!", SwingConstants.CENTER);
        lbl1.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl1.setForeground(C_HEADER);

        JLabel lbl2 = new JLabel("Vai trò: " + currentUser.getRoleDisplay(), SwingConstants.CENTER);
        lbl2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl2.setForeground(Color.GRAY);

        JLabel lbl3 = new JLabel(getPermissionSummary(), SwingConstants.CENTER);
        lbl3.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lbl3.setForeground(UiKit.INFO);

        card.add(ico); card.add(lbl1); card.add(lbl2);
        card.add(Box.createVerticalStrut(10));
        card.add(lbl3);

        panel.add(card);
        contentArea.add(panel, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    private String getPermissionSummary() {
        if (session.isAdmin()) return "Bạn có toàn quyền: Tài khoản, Khoa, Ngành và Hồ sơ.";
        if (session.isCanBoNhanSu()) return "Bạn có quyền quản lý Khoa, Ngành và Hồ sơ nhân sự.";
        return "Bạn có quyền xem hồ sơ cá nhân của mình.";
    }

    // ─── Mở các màn hình chức năng ───────────────────────────────
    private void openUserManagement() {
        if (!session.canManageUsers()) { accessDenied(); return; }
        loadContent(new user_view(new user_controller()));
    }

   private void openKhoaManagement() {
    if (!session.canManageKhoaNganh()) { accessDenied(); return; }
    loadContent(new khoa_view());
}

    private void openNganhManagement() {
    if (!session.canManageKhoaNganh()) { accessDenied(); return; }
    loadContent(new nganh_view());
}

   private void openHosoManagement() {
    if (!session.canManageHoSo()) { accessDenied(); return; }

    loadContent(new hoso_view());
}

    

    private void openMyProfile() {
        loadContent(new profile_view(currentUser));
    }

    private void openChangePassword() {
        change_password_dialog dialog = new change_password_dialog(this, currentUser, new user_controller());
        dialog.setVisible(true);
    }

    // ─── Load panel vào content area ─────────────────────────────
    private void loadContent(JPanel panel) {
        contentArea.removeAll();
        contentArea.add(panel, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    // ─── Đăng xuất ───────────────────────────────────────────────
  private void confirmLogout() {
    int confirm = JOptionPane.showConfirmDialog(this,
        "Bạn có chắc muốn đăng xuất?", "Xác nhận",
        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

    if (confirm == JOptionPane.YES_OPTION) {
        if (authCtrl != null) {
            authCtrl.doLogout(this);
        } else {
            System.out.println("authCtrl NULL !!!");
        }
    }
}

    private void accessDenied() {
        JOptionPane.showMessageDialog(this,
            "Bạn không có quyền truy cập chức năng này!",
            "Từ chối truy cập", JOptionPane.WARNING_MESSAGE);
    }

    // ─── UI helpers ──────────────────────────────────────────────
    private JButton makeSidebarItem(String text, ActionListener al) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(new Color(200, 220, 255));
        btn.setBackground(C_SIDEBAR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(11, 20, 11, 10));
        btn.setMaximumSize(new Dimension(250, 44));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(al);
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(C_SIDEBAR_H); btn.setForeground(C_WHITE); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(C_SIDEBAR); btn.setForeground(new Color(200, 220, 255)); }
        });
        return btn;
    }

    private JLabel makeSeparator(String text) {
        JLabel lbl = new JLabel("  " + text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(new Color(120, 160, 220));
        lbl.setBorder(new EmptyBorder(8, 10, 4, 10));
        lbl.setMaximumSize(new Dimension(220, 26));
        return lbl;
    }

    private JButton makeHeaderBtn(String text) {
        JButton btn = new JButton(text);
        UiKit.styleButton(btn, new Color(8, 57, 101));
        return btn;
    }

  
    public void close() { dispose(); }
}