import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/** Ventanita para escoger habilidades: selecciona una, lee su descripción, agrégala o retrocede. */
public class DialogoHabilidades extends JDialog {
    private final List<Habilidad> disponibles = new ArrayList<>();
    private final List<Habilidad> elegidas = new ArrayList<>();
    private final DefaultListModel<String> modelo = new DefaultListModel<>();
    private final JList<String> lista = new JList<>(modelo);
    private final JTextArea descripcion = new JTextArea(4, 24);
    private final JLabel lblElegidas = new JLabel(" ");
    private final JButton btnElegir = new JButton("Elegir");
    private final JButton btnRetroceder = new JButton("Retroceder");
    private final JButton btnListo = new JButton("Comenzar");

    private DialogoHabilidades(Frame padre) {
        super(padre, "Escoge tus habilidades", true);
        disponibles.add(new DuplicarPuntos());
        disponibles.add(new BonusPorFigura());
        disponibles.add(new SeguroDePasarse());
        for (Habilidad h : disponibles) modelo.addElement(h.getNombre());

        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        descripcion.setEditable(false);
        descripcion.setLineWrap(true);
        descripcion.setWrapStyleWord(true);
        descripcion.setText("Selecciona una habilidad para ver su descripción.");

        JPanel centro = new JPanel(new GridLayout(1, 2, 8, 0));
        centro.add(new JScrollPane(lista));
        centro.add(new JScrollPane(descripcion));
        centro.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
        botones.add(btnElegir);
        botones.add(btnRetroceder);
        botones.add(btnListo);

        JPanel sur = new JPanel(new BorderLayout());
        lblElegidas.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        sur.add(lblElegidas, BorderLayout.NORTH);
        sur.add(botones, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(centro, BorderLayout.CENTER);
        add(sur, BorderLayout.SOUTH);

        lista.addListSelectionListener(e -> {
            Habilidad h = seleccionada();
            if (h != null) descripcion.setText(h.getDescripcion());
            actualizar();
        });
        btnElegir.addActionListener(e -> {
            Habilidad h = seleccionada();
            if (h != null && !elegidas.contains(h)) elegidas.add(h);
            actualizar();
        });
        btnRetroceder.addActionListener(e -> {
            if (!elegidas.isEmpty()) elegidas.remove(elegidas.size() - 1);
            actualizar();
        });
        btnListo.addActionListener(e -> dispose());

        actualizar();
        pack();
        setSize(Math.max(getWidth(), 520), Math.max(getHeight(), 260));
        setLocationRelativeTo(padre);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private Habilidad seleccionada() {
        int i = lista.getSelectedIndex();
        return i < 0 ? null : disponibles.get(i);
    }

    private void actualizar() {
        Habilidad h = seleccionada();
        btnElegir.setEnabled(h != null && !elegidas.contains(h));
        btnRetroceder.setEnabled(!elegidas.isEmpty());
        StringBuilder sb = new StringBuilder("Elegidas: ");
        if (elegidas.isEmpty()) sb.append("(ninguna)");
        for (int i = 0; i < elegidas.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(elegidas.get(i).getNombre());
        }
        lblElegidas.setText(sb.toString());
    }

    /** Muestra la ventanita y devuelve las habilidades elegidas. */
    public static List<Habilidad> elegir(Frame padre) {
        DialogoHabilidades d = new DialogoHabilidades(padre);
        d.setVisible(true); // bloquea hasta "Comenzar"
        return d.elegidas;
    }
}
