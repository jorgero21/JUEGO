package a.poo.calipsis.zombi;


import a.poo.calipsis.zombi.Coordenada;
import a.poo.calipsis.zombi.Superviviente;
import a.poo.calipsis.zombi.Tablero;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Juego implements Serializable{

    private static final int TAMANO_TABLERO = 10;
    private Tablero tablero;
    private List<Superviviente> supervivientes;
    private List<Zombi> zombis;
    private AlmacenAtaques ataque;
    private int turno; // Contador de turnos
    private static int contadorZombisPartida=0;  // Contador de zombis por partida
    private int id=1;
    private String archivo = "src/estado_partida.txt"; 
    private static List<Zombi> zombisCargados = new ArrayList<>();
    Superviviente s;
    Zombi zo;
    private List<Superviviente> supervivientesSeleccionados; 
    private List<Zombi> zombisEliminados = new ArrayList<>();
    private boolean enJuego;
    private List<Superviviente> almacenSupervivientes;
    private Set<Equipo> inventario;
    private Inventario almacenInventario;
    private boolean consultaHabilitada = true;
    public static final String DIRECTORIO_GUARDADO = "guardados/";
    private String rutaAlmacenAtaques; // Variable para almacenar la ruta del archivo de ataques
    private String nombrePartida; // Para almacenar el nombre del archivo actual

    public Juego() {
        this.supervivientes = new ArrayList<>(); // Inicializa la lista de supervivientes
        this.enJuego = true; // Inicia el juego como activo
         this.ataque = new AlmacenAtaques();
        this.almacenSupervivientes= new ArrayList<>();
          this.tablero = new Tablero(); 
           this.almacenInventario = new Inventario(); 
             this.zombis = new ArrayList<>();  // Inicializa la lista de zombis
           // cargarDatosYCrearTablero(); // Carga los supervivientes desde el archivo
    }

    public Tablero getTablero() {
        return this.tablero;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }
    
   public List<Superviviente> seleccionarSupervivientes(List<Superviviente> todosSupervivientes) {
    List<Superviviente> seleccionados = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);

    System.out.println("Selecciona hasta 4 supervivientes:");

    // Mostrar la lista de todos los supervivientes
    for (int i = 0; i < todosSupervivientes.size(); i++) {
        System.out.println((i + 1) + ". " + todosSupervivientes.get(i).getNombre());
    }

    // Permitir la selección de hasta 2 supervivientes
    while (seleccionados.size() < 2) {
        System.out.print("Introduce el numero del superviviente que deseas seleccionar (o 0 para terminar): ");

        // Verificar que la entrada es un número entero
        if (scanner.hasNextInt()) {
            int eleccion = scanner.nextInt() - 1;

            if (eleccion == -1) {
                break;  // Si el jugador ingresa 0, termina la selección.
            }

            if (eleccion >= 0 && eleccion < todosSupervivientes.size()) {
                Superviviente elegido = todosSupervivientes.get(eleccion);

                if (!seleccionados.contains(elegido)) {
                    seleccionados.add(elegido);
                    System.out.println(elegido.getNombre() + " ha sido seleccionado.");
                } else {
                    System.out.println("Este superviviente ya ha sido seleccionado.");
                }
            } else {
                System.out.println("Selección invalida. Inténtalo de nuevo.");
            }
        } else {
            System.out.println("Entrada no válida. Debes ingresar un número.");
            scanner.next();  // Limpiar el buffer de entrada para evitar un bucle infinito
        }
    }

    return seleccionados;
}


    public List<Superviviente> getSupervivientesSeleccionados() {
        return supervivientesSeleccionados;
    }
    
    
    // Método para obtener el número de supervivientes seleccionados
    public int obtenerNumeroDeSupervivientesSeleccionados(List<Superviviente> supervivientesSeleccionados) {
    // Retorna el tamaño de la lista, es decir, el número de supervivientes seleccionados
    return supervivientesSeleccionados.size();
}

    public List<Superviviente> seleccionarSupervivientesSimulacion(List<Superviviente> todosSupervivientes) {
        List<Superviviente> seleccionados = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Selecciona 1 superviviente:");

        // Mostrar la lista de todos los supervivientes
        for (int i = 0; i < todosSupervivientes.size(); i++) {
            System.out.println((i + 1) + ". " + todosSupervivientes.get(i).getNombre());
        }

        // Permitir la selección de un solo superviviente
        while (seleccionados.size() < 1) {
            System.out.print("Introduce el numero del superviviente que deseas seleccionar (o 0 para terminar): ");

            // Verificar que la entrada es un número entero
            if (scanner.hasNextInt()) {
                int eleccion = scanner.nextInt() - 1;

                if (eleccion == -1) {
                    break;  // Si el jugador ingresa 0, termina la selección.
                }

                if (eleccion >= 0 && eleccion < todosSupervivientes.size()) {
                    Superviviente elegido = todosSupervivientes.get(eleccion);

                    if (!seleccionados.contains(elegido)) {
                        seleccionados.add(elegido);
                        System.out.println(elegido.getNombre() + " ha sido seleccionado.");
                    } else {
                        System.out.println("Este superviviente ya ha sido seleccionado.");
                    }
                } else {
                    System.out.println("Selección invalida. Inténtalo de nuevo.");
                }
            } else {
                System.out.println("Entrada no válida. Debes ingresar un número.");
                scanner.next();  // Limpiar el buffer de entrada para evitar un bucle infinito
            }
        }

        return seleccionados;
    }

    public void iniciarMenuPrincipal() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("Menu Principal:");
            System.out.println("1. Iniciar Juego Completo");
            System.out.println("2. Cargar Juego Guardado");
            System.out.println("3. Simular Ataque de Superviviente a Zombi");
            System.out.println("4. Simular Activacion de Zombi");
            System.out.println("5. Simular Busqueda de Equipo");
            System.out.println("6. Mostrar Estadísticas");
            System.out.println("7. Salir");
            System.out.print("Elige una opcion: ");

            if (scanner.hasNextInt()) {
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer

                switch (opcion) {
                    case 1:
                        iniciar(); // Inicia el juego completo
                        break;
                    case 2:
                        if (cargarEstadoConNombreCarga()) {
                           //continuarJuego();
                            System.out.println("Se pudo cargar el juego.");
                        } else {
                            System.out.println("No se pudo cargar el juego.");
                        }
                        break;
                    case 3:
                        simularAtaqueSupervivienteAZombi();
                        break;
                    case 4:
                        simularActivacionDeZombi();
                        break;
                    case 5:
                        simularBusquedaDeEquipo();
                        break;
                    case 6:
                        mostrarEstadisticas(scanner);  // Llamada al método de estadísticas
                        break;
                    case 7:
                        salir = true;
                        System.out.println("Saliendo del juego. Hasta luego!");
                        break;
                    default:
                        System.out.println("Opcion invalida. Intente de nuevo.");
                }
            } else {
                System.out.println("Entrada no valida. Por favor, elige un número del 1 al 7.");
                scanner.nextLine(); // Limpiar el buffer en caso de entrada no válida
            }
        }
    }
      
    private void realizarAccionesSupervivientes(Superviviente s, String rutaAlmacenAtaques) throws IOException {
        Scanner scanner = new Scanner(System.in);
        // Variable para controlar si ya se terminó de consultar la información
        // Variable para controlar si ya se terminó de consultar la información

        // Recorrer los supervivientes seleccionados
        for (int i = 0; i < supervivientesSeleccionados.size(); i++) {
            Superviviente superviviente = supervivientesSeleccionados.get(i);

            // Consultar información antes de la acción de cada superviviente
          if (consultaHabilitada) {
               mostrarEstadoJuego();
            consultarInformacion(superviviente);
        }

            while (superviviente.getAcciones() > 0 && superviviente.isVivo() == true) {
                 comprobarFinDeJuego(supervivientesSeleccionados); 
                System.out.println(superviviente.getNombre() + ", elige tu accion (moverse/buscar/atacar/elegir arma/no hacer nada):");
                String accion = scanner.nextLine();

                switch (accion.toLowerCase()) {
                    case "moverse":

                        superviviente.moverse(zo,supervivientesSeleccionados,tablero);

                        break;

                   case "buscar":

                       superviviente.buscar(almacenInventario);

                        break;

            case "atacar":
               // Suponiendo que tienes las variables necesarias inicializadas
            superviviente.atacar(tablero, ataque,rutaAlmacenAtaques,superviviente);

            break;
               case "elegir arma":
                     superviviente.elegirArmaActiva();
                        break;
                    case "no hacer nada":
                           // Aquí podrías implementar la lógica para atacar a un zombi
                            tablero.mostrarTablero();
                           superviviente.restarAccion();
                         break;

                    default:
                        System.out.println("Accion no valida. Intenta de nuevo.");
                }
                System.out.println("Inventario de " + superviviente.getNombre() + ":");

                for (Equipo equipo : superviviente.getInventario()) {
                       System.out.println("- " + equipo.getNombre());
                    }
                    System.out.println(); // Añadir un salto de línea para mayor claridad
                    // Mostrar las armas activas del superviviente
                      System.out.println("Armas activas de " + superviviente.getNombre() + ":");
                    List<Arma> armasActivas = superviviente.getArmasActivas(); // Asegúrate de tener un getter para armas activas

                    if (armasActivas == null || armasActivas.isEmpty()) {
                        System.out.println("No hay armas activas.");
                    } else {
                        for (int j = 0; j < armasActivas.size(); j++) {
                            System.out.println((j + 1) + ". " + armasActivas.get(j).getNombre());
                        }
                    }
            }
         // Si hemos llegado al último superviviente, desactivamos la consulta para este turno
        if (i == supervivientesSeleccionados.size() - 1) {
            desactivarConsulta();  // Desactivamos la consulta de información al terminar el último superviviente
        }   

        }
    }
  
    private void realizarAccionesSupervivientesSimulacionAtaque(Superviviente s) throws IOException {
    Scanner scanner = new Scanner(System.in);

    // Recorrer los supervivientes seleccionados
    for (int i = 0; i < supervivientesSeleccionados.size(); i++) {
        Superviviente superviviente = supervivientesSeleccionados.get(i);

        // Consultar información antes de la acción de cada superviviente
        if (consultaHabilitada) {
            mostrarEstadoJuego();
            consultarInformacionSimulacion4(superviviente);
        }

        while (superviviente.getAcciones() > 0 && superviviente.isVivo() == true) {
            System.out.println(superviviente.getNombre() + ", elige tu accion (moverse/buscar/atacar/elegir arma/no hacer nada):");
            String accion = scanner.nextLine();

            switch (accion.toLowerCase()) {
                case "moverse":
                    superviviente.moverse(zo,supervivientesSeleccionados,tablero);
                    break;

                case "buscar":
                    superviviente.buscar(almacenInventario);
                    break;
                     case "atacar":
           // Suponiendo que tienes las variables necesarias inicializadas
        superviviente.atacarSimulacion(tablero, ataque,this);
                break;
                case "elegir arma":
                    superviviente.elegirArmaActiva();
                    break;

                case "no hacer nada":
                    // Aquí podrías implementar la lógica para atacar a un zombi
                    tablero.mostrarTablero();
                    superviviente.restarAccion();
                    break;

                default:
                    System.out.println("Acción no válida. Intenta de nuevo.");
            }

            // Mostrar inventario y armas activas
            System.out.println("Inventario de " + superviviente.getNombre() + ":");
            for (Equipo equipo : superviviente.getInventario()) {
                System.out.println("- " + equipo.getNombre());
            }
            System.out.println(); // Añadir un salto de línea para mayor claridad

            // Mostrar las armas activas del superviviente
            System.out.println("Armas activas de " + superviviente.getNombre() + ":");
            List<Arma> armasActivas = superviviente.getArmasActivas();
            if (armasActivas == null || armasActivas.isEmpty()) {
                System.out.println("No hay armas activas.");
            } else {
                for (int j = 0; j < armasActivas.size(); j++) {
                    System.out.println((j + 1) + ". " + armasActivas.get(j).getNombre());
                }
            }
        }

        // Si hemos llegado al último superviviente, desactivamos la consulta para este turno
        if (i == supervivientesSeleccionados.size() - 1) {
            desactivarConsulta();  // Desactivamos la consulta de información al terminar el último superviviente
        }
    }
}
  
    private void realizarAccionesSupervivientesSimulacionBusqueda(Superviviente s) {
        Scanner scanner = new Scanner(System.in);

        // Recorrer los supervivientes seleccionados
        for (int i = 0; i < supervivientesSeleccionados.size(); i++) {
            Superviviente superviviente = supervivientesSeleccionados.get(i);

            // Consultar información antes de la acción de cada superviviente
            if (consultaHabilitada) {
                mostrarEstadoJuego();
                consultarInformacionSimulacion4(superviviente);
            }

            while (superviviente.getAcciones() > 0 && superviviente.isVivo() == true) {
                
                System.out.println(superviviente.getNombre() + ", elige tu accion (moverse/buscar/elegir arma/no hacer nada):");
                String accion = scanner.nextLine();

                switch (accion.toLowerCase()) {
                    case "moverse":
                        //Zombi zombi, List<Superviviente> supervivientes, Tablero tablero
                        superviviente.moverse(zo,supervivientesSeleccionados,tablero);
                        break;

                    case "buscar":
                        superviviente.buscar(almacenInventario);
                        break;

                    case "elegir arma":
                        superviviente.elegirArmaActiva();
                        break;

                    case "no hacer nada":
                        // Aquí podrías implementar la lógica para atacar a un zombi
                        tablero.mostrarTablero();
                        superviviente.restarAccion();
                        break;

                    default:
                        System.out.println("Acción no válida. Intenta de nuevo.");
                }

                // Mostrar inventario y armas activas
                System.out.println("Inventario de " + superviviente.getNombre() + ":");
                for (Equipo equipo : superviviente.getInventario()) {
                    System.out.println("- " + equipo.getNombre());
                }
                System.out.println(); // Añadir un salto de línea para mayor claridad

                // Mostrar las armas activas del superviviente
                System.out.println("Armas activas de " + superviviente.getNombre() + ":");
                List<Arma> armasActivas = superviviente.getArmasActivas();
                if (armasActivas == null || armasActivas.isEmpty()) {
                    System.out.println("No hay armas activas.");
                } else {
                    for (int j = 0; j < armasActivas.size(); j++) {
                        System.out.println((j + 1) + ". " + armasActivas.get(j).getNombre());
                    }
                }
            }

            // Si hemos llegado al último superviviente, desactivamos la consulta para este turno
            if (i == supervivientesSeleccionados.size() - 1) {
                desactivarConsulta();  // Desactivamos la consulta de información al terminar el último superviviente
            }
        }
    }
    
    public void iniciar() throws IOException {
        if (this.supervivientesSeleccionados != null) {
             for (Superviviente superviviente : supervivientesSeleccionados) {
              superviviente.reiniciarEstado();
              superviviente.reiniciarInventario();
              superviviente.setTablero(null);
              this.tablero = new Tablero(); 
              activarConsulta();
              this.supervivientesSeleccionados = null;
               this.zombis.clear(); 
               this.turno = 0; 
               this.enJuego = true;
          }
        }
        try {
            // Cargar zombis de los archivos guardados
            for (int i = 1; i <= contadorZombisPartida; i++) {
                Zombi zombi = cargarZombi("src/zombi/zombi" + i + ".dat");
                zombisCargados.add(zombi);  // Agregar a la lista de zombis cargados
            }
               // Establecer los zombis en la partida

            System.out.println("Zombis cargados correctamente.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar los zombis: " + e.getMessage());
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce un nombre para la partida: ");
        String nombrePartida = scanner.nextLine().trim();

        if (nombrePartida.isEmpty()) {
            nombrePartida = "Default";
        }
        //String rutaAlmacenAtaques = "src/registroAtaques_" + nombrePartida + ".dat";
        String rutaAlmacenAtaques = DIRECTORIO_GUARDADO + nombrePartida + "_ataques.dat";


         try {
            ataque = cargarAlmacen(rutaAlmacenAtaques);
            System.out.println("Almacén cargado correctamente para la partida: " + nombrePartida);
        } catch (IOException | ClassNotFoundException e) {
            ataque = new AlmacenAtaques();
            System.out.println("No se encontró un almacén de ataques previo. Se creó uno nuevo.");
        }
         
        // Mostrar todos los supervivientes cargados en el almacenSupervivientes
        // Seleccionar los supervivientes que van a participar (máximo 2)
        cargarSupervivientes();
        cargarInventario();
        cargarContadorZombis();


        // Seleccionar los supervivientes que participarán en la partida
        List<Superviviente> supervivientesSeleccionados = seleccionarSupervivientes(almacenSupervivientes);
        if (supervivientesSeleccionados.isEmpty()) {
            System.out.println("No se seleccionaron supervivientes.");
            return;
        }

        // Inicializa el tablero con los supervivientes seleccionados
        this.tablero = new Tablero();  // Inicializa el tablero después de seleccionar los supervivientes

        if (supervivientesSeleccionados.isEmpty()) {
            System.out.println("No se han seleccionado supervivientes. El juego no puede continuar.");
            this.enJuego = false;  // Si no hay supervivientes, termina el juego
            return;
        } else {
            System.out.println("Se han seleccionado supervivientes.");
        }

        comprobarFinDeJuego(supervivientesSeleccionados);

        // Guarda la lista de supervivientes seleccionados en la variable de instancia
        this.supervivientesSeleccionados = supervivientesSeleccionados;


        // Asocia el tablero con cada superviviente
        for (Superviviente superviviente : supervivientesSeleccionados) {
            superviviente.reiniciarEstado();
            superviviente.setTablero(tablero); // Asocia el tablero a cada superviviente

        }



        // Verifica que el tablero no sea nulo antes de continuar
        if (this.tablero != null) {
            System.out.println("\nEl tablero se ha inicializado correctamente.");
        } else {
            System.out.println("Error al inicializar el tablero. El juego no puede continuar.");
            this.enJuego = false;
        }

        // Generar múltiples zombis según la cantidad de supervivientes
        int numZombis = 3;  // Puedes modificar la lógica para determinar cuántos zombis generar
        for (int i = 1; i <= numZombis; i++) {

            Zombi nuevoZombi = tablero.generarZombiConProbabilidades(contadorZombisPartida, tablero);
            if (nuevoZombi != null) {
                zombis.add(nuevoZombi);  // Agregar el nuevo zombi a la lista
                System.out.println("Zombi generado: " + nuevoZombi.getTipo()+ ", ID: "+contadorZombisPartida+ ","+nuevoZombi.getId());
                  contadorZombisPartida++;
            }
        }


        // Inicializar el contador de turnos
       this.turno = 1; // El juego empieza en el turno 1

        tablero.asignarSupervivientesAlTablero(supervivientesSeleccionados);

        // Bucle principal del juego
        while (enJuego) {
            System.out.println("Turno " + this.turno);



            System.out.println("FASE DE SUPERVIVIENTES: \n");
            // Procesar las acciones de todos los supervivientes
            for (Superviviente superviviente : supervivientesSeleccionados) {
                if (superviviente.isVivo()) { // Solo si el superviviente está vivo
                    realizarAccionesSupervivientes(superviviente,rutaAlmacenAtaques); // Permite al superviviente realizar sus acciones
                    comprobarFinDeJuego(supervivientesSeleccionados); // Verifica si se ha cumplido alguna condición de fin de juego
                    // Preguntar si el jugador quiere guardar el juego



                    if (!enJuego) { // Si el juego ha terminado, salimos del bucle
                        System.out.println("El juego ha finalizado. Regresando al menú principal...");
                        return;
                    }
                }
            }

                if (!enJuego) { // Si el juego ha terminado, salimos del bucle
                      System.out.println("El juego ha finalizado. Regresando al menú principal...");
                      return;
                  }

            // Después de que todos los supervivientes terminen sus acciones, procedemos con los zombis
            if (enJuego) {  // Verificar si el juego sigue en marcha antes de activar los zombis
                // Aquí puedes agregar la lógica para los zombis
                System.out.println("FASE DE ACTIVACIÓN DE ZOMBIS: \n");
                for (Zombi z : zombis) {
                      if (!z.isVivo()) {
                            continue; // Saltar zombis eliminados
                        }else{
                           z.moverse(z, supervivientesSeleccionados, tablero);
                      }
                        
                }
                System.out.println("\n");
                System.out.println("Estado del tablero:");
                tablero.mostrarTablero();
                System.out.println("------------------ \n");
            }



            // Mostrar el estado final después de que todos los zombis hayan actuado
            System.out.println("Fin del turno " + turno);

            // Evaluar si el juego ha terminado después de que los zombis hayan hecho su acción
            comprobarFinDeJuego(supervivientesSeleccionados);
             if (!enJuego) { // Si el juego ha terminado, salimos del bucle
                     for (Superviviente s : supervivientesSeleccionados) {
                         //s.guardarEnArchivo();
                         s.guardarHistorico();
                         s.guardarActual();
                    }
                        

                    for (Zombi zombi : zombis) {
                        // Guarda cada zombi con su nombre basado en su ID
                        String nombreArchivo = "src/zombi/zombi" + zombi.getId() + ".dat";  // Crea el nombre del archivo con el ID del zombi
                        try {
                            guardarZombi(zombi, nombreArchivo);  // Llama a la función de guardar
                            System.out.println("Zombi con ID " + zombi.getId() + " guardado en el archivo " + nombreArchivo);
                        } catch (IOException e) {
                            System.out.println("Error al guardar el zombi con ID " + zombi.getId() + ": " + e.getMessage());
                        }
                    }
                     guardarContadorZombis();
                     guardarEstadoConNombre(nombrePartida);
                        System.out.println("El juego ha finalizado. Regresando al menú principal...");
                        
                        return;
                    }

            // Si el juego no ha terminado, avanzamos al siguiente turno
           // Al final del turno, incrementa el contador global
            this.turno++;

            // Generar nuevos zombis según la cantidad de supervivientes
            activarConsulta();
            String respuesta = "";
                while (!respuesta.equals("s") && !respuesta.equals("n")) {
                    System.out.println("¿Deseas guardar el juego? (s/n): ");
                    respuesta = scanner.nextLine().trim().toLowerCase();
                    if (!respuesta.equals("s") && !respuesta.equals("n")) {
                        System.out.println("Entrada no válida. Por favor, escribe 's' para sí o 'n' para no.");
                    }
                }
                if (respuesta.equals("s")) {
                    guardarEstadoConNombre(nombrePartida); // Guardar el estado del juego
                    System.out.println("Juego guardado.");
                }

                // Preguntar si el jugador desea finalizar la partida
                respuesta = ""; // Reinicia la respuesta para la siguiente pregunta
                while (!respuesta.equals("s") && !respuesta.equals("n")) {
                    System.out.println("¿Deseas finalizar la partida? (s/n): ");
                    respuesta = scanner.nextLine().trim().toLowerCase();
                    if (!respuesta.equals("s") && !respuesta.equals("n")) {
                        System.out.println("Entrada no válida. Por favor, escribe 's' para sí o 'n' para no.");
                    }
                }
                if (respuesta.equals("s")) {

                    enJuego = false;
                   
                    System.out.println("Has decidido finalizar la partida voluntariamente.");
                }
                if (!enJuego) { // Si el juego ha terminado, salimos del bucle
                     for (Superviviente s : supervivientesSeleccionados) {
                         //s.guardarEnArchivo();
                         s.guardarHistorico();
                         s.guardarActual();
                    }
                        

                    for (Zombi zombi : zombis) {
                        // Guarda cada zombi con su nombre basado en su ID
                        String nombreArchivo = "src/zombi/zombi" + zombi.getId() + ".dat";  // Crea el nombre del archivo con el ID del zombi
                        try {
                            guardarZombi(zombi, nombreArchivo);  // Llama a la función de guardar
                            System.out.println("Zombi con ID " + zombi.getId() + " guardado en el archivo " + nombreArchivo);
                        } catch (IOException e) {
                            System.out.println("Error al guardar el zombi con ID " + zombi.getId() + ": " + e.getMessage());
                        }
                    }
                     guardarContadorZombis();
                     guardarEstadoConNombre(nombrePartida);
                        System.out.println("El juego ha finalizado. Regresando al menú principal...");
                        
                        return;
                    }
                 // Reiniciar el turno de todos los supervivientes
            for (Superviviente superviviente : supervivientesSeleccionados) {
                superviviente.reiniciarAcciones();
            }
            System.out.println("FASE DE APARICIoN: \n");
            for (int i = 1; i <= 1; i++) {
                Zombi nuevoZombi = tablero.generarZombiConProbabilidades(contadorZombisPartida, tablero);
                if (nuevoZombi != null) {

                    zombis.add(nuevoZombi);  // Agregar el nuevo zombi a la lista
                    System.out.println("Zombi generado: " + nuevoZombi.getTipo()+ ", ID: "+contadorZombisPartida+ ","+nuevoZombi.getId());
                    contadorZombisPartida++;
                }
            }
        }
    }
   
    public static AlmacenAtaques cargarAlmacen(String archivo) throws IOException, ClassNotFoundException {
        File file = new File(archivo);
        if (!file.exists()) {
            System.out.println("El archivo no existe: " + archivo);
            return new AlmacenAtaques();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            System.out.println("Cargando almacén desde: " + archivo);
            AlmacenAtaques almacen = (AlmacenAtaques) ois.readObject();
            System.out.println("Almacen cargado con éxito.");
            return almacen;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar el almacén: " + e.getMessage());
            throw e;
        }
    }
public String getNombrePartida() {
    return this.nombrePartida;
}

    /**
     *
     */
    public void continuarJuego( String nombrePartida ) throws IOException {
         Scanner scanner = new Scanner(System.in);
      
        enJuego=true;
          activarConsulta();
        //System.out.println("Introduce el nombre de la partida: ");
        //String nombrePartida = scanner.nextLine().trim();

        if (nombrePartida.isEmpty()) {
            nombrePartida = "Default";
        }
          System.out.println("Reanudando la partida desde el turno " + this.turno);
          
          String rutaAlmacenAtaques = DIRECTORIO_GUARDADO + nombrePartida + "_ataques.dat";
        // Solicitar el nombre del archivo si no está inicializado
        if (rutaAlmacenAtaques == null || rutaAlmacenAtaques.isEmpty()) {
            System.out.println("Por favor, introduce el nombre del archivo de estado para cargar: ");
            rutaAlmacenAtaques = scanner.nextLine().trim();
        }

       

         // Reiniciar el turno de los supervivientes
           try {
        // Cargar zombis de los archivos guardados
        for (int i = 1; i <= contadorZombisPartida; i++) {
            Zombi zombi = cargarZombi("src/zombi/zombi" + i + ".dat");
            zombisCargados.add(zombi);  // Agregar a la lista de zombis cargados
        }
           // Establecer los zombis en la partida

                System.out.println("Zombis cargados correctamente.");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error al cargar los zombis: " + e.getMessage());
            }
         // Cargar datos esenciales
        cargarSupervivientes(); // Si los supervivientes deben restaurarse desde un archivo
        cargarInventario(); // Si los supervivientes tienen inventarios personalizados
        cargarContadorZombis(); // Si el contador de zombis influye en el estado del juego

        // Validar que el tablero no sea nulo y que los supervivientes estén correctamente asignados
        if (tablero == null || supervivientesSeleccionados == null) {
            System.out.println("Error al cargar la partida: datos incompletos.");
            return;
        }
            for (Superviviente superviviente : supervivientesSeleccionados) {
                superviviente.reiniciarAcciones();

            }

        while (enJuego) {
            System.out.println("Turno " + this.turno);

            System.out.println("FASE DE SUPERVIVIENTES: \n");
            // Acciones de los supervivientes
            for (Superviviente superviviente : supervivientesSeleccionados) {
                if (superviviente.isVivo()) {
                    realizarAccionesSupervivientes(superviviente,rutaAlmacenAtaques);
                    comprobarFinDeJuego(supervivientesSeleccionados);
                     if (!enJuego) { // Si el juego ha terminado, salimos del bucle
                        System.out.println("El juego ha finalizado. Regresando al menú principal...");
                        return;
                    }
                }
            }



            // Después de que todos los supervivientes terminen sus acciones, procedemos con los zombis
            if (enJuego) {  // Verificar si el juego sigue en marcha antes de activar los zombis
                System.out.println("FASE DE ACTIVACIÓN DE ZOMBIS: \n");
                // Aquí puedes agregar la lógica para los zombis
                for (Zombi z : zombis) {
                      if (!z.isVivo()) {
                            continue; // Saltar zombis eliminados
                        }else{
                           z.moverse(z, supervivientesSeleccionados, tablero);
                      }
                        System.out.println("\n");
                        System.out.println("Estado del tablero:");
                        tablero.mostrarTablero();

                }
            }
            
               // Mostrar el estado final después de que todos los zombis hayan actuado
            System.out.println("Fin del turno " + turno);
             // Evaluar si el juego ha terminado después de que los zombis hayan hecho su acción
            comprobarFinDeJuego(supervivientesSeleccionados);
             if (!enJuego) { // Si el juego ha terminado, salimos del bucle
                     for (Superviviente s : supervivientesSeleccionados) {
                        // s.guardarEnArchivo();
                         s.guardarHistorico();
                         s.guardarActual();
                    }
                    for (Zombi zombi : zombis) {
                        // Guarda cada zombi con su nombre basado en su ID
                        String nombreArchivo = "src/zombi/zombi" + zombi.getId() + ".dat";  // Crea el nombre del archivo con el ID del zombi
                        try {
                            guardarZombi(zombi, nombreArchivo);  // Llama a la función de guardar
                            System.out.println("Zombi con ID " + zombi.getId() + " guardado en el archivo " + nombreArchivo);
                        } catch (IOException e) {
                            System.out.println("Error al guardar el zombi con ID " + zombi.getId() + ": " + e.getMessage());
                        }
                    }
                     guardarContadorZombis();
                      guardarEstadoConNombre(nombrePartida);
                        System.out.println("El juego ha finalizado. Regresando al menú principal...");
                        return;
                    }

            // Incrementar el turno global
            this.turno++;
              activarConsulta();
                String respuesta = "";
                while (!respuesta.equals("s") && !respuesta.equals("n")) {
                    System.out.println("¿Deseas guardar el juego? (s/n): ");
                    respuesta = scanner.nextLine().trim().toLowerCase();
                    if (!respuesta.equals("s") && !respuesta.equals("n")) {
                        System.out.println("Entrada no válida. Por favor, escribe 's' para sí o 'n' para no.");
                    }
                }
                if (respuesta.equals("s")) {
                    guardarEstadoConNombre(nombrePartida); // Guardar el estado del juego
                    System.out.println("Juego guardado.");
                }

                // Preguntar si el jugador desea finalizar la partida
                respuesta = ""; // Reinicia la respuesta para la siguiente pregunta
                while (!respuesta.equals("s") && !respuesta.equals("n")) {
                    System.out.println("¿Deseas finalizar la partida? (s/n): ");
                    respuesta = scanner.nextLine().trim().toLowerCase();
                    if (!respuesta.equals("s") && !respuesta.equals("n")) {
                        System.out.println("Entrada no válida. Por favor, escribe 's' para sí o 'n' para no.");
                    }
                }
                if (respuesta.equals("s")) {
                    enJuego = false;
                   

                    System.out.println("Has decidido finalizar la partida voluntariamente.");
                }
                if (!enJuego) { // Si el juego ha terminado, salimos del bucle
                     for (Superviviente s : supervivientesSeleccionados) {
                        // s.guardarEnArchivo();
                         s.guardarHistorico();
                         s.guardarActual();
                    }
                    for (Zombi zombi : zombis) {
                        // Guarda cada zombi con su nombre basado en su ID
                        String nombreArchivo = "src/zombi/zombi" + zombi.getId() + ".dat";  // Crea el nombre del archivo con el ID del zombi
                        try {
                            guardarZombi(zombi, nombreArchivo);  // Llama a la función de guardar
                            System.out.println("Zombi con ID " + zombi.getId() + " guardado en el archivo " + nombreArchivo);
                        } catch (IOException e) {
                            System.out.println("Error al guardar el zombi con ID " + zombi.getId() + ": " + e.getMessage());
                        }
                    }
                     guardarContadorZombis();
                      guardarEstadoConNombre(nombrePartida);
                        System.out.println("El juego ha finalizado. Regresando al menú principal...");
                        return;
                    }

            // Reiniciar el turno de los supervivientes
            for (Superviviente superviviente : supervivientesSeleccionados) {
                superviviente.reiniciarAcciones();
            }
            System.out.println("FASE DE APARICIÓN: \n");
            for (int i = 1; i <= supervivientesSeleccionados.size(); i++) {
                Zombi nuevoZombi = tablero.generarZombiConProbabilidades(contadorZombisPartida, tablero);
                if (nuevoZombi != null) {
                    contadorZombisPartida++;
                    zombis.add(nuevoZombi);  // Agregar el nuevo zombi a la lista
                    System.out.println("Zombi generado: " + nuevoZombi.getTipo()+ ", ID: "+nuevoZombi.getId());
                }
            }

         

        }

        System.out.println("El juego ha terminado. Regresando al menú principal...");
    }
    
   public void simularAtaqueSupervivienteAZombi() throws IOException{
    // Mostrar todos los supervivientes cargados en el almacenSupervivientes
    // Reiniciar el turno de todos los supervivientes
        // Incrementar turno
         if (this.supervivientesSeleccionados != null) {
             for (Superviviente superviviente : supervivientesSeleccionados) {
              superviviente.reiniciarEstado();
              superviviente.reiniciarInventario();
              superviviente.setTablero(null);
              this.tablero = new Tablero(); 
              activarConsulta();
              this.supervivientesSeleccionados = null;
               this.zombis.clear(); 
               this.turno = 0; 
               this.enJuego = true;
          }
        }
        activarConsulta();
    cargarSupervivientes();
    cargarInventario();

    // Seleccionar los supervivientes que participarán (máximo 2)
    List<Superviviente> supervivientesSeleccionados = seleccionarSupervivientesSimulacion(almacenSupervivientes);
    if (supervivientesSeleccionados.isEmpty()) {
        System.out.println("No se seleccionaron supervivientes.");
        return;
    }

    Scanner scanner = new Scanner(System.in);
    int tamaño;
    // Preguntar el tamaño del tablero
    while (true) {
    System.out.println("¿Qué tamaño deseas para el tablero? (Ejemplo: 10 para un tablero 10x10): ");
    
    if (scanner.hasNextInt()) {
        tamaño = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea pendiente después de nextInt()
        
        // Validar que el tamaño esté en el rango de 2 a 10
        if (tamaño > 1 && tamaño < 11) {
            break;  // Salir del bucle si el tamaño es válido
        } else {
            System.out.println("El tamaño debe estar entre 2 y 10. Intenta nuevamente.");
        }
    } else {
        System.out.println("Entrada no válida. Debes ingresar un número entero.");
        scanner.nextLine(); // Limpiar el buffer de entrada
    }
}
    // Inicializa el tablero con los supervivientes seleccionados
    this.tablero = new Tablero(tamaño);

    if (supervivientesSeleccionados.isEmpty()) {
        System.out.println("No se han seleccionado supervivientes. El juego no puede continuar.");
        this.enJuego = false;  // Si no hay supervivientes, termina el juego
        return;
    } else {
        System.out.println("Se han seleccionado supervivientes.");
    }

    // Crear un nuevo listado de supervivientes con sus coordenadas personalizadas
    List<Superviviente> supervivientesConCoordenadas = new ArrayList<>();
    for (Superviviente superviviente : supervivientesSeleccionados) {
        // Pedir al usuario las coordenadas para el superviviente
        System.out.println("Ingresa la fila y columna para " + superviviente.getNombre() + ":");

        int fila = -1, columna = -1;
        while (true) {
            // Validar fila
            System.out.print("Fila: ");
            if (scanner.hasNextInt()) {
                fila = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea pendiente después de nextInt()
                // Validar que la fila esté dentro del rango
                if (fila >= 0 && fila < tamaño) {
                    break;
                } else {
                    System.out.println("La fila es inválida. Debe estar dentro del rango del tablero.");
                }
            } else {
                System.out.println("Entrada no válida. Debes ingresar un número entero para la fila.");
                scanner.nextLine(); // Limpiar el buffer
            }
        }

        while (true) {
            // Validar columna
            System.out.print("Columna: ");
            if (scanner.hasNextInt()) {
                columna = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea pendiente después de nextInt()
                // Validar que la columna esté dentro del rango
                if (columna >= 0 && columna < tamaño) {
                    break;
                } else {
                    System.out.println("La columna es inválida. Debe estar dentro del rango del tablero.");
                }
            } else {
                System.out.println("Entrada no válida. Debes ingresar un número entero para la columna.");
                scanner.nextLine(); // Limpiar el buffer
            }
        }

        // Crear la coordenada y asignársela al superviviente
        Coordenada coordenada = new Coordenada(fila, columna);
        Superviviente supervivienteConCoordenada = new Superviviente(superviviente.getNombre(), coordenada);

        // Añadir el superviviente con la coordenada al nuevo listado
        supervivientesConCoordenadas.add(supervivienteConCoordenada);
    }
    

    if (supervivientesConCoordenadas.isEmpty()) {
        System.out.println("No se seleccionaron supervivientes.");
        return;
    }

    // Ahora sustituimos la lista de supervivientes seleccionados por los que tienen coordenadas personalizadas
    this.supervivientesSeleccionados = supervivientesConCoordenadas;

    // Asocia el tablero con cada superviviente
    for (Superviviente superviviente : supervivientesConCoordenadas) {
        superviviente.setTablero(tablero); // Asocia el tablero a cada superviviente
        superviviente.seleccionarEquipoManualmente(almacenInventario);
    }
 for (Superviviente superviviente : supervivientesConCoordenadas) {
            superviviente.reiniciarAcciones();
        }
    System.out.println("¿Cuántos zombis deseas agregar?");
int numZombisAgregar1;
while (true) {
    if (scanner.hasNextInt()) {
        numZombisAgregar1 = scanner.nextInt();
        scanner.nextLine();  // Consumir el salto de línea pendiente
        if (numZombisAgregar1 > 0) {  // Puedes añadir una validación para permitir solo números positivos
            break;  // Salir del bucle si el número es válido
        } else {
            System.out.println("El número de zombis no puede ser negativo. Intenta nuevamente.");
        }
    } else {
        System.out.println("Entrada no válida. Debes ingresar un número entero.");
        scanner.nextLine();  // Limpiar el buffer de entrada
    }
}
 // Generar y agregar los nuevos zombis
    for (int i = 0; i < numZombisAgregar1; i++) {
        Zombi nuevoZombi = tablero.generarZombiManual(tablero, id);
        if (nuevoZombi != null) {
            id++;
            zombis.add(nuevoZombi);  // Agregar el nuevo zombi a la lista
            System.out.println("Zombi generado: " + nuevoZombi.getTipo());
        }
    }

    // Inicializar el contador de turnos
    int turno = 1;
    tablero.asignarSupervivientesAlTablero(supervivientesConCoordenadas);
    //tablero.mostrarTableroSimulacion(tamaño);

    // Bucle principal del juego
    while (enJuego) {
        System.out.println("Turno " + turno);
        // Si los supervivientes están vivos, procesamos sus acciones
        for (Superviviente superviviente : supervivientesConCoordenadas) {
            if (superviviente.isVivo()) { // Solo si el superviviente está vivo
                realizarAccionesSupervivientesSimulacionAtaque(superviviente); // Permite al superviviente realizar sus acciones
                if (!superviviente.isVivo()) {
                    System.out.println(superviviente.getNombre() + " ha muerto.");
                    enJuego = false;
                }
                // Salir del bucle si se finaliza el juego
                if (!enJuego) { // Si el juego ha terminado, salimos del bucle
                    return;
                }
                // Preguntar si desea finalizar, continuar o agregar un zombi
                System.out.println("¿Deseas finalizar la simulación (s), continuar (n) o agregar un zombi (a)?");
                String respuesta = scanner.nextLine().trim().toLowerCase();
                if (respuesta.equals("n")) {
                    continue;
                }
                // Acción de finalizar simulación
                if (respuesta.equals("s")) {
                    enJuego = false;
                    System.out.println("Has decidido finalizar la simulación.");
                    break;
                }
                // Acción de agregar un zombi
                // Acción de agregar un zombi
                if (respuesta.equals("a")) {
                    System.out.println("¿Cuántos zombis deseas agregar?");
                    int numZombisAgregar;
                    while (true) {
                        if (scanner.hasNextInt()) {
                            numZombisAgregar = scanner.nextInt();
                            scanner.nextLine();  // Consumir el salto de línea pendiente
                            if (numZombisAgregar > 0) {  // Permitir solo valores positivos
                                break;  // Salir del bucle si el número es válido
                            } else {
                                System.out.println("El número de zombis no puede ser negativo. Intenta nuevamente.");
                            }
                        } else {
                            System.out.println("Entrada no válida. Debes ingresar un número entero.");
                            scanner.nextLine();  // Limpiar el buffer de entrada
                        }
                    }

                    // Generar y agregar los nuevos zombis
                    for (int i = 0; i < numZombisAgregar; i++) {
                        Zombi nuevoZombi = tablero.generarZombiManual(tablero, id);
                        if (nuevoZombi != null) {
                            id++;
                            zombis.add(nuevoZombi);  // Agregar el nuevo zombi a la lista
                            System.out.println("Zombi generado: " + nuevoZombi.getTipo());
                        }
                    }
                }


                // Salir del bucle si se finaliza el juego
                if (!enJuego) { // Si el juego ha terminado, salimos del bucle
                    return;
                }
            } else {
                System.out.println("Superviviente fallecido");
                enJuego = false;
                // Salir del bucle si se finaliza el juego
                if (!enJuego) { // Si el juego ha terminado, salimos del bucle
                    return;
                }
            }

            // Salir del bucle si se finaliza el juego
            if (!enJuego) { // Si el juego ha terminado, salimos del bucle
                return;
            }
        }

        // Si no hay supervivientes vivos o ya no hay zombis que atacar, terminar la simulación
        if (supervivientesSeleccionados.isEmpty() || zombis.isEmpty()) {
            enJuego = false;
            System.out.println("No hay más supervivientes o zombis. La simulación ha terminado.");
        }
        // Reiniciar el turno de todos los supervivientes
        for (Superviviente superviviente : supervivientesConCoordenadas) {
            superviviente.reiniciarAcciones();
        }
        // Incrementar turno
        turno++;
        activarConsulta();
    }
}

    public void simularBusquedaDeEquipo() {
        
         if (this.supervivientesSeleccionados != null) {
             for (Superviviente superviviente : supervivientesSeleccionados) {
              superviviente.reiniciarEstado();
              superviviente.reiniciarInventario();
              superviviente.setTablero(null);
              this.tablero = new Tablero(); 
              activarConsulta();
              this.supervivientesSeleccionados = null;
               this.zombis.clear(); 
               this.turno = 0; 
               this.enJuego = true;
          }
        }
        // Mostrar todos los supervivientes cargados en el almacenSupervivientes
        cargarSupervivientes();
        cargarInventario();

        // Seleccionar los supervivientes que participarán (máximo 2)
        List<Superviviente> supervivientesSeleccionados = seleccionarSupervivientesSimulacion(almacenSupervivientes);
        if (supervivientesSeleccionados.isEmpty()) {
            System.out.println("No se seleccionaron supervivientes.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
            int tamaño;
                // Preguntar el tamaño del tablero
                while (true) {
                System.out.println("¿Qué tamaño deseas para el tablero? (Ejemplo: 10 para un tablero 10x10): ");

                if (scanner.hasNextInt()) {
                    tamaño = scanner.nextInt();
                    scanner.nextLine(); // Consumir el salto de línea pendiente después de nextInt()

                    // Validar que el tamaño esté en el rango de 2 a 10
                    if (tamaño > 1 && tamaño < 11) {
                        break;  // Salir del bucle si el tamaño es válido
                    } else {
                        System.out.println("El tamaño debe estar entre 2 y 10. Intenta nuevamente.");
                    }
                } else {
                    System.out.println("Entrada no válida. Debes ingresar un número entero.");
                    scanner.nextLine(); // Limpiar el buffer de entrada
                }
            }
        // Inicializa el tablero con los supervivientes seleccionados
        this.tablero = new Tablero(tamaño);

        if (supervivientesSeleccionados.isEmpty()) {
            System.out.println("No se han seleccionado supervivientes. El juego no puede continuar.");
            this.enJuego = false;  // Si no hay supervivientes, termina el juego
            return;
        } else {
            System.out.println("Se han seleccionado supervivientes.");
        }
      // Crear un nuevo listado de supervivientes con sus coordenadas personalizadas
        List<Superviviente> supervivientesConCoordenadas = new ArrayList<>();
        for (Superviviente superviviente : supervivientesSeleccionados) {
            // Pedir al usuario las coordenadas para el superviviente
            System.out.println("Ingresa la fila y columna para " + superviviente.getNombre() + ":");

        int fila = -1, columna = -1;
        while (true) {
            // Validar fila
            System.out.print("Fila: ");
            if (scanner.hasNextInt()) {
                fila = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea pendiente después de nextInt()
                // Validar que la fila esté dentro del rango
                if (fila >= 0 && fila < tamaño) {
                    break;
                } else {
                    System.out.println("La fila es inválida. Debe estar dentro del rango del tablero.");
                }
            } else {
                System.out.println("Entrada no válida. Debes ingresar un número entero para la fila.");
                scanner.nextLine(); // Limpiar el buffer
            }
        }

        while (true) {
            // Validar columna
            System.out.print("Columna: ");
            if (scanner.hasNextInt()) {
                columna = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea pendiente después de nextInt()
                // Validar que la columna esté dentro del rango
                if (columna >= 0 && columna < tamaño) {
                    break;
                } else {
                    System.out.println("La columna es inválida. Debe estar dentro del rango del tablero.");
                }
            } else {
                System.out.println("Entrada no válida. Debes ingresar un número entero para la columna.");
                scanner.nextLine(); // Limpiar el buffer
            }
        }

            // Crear la coordenada y asignársela al superviviente
            Coordenada coordenada = new Coordenada(fila, columna);
            Superviviente supervivienteConCoordenada = new Superviviente(superviviente.getNombre(), coordenada);

            // Añadir el superviviente con la coordenada al nuevo listado
            supervivientesConCoordenadas.add(supervivienteConCoordenada);


        }
      if (supervivientesConCoordenadas.isEmpty()) {
                System.out.println("No se seleccionaron supervivientes.");
                return;
            }   

        // Ahora sustituimos la lista de supervivientes seleccionados por los que tienen coordenadas personalizadas
        this.supervivientesSeleccionados = supervivientesConCoordenadas;

        // Asocia el tablero con cada superviviente
        for (Superviviente superviviente : supervivientesConCoordenadas) {
            superviviente.setTablero(tablero); // Asocia el tablero a cada superviviente
        }

        // Verifica que el tablero no sea nulo antes de continuar
        if (this.tablero != null) {
            System.out.println("\nEl tablero se ha inicializado correctamente.");
        } else {
            System.out.println("Error al inicializar el tablero. El juego no puede continuar.");
            this.enJuego = false;
            return;
        }

        // Inicializar el contador de turnos
        int turno = 1;
        tablero.asignarSupervivientesAlTablero(supervivientesConCoordenadas);

        // Bucle principal del juego
        while (enJuego) {
            System.out.println("Turno " + turno);

            // Procesar las acciones de todos los supervivientes
            for (Superviviente superviviente : supervivientesConCoordenadas) {
                if (superviviente.isVivo()) { // Solo si el superviviente está vivo
                    realizarAccionesSupervivientesSimulacionBusqueda(superviviente); // Permite al superviviente realizar sus acciones
                }
            }

            // Preguntar si el jugador desea finalizar la simulación después de procesar las acciones
            System.out.println("¿Deseas finalizar la simulación? (s/n): ");
            String respuesta = scanner.nextLine().trim().toLowerCase();

            // Validar la respuesta del jugador
            while (!respuesta.equals("s") && !respuesta.equals("n")) {
                System.out.println("Opción no válida. Ingresa 's' para finalizar o 'n' para continuar.");
                respuesta = scanner.nextLine().trim().toLowerCase();
            }

            if (respuesta.equals("s")) {
                enJuego = false;
                System.out.println("Has decidido finalizar la simulación.");
            }


            // Si el juego ha terminado, salimos del bucle
            if (!enJuego) {
                return;
            }
             // Reiniciar el turno de todos los supervivientes
            for (Superviviente superviviente : supervivientesConCoordenadas) {
                superviviente.reiniciarAcciones();
            }
 // Mostrar el estado final después de que todos los zombis hayan actuado
            System.out.println("Fin del turno " + turno);

            // Incrementar turno
            turno++;
            activarConsulta();
        }
    }
    
    private void simularActivacionDeZombi() {
        // Mostrar todos los supervivientes cargados en el almacenSupervivientes
        
         if (this.supervivientesSeleccionados != null) {
             for (Superviviente superviviente : supervivientesSeleccionados) {
              superviviente.reiniciarEstado();
              superviviente.reiniciarInventario();
              superviviente.setTablero(null);
              this.tablero = new Tablero(); 
              activarConsulta();
              this.supervivientesSeleccionados = null;
               this.zombis.clear(); 
               this.turno = 0; 
               this.enJuego = true;
          }
        }
        cargarSupervivientes();
        cargarInventario();

        // Seleccionar los supervivientes que participarán (máximo 2)
        List<Superviviente> supervivientesSeleccionados = seleccionarSupervivientesSimulacion(almacenSupervivientes);
        if (supervivientesSeleccionados.isEmpty()) {
            System.out.println("No se seleccionaron supervivientes.");
            return;
        }

            Scanner scanner = new Scanner(System.in);
            int tamaño;
                // Preguntar el tamaño del tablero
                while (true) {
                System.out.println("Que tamanio deseas para el tablero? (Ejemplo: 10 para un tablero 10x10): ");

                if (scanner.hasNextInt()) {
                    tamaño = scanner.nextInt();
                    scanner.nextLine(); // Consumir el salto de línea pendiente después de nextInt()

                    // Validar que el tamaño esté en el rango de 2 a 10
                    if (tamaño > 1 && tamaño < 11) {
                        break;  // Salir del bucle si el tamaño es válido
                    } else {
                        System.out.println("El tamanio debe estar entre 2 y 10. Intenta nuevamente.");
                    }
                } else {
                    System.out.println("Entrada no valida. Debes ingresar un número entero.");
                    scanner.nextLine(); // Limpiar el buffer de entrada
                }
            }
        // Inicializa el tablero con los supervivientes seleccionados
        this.tablero = new Tablero(tamaño);

        if (supervivientesSeleccionados.isEmpty()) {
            System.out.println("No se han seleccionado supervivientes. El juego no puede continuar.");
            this.enJuego = false;  // Si no hay supervivientes, termina el juego
            return;
        } else {
            System.out.println("Se han seleccionado supervivientes.");
        }

        // Crear un nuevo listado de supervivientes con sus coordenadas personalizadas
        List<Superviviente> supervivientesConCoordenadas = new ArrayList<>();
        for (Superviviente superviviente : supervivientesSeleccionados) {
            // Pedir al usuario las coordenadas para el superviviente
            System.out.println("Ingresa la fila y columna para " + superviviente.getNombre() + ":");

          
        int fila = -1, columna = -1;
        while (true) {
            // Validar fila
            System.out.print("Fila: ");
            if (scanner.hasNextInt()) {
                fila = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea pendiente después de nextInt()
                // Validar que la fila esté dentro del rango
                if (fila >= 0 && fila < tamaño) {
                    break;
                } else {
                    System.out.println("La fila es invalida. Debe estar dentro del rango del tablero.");
                }
            } else {
                System.out.println("Entrada no válida. Debes ingresar un número entero para la fila.");
                scanner.nextLine(); // Limpiar el buffer
            }
        }

        while (true) {
            // Validar columna
            System.out.print("Columna: ");
            if (scanner.hasNextInt()) {
                columna = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea pendiente después de nextInt()
                // Validar que la columna esté dentro del rango
                if (columna >= 0 && columna < tamaño) {
                    break;
                } else {
                    System.out.println("La columna es inválida. Debe estar dentro del rango del tablero.");
                }
            } else {
                System.out.println("Entrada no válida. Debes ingresar un número entero para la columna.");
                scanner.nextLine(); // Limpiar el buffer
            }
        }
            // Crear la coordenada y asignársela al superviviente
            Coordenada coordenada = new Coordenada(fila, columna);
            Superviviente supervivienteConCoordenada = new Superviviente(superviviente.getNombre(), coordenada);
            // Añadir el superviviente con la coordenada al nuevo listado
            supervivientesConCoordenadas.add(supervivienteConCoordenada);
        }

        if (supervivientesConCoordenadas.isEmpty()) {
            System.out.println("No se seleccionaron supervivientes.");
            return;
        }

        // Ahora sustituimos la lista de supervivientes seleccionados por los que tienen coordenadas personalizadas
        this.supervivientesSeleccionados = supervivientesConCoordenadas;

        // Asocia el tablero con cada superviviente
        for (Superviviente superviviente : supervivientesConCoordenadas) {
            superviviente.setTablero(tablero); // Asocia el tablero a cada superviviente
        }

        // Generar múltiples zombis según la cantidad de supervivientes
        int numZombis = 1;  // Puedes modificar la lógica para determinar cuántos zombis generar
        for (int i = 0; i < numZombis; i++) {
            Zombi nuevoZombi = tablero.generarZombiManual(tablero,id);
            if (nuevoZombi != null) {
                id++;
                zombis.add(nuevoZombi);  // Agregar el nuevo zombi a la lista
                System.out.println("Zombi generado: " + nuevoZombi.getTipo());
            }
        }
        

        // Inicializar el contador de turnos
        int turno = 1;
        tablero.asignarSupervivientesAlTablero(supervivientesConCoordenadas);
        //tablero.mostrarTableroSimulacion(tamaño);
        // Bucle principal del juego
        while (enJuego) {
            System.out.println("Turno " + turno);
   tablero.mostrarTableroSimulacion(tamaño); 
            // Consultar información antes de la acción de cada superviviente

            System.out.println("\n¿Deseas consultar información del zombi?");
            System.out.println("1. Consultar zombi");
            System.out.println("2. Continuar con las acciones");
            System.out.print("Elige una opción: ");

            if (scanner.hasNextInt()) { // Verifica que el usuario introduzca un número
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                switch (opcion) {

                    case 1: // Consultar zombis
                        System.out.println("Zombis en el tablero:");
                        for (int i = 0; i < zombis.size(); i++) {
                             Zombi zombi = zombis.get(i);

                            System.out.println((i + 1) + ". Tipo: " + zombi.getTipo()+
                                               ", Aguante: " + zombi.getAguante() +
                                               ", Activación: " + (zombi.getActivaciones()));
                        }
                        System.out.print("Elige el número del zombi para ver detalles (o 0 para volver): ");

                        if (scanner.hasNextInt()) { // Verifica que la selección sea un número
                            int indiceZombi = scanner.nextInt() - 1;
                            scanner.nextLine(); // Consumir el salto de línea
                            if (indiceZombi >= 0 && indiceZombi < zombis.size()) {
                                Zombi zombiSeleccionado = zombis.get(indiceZombi);

                                // Determinar el subtipo del zombi seleccionado
                                String tipoZombi = null;
                                if (zombiSeleccionado.isNormal()) {
                                    tipoZombi = "Normal";
                                } else if (zombiSeleccionado.isBerserker()) {
                                    tipoZombi = "Berserker";
                                } else if (zombiSeleccionado.isToxico()) {
                                    tipoZombi = "Toxico";
                                }

                                System.out.println("Detalles del zombi:");
                                System.out.println("Tipo: " + zombiSeleccionado.getTipo());
                                 System.out.println("Subtipo: " + tipoZombi);
                                System.out.println("Aguante: " + zombiSeleccionado.getAguante());
                                System.out.println("Activación: " + (zombiSeleccionado.getActivaciones()));
                            } else if (indiceZombi == -1) {
                                System.out.println("Volviendo al menú principal...");
                            } else {
                                System.out.println("Número inválido. Por favor, selecciona un zombi de la lista.");
                            }
                        } else {
                            System.out.println("Entrada inválida. Debes ingresar un número.");
                            scanner.nextLine(); // Limpiar el buffer en caso de error
                        }
                        break;

                    case 2: // Continuar con las acciones
                           for (Zombi z : zombis) {
                z.actualizarListaSupervivientes(supervivientesSeleccionados);
                z.acercarseAlSupervivienteSimulacion(z, supervivientesConCoordenadas, tablero);
                //tablero.mostrarTableroSimulacion(tamaño); 
            }
              System.out.println("\n ");
          

            // Preguntar si el jugador desea finalizar la simulación o agregar más supervivientes
            System.out.println("¿Deseas finalizar la simulación (s), agregar un nuevo superviviente (a) o continuar (n)?");
            String respuesta = scanner.nextLine().trim().toLowerCase();

            // Validar la respuesta del jugador
            while (!respuesta.equals("s") && !respuesta.equals("a") && !respuesta.equals("n")) {
                System.out.println("Opción no válida. Ingresa 's' para finalizar, 'a' para agregar un superviviente o 'n' para continuar.");
                respuesta = scanner.nextLine().trim().toLowerCase();
            }

            if (respuesta.equals("s")) {
                enJuego = false;
                System.out.println("Has decidido finalizar la simulación.");
            } else if (respuesta.equals("a")) {
                // Lógica para agregar un nuevo superviviente
                System.out.println("Agregar nuevo superviviente:");
                List<Superviviente> supervivientesSeleccionados2 = seleccionarSupervivientesSimulacion(almacenSupervivientes);

                if (supervivientesSeleccionados2.isEmpty()) {
                    System.out.println("No se seleccionaron supervivientes.");
                    return;
                }

                // Crear un nuevo listado de supervivientes con sus coordenadas personalizadas
                List<Superviviente> supervivientesConCoordenadas2 = new ArrayList<>();
                for (Superviviente superviviente : supervivientesSeleccionados2) {
                    // Pedir al usuario las coordenadas para el superviviente
                    System.out.println("Ingresa la fila y columna para " + superviviente.getNombre() + ":");

                    
        int fila = -1, columna = -1;
        while (true) {
            // Validar fila
            System.out.print("Fila: ");
            if (scanner.hasNextInt()) {
                fila = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea pendiente después de nextInt()
                // Validar que la fila esté dentro del rango
                if (fila >= 0 && fila < tamaño) {
                    break;
                } else {
                    System.out.println("La fila es inválida. Debe estar dentro del rango del tablero.");
                }
            } else {
                System.out.println("Entrada no válida. Debes ingresar un número entero para la fila.");
                scanner.nextLine(); // Limpiar el buffer
            }
        }

        while (true) {
            // Validar columna
            System.out.print("Columna: ");
            if (scanner.hasNextInt()) {
                columna = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea pendiente después de nextInt()
                // Validar que la columna esté dentro del rango
                if (columna >= 0 && columna < tamaño) {
                    break;
                } else {
                    System.out.println("La columna es inválida. Debe estar dentro del rango del tablero.");
                }
            } else {
                System.out.println("Entrada no válida. Debes ingresar un número entero para la columna.");
                scanner.nextLine(); // Limpiar el buffer
            }
        }

                    // Crear la coordenada y asignársela al superviviente
                    Coordenada coordenada = new Coordenada(fila, columna);
                    Superviviente supervivienteConCoordenada = new Superviviente(superviviente.getNombre(), coordenada);

                    // Añadir el superviviente con la coordenada al nuevo listado
                    supervivientesConCoordenadas2.add(supervivienteConCoordenada);
                    // Asocia el tablero con cada superviviente
                    supervivienteConCoordenada.setTablero(tablero); // Asocia el tablero a cada superviviente
                }

                // Ahora sustituimos la lista de supervivientes seleccionados por los que tienen coordenadas personalizadas
                this.supervivientesSeleccionados.addAll(supervivientesConCoordenadas2);
                System.out.println("Nuevos supervivientes al juego.");

                // Mostrar el tablero después de agregar el nuevo superviviente
                 tablero.asignarSupervivientesAlTablero(supervivientesConCoordenadas2);
               // tablero.mostrarTableroSimulacion(tamaño); // Mostrar el tablero actualizado
            } else if (respuesta.equals("n")) {
    // Continuar con el próximo turno sin hacer nada adicional.
    System.out.println("Continuando con el siguiente turno...");
    // Solo aumentar el turno y continuar el bucle sin interrumpir el juego.
    turno++;
}

            
            
        
                        break;

                    default: // Opción inválida
                        System.out.println("Opción inválida. Por favor, elige una opción válida.");
                }
            } else {
                System.out.println("Entrada inválida. Por favor, elige un número entre 1 y 3.");
                scanner.nextLine(); // Limpiar el buffer en caso de entrada no válida
            }
         // Aquí se permite la consulta de información de zombis
         
    }
    }

    private static void mostrarEstadisticas(Scanner scanner) throws ClassNotFoundException, IOException {
        System.out.println("Elige qué deseas visualizar:");
        System.out.println("1. Zombis eliminados");
        System.out.println("2. Supervivientes heridos");
        System.out.println("3. Almacén de ataques");  // Nueva opción para Almacén de Ataques
        System.out.print("Elige una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        if (opcion == 1) {
            // Mostrar estadísticas de zombis eliminados
             try {
        System.out.println("Elige el tipo de estadísticas de zombis eliminados:");
        System.out.println("1. Zombis eliminados históricamente");
        System.out.println("2. Zombis eliminados en esta partida");
        System.out.print("Elige una opción: ");
        int tipo = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        if (tipo == 1) {
            // Mostrar estadísticas históricas
            System.out.print("Introduce el nombre del superviviente: ");
            String nombre = scanner.nextLine();
            Superviviente superviviente2 = Superviviente.cargarHistorico(nombre);

            // Obtener la lista histórica
            List<Zombi> historico = superviviente2.getHistorico();

            if (historico.isEmpty()) {
                System.out.println("No hay zombis eliminados históricamente por " + nombre + ".");
                return;
            }

            // Preguntar si desea ordenar
            System.out.print("¿Deseas ver la lista ordenada por el ID del zombi? (S/N): ");
            String ordenar = scanner.nextLine().trim().toUpperCase();

            if (ordenar.equals("S")) {
                historico.sort(Comparator.comparing(Zombi::getId)); // Ordenar por ID
                System.out.println("Zombis eliminados históricamente por " + nombre + " (ordenados por ID):");
            } else {
                System.out.println("Zombis eliminados históricamente por " + nombre + ":");
            }

            // Mostrar la lista
            for (Zombi z : historico) {
                System.out.println(z);
            }

        } else if (tipo == 2) {
            // Mostrar estadísticas de esta partida
            Juego miJuego = new Juego();

            // Cargar el estado del juego
            if (miJuego.cargarEstadoConNombreCarga()) {
                System.out.println("Introduce el nombre del superviviente para ver sus estadísticas:");
               // Limpiar el buffer antes de usar nextLine()
                String nombreSuperviviente = scanner.nextLine().trim();
                // Buscar el superviviente por su nombre
                Superviviente superviviente = miJuego.obtenerSupervivientePorNombre(nombreSuperviviente);

                if (superviviente == null) {
                    System.out.println("No se encontró un superviviente con ese nombre.");
                    return;
                }

                // Obtener la lista actual
                List<Zombi> actual = superviviente.getZombisEliminados();

                if (actual.isEmpty()) {
                    System.out.println("No hay zombis eliminados por " + nombreSuperviviente + " en esta partida.");
                    return;
                }

                // Preguntar si desea ordenar
                System.out.print("¿Deseas ver la lista ordenada por el ID del zombi? (S/N): ");
                String ordenar = scanner.nextLine().trim().toUpperCase();

                if (ordenar.equals("S")) {
                    actual.sort(Comparator.comparing(Zombi::getId)); // Ordenar por ID
                    System.out.println("Zombis eliminados en esta partida por " + nombreSuperviviente + " (ordenados por ID):");
                } else {
                    System.out.println("Zombis eliminados en esta partida por " + nombreSuperviviente + ":");
                }

                // Mostrar la lista
                for (Zombi z : actual) {
                    System.out.println(z);
                }
            } else {
                System.out.println("No se pudo cargar el estado del juego.");
            }

                } else {
                    System.out.println("Opción inválida.");
                }
            } catch (IOException e) {
                System.out.println("Error al cargar estadísticas: " + e.getMessage());
            }

        } else if (opcion == 2) {
            cargarZombis();  // Aquí se carga la lista de zombis
            System.out.println("Cargando zombis...");
            if (zombisCargados.isEmpty()) {  // Si la lista está vacía, los zombis no se han cargado
                System.out.println("No hay zombis cargados.");
            }

            System.out.print("Introduce el ID del zombi: ");
            // Permitir la consulta por ID de zombi
            int zombiId = scanner.nextInt();

            boolean zombiEncontrado = false;
            for (Zombi zombi : zombisCargados) {
                if (zombi.getId() == zombiId) {
                    zombi.mostrarHeridosPorZombi(zombiId); // Muestra las estadísticas del zombi
                    zombiEncontrado = true;
                    break; // Salimos del bucle una vez que encontramos el zombi
                }
            }
            if (!zombiEncontrado) {
                System.out.println("Zombi con ID " + zombiId + " no encontrado.");
            }

        }else if (opcion == 3) {
       /* // Nueva opción para mostrar estadísticas del Almacén de Ataques
        try {

            // Intentamos cargar el almacén de ataques
            System.out.println("Cargando almacén de ataques...");
            AlmacenAtaques almacen = cargarAlmacen("src/registroAtaques.dat");  // Método estático que carga el almacén desde un archivo o fuente
               System.out.println("Número de ataques DESPUES de CARGAR: " + almacen.getAtaques().size());
            // Comprobamos si el almacén se ha cargado correctamente
            if (almacen == null) {
                System.out.println("El almacén de ataques no pudo ser cargado.");
                return;
            }
            System.out.println("Almacén cargado con éxito. Número de ataques: " + almacen.getAtaques().size());

            // Obtenemos la lista de ataques
            List<Ataque> ataques = almacen.getAtaques();

            // Depuración: Verificar el tamaño de la lista de ataques
            System.out.println("Número de ataques en el almacén: " + ataques.size());

            // Verificar si la lista está vacía
            if (ataques.isEmpty()) {
                System.out.println("No hay ataques registrados.");
            } else {
                System.out.println("Estadísticas del Almacén de Ataques:");
                for (Ataque ataque : ataques) {
                    // Mostrar detalles de cada ataque, asumiendo que Ataque tiene un método toString
                    System.out.println(ataque);  
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            // Capturamos excepciones relacionadas con la carga
            System.out.println("Error al cargar el almacén de ataques: " + e.getMessage());
        }
        */
       // Crear una nueva instancia del juego
            // Crear una nueva instancia del juego
            Juego miJuego = new Juego();

            // Intentar cargar el estado de la partida
            if (miJuego.cargarEstadoConNombreCarga()) {
                System.out.println("Estado del juego cargado correctamente.");

                // Ahora que el juego está cargado, puedes acceder al almacén de ataques
                AlmacenAtaques almacen = miJuego.getAlmacenAtaques();  // Aquí asumiendo que tienes un getter para el almacén

                // Mostrar estadísticas del almacén de ataques
                if (almacen != null) {
                    System.out.println("Número de ataquesFINAL: " + almacen.getAtaques2().size());
                    for (Ataque ataque : almacen.getAtaques2()) {
                        System.out.println(ataque);  // Mostramos cada ataque
                    }
                } else {
                    System.out.println("No se pudo cargar el almacén de ataques.");
                }
            } else {
                System.out.println("No se pudo cargar el estado del juego.");
            }


        } else {
            System.out.println("Opción inválida.");
        }
    }
    // Getter para obtener el almacén de ataques
    public AlmacenAtaques getAlmacenAtaques() {
        return ataque;
    }

    private void consultarInformacion(Superviviente supervivienteActual) {
        Scanner scanner = new Scanner(System.in);
        boolean consultaTerminada = false;

        while (!consultaTerminada) {
            System.out.println("\n¿Deseas consultar información de algún superviviente o zombi?");
            System.out.println("1. Consultar superviviente");
            System.out.println("2. Consultar zombi");
            System.out.println("3. Continuar con las acciones de " + supervivienteActual.getNombre());
            System.out.print("Elige una opción: ");

            if (scanner.hasNextInt()) { // Verifica que el usuario introduzca un número
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                switch (opcion) {
                    case 1: // Consultar superviviente
                        System.out.println("Supervivientes:");
                        for (int i = 0; i < supervivientesSeleccionados.size(); i++) {
                            System.out.println((i + 1) + ". " + supervivientesSeleccionados.get(i).getNombre());
                        }
                        System.out.print("Elige el número del superviviente para ver detalles: ");

                        if (scanner.hasNextInt()) { // Verifica que la selección sea un número
                            int indiceSuperviviente = scanner.nextInt() - 1;
                            scanner.nextLine(); // Consumir el salto de línea
                            if (indiceSuperviviente >= 0 && indiceSuperviviente < supervivientesSeleccionados.size()) {
                                Superviviente superviviente = supervivientesSeleccionados.get(indiceSuperviviente);
                                System.out.println("Detalles del superviviente:");
                                System.out.println(superviviente);

                                // Mostrar armas activas
                                if (superviviente.getArmasActivas() != null && !superviviente.getArmasActivas().isEmpty()) {
                                    System.out.println("Armas activas:");
                                    for (Arma arma : superviviente.getArmasActivas()) {
                                        System.out.println("- " + arma.getNombre());
                                    }
                                } else {
                                    System.out.println("No tiene armas activas.");
                                }

                                // Mostrar inventario
                                if (superviviente.getInventario() != null && !superviviente.getInventario().isEmpty()) {
                                    System.out.println("Inventario:");
                                   for (Equipo equipo : superviviente.getInventario()) {
                                        System.out.println("- " + equipo.getNombre());
                                     }
                                } else {
                                    System.out.println("El inventario está vacío.");
                                }
                            } else {
                                System.out.println("Número inválido. Por favor, selecciona un superviviente de la lista.");
                            }
                        } else {
                            System.out.println("Entrada inválida. Debes ingresar un número.");
                            scanner.nextLine(); // Limpiar el buffer en caso de error
                        }
                        break;

                    case 2: // Consultar zombis
                        System.out.println("Zombis en el tablero:");
                        for (int i = 0; i < zombis.size(); i++) {
                             Zombi zombi = zombis.get(i);

                            System.out.println((i + 1) + ". Tipo: " + zombi.getTipo()+
                                               ", Aguante: " + zombi.getAguante() +
                                               ", Activación: " + (zombi.getActivaciones()));
                        }
                        System.out.print("Elige el número del zombi para ver detalles (o 0 para volver): ");

                        if (scanner.hasNextInt()) { // Verifica que la selección sea un número
                            int indiceZombi = scanner.nextInt() - 1;
                            scanner.nextLine(); // Consumir el salto de línea
                            if (indiceZombi >= 0 && indiceZombi < zombis.size()) {
                                Zombi zombiSeleccionado = zombis.get(indiceZombi);

                                // Determinar el subtipo del zombi seleccionado
                                String tipoZombi = null;
                                if (zombiSeleccionado.isNormal()) {
                                    tipoZombi = "Normal";
                                } else if (zombiSeleccionado.isBerserker()) {
                                    tipoZombi = "Berserker";
                                } else if (zombiSeleccionado.isToxico()) {
                                    tipoZombi = "Toxico";
                                }

                                System.out.println("Detalles del zombi:");
                                System.out.println("Tipo: " + zombiSeleccionado.getTipo());
                                 System.out.println("Subtipo: " + tipoZombi);
                                System.out.println("Aguante: " + zombiSeleccionado.getAguante());
                                System.out.println("Activación: " + (zombiSeleccionado.getActivaciones()));
                            } else if (indiceZombi == -1) {
                                System.out.println("Volviendo al menú principal...");
                            } else {
                                System.out.println("Número inválido. Por favor, selecciona un zombi de la lista.");
                            }
                        } else {
                            System.out.println("Entrada inválida. Debes ingresar un número.");
                            scanner.nextLine(); // Limpiar el buffer en caso de error
                        }
                        break;

                    case 3: // Continuar con las acciones
                        consultaTerminada = true;
                        break;

                    default: // Opción inválida
                        System.out.println("Opción inválida. Por favor, elige una opción válida.");
                }
            } else {
                System.out.println("Entrada inválida. Por favor, elige un número entre 1 y 3.");
                scanner.nextLine(); // Limpiar el buffer en caso de entrada no válida
            }
        }
    }

    private void consultarInformacionZombi() {
        Scanner scanner = new Scanner(System.in);
        boolean consultaTerminada = false;

        while (!consultaTerminada) {
            System.out.println("\n¿Deseas consultar información del zombi?");
            System.out.println("1. Consultar zombi");
            System.out.println("2. Continuar con las acciones");
            System.out.print("Elige una opción: ");

            if (scanner.hasNextInt()) { // Verifica que el usuario introduzca un número
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                switch (opcion) {

                    case 1: // Consultar zombis
                        System.out.println("Zombis en el tablero:");
                        for (int i = 0; i < zombis.size(); i++) {
                             Zombi zombi = zombis.get(i);

                            System.out.println((i + 1) + ". Tipo: " + zombi.getTipo()+
                                               ", Aguante: " + zombi.getAguante() +
                                               ", Activación: " + (zombi.getActivaciones()));
                        }
                        System.out.print("Elige el número del zombi para ver detalles (o 0 para volver): ");

                        if (scanner.hasNextInt()) { // Verifica que la selección sea un número
                            int indiceZombi = scanner.nextInt() - 1;
                            scanner.nextLine(); // Consumir el salto de línea
                            if (indiceZombi >= 0 && indiceZombi < zombis.size()) {
                                Zombi zombiSeleccionado = zombis.get(indiceZombi);

                                // Determinar el subtipo del zombi seleccionado
                                String tipoZombi = null;
                                if (zombiSeleccionado.isNormal()) {
                                    tipoZombi = "Normal";
                                } else if (zombiSeleccionado.isBerserker()) {
                                    tipoZombi = "Berserker";
                                } else if (zombiSeleccionado.isToxico()) {
                                    tipoZombi = "Toxico";
                                }

                                System.out.println("Detalles del zombi:");
                                System.out.println("Tipo: " + zombiSeleccionado.getTipo());
                                 System.out.println("Subtipo: " + tipoZombi);
                                System.out.println("Aguante: " + zombiSeleccionado.getAguante());
                                System.out.println("Activación: " + (zombiSeleccionado.getActivaciones()));
                            } else if (indiceZombi == -1) {
                                System.out.println("Volviendo al menú principal...");
                            } else {
                                System.out.println("Número inválido. Por favor, selecciona un zombi de la lista.");
                            }
                        } else {
                            System.out.println("Entrada inválida. Debes ingresar un número.");
                            scanner.nextLine(); // Limpiar el buffer en caso de error
                        }
                        break;

                    case 2: // Continuar con las acciones
                        consultaTerminada = true;
                        break;

                    default: // Opción inválida
                        System.out.println("Opción inválida. Por favor, elige una opción válida.");
                }
            } else {
                System.out.println("Entrada inválida. Por favor, elige un número entre 1 y 3.");
                scanner.nextLine(); // Limpiar el buffer en caso de entrada no válida
            }
        }
    }

    private void consultarInformacionSimulacion4(Superviviente supervivienteActual) {
        Scanner scanner = new Scanner(System.in);
        boolean consultaTerminada = false;

        while (!consultaTerminada) {
            System.out.println("\n¿Deseas consultar información de algún superviviente?");
            System.out.println("1. Consultar superviviente");
            System.out.println("2. Continuar con las acciones de " + supervivienteActual.getNombre());
            System.out.print("Elige una opción: ");

            if (scanner.hasNextInt()) { // Verifica que el usuario introduzca un número
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                switch (opcion) {
                    case 1: // Consultar superviviente
                        System.out.println("Supervivientes:");
                        for (int i = 0; i < supervivientesSeleccionados.size(); i++) {
                            System.out.println((i + 1) + ". " + supervivientesSeleccionados.get(i).getNombre());
                        }
                        System.out.print("Elige el número del superviviente para ver detalles: ");

                        if (scanner.hasNextInt()) { // Verifica que la selección sea un número
                            int indiceSuperviviente = scanner.nextInt() - 1;
                            scanner.nextLine(); // Consumir el salto de línea
                            if (indiceSuperviviente >= 0 && indiceSuperviviente < supervivientesSeleccionados.size()) {
                                Superviviente superviviente = supervivientesSeleccionados.get(indiceSuperviviente);
                                System.out.println("Detalles del superviviente:");
                                System.out.println(superviviente);

                                // Mostrar armas activas
                                if (superviviente.getArmasActivas() != null && !superviviente.getArmasActivas().isEmpty()) {
                                    System.out.println("Armas activas:");
                                    for (Arma arma : superviviente.getArmasActivas()) {
                                        System.out.println("- " + arma.getNombre());
                                    }
                                } else {
                                    System.out.println("No tiene armas activas.");
                                }

                                // Mostrar inventario
                                if (superviviente.getInventario() != null && !superviviente.getInventario().isEmpty()) {
                                    System.out.println("Inventario:");
                                   for (Equipo equipo : superviviente.getInventario()) {
                                        System.out.println("- " + equipo.getNombre());
                                     }
                                } else {
                                    System.out.println("El inventario está vacío.");
                                }
                            } else {
                                System.out.println("Número inválido. Por favor, selecciona un superviviente de la lista.");
                            }
                        } else {
                            System.out.println("Entrada inválida. Debes ingresar un número.");
                            scanner.nextLine(); // Limpiar el buffer en caso de error
                        }
                        break;

                    case 2: // Continuar con las acciones
                        consultaTerminada = true;
                        break;

                    default: // Opción inválida
                        System.out.println("Opción inválida. Por favor, elige una opción válida.");
                }
            } else {
                System.out.println("Entrada inválida. Por favor, elige un número entre 1 y 3.");
                scanner.nextLine(); // Limpiar el buffer en caso de entrada no válida
            }
        }
    }

    private void mostrarEstadoJuego() {
        System.out.println("Estado del tablero:");
        tablero.mostrarTablero();  // Muestra el tablero

        System.out.println("Supervivientes:");
        // Usamos la lista de supervivientes seleccionados, no la lista completa
        for (Superviviente superviviente : supervivientesSeleccionados) {
            System.out.println(superviviente);  // Imprime el estado de cada superviviente seleccionado
        }
    }

    // Desactiva la consulta de información para este turno
    private void desactivarConsulta() {
        consultaHabilitada = false;
    }
    
    // Reactiva la consulta de información cuando se decida
    private void activarConsulta() {
        consultaHabilitada = true;
    }
   
    public void configurarTableroParaSupervivientes() {
        for (Superviviente superviviente : supervivientes) {
            superviviente.setTablero(this.tablero); // Asigna el tablero
        }
    }

    public void comprobarFinDeJuego(List<Superviviente> supervivientesSeleccionados) {
        int supervivientesEnObjetivo = 0;  // Contador de supervivientes que han llegado al objetivo
           int supervivientesConProvisiones = 0;  // Contador de supervivientes con al menos una provisión
           boolean algunoEliminado = false;  // Para saber si algún superviviente ha sido eliminado

           // Verificación de la lista de supervivientes
           /*System.out.println("Cantidad de supervivientes: " + supervivientesSeleccionados.size());
           if (supervivientesSeleccionados.isEmpty()) { // Usar supervivientesSeleccionados en vez de 'supervivientes'
               System.out.println("La lista de supervivientes está vacía.");
               return;  // Termina la ejecución del método si la lista está vacía
           }else{
               System.out.println("La lista de supervivientes está llena.");
           }*/

           // Recorremos a todos los supervivientes para comprobar su estado
           for (int i = 0; i < supervivientesSeleccionados.size(); i++) { // Usamos un índice en el bucle
               Superviviente superviviente = supervivientesSeleccionados.get(i);  // Obtenemos el superviviente actual
               int cantidadProvisiones = 0;
              // System.out.println("Superviviente " + (i + 1));  // Imprimir el número del superviviente (1 o 2)

               // Verificamos si el superviviente ha sido eliminado
               if (!superviviente.isVivo()) {
                   algunoEliminado = true;  // Se marca que al menos un superviviente ha sido eliminado
                   System.out.println(superviviente.getNombre() + " ha sido eliminado.");
                   break;  // Terminamos aquí si alguien ha sido eliminado
               }

               // Contamos las provisiones en el inventario del superviviente
               for (Equipo equipo : superviviente.getInventario()) {
                   if (equipo instanceof Provisiones) {
                       cantidadProvisiones++;
                   }
               }
               //System.out.println("Posición del superviviente: " + superviviente.getPosicion());
              // System.out.println("Posición del objetivo: " + tablero.getObjetivo());

               // Verificamos si el superviviente ha llegado a la casilla objetivo
               if (superviviente.getCoordenadas().equals(tablero.getObjetivo())) {
                   supervivientesEnObjetivo++;  // Aumentamos el contador de los que llegaron al objetivo
                   System.out.println(superviviente.getNombre() + " ha llegado al objetivo.");
               }

               // Verificamos si el superviviente tiene al menos una provisión
               if (cantidadProvisiones >= 1) {
                   supervivientesConProvisiones++;  // Aumentamos el contador de los que tienen provisiones
               }
                   // Si todos los supervivientes han llegado al objetivo y tienen provisiones
              if (supervivientesEnObjetivo ==  supervivientesSeleccionados.size() && supervivientesConProvisiones ==  supervivientesSeleccionados.size()) {
                  enJuego = false;
                  System.out.println("Todos los supervivientes han llegado al objetivo con provisiones! ¡Han ganado!");
                  System.exit(0);  // Termina el juego
              }
              if (algunoEliminado) {
               enJuego = false;
               System.out.println("El juego ha terminado porque un superviviente ha sido eliminado.");
               //System.exit(0);  // Termina el juego
               return;
           }

           }

           // Mostrar los resultados finales
           //System.out.println("Holaaa1 " + supervivientesEnObjetivo);
           //System.out.println("Holaaa2 " + supervivientesConProvisiones);

           // Si algún superviviente ha sido eliminado, terminamos el juego
           if (algunoEliminado) {
               enJuego = false;
               System.out.println("El juego ha terminado porque un superviviente ha sido eliminado.");
               //System.exit(0);  // Termina el juego
               return;
           }

           // Si todos los supervivientes han llegado al objetivo y tienen provisiones
           if (supervivientesEnObjetivo == 2 && supervivientesConProvisiones == 2) {
               enJuego = false;
               System.out.println("Todos los supervivientes han llegado al objetivo con provisiones! ¡Han ganado!");
               System.exit(0);  // Termina el juego
           }

           // Si no todos han llegado al objetivo
           /*if (supervivientesEnObjetivo <  supervivientesSeleccionados.size()) {
               System.out.println("No todos los supervivientes han llegado al objetivo.");
           }*/

           // Si no todos tienen provisiones
           /*if (supervivientesConProvisiones <  supervivientesSeleccionados.size()) {
               System.out.println("No todos los supervivientes tienen provisiones.");
           }*/

    }
    
   public void listarArchivosGuardados() {
    File carpeta = new File(DIRECTORIO_GUARDADO);
    
    // Filtrar solo los archivos que terminan con .dat y que no contienen el sufijo '_ataques'
    File[] archivos = carpeta.listFiles((dir, name) -> name.toLowerCase().endsWith(".dat") && !name.contains("_ataques"));
    
    if (archivos != null && archivos.length > 0) {
        System.out.println("Archivos disponibles para cargar:");
        for (int i = 0; i < archivos.length; i++) {
            System.out.println((i + 1) + ". " + archivos[i].getName());
        }
    } else {
        System.out.println("No hay archivos disponibles para cargar.");
    }
}


    public void guardarZombi(Zombi zombi, String archivo) throws IOException {
        // Verificar si la carpeta existe y crearla si no
        File file = new File(archivo);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();  // Crea el directorio si no existe
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(zombi); // Guarda el objeto zombi
        } catch (IOException e) {
            System.out.println("Error al guardar el zombi con ID " + zombi.getId() + " en el archivo: " + archivo + ". Error: " + e.getMessage());
            throw e; // Vuelve a lanzar la excepción para manejarla donde sea necesario
        }
    }
    
    public static Zombi cargarZombi(String archivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (Zombi) ois.readObject();
        }
    }
    
    // Método para cargar los supervivientes desde el fichero
    public void cargarSupervivientes() {
        // Usamos el método cargarSupervivientesDesdeFichero para cargar todos los supervivientes en la lista
        almacenSupervivientes = tablero.cargarSupervivientesDesdeFichero("src/supervivientes.txt");
    }
    
    public void cargarInventario() {
        System.out.println("Iniciando carga del inventario...");
        
        try {
            // Cargar el inventario desde el archivo
            almacenInventario.cargarDesdeFichero("src/inventario.txt");

            // Acceder a las armas y provisiones cargadas
            Set<Arma> armas = almacenInventario.getArmas();  // Obtenemos las armas
            Set<Provisiones> provisiones = almacenInventario.getProvisiones();  // Obtenemos las provisiones

            // Mostrar las armas cargadas
            if (!armas.isEmpty()) {
                System.out.println("Armas cargadas:");
                for (Arma arma : armas) {
                    System.out.println("- " + arma.getNombre());
                }
            } else {
                System.out.println("No se cargaron armas.");
            }

            // Mostrar las provisiones cargadas
            if (!provisiones.isEmpty()) {
                System.out.println("Provisiones cargadas:");
                for (Provisiones provision : provisiones) {
                    System.out.println("- " + provision.getNombre() + " (Cantidad: " + provision.getValorEnergetico() + ")");
                }
            } else {
                System.out.println("No se cargaron provisiones.");
            }

        } catch (IOException e) {
            System.err.println("Error al cargar el inventario: " + e.getMessage());
        }
        
    }


    private static void cargarZombis() {
        Juego j= new Juego();
        j.cargarContadorZombis();
        for (int i = 0; i < contadorZombisPartida; i++) {
            try {
                Zombi zombi = cargarZombi("src/zombi/zombi" + i + ".dat");  // Cargar el zombi desde el archivo correspondiente
                zombisCargados.add(zombi);  // Agregar a la lista de zombis cargados
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error al cargar el zombi con ID " + i + ": " + e.getMessage());
            }
        }
    }

    public void guardarEstadoConNombre(String nombreArchivo) {
        crearDirectorioSiNoExiste(); // Asegura que el directorio exista
        if (nombreArchivo.isEmpty()) {
            System.out.println("Nombre inválido. Guardando con el nombre predeterminado 'estadoJuego.dat'.");
            nombreArchivo = "estadoJuego";
        }
        String rutaCompleta = DIRECTORIO_GUARDADO + nombreArchivo + ".dat";
        
         String rutaCompletaAtaques = DIRECTORIO_GUARDADO + nombreArchivo + "_ataques.dat";
        try {
            guardarJuego(this, rutaCompleta);
            System.out.println("Juego guardado con éxito como: " + nombreArchivo + ".dat");
                        // Obtener la lista de ataques usando el método getAtaques2
           // List<Ataque> listaDeAtaques = ataque.getAtaques2();

            // Ahora, guardar solo la lista de ataques, no el AlmacenAtaques completo
            //guardarListaAtaques(listaDeAtaques, rutaCompletaAtaques);

             // Obtener la lista de ataques usando el método getAtaques2
        // Obtener la lista de ataques
        List<Ataque> listaDeAtaques = ataque.getAtaques2(); 

        // Verifica el tamaño de la lista antes de guardar
        System.out.println("Número de ataques a guardar: " + listaDeAtaques.size());

        // Ahora, guarda solo la lista de ataques
        guardarListaAtaques(listaDeAtaques, rutaCompletaAtaques);  // Método para guardar la lista de ataques
   //guardarAlmacenAtaques(ataque, rutaCompletaAtaques);  // Guardar el almacen de ataques
             System.out.println("Almacén de ataques guardado en: " + rutaCompletaAtaques);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al guardar el estado del juego.");
        }
    }

    public void guardarAlmacenAtaques(AlmacenAtaques almacen, String ruta) {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ruta))) {
            salida.writeObject(almacen);  // Serializar y guardar el objeto almacen
        } catch (IOException e) {
            System.out.println("Error al guardar el almacén de ataques: " + e.getMessage());
        }
    }
