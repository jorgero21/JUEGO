/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package a.poo.calipsis.zombi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.util.Scanner;

public class Superviviente implements Activable, Serializable {
    
    private String nombre;
    private boolean vivo; // "VIVO" o "ELIMINADO"
    private int zombis_eliminados;
    private int heridas_recibidas;
    private static final int MAX_HERIDAS = 2;
    private static final int MAX_ACCIONES = 3;
    private static final int MAX_INVENTARIO=5;
    private int acciones=MAX_ACCIONES;
    private List<Arma> armasActivas;
    private Coordenada coordenada; // Coordenada del superviviente
    private Tablero tablero;
    private Juego juego;
    private List<Equipo> inventario;
    private String tipoDañado;
    private List<Zombi> historico = new ArrayList<>();
    private List<Zombi> actual = new ArrayList<>();
    private List<TipoDeHerida> heridasRecibidas; 
    private Set<Equipo> equiposEntregados = new HashSet<>();
    private List<Zombi> zombisEliminados = new ArrayList<>();
    
    //CONSTRUCTORES
    
    public Superviviente(String nombre) {
        this.nombre = nombre;
        this.vivo = true;
        this.inventario = new ArrayList<>();
        this.heridasRecibidas = new ArrayList<>();
        this.armasActivas = new ArrayList<>();  
        this.zombis_eliminados = 0;
        this.heridas_recibidas = 0;
        this.coordenada = new Coordenada(0, 0); // Coordenada inicial (0, 0)
    }
    
    public Superviviente(String nombre, Coordenada coordenada) {
        this.nombre = nombre;
        this.vivo = true;
        this.inventario = new ArrayList<>();
        this.armasActivas = new ArrayList<>();
        this.zombis_eliminados = 0;
        this.heridas_recibidas = 0;
        this.heridasRecibidas = new ArrayList<>();
        this.coordenada = coordenada; // Coordenada que se pasa al constructor
    }

