import javax.swing.SwingUtilities;
import Engines.InitEngine;

public class Start {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new InitEngine();
            }
        });
    }
}