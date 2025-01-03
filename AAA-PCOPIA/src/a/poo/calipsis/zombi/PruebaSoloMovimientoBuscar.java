package a.poo.calipsis.zombi;

import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

public class PruebaSoloMovimientoBuscar {
    public static void main(String[] args) throws ParseException, IOException, ClassNotFoundException {
    Juego juego = new Juego(); 
    Scanner scanner = new Scanner(System.in);
    System.out.println("¿Quieres cargar un juego guardado? (S/N)");
    String respuesta = scanner.nextLine().trim().toUpperCase();  // Leer la respuesta y convertirla a mayúsculas
    if (respuesta.equals("S")) {
        boolean archivoCargado = juego.cargarEstadoConNombreCarga();
        if (!archivoCargado) {
            System.out.println("No se encontró un archivo guardado. Continuando sin cargar.");
        }
    } else {
        System.out.println("No se cargará ningún archivo guardado.");
    }
    juego.iniciarMenuPrincipal();
}

}
