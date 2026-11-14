import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazo {
    private final List<Carta> cartas = new ArrayList<>();

    public Mazo() { reiniciar(); }

    public void barajar() { Collections.shuffle(cartas); }

    public Carta repartir() {
        if (cartas.isEmpty()) {
            reiniciar();
            barajar();
        }
        return cartas.remove(cartas.size() - 1);
    }

    public void reiniciar() {
        cartas.clear();
        for (Palo p : Palo.values())
            for (Valor v : Valor.values())
                cartas.add(new Carta(p, v));
    }
}
