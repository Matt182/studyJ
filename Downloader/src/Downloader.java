
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by matt on 12.01.2016.
 */
public class Downloader extends JPanel implements Runnable {
    private URL downloadURL;
    private InputStream inputStream;
    private OutputStream outputStream;
    private byte[] buffer;

    private int fileSize;
    private int bytesread;

    private JLabel urlLabel;
    private JLabel sizeLabel;
    private JLabel completeLabel;
    private JProgressBar progressBar;
    public final static int BUFFER_SIZE = 1000;
    private boolean sleepSheduler;

    public static int SLEEP_TIME = 5 * 1000;

    private boolean stopped;
    private boolean suspended;

    private Thread thisThread;

    public static ThreadGroup downloadGroup = new ThreadGroup("Download threads");

    public Downloader(URL url, OutputStream os) throws IOException {
        downloadURL = url;
        outputStream = os;
        bytesread = 0;
        URLConnection urlConnection = downloadURL.openConnection();
        fileSize = urlConnection.getContentLength();
        if (fileSize == -1) {
            throw new FileNotFoundException(url.toString());
        }
        inputStream = new BufferedInputStream(urlConnection.getInputStream());
        buffer = new byte[BUFFER_SIZE];
        thisThread = new Thread(downloadGroup, this);

        buildLayout();
        stopped = false;
        sleepSheduler = false;
        suspended = false;
    }

    public static void cancelAllAndWait() {
        int count = downloadGroup.activeCount();
        Thread[] threads = new Thread[count];
        count = downloadGroup.enumerate(threads);
        downloadGroup.interrupt();

        for (int i = 0; i < count; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void startDownload(){
        thisThread.start();
    }

    public synchronized void resumeDownload() {
        this.notify();
    }

    public void stopDownloads(){
        thisThread.interrupt();
    }

    public synchronized void setStopped(boolean stop){
        stopped = stop;
    }

    public synchronized boolean isStopped() {
        return stopped;
    }

    public synchronized void setSuspended(boolean suspend) {
        suspended = suspend;
    }

    public synchronized boolean isSuspended() {
        return suspended;
    }

    public synchronized void setSleepSheduler(boolean doSleep){
        sleepSheduler = doSleep;
    }

    public synchronized boolean isSleepSheduled() {
        return sleepSheduler;
    }

    private void buildLayout() {
        JLabel label;
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 10, 5, 10);

        constraints.gridx = 0;
        label = new JLabel("URL:", JLabel.LEFT);
        add(label, constraints);

        label = new JLabel("Complete:",JLabel.LEFT);
        add(label,constraints);

        label = new JLabel("Downloaded:", JLabel.LEFT);
        add(label, constraints);

        constraints.gridx = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weightx = 1;
        urlLabel = new JLabel(downloadURL.toString());
        add(urlLabel, constraints);

        progressBar = new JProgressBar(0, fileSize);
        progressBar.setStringPainted(true);
        add(progressBar, constraints);

        constraints.gridwidth = 1;
        completeLabel = new JLabel(Integer.toString(bytesread));
        add(completeLabel, constraints);

        constraints.gridx = 2;
        constraints.weightx = 0;
        constraints.anchor = GridBagConstraints.EAST;
        label = new JLabel("Size:", JLabel.LEFT);
        add(label, constraints);

        constraints.gridx = 3;
        constraints.weightx = 1;
        sizeLabel = new JLabel(Integer.toString(fileSize));
        add(sizeLabel, constraints);
    }

    public void run() {
        performDownload();
    }

    public void performDownload() {
        int byteCount;
        Runnable progressUpdate = new Runnable() {
            public void run(){
                progressBar.setValue(bytesread);
                completeLabel.setText(Integer.toString(bytesread));
            }
        };
        while ((bytesread < fileSize) && (!isStopped())) {
            try {
                if (isSleepSheduled()) {
                    try {
                        Thread.sleep(SLEEP_TIME);
                        setSleepSheduler(false);
                    } catch (Exception e) {
                        setStopped(true);
                        break;
                    }
                }
                byteCount = inputStream.read(buffer);
                if (byteCount == -1) {
                    setStopped(true);
                    break;
                } else {
                    outputStream.write(buffer, 0, byteCount);
                    bytesread += byteCount;
                    SwingUtilities.invokeLater(progressUpdate);
                }
            } catch (IOException e) {
                setStopped(true);
                JOptionPane.showMessageDialog(this, e.getMessage(), "IO Error", JOptionPane.ERROR_MESSAGE);
                break;
            }
            synchronized (this) {
                if (isSuspended()) {
                    try {
                        this.wait();
                        setSuspended(false);
                    } catch (InterruptedException e) {
                        setStopped(true);
                        break;
                    }
                }
            }
            if (Thread.interrupted()) {
                setStopped(true);
                break;
            }
        }
        try {
            outputStream.close();
            inputStream.close();
            File file = new File("C:\\Users\\matt\\Desktop\\studyJ\\zzzz.jpg");
            file.delete();

        } catch (IOException e) {

        }
    }

    public static void main(String[] args) throws Exception {
        Downloader dl = null;
        if (args.length < 2) {
            System.out.println("enter 2 args please url and folder");
            System.exit(0);
        }
        URL url = new URL(args[0]);
        FileOutputStream fo = new FileOutputStream(args[1]);
        DownloadManager dm = new DownloadManager(url, fo);
        dl = new Downloader(url, fo);
        JFrame f = new JFrame();
        f.getContentPane().add(dm);
        f.setSize(600, 400);
        f.setVisible(true);
        dl.thisThread.start();
    }
}