  public void guardarAlmacen(AlmacenAtaques almacen, String rutaPorDefecto, String rutaAlmacenAtaques) throws IOException {
    // Determinar qué ruta utilizar
    String ruta = (rutaAlmacenAtaques != null && !rutaAlmacenAtaques.isEmpty()) 
                  ? rutaAlmacenAtaques 
                  : rutaPorDefecto;

    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
        oos.writeObject(almacen); // Serializar el almacén
        System.out.println("Almacén de ataques guardado exitosamente en: " + ruta);
    } catch (IOException e) {
        System.out.println("Error al guardar el almacén de ataques: " + e.getMessage());
        throw e; // Rethrow para manejarlo en el nivel superior
    }
}

      
    public enum TipoDeHerida {
        MORDEDURA,
        HERIDA
    }
    
    //GET Y SETS
    
    public String getNombre() {
        return nombre;
    }

    
    public List<Zombi> getZombisEliminados() {
        return zombisEliminados;
    }
    
     public void agregarZombisEliminados(Zombi zombi) {
    if (this.zombisEliminados == null) {
        this.zombisEliminados = new ArrayList<>();
    }
    this.zombisEliminados.add(zombi);
}

     
    public int getZombis_eliminados() {
        return zombis_eliminados;
    }

    public int getHeridas_recibidas() {
        return heridas_recibidas;
    }

    public void setZombis_eliminados(int zombis_eliminados) {
        this.zombis_eliminados = zombis_eliminados;
    }

    public void setHeridas_recibidas(int heridas_recibidas) {
        this.heridas_recibidas = heridas_recibidas;
    }

    public boolean isVivo() {
        return vivo;
    }

    public void setVivo(boolean vivo) {
        this.vivo = vivo;
    }
    
    public int getAcciones() {
        return acciones;
    }
    
    public Set<Arma> getArmas() {
        Set<Arma> armas = new HashSet<>();
        for (Equipo equipo : inventario) {
            if (equipo instanceof Arma) {
                armas.add((Arma) equipo);
            }
        }
        return armas;
    }

    public Set<Provisiones> getProvisiones() {
        Set<Provisiones> provisiones = new HashSet<>();
        for (Equipo equipo : inventario) {
            if (equipo instanceof Provisiones) {
                provisiones.add((Provisiones) equipo);
            }
        }
        return provisiones;
    }

    public List<Arma> getArmasActivas() {
        return armasActivas;
    }

    public List<Equipo> getInventario() {
        return inventario;
    }

    public void setTablero(Tablero tablero) {
           this.tablero = tablero;
       }
    
    
    public List<TipoDeHerida> getHeridasRecibidas() {
        return heridasRecibidas;
    }
      
    public String getTipoLesion() {
        return tipoDañado;
    }

    public List<Zombi> getHistorico() {
        return historico;
    }

    public List<Zombi> getActual() {
        return actual;
    }

    
    // METODOS ACTIVABLE
    
    //ME FALTA QUE SI HAY UN ZOMBI EN LA CASILLA CON SUPERVIVIENTE
    //QUE CUESTE UN MOVIMIENTO EXTRA
    
     
  /*  public void moverse() {
        if (this.tablero == null) { // Validar que el tablero no sea null
            System.out.println("Error: El tablero no está asociado al superviviente.");
            return;
        }
        Coordenada nuevaPosicion = new Coordenada(coordenada.getFila(), coordenada.getColumna());
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hacia donde quieres moverte? (arriba/abajo/izquierda/derecha):");
        String direccion = scanner.nextLine();

        switch (direccion.toLowerCase()) {
            case "arriba":
                nuevaPosicion.setColumna(nuevaPosicion.getColumna() + 1);
                break;
            case "abajo":
                nuevaPosicion.setColumna(nuevaPosicion.getColumna() - 1);
                break;
            case "izquierda":
                nuevaPosicion.setFila(nuevaPosicion.getFila() - 1);
                break;
            case "derecha":
                nuevaPosicion.setFila(nuevaPosicion.getFila() + 1);
                break;
            default:
                System.out.println("Direccion no valida. Intenta de nuevo.");
                return;
        }

        System.out.println("Intentando mover a: " + nuevaPosicion);

        // Validar la nueva posición utilizando el tablero asociado
        if (this.tablero.esPosicionValida(nuevaPosicion)) {
            tablero.actualizarTablero(this, coordenada, nuevaPosicion);
            this.coordenada = nuevaPosicion;

            System.out.println(this.nombre + " se ha movido a " + nuevaPosicion);
            restarAccion();
        } else {
            System.out.println("Movimiento no valido. Estas tratando de salir del tablero.");
        }
    }
    
    */
    @Override
    public void moverse() {
    if (this.tablero == null) { // Validar que el tablero no sea null
        System.out.println("Error: El tablero no está asociado al superviviente.");
        return;
    }
    
    // Contar los zombis en la casilla de origen
    int zombisEnCasillaOrigen = tablero.contarZombisEnCasilla(coordenada);
    System.out.println("Zombis en la casilla origen: " + zombisEnCasillaOrigen);
    // Obtener la casilla origen

    // Calcular el costo adicional por los zombis presentes en la casilla origen
    int costoTotal = 1 + zombisEnCasillaOrigen; // 1 acción base + 1 acción extra por zombi
    if (this.acciones < costoTotal) { // Verificar si el superviviente tiene suficientes acciones
        System.out.println("No tienes suficientes acciones para moverte. Requiere " + costoTotal + " acciones.");
        return;
    }else{
         System.out.println("Puedes moverte");
    }

    // Determinar la nueva posición
    Coordenada nuevaPosicion = new Coordenada(coordenada.getFila(), coordenada.getColumna());
    Scanner scanner = new Scanner(System.in);
    System.out.println("¿Hacia dónde quieres moverte? (arriba/abajo/izquierda/derecha):");
    String direccion = scanner.nextLine();

    switch (direccion.toLowerCase()) {
        case "arriba":
            nuevaPosicion.setColumna(nuevaPosicion.getColumna() + 1);
            break;
        case "abajo":
            nuevaPosicion.setColumna(nuevaPosicion.getColumna() - 1);
            break;
        case "izquierda":
            nuevaPosicion.setFila(nuevaPosicion.getFila() - 1);
            break;
        case "derecha":
            nuevaPosicion.setFila(nuevaPosicion.getFila() + 1);
            break;
        default:
            System.out.println("Dirección no válida. Intenta de nuevo.");
            return;
    }

    System.out.println("Intentando mover a: " + nuevaPosicion);

    // Validar la nueva posición utilizando el tablero asociado
    if (this.tablero.esPosicionValida(nuevaPosicion)) {
        // Realizar el movimiento
        tablero.actualizarTablero(this, coordenada, nuevaPosicion);
        this.coordenada = nuevaPosicion;

        // Restar las acciones necesarias
        for (int i = 0; i < costoTotal; i++) {
            restarAccion();
        }

        System.out.println(this.nombre + " se ha movido a " + nuevaPosicion + " gastando " + costoTotal + " acciones.");
    } else {
        System.out.println("Movimiento no válido. Estás tratando de salir del tablero.");
    }
}

     // ESTO NO HACE NADA, RETOCAR
    @Override
    public void atacar() {
        if (!armasActivas.isEmpty()) {
            System.out.println(nombre + " está atacando con " + armasActivas.get(0).getNombre());
        } else {
            System.out.println(nombre + " no tiene un arma equipada.");
        }
    }
    
