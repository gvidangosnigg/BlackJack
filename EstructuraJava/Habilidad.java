public interface Habilidad {
    String getNombre();

    Momento getMomento();

    void aplicar(ContextoRonda ctx);
}
