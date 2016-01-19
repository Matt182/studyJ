import javax.swing.*;
import java.awt.*;

/**
 * Created by matt on 18.01.2016.
 */
public class CardTest extends JFrame {

    private CardLayout layout;

    public static void main(String[] args) {
        CardTest ct = new CardTest();
        ct.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ct.displayTab("red tab");
        ct.setSize(400, 400);
        ct.setVisible(true);
    }

    public CardTest() {
        JPanel tab;
        Container pane = getContentPane();
        layout = new CardLayout();
        pane.setLayout(layout);
        tab = new JPanel();
        tab.setBackground(Color.red);
        pane.add(tab, "Red tab");
        tab = new JPanel();
        tab.setBackground(Color.green);
        pane.add(tab, "green tab");
        tab = new JPanel();
        tab.setBackground(Color.blue);
        pane.add(tab, "blue tab");

    }
    public void displayTab(String name){
        layout.show(this.getContentPane(), name);
    }
}
