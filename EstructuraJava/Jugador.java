import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private String nombre;
    private double saldo;
    private Mano mano = new Mano();
    private List<Habilidad> habilidades = new ArrayList<>();

    private double apuesta;
    private ContextoRonda contexto = new ContextoRonda(mano);
    private final List<String> eventos = new ArrayList<>();

    public Jugador(String nombre, double saldo) {
        this.nombre = nombre;
        this.saldo = saldo;
    }

    public void apostar(double monto) {
        if (monto <= 0 || monto > saldo)
            throw new IllegalArgumentException("Apuesta inválida");
        saldo -= monto;
        apuesta = monto;
    }

    public void recibirCarta(Carta c) { mano.agregar(c); }

    public boolean decidirJugada() { return mano.calcularValor() < 17; }

    public void usarHabilidades(Momento momento) {
        for (Habilidad h : habilidades) {
            if (h.getMomento() != momento) continue;
            int p0 = contexto.getPuntos();
            double m0 = contexto.getMultiplicador();
            h.aplicar(contexto);
            int dp = contexto.getPuntos() - p0;
            double dm = contexto.getMultiplicador() / m0;
            if (dp != 0) eventos.add("⚡ " + nombre + ": " + h.getNombre() + " (+" + dp + " fichas)");
            if (dm != 1.0) eventos.add("⚡ " + nombre + ": " + h.getNombre() + " (ganancia x" + dm + ")");
        }
    }

    public void reiniciarRonda() {
        mano = new Mano();
        contexto = new ContextoRonda(mano);
        apuesta = 0;
    }

    public void agregarHabilidad(Habilidad h) { habilidades.add(h); }

    public void ajustarSaldo(double delta) { saldo += delta; }

    public List<String> retirarEventos() {
        List<String> copia = new ArrayList<>(eventos);
        eventos.clear();
        return copia;
    }

    public String getNombre() { return nombre; }

    public double getSaldo() { return saldo; }

    public Mano getMano() { return mano; }

    public List<Habilidad> getHabilidades() { return habilidades; }

    public double getApuesta() { return apuesta; }

    public ContextoRonda getContexto() { return contexto; }
}
