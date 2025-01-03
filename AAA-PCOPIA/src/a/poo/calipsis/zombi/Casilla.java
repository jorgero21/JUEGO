package a.poo.calipsis.zombi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Casilla implements Activable, Serializable{
    
    private Coordenada posicion; 
    private List<Object> entidades; 
    private boolean explorada;  
    private Tablero tablero;   
    private Superviviente superviviente;  
    private Set<Coordenada> casillasBuscadas = new HashSet<>();
    private List<Superviviente> supervivientes;  
    private List<Zombi> zombis;  
    private boolean fueBuscada;

    // Constructor
    public Casilla(Coordenada posicion, Tablero tablero) {
        this.posicion = posicion;
        this.explorada = false; 
        this.tablero = tablero;
        this.supervivientes = new ArrayList<>();
        this.zombis = new ArrayList<>();
        this.entidades = new ArrayList<>();
        this.fueBuscada = false;
    }

    public List<Superviviente> getSupervivientes() { return supervivientes; }
    public Superviviente getSuperviviente() {  return superviviente; }
    public void setSuperviviente(Superviviente superviviente) {   this.superviviente = superviviente; }
    public Coordenada getPosicion() {  return posicion;  }
    public boolean fueBuscada() {  return fueBuscada; }
    public void marcarComoBuscada() {   this.fueBuscada = true; }
    public void colocarSuperviviente(Superviviente superviviente) {  this.superviviente = superviviente; }
    public List<Object> getEntidades() {  return entidades;}
    public void agregarEntidad(Object entidad) {  entidades.add(entidad); }
    public void eliminarEntidad(Object entidad) {  entidades.remove(entidad);}
    public void agregarSuperviviente(Superviviente superviviente) { supervivientes.add(superviviente);}
    public void agregarZombi(Zombi z) {  zombis.add(z); }
    public boolean estaVacia() { return this.supervivientes.isEmpty(); } 
    public void marcarComoBuscada(Superviviente superviviente) { casillasBuscadas.add(superviviente.getCoordenadas());  }
    public boolean estaBuscada() {  return casillasBuscadas.contains(posicion);  }
    
    @Override
    public void moverse() {   System.out.println("Moviendo algo en la casilla " + posicion);  }
    @Override
    public void atacar() {  System.out.println("Atacando algo en la casilla " + posicion);  }
    @Override
    public Coordenada getCoordenadas() {
        System.out.println("Coordenadas de la casilla: " + posicion);
        return posicion;
    }
    
    public boolean estaBuscadaPorSupervivientes(List<Superviviente> supervivientesSeleccionados) {
        for (Superviviente superviviente : supervivientesSeleccionados) {
            if (casillasBuscadas.contains(superviviente.getCoordenadas())) {
                return true; 
            }
        }
        return false; 
    }
    
   public int contarZombis() {
        int contador = 0;
        for (Object objeto : entidades) { 
            if (objeto instanceof Zombi) { 
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
