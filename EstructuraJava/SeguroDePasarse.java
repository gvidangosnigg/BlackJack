/** Si te pasas de 21, devuelve fichas (limitado por usos restantes). */
public class SeguroDePasarse implements Habilidad {
    private static final int DEVOLUCION = 25;
    private int usosRestantes;

    public SeguroDePasarse() { this(2); }

    public SeguroDePasarse(int usos) { this.usosRestantes = usos; }

    @Override
    public void aplicar(ContextoRonda ctx) {
        if (usosRestantes > 0 && ctx.getMano().sePasa()) {
            usosRestantes--;
            ctx.sumarPuntos(DEVOLUCION);
        }
    }

    @Override
    public Momento getMomento() { return Momento.AL_CALCULAR_PUNTOS; }

    @Override
    public String getDescripcion() {
        return "Si te pasas de 21, recuperas " + DEVOLUCION + " fichas. Se puede usar " + usosRestantes + " veces.";
    }

    @Override
    public String getNombre() { return "Seguro de Pasarse (" + usosRestantes + " usos)"; }
}
