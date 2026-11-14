public enum Palo {
    CORAZONES("Corazones", '\u2665', true),
    DIAMANTES("Diamantes", '\u2666', true),
    TREBOLES("Tréboles",  '\u2663', false),
    PICAS("Picas",        '\u2660', false);

    private final String nombre;
    private final char simbolo;
    private final boolean rojo;

    Palo(String nombre, char simbolo, boolean rojo) {
        this.nombre = nombre;
        this.simbolo = simbolo;
        this.rojo = rojo;
    }

    public String getNombre()  { return nombre; }
    public char getSimbolo()   { return simbolo; }
    public boolean esRojo()    { return rojo; }
    public boolean esNegro()   { return !rojo; }
}
