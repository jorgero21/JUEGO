/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package a.poo.calipsis.zombi;

import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

/**
 *
 * @author Jorge
 */
public class PruebaSoloMovimientoBuscar {
    public static void main(String[] args) throws ParseException, IOException, ClassNotFoundException {
    Juego juego = new Juego();  // Crear una instancia del juego
    
    // Preguntar al usuario si desea cargar un juego guardado
    Scanner scanner = new Scanner(System.in);
    System.out.println("¿Quieres cargar un juego guardado? (S/N)");

    String respuesta = scanner.nextLine().trim().toUpperCase();  // Leer la respuesta y convertirla a mayúsculas

    if (respuesta.equals("S")) {
        // Intentar cargar el estado guardado automáticamente
        boolean archivoCargado = juego.cargarEstadoConNombreCarga();
        //juego.continuarJuego();
                   
        if (!archivoCargado) {
            // Si no se puede cargar, mostrar un mensaje y continuar con el flujo
            System.out.println("No se encontró un archivo guardado. Continuando sin cargar.");
        }
    } else {
        // Si el usuario no quiere cargar nada, mostrar mensaje
        System.out.println("No se cargará ningún archivo guardado.");
    }
    
    // Continúa con el flujo normal del juego (ya sea cargando o comenzando nuevo)
    juego.iniciarMenuPrincipal();
}

}
