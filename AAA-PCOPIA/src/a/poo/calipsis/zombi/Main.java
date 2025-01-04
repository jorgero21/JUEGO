package a.poo.calipsis.zombi;


import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Juego juego = new Juego(); 
            Scanner scanner = new Scanner(System.in);

            System.out.println("¿Quieres cargar un juego guardado? (S/N)");
            String respuesta = scanner.nextLine().trim().toUpperCase(); // Leer la respuesta y convertirla a mayúsculas

            if (respuesta.equals("S")) {
                boolean archivoCargado = juego.cargarEstadoConNombreCarga();
                if (!archivoCargado) {
                    System.out.println("No se encontró un archivo guardado. Continuando sin cargar.");
                }
            } else {
                System.out.println("No se cargará ningún archivo guardado.");
            }

            juego.iniciarMenuPrincipal();
        } catch (IOException e) {
            System.err.println("Ocurrió un error de entrada/salida: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("No se encontró la clase necesaria: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ocurrió un error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
