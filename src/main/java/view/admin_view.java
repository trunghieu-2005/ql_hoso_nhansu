package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controller.admin_controller;
import view.ui.UiKit;

public class admin_view extends JFrame {
    private static final Color APP_BG = UiKit.APP_BG;
    private static final Color CARD_BG = UiKit.CARD_BG;
    private static final Color CARD_BORDER = UiKit.BORDER;
    private static final Color BRAND_1 = UiKit.PRIMARY_DARK;
    private static final Color NAV_BG = UiKit.PRIMARY;
    private static final Color TEXT_MAIN = UiKit.TEXT;
    private static final Color TEXT_SUB = UiKit.TEXT_SUB;

    private admin_controller controller;

    private JButton btnQuanLyHoSo;
    private JButton btnQuanLyKhoa;
    private JButton btnThoat;

    public admin_view() {
        initUI();
    }

    private void initUI() {
        setTitle("Admin - Quản lý hồ sơ nhân sự");
        setIconImage(createHrLogoImage(64));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 760);
        setMinimumSize(new Dimension(1120, 680));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(14, 14));
        root.setBorder(new EmptyBorder(14, 14, 14, 14));
        root.setBackground(APP_BG);
        setContentPane(root);

        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildDashboard(), BorderLayout.CENTER);

        bindActions();
    }

    private JPanel buildSidebar() {
        GradientPanel sidebar = new GradientPanel(BRAND_1, NAV_BG);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(280, 0));
        sidebar.setBorder(new EmptyBorder(22, 18, 22, 18));

        JLabel lblUni = new JLabel("ADMIN PANEL");
        lblUni.setForeground(Color.WHITE);
        lblUni.setFont(new Font("Segoe UI", Font.BOLD, 21));

        JLabel lblSub = new JLabel("Quản lý hệ thống nhân sự");
        lblSub.setForeground(new Color(203, 220, 245));
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel badge = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        badge.setOpaque(false);
        JLabel logo = new JLabel(new ImageIcon(createHrLogoImage(28)));
        JLabel online = new JLabel("Online - Quản trị viên");
        online.setForeground(new Color(219, 234, 255));
        online.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        badge.add(logo);
        badge.add(online);

        btnThoat = createSidebarButton("Thoát", new Color(228, 93, 93));

        sidebar.add(lblUni);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(lblSub);
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(badge);
        sidebar.add(Box.createVerticalStrut(22));
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnThoat);
        return sidebar;
    }

    private JButton createSidebarButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        UiKit.styleButton(btn, bg);
        return btn;
    }

    private JPanel buildDashboard() {
        JPanel dashboard = new JPanel(new BorderLayout(0, 12));
        dashboard.setOpaque(false);
        dashboard.add(buildHeaderBanner(), BorderLayout.NORTH);
        dashboard.add(buildContentGrid(), BorderLayout.CENTER);
        return dashboard;
    }

    private JPanel buildHeaderBanner() {
        GradientPanel banner = new GradientPanel(new Color(16, 77, 168), new Color(45, 133, 214));
        banner.setLayout(new BorderLayout());
        banner.setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel title = new JLabel("BẢNG ĐIỀU KHIỂN QUẢN TRỊ");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 27));

        JLabel desc = new JLabel("Quản lý các chức năng hệ thống từ một màn hình trung tâm.");
        desc.setForeground(new Color(225, 238, 255));
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(title);
        left.add(Box.createVerticalStrut(4));
        left.add(desc);

        banner.add(left, BorderLayout.CENTER);
        return banner;
    }

    private JPanel buildContentGrid() {
        JPanel wrap = new JPanel(new BorderLayout(0, 12));
        wrap.setOpaque(false);

        JPanel modules = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        modules.setOpaque(false);
        modules.add(createMenuTile("Quản lý hồ sơ", "Mở", true, false));
        modules.add(createMenuTile("Quản lý khoa", "Mở", false, false));

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(CARD_BG);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER),
                new EmptyBorder(12, 14, 12, 14)
        ));

        JLabel noticeTitle = new JLabel("Thông báo hệ thống");
        noticeTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        noticeTitle.setForeground(TEXT_MAIN);

        JLabel noticeBody = new JLabel("Bạn có thể thêm chức năng mới vào các ô menu mà không cần sửa kiến trúc admin.");
        noticeBody.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        noticeBody.setForeground(TEXT_SUB);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(noticeTitle);
        text.add(Box.createVerticalStrut(3));
        text.add(noticeBody);
        footer.add(text, BorderLayout.CENTER);

        wrap.add(modules, BorderLayout.CENTER);
        wrap.add(footer, BorderLayout.SOUTH);
        return wrap;
    }

    private JPanel createMenuTile(String title, String actionText, boolean isHoSo, boolean disabled) {
        JPanel card = new JPanel(new BorderLayout(0, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(237, 244, 254));
                g2.fillRoundRect(8, 8, getWidth() - 16, 28, 12, 12);
                g2.dispose();
            }
        };
        card.setPreferredSize(new Dimension(230, 110));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(TEXT_MAIN);

        JButton actionBtn = new JButton(actionText);
        UiKit.styleButton(actionBtn, isHoSo ? UiKit.INFO : new Color(96, 125, 139));
        actionBtn.setEnabled(!disabled);

        if (isHoSo && !disabled) {
            btnQuanLyHoSo = actionBtn;
        } else if (!disabled) {
            btnQuanLyKhoa = actionBtn;
        }

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        bottom.setOpaque(false);
        bottom.add(actionBtn);

        card.add(lblTitle, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);
        return card;
    }

    private void bindActions() {
        btnQuanLyHoSo.addActionListener(e -> runControllerAction(() -> controller.onMoQuanLyHoSo()));
        btnQuanLyKhoa.addActionListener(e -> runControllerAction(() -> controller.onMoQuanLyKhoa()));
        btnThoat.addActionListener(e -> runControllerAction(() -> controller.onThoatClicked()));
    }

    private void runControllerAction(Runnable action) {
        if (controller != null) {
            action.run();
        }
    }

    private Image createHrLogoImage(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int pad = Math.max(2, size / 16);
        int inner = size - pad * 2;

        g2.setPaint(new GradientPaint(0, 0, new Color(43, 123, 235), size, size, new Color(26, 84, 171)));
        g2.fillRoundRect(pad, pad, inner, inner, size / 3, size / 3);

        g2.setColor(new Color(255, 255, 255, 230));
        g2.setStroke(new BasicStroke(Math.max(1.5f, size / 20f)));
        g2.drawRoundRect(pad, pad, inner, inner, size / 3, size / 3);

        int centerX = size / 2;
        int centerY = size / 2;
        int head = Math.max(3, size / 8);

        g2.setColor(Color.WHITE);
        g2.fillOval(centerX - head, centerY - head - size / 8, head * 2, head * 2);
        g2.fillRoundRect(centerX - head * 2, centerY, head * 4, size / 5, head, head);

        int small = Math.max(2, size / 11);
        g2.setColor(new Color(225, 239, 255));
        g2.fillOval(centerX - size / 4 - small, centerY - size / 8 - small, small * 2, small * 2);
        g2.fillOval(centerX + size / 4 - small, centerY - size / 8 - small, small * 2, small * 2);
        g2.fillRoundRect(centerX - size / 4 - small * 2, centerY + small / 2, small * 4, size / 7, small, small);
        g2.fillRoundRect(centerX + size / 4 - small * 2, centerY + small / 2, small * 4, size / 7, small, small);

        g2.dispose();
        return img;
    }

    public void setController(admin_controller controller) {
        this.controller = controller;
    }

    private static class GradientPanel extends JPanel {
        private final Color start;
        private final Color end;

        GradientPanel(Color start, Color end) {
            this.start = start;
            this.end = end;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, start, getWidth(), getHeight(), end);
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.setColor(new Color(255, 255, 255, 45));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
