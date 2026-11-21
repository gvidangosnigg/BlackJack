/** Al ganar, multiplica la ganancia por un factor (por defecto x2). */
public class DuplicarPuntos implements Habilidad {
    private double factor;

    public DuplicarPuntos() { this(2.0); }

    public DuplicarPuntos(double factor) { this.factor = factor; }

    @Override
    public void aplicar(ContextoRonda ctx) { ctx.multiplicar(factor); }

    @Override
    public Momento getMomento() { return Momento.AL_GANAR; }

    @Override
    public String getDescripcion() {
        return "Al ganar una ronda, tu ganancia se multiplica por " + (int) factor + ".";
    }

    @Override
    public String getNombre() { return "Duplicar Puntos"; }
}
