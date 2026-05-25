package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import controller.khoa_controller;
import model.khoa_model;
import view.ui.UiKit;

public class khoa_view extends JPanel {
	private static final Color BG = UiKit.APP_BG;
	private static final Color CARD_BG = UiKit.CARD_BG;
	private static final Color BORDER = UiKit.BORDER;
	private static final Color TITLE = UiKit.PRIMARY_DARK;

	private khoa_controller controller;

	private JTextField txtId;
	private JTextField txtTenKhoa;
	private JTextArea txtMoTa;
	private JTextField txtTuKhoaTimKiem;
	private JTable tblKhoa;
	private DefaultTableModel tableModel;

	private JButton btnThem;
	private JButton btnSua;
	private JButton btnXoa;
	private JButton btnLamMoi;
	private JButton btnTimKiem;

	public khoa_view() {
		initUI();
		this.controller = new khoa_controller(this);
		 controller.loadData();
	}

	private void initUI() {
		setBackground(BG);
		setLayout(new BorderLayout(12, 12));

		JPanel root = new JPanel(new BorderLayout(12, 12));
		root.setBackground(BG);
		root.setBorder(new EmptyBorder(12, 12, 12, 12));
		

		root.add(buildTopPanel(), BorderLayout.NORTH);
		root.add(buildTablePanel(), BorderLayout.CENTER);
		root.add(buildButtonPanel(), BorderLayout.SOUTH);
add(root);
		bindActions();
	}

	private void bindActions() {
		btnThem.addActionListener(e -> runControllerAction(() -> controller.onThemClicked()));
		btnSua.addActionListener(e -> runControllerAction(() -> controller.onSuaClicked()));
		btnXoa.addActionListener(e -> runControllerAction(() -> controller.onXoaClicked()));
		btnLamMoi.addActionListener(e -> runControllerAction(() -> controller.onLamMoiClicked()));
		btnTimKiem.addActionListener(e -> runControllerAction(() -> controller.onTimKiemClicked()));
		txtTuKhoaTimKiem.addActionListener(e -> runControllerAction(() -> controller.onTimKiemClicked()));
	}

	private void runControllerAction(Runnable action) {
		if (controller != null) {
			action.run();
		}
	}

	private TitledBorder createCenteredTitleBorder(String title) {
		return BorderFactory.createTitledBorder(null, title, TitledBorder.CENTER, TitledBorder.TOP);
	}

