public enum Valor {
    DOS(2, "2", "Dos"), TRES(3, "3", "Tres"), CUATRO(4, "4", "Cuatro"),
    CINCO(5, "5", "Cinco"), SEIS(6, "6", "Seis"), SIETE(7, "7", "Siete"),
    OCHO(8, "8", "Ocho"), NUEVE(9, "9", "Nueve"), DIEZ(10, "10", "Diez"),
    J(10, "J", "Jota"), Q(10, "Q", "Reina"), K(10, "K", "Rey"),
    AS(11, "A", "As");

    private final int puntos;
    private final String simbolo;
    private final String nombre;

    Valor(int puntos, String simbolo, String nombre) {
        this.puntos = puntos;
        this.simbolo = simbolo;
        this.nombre = nombre;
    }

    public int getPuntos() { return puntos; }

    public int getPuntosBajos() { return this == AS ? 1 : puntos; }

    public String getSimbolo() { return simbolo; }
    public String getNombre()  { return nombre; }

    public boolean esAs()     { return this == AS; }
    public boolean esFigura() { return this == J || this == Q || this == K; }
    public boolean valeDiez() { return puntos == 10; }

    public static Valor desdeSimbolo(String s) {
        for (Valor v : values()) {
            if (v.simbolo.equalsIgnoreCase(s)) return v;
        }
        throw new IllegalArgumentException("Símbolo de valor no válido: " + s);
    }
}
