import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaBlackjack extends JFrame {
    private static final Color MESA = new Color(0x0B6623);

    private final Estadisticas stats = new Estadisticas();
    private Juego juego;
    private Jugador humano;
    private Crupier crupier;

    private final PanelMano panelCrupier = new PanelMano();
    private final PanelMano panelJugador = new PanelMano();
    private final JLabel lblCrupier = etiqueta("Crupier");
    private final JLabel lblJugador = etiqueta("Jugador");
    private final JLabel lblSaldo = etiqueta("");
    private final JLabel lblHabilidades = etiqueta("");
    private final JSpinner spApuesta = new JSpinner(new SpinnerNumberModel(10, 10, 1000, 10));
    private final JButton btnRepartir = new JButton("Repartir");
    private final JButton btnPedir = new JButton("Pedir carta");
    private final JButton btnPlantarse = new JButton("Plantarse");
    private final JButton btnReiniciar = new JButton("Nueva partida");
    private final JTextArea log = new JTextArea(6, 40);

    public VentanaBlackjack() {
        super("Blackjack con habilidades");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        construirUI();
        mostrarBienvenida();
        nuevaPartida();
        pack();
        setMinimumSize(new Dimension(760, 640));
        setLocationRelativeTo(null);
    }

    private static JLabel etiqueta(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("SansSerif", Font.BOLD, 15));
        return l;
    }

    private void construirUI() {
        JPanel mesa = new JPanel(new GridLayout(2, 1, 0, 10));
        mesa.setBackground(MESA);
        mesa.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        mesa.add(zona(lblCrupier, panelCrupier));
        mesa.add(zona(lblJugador, panelJugador));

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setBackground(MESA);
        info.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        info.add(lblSaldo);
        info.add(lblHabilidades);

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        controles.setBackground(new Color(0x084C1B));
        JLabel lblApuesta = etiqueta("Apuesta:");
        controles.add(lblApuesta);
        controles.add(spApuesta);
        controles.add(btnRepartir);
        controles.add(btnPedir);
        controles.add(btnPlantarse);
        controles.add(btnReiniciar);

        log.setEditable(false);
        log.setLineWrap(true);
        log.setWrapStyleWord(true);

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(info, BorderLayout.NORTH);
        norte.add(mesa, BorderLayout.CENTER);

        JPanel sur = new JPanel(new BorderLayout());
        sur.add(controles, BorderLayout.NORTH);
        sur.add(new JScrollPane(log), BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(norte, BorderLayout.CENTER);
        add(sur, BorderLayout.SOUTH);

        btnRepartir.addActionListener(e -> repartir());
        btnPedir.addActionListener(e -> pedir());
        btnPlantarse.addActionListener(e -> plantarse());
        btnReiniciar.addActionListener(e -> nuevaPartida());
    }

    private JPanel zona(JLabel titulo, PanelMano mano) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(titulo, BorderLayout.NORTH);
        p.add(mano, BorderLayout.CENTER);
        return p;
    }

    private void mostrarBienvenida() {
        String msg;
        if (!stats.hayHistorial()) {
            msg = "¡Bienvenido a Blackjack!\n\n"
                + "Reglas:\n"
                + "- Acércate a 21 sin pasarte.\n"
                + "- Las figuras valen 10 y el As vale 11 u 1.\n"
                + "- Blackjack (21 con dos cartas) paga 3:2.\n"
                + "- El crupier pide carta con 16 o menos y se planta con 17 o más.\n"
                + "- Las habilidades te dan ventajas especiales durante la ronda.";
        } else {
            msg = "Bienvenido de nuevo:\npartidas ganadas: " + stats.getGanadas()
                + ", perdidas: " + stats.getPerdidas();
        }
        JOptionPane.showMessageDialog(this, msg, "Blackjack", JOptionPane.INFORMATION_MESSAGE);
    }

    private void nuevaPartida() {
        humano = new Jugador("Jugador", 500);
        List<Habilidad> elegidas = DialogoHabilidades.elegir(this);
        for (Habilidad h : elegidas) humano.agregarHabilidad(h);
        juego = new Juego(humano);
        crupier = juego.getCrupier();
        log.setText("¡Bienvenido! Tienes 500 fichas y " + elegidas.size() + " habilidad(es)\n");
        panelCrupier.setMano(null, false);
        panelJugador.setMano(null, false);
        modoApuesta();
        refrescar();
    }

    private void repartir() {
        double apuesta = ((Number) spApuesta.getValue()).doubleValue();
        if (apuesta > humano.getSaldo()) {
            JOptionPane.showMessageDialog(this, "No tienes saldo suficiente");
            return;
        }
        juego.iniciar();
        humano.apostar(apuesta);
        juego.repartirInicial();
        log.append("\n--- Nueva ronda: apuesta " + (int) apuesta + " \n");
        volcarEventos();
        if (humano.getMano().esBlackjack() || crupier.getMano().esBlackjack()) {
            terminarRonda();
        } else {
            btnRepartir.setEnabled(false);
            spApuesta.setEnabled(false);
            btnPedir.setEnabled(true);
            btnPlantarse.setEnabled(true);
            refrescar();
        }
    }

    private void pedir() {
        juego.pedirCarta(humano);
        volcarEventos();
        int v = humano.getMano().calcularValor();
        if (humano.getMano().sePasa() || v == 21) terminarRonda();
        else refrescar();
    }

    private void plantarse() { terminarRonda(); }

    private void terminarRonda() {
        juego.jugarRonda();
        String resultado = juego.determinarGanador();
        volcarEventos();
        if (juego.getUltimoResultado() > 0) stats.registrarVictoria();
        else if (juego.getUltimoResultado() < 0) stats.registrarDerrota();
        log.append(resultado + "\n");
        modoApuesta();
        refrescar();
        if (humano.getSaldo() < 10) {
            btnRepartir.setEnabled(false);
            JOptionPane.showMessageDialog(this, "Te quedaste sin fichas. Inicia una nueva partida");
        }
    }

    private void modoApuesta() {
        btnRepartir.setEnabled(true);
        spApuesta.setEnabled(true);
        btnPedir.setEnabled(false);
        btnPlantarse.setEnabled(false);
        int max = Math.max(10, (int) humano.getSaldo());
        SpinnerNumberModel m = (SpinnerNumberModel) spApuesta.getModel();
        m.setMaximum(max);
        if (((Number) m.getValue()).intValue() > max) m.setValue(max);
    }

    private void volcarEventos() {
        List<String> ev = juego.retirarEventos();
        for (String s : ev) log.append(s + "\n");
        log.setCaretPosition(log.getDocument().getLength());
    }

    private void refrescar() {
        boolean ocultar = crupier.isOcultaCarta() && !crupier.getMano().getCartas().isEmpty();
        panelCrupier.setMano(crupier.getMano(), ocultar);
        panelJugador.setMano(humano.getMano(), false);

        String vc = crupier.getMano().getCartas().isEmpty() ? "-"
                : ocultar ? String.valueOf(crupier.getMano().getCartas().get(0).getPuntos())
                          : String.valueOf(crupier.getMano().calcularValor());
        lblCrupier.setText("Crupier  (puntos: " + vc + ")");
        lblJugador.setText("Jugador  (puntos: " + humano.getMano().calcularValor() + ")");
        lblSaldo.setText(String.format("Saldo: %.0f fichas   |   Apuesta actual: %.0f",
                humano.getSaldo(), humano.getApuesta()));
        StringBuilder sb = new StringBuilder("Habilidades: ");
        for (Habilidad h : humano.getHabilidades())
            sb.append("[").append(h.getNombre()).append("] ");
        lblHabilidades.setText(sb.toString());
    }

    private static class PanelMano extends JPanel {
        private Mano mano;
        private boolean ocultarSegunda;

        PanelMano() { setOpaque(false); setPreferredSize(new Dimension(500, 120)); }

        void setMano(Mano m, boolean ocultarSegunda) {
            this.mano = m;
            this.ocultarSegunda = ocultarSegunda;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            if (mano == null) return;
            Graphics2D g = (Graphics2D) g0.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = 70, h = 100, x = 10, y = 8;
            int i = 0;
            for (Carta c : mano.getCartas()) {
                if (ocultarSegunda && i == 1) dibujarReverso(g, x, y, w, h);
                else dibujarCarta(g, c, x, y, w, h);
                x += w + 10;
                i++;
            }
            g.dispose();
        }

        private void dibujarCarta(Graphics2D g, Carta c, int x, int y, int w, int h) {
            g.setColor(Color.WHITE);
            g.fillRoundRect(x, y, w, h, 12, 12);
            g.setColor(Color.DARK_GRAY);
            g.drawRoundRect(x, y, w, h, 12, 12);
            g.setColor(c.getPalo().esRojo() ? new Color(0xC62828) : Color.BLACK);
            g.setFont(new Font("SansSerif", Font.BOLD, 18));
            g.drawString(c.getValor().getSimbolo(), x + 7, y + 22);
            g.setFont(new Font("SansSerif", Font.PLAIN, 34));
            FontMetrics fm = g.getFontMetrics();
            String s = c.getPalo().getSimbolo();
            g.drawString(s, x + (w - fm.stringWidth(s)) / 2, y + h / 2 + 14);
        }

        private void dibujarReverso(Graphics2D g, int x, int y, int w, int h) {
            g.setColor(new Color(0x1A237E));
            g.fillRoundRect(x, y, w, h, 12, 12);
            g.setColor(Color.WHITE);
            g.drawRoundRect(x + 5, y + 5, w - 10, h - 10, 8, 8);
            g.setFont(new Font("SansSerif", Font.BOLD, 30));
            g.drawString("?", x + w / 2 - 8, y + h / 2 + 10);
        }
    }
}
