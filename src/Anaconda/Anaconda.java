/**
 * Anaconda main program to execute the game.
 */
package Anaconda;
import javax.swing.JFrame;


public class Anaconda extends JFrame {

    public Anaconda() {
        add(new Board());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(596, 400);
        setLocationRelativeTo(null);
        setTitle("ANACONDA");
        setResizable(false);
        setVisible(true);
    }
  
    public static void main(String[] args) {
        new Anaconda(); 
    }
}
