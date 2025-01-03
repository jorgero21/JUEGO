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
    
    private Tablero tablero;
    private List<Superviviente> supervivientes;
    private List<Zombi> zombis;
    private AlmacenAtaques ataque;
    private int turno; 
    private static int contadorZombisPartida=0;  
    private int id=1;
    private String archivo = "src/estado_partida.txt"; 
    private static List<Zombi> zombisCargados = new ArrayList<>();
    private List<Superviviente> supervivientesSeleccionados; 
    private boolean enJuego;
    private List<Superviviente> almacenSupervivientes;
    private Set<Equipo> inventario;
    private Inventario almacenInventario;
    private boolean consultaHabilitada = true;
    public static final String DIRECTORIO_GUARDADO = "guardados/";
    private String nombrePartida; 
    
    public Juego() {
        this.supervivientes = new ArrayList<>();
        this.enJuego = true; 
        this.ataque = new AlmacenAtaques();
        this.almacenSupervivientes= new ArrayList<>();
        this.tablero = new Tablero(); 
        this.almacenInventario = new Inventario(); 
        this.zombis = new ArrayList<>();  
           
    }

    public Tablero getTablero() {   return this.tablero;}
    public int getTurno() {   return turno; }
    public void setTurno(int turno) {  this.turno = turno;   }
    public List<Superviviente> getSupervivientesSeleccionados() {  return supervivientesSeleccionados; }
    public int obtenerNumeroDeSupervivientesSeleccionados(List<Superviviente> supervivientesSeleccionados) { return supervivientesSeleccionados.size();}
    public String getNombrePartida() {  return this.nombrePartida;}
    
    public List<Superviviente> seleccionarSupervivientes(List<Superviviente> todosSupervivientes) {
        List<Superviviente> seleccionados = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Selecciona hasta 4 supervivientes:");
        for (int i = 0; i < todosSupervivientes.size(); i++) {
            System.out.println((i + 1) + ". " + todosSupervivientes.get(i).getNombre());
        }
        while (seleccionados.size() < 2) {
            System.out.print("Introduce el numero del superviviente que deseas seleccionar (o 0 para terminar): ");
            if (scanner.hasNextInt()) {
                int eleccion = scanner.nextInt() - 1;
                if (eleccion == -1) {
                    break; 
                }
                if (eleccion >= 0 && eleccion < todosSupervivientes.size()) {
                    Superviviente elegido = todosSupervivientes.get(eleccion);
                    if (!seleccionados.contains(elegido)) {
                        seleccionados.add(elegido);
                        System.out.println(elegido.getNombre() + " ha sido seleccionado");
                    } else {
                        System.out.println("Este superviviente ya ha sido seleccionado");
                    }
                } else {
                    System.out.println("Seleccion invalida. Intentalo de nuevo");
                }
            } else {
                System.out.println("Entrada no valida. Debes ingresar un numero");
                scanner.next();  
            }
        }

        return seleccionados;
    }

    public List<Superviviente> seleccionarSupervivientesSimulacion(List<Superviviente> todosSupervivientes) {
        List<Superviviente> seleccionados = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Selecciona 1 superviviente:");
        for (int i = 0; i < todosSupervivientes.size(); i++) {
            System.out.println((i + 1) + ". " + todosSupervivientes.get(i).getNombre());
        }
        while (seleccionados.size() < 1) {
            System.out.print("Introduce el numero del superviviente que deseas seleccionar (o 0 para terminar): ");
            if (scanner.hasNextInt()) {
                int eleccion = scanner.nextInt() - 1;
                if (eleccion == -1) {
                    break;  
                }
                if (eleccion >= 0 && eleccion < todosSupervivientes.size()) {
                    Superviviente elegido = todosSupervivientes.get(eleccion);
                    if (!seleccionados.contains(elegido)) {
                        seleccionados.add(elegido);
                        System.out.println(elegido.getNombre() + " ha sido seleccionado");
                    } else {
                        System.out.println("Este superviviente ya ha sido seleccionado");
                    }
                } else {
                    System.out.println("Seleccion invalida. Intentalo de nuevo");
                }
            } else {
                System.out.println("Entrada no valida. Debes ingresar un número");
                scanner.next(); 
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
            System.out.println("6. Mostrar Estadisticas");
            System.out.println("7. Salir");
            System.out.print("Elige una opcion: ");

            if (scanner.hasNextInt()) {
                int opcion = scanner.nextInt();
                scanner.nextLine(); 

                switch (opcion) {
                    case 1:
                        iniciar(); 
                        break;
                    case 2:
                        if (cargarEstadoConNombreCarga()) {
                            System.out.println("Se pudo cargar el juego");
                        } else {
                            System.out.println("No se pudo cargar el juego");
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
                        mostrarEstadisticas(scanner);  
                        break;
                    case 7:
                        salir = true;
                        System.out.println("Saliendo del juego. Hasta luego!");
                        break;
                    default:
                        System.out.println("Opcion invalida. Intente de nuevo");
                }
            } else {
                System.out.println("Entrada no valida. Por favor, elige un numero del 1 al 7");
                scanner.nextLine(); 
            }
        }
    }
      
    private void realizarAccionesSupervivientes(Superviviente s, String rutaAlmacenAtaques) throws IOException {
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < supervivientesSeleccionados.size(); i++) {
            Superviviente superviviente = supervivientesSeleccionados.get(i);
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
                        superviviente.moverse();
                        break;
                   case "buscar":
                       superviviente.buscar(almacenInventario);
                        break;
                    case "atacar":
                        superviviente.atacar(tablero, ataque,rutaAlmacenAtaques,superviviente);
                    break;
                    case "elegir arma":
                          superviviente.elegirArmaActiva();
                             break;
                    case "no hacer nada":
                           tablero.mostrarTablero();
                           superviviente.restarAccion();
                         break;
                    default:
                        System.out.println("Accion no valida. Intenta de nuevo");
                }
                System.out.println("Inventario de " + superviviente.getNombre() + ":");
                for (Equipo equipo : superviviente.getInventario()) {
                       System.out.println("- " + equipo.getNombre());
                    }
                    System.out.println(); 
                    System.out.println("Armas activas de " + superviviente.getNombre() + ":");
                    List<Arma> armasActivas = superviviente.getArmasActivas(); 
                    if (armasActivas == null || armasActivas.isEmpty()) {
                        System.out.println("No hay armas activas");
                    } else {
                        for (int j = 0; j < armasActivas.size(); j++) {
                            System.out.println((j + 1) + ". " + armasActivas.get(j).getNombre());
                        }
                    }
            }
            if (i == supervivientesSeleccionados.size() - 1) {
                desactivarConsulta(); 
            }   

        }
    }
  
    private void realizarAccionesSupervivientesSimulacionAtaque(Superviviente s) throws IOException {
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < supervivientesSeleccionados.size(); i++) {
            Superviviente superviviente = supervivientesSeleccionados.get(i);
            if (consultaHabilitada) {
                mostrarEstadoJuego();
                consultarInformacionSimulacion4(superviviente);
            }
            while (superviviente.getAcciones() > 0 && superviviente.isVivo() == true) {
                System.out.println(superviviente.getNombre() + ", elige tu accion (moverse/buscar/atacar/elegir arma/no hacer nada):");
                String accion = scanner.nextLine();

                switch (accion.toLowerCase()) {
                    case "moverse":
                        superviviente.moverse();
                        break;
                    case "buscar":
                        superviviente.buscar(almacenInventario);
                        break;
                    case "atacar":
                        superviviente.atacarSimulacion(tablero, ataque,this);
                        break;
                    case "elegir arma":
                        superviviente.elegirArmaActiva();
                        break;
                    case "no hacer nada":
                        tablero.mostrarTablero();
                        superviviente.restarAccion();
                        break;
                    default:
                        System.out.println("Accion no valida. Intenta de nuevo");
                }
                System.out.println("Inventario de " + superviviente.getNombre() + ":");
                for (Equipo equipo : superviviente.getInventario()) {
                    System.out.println("- " + equipo.getNombre());
                }
                System.out.println(); 
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
            if (i == supervivientesSeleccionados.size() - 1) {
                desactivarConsulta();  
            }
        }
    }

    private void realizarAccionesSupervivientesSimulacionBusqueda(Superviviente s) {
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < supervivientesSeleccionados.size(); i++) {
            Superviviente superviviente = supervivientesSeleccionados.get(i);
            if (consultaHabilitada) {
                mostrarEstadoJuego();
                consultarInformacionSimulacion4(superviviente);
            }
            while (superviviente.getAcciones() > 0 && superviviente.isVivo() == true) {
                System.out.println(superviviente.getNombre() + ", elige tu accion (moverse/buscar/elegir arma/no hacer nada):");
                String accion = scanner.nextLine();
                switch (accion.toLowerCase()) {
                    case "moverse":
                        superviviente.moverse();
                        break;
                    case "buscar":
                        superviviente.buscar(almacenInventario);
                        break;
                    case "elegir arma":
                        superviviente.elegirArmaActiva();
                        break;
                    case "no hacer nada":
                        tablero.mostrarTablero();
                        superviviente.restarAccion();
                        break;
                    default:
                        System.out.println("Accion no valida. Intenta de nuevo");
                }
                System.out.println("Inventario de " + superviviente.getNombre() + ":");
                for (Equipo equipo : superviviente.getInventario()) {
                    System.out.println("- " + equipo.getNombre());
                }
                System.out.println();
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
            if (i == supervivientesSeleccionados.size() - 1) {
                desactivarConsulta();  
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
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce un nombre para la partida: ");
        String nombrePartida = scanner.nextLine().trim();
        if (nombrePartida.isEmpty()) {
            nombrePartida = "Default";
        }
        String rutaAlmacenAtaques = DIRECTORIO_GUARDADO + nombrePartida + "_ataques.dat";
        try {
            for (int i = 1; i <= contadorZombisPartida; i++) {
                Zombi zombi = cargarZombi("src/zombi/zombi" + i + ".dat");
                zombisCargados.add(zombi);  // Agregar a la lista de zombis cargados
            }
            System.out.println("Zombis cargados correctamente");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar los zombis: " + e.getMessage());
        }
         try {
            ataque = cargarAlmacen(rutaAlmacenAtaques);
            System.out.println("Almacen cargado correctamente para la partida: " + nombrePartida);
        } catch (IOException | ClassNotFoundException e) {
            ataque = new AlmacenAtaques();
            System.out.println("No se encontro un almacen de ataques previo. Se creo uno nuevo");
        }
        cargarSupervivientes();
        cargarInventario();
        cargarContadorZombis();
        List<Superviviente> supervivientesSeleccionados = seleccionarSupervivientes(almacenSupervivientes);
        if (supervivientesSeleccionados.isEmpty()) {
            System.out.println("No se seleccionaron supervivientes");
            return;
        }
        this.tablero = new Tablero();  
        if (supervivientesSeleccionados.isEmpty()) {
            System.out.println("No se han seleccionado supervivientes. El juego no puede continuar");
            this.enJuego = false;  
            return;
        } else {
            System.out.println("Se han seleccionado supervivientes");
        }
        comprobarFinDeJuego(supervivientesSeleccionados);
        this.supervivientesSeleccionados = supervivientesSeleccionados;
        for (Superviviente superviviente : supervivientesSeleccionados) {
            superviviente.reiniciarEstado();
            superviviente.setTablero(tablero); 
        }
        if (this.tablero != null) {
            System.out.println("\nEl tablero se ha inicializado correctamente");
        } else {
            System.out.println("Error al inicializar el tablero. El juego no puede continuar");
            this.enJuego = false;
        }
        int numZombis = 3; 
        for (int i = 1; i <= numZombis; i++) {
            Zombi nuevoZombi = tablero.generarZombiConProbabilidades(contadorZombisPartida, tablero);
            if (nuevoZombi != null) {
                zombis.add(nuevoZombi); 
                System.out.println("Zombi generado: " + nuevoZombi.getTipo()+ ", ID: "+contadorZombisPartida+ ","+nuevoZombi.getId());
                  contadorZombisPartida++;
            }
        }
       this.turno = 1; 
        tablero.asignarSupervivientesAlTablero(supervivientesSeleccionados);
        while (enJuego) {
            System.out.println("Turno " + this.turno);
            System.out.println("FASE DE SUPERVIVIENTES: \n");
            for (Superviviente superviviente : supervivientesSeleccionados) {
                if (superviviente.isVivo()) {
                    realizarAccionesSupervivientes(superviviente,rutaAlmacenAtaques); 
                    comprobarFinDeJuego(supervivientesSeleccionados); 
                    if (!enJuego) { 
                        System.out.println("El juego ha finalizado. Regresando al menu principal...");
                        return;
                    }
                }
            }
            if (!enJuego) { 
                  System.out.println("El juego ha finalizado. Regresando al menu principal...");
                  return;
            }
            if (enJuego) { 
                System.out.println("FASE DE ACTIVACION DE ZOMBIS: \n");
                for (Zombi z : zombis) {
                      if (!z.isVivo()) {
                            continue; 
                        }else{
                           z.moverse(z, supervivientesSeleccionados, tablero);
                      }
                }
                System.out.println("\n");
                System.out.println("Estado del tablero:");
                tablero.mostrarTablero();
                System.out.println("------------------ \n");
            }
            System.out.println("Fin del turno " + turno);
            comprobarFinDeJuego(supervivientesSeleccionados);
            if (!enJuego) { // Si el juego ha terminado, salimos del bucle
                for (Superviviente s : supervivientesSeleccionados) {
                    //s.guardarEnArchivo();
                    s.guardarHistorico();
                    s.guardarActual();
                }
                for (Zombi zombi : zombis) {
                    String nombreArchivo = "src/zombi/zombi" + zombi.getId() + ".dat"; 
                    try {
                        guardarZombi(zombi, nombreArchivo);  
                        System.out.println("Zombi con ID " + zombi.getId() + " guardado en el archivo " + nombreArchivo);
                    } catch (IOException e) {
                        System.out.println("Error al guardar el zombi con ID " + zombi.getId() + ": " + e.getMessage());
                    }
                }
                guardarContadorZombis();
                guardarEstadoConNombre(nombrePartida);
                System.out.println("El juego ha finalizado. Regresando al menu principal...");

                return;
            }
            this.turno++;
            activarConsulta();
            String respuesta = "";
            while (!respuesta.equals("s") && !respuesta.equals("n")) {
                System.out.println("Deseas guardar el juego? (s/n): ");
                respuesta = scanner.nextLine().trim().toLowerCase();
                if (!respuesta.equals("s") && !respuesta.equals("n")) {
                    System.out.println("Entrada no valida. Por favor, escribe 's' para si o 'n' para no");
                }
            }
            if (respuesta.equals("s")) {
                guardarEstadoConNombre(nombrePartida);
                System.out.println("Juego guardado.");
            }
            respuesta = ""; 
            while (!respuesta.equals("s") && !respuesta.equals("n")) {
                System.out.println("Deseas finalizar la partida? (s/n): ");
                respuesta = scanner.nextLine().trim().toLowerCase();
                if (!respuesta.equals("s") && !respuesta.equals("n")) {
                    System.out.println("Entrada no valida. Por favor, escribe 's' para si o 'n' para no");
                }
            }
            if (respuesta.equals("s")) {
                enJuego = false;
                System.out.println("Has decidido finalizar la partida voluntariamente");
            }
            if (!enJuego) {
                for (Superviviente s : supervivientesSeleccionados) {
                    s.guardarHistorico();
                    s.guardarActual();
                }
               for (Zombi zombi : zombis) {
                   String nombreArchivo = "src/zombi/zombi" + zombi.getId() + ".dat"; 
                   try {
                       guardarZombi(zombi, nombreArchivo);  
                       System.out.println("Zombi con ID " + zombi.getId() + " guardado en el archivo " + nombreArchivo);
                    } catch (IOException e) {
                       System.out.println("Error al guardar el zombi con ID " + zombi.getId() + ": " + e.getMessage());
                    }
                }
                guardarContadorZombis();
                guardarEstadoConNombre(nombrePartida);
                System.out.println("El juego ha finalizado. Regresando al menu principal...");
                return;
            }
            for (Superviviente superviviente : supervivientesSeleccionados) {
                superviviente.reiniciarAcciones();
            }
            System.out.println("FASE DE APARICION: \n");
            for (int i = 1; i <= 1; i++) {
                Zombi nuevoZombi = tablero.generarZombiConProbabilidades(contadorZombisPartida, tablero);
                if (nuevoZombi != null) {
                    zombis.add(nuevoZombi); 
                    System.out.println("Zombi generado: " + nuevoZombi.getTipo()+ ", ID: "+contadorZombisPartida+ ","+nuevoZombi.getId());
                    contadorZombisPartida++;
                }
            }
        }
    }

    public void continuarJuego( String nombrePartida ) throws IOException {
        Scanner scanner = new Scanner(System.in);
        enJuego=true;
        activarConsulta();
        if (nombrePartida.isEmpty()) {
            nombrePartida = "Default";
        }
        System.out.println("Reanudando la partida desde el turno " + this.turno);
        String rutaAlmacenAtaques = DIRECTORIO_GUARDADO + nombrePartida + "_ataques.dat";
        if (rutaAlmacenAtaques == null || rutaAlmacenAtaques.isEmpty()) {
            System.out.println("Por favor, introduce el nombre del archivo de estado para cargar: ");
            rutaAlmacenAtaques = scanner.nextLine().trim();
        }
        try {
            for (int i = 1; i <= contadorZombisPartida; i++) {
                Zombi zombi = cargarZombi("src/zombi/zombi" + i + ".dat");
                zombisCargados.add(zombi);  
            }
            System.out.println("Zombis cargados correctamente.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar los zombis: " + e.getMessage());
        }
        cargarSupervivientes(); 
        cargarInventario(); 
        cargarContadorZombis(); 
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
            for (Superviviente superviviente : supervivientesSeleccionados) {
                if (superviviente.isVivo()) {
                    realizarAccionesSupervivientes(superviviente,rutaAlmacenAtaques);
                    comprobarFinDeJuego(supervivientesSeleccionados);
                     if (!enJuego) {
                        System.out.println("El juego ha finalizado. Regresando al menú principal...");
                        return;
                    }
                }
            }
            if (enJuego) {  
                System.out.println("FASE DE ACTIVACIoN DE ZOMBIS: \n");
                for (Zombi z : zombis) {
                    if (!z.isVivo()) {
                          continue; 
                    }else{
                       z.moverse(z, supervivientesSeleccionados, tablero);
                    }
                    System.out.println("\n");
                    System.out.println("Estado del tablero:");
                    tablero.mostrarTablero();
                }
            }
            System.out.println("Fin del turno " + turno);
            comprobarFinDeJuego(supervivientesSeleccionados);
            if (!enJuego) { // Si el juego ha terminado, salimos del bucle
                for (Superviviente s : supervivientesSeleccionados) {
                    s.guardarHistorico();
                    s.guardarActual();
                }
                for (Zombi zombi : zombis) {
                    String nombreArchivo = "src/zombi/zombi" + zombi.getId() + ".dat";  
                    try {
                        guardarZombi(zombi, nombreArchivo);  
                        System.out.println("Zombi con ID " + zombi.getId() + " guardado en el archivo " + nombreArchivo);
                    } catch (IOException e) {
                        System.out.println("Error al guardar el zombi con ID " + zombi.getId() + ": " + e.getMessage());
                    }
                }
                guardarContadorZombis();
                guardarEstadoConNombre(nombrePartida);
                System.out.println("El juego ha finalizado. Regresando al menu principal...");
                return;
            }
            this.turno++;
            activarConsulta();
            String respuesta = "";
            while (!respuesta.equals("s") && !respuesta.equals("n")) {
                System.out.println("Deseas guardar el juego? (s/n): ");
                respuesta = scanner.nextLine().trim().toLowerCase();
                if (!respuesta.equals("s") && !respuesta.equals("n")) {
                    System.out.println("Entrada no valida. Por favor, escribe 's' para si o 'n' para no");
                }
            }
            if (respuesta.equals("s")) {
                guardarEstadoConNombre(nombrePartida); // Guardar el estado del juego
                System.out.println("Juego guardado.");
            }
            respuesta = "";
            while (!respuesta.equals("s") && !respuesta.equals("n")) {
                System.out.println("Deseas finalizar la partida? (s/n): ");
                respuesta = scanner.nextLine().trim().toLowerCase();
                if (!respuesta.equals("s") && !respuesta.equals("n")) {
                    System.out.println("Entrada no valida. Por favor, escribe 's' para si o 'n' para no");
                }
            }
            if (respuesta.equals("s")) {
                enJuego = false;


                System.out.println("Has decidido finalizar la partida voluntariamente.");
            }
            if (!enJuego) {
                 for (Superviviente s : supervivientesSeleccionados) {
                     s.guardarHistorico();
                     s.guardarActual();
                }
            for (Zombi zombi : zombis) {
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
                System.out.println("El juego ha finalizado. Regresando al menu principal...");
                return;
            }
            for (Superviviente superviviente : supervivientesSeleccionados) {
                superviviente.reiniciarAcciones();
            }
            System.out.println("FASE DE APARICION: \n");
            for (int i = 1; i <= supervivientesSeleccionados.size(); i++) {
                Zombi nuevoZombi = tablero.generarZombiConProbabilidades(contadorZombisPartida, tablero);
                if (nuevoZombi != null) {
                    contadorZombisPartida++;
                    zombis.add(nuevoZombi); 
                    System.out.println("Zombi generado: " + nuevoZombi.getTipo()+ ", ID: "+nuevoZombi.getId());
                }
            }
        }
        System.out.println("El juego ha terminado. Regresando al menu principal...");
    }
    
    public void simularAtaqueSupervivienteAZombi() throws IOException{
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
        List<Superviviente> supervivientesSeleccionados = seleccionarSupervivientesSimulacion(almacenSupervivientes);
        if (supervivientesSeleccionados.isEmpty()) {
            System.out.println("No se seleccionaron supervivientes.");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        int tamaño;
        while (true) {
            System.out.println("Qué tamanio deseas para el tablero? (Ejemplo: 10 para un tablero 10x10): ");
            if (scanner.hasNextInt()) {
                tamaño = scanner.nextInt();
                scanner.nextLine(); 
                if (tamaño > 1 && tamaño < 11) {
                    break;  
                } else {
                    System.out.println("El tamanio debe estar entre 2 y 10. Intenta nuevamente");
                }
            } else {
                System.out.println("Entrada no valida. Debes ingresar un numero entero");
                scanner.nextLine(); 
            }
        }
        this.tablero = new Tablero(tamaño);
        if (supervivientesSeleccionados.isEmpty()) {
            System.out.println("No se han seleccionado supervivientes. El juego no puede continuar");
            this.enJuego = false; 
            return;
        } else {
            System.out.println("Se han seleccionado supervivientes");
        }
        List<Superviviente> supervivientesConCoordenadas = new ArrayList<>();
        for (Superviviente superviviente : supervivientesSeleccionados) {
            System.out.println("Ingresa la fila y columna para " + superviviente.getNombre() + ":");
            int fila = -1, columna = -1;
            while (true) {
                System.out.print("Fila: ");
                if (scanner.hasNextInt()) {
                    fila = scanner.nextInt();
                    scanner.nextLine();
                    if (fila >= 0 && fila < tamaño) {
                        break;
                    } else {
                        System.out.println("La fila es invalida. Debe estar dentro del rango del tablero");
                    }
                } else {
                    System.out.println("Entrada no valida. Debes ingresar un numero entero para la fila");
                    scanner.nextLine();
                }
            }
            while (true) {
                System.out.print("Columna: ");
                if (scanner.hasNextInt()) {
                    columna = scanner.nextInt();
                    scanner.nextLine(); 
                    if (columna >= 0 && columna < tamaño) {
                        break;
                    } else {
                        System.out.println("La columna es invalida. Debe estar dentro del rango del tablero");
                    }
                } else {
                    System.out.println("Entrada no valida. Debes ingresar un numero entero para la columna");
                    scanner.nextLine(); 
                }
            }
            Coordenada coordenada = new Coordenada(fila, columna);
            Superviviente supervivienteConCoordenada = new Superviviente(superviviente.getNombre(), coordenada);
            supervivientesConCoordenadas.add(supervivienteConCoordenada);
        }
    

        if (supervivientesConCoordenadas.isEmpty()) {
            System.out.println("No se seleccionaron supervivientes.");
            return;
        }
        this.supervivientesSeleccionados = supervivientesConCoordenadas;
        for (Superviviente superviviente : supervivientesConCoordenadas) {
            superviviente.setTablero(tablero); 
            superviviente.seleccionarEquipoManualmente(almacenInventario);
        }
        for (Superviviente superviviente : supervivientesConCoordenadas) {
            superviviente.reiniciarAcciones();
        }
        System.out.println("Cuantos zombis deseas agregar?");
        int numZombisAgregar1;
        while (true) {
            if (scanner.hasNextInt()) {
                numZombisAgregar1 = scanner.nextInt();
                scanner.nextLine();  
                if (numZombisAgregar1 > 0) { 
                    break;  
                } else {
                    System.out.println("El numero de zombis no puede ser negativo. Intenta nuevamente");
                }
            } else {
                System.out.println("Entrada no valida. Debes ingresar un numero entero");
                scanner.nextLine(); 
            }
        }
        for (int i = 0; i < numZombisAgregar1; i++) {
            Zombi nuevoZombi = tablero.generarZombiManual(tablero, id);
            if (nuevoZombi != null) {
                id++;
                zombis.add(nuevoZombi); 
                System.out.println("Zombi generado: " + nuevoZombi.getTipo());
            }
        }
        int turno = 1;
        tablero.asignarSupervivientesAlTablero(supervivientesConCoordenadas);
        while (enJuego) {
            System.out.println("Turno " + turno);
            for (Superviviente superviviente : supervivientesConCoordenadas) {
                if (superviviente.isVivo()) { 
                    realizarAccionesSupervivientesSimulacionAtaque(superviviente);
                    if (!superviviente.isVivo()) {
                        System.out.println(superviviente.getNombre() + " ha muerto");
                        enJuego = false;
                    }
                    if (!enJuego) { 
                        return;
                    }
                    System.out.println("Deseas finalizar la simulacion (s), continuar (n) o agregar un zombi (a)?");
                    String respuesta = scanner.nextLine().trim().toLowerCase();
                    if (respuesta.equals("n")) {
                        continue;
                    }
                    if (respuesta.equals("s")) {
                        enJuego = false;
                        System.out.println("Has decidido finalizar la simulación");
                        break;
                    }
                    if (respuesta.equals("a")) {
                        System.out.println("Cuantos zombis deseas agregar?");
                        int numZombisAgregar;
                        while (true) {
                            if (scanner.hasNextInt()) {
                                numZombisAgregar = scanner.nextInt();
                                scanner.nextLine();  
                                if (numZombisAgregar > 0) { 
                                    break;  
                                } else {
                                    System.out.println("El numero de zombis no puede ser negativo. Intenta nuevamente");
                                }
                            } else {
                                System.out.println("Entrada no valida. Debes ingresar un número entero");
                                scanner.nextLine();  
                            }
                        }
                        for (int i = 0; i < numZombisAgregar; i++) {
                            Zombi nuevoZombi = tablero.generarZombiManual(tablero, id);
                            if (nuevoZombi != null) {
                                id++;
                                zombis.add(nuevoZombi); 
                                System.out.println("Zombi generado: " + nuevoZombi.getTipo());
                            }
                        }
                    }
                    if (!enJuego) { 
                        return;
                    }
                } else {
                    System.out.println("Superviviente fallecido");
                    enJuego = false;
                    if (!enJuego) { 
                        return;
                    }
                }
                if (!enJuego) {
                    return;
                }
            }
            if (supervivientesSeleccionados.isEmpty() || zombis.isEmpty()) {
                enJuego = false;
                System.out.println("No hay mas supervivientes o zombis. La simulacion ha terminado");
            }
            for (Superviviente superviviente : supervivientesConCoordenadas) {
                superviviente.reiniciarAcciones();
            }
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
        cargarSupervivientes();
        cargarInventario();
        List<Superviviente> supervivientesSeleccionados = seleccionarSupervivientesSimulacion(almacenSupervivientes);
        if (supervivientesSeleccionados.isEmpty()) {
            System.out.println("No se seleccionaron supervivientes");
            return;
        }
        Scanner scanner = new Scanner(System.in);
            int tamaño;
                while (true) {
                System.out.println("Que tamanio deseas para el tablero? (Ejemplo: 10 para un tablero 10x10): ");
                if (scanner.hasNextInt()) {
                    tamaño = scanner.nextInt();
                    scanner.nextLine(); 
                    if (tamaño > 1 && tamaño < 11) {
                        break;  
                    } else {
                        System.out.println("El tamanio debe estar entre 2 y 10. Intenta nuevamente");
                    }
                } else {
                    System.out.println("Entrada no valida. Debes ingresar un numero entero");
                    scanner.nextLine();
                }
            }
        this.tablero = new Tablero(tamaño);

        if (supervivientesSeleccionados.isEmpty()) {
            System.out.println("No se han seleccionado supervivientes. El juego no puede continuar");
            this.enJuego = false; 
            return;
        } else {
            System.out.println("Se han seleccionado supervivientes.");
        }
        List<Superviviente> supervivientesConCoordenadas = new ArrayList<>();
        for (Superviviente superviviente : supervivientesSeleccionados) {
            System.out.println("Ingresa la fila y columna para " + superviviente.getNombre() + ":");
            int fila = -1, columna = -1;
            while (true) {
                System.out.print("Fila: ");
                if (scanner.hasNextInt()) {
                    fila = scanner.nextInt();
                    scanner.nextLine();
                    if (fila >= 0 && fila < tamaño) {
                        break;
                    } else {
                        System.out.println("La fila es invalida. Debe estar dentro del rango del tablero");
                    }
                } else {
                    System.out.println("Entrada no valida. Debes ingresar un numero entero para la fila");
                    scanner.nextLine(); 
                }
            }
            while (true) {
                System.out.print("Columna: ");
                if (scanner.hasNextInt()) {
                    columna = scanner.nextInt();
                    scanner.nextLine();
                    if (columna >= 0 && columna < tamaño) {
                        break;
                    } else {
                        System.out.println("La columna es invalida. Debe estar dentro del rango del tablero");
                    }
                } else {
                    System.out.println("Entrada no valida. Debes ingresar un numero entero para la columna");
                    scanner.nextLine(); 
                }
            }
            Coordenada coordenada = new Coordenada(fila, columna);
            Superviviente supervivienteConCoordenada = new Superviviente(superviviente.getNombre(), coordenada);
            supervivientesConCoordenadas.add(supervivienteConCoordenada);
        }
        if (supervivientesConCoordenadas.isEmpty()) {
            System.out.println("No se seleccionaron supervivientes.");
            return;
        }   
        this.supervivientesSeleccionados = supervivientesConCoordenadas;
        for (Superviviente superviviente : supervivientesConCoordenadas) {
            superviviente.setTablero(tablero); 
        }
        if (this.tablero != null) {
            System.out.println("\nEl tablero se ha inicializado correctamente");
        } else {
            System.out.println("Error al inicializar el tablero. El juego no puede continuar");
            this.enJuego = false;
            return;
        }
        int turno = 1;
        tablero.asignarSupervivientesAlTablero(supervivientesConCoordenadas);
        while (enJuego) {
            System.out.println("Turno " + turno);
            for (Superviviente superviviente : supervivientesConCoordenadas) {
                if (superviviente.isVivo()) { 
                    realizarAccionesSupervivientesSimulacionBusqueda(superviviente); 
                }
            }
            System.out.println("Deseas finalizar la simulacion? (s/n): ");
            String respuesta = scanner.nextLine().trim().toLowerCase();
            while (!respuesta.equals("s") && !respuesta.equals("n")) {
                System.out.println("Opcion no valida. Ingresa 's' para finalizar o 'n' para continuar");
                respuesta = scanner.nextLine().trim().toLowerCase();
            }
            if (respuesta.equals("s")) {
                enJuego = false;
                System.out.println("Has decidido finalizar la simulacion");
            }
            if (!enJuego) {
                return;
            }
            for (Superviviente superviviente : supervivientesConCoordenadas) {
                superviviente.reiniciarAcciones();
            }
            System.out.println("Fin del turno " + turno);
            turno++;
            activarConsulta();
        }
    }
    
    private void simularActivacionDeZombi() {
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
        List<Superviviente> supervivientesSeleccionados = seleccionarSupervivientesSimulacion(almacenSupervivientes);
        if (supervivientesSeleccionados.isEmpty()) {
            System.out.println("No se seleccionaron supervivientes");
            return;
        }
            Scanner scanner = new Scanner(System.in);
            int tamaño;
                while (true) {
                System.out.println("Que tamanio deseas para el tablero? (Ejemplo: 10 para un tablero 10x10): ");
                if (scanner.hasNextInt()) {
                    tamaño = scanner.nextInt();
                    scanner.nextLine(); 
                    if (tamaño > 1 && tamaño < 11) {
                        break;  
                    } else {
                        System.out.println("El tamanio debe estar entre 2 y 10. Intenta nuevamente");
                    }
                } else {
                    System.out.println("Entrada no valida. Debes ingresar un numero entero");
                    scanner.nextLine();
                }
            }
        this.tablero = new Tablero(tamaño);

        if (supervivientesSeleccionados.isEmpty()) {
            System.out.println("No se han seleccionado supervivientes. El juego no puede continuar");
            this.enJuego = false;  
            return;
        } else {
            System.out.println("Se han seleccionado supervivientes.");
        }
        List<Superviviente> supervivientesConCoordenadas = new ArrayList<>();
        for (Superviviente superviviente : supervivientesSeleccionados) {
            System.out.println("Ingresa la fila y columna para " + superviviente.getNombre() + ":");


            int fila = -1, columna = -1;
            while (true) {
                System.out.print("Fila: ");
                if (scanner.hasNextInt()) {
                    fila = scanner.nextInt();
                    scanner.nextLine();
                    if (fila >= 0 && fila < tamaño) {
                        break;
                    } else {
                        System.out.println("La fila es invalida. Debe estar dentro del rango del tablero");
                    }
                } else {
                    System.out.println("Entrada no valida. Debes ingresar un numero entero para la fila");
                    scanner.nextLine(); 
                }
            }
            while (true) {
                System.out.print("Columna: ");
                if (scanner.hasNextInt()) {
                    columna = scanner.nextInt();
                    scanner.nextLine(); 
                    if (columna >= 0 && columna < tamaño) {
                        break;
                    } else {
                        System.out.println("La columna es invalida. Debe estar dentro del rango del tablero");
                    }
                } else {
                    System.out.println("Entrada no valida. Debes ingresar un numero entero para la columna");
                    scanner.nextLine(); 
                }
            }
            Coordenada coordenada = new Coordenada(fila, columna);
            Superviviente supervivienteConCoordenada = new Superviviente(superviviente.getNombre(), coordenada);
            supervivientesConCoordenadas.add(supervivienteConCoordenada);
        }
        if (supervivientesConCoordenadas.isEmpty()) {
            System.out.println("No se seleccionaron supervivientes");
            return;
        }
        this.supervivientesSeleccionados = supervivientesConCoordenadas;
        for (Superviviente superviviente : supervivientesConCoordenadas) {
            superviviente.setTablero(tablero);
        }
        int numZombis = 1;
        for (int i = 0; i < numZombis; i++) {
            Zombi nuevoZombi = tablero.generarZombiManual(tablero,id);
            if (nuevoZombi != null) {
                id++;
                zombis.add(nuevoZombi); 
                System.out.println("Zombi generado: " + nuevoZombi.getTipo());
            }
        }
        int turno = 1;
        tablero.asignarSupervivientesAlTablero(supervivientesConCoordenadas);
        while (enJuego) {
            System.out.println("Turno " + turno);
            tablero.mostrarTableroSimulacion(tamaño); 
            System.out.println("\n Deseas consultar informacion del zombi?");
            System.out.println("1. Consultar zombi");
            System.out.println("2. Continuar con las acciones");
            System.out.print("Elige una opcion: ");
            if (scanner.hasNextInt()) { 
                int opcion = scanner.nextInt();
                scanner.nextLine(); 

                switch (opcion) {

                    case 1:
                        System.out.println("Zombis en el tablero:");
                        for (int i = 0; i < zombis.size(); i++) {
                            Zombi zombi = zombis.get(i);
                            System.out.println((i + 1) + ". Tipo: " + zombi.getTipo()+
                                               ", Aguante: " + zombi.getAguante() +
                                               ", Activación: " + (zombi.getActivaciones()));
                        }
                        System.out.print("Elige el numero del zombi para ver detalles (o 0 para volver): ");
                        if (scanner.hasNextInt()) {
                            int indiceZombi = scanner.nextInt() - 1;
                            scanner.nextLine();
                            if (indiceZombi >= 0 && indiceZombi < zombis.size()) {
                                Zombi zombiSeleccionado = zombis.get(indiceZombi);
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
                                System.out.println("Activacion: " + (zombiSeleccionado.getActivaciones()));
                            } else if (indiceZombi == -1) {
                                System.out.println("Volviendo al menu principal...");
                            } else {
                                System.out.println("Numero invalido. Por favor, selecciona un zombi de la lista");
                            }
                        } else {
                            System.out.println("Entrada invalida. Debes ingresar un numero");
                            scanner.nextLine(); 
                        }
                        break;
                    case 2:
                           for (Zombi z : zombis) {
                                z.actualizarListaSupervivientes(supervivientesSeleccionados);
                                z.acercarseAlSupervivienteSimulacion(z, supervivientesConCoordenadas, tablero);
                            }
                            System.out.println("\n ");
                            System.out.println("Deseas finalizar la simulacion (s), agregar un nuevo superviviente (a) o continuar (n)?");
                            String respuesta = scanner.nextLine().trim().toLowerCase();
                            while (!respuesta.equals("s") && !respuesta.equals("a") && !respuesta.equals("n")) {
                                System.out.println("Opcion no valida. Ingresa 's' para finalizar, 'a' para agregar un superviviente o 'n' para continuar");
                                respuesta = scanner.nextLine().trim().toLowerCase();
                            }

                            if (respuesta.equals("s")) {
                                enJuego = false;
                                System.out.println("Has decidido finalizar la simulacion");
                            } else if (respuesta.equals("a")) {
                                System.out.println("Agregar nuevo superviviente:");
                                List<Superviviente> supervivientesSeleccionados2 = seleccionarSupervivientesSimulacion(almacenSupervivientes);
                                if (supervivientesSeleccionados2.isEmpty()) {
                                    System.out.println("No se seleccionaron supervivientes.");
                                    return;
                                }
                                List<Superviviente> supervivientesConCoordenadas2 = new ArrayList<>();
                                for (Superviviente superviviente : supervivientesSeleccionados2) {
                                    System.out.println("Ingresa la fila y columna para " + superviviente.getNombre() + ":");
                                    int fila = -1, columna = -1;
                                    while (true) {
                                        System.out.print("Fila: ");
                                        if (scanner.hasNextInt()) {
                                            fila = scanner.nextInt();
                                            scanner.nextLine(); 
                                            if (fila >= 0 && fila < tamaño) {
                                                break;
                                            } else {
                                                System.out.println("La fila es invalida. Debe estar dentro del rango del tablero");
                                            }
                                        } else {
                                            System.out.println("Entrada no valida. Debes ingresar un nimero entero para la fila");
                                            scanner.nextLine(); 
                                        }
                                    }
                                    while (true) {
                                        System.out.print("Columna: ");
                                        if (scanner.hasNextInt()) {
                                            columna = scanner.nextInt();
                                            scanner.nextLine(); 
                                            if (columna >= 0 && columna < tamaño) {
                                                break;
                                            } else {
                                                System.out.println("La columna es invalida. Debe estar dentro del rango del tablero");
                                            }
                                        } else {
                                            System.out.println("Entrada no valida. Debes ingresar un numerp entero para la columna");
                                            scanner.nextLine(); 
                                        }
                                    }
                                    Coordenada coordenada = new Coordenada(fila, columna);
                                    Superviviente supervivienteConCoordenada = new Superviviente(superviviente.getNombre(), coordenada);
                                    supervivientesConCoordenadas2.add(supervivienteConCoordenada);
                                    supervivienteConCoordenada.setTablero(tablero);
                                }
                        this.supervivientesSeleccionados.addAll(supervivientesConCoordenadas2);
                        System.out.println("Nuevos supervivientes al juego.");
                        tablero.asignarSupervivientesAlTablero(supervivientesConCoordenadas2);
                        } else if (respuesta.equals("n")) {
                            System.out.println("Continuando con el siguiente turno...");
                            turno++;
                        }
                        break;
                    default: 
                        System.out.println("Opción inválida. Por favor, elige una opcion valida");
                }
            } else {
                System.out.println("Entrada invalida. Por favor, elige un número entre 1 y 3");
                scanner.nextLine(); 
            }
        }
    }

    private static void mostrarEstadisticas(Scanner scanner) throws ClassNotFoundException, IOException {
        System.out.println("Elige que deseas visualizar:");
        System.out.println("1. Zombis eliminados");
        System.out.println("2. Supervivientes heridos");
        System.out.println("3. Almacen de ataques"); 
        System.out.print("Elige una opcion: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();
        if (opcion == 1) {
            try {
                System.out.println("Elige el tipo de estadísticas de zombis eliminados:");
                System.out.println("1. Zombis eliminados historicamente");
                System.out.println("2. Zombis eliminados en esta partida");
                System.out.print("Elige una opcion: ");
                int tipo = scanner.nextInt();
                scanner.nextLine(); 
                if (tipo == 1) {
                    System.out.print("Introduce el nombre del superviviente: ");
                    String nombre = scanner.nextLine();
                    Superviviente superviviente2 = Superviviente.cargarHistorico(nombre);
                    List<Zombi> historico = superviviente2.getHistorico();
                    if (historico.isEmpty()) {
                        System.out.println("No hay zombis eliminados historicamente por " + nombre + ".");
                        return;
                    }
                    System.out.print("Deseas ver la lista ordenada por el ID del zombi? (S/N): ");
                    String ordenar = scanner.nextLine().trim().toUpperCase();
                    if (ordenar.equals("S")) {
                        historico.sort(Comparator.comparing(Zombi::getId));
                        System.out.println("Zombis eliminados historicamente por " + nombre + " (ordenados por ID):");
                    } else {
                        System.out.println("Zombis eliminados historicamente por " + nombre + ":");
                    }
                    for (Zombi z : historico) {
                        System.out.println(z);
                    }
                } else if (tipo == 2) {
                    Juego miJuego = new Juego();
                    if (miJuego.cargarEstadoConNombreCarga()) {
                        System.out.println("Introduce el nombre del superviviente para ver sus estadisticas:");
                        String nombreSuperviviente = scanner.nextLine().trim();
                        Superviviente superviviente = miJuego.obtenerSupervivientePorNombre(nombreSuperviviente);
                        if (superviviente == null) {
                            System.out.println("No se encontro un superviviente con ese nombre");
                            return;
                        }
                        List<Zombi> actual = superviviente.getZombisEliminados();
                        if (actual.isEmpty()) {
                            System.out.println("No hay zombis eliminados por " + nombreSuperviviente + " en esta partida");
                            return;
                        }
                        System.out.print("¿Deseas ver la lista ordenada por el ID del zombi? (S/N): ");
                        String ordenar = scanner.nextLine().trim().toUpperCase();
                        if (ordenar.equals("S")) {
                            actual.sort(Comparator.comparing(Zombi::getId)); 
                            System.out.println("Zombis eliminados en esta partida por " + nombreSuperviviente + " (ordenados por ID):");
                        } else {
                            System.out.println("Zombis eliminados en esta partida por " + nombreSuperviviente + ":");
                        }
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
            cargarZombis();  
            System.out.println("Cargando zombis...");
            if (zombisCargados.isEmpty()) {  
                System.out.println("No hay zombis cargados");
            }
            System.out.print("Introduce el ID del zombi: ");
            int zombiId = scanner.nextInt();
            boolean zombiEncontrado = false;
            for (Zombi zombi : zombisCargados) {
                if (zombi.getId() == zombiId) {
                    zombi.mostrarHeridosPorZombi(zombiId); 
                    zombiEncontrado = true;
                    break; 
                }
            }
            if (!zombiEncontrado) {
                System.out.println("Zombi con ID " + zombiId + " no encontrado");
            }
        }else if (opcion == 3) {
            Juego miJuego = new Juego();
            if (miJuego.cargarEstadoConNombreCarga()) {
                System.out.println("Estado del juego cargado correctamente");
                AlmacenAtaques almacen = miJuego.getAlmacenAtaques();  
                if (almacen != null) {
                    System.out.println("Numero de ataquesFINAL: " + almacen.getAtaques2().size());
                    for (Ataque ataque : almacen.getAtaques2()) {
                        System.out.println(ataque); 
                    }
                } else {
                    System.out.println("No se pudo cargar el almacen de ataques");
                }
            } else {
                System.out.println("No se pudo cargar el estado del juego");
            }
        } else {
            System.out.println("Opcion invalida");
        }
    }
    public AlmacenAtaques getAlmacenAtaques() {
        return ataque;
    }

    private void consultarInformacion(Superviviente supervivienteActual) {
        Scanner scanner = new Scanner(System.in);
        boolean consultaTerminada = false;
        while (!consultaTerminada) {
            System.out.println("\n Deseas consultar informacion de algún superviviente o zombi?");
            System.out.println("1. Consultar superviviente");
            System.out.println("2. Consultar zombi");
            System.out.println("3. Continuar con las acciones de " + supervivienteActual.getNombre());
            System.out.print("Elige una opcion: ");
            if (scanner.hasNextInt()) { 
                int opcion = scanner.nextInt();
                scanner.nextLine(); 
                switch (opcion) {
                    case 1: 
                        System.out.println("Supervivientes:");
                        for (int i = 0; i < supervivientesSeleccionados.size(); i++) {
                            System.out.println((i + 1) + ". " + supervivientesSeleccionados.get(i).getNombre());
                        }
                        System.out.print("Elige el número del superviviente para ver detalles: ");
                        if (scanner.hasNextInt()) {
                            int indiceSuperviviente = scanner.nextInt() - 1;
                            scanner.nextLine(); 
                            if (indiceSuperviviente >= 0 && indiceSuperviviente < supervivientesSeleccionados.size()) {
                                Superviviente superviviente = supervivientesSeleccionados.get(indiceSuperviviente);
                                System.out.println("Detalles del superviviente:");
                                System.out.println(superviviente);
                                if (superviviente.getArmasActivas() != null && !superviviente.getArmasActivas().isEmpty()) {
                                    System.out.println("Armas activas:");
                                    for (Arma arma : superviviente.getArmasActivas()) {
                                        System.out.println("- " + arma.getNombre());
                                    }
                                } else {
                                    System.out.println("No tiene armas activas");
                                }
                                if (superviviente.getInventario() != null && !superviviente.getInventario().isEmpty()) {
                                    System.out.println("Inventario:");
                                   for (Equipo equipo : superviviente.getInventario()) {
                                        System.out.println("- " + equipo.getNombre());
                                     }
                                } else {
                                    System.out.println("El inventario esta vacio");
                                }
                            } else {
                                System.out.println("Numero invalido. Por favor, selecciona un superviviente de la lista");
                            }
                        } else {
                            System.out.println("Entrada invalida. Debes ingresar un numero");
                            scanner.nextLine(); 
                        }
                        break;
                    case 2: 
                        System.out.println("Zombis en el tablero:");
                        for (int i = 0; i < zombis.size(); i++) {
                             Zombi zombi = zombis.get(i);

                            System.out.println((i + 1) + ". Tipo: " + zombi.getTipo()+
                                               ", Aguante: " + zombi.getAguante() +
                                               ", Activación: " + (zombi.getActivaciones()));
                        }
                        System.out.print("Elige el número del zombi para ver detalles (o 0 para volver): ");
                        if (scanner.hasNextInt()) {
                            int indiceZombi = scanner.nextInt() - 1;
                            scanner.nextLine(); 
                            if (indiceZombi >= 0 && indiceZombi < zombis.size()) {
                                Zombi zombiSeleccionado = zombis.get(indiceZombi);
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
                                System.out.println("Activacion: " + (zombiSeleccionado.getActivaciones()));
                            } else if (indiceZombi == -1) {
                                System.out.println("Volviendo al menu principal...");
                            } else {
                                System.out.println("Número invalido. Por favor, selecciona un zombi de la lista");
                            }
                        } else {
                            System.out.println("Entrada invalida. Debes ingresar un numero");
                            scanner.nextLine();
                        }
                        break;
                    case 3: 
                        consultaTerminada = true;
                        break;
                    default: 
                        System.out.println("Opcion invalida. Por favor, elige una opción valida");
                }
            } else {
                System.out.println("Entrada invalida. Por favor, elige un nimero entre 1 y 3");
                scanner.nextLine();
            }
        }
    }

    private void consultarInformacionSimulacion4(Superviviente supervivienteActual) {
        Scanner scanner = new Scanner(System.in);
        boolean consultaTerminada = false;
        while (!consultaTerminada) {
            System.out.println("\n Deseas consultar información de algun superviviente?");
            System.out.println("1. Consultar superviviente");
            System.out.println("2. Continuar con las acciones de " + supervivienteActual.getNombre());
            System.out.print("Elige una opción: ");
            if (scanner.hasNextInt()) { 
                int opcion = scanner.nextInt();
                scanner.nextLine(); 
                switch (opcion) {
                    case 1:
                        System.out.println("Supervivientes:");
                        for (int i = 0; i < supervivientesSeleccionados.size(); i++) {
                            System.out.println((i + 1) + ". " + supervivientesSeleccionados.get(i).getNombre());
                        }
                        System.out.print("Elige el numero del superviviente para ver detalles: ");
                        if (scanner.hasNextInt()) { 
                            int indiceSuperviviente = scanner.nextInt() - 1;
                            scanner.nextLine(); 
                            if (indiceSuperviviente >= 0 && indiceSuperviviente < supervivientesSeleccionados.size()) {
                                Superviviente superviviente = supervivientesSeleccionados.get(indiceSuperviviente);
                                System.out.println("Detalles del superviviente:");
                                System.out.println(superviviente);
                                if (superviviente.getArmasActivas() != null && !superviviente.getArmasActivas().isEmpty()) {
                                    System.out.println("Armas activas:");
                                    for (Arma arma : superviviente.getArmasActivas()) {
                                        System.out.println("- " + arma.getNombre());
                                    }
                                } else {
                                    System.out.println("No tiene armas activas");
                                }
                                if (superviviente.getInventario() != null && !superviviente.getInventario().isEmpty()) {
                                    System.out.println("Inventario:");
                                   for (Equipo equipo : superviviente.getInventario()) {
                                        System.out.println("- " + equipo.getNombre());
                                     }
                                } else {
                                    System.out.println("El inventario esta vacio");
                                }
                            } else {
                                System.out.println("Numero invalido. Por favor, selecciona un superviviente de la lista");
                            }
                        } else {
                            System.out.println("Entrada invalida. Debes ingresar un numero");
                            scanner.nextLine();
                        }
                        break;
                    case 2: 
                        consultaTerminada = true;
                        break;
                    default: 
                        System.out.println("Opcion invalida. Por favor, elige una opción valida");
                }
            } else {
                System.out.println("Entrada invalida. Por favor, elige un numero entre 1 y 3");
                scanner.nextLine(); 
            }
        }
    }

    private void mostrarEstadoJuego() {
        System.out.println("Estado del tablero:");
        tablero.mostrarTablero(); 
        System.out.println("Supervivientes:");
        for (Superviviente superviviente : supervivientesSeleccionados) {
            System.out.println(superviviente); 
        }
    }

    private void desactivarConsulta() {
        consultaHabilitada = false;
    }
    
    private void activarConsulta() {
        consultaHabilitada = true;
    }
   
    public void configurarTableroParaSupervivientes() {
        for (Superviviente superviviente : supervivientes) {
            superviviente.setTablero(this.tablero);
        }
    }

    public void comprobarFinDeJuego(List<Superviviente> supervivientesSeleccionados) {
        int supervivientesEnObjetivo = 0;  
        int supervivientesConProvisiones = 0;  
        boolean algunoEliminado = false; 
        for (int i = 0; i < supervivientesSeleccionados.size(); i++) {
            Superviviente superviviente = supervivientesSeleccionados.get(i);  
            int cantidadProvisiones = 0;
            if (!superviviente.isVivo()) {
                algunoEliminado = true;  
                System.out.println(superviviente.getNombre() + " ha sido eliminado");
                break; 
            }
            for (Equipo equipo : superviviente.getInventario()) {
                if (equipo instanceof Provisiones) {
                    cantidadProvisiones++;
                }
            }
            if (superviviente.getCoordenadas().equals(tablero.getObjetivo())) {
                supervivientesEnObjetivo++; 
                System.out.println(superviviente.getNombre() + " ha llegado al objetivo");
            }
            if (cantidadProvisiones >= 1) {
                supervivientesConProvisiones++;
            }
           if (supervivientesEnObjetivo ==  supervivientesSeleccionados.size() && supervivientesConProvisiones ==  supervivientesSeleccionados.size()) {
               enJuego = false;
               System.out.println("Todos los supervivientes han llegado al objetivo con provisiones! Han ganado!");
               System.exit(0);  
           }
           if (algunoEliminado) {
                enJuego = false;
                System.out.println("El juego ha terminado porque un superviviente ha sido eliminado");
                return;
            }
        }
        if (algunoEliminado) {
            enJuego = false;
            System.out.println("El juego ha terminado porque un superviviente ha sido eliminado");
            return;
        }
        if (supervivientesEnObjetivo == 2 && supervivientesConProvisiones == 2) {
            enJuego = false;
            System.out.println("Todos los supervivientes han llegado al objetivo con provisiones! Han ganado!");
            System.exit(0);  
        }
    }
    
    public void listarArchivosGuardados() {
        File carpeta = new File(DIRECTORIO_GUARDADO);
        File[] archivos = carpeta.listFiles((dir, name) -> name.toLowerCase().endsWith(".dat") && !name.contains("_ataques"));
        if (archivos != null && archivos.length > 0) {
            System.out.println("Archivos disponibles para cargar:");
            for (int i = 0; i < archivos.length; i++) {
                System.out.println((i + 1) + ". " + archivos[i].getName());
            }
        } else {
            System.out.println("No hay archivos disponibles para cargar");
        }
    }

    public void guardarZombi(Zombi zombi, String archivo) throws IOException {
        File file = new File(archivo);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs(); 
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(zombi); 
        } catch (IOException e) {
            System.out.println("Error al guardar el zombi con ID " + zombi.getId() + " en el archivo: " + archivo + ". Error: " + e.getMessage());
            throw e;
        }
    }
    
    public static Zombi cargarZombi(String archivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (Zombi) ois.readObject();
        }
    }
    
    public void cargarSupervivientes() {
        almacenSupervivientes = tablero.cargarSupervivientesDesdeFichero("src/supervivientes.txt");
    }
    
    public void cargarInventario() {
        System.out.println("Iniciando carga del inventario...");
        try {
            almacenInventario.cargarDesdeFichero("src/inventario.txt");
            Set<Arma> armas = almacenInventario.getArmas();  
            Set<Provisiones> provisiones = almacenInventario.getProvisiones(); 
            if (!armas.isEmpty()) {
                System.out.println("Armas cargadas:");
                for (Arma arma : armas) {
                    System.out.println("- " + arma.getNombre());
                }
            } else {
                System.out.println("No se cargaron armas");
            }
            if (!provisiones.isEmpty()) {
                System.out.println("Provisiones cargadas:");
                for (Provisiones provision : provisiones) {
                    System.out.println("- " + provision.getNombre() + " (Cantidad: " + provision.getValorEnergetico() + ")");
                }
            } else {
                System.out.println("No se cargaron provisiones");
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
                Zombi zombi = cargarZombi("src/zombi/zombi" + i + ".dat");  
                zombisCargados.add(zombi); 
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error al cargar el zombi con ID " + i + ": " + e.getMessage());
            }
        }
    }

    public void guardarEstadoConNombre(String nombreArchivo) {
        crearDirectorioSiNoExiste(); 
        if (nombreArchivo.isEmpty()) {
            System.out.println("Nombre invalido. Guardando con el nombre predeterminado 'estadoJuego.dat'.");
            nombreArchivo = "estadoJuego";
        }
        String rutaCompleta = DIRECTORIO_GUARDADO + nombreArchivo + ".dat";
        String rutaCompletaAtaques = DIRECTORIO_GUARDADO + nombreArchivo + "_ataques.dat";
        try {
            guardarJuego(this, rutaCompleta);
            System.out.println("Juego guardado con exito como: " + nombreArchivo + ".dat");
            List<Ataque> listaDeAtaques = ataque.getAtaques2(); 
            System.out.println("Numero de ataques a guardar: " + listaDeAtaques.size());
            guardarListaAtaques(listaDeAtaques, rutaCompletaAtaques); 
            System.out.println("Almacen de ataques guardado en: " + rutaCompletaAtaques);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al guardar el estado del juego");
        }
    }

    public void guardarAlmacenAtaques(AlmacenAtaques almacen, String ruta) {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ruta))) {
            salida.writeObject(almacen);
        } catch (IOException e) {
            System.out.println("Error al guardar el almacen de ataques: " + e.getMessage());
        }
    }
    
    public void guardarListaAtaques(List<Ataque> listaAtaques, String ruta) {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ruta))) {
            salida.writeObject(listaAtaques);  
            System.out.println("Lista de ataques guardada correctamente en: " + ruta);
        } catch (IOException e) {
            System.out.println("Error al guardar la lista de ataques: " + e.getMessage());
        }
    }
    
    public List<Ataque> cargarListaAtaques(String ruta) {
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ruta))) {
            return (List<Ataque>) entrada.readObject(); 
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar la lista de ataques: " + e.getMessage());
            return new ArrayList<>(); 
        }
    }

    public boolean cargarEstadoConNombreCarga() throws IOException, ClassNotFoundException, ClassCastException {
    listarArchivosGuardados();
    Scanner scanner = new Scanner(System.in);
    System.out.println("Introduce el numero del archivo que deseas cargar:");
    int opcion = scanner.nextInt();
    File carpeta = new File(DIRECTORIO_GUARDADO);
    File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".dat"));
    if (opcion <= 0 || opcion > archivos.length) {
        System.out.println("Opcion no valida. Por favor, elige un numero dentro del rango");
        return false;
    }
    String nombreArchivo = archivos[opcion - 1].getName();
    if (!nombreArchivo.endsWith(".dat")) {
        nombreArchivo += ".dat";
    }
    String nombrePartida = nombreArchivo.replace(".dat", "");  
    String rutaCompletaJuego = DIRECTORIO_GUARDADO + File.separator + nombreArchivo;
    String rutaCompletaAtaques = DIRECTORIO_GUARDADO + File.separator + nombreArchivo.replace(".dat", "_ataques.dat");
    Object obj = cargarJuego(rutaCompletaJuego);
    if (obj == null) {
        System.out.println("El archivo esta vacío o no se pudo leer.");
        return false;
    }
    if (!(obj instanceof Juego)) {
        System.out.println("El archivo no contiene un objeto de tipo 'Juego'. Tipo encontrado: " + obj.getClass().getName());
        return false;
    }
    Juego estadoCargado = (Juego) obj;
    this.supervivientesSeleccionados = estadoCargado.getSupervivientesSeleccionados();
    this.zombis = estadoCargado.zombis;
    this.tablero = estadoCargado.tablero;
    this.turno = estadoCargado.turno;
    this.enJuego = estadoCargado.enJuego;
    System.out.println("Juego cargado con exito desde: " + rutaCompletaJuego);
    List<Ataque> listaDeAtaques = cargarListaAtaques(rutaCompletaAtaques);
    System.out.println("Lista de ataques cargada correctamente desde: " + rutaCompletaAtaques);
    System.out.println("Numero de ataques cargados: " + listaDeAtaques.size());
    ataque.setAtaques2(listaDeAtaques);
    continuarJuego(nombrePartida);  
    return true; 
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
               System.out.println("Juego guardado con exito");
           } catch (IOException e) {
               e.printStackTrace();
               System.out.println("Error al guardar el juego");
           }
       }
 
    public static Object cargarJuego(String nombreArchivo) {
        Object objetoCargado = null;
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(nombreArchivo))) {
            objetoCargado = entrada.readObject(); 
            System.out.println("Juego cargado con exito");
        } catch (FileNotFoundException e) {
            System.out.println("El archivo no existe: " + nombreArchivo);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error al cargar el juego");
        }
        return objetoCargado;
    }
    
    private Superviviente obtenerSupervivientePorNombre(String nombreSuperviviente) {
        for (Superviviente s : this.supervivientesSeleccionados) {
            if (s.getNombre().equalsIgnoreCase(nombreSuperviviente)) {
                return s; 
            }
        }
        return null; 
    }

    public void mostrarZombisEliminados(String nombreSuperviviente) {
        Superviviente superviviente = null;
        for (Superviviente s : supervivientesSeleccionados) {
            if (s.getNombre().equalsIgnoreCase(nombreSuperviviente)) {
                superviviente = s;
                break;
            }
        }
        if (superviviente == null) {
            System.out.println("No se encontro un superviviente con ese nombre");
            return;
        }
        List<Zombi> zombisEliminados = superviviente.getZombisEliminados();
        if (zombisEliminados.isEmpty()) {
            System.out.println("Este superviviente no ha eliminado zombis");
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
                System.out.println("No se encontro archivo, contador iniciado a 0");
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
    
    public static AlmacenAtaques cargarAlmacen(String archivo) throws IOException, ClassNotFoundException {
      File file = new File(archivo);
      if (!file.exists()) {
          System.out.println("El archivo no existe: " + archivo);
          return new AlmacenAtaques();
      }
      try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
          System.out.println("Cargando almacen desde: " + archivo);
          AlmacenAtaques almacen = (AlmacenAtaques) ois.readObject();
          System.out.println("Almacen cargado con exito.");
          return almacen;
      } catch (IOException | ClassNotFoundException e) {
          System.out.println("Error al cargar el almacen: " + e.getMessage());
          throw e;
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

