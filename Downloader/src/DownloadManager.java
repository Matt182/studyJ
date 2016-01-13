import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by matt on 12.01.2016.
 */
public class DownloadManager extends JPanel {

    private JButton startButton;
    private JButton sleepButton;
    private JButton suspendButton;
    private JButton resumeButton;
    private JButton stopButton;

    Downloader downloader;
    public DownloadManager(URL url, OutputStream os) throws IOException {
        downloader = new Downloader(url, os);
        buildLayout();
        Border border = new BevelBorder(BevelBorder.RAISED);
        String name = url.toString();
        int index = name.lastIndexOf('/');
        border = new TitledBorder(border, name.substring(index + 1));
        setBorder(border);
    }

    private void buildLayout() {
        setLayout(new BorderLayout());
        downloader.setBorder(new BevelBorder(BevelBorder.RAISED));
        add(downloader, BorderLayout.CENTER);

        add(getButtonPane(), BorderLayout.SOUTH);
    }

    private JPanel getButtonPane() {
        JPanel outerPanel;
        JPanel innerPanel;

        innerPanel = new JPanel();
        innerPanel.setLayout(new GridLayout(1, 5, 10, 0));

        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(false);
                sleepButton.setEnabled(true);
                suspendButton.setEnabled(true);
                resumeButton.setEnabled(false);
                stopButton.setEnabled(true);
                downloader.startDownload();
            }
        });
        innerPanel.add(startButton);

        sleepButton = new JButton("Sleep");
        sleepButton.setEnabled(false);
        sleepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downloader.setSleepSheduler(true);
            }
        });
        innerPanel.add(sleepButton);

        suspendButton = new JButton("Suspend");
        suspendButton.setEnabled(false);
        suspendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                suspendButton.setEnabled(false);
                resumeButton.setEnabled(true);
                stopButton.setEnabled(true);
                downloader.setSuspended(true);
            }
        });
        innerPanel.add(suspendButton);

        resumeButton = new JButton("Resume");
        resumeButton.setEnabled(false);
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resumeButton.setEnabled(false);
                suspendButton.setEnabled(true);
                stopButton.setEnabled(true);
                downloader.resumeDownload();
            }
        });
        innerPanel.add(resumeButton);

        stopButton = new JButton("Stop");
        stopButton.setEnabled(false);
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopButton.setEnabled(false);
                sleepButton.setEnabled(false);
                suspendButton.setEnabled(false);
                resumeButton.setEnabled(false);
                downloader.stopDownloads();
            }
        });
        innerPanel.add(stopButton);

        outerPanel = new JPanel();
        outerPanel.add(innerPanel);
        return outerPanel;
    }

    public static void main(String[] args) throws Exception {
        URL url = new URL(args[0]);
        FileOutputStream fos = new FileOutputStream(args[1]);
        JFrame f = new JFrame();
        DownloadManager dm = new DownloadManager(url, fos);
        f.getContentPane().add(dm);
        f.setSize(600,400);
        f.setVisible(true);
    }
}
