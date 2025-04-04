package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class Notepad extends JFrame {

    private static final Font MENU_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Color MENU_BAR_BACKGROUND = Color.WHITE;

    Notepad() {
        setTitle("Notepad");
        setIconImage(loadIcon("notepad.png"));
        setJMenuBar(createMenuBar());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private Image loadIcon(String resourceName) {
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(resourceName));
        return icon.getImage();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        List<JMenu> menus = List.of(
                createMenu("File", List.of(
                        createMenuItem("New", KeyEvent.VK_N, ActionEvent.CTRL_MASK),
                        createMenuItem("Open", KeyEvent.VK_O, ActionEvent.CTRL_MASK),
                        createMenuItem("Save", KeyEvent.VK_S, ActionEvent.CTRL_MASK),
                        createMenuItem("Exit", KeyEvent.VK_Q, ActionEvent.CTRL_MASK))),
                createMenu("Edit", List.of(
                        createMenuItem("Undo", KeyEvent.VK_Z, ActionEvent.CTRL_MASK),
                        createMenuItem("Cut", KeyEvent.VK_X, ActionEvent.CTRL_MASK),
                        createMenuItem("Copy", KeyEvent.VK_C, ActionEvent.CTRL_MASK),
                        createMenuItem("Paste", KeyEvent.VK_V, ActionEvent.CTRL_MASK),
                        createMenuItem("Select All", KeyEvent.VK_A, ActionEvent.CTRL_MASK))),
                createMenu("Format"),
                createMenu("View"),
                createMenu("Help")
        );

        for (JMenu menu : menus) {
            menu.setFont(MENU_FONT);
            menuBar.add(menu);
        }

        menuBar.setBackground(MENU_BAR_BACKGROUND);
        JTextArea textArea = new JTextArea();

        add(textArea);
        return menuBar;
    }

    private JMenu createMenu(String title) {
        return new JMenu(title);
    }

    private JMenu createMenu(String title, List<JMenuItem> items) {
        JMenu menu = new JMenu(title);
        for (JMenuItem item : items) {
            menu.add(item);
        }
        return menu;
    }

    private JMenuItem createMenuItem(String title, int keyEvent, int modifiers) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(keyEvent, modifiers));
        return menuItem;
    }

}