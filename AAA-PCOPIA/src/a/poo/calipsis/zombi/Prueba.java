/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package a.poo.calipsis.zombi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Jorge
 */
public class Prueba {

   
    public static void main(String[] args) throws ParseException, IOException, ClassNotFoundException {
        // TODO code application logic here
        
       
    /* Inventario inventario=new Inventario();
        inventario.agregarArma(new Arma("Pistola",1,2,2,3));
        inventario.agregarArma(new Arma("Escopeta",3,0,2,2));
        inventario.agregarArma(new Arma("Sniper",3,4,2,2));
        inventario.agregarArma(new Arma("Rifle",2,2,3,4));
        inventario.agregarArma(new Arma("Katana",4,0,1,1));
        inventario.agregarArma(new Arma("Cuchillo",1,0,1,4));
        inventario.agregarArma(new Arma("Bate de beisbol",2,0,4,2));
        inventario.agregarArma(new Arma("Martillo",3,0,2,3));
        inventario.guardarEnFichero("src/inventario.txt");
        inventario.cargarDesdeFichero("src/inventario.txt");
        inventario.listarArmas();
        
          LocalDate fechaCaducidad = LocalDate.parse("2025-11-04");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        inventario.agregarProvision(new Provisiones("Agua",10,LocalDate.parse("2025-11-04")));
        inventario.agregarProvision(new Provisiones("Cheetos",5,LocalDate.parse("2025-11-04")));
        inventario.agregarProvision(new Provisiones("Tortilla",20,LocalDate.parse("2025-11-04")));
        inventario.agregarProvision(new Provisiones("Ensalada",10,LocalDate.parse("2025-11-04")));
        inventario.guardarEnFichero("src/inventario.txt");
        inventario.cargarDesdeFichero("src/inventario.txt");
        inventario.listarProvisiones();
        
        
        */
/* Juego j= new Juego();
 j.iniciar();*/
        /*
        List<Superviviente> supervivientesSeleccionados = new ArrayList<>();
         Tablero tablero = new Tablero(supervivientesSeleccionados);
        tablero.agregarSuperviviente(new Superviviente("Zoey"));
        tablero.agregarSuperviviente(new Superviviente("Louis"));
        tablero.agregarSuperviviente(new Superviviente("Francis"));
        tablero.agregarSuperviviente(new Superviviente("Bill"));
        tablero.agregarSuperviviente(new Superviviente("Ellis"));
        tablero.agregarSuperviviente(new Superviviente("Nick"));
        tablero.agregarSuperviviente(new Superviviente("Coach"));
        tablero.agregarSuperviviente(new Superviviente("Rochelle"));
        
        tablero.guardarSupervivientesEnFichero("src/supervivientes.txt");
        
        tablero.cargarSupervivientesDesdeFichero("src/supervivientes.txt");
        tablero.mostrarSupervivientes();
        */
        
      /*
                Coordenada posicionInicial = new Coordenada(0, 0);
             Juego j= new Juego();
             // Crear un zombi con las características especificadas
             Zombi zombi1 = new Zombi(
                     1, // ID inicial
                     TipoZombi.CAMINANTE, // Tipo de zombi
                     true, // Es normal
                     false, // No es berserker
                     false, // No es tóxico
                     posicionInicial // Posición inicial
             );

             Superviviente zoey = new Superviviente("Zoey");
             Superviviente francis = new Superviviente("Francis");

             // Simular heridas y mordeduras
             zoey.recibirMordedura(zombi1);
             zoey.recibirHerida(zombi1);
             francis.recibirHerida(zombi1);


             // Guardar y cargar estadísticas
             j.guardarZombi(zombi1, "zombi1.dat");
              Zombi cargado= j.cargarZombi("zombi1.dat");

             // Mostrar estadísticas después de cargar
             System.out.println("Estadísticas después de cargar:");
           // Permitir la consulta por ID de zombi
            Scanner scanner = new Scanner(System.in);
            System.out.println("Introduce el ID del zombi para ver las estadísticas de sus víctimas:");
            int zombiId = scanner.nextInt();

            cargado.mostrarHeridosPorZombi(zombiId);*/
        /*Coordenada posicionInicial = new Coordenada(0, 0);
        Juego j = new Juego();
        
        // Crear varios zombis con las características especificadas
        Zombi zombi1 = new Zombi(
                1,
                TipoZombi.CAMINANTE, // Tipo de zombi
                true, // Es normal
                false, // No es berserker
                false, // No es tóxico
                posicionInicial // Posición inicial
        );

        Zombi zombi2 = new Zombi(
                2,
                TipoZombi.CORREDOR, // Otro tipo de zombi
                true, 
                true,  // Es berserker
                false, 
                posicionInicial
        );

        Zombi zombi3 = new Zombi(
                3,
                TipoZombi.ABOMINACION, // Zombi tóxico
                false, 
                false, 
                true,  // Es tóxico
                posicionInicial
        );

        Zombi zombi4 = new Zombi(
                4,
                TipoZombi.CAMINANTE, 
                true, 
                false, 
                false, 
                posicionInicial
        );
        
        // Crear varios supervivientes
        Superviviente zoey = new Superviviente("Zoey");
        Superviviente francis = new Superviviente("Francis");
        Superviviente bill = new Superviviente("Bill");
        Superviviente louis = new Superviviente("Louis");
        
        // Simular mordeduras y heridas
        zoey.recibirMordedura(zombi1);
        zoey.recibirHerida(zombi2);
        
        francis.recibirHerida(zombi1);
        francis.recibirMordedura(zombi3);
        
        bill.recibirHerida(zombi3);
        bill.recibirHerida(zombi4);
        
        louis.recibirMordedura(zombi2);
        louis.recibirHerida(zombi4);

        // Guardar y cargar estadísticas
        j.guardarZombi(zombi1, "zombi1.dat");
        j.guardarZombi(zombi2, "zombi2.dat");
        j.guardarZombi(zombi3, "zombi3.dat");
        j.guardarZombi(zombi4, "zombi4.dat");

        // Cargar los zombis y mostrar estadísticas
        Zombi cargado1 = j.cargarZombi("zombi1.dat");
        Zombi cargado2 = j.cargarZombi("zombi2.dat");
        Zombi cargado3 = j.cargarZombi("zombi3.dat");
        Zombi cargado4 = j.cargarZombi("zombi4.dat");

        // Permitir la consulta por ID de zombi
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce el ID del zombi para ver las estadísticas de sus víctimas:");
        int zombiId = scanner.nextInt();

        // Mostrar heridos por cada zombi cargado
        if (zombiId == cargado1.getId()) {
            cargado1.mostrarHeridosPorZombi(zombiId);
        } else if (zombiId == cargado2.getId()) {
            cargado2.mostrarHeridosPorZombi(zombiId);
        } else if (zombiId == cargado3.getId()) {
            cargado3.mostrarHeridosPorZombi(zombiId);
        } else if (zombiId == cargado4.getId()) {
            cargado4.mostrarHeridosPorZombi(zombiId);
        } else {
            System.out.println("Zombi con ID " + zombiId + " no encontrado.");
        }

        scanner.close();*/
         // Inicializar el tablero, almacén y otras configuraciones
         
         
     
         
      // Cargar el inventario del jugador y el almacén de ataques

      
    // Cargar el inventario del jugador y el almacén de ataques
  // Crear un superviviente y agregar zombis eliminados a la partida actual
   /*     Juego j = new Juego();
        Coordenada posicionInicial = new Coordenada(0, 0);
        Superviviente superviviente1 = new Superviviente("Superviviente1");
        Zombi z1 = new Zombi(
                1,
                TipoZombi.CORREDOR, // Tipo de zombi
                false, // Es normal
                false, // No es berserker
                true, // No es tóxico
                posicionInicial // Posición inicial
        );

        Zombi z2 = new Zombi(
                2,
                TipoZombi.ABOMINACION, // Otro tipo de zombi
                false,
                true,  // Es berserker
                false,
                posicionInicial
        );
        // Crear algunos zombis para probar

        // Agregar zombis a la lista de eliminados de la partida
        superviviente1.agregarZombiEliminado(z1);
        superviviente1.agregarZombiEliminado(z2);

        // Preguntar al usuario si quiere guardar el estado del juego
        Scanner scanner = new Scanner(System.in);
        System.out.println("¿Quieres guardar el estado del juego? (s/n)");
        String respuesta = scanner.nextLine().trim().toLowerCase();

        if (respuesta.equals("s")) {
            j.guardarEstadoConNombre();
        } else {
            System.out.println("El estado del juego no se guardó.");
        }

        // Cargar el estado del juego
        j.cargarEstadoConNombre();

        // Simulando la opción 2 en el menú
        System.out.println("Selecciona una opción:");
        System.out.println("1. Mostrar zombis eliminados históricamente");
        System.out.println("2. Mostrar zombis eliminados en esta partida");
        int tipo = scanner.nextInt();

        if (tipo == 2) {
            // Ordenar y mostrar zombis eliminados en esta partida
            List<Zombi> actual = superviviente1.getZombisEliminados();
            actual.sort(Comparator.comparing(Zombi::getId)); // Ordenar por ID
            System.out.println("Zombis eliminados en esta partida por " + superviviente1.getNombre() + ":");
            for (Zombi z : actual) {
                System.out.println(z);
            }

        } else {
            System.out.println("Opción inválida.");
        }
    }

    
*/
    
     
    }
}

   
     
    

