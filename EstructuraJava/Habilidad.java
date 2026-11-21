public interface Habilidad {
    String getNombre();

    String getDescripcion();

    Momento getMomento();

    void aplicar(ContextoRonda ctx);
}
