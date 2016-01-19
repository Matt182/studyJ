import javax.swing.*;
import java.awt.*;

/**
 * Created by matt on 18.01.2016.
 */
public class GridTest extends JFrame {
    public static void main(String[] args) {
        GridTest gt = new GridTest();
        gt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gt.pack();
        gt.setVisible(true);
    }

    public GridTest() {
        Container pane = getContentPane();
        pane.setLayout(new GridLayout(2,2));
        JButton btn = new JButton("first");
        pane.add(btn);
        btn = new JButton("second ");
        pane.add(btn);
        btn = new JButton("third");
        pane.add(btn);
    }
}
