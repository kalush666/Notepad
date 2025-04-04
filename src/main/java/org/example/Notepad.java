package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.io.*;
import java.util.List;

public class Notepad extends JFrame implements ActionListener {

    private static final Font MENU_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Color MENU_BAR_BACKGROUND = Color.WHITE;
    private File currentFile; // Declare currentFile to store the file path
    private JTextArea textArea; // Declare textArea to use in printContent and saveFile methods

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
                        createMenuItem("New Window", KeyEvent.VK_N, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK),
                        createMenuItem("Open...", KeyEvent.VK_O, ActionEvent.CTRL_MASK),
                        createMenuItem("Save", KeyEvent.VK_S, ActionEvent.CTRL_MASK),
                        createMenuItem("Save As...", KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK),
                        new JSeparator(),
                        createMenuItem("Page Setup...", KeyEvent.VK_P, ActionEvent.CTRL_MASK),
                        createMenuItem("Print...", KeyEvent.VK_P, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK),
                        new JSeparator(),
                        createMenuItem("Exit", KeyEvent.VK_Q, ActionEvent.CTRL_MASK))),
                createMenu("Edit", List.of(
                        createMenuItem("Undo", KeyEvent.VK_Z, ActionEvent.CTRL_MASK),
                        new JSeparator(),
                        createMenuItem("Cut", KeyEvent.VK_X, ActionEvent.CTRL_MASK),
                        createMenuItem("Copy", KeyEvent.VK_C, ActionEvent.CTRL_MASK),
                        createMenuItem("Paste", KeyEvent.VK_V, ActionEvent.CTRL_MASK),
                        createMenuItem("Delete", KeyEvent.VK_DELETE, 0),
                        new JSeparator(),
                        createMenuItem("Find...", KeyEvent.VK_F, ActionEvent.CTRL_MASK),
                        createMenuItem("Find Next", KeyEvent.VK_F3, 0),
                        createMenuItem("Find Previous", KeyEvent.VK_F3, ActionEvent.SHIFT_MASK),
                        createMenuItem("Replace...", KeyEvent.VK_H, ActionEvent.CTRL_MASK),
                        createMenuItem("Go To...", KeyEvent.VK_G, ActionEvent.CTRL_MASK),
                        new JSeparator(),
                        createMenuItem("Select All", KeyEvent.VK_A, ActionEvent.CTRL_MASK),
                        createMenuItem("Time/Date", KeyEvent.VK_F5, 0))),
                createMenu("Format", List.of(
                        createMenuItem("Word Wrap", KeyEvent.VK_W, ActionEvent.CTRL_MASK),
                        createMenuItem("Font...", KeyEvent.VK_D, ActionEvent.CTRL_MASK))),
                createMenu("View", List.of(
                        createMenuItem("Zoom", KeyEvent.VK_Z, ActionEvent.CTRL_MASK),
                        createMenuItem("Status Bar", KeyEvent.VK_S, ActionEvent.CTRL_MASK))),
                createMenu("Help", List.of(
                        createMenuItem("View Help", KeyEvent.VK_H, ActionEvent.CTRL_MASK),
                        createMenuItem("Send Feedback", KeyEvent.VK_F, ActionEvent.CTRL_MASK),
                        new JSeparator(),
                        createMenuItem("About Notepad", KeyEvent.VK_A, ActionEvent.CTRL_MASK)))
        );

        for (JMenu menu : menus) {
            menu.setFont(MENU_FONT);
            menuBar.add(menu);
        }

        menuBar.setBackground(MENU_BAR_BACKGROUND);
        textArea = new JTextArea(); // Initialize textArea
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        textArea.setWrapStyleWord(true);
        add(textArea);
        return menuBar;
    }

    private JMenu createMenu(String title) {
        return new JMenu(title);
    }

    private JMenu createMenu(String title, List<Object> items) {
        JMenu menu = new JMenu(title);
        for (Object item : items) {
            if (item instanceof JSeparator) {
                menu.add((JSeparator) item);
            } else {
                menu.add((JMenuItem) item);
            }
        }
        return menu;
    }

    private JMenuItem createMenuItem(String title, int keyEvent, int modifiers) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(keyEvent, modifiers));
        menuItem.addActionListener(this);
        return menuItem;
    }

    public static void main(String[] args) {
        new Notepad();
    }

    private void printContent() {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
                if (pageIndex > 0) {
                    return NO_SUCH_PAGE;
                }
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                textArea.printAll(graphics); // Use textArea
                return PAGE_EXISTS;
            }
        });

        boolean doPrint = printerJob.printDialog();
        if (doPrint) {
            try {
                printerJob.print();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFile() {
        if (currentFile != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
                writer.write(textArea.getText()); // Use textArea
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            saveFileAs();
        }
    }

    private void saveFileAs() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            saveFile();
        }
    }
    private void openFIle(){
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                textArea.read(reader, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "New":
                this.dispose();
                new Notepad();
                break;
            case "New Window":
                break;
            case "Open...":
                openFIle();
                break;
            case "Save":
                saveFile();
                break;
            case "Save As...":
                saveFileAs();
                break;
            case "Page Setup...":
                // Handle Page Setup action
                break;
            case "Print...":
                printContent();
                break;
            case "Exit":
                System.exit(0);
                break;
            case "Undo":
                // Handle Undo action
                break;
            case "Cut":
                // Handle Cut action
                break;
            case "Copy":
                // Handle Copy action
                break;
            case "Paste":
                // Handle Paste action
                break;
            case "Delete":
                // Handle Delete action
                break;
            case "Find...":
                // Handle Find action
                break;
            case "Find Next":
                // Handle Find Next action
                break;
            case "Find Previous":
                // Handle Find Previous action
                break;
            case "Replace...":
                // Handle Replace action
                break;
            case "Go To...":
                // Handle Go To action
                break;
            case "Select All":
                // Handle Select All action
                break;
            case "Time/Date":
                // Handle Time/Date action
                break;
            case "Word Wrap":
                // Handle Word Wrap action
                break;
            case "Font...":
                // Handle Font action
                break;
            case "Zoom":
                // Handle Zoom action
                break;
            case "Status Bar":
                // Handle Status Bar action
                break;
            case "View Help":
                // Handle View Help action
                break;
            case "Send Feedback":
                // Handle Send Feedback action
                break;
            case "About Notepad":
                JOptionPane.showMessageDialog(this, "Notepad version 1.0");
                break;
            default:
                break;
        }
    }
}