package a.poo.calipsis.zombi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Casilla implements Activable, Serializable{
    
    private Coordenada posicion; // Posición de la casilla en el tablero
    private List<Object> entidades; // Lista para almacenar entidades (zombis, supervivientes, etc.)
    private boolean explorada;   // Estado de la casilla (si ha sido explorada)
    private Tablero tablero;     // Referencia al tablero que contiene esta casilla
    private Superviviente superviviente;  // Puede ser null si la casilla está vacía
    private Set<Coordenada> casillasBuscadas = new HashSet<>(); // Para controlar si la casilla fue buscada por cualquier superviviente
    private List<Superviviente> supervivientes;  
    private List<Zombi> zombis;  
    private boolean fueBuscada;

    // Constructor
    public Casilla(Coordenada posicion, Tablero tablero) {
        this.posicion = posicion;
        this.explorada = false; // Por defecto, la casilla no ha sido explorada
        this.tablero = tablero;
        this.supervivientes = new ArrayList<>();
           this.zombis = new ArrayList<>();
        this.entidades = new ArrayList<>();
        this.fueBuscada = false;
    }

    public List<Superviviente> getSupervivientes() {
        return supervivientes;
    }
      
    public Superviviente getSuperviviente() {
        return superviviente;
    }

    public void setSuperviviente(Superviviente superviviente) {
        this.superviviente = superviviente;
    }

    // Métodos adicionales
    public Coordenada getPosicion() {
        return posicion;
    }
    
        // Devuelve si la casilla fue buscada
    public boolean fueBuscada() {
        return fueBuscada;
    }

    // Marca la casilla como buscada
    public void marcarComoBuscada() {
        this.fueBuscada = true;
    }
    
    // Método para colocar un superviviente en la casilla
    public void colocarSuperviviente(Superviviente superviviente) {
        this.superviviente = superviviente;
    }
    public List<Object> getEntidades() {
        return entidades;
    }

    public void agregarEntidad(Object entidad) {
        entidades.add(entidad);
    }

    public void eliminarEntidad(Object entidad) {
        entidades.remove(entidad);
    }

    // Métodos adicionales si necesitas
    public void agregarSuperviviente(Superviviente superviviente) {
        supervivientes.add(superviviente);
    }
    // Métodos adicionales si necesitas
    public void agregarZombi(Zombi z) {
        zombis.add(z);
    }
      
    // Método para verificar si la casilla está vacía
    public boolean estaVacia() {
        return this.supervivientes.isEmpty();
    }

    // Implementación del método moverse()
    @Override
    public void moverse() {
        System.out.println("Moviendo algo en la casilla " + posicion);
        // Lógica del movimiento (puede ser específica del juego)
    }

    // Implementación del método atacar()
    @Override
    public void atacar() {
        System.out.println("Atacando algo en la casilla " + posicion);
        // Lógica del ataque (puede ser específica del juego)
    }

    // Implementación del método getCoordenadas()
    @Override
    public Coordenada getCoordenadas() {
        System.out.println("Coordenadas de la casilla: " + posicion);
        return posicion;
    }

    // Método para verificar si la casilla ya fue buscada por algún superviviente
    public boolean estaBuscadaPorSupervivientes(List<Superviviente> supervivientesSeleccionados) {
        // Recorremos todos los supervivientes y comprobamos si alguno ha buscado la casilla
        for (Superviviente superviviente : supervivientesSeleccionados) {
            if (casillasBuscadas.contains(superviviente.getCoordenadas())) {
                return true; // Si algún superviviente ha buscado la casilla, retornamos true
            }
        }
        return false; // Si ningún superviviente ha buscado la casilla, retornamos false
    }

    // Método que marca la casilla como "buscada" por el superviviente
    public void marcarComoBuscada(Superviviente superviviente) {
        casillasBuscadas.add(superviviente.getCoordenadas()); // Marcar la casilla como ya buscada por este superviviente
    }

    // Método para verificar si la casilla ha sido buscada en esta posición
    public boolean estaBuscada() {
        return casillasBuscadas.contains(posicion); // Verificar si la casilla ha sido buscada en esa coordenada
    }

   public int contarZombis() {
        int contador = 0;
        for (Object objeto : entidades) { // Recorremos la lista de objetos
            if (objeto instanceof Zombi) { // Verificamos si el objeto es un Zombi
                contador++;
            }
        }
        return contador;
    }


    @Override
    public String toString() {
        return "Casilla en " + posicion + ", explorada: " + explorada;
    }
    

}
