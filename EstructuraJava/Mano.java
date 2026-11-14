import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mano {
    private final List<Carta> cartas = new ArrayList<>();

    public void agregar(Carta c) { cartas.add(c); }

    public List<Carta> getCartas() { return Collections.unmodifiableList(cartas); }

    public int calcularValor() {
        int total = 0, ases = 0;
        for (Carta c : cartas) {
            total += c.getPuntos();
            if (c.getValor() == Valor.AS) ases++;
        }
        while (total > 21 && ases > 0) {
            total -= 10;
            ases--;
        }
        return total;
    }

    public boolean esBlackjack() { return cartas.size() == 2 && calcularValor() == 21; }

    public boolean sePasa() { return calcularValor() > 21; }
}
