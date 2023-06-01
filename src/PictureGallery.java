import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import java.awt.Image;

public class PictureGallery extends JFrame {
    private JPanel picturePanel;
    // Görsellerin ekrana güzel sığması için 1920x1080'i yarıya böldük
    private int imageWidth = 960;
    private int imageHeight = 540;

    public PictureGallery(String directoryPath) {
        setTitle("Paylaşılan Fotoğraflar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Swing üzerinde android recyclerview tarzında sürüklenebilir panel yapıyoruz
        picturePanel = new JPanel();
        picturePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        picturePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // scroller ekliyoruz
        JScrollPane scrollPane = new JScrollPane(picturePanel);

        add(scrollPane, BorderLayout.CENTER);

        // Ekranın görsel boyutuna uygun olmasını sağlıyoruz
        setSize(imageWidth + 40, imageHeight + 120);
        setVisible(true);

        // paylaşılan görsellerin olduğu klasörden görselleri yüklüyoruz
        loadPictures(directoryPath);
    }
    private void loadPictures(String directoryPath) {
        File directory = new File(directoryPath);
        if (directory.isDirectory()) {
            File[] pictureFiles = directory.listFiles();
            if (pictureFiles != null) {
                for (File pictureFile : pictureFiles) {
                    if (isImageFile(pictureFile)) {
                        ImageIcon imageIcon = new ImageIcon(pictureFile.getAbsolutePath());
                        Image scaledImage = imageIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
                        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
                        JLabel imageLabel = new JLabel(scaledImageIcon);
                        imageLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
                        picturePanel.add(imageLabel);
                    }
                }
            }
        }

        // Yeni görsellerin eklenmesi durumunda paneli güncelliyoruz
        picturePanel.revalidate();
        picturePanel.repaint();
    }

    // Görselleri yüklerken bu fonksiyon sayesinde sadece görselleri göstereceğiz
    private boolean isImageFile(File file) {
        String name = file.getName();
        String extension = name.substring(name.lastIndexOf(".") + 1);
        String[] imageExtensions = {"jpg", "jpeg", "png", "gif"};
        for (String ext : imageExtensions) {
            if (ext.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
}
