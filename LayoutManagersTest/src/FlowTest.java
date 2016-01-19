import javax.swing.*;
import java.awt.*;

/**
 * Created by matt on 18.01.2016.
 */
public class FlowTest extends JFrame {
    public static void main(String[] args) {
        FlowTest ft = new FlowTest();
        ft.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ft.setSize(400, 300);
        ft.setVisible(true);
    }

    public FlowTest() {
        super();
        Container pane = getContentPane();
        pane.setLayout(new FlowLayout(FlowLayout.CENTER));
        pane.add(new JLabel("This is test"));
        pane.add(new JButton("of a flowlayout"));
        pane.add(new JTextField(30));
        pane.add(new JTextArea("This is text area", 3 ,10));
        pane.add(new JLabel("This is FlowLayout test with long string"));
    }
}