public void guardarListaAtaques(List<Ataque> listaAtaques, String ruta) {
    try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ruta))) {
        salida.writeObject(listaAtaques);  // Serializar y guardar la lista de ataques
        System.out.println("Lista de ataques guardada correctamente en: " + ruta);
    } catch (IOException e) {
        System.out.println("Error al guardar la lista de ataques: " + e.getMessage());
    }
}
public List<Ataque> cargarListaAtaques(String ruta) {
    try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ruta))) {
        return (List<Ataque>) entrada.readObject();  // Deserializa y carga la lista de ataques
    } catch (IOException | ClassNotFoundException e) {
        System.out.println("Error al cargar la lista de ataques: " + e.getMessage());
        return new ArrayList<>();  // Retorna una lista vacía si ocurre un error
    }
}

    
    /*
    public boolean cargarEstadoConNombre() {
        // Primero, listar los archivos disponibles para cargar
        listarArchivosGuardados();

        // Solicitar el nombre del archivo al usuario
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce el nombre del archivo que deseas cargar (incluye .dat): ");
        String nombreArchivo = scanner.nextLine().trim();

        // Verificar si el nombre de archivo ya incluye la extensión .dat, si no, agregarla
        if (!nombreArchivo.endsWith(".dat")) {
            nombreArchivo += ".dat";  // Agregar la extensión si no la tiene
        }

        // Crear la ruta completa del archivo para el juego y el almacén de ataques
        String rutaCompletaJuego = DIRECTORIO_GUARDADO + File.separator + nombreArchivo;
        String rutaCompletaAtaques = DIRECTORIO_GUARDADO + File.separator + nombreArchivo.replace(".dat", "_ataques.dat");

        // Verificar si el archivo del estado del juego existe
        File archivoJuego = new File(rutaCompletaJuego);
        if (!archivoJuego.exists()) {  // Si el archivo no existe
            System.out.println("El archivo " + nombreArchivo + " no existe. Verifica el nombre o la ruta.");
            return false;  // Terminar la ejecución sin intentar cargar el archivo
        }

        // Si el archivo del juego existe, intentar cargarlo
        Juego estadoCargado = (Juego) cargarJuego(rutaCompletaJuego);
        if (estadoCargado != null) {
            // Si la carga fue exitosa, restaurar el estado del juego
            this.supervivientesSeleccionados = estadoCargado.getSupervivientesSeleccionados();
            this.zombis = estadoCargado.zombis;
            this.tablero = estadoCargado.tablero;
            this.turno = estadoCargado.turno;
            this.enJuego = estadoCargado.enJuego;
            System.out.println("Juego cargado con éxito desde: " + nombreArchivo);

            // Ahora intentamos cargar el almacén de ataques
            File archivoAtaques = new File(rutaCompletaAtaques);
            if (archivoAtaques.exists()) {
                try {
                    AlmacenAtaques almacenCargado = cargarAlmacen(archivoAtaques.getPath());
                    if (almacenCargado != null) {
                        // Si el almacén de ataques se carga correctamente
                        System.out.println("Almacén de ataques cargado con éxito desde: " + archivoAtaques.getName());
                        // Actualizamos el almacén de ataques en el juego
                        this.ataque = almacenCargado;
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Error al cargar el almacén de ataques: " + e.getMessage());
                }
            } else {
                System.out.println("No se encontró un archivo de almacén de ataques asociado.");
            }

            return true;  // Retornar true si el juego y el almacén se cargaron correctamente
        } else {
            // Si hubo un problema al cargar el archivo del juego
            System.out.println("No se pudo cargar el archivo del juego. Verifica que el nombre sea correcto.");
            return false;  // Retornar false si hubo un error al cargar
        }
    }
*/
  /*  public boolean cargarEstadoConNombre() {
    // Primero, listar los archivos disponibles para cargar
    listarArchivosGuardados();

    // Solicitar el nombre del archivo al usuario
    Scanner scanner = new Scanner(System.in);
    System.out.println("Introduce el nombre del archivo que deseas cargar (incluye .dat): ");
    String nombreArchivo = scanner.nextLine().trim();

    // Verificar si el nombre de archivo ya incluye la extensión .dat, si no, agregarla
    if (!nombreArchivo.endsWith(".dat")) {
        nombreArchivo += ".dat";  // Agregar la extensión si no la tiene
    }

    // Crear la ruta completa del archivo para el juego y el almacén de ataques
    String rutaCompletaJuego = DIRECTORIO_GUARDADO + File.separator + nombreArchivo;
    String rutaCompletaAtaques = DIRECTORIO_GUARDADO + File.separator + nombreArchivo.replace(".dat", "_ataques.dat");

    // Verificar si el archivo del estado del juego existe
    File archivoJuego = new File(rutaCompletaJuego);
    if (!archivoJuego.exists()) {  // Si el archivo no existe
        System.out.println("El archivo " + nombreArchivo + " no existe. Verifica el nombre o la ruta.");
        return false;  // Terminar la ejecución sin intentar cargar el archivo
    }

    // Si el archivo del juego existe, intentar cargarlo
    Juego estadoCargado = (Juego) cargarJuego(rutaCompletaJuego);
    if (estadoCargado != null) {
        // Si la carga fue exitosa, restaurar el estado del juego
        this.supervivientesSeleccionados = estadoCargado.getSupervivientesSeleccionados();
        this.zombis = estadoCargado.zombis;
        this.tablero = estadoCargado.tablero;
        this.turno = estadoCargado.turno;
        this.enJuego = estadoCargado.enJuego;
        System.out.println("Juego cargado con éxito desde: " + nombreArchivo);

        // Asignar la ruta de almacén de ataques para usarla en continuarJuego
        this.rutaAlmacenAtaques = rutaCompletaAtaques;
         // **Asignar el nombre del archivo cargado a la variable global**
        this.nombrePartida = nombreArchivo.replace(".dat", ""); // Sin la extensión .dat
        
    almacen = cargarAlmacenAtaques(rutaCompletaAtaques);  // Cargar el almacen de ataques
        System.out.println("Almacén de ataques cargado correctamente.");
        
        } else {
            System.out.println("No se encontró un archivo de almacén de ataques asociado.");
        }

        return true;  // Retornar true si el juego y el almacén se cargaron correctamente
   
}
*/
/*
public boolean cargarEstadoConNombre() {
    // Primero, listar los archivos disponibles para cargar
    listarArchivosGuardados();

    // Solicitar el nombre del archivo al usuario
    Scanner scanner = new Scanner(System.in);
    System.out.println("Introduce el nombre del archivo que deseas cargar (incluye .dat): ");
    String nombreArchivo = scanner.nextLine().trim();

    // Verificar si el nombre de archivo ya incluye la extensión .dat, si no, agregarla
    if (!nombreArchivo.endsWith(".dat")) {
        nombreArchivo += ".dat";  // Agregar la extensión si no la tiene
    }

    // Crear la ruta completa del archivo para el juego y el almacén de ataques
    String rutaCompletaJuego = DIRECTORIO_GUARDADO + File.separator + nombreArchivo;
    String rutaCompletaAtaques = DIRECTORIO_GUARDADO + File.separator + nombreArchivo.replace(".dat", "_ataques.dat");

    // Verificar si el archivo del estado del juego existe
    File archivoJuego = new File(rutaCompletaJuego);
    if (!archivoJuego.exists()) {  // Si el archivo no existe
        System.out.println("El archivo " + nombreArchivo + " no existe. Verifica el nombre o la ruta.");
        return false;  // Terminar la ejecución sin intentar cargar el archivo
    }
/*
    // Si el archivo del juego existe, intentar cargarlo
    Juego estadoCargado = (Juego) cargarJuego(rutaCompletaJuego);
    if (estadoCargado != null) {
        // Si la carga fue exitosa, restaurar el estado del juego
        this.supervivientesSeleccionados = estadoCargado.getSupervivientesSeleccionados();
        this.zombis = estadoCargado.zombis;
        this.tablero = estadoCargado.tablero;
        this.turno = estadoCargado.turno;
        this.enJuego = estadoCargado.enJuego;
        System.out.println("Juego cargado con éxito desde: " + nombreArchivo);

        // Asignar la ruta de almacén de ataques para usarla en continuarJuego
        this.rutaAlmacenAtaques = rutaCompletaAtaques;
        
        // Asignar el nombre del archivo cargado a la variable global
        this.nombrePartida = nombreArchivo.replace(".dat", ""); // Sin la extensión .dat
        ataque = cargarAlmacenAtaques(rutaCompletaAtaques);  // Cargar el almacen de ataques
        System.out.println("Almacén de ataques cargado correctamente.");

    } else {
        System.out.println("Error al cargar el estado del juego. No se encontró el archivo de juego.");
        return false;
    }
try {
        // Validar que el archivo contiene un objeto de tipo Juego
        Object obj = cargarJuego(rutaCompletaJuego);
        if (!(obj instanceof Juego)) {
            throw new ClassCastException("El archivo no contiene un objeto de tipo Juego.");
        }
        Juego estadoCargado = (Juego) obj;

        // Restaurar el estado del juego
        this.supervivientesSeleccionados = estadoCargado.getSupervivientesSeleccionados();
        this.zombis = estadoCargado.zombis;
        this.tablero = estadoCargado.tablero;
        this.turno = estadoCargado.turno;
        this.enJuego = estadoCargado.enJuego;

        System.out.println("Juego cargado con éxito desde: " + nombreArchivo);

        // Cargar el almacén de ataques
        ataque = cargarAlmacenAtaques(rutaCompletaAtaques);
        System.out.println("Almacén de ataques cargado correctamente.");

    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Error al cargar el estado del juego.");
        return false;
    }

    return true;  // Retornar true si el juego y el almacén se cargaron correctamente
}*/

   
  /*   public boolean cargarEstadoConNombreCarga() throws IOException {
        // Listar los archivos disponibles
        listarArchivosGuardados();

        // Solicitar el nombre del archivo
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce el nombre del archivo que deseas cargar (sin incluir '_ataques.dat'):");
        String nombreArchivo = scanner.nextLine().trim();

        
        
    if (nombreArchivo.equals("salir")) {
        System.out.println("Carga cancelada. Regresando al menú principal.");
        return false;  // Salir de la carga
    }
    
    
        // Asegurar la extensión
        if (!nombreArchivo.endsWith(".dat")) {
            nombreArchivo += ".dat";
        }

        // Rutas completas para el juego y el almacén de ataques
        String rutaCompletaJuego = DIRECTORIO_GUARDADO + File.separator + nombreArchivo;
        String rutaCompletaAtaques = DIRECTORIO_GUARDADO + File.separator + nombreArchivo.replace(".dat", "_ataques.dat");

        // Intentar cargar el juego
        try {
            Object obj = cargarJuego(rutaCompletaJuego);

            // Validar que sea un objeto de tipo Juego
            if (!(obj instanceof Juego)) {
                throw new ClassCastException("El archivo no contiene un objeto de tipo Juego.");
            }

            Juego estadoCargado = (Juego) obj;

            // Restaurar el estado del juego
            this.supervivientesSeleccionados = estadoCargado.getSupervivientesSeleccionados();
            this.zombis = estadoCargado.zombis;
            this.tablero = estadoCargado.tablero;
            this.turno = estadoCargado.turno;
            this.enJuego = estadoCargado.enJuego;

            System.out.println("Juego cargado con éxito desde: " + rutaCompletaJuego);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar el archivo del juego: " + e.getMessage());
            return false;
        }

        // Intentar cargar la lista de ataques
        try {
            List<Ataque> listaDeAtaques = cargarListaAtaques(rutaCompletaAtaques);
            System.out.println("Lista de ataques cargada correctamente desde: " + rutaCompletaAtaques);
            System.out.println("Número de ataques cargados: " + listaDeAtaques.size());

            // Asignar la lista de ataques cargada al objeto correspondiente
            ataque.setAtaques2(listaDeAtaques);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar la lista de ataques. Se usará una lista vacía.");
        }
      /*  // Intentar cargar el almacén de ataques
        try {
            ataque = cargarAlmacenAtaques(rutaCompletaAtaques);
            System.out.println("Almacén de ataques cargado correctamente desde: " + rutaCompletaAtaques);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar el almacén de ataques. Se usará un almacén vacío.");
            ataque = new AlmacenAtaques(); // Inicializar un almacén vacío si hay error
        }
   
        continuarJuego();
        return true; // Ambas cargas completadas (juego y almacén)
    } */  

   public boolean cargarEstadoConNombreCarga() throws IOException, ClassNotFoundException, ClassCastException {
    // Listar los archivos disponibles
    listarArchivosGuardados();

    // Solicitar el número del archivo
    Scanner scanner = new Scanner(System.in);
    System.out.println("Introduce el número del archivo que deseas cargar:");
    int opcion = scanner.nextInt();

    // Listar los archivos disponibles
    File carpeta = new File(DIRECTORIO_GUARDADO);
    File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".dat"));

    // Verificar que la opción esté dentro del rango
    if (opcion <= 0 || opcion > archivos.length) {
        System.out.println("Opción no válida. Por favor, elige un número dentro del rango.");
        return false;
    }

    // Obtener el nombre del archivo seleccionado
    String nombreArchivo = archivos[opcion - 1].getName();
    // Asegurar que el archivo tenga la extensión correcta
    if (!nombreArchivo.endsWith(".dat")) {
        nombreArchivo += ".dat";
    }

    // Guardar el nombre de la partida automáticamente para usarlo en continuarJuego
    String nombrePartida = nombreArchivo.replace(".dat", "");  // Eliminar la extensión .dat

    // Rutas completas para el juego y el almacén de ataques
    String rutaCompletaJuego = DIRECTORIO_GUARDADO + File.separator + nombreArchivo;
    String rutaCompletaAtaques = DIRECTORIO_GUARDADO + File.separator + nombreArchivo.replace(".dat", "_ataques.dat");

    // Intentar cargar el juego
    Object obj = cargarJuego(rutaCompletaJuego);

    // Verificar si el archivo cargado es del tipo Juego
    if (obj == null) {
        System.out.println("El archivo está vacío o no se pudo leer.");
        return false;
    }

    // Validar que el objeto cargado sea del tipo Juego
    if (!(obj instanceof Juego)) {
        System.out.println("El archivo no contiene un objeto de tipo 'Juego'. Tipo encontrado: " + obj.getClass().getName());
        return false;
    }

    Juego estadoCargado = (Juego) obj;

    // Restaurar el estado del juego
    this.supervivientesSeleccionados = estadoCargado.getSupervivientesSeleccionados();
    this.zombis = estadoCargado.zombis;
    this.tablero = estadoCargado.tablero;
    this.turno = estadoCargado.turno;
    this.enJuego = estadoCargado.enJuego;

    System.out.println("Juego cargado con éxito desde: " + rutaCompletaJuego);

    // Intentar cargar la lista de ataques
    List<Ataque> listaDeAtaques = cargarListaAtaques(rutaCompletaAtaques);
    System.out.println("Lista de ataques cargada correctamente desde: " + rutaCompletaAtaques);
    System.out.println("Número de ataques cargados: " + listaDeAtaques.size());

    // Asignar la lista de ataques cargada al objeto correspondiente
    ataque.setAtaques2(listaDeAtaques);

    // Llamar a continuarJuego pasando el nombre de la partida cargada
    continuarJuego(nombrePartida);  // Pasa el nombrePartida sin pedirlo al usuario
    return true; // Ambas cargas completadas (juego y almacén)
}





    public AlmacenAtaques cargarAlmacenAtaques(String ruta) {
    try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ruta))) {
        return (AlmacenAtaques) entrada.readObject();  // Deserializar el objeto almacen de ataques
    } catch (IOException | ClassNotFoundException e) {
        System.out.println("Error al cargar el almacén de ataques: " + e.getMessage());
        return new AlmacenAtaques();  // Retornar un almacén vacío si ocurre un error
    }
}

    public static void guardarJuego(Object objeto, String nombreArchivo) {
           try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
               salida.writeObject(objeto); // Serializa el objeto y lo escribe en el archivo
               System.out.println("Juego guardado con éxito.");
           } catch (IOException e) {
               e.printStackTrace();
               System.out.println("Error al guardar el juego.");
           }
       }
 
    public static Object cargarJuego(String nombreArchivo) {
        Object objetoCargado = null;
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(nombreArchivo))) {
            objetoCargado = entrada.readObject(); // Deserializa el objeto desde el archivo
            System.out.println("Juego cargado con éxito.");
        } catch (FileNotFoundException e) {
            System.out.println("El archivo no existe: " + nombreArchivo);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error al cargar el juego.");
        }
        return objetoCargado;
    }
    
  // Método en el contexto de instancia
    private Superviviente obtenerSupervivientePorNombre(String nombreSuperviviente) {
        for (Superviviente s : this.supervivientesSeleccionados) {
            if (s.getNombre().equalsIgnoreCase(nombreSuperviviente)) {
                return s; // Devuelve el superviviente si el nombre coincide
            }
        }
        return null; // Devuelve null si no encuentra un superviviente con ese nombre
    }


    public void mostrarZombisEliminados(String nombreSuperviviente) {
        // Buscar el superviviente
        Superviviente superviviente = null;
        for (Superviviente s : supervivientesSeleccionados) {
            if (s.getNombre().equalsIgnoreCase(nombreSuperviviente)) {
                superviviente = s;
                break;
            }
    }

    if (superviviente == null) {
        System.out.println("No se encontró un superviviente con ese nombre.");
        return;
    }

    // Mostrar los zombis eliminados por este superviviente
    List<Zombi> zombisEliminados = superviviente.getZombisEliminados();
    if (zombisEliminados.isEmpty()) {
        System.out.println("Este superviviente no ha eliminado zombis.");
    } else {
        System.out.println("Zombis eliminados por " + nombreSuperviviente + ":");
        for (Zombi z : zombisEliminados) {
            System.out.println("Zombi: " + z.getTipo() + ", Aguante: " + z.getAguante());
        }
    }
}

    private void cargarContadorZombis() {
        
        try (BufferedReader lector = new BufferedReader(new FileReader("src/estado_partida.txt"))) {
            String linea = lector.readLine();
            if (linea != null) {
                this.contadorZombisPartida = Integer.parseInt(linea);
                System.out.println("Contador de zombis cargado: " + contadorZombisPartida);  // Confirma que se ha cargado el valor
            } else {
                this.contadorZombisPartida = 0;  // Si no hay archivo, iniciamos desde 0
                System.out.println("No se encontró archivo, contador iniciado a 0.");
            }
        } catch (IOException e) {
            System.out.println("Error al cargar el contador de zombis: " + e.getMessage());
            this.contadorZombisPartida = 0;  // Empezamos desde 0 si no se pudo cargar
        }
    }

    public void guardarContadorZombis() {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter("src/estado_partida.txt"))) {
            escritor.write(String.valueOf(contadorZombisPartida));  // Guardamos el último ID generado
            System.out.println("Contador de zombis guardado: " + contadorZombisPartida);  // Confirma que se ha guardado
        } catch (IOException e) {
            System.out.println("Error al guardar el contador de zombis: " + e.getMessage());
        }
    }

    public static void crearDirectorioSiNoExiste() {
    File carpeta = new File(DIRECTORIO_GUARDADO);
    if (!carpeta.exists()) {
        carpeta.mkdirs();
        System.out.println("Directorio de guardados creado: " + DIRECTORIO_GUARDADO);
    }
}

  
}

