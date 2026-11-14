public class Crupier extends Jugador {
    private boolean ocultaCarta = true;

    public Crupier() { super("Crupier", 0); }

    /** El crupier pide carta con 16 o menos. */
    @Override
    public boolean decidirJugada() { return getMano().calcularValor() < 17; }

    public void revelarCarta() { ocultaCarta = false; }

    public boolean isOcultaCarta() { return ocultaCarta; }

    @Override
    public void reiniciarRonda() {
        super.reiniciarRonda();
        ocultaCarta = true;
    }
}
