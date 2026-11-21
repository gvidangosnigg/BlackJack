/** Al ganar, da fichas extra por cada figura (J, Q, K) en la mano. */
public class BonusPorFigura implements Habilidad {
    private int fichasExtra;

    public BonusPorFigura() { this(10); }

    public BonusPorFigura(int fichasExtra) { this.fichasExtra = fichasExtra; }

    @Override
    public void aplicar(ContextoRonda ctx) {
        int figuras = 0;
        for (Carta c : ctx.getMano().getCartas())
            if (c.getValor().esFigura()) figuras++;
        ctx.sumarPuntos(figuras * fichasExtra);
    }

    @Override
    public Momento getMomento() { return Momento.AL_GANAR; }

    @Override
    public String getDescripcion() {
        return "Al ganar, recibes " + fichasExtra + " fichas extra por cada figura (J, Q, K) en tu mano.";
    }

    @Override
    public String getNombre() { return "Bonus por Figura"; }
}
