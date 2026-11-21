import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/** Guarda y lee victorias/derrotas en partidas.txt (dos líneas: ganadas y perdidas). */
public class Estadisticas {
    private static final String ARCHIVO = "partidas.txt";
    private int ganadas;
    private int perdidas;
    private boolean existia;

    public Estadisticas() { cargar(); }

    private void cargar() {
        File f = new File(ARCHIVO);
        if (!f.exists() || f.length() == 0) return;
        try (Scanner sc = new Scanner(f)) {
            ganadas = Integer.parseInt(sc.nextLine().trim());
            perdidas = Integer.parseInt(sc.nextLine().trim());
            existia = true;
        } catch (FileNotFoundException | RuntimeException e) {
            ganadas = 0;
            perdidas = 0;
            existia = false;
        }
    }

    private void guardar() {
        try (PrintWriter pw = new PrintWriter(ARCHIVO)) {
            pw.println(ganadas);
            pw.println(perdidas);
            existia = true;
        } catch (FileNotFoundException e) {
            System.err.println("No se pudo escribir " + ARCHIVO + ": " + e.getMessage());
        }
    }

    public void registrarVictoria() { ganadas++; guardar(); }

    public void registrarDerrota() { perdidas++; guardar(); }

    public boolean hayHistorial() { return existia; }

    public int getGanadas() { return ganadas; }

    public int getPerdidas() { return perdidas; }
}
