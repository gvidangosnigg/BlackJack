import java.util.Objects;

public class Carta implements Comparable<Carta> {
    private final Palo palo;
    private final Valor valor;
    private boolean bocaArriba;

    public Carta(Palo palo, Valor valor) {
        this(palo, valor, true);
    }

    public Carta(Palo palo, Valor valor, boolean bocaArriba) {
        this.palo = Objects.requireNonNull(palo);
        this.valor = Objects.requireNonNull(valor);
        this.bocaArriba = bocaArriba;
    }
    public Palo getPalo()   { return palo; }
    public Valor getValor() { return valor; }
    public int getPuntos()  { return valor.getPuntos(); }

    public int getPuntosBajos()  { return valor.getPuntosBajos(); }
    public boolean esAs()        { return valor.esAs(); }
    public boolean esFigura()    { return valor.esFigura(); }   
    public boolean esRoja()      { return palo.esRojo(); }

    public boolean isBocaArriba()        { return bocaArriba; }
    public void setBocaArriba(boolean b) { this.bocaArriba = b; }
    public void voltear()                { this.bocaArriba = !bocaArriba; }
    public void ocultar()                { this.bocaArriba = false; }   
    public void revelar()                { this.bocaArriba = true; }    

    public String getEtiqueta() {
        return valor.getSimbolo() + palo.getSimbolo();
    }
    public String getNombreArchivoImagen() {
        return valor.name() + "_" + palo.name() + ".png";
    }

    @Override
    public int compareTo(Carta otra) {
        int c = valor.compareTo(otra.valor);
        return c != 0 ? c : palo.compareTo(otra.palo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Carta)) return false;
        Carta c = (Carta) o;
        return palo == c.palo && valor == c.valor;
    }

    @Override
    public int hashCode() { return Objects.hash(palo, valor); }

    @Override
    public String toString() {
        return valor.getNombre() + " de " + palo.getNombre();
    }
}