public void atacar(Tablero tablero, AlmacenAtaques almacen, String rutaAlmacenAtaques) throws IOException {
    if (armasActivas.isEmpty()) {
        System.out.println("No tienes armas activas. Usa el comando 'elegirArmaActiva' para activar una.");
        return;
    }

    // Mostrar las armas activas y permitir elegir una
    Scanner scanner = new Scanner(System.in);
    System.out.println("Selecciona un arma activa para atacar:");

    for (int i = 0; i < armasActivas.size(); i++) {
        System.out.println((i + 1) + ". " + armasActivas.get(i).getNombre() + 
                           " (Alcance: " + armasActivas.get(i).getAlcance() + 
                           ", Dados: " + armasActivas.get(i).getNumeroDados() + 
                           ", Potencia: " + armasActivas.get(i).getPotencia() + ")");
    }

    int seleccion = -1;
    while (seleccion < 1 || seleccion > armasActivas.size()) {
        System.out.print("Ingresa el número correspondiente al arma: ");
        // Validar que se ingrese un número
        if (scanner.hasNextInt()) {
            seleccion = scanner.nextInt();
            if (seleccion < 1 || seleccion > armasActivas.size()) {
                System.out.println("Selección inválida. Elige un número entre 1 y " + armasActivas.size() + ".");
            }
        } else {
            System.out.println("Entrada inválida. Por favor, ingresa un número.");
            scanner.next(); // Limpiar el buffer del scanner
        }
    }

    Arma armaSeleccionada = armasActivas.get(seleccion - 1);

    // Crear un nuevo ataque
    Ataque ataque = new Ataque(armaSeleccionada.getNumeroDados());
    int exitos = ataque.lanzarDados(armaSeleccionada.getValorExito()); // Lanzar los dados y calcular éxitos

    if (exitos > 0) {
        System.out.println("Se han obtenido " + exitos + " éxitos.");

        // Procesar cada éxito individualmente
       // Procesar cada éxito individualmente
while (exitos > 0) {
    // Preguntar si el jugador desea usar el éxito
    String decision = "";
    while (true) {
        System.out.println("¿Deseas usar el siguiente éxito para atacar? (s/n)");
        decision = scanner.next();

        if (decision.equalsIgnoreCase("s")) {
        // Selección de la casilla objetivo
        System.out.println("Introduce las coordenadas de la casilla objetivo:");
        tablero.mostrarTablero();

        // Validar que la fila sea un número entero
        int fila = -1;
        while (fila < 0) {
            System.out.print("Fila: ");
            if (scanner.hasNextInt()) {
                fila = scanner.nextInt();
                if (fila < 0) {
                    System.out.println("Por favor, introduce un número positivo para la fila.");
                }
            } else {
                System.out.println("Entrada inválida. Debes ingresar un número.");
                scanner.next(); // Limpiar el buffer del scanner
            }
        }

        // Validar que la columna sea un número entero
        int columna = -1;
        while (columna < 0) {
            System.out.print("Columna: ");
            if (scanner.hasNextInt()) {
                columna = scanner.nextInt();
                if (columna < 0) {
                    System.out.println("Por favor, introduce un número positivo para la columna.");
                }
            } else {
                System.out.println("Entrada inválida. Debes ingresar un número.");
                scanner.next(); // Limpiar el buffer del scanner
            }
        }

        Casilla casillaObjetivo = tablero.getCasilla(fila, columna);

        if (casillaObjetivo == null) {
            System.out.println("La casilla objetivo no existe. Intenta de nuevo.");
            continue; // Volver a intentar
        }

        // Verificar si la casilla está dentro del alcance del arma
        int distancia = tablero.calcularDistancia(this.coordenada, casillaObjetivo.getCoordenadas());
        if (armaSeleccionada.getAlcance() == 0 && distancia > 0) {
            System.out.println("El arma es de cuerpo a cuerpo. Debes atacar una casilla donde estés.");
            continue; // Volver a intentar
        }
        if (armaSeleccionada.getAlcance() > 0 && distancia > armaSeleccionada.getAlcance()) {
            System.out.println("La casilla está fuera del alcance del arma.");
            continue; // Volver a intentar
        }

        // Verificar si hay zombis en la casilla
        List<Zombi> zombisEnCasilla = new ArrayList<>();
        for (Object entidad : casillaObjetivo.getEntidades()) {
            if (entidad instanceof Zombi) {
                zombisEnCasilla.add((Zombi) entidad);
            }
        }

        if (zombisEnCasilla.isEmpty()) {
            System.out.println("No hay zombis en la casilla seleccionada.");
            continue; // Volver a intentar
        }

        // Realizar el ataque para cada zombi en la casilla
        for (Zombi z : zombisEnCasilla) {
            // Si es un zombi Berserker y el arma es a distancia, ignorar
            if (z.isBerserker() && armaSeleccionada.getAlcance() > 0) {
                this.restarAccion();
                System.out.println("El zombi Berserker es inmune a ataques a distancia.");
                exitos--; // Restar un éxito aunque no se ataque al zombi
                continue;
            }

            // Si el zombi puede ser eliminado
            if (z.getAguante() <= armaSeleccionada.getPotencia()) {
                this.restarAccion();
                System.out.println("El zombi " + z.getTipo() + " ha sido eliminado.");
                agregarZombiEliminado(z);
                exitos--;
                z.setVivo(false);
                // Comprobar si el zombi es tóxico y si está en la misma casilla
                if (z.isToxico() && this.coordenada.equals(casillaObjetivo.getCoordenadas())) {
                    System.out.println("El zombi tóxico te ha causado una herida al ser eliminado!");
                    recibirHerida(z);
                }
                casillaObjetivo.eliminarEntidad(z); // Eliminar el zombi de la casilla
                tablero.mostrarTablero();
                agregarZombisEliminados(z);
                zombis_eliminados++;
                almacen.registrarAtaque(ataque, rutaAlmacenAtaques);
                System.out.println("Número de ataques: " + almacen.getAtaques2().size());
            } else {
                System.out.println("El zombi " + z.getTipo() + " es demasiado resistente para este ataque.");
                this.restarAccion();
                exitos--; // Restar un éxito aunque no sea derrotado
            }

            // Detenerse si no hay más éxitos disponibles
            if (exitos == 0) {
                break;
            }
        }

        // Si no hay más éxitos, terminar el ataque
        if (exitos == 0) {
            System.out.println("No hay más éxitos disponibles.");
        }
    }
 else if (decision.equalsIgnoreCase("n")) {
            // Si el jugador selecciona "n", no hacer nada y restar el éxito
            exitos--;  
            System.out.println("El éxito no se utilizará.");
            break; // Sale del bucle de decisiones y pasa al siguiente éxito
        } else {
            // Si la entrada es inválida, mostrar mensaje y pedir una nueva entrada
            System.out.println("Entrada inválida. Por favor, ingresa 's' para usar el éxito o 'n' para no usarlo.");
        }
    }

    // Si el jugador eligió "s", se procede con la selección de la casilla objetivo
}

    } else {
        System.out.println("No se consiguieron éxitos. El ataque ha fallado.");
    }
}


        public void atacarSimulacion(Tablero tablero, AlmacenAtaques almacen, Juego j) throws IOException {
        if (armasActivas.isEmpty()) {
            System.out.println("No tienes armas activas. Usa el comando 'elegirArmaActiva' para activar una.");
            return;
        }

        // Mostrar las armas activas y permitir elegir una
        Scanner scanner = new Scanner(System.in);
        System.out.println("Selecciona un arma activa para atacar:");

        for (int i = 0; i < armasActivas.size(); i++) {
            System.out.println((i + 1) + ". " + armasActivas.get(i).getNombre() + 
                               " (Alcance: " + armasActivas.get(i).getAlcance() + 
                               ", Dados: " + armasActivas.get(i).getNumeroDados() + 
                               ", Potencia: " + armasActivas.get(i).getPotencia() + ")");
        }

        int seleccion = scanner.nextInt();

        if (seleccion < 1 || seleccion > armasActivas.size()) {
            System.out.println("Selección inválida. Ataque cancelado.");
            return;
        }

        Arma armaSeleccionada = armasActivas.get(seleccion - 1);

        // Crear un nuevo ataque
        Ataque ataque = new Ataque(armaSeleccionada.getNumeroDados());
        int exitos = ataque.lanzarDados(armaSeleccionada.getValorExito()); // Lanzar los dados y calcular éxitos

        if (exitos > 0) {
            System.out.println("Se han obtenido " + exitos + " éxitos.");

            // Procesar cada éxito individualmente
            while (exitos > 0) {
                // Preguntar si el jugador desea usar el éxito
                System.out.println("¿Deseas usar el siguiente éxito para atacar? (s/n)");
                String decision = scanner.next();

                if (decision.equalsIgnoreCase("n")) {
                    System.out.println("El éxito no se utilizará.");
                    exitos--;
                     // Restar un éxito sin usarlo
                    continue; // Pasar al siguiente éxito
                }

                // Selección de la casilla objetivo
                System.out.println("Introduce las coordenadas de la casilla objetivo:");
                tablero.mostrarTablero();
                System.out.print("Fila: ");
                int fila = scanner.nextInt();
                System.out.print("Columna: ");
                int columna = scanner.nextInt();

                Casilla casillaObjetivo = tablero.getCasilla(fila, columna);

                if (casillaObjetivo == null) {
                    System.out.println("La casilla objetivo no existe. Intenta de nuevo.");
                    continue; // Volver a intentar
                }

                // Verificar si la casilla está dentro del alcance del arma
                int distancia = tablero.calcularDistancia(this.coordenada, casillaObjetivo.getCoordenadas());
                if (armaSeleccionada.getAlcance() == 0 && distancia > 0) {
                    System.out.println("El arma es de cuerpo a cuerpo. Debes atacar una casilla donde estés.");
                    continue; // Volver a intentar
                }
                if (armaSeleccionada.getAlcance() > 0 && distancia > armaSeleccionada.getAlcance()) {
                    System.out.println("La casilla está fuera del alcance del arma.");
                    continue; // Volver a intentar
                }

                // Verificar si hay zombis en la casilla
                List<Zombi> zombisEnCasilla = new ArrayList<>();
                for (Object entidad : casillaObjetivo.getEntidades()) {
                    if (entidad instanceof Zombi) {
                        zombisEnCasilla.add((Zombi) entidad);
                    }
                }

                if (zombisEnCasilla.isEmpty()) {
                    System.out.println("No hay zombis en la casilla seleccionada.");
                    continue; // Volver a intentar
                }

                // Realizar el ataque para cada zombi en la casilla
                for (Zombi z : zombisEnCasilla) {
                    // Si es un zombi Berserker y el arma es a distancia, ignorar
                    if (z.isBerserker() && armaSeleccionada.getAlcance() > 0) {
                         this.restarAccion();
                        System.out.println("El zombi Berserker es inmune a ataques a distancia.");
                        exitos--; // Restar un éxito aunque no se ataque al zombi
                        continue;
                    }

                    // Si el zombi puede ser eliminado
                    if (z.getAguante() <= armaSeleccionada.getPotencia()) {
                         this.restarAccion();
                        System.out.println("El zombi " + z.getTipo() + " ha sido eliminado.");
                        agregarZombiEliminado(z);
                        exitos--;
                        z.setVivo(false);
                         // Comprobar si el zombi es tóxico y si está en la misma casilla
                        if (z.isToxico() && this.coordenada.equals(casillaObjetivo.getCoordenadas())) {
                            System.out.println("El zombi tóxico te ha causado una herida al ser eliminado!");
                            recibirHerida(z);
                           

                        }
                        casillaObjetivo.eliminarEntidad(z); // Eliminar el zombi de la casilla
                        tablero.mostrarTablero();
                        agregarZombisEliminados(z);
                        zombis_eliminados++;
                    } else {
                        System.out.println("El zombi " + z.getTipo() + " es demasiado resistente para este ataque.");
                         this.restarAccion();
                        exitos--; // Restar un éxito aunque no sea derrotado
                    }

                    // Detenerse si no hay más éxitos disponibles
                    if (exitos == 0) {
                        break;
                    }
                }

                // Si no hay más éxitos, terminar el ataque
                if (exitos == 0) {
                    System.out.println("No hay más éxitos disponibles.");
                }
            }
        } else {
            System.out.println("No se consiguieron éxitos. El ataque ha fallado.");
        }

        // Registrar el ataque en el almacén
        
        almacen.registrarAtaque(ataque, j);
          System.out.println("Número de ataques: " + almacen.getAtaques().size());
          // guardarAlmacen(almacen, "src/registroAtaques.dat",j); // Guardar el almacén de ataques
    }


     // Obtener las coordenadas actuales del superviviente
    @Override
    public Coordenada getCoordenadas() {
         return coordenada;
    }
    
    //EL RESTO DE METODOS
    
    // Método para verificar si un equipo ya ha sido entregado
    public boolean equipoEntregado(Equipo equipo) {
        return equiposEntregados.contains(equipo);
    }

    // Método para agregar un equipo a los entregados
    public void agregarEquipoEntregado(Equipo equipo) {
        equiposEntregados.add(equipo);
    }

     // Método para buscar equipo
    public boolean buscarEquipo(Equipo equipo) {
        if (getAcciones() > 0){
            if (inventario.size() < MAX_INVENTARIO) {
                // Aquí se añade el equipo al inventario, independientemente de su tipo
                inventario.add(equipo);
                System.out.println(nombre + " ha encontrado " + equipo.getNombre());
                return true;
            } else {
            System.out.println("No puedes llevar mas equipo. Revisa tu inventario.");
             }
        }else{
            System.out.println("No puedes buscar equipo. Revisa tus acciones o espacio en el inventario.");
            
        }
        return false;
    }
    
    public void buscar(Inventario almacenInventario) {
        // Obtener la posición y la casilla actual
        Coordenada posicionActual = this.getCoordenadas();  
        Casilla casilla = tablero.obtenerCasilla(posicionActual);

        // Verificar si la casilla ya fue buscada
        if (casilla.fueBuscada()) {
            System.out.println("Ya se ha buscado en esta casilla. No puedes buscar nuevamente.");
            return;
        }

        // Marcar la casilla como buscada
        casilla.marcarComoBuscada();

        // Inicia la búsqueda de equipo
        System.out.println("Buscando en la casilla...");
        int probabilidad = new Random().nextInt(100) + 1; // Número aleatorio entre 1 y 100

        if (probabilidad <= 100) { // 90% de probabilidad de éxito (ajusté el porcentaje al 80%)
            // Seleccionar aleatoriamente un equipo disponible
            Set<Equipo> equiposDisponibles = almacenInventario.getEquipos(); // Obtener equipos desde el inventario

            // Verificar si hay equipos disponibles
            if (!equiposDisponibles.isEmpty()) {
                Equipo equipoAleatorio = null;
                boolean equipoValido = false;

                // Repetir hasta encontrar un equipo que no esté en el inventario
                while (!equipoValido) {
                    // Seleccionar un equipo aleatorio
                    equipoAleatorio = (Equipo) equiposDisponibles.toArray()[new Random().nextInt(equiposDisponibles.size())];

                    // Comprobar que este equipo no haya sido entregado previamente a este superviviente
                    if (!this.equipoEntregado(equipoAleatorio)) {
                        // Intentar agregar el equipo al inventario del superviviente
                        if (this.buscarEquipo(equipoAleatorio)) {
                            // Marcar el equipo como entregado (sin eliminarlo del inventario)
                            this.agregarEquipoEntregado(equipoAleatorio);
                            System.out.println("Has encontrado " + equipoAleatorio.getNombre() + " y lo has añadido a tu inventario!");
                            equipoValido = true;  // Hemos encontrado un equipo válido
                        } else {
                            System.out.println("No puedes llevar más equipo.");
                            equipoValido = true;  // En caso de que no pueda llevar más equipo, salimos del bucle
                        }
                    } else {
                        System.out.println("Ya tienes este equipo en tu inventario, buscando otro...");
                    }
                }
            } else {
                System.out.println("No hay equipo disponible en esta casilla.");
            }
        } else {
            System.out.println("No has encontrado nada en esta búsqueda.");
        }

        // Resta una acción si la búsqueda fue exitosa
        this.restarAccion();
    }

    
