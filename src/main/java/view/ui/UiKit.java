package view.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public final class UiKit {
    private UiKit() {
    }

    public static final Color APP_BG = new Color(236, 243, 250);
    public static final Color CARD_BG = new Color(255, 255, 255);
    public static final Color CARD_BG_SOFT = new Color(247, 251, 255);

    public static final Color BORDER = new Color(201, 214, 230);

    public static final Color PRIMARY = new Color(18, 76, 131);
    public static final Color PRIMARY_DARK = new Color(13, 57, 98);
    public static final Color INFO = new Color(0, 133, 151);
    public static final Color SUCCESS = new Color(34, 131, 104);
    public static final Color WARNING = new Color(171, 126, 18);
    public static final Color DANGER = new Color(176, 55, 55);

    public static final Color TEXT = new Color(29, 44, 64);
    public static final Color TEXT_SUB = new Color(89, 106, 126);

    public static final Color TABLE_HEADER_BG = new Color(17, 70, 120);
    public static final Color TABLE_HEADER_FG = Color.WHITE;
    public static final Color TABLE_ROW_ALT = new Color(245, 249, 255);
    public static final Color TABLE_SELECTION_BG = new Color(203, 225, 247);
    public static final Color TABLE_SELECTION_FG = new Color(21, 44, 72);

    private static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);

    public static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        );
    }

    public static void styleTextField(JTextField textField) {
        textField.setFont(FONT_BODY);
        textField.setForeground(TEXT);
        textField.setBackground(Color.WHITE);
        textField.setCaretColor(TEXT);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
    }

    public static void styleTextArea(JTextArea textArea) {
        textArea.setFont(FONT_BODY);
        textArea.setForeground(TEXT);
        textArea.setBackground(Color.WHITE);
        textArea.setCaretColor(TEXT);
        textArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
    }

    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(FONT_BODY);
        comboBox.setForeground(TEXT);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createLineBorder(BORDER));
    }

    public static void styleButton(JButton button, Color background) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(background);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }

    public static void styleNeutralButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(TEXT);
        button.setBackground(new Color(236, 242, 250));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(7, 13, 7, 13)
        ));
        button.setFocusPainted(false);
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }

    public static void styleTable(JTable table) {
        table.setFont(FONT_BODY);
        table.setForeground(TEXT);
        table.setBackground(Color.WHITE);
        table.setGridColor(BORDER);
        table.setRowHeight(32);
        table.setSelectionBackground(TABLE_SELECTION_BG);
        table.setSelectionForeground(TABLE_SELECTION_FG);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 34));

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                setOpaque(true);
                setBackground(TABLE_HEADER_BG);
                setForeground(TABLE_HEADER_FG);
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(57, 106, 152)),
                        BorderFactory.createEmptyBorder(0, 8, 0, 8)
                ));
                setHorizontalAlignment(LEFT);
                return this;
            }
        });

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ROW_ALT);
                    setForeground(TEXT);
                }
                return this;
            }
        });
    }
}