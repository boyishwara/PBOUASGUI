package view.gui;

import java.awt.Component;
import javax.swing.JOptionPane;

public class DialogHelper {

    public static void showInfoMessage(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    // Overloaded method to accept a Component (like JScrollPane) as the message
    public static void showInfoMessage(Component parent, Component messageComponent, String title) {
        JOptionPane.showMessageDialog(parent, messageComponent, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showWarningMessage(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.WARNING_MESSAGE);
    }

    public static void showErrorMessage(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static boolean showConfirmDialog(Component parent, String message, String title) {
        int response = JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return response == JOptionPane.YES_OPTION;
    }

    public static String showInputDialog(Component parent, String message, String title) {
        return JOptionPane.showInputDialog(parent, message, title, JOptionPane.PLAIN_MESSAGE);
    }
}
