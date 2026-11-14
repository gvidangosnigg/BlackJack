import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class CartaPanel extends JPanel {
    private Carta carta;

    public CartaPanel(Carta carta) {
        this.carta = carta;
        setOpaque(false);
        setPreferredSize(new Dimension(90, 130));
    }

    public Carta getCarta() { return carta; }

    public void setCarta(Carta carta) {
        this.carta = carta;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth() - 2, h = getHeight() - 2;
        RoundRectangle2D forma = new RoundRectangle2D.Double(1, 1, w, h, 14, 14);

        if (carta == null) {
            g2.setColor(new Color(255, 255, 255, 60));
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                    0, new float[]{6f, 6f}, 0));
            g2.draw(forma);
        } else if (carta.isBocaArriba()) {
            dibujarFrente(g2, forma, w, h);
        } else {
            dibujarReverso(g2, forma, w, h);
        }
        g2.dispose();
    }

    private void dibujarFrente(Graphics2D g2, RoundRectangle2D forma, int w, int h) {
        g2.setColor(Color.WHITE);
        g2.fill(forma);
        g2.setColor(Color.GRAY);
        g2.draw(forma);

        g2.setColor(carta.esRoja() ? new Color(200, 30, 30) : Color.BLACK);
        String valor = carta.getValor().getSimbolo();
        String palo = String.valueOf(carta.getPalo().getSimbolo());

        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, h / 8));
        g2.drawString(valor, 8, h / 8 + 6);
        g2.drawString(palo, 8, h / 4 + 6);

        g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, h / 2));
        FontMetrics fm = g2.getFontMetrics();
        int x = (w - fm.stringWidth(palo)) / 2 + 1;
        int y = (h + fm.getAscent() - fm.getDescent()) / 2 + 1;
        g2.drawString(palo, x, y);
    }

    private void dibujarReverso(Graphics2D g2, RoundRectangle2D forma, int w, int h) {
        g2.setColor(new Color(25, 60, 130));
        g2.fill(forma);
        g2.setColor(Color.WHITE);
        g2.draw(forma);
        g2.setColor(new Color(255, 255, 255, 70));
        g2.drawRoundRect(8, 8, w - 16, h - 16, 8, 8);
        for (int i = 14; i < w - 14; i += 10) {
            g2.drawLine(i, 14, i, h - 14);
        }
    }
}
