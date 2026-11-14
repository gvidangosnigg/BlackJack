
public class ContextoRonda {
    private final Mano mano;
    private int puntos;
    private double multiplicador = 1.0;

    public ContextoRonda(Mano mano) { this.mano = mano; }

    public Mano getMano() { return mano; }

    public int getPuntos() { return puntos; }

    public double getMultiplicador() { return multiplicador; }

    public void sumarPuntos(int p) { puntos += p; }

    public void multiplicar(double f) { multiplicador *= f; }
}
