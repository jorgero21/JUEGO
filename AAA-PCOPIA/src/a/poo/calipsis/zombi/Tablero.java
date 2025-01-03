package a.poo.calipsis.zombi;

import a.poo.calipsis.zombi.Superviviente;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Tablero implements Serializable{
    private int tamaño = 10; 
    private final Casilla[][] tablero;  
    private final List<Superviviente> supervivientes;
    private  List<Zombi> zombis;
    private final Coordenada objetivo; 
    private Set<Coordenada> casillasBuscadas = new HashSet<>();
    private static final Random random = new Random();

    public Tablero() {
        this.tablero = new Casilla[tamaño][tamaño];
        this.supervivientes = new ArrayList<>();
        this.zombis = new ArrayList<>();
        //IMPORTANTE PONERLO EN 9,9 PARA EL FINAL
        this.objetivo = new Coordenada(0, 2); 
        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                tablero[i][j] = new Casilla(new Coordenada(i, j), this);
            }
        }
    }
    //PONER EL OBJETIVO AL 9,9 EN CUANTO TODO FUNCIONE BIEN
    public Tablero(int tamaño) {
        this.tamaño = tamaño;
        this.tablero = new Casilla[tamaño][tamaño];
        this.supervivientes = new ArrayList<>();
        this.zombis = new ArrayList<>();
        this.objetivo = new Coordenada(0, 2);  
        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                tablero[i][j] = new Casilla(new Coordenada(i, j), this);
            }
        }
    }

    public Casilla getCasilla(int fila, int columna) {
        if (esPosicionValida(fila, columna)) { 
            return tablero[fila][columna];
        } else {
            return null; 
        }
    }
    
    public void setZombis(List<Zombi> zombis) { this.zombis = zombis;}   
    public List<Zombi> getZombis() { return zombis; }
    public Coordenada getObjetivo() {  return objetivo; }

    public void colocarSupervivienteEnTablero(Superviviente superviviente, int fila, int columna) {
        if (fila >= 0 && fila < tablero.length && columna >= 0 && columna < tablero[0].length) {
            tablero[fila][columna].colocarSuperviviente(superviviente);
            supervivientes.add(superviviente);  
            System.out.println("Superviviente " + superviviente.getNombre() + " colocado en la casilla (" + fila + ", " + columna + ")");
        } else {
            System.out.println("Posicion invalida en el tablero");
        }
    }

    public boolean esPosicionValida(Coordenada coordenada) {
        return coordenada.getFila() >= 0 && coordenada.getFila() < tamaño &&
               coordenada.getColumna() >= 0 && coordenada.getColumna() < tamaño;
    }
    
    public boolean esPosicionValida(int fila, int columna) {
        return fila >= 0 && fila < tamaño &&
               columna >= 0 && columna < tamaño;
    }

    public void asignarSupervivientesAlTablero(List<Superviviente> supervivientes) {
        for (Superviviente s : supervivientes) {
            Coordenada pos = s.getCoordenadas();  
            Casilla casilla = tablero[pos.getFila()][pos.getColumna()];  
            casilla.getEntidades().add(s);  
        }
    }
    
    public void mostrarTablero() {
        for (int i = tamaño - 1; i >= 0; i--) {  
            for (int j = 0; j < tamaño; j++) {  
                Casilla casilla = tablero[j][i]; 
                List<Object> entidades = casilla.getEntidades(); 

                if (entidades == null) {
                    entidades = new ArrayList<>(); 
                }

                if (entidades.isEmpty()) {
                    System.out.print("[ ] "); 
                } else {
                    StringBuilder contenidoCasilla = new StringBuilder("[");
                    for (int k = 0; k < entidades.size(); k++) {
                        Object entidad = entidades.get(k);
                        if (entidad instanceof Superviviente) {
                            Superviviente superviviente = (Superviviente) entidad;
                            contenidoCasilla.append(superviviente.getNombre());
                        } 
                        if (entidad instanceof Zombi) {
                            Zombi zombi = (Zombi) entidad;
                            contenidoCasilla.append(zombi.getTipo());
                            if (zombi.isBerserker()) {
                                contenidoCasilla.append("-Berserker");
                            }
                            if (zombi.isToxico()) {
                                contenidoCasilla.append("-Toxico");
                            }
                            if (!zombi.isBerserker() && !zombi.isToxico()) {
                                contenidoCasilla.append("-Normal");
                            }
                        }
                        if (k < entidades.size() - 1) {
                            contenidoCasilla.append(", ");
                        }
                    }
                    contenidoCasilla.append("] ");
                    System.out.print(contenidoCasilla); 
                }
            }
            System.out.println();
        }
    }
    
    public void mostrarTableroSimulacion(int tamaño) {
        for (int i = tamaño - 1; i >= 0; i--) {  
            for (int j = 0; j < tamaño; j++) {  
                Casilla casilla = tablero[j][i]; 
                List<Object> entidades = casilla.getEntidades(); 
                if (entidades == null) {
                    entidades = new ArrayList<>(); 
                }
                if (entidades.isEmpty()) {
                    System.out.print("[ ] "); 
                } else {
                    StringBuilder contenidoCasilla = new StringBuilder("[");
                    for (int k = 0; k < entidades.size(); k++) {
                        Object entidad = entidades.get(k);
                        if (entidad instanceof Superviviente) {
                            Superviviente superviviente = (Superviviente) entidad;
                            contenidoCasilla.append(superviviente.getNombre());
                        } 
                        if (entidad instanceof Zombi) {
                            Zombi zombi = (Zombi) entidad;
                            contenidoCasilla.append(zombi.getTipo());
                            if (zombi.isBerserker()) {
                                contenidoCasilla.append("-Berserker");
                            }
                            if (zombi.isToxico()) {
                                contenidoCasilla.append("-Toxico");
                            }
                            if (!zombi.isBerserker() && !zombi.isToxico()) {
                                contenidoCasilla.append("-Normal");
                            }
                        }
                        if (k < entidades.size() - 1) {
                            contenidoCasilla.append(", ");
                        }
                    }
                    contenidoCasilla.append("] ");
                    System.out.print(contenidoCasilla); 
                }
            }
            System.out.println(); 
        }
    }

    public void actualizarTablero(Superviviente superviviente, Coordenada posicionAnterior, Coordenada nuevaPosicion) {
        if (posicionAnterior != null) {
            Casilla casillaAnterior = tablero[posicionAnterior.getFila()][posicionAnterior.getColumna()];
            if (casillaAnterior != null) {
                casillaAnterior.getEntidades().remove(superviviente); 
                System.out.println("Superviviente eliminado de la posicion anterior: " + posicionAnterior);
            }
        }
        Casilla nuevaCasilla = tablero[nuevaPosicion.getFila()][nuevaPosicion.getColumna()];
        if (nuevaCasilla != null) {
            nuevaCasilla.getEntidades().add(superviviente);
            System.out.println("Superviviente movido a la nueva posicion: " + nuevaPosicion);
        }
        mostrarTablero();
    }

    public void actualizarTablero(Zombi zombi, Coordenada posicionAnterior, Coordenada nuevaPosicion) {
        if (posicionAnterior != null) {
            Casilla casillaAnterior = tablero[posicionAnterior.getFila()][posicionAnterior.getColumna()];
            if (casillaAnterior != null) {
                casillaAnterior.getEntidades().remove(zombi); 
            }
        }
        Casilla nuevaCasilla = tablero[nuevaPosicion.getFila()][nuevaPosicion.getColumna()];
        if (nuevaCasilla != null) {
            nuevaCasilla.getEntidades().add(zombi);
        }
    }

    public int calcularDistancia(Coordenada origen, Coordenada destino) {
        int distancia = Math.abs(origen.getFila() - destino.getFila()) + Math.abs(origen.getColumna() - destino.getColumna());
        return distancia;
    }

    public void eliminarSuperviviente(Superviviente superviviente) {
        if (superviviente == null) {
            System.out.println("El superviviente ya es null, no se necesita eliminar.");
            return;
        }

        Coordenada posicionSuperviviente = superviviente.getCoordenadas();
        if (posicionSuperviviente != null) {
            Casilla casilla = tablero[posicionSuperviviente.getFila()][posicionSuperviviente.getColumna()];
            if (casilla != null) {
                casilla.getEntidades().remove(superviviente);
                System.out.println("Superviviente eliminado de la posicion: " + posicionSuperviviente);
            }
        }
    }

    public Casilla obtenerCasilla(Coordenada coordenada) {
        if (coordenada.getFila() >= 0 && coordenada.getFila() < tamaño &&
            coordenada.getColumna() >= 0 && coordenada.getColumna() < tamaño) {
            return tablero[coordenada.getFila()][coordenada.getColumna()];  
        } else {
            return null;  
        }
    }
    
    public Zombi generarZombiConProbabilidades(int id, Tablero tablero) {
        int categoria = random.nextInt(3);
        double probabilidad = random.nextDouble();
        TipoZombi tipo;
        if (probabilidad < 0.6) {
            tipo = TipoZombi.CAMINANTE;
        } else if (probabilidad < 0.9) {
            tipo = TipoZombi.CORREDOR;
        } else {
            tipo = TipoZombi.ABOMINACION;
        }
        boolean esNormal = (categoria == 0);
        boolean esBerserker = (categoria == 1);
        boolean esToxico = (categoria == 2);
        Random random = new Random();
        int fila = random.nextInt(tablero.tamaño);  
        int columna = random.nextInt(tablero.tamaño); 
        Coordenada posicion = new Coordenada(fila, columna); 
        Zombi nuevoZombi = new Zombi(id, tipo, esNormal, esBerserker, esToxico, posicion);
        Casilla casilla = tablero.tablero[fila][columna]; 
        if (casilla.getEntidades().isEmpty()) { 
            casilla.getEntidades().add(nuevoZombi); 
        } else {
            return generarZombiConProbabilidades(id, tablero);  
        }
        return nuevoZombi;
    }

    public Zombi generarZombiManual(Tablero tablero, int id) {
        Scanner scanner = new Scanner(System.in);
        int subtipo = 0;
        while (subtipo < 1 || subtipo > 3) {
            System.out.println("Selecciona el tipo de zombi:");
            System.out.println("1. Caminante");
            System.out.println("2. Corredor");
            System.out.println("3. Abominacion");
            if (scanner.hasNextInt()) {
                subtipo = scanner.nextInt();
            } else {
                System.out.println("Por favor ingresa un numero valido.");
                scanner.nextLine(); 
                continue;
            }
        }
        TipoZombi tipo;
        switch (subtipo) {
            case 1:
                tipo = TipoZombi.CAMINANTE;
                break;
            case 2:
                tipo = TipoZombi.CORREDOR;
                break;
            case 3:
                tipo = TipoZombi.ABOMINACION;
                break;
            default:
                tipo = TipoZombi.CAMINANTE; 
                break;
        }
        int categoria = 0;
        while (categoria < 1 || categoria > 3) {
            System.out.println("Selecciona el subtipo de zombi:");
            System.out.println("1. Normal");
            System.out.println("2. Berserker");
            System.out.println("3. Toxico");
            if (scanner.hasNextInt()) {
                categoria = scanner.nextInt();
            } else {
                System.out.println("Por favor ingresa un numero valido");
                scanner.nextLine(); 
                continue;
            }
        }
        boolean esNormal = categoria == 1;
        boolean esBerserker = categoria == 2;
        boolean esToxico = categoria == 3;
        int fila = -1, columna = -1;
        while (fila < 0 || fila >= tablero.tamaño) {
            System.out.println("Introduce las coordenadas donde colocar el zombi:");
            tablero.mostrarTablero(); 
            System.out.print("Fila: ");
            if (scanner.hasNextInt()) {
                fila = scanner.nextInt();
            } else {
                System.out.println("Por favor ingresa un numero válido para la fila");
                scanner.nextLine(); 
                continue;
            }
        }
        while (columna < 0 || columna >= tablero.tamaño) {
            System.out.print("Columna: ");
            if (scanner.hasNextInt()) {
                columna = scanner.nextInt();
            } else {
                System.out.println("Por favor ingresa un numero válido para la columna");
                scanner.nextLine(); 
                continue;
            }
        }
        if (fila < 0 || fila >= tablero.tamaño || columna < 0 || columna >= tablero.tamaño) {
            System.out.println("Coordenadas fuera del rango del tablero. No se pudo crear el zombi");
            return null; 
        }
        Coordenada posicion = new Coordenada(fila, columna);
        Casilla casilla = tablero.tablero[fila][columna];
        Zombi nuevoZombi = new Zombi(id, tipo, esNormal, esBerserker, esToxico, posicion);
        casilla.getEntidades().add(nuevoZombi);
        System.out.println("Zombi creado exitosamente y colocado en la casilla seleccionada!");
        return nuevoZombi;
    }

    public void agregarSuperviviente(Superviviente superviviente) {
        supervivientes.add(superviviente);
        System.out.println(superviviente.getNombre() + " ha sido agregado al almacen de supervivientes");
    }
   
    public List<Superviviente> getSupervivientes() {
        return supervivientes;
    }

    public List<Superviviente> obtenerSupervivientes() {
        return supervivientes;
    }

    public void mostrarSupervivientes() {
            if (supervivientes.isEmpty()) {
                System.out.println("No hay supervivientes en el almacen");
            } else {
                System.out.println("Supervivientes en el almacen:");
                for (Superviviente s : supervivientes) {
                    System.out.println(s); 
                }
            }
        }

    public void guardarSupervivientesEnFichero(String nombreFichero) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreFichero))) {
            for (Superviviente s : supervivientes) {
                writer.write(s.getNombre() + "," + s.isVivo() + "," + s.getHeridas_recibidas() + "," + s.getZombis_eliminados());
                writer.newLine();
            }
            System.out.println("Supervivientes guardados en el fichero: " + nombreFichero);
        } catch (IOException e) {
            System.err.println("Error al guardar los supervivientes: " + e.getMessage());
        }
    }

    public List<Superviviente> cargarSupervivientesDesdeFichero(String nombreFichero) {
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreFichero))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] partes = line.split(",");
                if (partes.length == 4) {
                    String nombre = partes[0];
                    boolean vivo = Boolean.parseBoolean(partes[1]);             
                    int heridas = Integer.parseInt(partes[2]);
                    int contadorZombisEliminados = Integer.parseInt(partes[3]);
                    Superviviente superviviente = new Superviviente(nombre);
                    superviviente.setVivo(true);
                    superviviente.setHeridas_recibidas(heridas); 
                    superviviente.setZombis_eliminados(contadorZombisEliminados); 
                    agregarSuperviviente(superviviente);
                }
            }
            System.out.println("Supervivientes cargados desde el fichero: " + nombreFichero);
        } catch (IOException e) {
            System.err.println("Error al cargar los supervivientes: " + e.getMessage());
        }
        return supervivientes; 
    }
    
    public int contarZombisEnCasilla(Coordenada coordenada) {
        Casilla casilla = obtenerCasilla(coordenada);
        return (int) (casilla != null ? casilla.getEntidades().stream().filter(e -> e instanceof Zombi).count() : 0);
    }
}