public void seleccionarEquipoManualmente(Inventario almacenInventario) {
    Scanner scanner = new Scanner(System.in);

    // Verificar que haya equipos disponibles en el inventario
    Set<Equipo> equiposDisponibles = almacenInventario.getEquipos(); // Obtener los equipos directamente desde el inventario
    if (equiposDisponibles.isEmpty()) {
        System.out.println("No hay equipos disponibles para seleccionar.");
        return;
    }

    // Mostrar los equipos disponibles
    System.out.println("Equipos disponibles para seleccionar:");
    List<Equipo> listaEquiposDisponibles = new ArrayList<>(equiposDisponibles); // Convertir el Set a una lista para mostrar los índices
    for (int i = 0; i < listaEquiposDisponibles.size(); i++) {
        Equipo equipo = listaEquiposDisponibles.get(i);
        System.out.println((i + 1) + ". " + equipo.getNombre());
    }

    // Permitir al jugador elegir 5 equipos para su inventario
    int maxEquipos = 5;
    List<Equipo> seleccionados = new ArrayList<>();
    while (seleccionados.size() < maxEquipos) {
        System.out.println("\nSelecciona un equipo ingresando su número (o ingresa 0 para finalizar):");

        // Validar que la entrada sea un número
        int opcion = -1;
        while (true) {
            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea pendiente
                break; // Salir del bucle si es un número válido
            } else {
                System.out.println("Entrada no válida. Debes ingresar un número entero.");
                scanner.nextLine(); // Limpiar el buffer de entrada
            }
        }

        if (opcion == 0) {
            System.out.println("Has finalizado la selección de equipos.");
            break;
        }

        if (opcion < 1 || opcion > listaEquiposDisponibles.size()) {
            System.out.println("Selección inválida. Intenta nuevamente.");
            continue;
        }

        // Obtener el equipo seleccionado
        Equipo equipoSeleccionado = listaEquiposDisponibles.get(opcion - 1);

        // Verificar si ya fue seleccionado
        if (seleccionados.contains(equipoSeleccionado)) {
            System.out.println("Ya has seleccionado este equipo. Elige otro.");
        } else {
            seleccionados.add(equipoSeleccionado);
            System.out.println("Has añadido " + equipoSeleccionado.getNombre() + " a tu inventario.");
        }
    }

    // Agregar los equipos seleccionados al inventario del superviviente
    for (Equipo equipo : seleccionados) {
        if (this.buscarEquipo(equipo)) {
            this.agregarEquipoEntregado(equipo); // Marca el equipo como entregado
        } else {
            System.out.println("No puedes llevar más equipo, inventario lleno.");
            break;
        }
    }
}

