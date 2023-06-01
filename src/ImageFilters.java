import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageFilters {

    // Görsellere uygulamak için filtrelerimiz

    // Görsellerimiz üzerinde daha iyi oynama yapabilmemiz için buffered image olarak alınıyor

    // Siyah Beyaz filtresi
    public static BufferedImage applyBlackAndWhiteFilter(BufferedImage original) {
        BufferedImage filtered = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        filtered.getGraphics().drawImage(original, 0, 0, null);
        return filtered;
    }

    public static BufferedImage applyColorEffect(BufferedImage original) {
        BufferedImage filtered = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < original.getWidth(); x++) {
            for (int y = 0; y < original.getHeight(); y++) {
                int rgb = original.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                int intensity = (r + g + b) / 3; // Calculate the intensity of the pixel

                int threshold = 100; // Adjust this threshold value to control the sensitivity of color detection

                if (intensity < threshold) {
                    // Convert subtle colors to black and white
                    int grayscale = (intensity << 16) | (intensity << 8) | intensity;
                    filtered.setRGB(x, y, grayscale);
                } else {
                    // Make prominent colors appear red
                    int red = 255;
                    int redPixel = (red << 16) | (0 << 8) | 0;
                    filtered.setRGB(x, y, redPixel);
                }
            }
        }
        return filtered;
    }

    // parlaklık arttırma
    public static BufferedImage applyBrightnessUpFilter(BufferedImage original) {
        BufferedImage filtered = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        float scaleFactor = 1.2f;
        for (int x = 0; x < original.getWidth(); x++) {
            for (int y = 0; y < original.getHeight(); y++) {
                int rgb = original.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int r2 = Math.min(255, (int) (r * scaleFactor));
                int g2 = Math.min(255, (int) (g * scaleFactor));
                int b2 = Math.min(255, (int) (b * scaleFactor));
                int rgb2 = (r2 << 16) | (g2 << 8) | b2;
                filtered.setRGB(x, y, rgb2);
            }
        }
        return filtered;
    }

    // Negatif filtre
    public static BufferedImage applyInvertFilter(BufferedImage original) {
        BufferedImage filtered = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        for (int x = 0; x < original.getWidth(); x++) {
            for (int y = 0; y < original.getHeight(); y++) {
                int rgb = original.getRGB(x, y);
                int r = 255 - (rgb >> 16) & 0xFF;
                int g = 255 - (rgb >> 8) & 0xFF;
                int b = 255 - rgb & 0xFF;
                int rgb2 = (r << 16) | (g << 8) | b;
                filtered.setRGB(x, y, rgb2);
            }
        }
        return filtered;
    }

    // Sepya filtre
    public static BufferedImage applySepiaFilter(BufferedImage original) {
        BufferedImage filtered = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        for (int x = 0; x < original.getWidth(); x++) {
            for (int y = 0; y < original.getHeight(); y++) {
                int rgb = original.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int r2 = Math.min(255, (int) (0.393*r + 0.769*g + 0.189*b));
                int g2 = Math.min(255, (int) (0.349*r + 0.686*g + 0.168*b));
                int b2 = Math.min(255, (int) (0.272*r + 0.534*g + 0.131*b));
                int rgb2 = (r2 << 16) | (g2 << 8) | b2;
                filtered.setRGB(x, y, rgb2);
            }
        }
        return filtered;
    }

    // Keskinleştirme filtresi
    public static BufferedImage applyHighPassFilter(BufferedImage original) {
        BufferedImage filtered = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        int[][] kernel = {{-1, -1, -1}, {-1, 9, -1}, {-1, -1, -1}};
        for (int x = 1; x < original.getWidth() - 1; x++) {
            for (int y = 1; y < original.getHeight() - 1; y++) {
                int r = 0, g = 0, b = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int rgb = original.getRGB(x + i, y + j);
                        r += ((rgb >> 16) & 0xFF) * kernel[i + 1][j + 1];
                        g += ((rgb >> 8) & 0xFF) * kernel[i + 1][j + 1];
                        b += (rgb & 0xFF) * kernel[i + 1][j + 1];
                    }
                }
                r = Math.min(255, Math.max(0, r));
                g = Math.min(255, Math.max(0, g));
                b = Math.min(255, Math.max(0, b));
                int rgb = (r << 16) | (g << 8) | b;
                filtered.setRGB(x, y, rgb);
            }
        }
        return filtered;
    }

}
