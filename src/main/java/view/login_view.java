package view;

import controller.auth_controller;
import view.ui.UiKit;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class login_view extends JFrame {

    private auth_controller controller;

    public login_view() {
        initUI();
    }

    public void setController(auth_controller controller) {
        this.controller = controller;
    }

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblError;

    private static final Color COLOR_PRIMARY = UiKit.PRIMARY;
    private static final Color COLOR_SECONDARY = UiKit.APP_BG;
    private static final Color COLOR_ERROR = new Color(176, 55, 55);

    public login_view(auth_controller controller) {
        this.controller = controller;
        initUI();
    }

    private void initUI() {
        setTitle("Đăng nhập - Hệ thống Quản lý Hồ sơ Nhân sự");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(860, 520));
        setSize(920, 560);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_SECONDARY);
        mainPanel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JPanel shell = new JPanel(new GridLayout(1, 2, 18, 0));
        shell.setOpaque(false);

        shell.add(buildBrandPanel());
        shell.add(buildFormPanel());

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        JLabel lblFooter = new JLabel("© 2026 - Hệ thống Quản lý Hồ sơ Nhân sự");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(new Color(118, 131, 149));
        footer.add(lblFooter);

        mainPanel.add(shell, BorderLayout.CENTER);
        mainPanel.add(footer, BorderLayout.SOUTH);

        add(mainPanel);

        btnLogin.addActionListener(e -> doLogin());
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doLogin();
                }
            }
        });

        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(UiKit.PRIMARY_DARK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(UiKit.PRIMARY);
            }
        });
    }

    private JPanel buildBrandPanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, new Color(16, 63, 110), getWidth(), getHeight(), new Color(12, 93, 130));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(30, 26, 30, 26));

        JLabel lblBrand = new JLabel("UNIVERSITY HR SUITE");
        lblBrand.setForeground(new Color(200, 228, 253));
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel lblTitle = new JLabel("Hệ thống quản lý nhân sự");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));

        JLabel lblDesc = new JLabel("Quản lý hồ sơ, khoa, ngành và phân quyền người dùng trên một nền tảng thống nhất.");
        lblDesc.setForeground(new Color(217, 234, 252));
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(lblBrand);
        text.add(Box.createVerticalStrut(20));
        text.add(lblTitle);
        text.add(Box.createVerticalStrut(10));
        text.add(lblDesc);

        panel.add(text, BorderLayout.NORTH);
        return panel;
    }

    private JPanel buildFormPanel() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(UiKit.CARD_BG);
        wrap.setBorder(UiKit.cardBorder());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblLogin = new JLabel("Đăng nhập hệ thống");
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblLogin.setForeground(UiKit.PRIMARY_DARK);
        wrap.add(lblLogin, gbc);

        gbc.gridy++;
        JLabel lblSub = new JLabel("Sử dụng tài khoản được cấp để truy cập dữ liệu nhân sự.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(UiKit.TEXT_SUB);
        wrap.add(lblSub, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        wrap.add(new JLabel("Tên đăng nhập"), gbc);

        txtUsername = new JTextField();
        UiKit.styleTextField(txtUsername);
        gbc.gridy++;
        gbc.gridwidth = 2;
        wrap.add(txtUsername, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        wrap.add(new JLabel("Mật khẩu"), gbc);

        txtPassword = new JPasswordField();
        UiKit.styleTextField(txtPassword);
        gbc.gridy++;
        gbc.gridwidth = 2;
        wrap.add(txtPassword, gbc);

        lblError = new JLabel(" ");
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblError.setForeground(COLOR_ERROR);
        gbc.gridy++;
        wrap.add(lblError, gbc);

        btnLogin = new JButton("Đăng nhập");
        UiKit.styleButton(btnLogin, COLOR_PRIMARY);
        gbc.gridy++;
        wrap.add(btnLogin, gbc);

        return wrap;
    }

    private void doLogin() {
        lblError.setText("");
        btnLogin.setEnabled(false);
        btnLogin.setText("Đang xử lý...");
        SwingUtilities.invokeLater(() -> {
            controller.doLogin(txtUsername.getText(), new String(txtPassword.getPassword()), this);
            btnLogin.setEnabled(true);
            btnLogin.setText("Đăng nhập");
        });
    }

    public void showError(String message) {
        lblError.setText(message);
    }

    public void showLogin() {
        setVisible(true);
        txtUsername.requestFocusInWindow();
    }

    public void close() {
        dispose();
    }
}