public void elegirArmaActiva() {
    Scanner scanner = new Scanner(System.in);

    // Mostrar las armas en el inventario
    List<Arma> armasEnInventario = inventario.stream()
            .filter(item -> item instanceof Arma)
            .map(item -> (Arma) item)
            .toList();

    if (armasEnInventario.isEmpty()) {
        System.out.println("No tienes armas en tu inventario.");
        return;
    }

    System.out.println("Armas disponibles en tu inventario:");
    for (int i = 0; i < armasEnInventario.size(); i++) {
        System.out.println((i + 1) + ". " + armasEnInventario.get(i).getNombre());
    }

    System.out.println("\nTus armas activas actuales:");
    for (int i = 0; i < armasActivas.size(); i++) {
        System.out.println((i + 1) + ". " + armasActivas.get(i).getNombre());
    }

    // Pedir al jugador que elija un arma del inventario
    int seleccion = -1;
    while (seleccion < 1 || seleccion > armasEnInventario.size()) {
        System.out.println("\nSelecciona un arma para activarla (ingresa el número):");

        // Verificar que la entrada sea un número entero
        if (scanner.hasNextInt()) {
            seleccion = scanner.nextInt();
            if (seleccion < 1 || seleccion > armasEnInventario.size()) {
                System.out.println("Selección inválida. Debes elegir un número entre 1 y " + armasEnInventario.size() + ".");
            }
        } else {
            // Si no es un número, mostramos un mensaje y pedimos la entrada nuevamente
            System.out.println("Entrada inválida. Debes ingresar un número.");
            scanner.next(); // Limpiar el buffer del scanner
        }
    }

    Arma armaSeleccionada = armasEnInventario.get(seleccion - 1);

    // Si ya tiene dos armas activas
    if (armasActivas.size() >= 2) {
        System.out.println("\nYa tienes dos armas activas:");
        for (int i = 0; i < armasActivas.size(); i++) {
            System.out.println((i + 1) + ". " + armasActivas.get(i).getNombre());
        }

        // Pedir al jugador que elija el arma a reemplazar
        int reemplazo = -1;
        while (reemplazo < 0 || reemplazo > armasActivas.size()) {
            System.out.println("¿Cuál deseas reemplazar? (1 o 2, o 0 para cancelar):");

            // Verificar que la entrada sea un número entero
            if (scanner.hasNextInt()) {
                reemplazo = scanner.nextInt();
                if (reemplazo < 0 || reemplazo > armasActivas.size()) {
                    System.out.println("Selección inválida. Debes elegir 1, 2, o 0 para cancelar.");
                }
            } else {
                // Si no es un número, mostramos un mensaje y pedimos la entrada nuevamente
                System.out.println("Entrada inválida. Debes ingresar un número.");
                scanner.next(); // Limpiar el buffer del scanner
            }
        }

        if (reemplazo == 0) {
            System.out.println("No se realizó ningún cambio.");
            return;
        }

        // Reemplazar el arma seleccionada
        armasActivas.set(reemplazo - 1, armaSeleccionada);
        System.out.println("Has reemplazado tu arma activa con " + armaSeleccionada.getNombre());
    } else {
        // Añadir el arma como activa
        armasActivas.add(armaSeleccionada);
        System.out.println("Has activado el arma " + armaSeleccionada.getNombre());
    }
}



    /*public void elegirArmaActiva() {
        Scanner scanner = new Scanner(System.in);

        // Mostrar las armas en el inventario
        List<Arma> armasEnInventario = inventario.stream()
                .filter(item -> item instanceof Arma)
                .map(item -> (Arma) item)
                .toList();

        if (armasEnInventario.isEmpty()) {
            System.out.println("No tienes armas en tu inventario.");
            return;
        }

        System.out.println("Armas disponibles en tu inventario:");
        for (int i = 0; i < armasEnInventario.size(); i++) {
            System.out.println((i + 1) + ". " + armasEnInventario.get(i).getNombre());
        }

        System.out.println("\nTus armas activas actuales:");
        for (int i = 0; i < armasActivas.size(); i++) {
            System.out.println((i + 1) + ". " + armasActivas.get(i).getNombre());
        }

        // Pedir al jugador que elija un arma del inventario
        System.out.println("\nSelecciona un arma para activarla (ingresa el número):");
        int seleccion = scanner.nextInt();

        if (seleccion < 1 || seleccion > armasEnInventario.size()) {
            System.out.println("Selección inválida.");
            return;
        }

        Arma armaSeleccionada = armasEnInventario.get(seleccion - 1);

        // Si ya tiene dos armas activas
        if (armasActivas.size() >= 2) {
            System.out.println("\nYa tienes dos armas activas:");
            for (int i = 0; i < armasActivas.size(); i++) {
                System.out.println((i + 1) + ". " + armasActivas.get(i).getNombre());
            }

            System.out.println("¿Cuál deseas reemplazar? (1 o 2, o 0 para cancelar):");
            int reemplazo = scanner.nextInt();

            if (reemplazo == 0) {
                System.out.println("No se realizó ningún cambio.");
                return;
            }

            if (reemplazo < 1 || reemplazo > armasActivas.size()) {
                System.out.println("Selección inválida.");
                return;
            }

            // Reemplazar el arma seleccionada
            armasActivas.set(reemplazo - 1, armaSeleccionada);
            System.out.println("Has reemplazado tu arma activa con " + armaSeleccionada.getNombre());
        } else {
            // Añadir el arma como activa
            armasActivas.add(armaSeleccionada);
            System.out.println("Has activado el arma " + armaSeleccionada.getNombre());
        }
    }
    */
    public void restarAccion(){
        this.acciones-=1;
    }
     public void restarAccionesExtras(int extras){
        this.acciones-=1+extras;
    }

    public void reiniciarAcciones() {
        this.acciones = 3;
    }

    public void recibirDaño() {
        heridas_recibidas++;
        if (heridas_recibidas >= MAX_HERIDAS) {
            vivo = false;
            setVivo(false);
            System.out.println(nombre + " ha recibido 2 heridas y ha sido eliminado.");
            // Llamar al método para eliminarlo del tablero cuando muere
        if (tablero != null) {
            tablero.eliminarSuperviviente(this);  // Elimina el superviviente del tablero
        }
        } else {
            System.out.println(nombre + " ha recibido una herida. Total de heridas: " + heridas_recibidas);
        }
    }

    public int contarProvisiones() {
        int cantidadProvisiones = 0;
        // Itera sobre el inventario del superviviente
        for (Equipo equipo : this.getInventario()) {
            if (equipo instanceof Provisiones) {
                cantidadProvisiones++;  // Si el equipo es una instancia de Provisiones, incrementamos el contador
            }
        }
        return cantidadProvisiones;
    }

    public void recibirMordedura(Zombi zombi) {
        System.out.println("Mordedura causada por Zombi: " + zombi.getId());
        heridasRecibidas.add(TipoDeHerida.MORDEDURA); // Agregar una mordedura
          zombi.añadirSupervivienteHerido(this,TipoDeHerida.MORDEDURA);
        recibirDaño();
    }
 
    public void recibirHerida(Zombi zombi) {
          System.out.println("Herida causada por Zombi: " + zombi.getId());
        heridasRecibidas.add(TipoDeHerida.HERIDA); // Agregar una herida
           zombi.añadirSupervivienteHerido(this,TipoDeHerida.HERIDA);
        recibirDaño ();
    }
    
    public void reiniciarInventario() {
        this.inventario.clear(); // Limpia el inventario anterior
    }

    public void reiniciarEstado() {
        this.vivo = true;
        this.zombis_eliminados = 0;
        //this.actual.clear(); 
        reiniciarAcciones(); // Reinicia las acciones disponibles para el turno
        // Reinicia cualquier otro estado relevante
    }

    public void agregarZombiEliminado(Zombi zombi) {
        
            historico.add(zombi);
        
            actual.add(zombi);
        
    }
    
    public void guardarAlmacen(AlmacenAtaques almacen, String archivo, Juego j) throws IOException {
        File file = new File(archivo);
        File directorio = file.getParentFile();
        if (directorio != null && !directorio.exists()) {
            if (directorio.mkdirs()) {
                System.out.println("Directorio creado: " + directorio.getAbsolutePath());
            } else {
                System.out.println("No se pudo crear el directorio.");
            }
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(almacen);
            System.out.println("Almacen guardado en: " + archivo);
        } catch (IOException e) {
            System.out.println("Error al guardar el almacén: " + e.getMessage());
            throw e;
        }
    }
 
    public void guardarHistorico() throws IOException {
        File archivo = new File("src/supervivientes/historico/" + nombre + "_historico.txt");
         archivo.getParentFile().mkdirs();
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo,true))) {
            writer.println("Histórico:\n");
            for (Zombi zombi : this.historico) {
                writer.println(zombi.toString() + "\n");
            }
        }
        System.out.println("Histórico guardado con éxito como: " + nombre + "_historico.txt");
    }
    
    public static Superviviente cargarHistorico(String nombre) throws IOException {
        File archivo = new File("src/supervivientes/historico/" + nombre + "_historico.txt");
        if (!archivo.exists()) {
            System.out.println("Archivo HISTORICO no encontrado: " + nombre + "_historico.txt");
           return new Superviviente(nombre);
        }
         Superviviente superviviente = new Superviviente(nombre);
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.equals("Histórico:") && !linea.isBlank()) {
                    Zombi zombi = Zombi.desdeString(linea);
                    superviviente.historico.add(zombi);
                }
            }
        }

        System.out.println("Histórico cargado con éxito desde: " + nombre + "_historico.txt");
        return superviviente;
    }

    public void guardarActual() throws IOException {
        File archivo = new File("src/supervivientes/actual/" + nombre + "_actual.txt");
        archivo.getParentFile().mkdirs();
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            writer.println("Actual:\n");
            for (Zombi zombi : this.actual) {
                writer.println(zombi.toString() + "\n");
            }
        }
        System.out.println("Actual guardado con éxito como: " + nombre + "_actual.txt");
    }

    public static Superviviente cargarActual(String nombre) throws IOException {
        File archivo = new File("src/supervivientes/actual/" + nombre + "_actual.txt");
        if (!archivo.exists()) {
            System.out.println("Archivo ACTUAL no encontrado: " + nombre + "actual.txt");
           return new Superviviente(nombre);
        }
         Superviviente superviviente = new Superviviente(nombre);
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            //superviviente.actual.clear(); // Limpiamos la lista histórica antes de cargar
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.equals("Actual:") && !linea.isBlank()) {
                    Zombi zombi = Zombi.desdeString(linea);
                    superviviente.actual.add(zombi);
                }
            }
        }

        System.out.println("Actual cargado con exito desde: " + nombre + "actual.txt");
        return superviviente;
    }

    @Override
    public String toString() {
        return "Superviviente: " + nombre +
               ", Estado: " + (vivo ? "VIVO" : "ELIMINADO") +
               ", Zombis Eliminados: " + zombis_eliminados +
               ", Heridas Recibidas: " + heridas_recibidas +
               ", Coordenadas: " + coordenada;
    }

   
}
