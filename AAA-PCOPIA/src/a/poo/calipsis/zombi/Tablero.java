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
    private int tamaño = 10; // Tamaño del tablero 10x10
    private final Casilla[][] tablero;  // Matriz de casillas
    private final List<Superviviente> supervivientes;
    private  List<Zombi> zombis;
    private final Coordenada objetivo; // Coordenada de la casilla objetivo
    private Set<Coordenada> casillasBuscadas = new HashSet<>();
    private static final Random random = new Random();

    public Tablero() {
        this.tablero = new Casilla[tamaño][tamaño];
        this.supervivientes = new ArrayList<>();
        this.zombis = new ArrayList<>();
        //IMPORTANTE PONERLO EN 9,9 PARA EL FINAL
        this.objetivo = new Coordenada(0, 2); // Objetivo en la casilla (0,2)

        // Inicializar el tablero con casillas vacías
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
        this.objetivo = new Coordenada(0, 2);  // Objetivo predeterminado en (0, 2)

        // Inicializa el tablero con casillas vacías
        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                tablero[i][j] = new Casilla(new Coordenada(i, j), this);
            }
        }
    }
    
    public Casilla getCasilla(int fila, int columna) {
        if (esPosicionValida(fila, columna)) { // Verifica si la posición es válida
            return tablero[fila][columna];
        } else {
            return null; // Retorna null si la posición no es válida
        }
    }
    
    public void setZombis(List<Zombi> zombis) {
        this.zombis = zombis;
    }   

    public List<Zombi> getZombis() {
        return zombis;
    }

    public Coordenada getObjetivo() {
        return objetivo;
    }

    // Método para colocar un superviviente en el tablero en una posición específica
    public void colocarSupervivienteEnTablero(Superviviente superviviente, int fila, int columna) {
        if (fila >= 0 && fila < tablero.length && columna >= 0 && columna < tablero[0].length) {
            tablero[fila][columna].colocarSuperviviente(superviviente);
            supervivientes.add(superviviente);  // Agregar a la lista de supervivientes
            System.out.println("Superviviente " + superviviente.getNombre() + " colocado en la casilla (" + fila + ", " + columna + ")");
        } else {
            System.out.println("Posición inválida en el tablero.");
        }
    }

    public boolean esPosicionValida(Coordenada coordenada) {
        return coordenada.getFila() >= 0 && coordenada.getFila() < tamaño &&
               coordenada.getColumna() >= 0 && coordenada.getColumna() < tamaño;
    }
    
    // Sobrecarga de esPosicionValida para recibir fila y columna
    public boolean esPosicionValida(int fila, int columna) {
        return fila >= 0 && fila < tamaño &&
               columna >= 0 && columna < tamaño;
    }

    public void asignarSupervivientesAlTablero(List<Superviviente> supervivientes) {
        for (Superviviente s : supervivientes) {
            Coordenada pos = s.getCoordenadas();  // Obtener la posición de cada superviviente
            Casilla casilla = tablero[pos.getFila()][pos.getColumna()];  // Obtener la casilla correspondiente
            casilla.getEntidades().add(s);  // Agregar al superviviente en esa casilla
        }
    }


    public void mostrarTablero() {
      // Recorremos el tablero desde la última fila hasta la primera para visualizar correctamente
      for (int i = tamaño - 1; i >= 0; i--) {  // Usar el tamaño dinámico
          for (int j = 0; j < tamaño; j++) {   // Usar el tamaño dinámico
              Casilla casilla = tablero[j][i];  // Obtiene la casilla actual
              List<Object> entidades = casilla.getEntidades(); 

              if (entidades == null) {
                  entidades = new ArrayList<>();  // Si la casilla es null, inicializa una lista vacía
              }

              if (entidades.isEmpty()) {
                  System.out.print("[ ] "); // Casilla vacía
              } else {
                  // Crear un String para almacenar las entidades juntas, separadas por comas
                  StringBuilder contenidoCasilla = new StringBuilder("[");

                  for (int k = 0; k < entidades.size(); k++) {
                      Object entidad = entidades.get(k);
                      if (entidad instanceof Superviviente) {
                          Superviviente superviviente = (Superviviente) entidad;
                          contenidoCasilla.append(superviviente.getNombre());
                      } 

                      // Verifica si es un zombi
                      if (entidad instanceof Zombi) {
                          Zombi zombi = (Zombi) entidad;
                          contenidoCasilla.append(zombi.getTipo());

                          // Indica si es Berserker o Tóxico
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

                      // Agregar una coma si no es la última entidad en la lista
                      if (k < entidades.size() - 1) {
                          contenidoCasilla.append(", ");
                      }
                  }

                  contenidoCasilla.append("] ");
                  System.out.print(contenidoCasilla); // Imprime las entidades juntas en una casilla
              }
          }
          System.out.println(); // Nueva línea para la siguiente fila
      }
  }
    
    public void mostrarTableroSimulacion(int tamaño) {
      // Recorremos el tablero desde la última fila hasta la primera para visualizar correctamente
      for (int i = tamaño - 1; i >= 0; i--) {  // Usar el tamaño dinámico
          for (int j = 0; j < tamaño; j++) {   // Usar el tamaño dinámico
              Casilla casilla = tablero[j][i];  // Obtiene la casilla actual
              List<Object> entidades = casilla.getEntidades(); 

              if (entidades == null) {
                  entidades = new ArrayList<>();  // Si la casilla es null, inicializa una lista vacía
              }

              if (entidades.isEmpty()) {
                  System.out.print("[ ] "); // Casilla vacía
              } else {
                  // Crear un String para almacenar las entidades juntas, separadas por comas
                  StringBuilder contenidoCasilla = new StringBuilder("[");

                  for (int k = 0; k < entidades.size(); k++) {
                      Object entidad = entidades.get(k);
                      if (entidad instanceof Superviviente) {
                          Superviviente superviviente = (Superviviente) entidad;
                          contenidoCasilla.append(superviviente.getNombre());
                      } 

                      // Verifica si es un zombi
                      if (entidad instanceof Zombi) {
                          Zombi zombi = (Zombi) entidad;
                          contenidoCasilla.append(zombi.getTipo());

                          // Indica si es Berserker o Tóxico
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

                      // Agregar una coma si no es la última entidad en la lista
                      if (k < entidades.size() - 1) {
                          contenidoCasilla.append(", ");
                      }
                  }

                  contenidoCasilla.append("] ");
                  System.out.print(contenidoCasilla); // Imprime las entidades juntas en una casilla
              }
          }
          System.out.println(); // Nueva línea para la siguiente fila
      }
  }

    public void actualizarTablero(Superviviente superviviente, Coordenada posicionAnterior, Coordenada nuevaPosicion) {
    // Eliminar al superviviente de su posición anterior
    if (posicionAnterior != null) {
        // Asegúrate de que la casilla anterior exista y sea válida
        Casilla casillaAnterior = tablero[posicionAnterior.getFila()][posicionAnterior.getColumna()];
        if (casillaAnterior != null) {
            casillaAnterior.getEntidades().remove(superviviente); // Elimina el superviviente de la lista de entidades
            System.out.println("Superviviente eliminado de la posición anterior: " + posicionAnterior);
        }
    }

    // Asegúrate de que la nueva posición esté inicializada
    Casilla nuevaCasilla = tablero[nuevaPosicion.getFila()][nuevaPosicion.getColumna()];
    if (nuevaCasilla != null) {
        nuevaCasilla.getEntidades().add(superviviente); // Agrega el superviviente a la nueva casilla
        System.out.println("Superviviente movido a la nueva posición: " + nuevaPosicion);
    }

    // Mostrar el tablero actualizado
    mostrarTablero();
}

    public void actualizarTablero(Zombi zombi, Coordenada posicionAnterior, Coordenada nuevaPosicion) {
        // Eliminar al zombi de su posición anterior
        if (posicionAnterior != null) {
            // Asegúrate de que la casilla anterior exista y sea válida
            Casilla casillaAnterior = tablero[posicionAnterior.getFila()][posicionAnterior.getColumna()];
            if (casillaAnterior != null) {
                casillaAnterior.getEntidades().remove(zombi); // Elimina el zombi de la lista de entidades
               // System.out.println("Zombi eliminado de la posición anterior: " + posicionAnterior);
            }
        }

        // Asegúrate de que la nueva posición esté inicializada
        Casilla nuevaCasilla = tablero[nuevaPosicion.getFila()][nuevaPosicion.getColumna()];
        if (nuevaCasilla != null) {
            nuevaCasilla.getEntidades().add(zombi); // Agrega el zombi a la nueva casilla
           // System.out.println("Zombi movido a la nueva posición: " + nuevaPosicion);
        }

        // Mostrar el tablero actualizado
        //mostrarTablero();
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
                // Elimina al superviviente de la lista de entidades
                casilla.getEntidades().remove(superviviente);
                System.out.println("Superviviente eliminado de la posición: " + posicionSuperviviente);
            }
        }
    }

    public Casilla obtenerCasilla(Coordenada coordenada) {
        // Asegúrate de que la coordenada está dentro de los límites del tablero
        if (coordenada.getFila() >= 0 && coordenada.getFila() < tamaño &&
            coordenada.getColumna() >= 0 && coordenada.getColumna() < tamaño) {
            return tablero[coordenada.getFila()][coordenada.getColumna()];  // Devuelve lo que esté en la casilla
        } else {
            return null;  // Si la coordenada es inválida, retorna null
        }
    }
    
    public Zombi generarZombiConProbabilidades(int id, Tablero tablero) {
            // Probabilidad inicial (Normales, Berserkers, Tóxicos)
            int categoria = random.nextInt(3); // 0 = Normal, 1 = Berserker, 2 = Tóxico
            double probabilidad = random.nextDouble();

            // Selección del subtipo dentro de la categoría
            TipoZombi tipo;
            if (probabilidad < 0.6) {
                tipo = TipoZombi.CAMINANTE;
            } else if (probabilidad < 0.9) {
                tipo = TipoZombi.CORREDOR;
            } else {
                tipo = TipoZombi.ABOMINACION;
            }

            // Crear el zombi con las características seleccionadas
            boolean esNormal = (categoria == 0);
            boolean esBerserker = (categoria == 1);
            boolean esToxico = (categoria == 2);

            // Generar una posición aleatoria dentro del tablero
            Random random = new Random();
            int fila = random.nextInt(tablero.tamaño);  // Suponiendo que "tablero.tamaño" representa las filas
            int columna = random.nextInt(tablero.tamaño); // "tablero.tamaño" también es el número de columnas
            Coordenada posicion = new Coordenada(fila, columna); // Crear la coordenada con fila y columna aleatorias

            // Crear el nuevo zombi con la coordenada aleatoria
            Zombi nuevoZombi = new Zombi(id, tipo, esNormal, esBerserker, esToxico, posicion);

            // Verificar si la casilla en esa coordenada está vacía
            Casilla casilla = tablero.tablero[fila][columna]; // Obtener la casilla correspondiente

            if (casilla.getEntidades().isEmpty()) {  // Si no hay entidades en la casilla
                // Colocar el zombi en la casilla correspondiente
                casilla.getEntidades().add(nuevoZombi);  // Coloca el zombi en la casilla
            } else {
                // Si la casilla está ocupada, generar otra coordenada aleatoria y volver a intentar
                return generarZombiConProbabilidades(id, tablero);  // Recursión para intentar colocar el zombi en otro sitio
            }

            // Retornar el zombi creado
            return nuevoZombi;
    }


    public Zombi generarZombiManual(Tablero tablero, int id) {
    Scanner scanner = new Scanner(System.in);

    int subtipo = 0;
    // Validar que el subtipo sea un número
    while (subtipo < 1 || subtipo > 3) {
        System.out.println("Selecciona el tipo de zombi:");
        System.out.println("1. Caminante");
        System.out.println("2. Corredor");
        System.out.println("3. Abominación");

        if (scanner.hasNextInt()) {
            subtipo = scanner.nextInt();
        } else {
            System.out.println("Por favor ingresa un número válido.");
            scanner.nextLine(); // Limpiar el buffer
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
            tipo = TipoZombi.CAMINANTE; // Por defecto
            break;
    }

    int categoria = 0;
    // Validar que la categoría sea un número
    while (categoria < 1 || categoria > 3) {
        System.out.println("Selecciona el subtipo de zombi:");
        System.out.println("1. Normal");
        System.out.println("2. Berserker");
        System.out.println("3. Tóxico");

        if (scanner.hasNextInt()) {
            categoria = scanner.nextInt();
        } else {
            System.out.println("Por favor ingresa un número válido.");
            scanner.nextLine(); // Limpiar el buffer
            continue;
        }
    }

    boolean esNormal = categoria == 1;
    boolean esBerserker = categoria == 2;
    boolean esToxico = categoria == 3;

    // Validar las coordenadas
    int fila = -1, columna = -1;
    while (fila < 0 || fila >= tablero.tamaño) {
        System.out.println("Introduce las coordenadas donde colocar el zombi:");
        tablero.mostrarTablero(); // Mostrar el tablero para que el jugador pueda elegir mejor
        System.out.print("Fila: ");
        if (scanner.hasNextInt()) {
            fila = scanner.nextInt();
        } else {
            System.out.println("Por favor ingresa un número válido para la fila.");
            scanner.nextLine(); // Limpiar el buffer
            continue;
        }
    }

    while (columna < 0 || columna >= tablero.tamaño) {
        System.out.print("Columna: ");
        if (scanner.hasNextInt()) {
            columna = scanner.nextInt();
        } else {
            System.out.println("Por favor ingresa un número válido para la columna.");
            scanner.nextLine(); // Limpiar el buffer
            continue;
        }
    }

    // Validar si las coordenadas están dentro del tablero
    if (fila < 0 || fila >= tablero.tamaño || columna < 0 || columna >= tablero.tamaño) {
        System.out.println("Coordenadas fuera del rango del tablero. No se pudo crear el zombi.");
        return null; // Devuelve null si las coordenadas son inválidas
    }

    Coordenada posicion = new Coordenada(fila, columna);

    // Obtener la casilla correspondiente
    Casilla casilla = tablero.tablero[fila][columna];

    // Crear el zombi
    Zombi nuevoZombi = new Zombi(id, tipo, esNormal, esBerserker, esToxico, posicion);

    // Colocar el zombi en la casilla
    casilla.getEntidades().add(nuevoZombi);
    System.out.println("¡Zombi creado exitosamente y colocado en la casilla seleccionada!");

    return nuevoZombi;
}


    // Método para agregar un superviviente al almacén
    public void agregarSuperviviente(Superviviente superviviente) {
        supervivientes.add(superviviente);
        System.out.println(superviviente.getNombre() + " ha sido agregado al almacen de supervivientes.");
    }
    
       // Método para seleccionar los supervivientes
    public List<Superviviente> seleccionarSupervivientes(List<Superviviente> todosSupervivientes) {
        List<Superviviente> seleccionados = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Selecciona 2 supervivientes:");

        // Mostrar la lista de todos los supervivientes
        for (int i = 0; i < todosSupervivientes.size(); i++) {
            System.out.println((i + 1) + ". " + todosSupervivientes.get(i).getNombre());
        }

        // Permitir la selección de 2 supervivientes
        // Permitir la selección de hasta 5 supervivientes
       while (seleccionados.size() <2) {  // Cambié el 4 por un 5
           System.out.print("Introduce el numero del superviviente que deseas seleccionar (o 0 para terminar): ");
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
        }

        return seleccionados;
    }

    public List<Superviviente> getSupervivientes() {
        return supervivientes;
    }

    // Método para obtener la lista de supervivientes
    public List<Superviviente> obtenerSupervivientes() {
        return supervivientes;
    }

    public void mostrarSupervivientes() {
            if (supervivientes.isEmpty()) {
                System.out.println("No hay supervivientes en el almacen.");
            } else {
                System.out.println("Supervivientes en el almacen:");
                for (Superviviente s : supervivientes) {
                    System.out.println(s); // Asumiendo que has sobrescrito toString en Superviviente
                }
            }
        }

    // Método para guardar los supervivientes en un fichero
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

    // Método para cargar los supervivientes desde un fichero
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
                    superviviente.setVivo(true); // Método setter que debes implementar
                    superviviente.setHeridas_recibidas(heridas); // Método setter que debes implementar
                    superviviente.setZombis_eliminados(contadorZombisEliminados); // Método setter que debes implementar

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