import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import com.restfb.BinaryAttachment;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.FacebookType;

public class FacebookHandler {

    // sınıf düzgün çalışmasına rağmen API izinleri verification nedeniyle paylaşım yapamiyoruz

    private String accessToken;
    public FacebookHandler(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean sharePicture(BufferedImage image, String message) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] imageData = baos.toByteArray();

            FacebookClient facebookClient = new DefaultFacebookClient(accessToken, Version.VERSION_12_0);
            List<BinaryAttachment> binaryAttachments = new ArrayList<>();
            binaryAttachments.add(BinaryAttachment.with("picture.jpg", imageData));

            FacebookType response = facebookClient.publish("me/photos",
                    FacebookType.class,
                    binaryAttachments,
                    Parameter.with("message", message));

            return response.getId() != null;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

}
