
import controller.AppController;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import view.gui.LoginPage;

public class MainAppGUI {
    public static void main(String[] args) {
        // Inisialisasi controller
        AppController appController = new AppController();

        // Set Look and Feel (opsional, untuk tampilan yang lebih modern)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Gagal mengatur Look and Feel: " + e.getMessage());
        }

        // Jalankan GUI di Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginPage loginPage = new LoginPage(appController); // Baris yang menyebabkan error jika LoginPage tidak ditemukan
                loginPage.setVisible(true);
            }
        });
    }
}
