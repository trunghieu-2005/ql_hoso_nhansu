package view;

import model.user_model;
import view.ui.UiKit;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Hiển thị hồ sơ cá nhân của người dùng đang đăng nhập.
 * Tất cả vai trò đều được xem màn hình này.
 */
public class profile_view extends JPanel {

    private final user_model user;

    private static final Color C_PRIMARY = UiKit.PRIMARY_DARK;
    private static final Color C_BG      = UiKit.APP_BG;
    private static final Color C_AVATAR  = new Color(226, 239, 252);

    public profile_view(user_model user) {
        this.user = user;
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        setBackground(C_BG);

        JPanel card = new JPanel(new BorderLayout(0, 16));
        card.setBackground(UiKit.CARD_BG);
        card.setBorder(new CompoundBorder(UiKit.cardBorder(), new EmptyBorder(18, 18, 18, 18)));
        card.setPreferredSize(new Dimension(560, 370));

        // ── Tiêu đề ──
        JLabel lblTitle = new JLabel("Hồ sơ cá nhân");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(C_PRIMARY);

        // ── Avatar ──
        JLabel avatar = new JLabel(getUserInitial(), SwingConstants.CENTER);
        avatar.setOpaque(true);
        avatar.setBackground(C_AVATAR);
        avatar.setForeground(C_PRIMARY);
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 30));
        avatar.setBorder(new CompoundBorder(
            new EmptyBorder(0, 0, 10, 0),
            new CompoundBorder(
                new LineBorder(new Color(189, 210, 238), 1, true),
                new EmptyBorder(18, 22, 18, 22)
            )
        ));

        // ── Thông tin ──
        JPanel info = new JPanel(new GridLayout(0, 2, 10, 14));
        info.setOpaque(false);

        addInfoRow(info, "Tên đăng nhập:", user.getUsername());
        addInfoRow(info, "Vai trò:",       user.getRoleDisplay());
        addInfoRow(info, "ID tài khoản:",  String.valueOf(user.getId()));

        String permText;
        switch (user.getRole()) {
            case "admin":
                permText = "Toàn quyền hệ thống";
                break;
            case "can_bo_nhan_su":
                permText = "Quản lý Khoa, Ngành, Hồ sơ";
                break;
            default:
                permText = "Xem hồ sơ cá nhân";
        }
        addInfoRow(info, "Quyền hạn:", permText);

        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setOpaque(false);
        center.add(avatar, BorderLayout.NORTH);
        center.add(info, BorderLayout.CENTER);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(center, BorderLayout.CENTER);

        add(card);
    }

    private void addInfoRow(JPanel panel, String label, String value) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(UiKit.TEXT_SUB);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        val.setForeground(UiKit.TEXT);

        panel.add(lbl);
        panel.add(val);
    }

    private String getUserInitial() {
        String username = user.getUsername();
        if (username == null || username.isBlank()) {
            return "U";
        }
        return username.substring(0, 1).toUpperCase();
    }
}