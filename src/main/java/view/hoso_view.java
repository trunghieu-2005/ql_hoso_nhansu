package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.hoso_controller;
import model.hoso_model;
import model.khoa_model;
import model.nganh_model;
import view.ui.UiKit;

public class hoso_view extends JPanel {
    private static final Color BG = UiKit.APP_BG;
    private static final Color CARD_BG = UiKit.CARD_BG;
    private static final Color BORDER = UiKit.BORDER;
    private static final Color TITLE = UiKit.PRIMARY_DARK;

    private hoso_controller controller;

    private JTextField txtId;
    private JTextField txtHoTen;
    private JTextField txtNgaySinh;
    private JComboBox<String> cboGioiTinh;
    private JTextField txtSoDienThoai;
    private JTextField txtEmail;
    private JTextField txtDiaChi;
    private JComboBox<LookupItem> cboKhoa;
    private JComboBox<LookupItem> cboNganh;
    private JTextField txtTrinhDo;
    private JTextField txtChucVu;

    private JTextField txtTimKiem;
    private JComboBox<LookupItem> cboTimKhoa;
    private JComboBox<LookupItem> cboTimNganh;
    private JComboBox<String> cboTimGioiTinh;
    private JTextField txtTimTrinhDo;
    private JTextField txtTimChucVu;

    private JTable tblHoSo;
    private DefaultTableModel tableModel;

    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private JButton btnTim;
    private JButton btnXoaLoc;

    private final List<LookupItem> khoaItems = new ArrayList<>();
    private final List<LookupItem> nganhItems = new ArrayList<>();
    private final Map<Integer, String> khoaNameMap = new HashMap<>();
    private final Map<Integer, String> nganhNameMap = new HashMap<>();
    private List<hoso_model> currentRows = new ArrayList<>();

    public hoso_view() {
        initUI();
        controller = new hoso_controller(this);
        controller.loadData();
    }

    private void initUI() {
        setSize(1200, 720);
        setMinimumSize(new Dimension(1060, 640));
        setLayout(new BorderLayout());
        setBackground(BG);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(14, 14, 14, 14));

        root.add(buildHeader(), BorderLayout.NORTH);
        JScrollPane contentScrollPane = new JScrollPane(buildContent());
        contentScrollPane.setBorder(null);
        contentScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        root.add(contentScrollPane, BorderLayout.CENTER);
        root.add(buildButtonPanel(), BorderLayout.SOUTH);
        add(root, BorderLayout.CENTER);

