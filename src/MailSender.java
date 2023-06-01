import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class MailSender {

    //  Mail göndermek için Google SMTP server'ına bağlandığımız key ile giriş yapmak için
    private final String username;// email
    private final String password;// smtp şifresi
    private final Properties properties;
    public MailSender(String username, String password, String smtpHost, String smtpPort) {
        this.username = username;
        this.password = password;


        //  SMTP server'ına bağlanmak için gerekli bilgileri yerleştiriyoruz
        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
    }


    //  Mail gönderme metodumuz; göndereceğimiz adres, başlık, içerik ve görsel alıyor alıyor
    public void sendEmail(String recipient, String subject, String body, BufferedImage image) throws MessagingException, IOException {

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // email mesajı oluşturuyoruz
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);

        // Multipart mesaj
        MimeMultipart multipart = new MimeMultipart();

        // mesaj ekle
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(body);
        multipart.addBodyPart(textPart);

        // Görsel ekle
        MimeBodyPart imagePart = new MimeBodyPart();

        // Buffered image -> byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", baos);
        byte[] imageBytes = baos.toByteArray();

        // Data formatı ayarlıyoruz
        DataSource ds = new ByteArrayDataSource(imageBytes, "image/jpeg");
        imagePart.setDataHandler(new DataHandler(ds));
        imagePart.setFileName("image.jpg");
        multipart.addBodyPart(imagePart);

        // multipart mesajı email mesajı yapıyoruz
        message.setContent(multipart);

        Transport.send(message);
    }

    // kendi metodunu kullanarak çoklu mail atma metodu
    public void sendMulitpleEmail(String recipientsString, String subject, String body, BufferedImage image) throws MessagingException, IOException {
        String[] recipients = recipientsString.split(",");

        for(String recipient : recipients){
            recipient = recipient.strip();
            sendEmail(recipient, subject, body, image);
        }
    }
}