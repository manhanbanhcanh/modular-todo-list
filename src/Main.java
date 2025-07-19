import javax.swing.*;

//logging stuff
import java.util.logging.Logger;
import java.util.logging.Level;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName()); //logs

        public static void main(String[] args) {

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); // Required Nimbus
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to set look and feel", e);
            }

            SwingUtilities.invokeLater(TodoUI::new);
            System.out.println("Loaded");
        }
    }