        bindActions();
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(12, 16, 12, 16)
        ));

        JLabel lbl = new JLabel("QUẢN LÝ HỒ SƠ NHÂN SỰ");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setForeground(TITLE);
        panel.add(lbl, BorderLayout.WEST);

        return panel;
    }

    private JPanel buildContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(12, 12, 12, 12)
        ));

        card.add(buildFormAndSearchPanel(), BorderLayout.NORTH);
        card.add(buildTablePanel(), BorderLayout.CENTER);

        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildFormAndSearchPanel() {
        JPanel wrap = new JPanel(new BorderLayout(0, 10));
        wrap.setOpaque(false);
        wrap.add(buildFormPanel(), BorderLayout.CENTER);
        wrap.add(buildSearchPanel(), BorderLayout.SOUTH);
        return wrap;
    }

    private JPanel buildFormPanel() {
        JPanel form = new JPanel(new GridLayout(6, 4, 10, 10));
        form.setBackground(UiKit.CARD_BG_SOFT);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Thông tin hồ sơ"),
                new EmptyBorder(10, 10, 10, 10)
        ));

        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setFocusable(false);
        txtId.setRequestFocusEnabled(false);
        txtId.setBackground(new Color(236, 242, 250));

        txtHoTen = new JTextField();
        txtNgaySinh = new JTextField();
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        txtSoDienThoai = new JTextField();
        txtEmail = new JTextField();
        txtDiaChi = new JTextField();
        cboKhoa = new JComboBox<>();
        cboNganh = new JComboBox<>();
        txtTrinhDo = new JTextField();
        txtChucVu = new JTextField();

        UiKit.styleTextField(txtId);
        txtId.setEditable(false);
        UiKit.styleTextField(txtHoTen);
        UiKit.styleTextField(txtNgaySinh);
        UiKit.styleComboBox(cboGioiTinh);
        UiKit.styleTextField(txtSoDienThoai);
        UiKit.styleTextField(txtEmail);
        UiKit.styleTextField(txtDiaChi);
        UiKit.styleComboBox(cboKhoa);
        UiKit.styleComboBox(cboNganh);
        UiKit.styleTextField(txtTrinhDo);
        UiKit.styleTextField(txtChucVu);

        form.add(new JLabel("ID"));
        form.add(txtId);
        form.add(new JLabel("Họ tên"));
        form.add(txtHoTen);

        form.add(new JLabel("Ngày sinh (yyyy-MM-dd)"));
        form.add(txtNgaySinh);
        form.add(new JLabel("Giới tính"));
        form.add(cboGioiTinh);

        form.add(new JLabel("Số điện thoại"));
        form.add(txtSoDienThoai);
        form.add(new JLabel("Email"));
        form.add(txtEmail);

        form.add(new JLabel("Địa chỉ"));
        form.add(txtDiaChi);
        form.add(new JLabel("Khoa"));
        form.add(cboKhoa);

        form.add(new JLabel("Ngành"));
        form.add(cboNganh);
        form.add(new JLabel("Trình độ"));
        form.add(txtTrinhDo);

        form.add(new JLabel("Chức vụ"));
        form.add(txtChucVu);
        form.add(new JLabel(""));
        form.add(new JLabel(""));

        return form;
    }

    private JPanel buildSearchPanel() {
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(CARD_BG);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Tìm kiếm nâng cao"),
                new EmptyBorder(8, 8, 8, 8)
        ));

        txtTimKiem = new JTextField(20);
        cboTimKhoa = new JComboBox<>();
        cboTimNganh = new JComboBox<>();
        cboTimGioiTinh = new JComboBox<>(new String[]{"Tất cả", "Nam", "Nữ", "Khác"});
        txtTimTrinhDo = new JTextField(10);
        txtTimChucVu = new JTextField(10);

        btnTim = new JButton("Tìm kiếm");
        btnXoaLoc = new JButton("Xóa lọc");

        UiKit.styleTextField(txtTimKiem);
        UiKit.styleTextField(txtTimTrinhDo);
        UiKit.styleTextField(txtTimChucVu);
        UiKit.styleComboBox(cboTimKhoa);
        UiKit.styleComboBox(cboTimNganh);
        UiKit.styleComboBox(cboTimGioiTinh);
        UiKit.styleNeutralButton(btnXoaLoc);
        UiKit.styleButton(btnTim, UiKit.INFO);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(new JLabel("Từ khóa"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        searchPanel.add(txtTimKiem, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        searchPanel.add(new JLabel("Khoa"), gbc);
        gbc.gridx = 3;
        searchPanel.add(cboTimKhoa, gbc);

        gbc.gridx = 4;
        searchPanel.add(new JLabel("Ngành"), gbc);
        gbc.gridx = 5;
        searchPanel.add(cboTimNganh, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        searchPanel.add(new JLabel("Giới tính"), gbc);
        gbc.gridx = 1;
        searchPanel.add(cboTimGioiTinh, gbc);

        gbc.gridx = 2;
        searchPanel.add(new JLabel("Trình độ"), gbc);
        gbc.gridx = 3;
        searchPanel.add(txtTimTrinhDo, gbc);

        gbc.gridx = 4;
        searchPanel.add(new JLabel("Chức vụ"), gbc);
        gbc.gridx = 5;
        searchPanel.add(txtTimChucVu, gbc);

        JPanel buttonWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonWrap.setOpaque(false);
        buttonWrap.add(btnXoaLoc);
        buttonWrap.add(btnTim);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        searchPanel.add(buttonWrap, gbc);

        return searchPanel;
    }

    private JScrollPane buildTablePanel() {
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Họ tên", "Ngày sinh", "Giới tính", "SĐT", "Email", "Địa chỉ", "Khoa", "Ngành", "Trình độ", "Chức vụ"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblHoSo = new JTable(tableModel);
        tblHoSo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UiKit.styleTable(tblHoSo);
        tblHoSo.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblHoSo);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER));
        return scrollPane;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setOpaque(false);

        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");

        UiKit.styleButton(btnThem, UiKit.SUCCESS);
        UiKit.styleButton(btnSua, UiKit.INFO);
        UiKit.styleButton(btnXoa, UiKit.DANGER);
        UiKit.styleNeutralButton(btnLamMoi);

        panel.add(btnLamMoi);
        panel.add(btnSua);
        panel.add(btnXoa);
        panel.add(btnThem);
        return panel;
    }

    private void bindActions() {
        btnThem.addActionListener(e -> runControllerAction(() -> controller.onThemClicked()));
        btnSua.addActionListener(e -> runControllerAction(() -> controller.onSuaClicked()));
        btnXoa.addActionListener(e -> runControllerAction(() -> controller.onXoaClicked()));
        btnLamMoi.addActionListener(e -> runControllerAction(() -> controller.onLamMoiClicked()));
        btnTim.addActionListener(e -> runControllerAction(() -> controller.onTimKiemClicked()));
        btnXoaLoc.addActionListener(e -> runControllerAction(() -> controller.onXoaLocTimKiemClicked()));

        txtTimKiem.addActionListener(e -> runControllerAction(() -> controller.onTimKiemClicked()));
        txtTimTrinhDo.addActionListener(e -> runControllerAction(() -> controller.onTimKiemClicked()));
        txtTimChucVu.addActionListener(e -> runControllerAction(() -> controller.onTimKiemClicked()));
        cboTimKhoa.addActionListener(e -> runControllerAction(() -> controller.onTimKiemClicked()));
        cboTimNganh.addActionListener(e -> runControllerAction(() -> controller.onTimKiemClicked()));
        cboTimGioiTinh.addActionListener(e -> runControllerAction(() -> controller.onTimKiemClicked()));
    }

    private void runControllerAction(Runnable action) {
        if (controller != null) {
            action.run();
        }
    }

    public void setController(hoso_controller controller) {
        this.controller = controller;
    }

    public void setKhoaOptions(List<khoa_model> list) {
        khoaItems.clear();
        khoaNameMap.clear();

        for (khoa_model item : list) {
            LookupItem lookup = new LookupItem(item.getId(), item.getTenKhoa());
            khoaItems.add(lookup);
            khoaNameMap.put(item.getId(), item.getTenKhoa());
        }
        reloadKhoaCombos();
    }

    public void setNganhOptions(List<nganh_model> list) {
        nganhItems.clear();
        nganhNameMap.clear();

        for (nganh_model item : list) {
            LookupItem lookup = new LookupItem(item.getId(), item.getTenNganh());
            nganhItems.add(lookup);
            nganhNameMap.put(item.getId(), item.getTenNganh());
        }
        reloadNganhCombos();
    }

    private void reloadKhoaCombos() {
        Integer selectedForm = getSelectedLookupId(cboKhoa);
        Integer selectedSearch = getSelectedLookupId(cboTimKhoa);

        cboKhoa.removeAllItems();
        cboTimKhoa.removeAllItems();

        cboKhoa.addItem(new LookupItem(null, "-- Chọn khoa --"));
        cboTimKhoa.addItem(new LookupItem(null, "Tất cả khoa"));

        for (LookupItem item : khoaItems) {
            cboKhoa.addItem(item);
            cboTimKhoa.addItem(item);
        }

        selectLookupById(cboKhoa, selectedForm);
        selectLookupById(cboTimKhoa, selectedSearch);
    }

    private void reloadNganhCombos() {
        Integer selectedForm = getSelectedLookupId(cboNganh);
        Integer selectedSearch = getSelectedLookupId(cboTimNganh);

        cboNganh.removeAllItems();
        cboTimNganh.removeAllItems();

        cboNganh.addItem(new LookupItem(null, "-- Chọn ngành --"));
        cboTimNganh.addItem(new LookupItem(null, "Tất cả ngành"));

        for (LookupItem item : nganhItems) {
            cboNganh.addItem(item);
            cboTimNganh.addItem(item);
        }

        selectLookupById(cboNganh, selectedForm);
        selectLookupById(cboTimNganh, selectedSearch);
    }

    public String getIdText() {
        return txtId.getText() == null ? "" : txtId.getText().trim();
    }

    public String getHoTenText() {
        return txtHoTen.getText() == null ? "" : txtHoTen.getText().trim();
    }

    public String getNgaySinhText() {
        return txtNgaySinh.getText() == null ? "" : txtNgaySinh.getText().trim();
    }

    public String getGioiTinhText() {
        String selected = (String) cboGioiTinh.getSelectedItem();
        return selected == null ? "" : selected.trim();
    }

    public String getSoDienThoaiText() {
        return txtSoDienThoai.getText() == null ? "" : txtSoDienThoai.getText().trim();
    }

    public String getEmailText() {
        return txtEmail.getText() == null ? "" : txtEmail.getText().trim();
    }

    public String getDiaChiText() {
        return txtDiaChi.getText() == null ? "" : txtDiaChi.getText().trim();
    }

    public Integer getSelectedKhoaId() {
        return getSelectedLookupId(cboKhoa);
    }

    public Integer getSelectedNganhId() {
        return getSelectedLookupId(cboNganh);
    }

    public String getTrinhDoText() {
        return txtTrinhDo.getText() == null ? "" : txtTrinhDo.getText().trim();
    }

    public String getChucVuText() {
        return txtChucVu.getText() == null ? "" : txtChucVu.getText().trim();
    }

    public String getTuKhoaTimKiem() {
        return txtTimKiem.getText() == null ? "" : txtTimKiem.getText().trim();
    }

    public Integer getTimKhoaId() {
        return getSelectedLookupId(cboTimKhoa);
    }

    public Integer getTimNganhId() {
        return getSelectedLookupId(cboTimNganh);
    }

    public String getTimGioiTinh() {
        String selected = (String) cboTimGioiTinh.getSelectedItem();
        if (selected == null || selected.equalsIgnoreCase("Tất cả")) {
            return "";
        }
        return selected.trim();
    }

    public String getTimTrinhDo() {
        return txtTimTrinhDo.getText() == null ? "" : txtTimTrinhDo.getText().trim();
    }

    public String getTimChucVu() {
        return txtTimChucVu.getText() == null ? "" : txtTimChucVu.getText().trim();
    }

    public int getSelectedRowIndex() {
        return tblHoSo.getSelectedRow();
    }

    public void setTableData(List<hoso_model> list) {
        currentRows = new ArrayList<>(list);
        tableModel.setRowCount(0);

        for (hoso_model item : currentRows) {
            tableModel.addRow(new Object[]{
                    safeValue(item.getId()),
                    safeValue(item.getHoTen()),
                    safeValue(item.getNgaySinh()),
                    safeValue(item.getGioiTinh()),
                    safeValue(item.getSoDienThoai()),
                    safeValue(item.getEmail()),
                    safeValue(item.getDiaChi()),
                    formatLookup(item.getKhoaId(), khoaNameMap),
                    formatLookup(item.getNganhId(), nganhNameMap),
                    safeValue(item.getTrinhDo()),
                    safeValue(item.getChucVu())
            });
        }
    }

    public void selectRow(int row) {
        if (row < 0 || row >= tblHoSo.getRowCount()) {
            return;
        }
        tblHoSo.setRowSelectionInterval(row, row);
        tblHoSo.scrollRectToVisible(tblHoSo.getCellRect(row, 0, true));
    }

    public void clearForm() {
        txtId.setText("");
        txtHoTen.setText("");
        txtNgaySinh.setText("");
        if (cboGioiTinh.getItemCount() > 0) {
            cboGioiTinh.setSelectedIndex(0);
        }
        txtSoDienThoai.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        txtTrinhDo.setText("");
        txtChucVu.setText("");

        if (cboKhoa.getItemCount() > 0) {
            cboKhoa.setSelectedIndex(0);
        }
        if (cboNganh.getItemCount() > 0) {
            cboNganh.setSelectedIndex(0);
        }

        tblHoSo.clearSelection();
        txtHoTen.requestFocus();
    }

    public void clearTimKiem() {
        txtTimKiem.setText("");
        txtTimTrinhDo.setText("");
        txtTimChucVu.setText("");
        cboTimGioiTinh.setSelectedIndex(0);

        if (cboTimKhoa.getItemCount() > 0) {
            cboTimKhoa.setSelectedIndex(0);
        }
        if (cboTimNganh.getItemCount() > 0) {
            cboTimNganh.setSelectedIndex(0);
        }
    }

    private void fillFormFromSelectedRow() {
        int row = tblHoSo.getSelectedRow();
        if (row < 0 || row >= currentRows.size()) {
            return;
        }

        hoso_model item = currentRows.get(row);
        txtId.setText(safeValue(item.getId()));
        txtHoTen.setText(safeValue(item.getHoTen()));
        txtNgaySinh.setText(safeValue(item.getNgaySinh()));
        String gioiTinh = safeValue(item.getGioiTinh());
        if ("Nữ".equalsIgnoreCase(gioiTinh)) {
            cboGioiTinh.setSelectedItem("Nữ");
        } else {
            cboGioiTinh.setSelectedItem("Nam");
        }
        txtSoDienThoai.setText(safeValue(item.getSoDienThoai()));
        txtEmail.setText(safeValue(item.getEmail()));
        txtDiaChi.setText(safeValue(item.getDiaChi()));
        txtTrinhDo.setText(safeValue(item.getTrinhDo()));
        txtChucVu.setText(safeValue(item.getChucVu()));

        selectLookupById(cboKhoa, item.getKhoaId());
        selectLookupById(cboNganh, item.getNganhId());
    }

    private Integer getSelectedLookupId(JComboBox<LookupItem> comboBox) {
        Object selected = comboBox.getSelectedItem();
        if (selected instanceof LookupItem lookupItem) {
            return lookupItem.id;
        }
        return null;
    }

    private void selectLookupById(JComboBox<LookupItem> comboBox, Integer id) {
        if (comboBox.getItemCount() == 0) {
            return;
        }
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            LookupItem item = comboBox.getItemAt(i);
            if (Objects.equals(item.id, id)) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
        comboBox.setSelectedIndex(0);
    }

    private String formatLookup(Integer id, Map<Integer, String> lookupNameMap) {
        if (id == null) {
            return "";
        }
        String name = lookupNameMap.get(id);
        if (name == null || name.isBlank()) {
            return String.valueOf(id);
        }
        return id + " - " + name;
    }

    private String safeValue(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private static final class LookupItem {
        private final Integer id;
        private final String label;

        private LookupItem(Integer id, String label) {
            this.id = id;
            this.label = label;
        }

        @Override
        public String toString() {
            if (id == null) {
                return label;
            }
            return id + " - " + label;
        }
    }
}
