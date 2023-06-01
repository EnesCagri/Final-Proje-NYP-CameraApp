import java.awt.*;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicButtonUI;

public class MyButton extends JButton {

    // sürekli kullanacağımız için butonlarımız özel bir buton class'ı üzerinden oluşturuyoruz
    private Color hoverBackgroundColor;
    private Color pressedBackgroundColor;

    public MyButton(String text, Color backgroundColor, Color hoverBackgroundColor) {
        super(text);
        this.setBackground(backgroundColor);
        this.hoverBackgroundColor = hoverBackgroundColor;
        this.pressedBackgroundColor = backgroundColor;

        this.setUI(new MyButtonUI());

        // mouse hareketlerine göre hover efekti veriyoruz
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(hoverBackgroundColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(backgroundColor);
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                setBackground(pressedBackgroundColor);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                setBackground(hoverBackgroundColor);
            }
        });
    }

    private static class MyButtonUI extends BasicButtonUI {
        @Override
        public void paint(Graphics g, javax.swing.JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getBackground());
            g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 20, 20);
            g2.dispose();
            super.paint(g, c);
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
        //remove border
    }
}
