import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.FontUIResource;

public class WebcamApp extends JFrame implements ActionListener {
    MyButton saveButton, takeButton, galleryButton, filter1Button, filter2Button, filter3Button, filter4Button, filter5Button, filter6Button, shareButton;
    JPanel webcamPanel, buttonPanel, filterPanel, photoPanel;
    BufferedImage photo;
    private Webcam webcam;
    private ColorPalette colorPalette = new ColorPalette();

    // facebook için accessToken almıştık ancak API işlemlerinde sıkıntı oldu
    private final String accessToken = "accesstoken";
    private final String senderEmail = "yourmail@gmail.com";
    private final String password = "password";

    // facebook içindi ancak kullanmayacağız
    private final FacebookHandler facebookHandler = new FacebookHandler(accessToken);
    private MailSender mailSender;

    // galerimizin olacağı path
    private String sharedPicturesFile;
    public WebcamApp() {

        // Masaüstünde Pictures adlı bir galeri oluşturmaya / ulaşmaya çalışmak için adres alıyoruz
        FileSystemView view = FileSystemView.getFileSystemView();
        File file = view.getHomeDirectory();
        String desktopPath = file.getPath();

        sharedPicturesFile = desktopPath + File.separator + "Pictures";

        try {
            // uygulamamızı modern swing görünümününe çeviriyoruz
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // fontları ayarlıyoruz
        UIManager.put("Button.font", new FontUIResource(new Font("Sans-serif", Font.BOLD, 24)));
        UIManager.put("Label.font", new FontUIResource(new Font("Arial", Font.PLAIN, 18)));
        UIManager.put("Panel.font", new FontUIResource(new Font("Arial", Font.PLAIN, 16)));


        // webcam alıyoruz bu bizim anlık görüntümüz olacak
        webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(640, 480));
        webcam.open();

        webcamPanel = new JPanel();
        webcamPanel.setPreferredSize(new Dimension(640, 480));

        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setPreferredSize(new Dimension(640, 480));
        webcamPanel.add(panel);

        webcamPanel.setLayout(new BorderLayout());
        webcamPanel.add(panel, BorderLayout.CENTER);

        // layout ayarlıyoruz butonlar filtreler webcam ve foto paneli
        buttonPanel = new JPanel();
        filterPanel = new JPanel(new GridLayout(2, 2));
        photoPanel = new JPanel();
        photoPanel.setPreferredSize(new Dimension(640, 480));

        // butonları oluşturup actionlistener ekliyoruz
        saveButton = new MyButton("KAYDET", colorPalette.cyan, colorPalette.red);
        saveButton.addActionListener(this);
        takeButton = new MyButton("YAKALA", colorPalette.cyan, colorPalette.red);
        takeButton.addActionListener(this);

        filter1Button = new MyButton("Siyah-Beyaz", colorPalette.cyan, colorPalette.red);
        filter1Button.addActionListener(this);
        filter2Button = new MyButton("Kırmızı", colorPalette.cyan, colorPalette.red);
        filter2Button.addActionListener(this);
        filter3Button = new MyButton("Parlat", colorPalette.cyan, colorPalette.red);
        filter3Button.addActionListener(this);
        filter4Button = new MyButton("Keskinleştir", colorPalette.cyan, colorPalette.red);
        filter4Button.addActionListener(this);
        filter5Button = new MyButton("Sepya", colorPalette.cyan, colorPalette.red);
        filter5Button.addActionListener(this);
        filter6Button = new MyButton("Negatif", colorPalette.cyan, colorPalette.red);
        filter6Button.addActionListener(this);

        shareButton = new MyButton("PAYLAŞ", colorPalette.cyan, colorPalette.red);
        shareButton.addActionListener(this);

        galleryButton = new MyButton("Galeri", colorPalette.cyan, colorPalette.red);
        galleryButton.addActionListener(this);

        // panellere butonları ekliyoruz
        buttonPanel.add(saveButton);
        buttonPanel.add(takeButton);
        buttonPanel.add(shareButton);
        buttonPanel.add(galleryButton);

        filterPanel.add(filter1Button);
        filterPanel.add(filter2Button);
        filterPanel.add(filter3Button);
        filterPanel.add(filter4Button);
        filterPanel.add(filter5Button);
        filterPanel.add(filter6Button);

        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));

        filterPanel.setBackground(colorPalette.black);
        buttonPanel.setBackground(colorPalette.black);
        photoPanel.setBackground(colorPalette.black);
        webcamPanel.setBackground(colorPalette.black);

        // tüm buttonlara aynı görünümü vermek için dizi üzerinden for döngüsü ile görünüm veriyoruz
        JButton[] buttons = {saveButton, takeButton, galleryButton, filter1Button, filter2Button, filter3Button, shareButton, filter4Button, filter5Button, filter6Button};
        for(JButton b: buttons){
            b.setFocusable(false);
            b.setForeground(colorPalette.white);
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setBorder(BorderFactory.createEmptyBorder(10, 62, 10, 62)); // Add padding
        }

        // panelleri layout üzerine ekliyoruz
        add(webcamPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.NORTH);
        add(filterPanel, BorderLayout.SOUTH);
        add(photoPanel, BorderLayout.EAST);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        pack();
        getContentPane().setBackground(colorPalette.navy);
        setVisible(true);

        File galleryDirectory = new File(sharedPicturesFile);

        // eğer galeri klasörü yoksa oluşturalım
        if (!galleryDirectory.exists()) {
            boolean created = galleryDirectory.mkdirs();
            if (!created) {
                System.out.println("Galeri oluştururken hata oluştu");
            }
        }

    }

    // fotoğraf çekme
    private void takePhoto() {
        try {
            photo = webcam.getImage();
            photo = scaleImage(photo, photoPanel.getWidth(), photoPanel.getHeight());
            photoPanel.getGraphics().drawImage(photo, 0, 0, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // paylaşılan görseli feed gibi klasöre yazıyoruz
    private void saveSharedImage() {
        try {
            File saveDirectory = new File(sharedPicturesFile);
            if (!saveDirectory.exists()) {
                saveDirectory.mkdirs();
            }

            // Kaydedilen görsellerin eşsiz adlarının olmasını sağlıyor kaydedilme tarihi ile
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "image_" + timeStamp + ".jpg";

            File saveFile = new File(saveDirectory, fileName);

            ImageIO.write(photo, "jpg", saveFile);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Hata Oluştu", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public BufferedImage scaleImage(BufferedImage image, int width, int height) {
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage scaledBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = scaledBufferedImage.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        graphics.dispose();
        return scaledBufferedImage;
    }

    // eventlistener'larımızı kuruyoruz her butona özel
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == takeButton) {
            takePhoto();
        }
        else if (e.getSource() == saveButton) {
            try {
                JFileChooser fileChooser = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("JPEG file", "jpg", "jpeg");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showSaveDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String filePath = file.getAbsolutePath();
                    if (!filePath.endsWith(".jpg")) {
                        file = new File(filePath + ".jpg");
                    }
                    ImageIO.write(photo, "jpg", file);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else if (e.getSource() == filter1Button) {
            photo = ImageFilters.applyBlackAndWhiteFilter(photo);
            photoPanel.getGraphics().drawImage(photo, 0, 0, null);
        }
        else if (e.getSource() == filter2Button) {
            photo = ImageFilters.applyColorEffect(photo);
            photoPanel.getGraphics().drawImage(photo, 0, 0, null);
        }
        else if (e.getSource() == filter3Button) {
            photo = ImageFilters.applyBrightnessUpFilter(photo);
            photoPanel.getGraphics().drawImage(photo, 0, 0, null);
        }
        else if (e.getSource() == filter4Button) {
            photo = ImageFilters.applyHighPassFilter(photo);
            photoPanel.getGraphics().drawImage(photo, 0, 0, null);
        }
        else if (e.getSource() == filter5Button) {
            photo = ImageFilters.applySepiaFilter(photo);
            photoPanel.getGraphics().drawImage(photo, 0, 0, null);
        }
        else if (e.getSource() == filter6Button) {
            photo = ImageFilters.applyInvertFilter(photo);
            photoPanel.getGraphics().drawImage(photo, 0, 0, null);
        }
        else if(e.getSource() == galleryButton){

            PictureGallery galleryApp = new PictureGallery(sharedPicturesFile);

        }
        else if (e.getSource() == shareButton) {

            // pop up açalım
            ShareDialog dialog = new ShareDialog(WebcamApp.this);
            dialog.setVisible(true);

            // mail göndereceğimiz kişileri alıyoruz
            String recipients = dialog.getRecipients();
            String header = dialog.getHeader();

            if (recipients != null && header != null) {
                try {
                    MailSender mailSender = new MailSender(senderEmail, password, "smtp.gmail.com", "587");
                    mailSender.sendMulitpleEmail(recipients, header, "", photo);

                    // mail gönderdikten sonra galeriye kaydediyoruz
                    saveSharedImage();

                    JOptionPane.showMessageDialog(this, "Görsel Başarıyla Paylaşıldı");
                } catch (MessagingException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

