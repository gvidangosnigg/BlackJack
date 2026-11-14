import java.util.ArrayList;
import java.util.List;

public class Juego {
    private Mazo mazo = new Mazo();
    private List<Jugador> jugadores = new ArrayList<>();

    public Juego(Jugador humano) {
        jugadores.add(humano);
        jugadores.add(new Crupier());
    }

    public void iniciar() {
        mazo.reiniciar();
        mazo.barajar();
        for (Jugador j : jugadores) j.reiniciarRonda();
    }

    public void repartirInicial() {
        for (int i = 0; i < 2; i++)
            for (Jugador j : jugadores) j.recibirCarta(mazo.repartir());
        for (Jugador j : jugadores) j.usarHabilidades(Momento.AL_REPARTIR);
    }

    public void pedirCarta(Jugador j) {
        j.recibirCarta(mazo.repartir());
        j.usarHabilidades(Momento.AL_PEDIR_CARTA);
    }

    public void jugarRonda() {
        Crupier c = getCrupier();
        c.revelarCarta();
        boolean alguienVivo = false;
        for (Jugador j : jugadores)
            if (!(j instanceof Crupier) && !j.getMano().sePasa()) alguienVivo = true;
        while (alguienVivo && c.decidirJugada()) pedirCarta(c);
    }

    public String determinarGanador() {
        Crupier c = getCrupier();
        StringBuilder sb = new StringBuilder();
        for (Jugador j : jugadores) {
            if (j instanceof Crupier) continue;
            j.usarHabilidades(Momento.AL_CALCULAR_PUNTOS);
            Mano mj = j.getMano(), mc = c.getMano();
            int vj = mj.calcularValor(), vc = mc.calcularValor();
            double base = 1.0;
            int resultado; 

            if (mj.sePasa()) resultado = -1;
            else if (mc.sePasa()) resultado = 1;
            else if (mj.esBlackjack() && !mc.esBlackjack()) { resultado = 1; base = 1.5; }
            else if (mc.esBlackjack() && !mj.esBlackjack()) resultado = -1;
            else resultado = Integer.compare(vj, vc);

            ContextoRonda ctx = j.getContexto();
            if (resultado > 0) {
                j.usarHabilidades(Momento.AL_GANAR);
                double ganancia = (j.getApuesta() * base + ctx.getPuntos()) * ctx.getMultiplicador();
                j.ajustarSaldo(j.getApuesta() + ganancia);
                sb.append(String.format("🏆 ¡%s gana! (%d vs %d) +%.0f fichas", j.getNombre(), vj, vc, ganancia));
            } else if (resultado == 0) {
                j.ajustarSaldo(j.getApuesta());
                sb.append(String.format("🤝 Empate (%d vs %d). Se devuelve la apuesta.", vj, vc));
            } else {
                j.ajustarSaldo(ctx.getPuntos()); 
                sb.append(String.format("💀 %s pierde (%d vs %d) -%.0f fichas", j.getNombre(), vj, vc, j.getApuesta()));
                if (ctx.getPuntos() > 0) sb.append(" (seguro devuelve " + ctx.getPuntos() + ")");
            }
        }
        return sb.toString();
    }

    public List<String> retirarEventos() {
        List<String> todos = new ArrayList<>();
        for (Jugador j : jugadores) todos.addAll(j.retirarEventos());
        return todos;
    }

    public List<Jugador> getJugadores() { return jugadores; }

    public Jugador getHumano() { return jugadores.get(0); }

    public Crupier getCrupier() { return (Crupier) jugadores.get(jugadores.size() - 1); }
}
