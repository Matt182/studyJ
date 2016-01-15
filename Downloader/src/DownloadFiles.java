import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by matt on 13.01.2016.
 */
public class DownloadFiles extends JPanel {
    private JPanel listPanel;
    private GridBagConstraints constraints;

    public static void main(String[] args) {
        JFrame f = new JFrame("Download files");
        DownloadFiles df = new DownloadFiles();
        for (int i = 0; i < args.length; i++) {
            df.createDownloader(args[i]);
        }
        f.getContentPane().add(df);
        f.setSize(600, 400);
        f.setVisible(true);
    }

    public DownloadFiles() {
        setLayout(new BorderLayout());
        listPanel = new JPanel();
        listPanel.setLayout(new GridBagLayout());
        constraints  = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTH;
        JScrollPane jsp = new JScrollPane(listPanel);
        add(jsp, BorderLayout.CENTER);

        add(getAddURLPanel(), BorderLayout.SOUTH);
    }

    private JPanel getAddURLPanel() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("URL:");
        final JTextField textField = new JTextField(20);
        JButton downloadBtn = new JButton("Download");
        ActionListener actionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (createDownloader(textField.getText())) {
                    textField.setText("");
                    revalidate();
                }
            }
        };
        textField.addActionListener(actionListener);
        downloadBtn.addActionListener(actionListener);
        JButton clearAll = new JButton("Cancel all");
        clearAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Downloader.cancelAllAndWait();
                listPanel.removeAll();
                revalidate();
                repaint();
            }
        });
        panel.add(label);
        panel.add(textField);
        panel.add(downloadBtn);
        panel.add(clearAll);
        return panel;
    }

    private boolean createDownloader(String url){
        try {
            URL downloadURL = new URL(url);
            URLConnection urlConn = downloadURL.openConnection();
            int length = urlConn.getContentLength();
            if (length < 0) throw new Exception("Unable to determine length");
            int index = url.lastIndexOf('/');
            FileOutputStream fos = new FileOutputStream(url.substring(index + 1));
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            DownloadManager dm = new DownloadManager(downloadURL, bos);
            listPanel.add(dm, constraints);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Unable to Download", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

}
