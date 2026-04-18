package app;

import java.awt.Color;
import java.awt.Font;
import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

public final class AppTheme {
    private AppTheme() {
    }

    public static void apply() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        FontUIResource defaultFont = new FontUIResource(new Font("Segoe UI", Font.PLAIN, 13));
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, defaultFont);
            }
        }

        ColorUIResource pageBg = new ColorUIResource(new Color(236, 243, 250));
        ColorUIResource controlBg = new ColorUIResource(new Color(255, 255, 255));
        ColorUIResource border = new ColorUIResource(new Color(201, 214, 230));
        ColorUIResource primary = new ColorUIResource(new Color(18, 76, 131));
        ColorUIResource primaryHover = new ColorUIResource(new Color(13, 57, 98));
        ColorUIResource selection = new ColorUIResource(new Color(203, 225, 247));
        ColorUIResource textColor = new ColorUIResource(new Color(29, 44, 64));

        UIManager.put("Panel.background", pageBg);
        UIManager.put("Viewport.background", pageBg);
        UIManager.put("ScrollPane.background", pageBg);
        UIManager.put("Label.foreground", textColor);

        UIManager.put("TextField.background", controlBg);
        UIManager.put("TextField.foreground", textColor);
        UIManager.put("TextField.caretForeground", textColor);

        UIManager.put("ComboBox.background", controlBg);
        UIManager.put("ComboBox.foreground", textColor);

        UIManager.put("Button.background", primary);
        UIManager.put("Button.foreground", new ColorUIResource(Color.WHITE));
        UIManager.put("Button.select", primaryHover);

        UIManager.put("Table.gridColor", border);
        UIManager.put("Table.selectionBackground", selection);
        UIManager.put("Table.selectionForeground", new ColorUIResource(new Color(21, 44, 72)));
        UIManager.put("Table.foreground", textColor);
        UIManager.put("Table.background", controlBg);
        UIManager.put("TableHeader.background", new ColorUIResource(new Color(17, 70, 120)));
        UIManager.put("TableHeader.foreground", new ColorUIResource(Color.WHITE));
        UIManager.put("TableHeader.font", new FontUIResource(new Font("Segoe UI", Font.BOLD, 12)));

        UIManager.put("Component.focusColor", new ColorUIResource(new Color(109, 156, 210)));
    }
}