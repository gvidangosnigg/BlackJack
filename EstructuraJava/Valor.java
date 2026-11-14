public enum Valor {
    DOS("2", 2), TRES("3", 3), CUATRO("4", 4), CINCO("5", 5), SEIS("6", 6),
    SIETE("7", 7), OCHO("8", 8), NUEVE("9", 9), DIEZ("10", 10),
    J("J", 10), Q("Q", 10), K("K", 10), AS("A", 11);

    private final String simbolo;
    private final int puntos;

    Valor(String simbolo, int puntos) {
        this.simbolo = simbolo;
        this.puntos = puntos;
    }

    public String getSimbolo() { return simbolo; }

    public int getPuntos() { return puntos; }

    public boolean esFigura() { return this == J || this == Q || this == K; }
}