	private JPanel buildTopPanel() {
		JPanel panel = new JPanel(new BorderLayout(0, 8));
		panel.setOpaque(false);

		JLabel lblTitle = new JLabel("QUẢN LÝ KHOA", SwingConstants.LEFT);
		lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 18f));
		lblTitle.setForeground(TITLE);

		panel.add(lblTitle, BorderLayout.NORTH);
		panel.add(buildFormPanel(), BorderLayout.CENTER);
		panel.add(buildSearchPanel(), BorderLayout.SOUTH);

		return panel;
	}

	private JPanel buildSearchPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
		panel.setBackground(CARD_BG);
		panel.setBorder(BorderFactory.createCompoundBorder(
				createCenteredTitleBorder("Tìm kiếm"),
				new EmptyBorder(6, 6, 6, 6)
		));

		JLabel lblTimKiem = new JLabel("Tìm kiếm:");
		txtTuKhoaTimKiem = new JTextField(28);
		btnTimKiem = new JButton("Tìm");
		UiKit.styleTextField(txtTuKhoaTimKiem);
		UiKit.styleButton(btnTimKiem, UiKit.INFO);

		panel.add(lblTimKiem);
		panel.add(txtTuKhoaTimKiem);
		panel.add(btnTimKiem);

		return panel;
	}

	private JPanel buildFormPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(CARD_BG);
		panel.setBorder(BorderFactory.createCompoundBorder(
				createCenteredTitleBorder("Thông tin khoa"),
				new EmptyBorder(6, 6, 6, 6)
		));
		JPanel formGrid = new JPanel(new GridLayout(3, 2, 6, 6));
		formGrid.setBackground(CARD_BG);

		JLabel lblId = new JLabel("ID:");
		JLabel lblTenKhoa = new JLabel("Tên khoa:");
		JLabel lblMoTa = new JLabel("Mô tả:");

		txtId = new JTextField();
		txtId.setEditable(false);
		txtId.setFocusable(false);
		txtId.setRequestFocusEnabled(false);
		txtTenKhoa = new JTextField();
		txtMoTa = new JTextArea(2, 20);
		txtMoTa.setLineWrap(true);
		txtMoTa.setWrapStyleWord(true);
		UiKit.styleTextField(txtId);
		txtId.setEditable(false);
		UiKit.styleTextField(txtTenKhoa);
		UiKit.styleTextArea(txtMoTa);

		formGrid.add(lblId);
		formGrid.add(txtId);
		formGrid.add(lblTenKhoa);
		formGrid.add(txtTenKhoa);
		formGrid.add(lblMoTa);
		JScrollPane moTaScrollPane = new JScrollPane(txtMoTa);
		moTaScrollPane.setPreferredSize(new Dimension(0, 42));
		formGrid.add(moTaScrollPane);

		panel.add(formGrid, BorderLayout.CENTER);

		return panel;
	}

	private JPanel buildTablePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(CARD_BG);
		panel.setBorder(BorderFactory.createCompoundBorder(
				createCenteredTitleBorder("Danh sách khoa"),
				new EmptyBorder(6, 6, 6, 6)
		));

		tableModel = new DefaultTableModel(new Object[] {"ID", "Tên khoa", "Mô tả"}, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tblKhoa = new JTable(tableModel);
		UiKit.styleTable(tblKhoa);
		tblKhoa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblKhoa.getTableHeader().setResizingAllowed(false);
		tblKhoa.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				fillFormFromSelectedRow();
			}
		});

		JScrollPane scrollPane = new JScrollPane(tblKhoa);
		scrollPane.setBorder(BorderFactory.createLineBorder(BORDER));
		scrollPane.setPreferredSize(new Dimension(800, 380));
		panel.add(scrollPane, BorderLayout.CENTER);
		return panel;
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

	public void setController(khoa_controller controller) {
		this.controller = controller;
	}

	public void setTableData(List<khoa_model> list) {
		tableModel.setRowCount(0);
		for (khoa_model item : list) {
			tableModel.addRow(new Object[] {item.getId(), item.getTenKhoa(), item.getMoTa()});
		}
	}

	private void fillFormFromSelectedRow() {
		int row = tblKhoa.getSelectedRow();
		if (row < 0) {
			return;
		}

		txtId.setText(String.valueOf(tableModel.getValueAt(row, 0)));
		txtTenKhoa.setText(String.valueOf(tableModel.getValueAt(row, 1)));
		Object motaValue = tableModel.getValueAt(row, 2);
		txtMoTa.setText(motaValue == null ? "" : String.valueOf(motaValue));
	}

	public String getIdText() {
		return txtId.getText() == null ? "" : txtId.getText().trim();
	}

	public String getTenKhoaText() {
		return txtTenKhoa.getText() == null ? "" : txtTenKhoa.getText().trim();
	}	

	public String getMoTaText() {
		return txtMoTa.getText() == null ? "" : txtMoTa.getText().trim();
	}

	public String getTuKhoaTimKiem() {
		return txtTuKhoaTimKiem.getText() == null ? "" : txtTuKhoaTimKiem.getText().trim();
	}

	public void clearTuKhoaTimKiem() {
		txtTuKhoaTimKiem.setText("");
	}

	public void clearForm() {
		txtId.setText("");
		txtTenKhoa.setText("");
		txtMoTa.setText("");
		tblKhoa.clearSelection();
		txtTenKhoa.requestFocus();
	}

	public void focusTenKhoa() {
		txtTenKhoa.requestFocus();
	}

	public void focusMoTa() {
		txtMoTa.requestFocus();
	}
}
