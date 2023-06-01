import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ShareDialog extends JDialog {
    private JTextField recipientField;
    private JTextField headerField;
    private JButton sendButton;
    private JButton cancelButton;

    private String recipients;
    private String header;

    // Bu class sayesinde paylaşım yaparken pop up dialog oluşturacağız
    public ShareDialog(Frame parent) {
        super(parent, "Mail Gönder", true);

        // panel oluşturuyoruz
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 10, 10, 10);

        // ALıcı textfield label'ı
        JLabel recipientLabel = new JLabel("Alıcılar:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(recipientLabel, constraints);

        // emailler buraya yazılacak
        recipientField = new JTextField("\",\" ile ayırınız",40);

        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(recipientField, constraints);

        // Header label'ı
        JLabel headerLabel = new JLabel("Başlık:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(headerLabel, constraints);

        headerField = new JTextField(40);
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(headerField, constraints);

        // Gönderme ve iptal butonları
        sendButton = new JButton("Gönder");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                recipients = recipientField.getText();
                header = headerField.getText();
                dispose();
            }
        });

        cancelButton = new JButton("İptal");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                recipients = null;
                header = null;
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(sendButton);
        buttonPanel.add(cancelButton);

        // panel layout ayarlıyoruz
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.PAGE_END);

        getContentPane().add(contentPanel);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    // Textfieldlardan getter fonksiyonlarımız
    public String getRecipients() {
        return recipients;
    }

    public String getHeader() {
        return header;
    }
}