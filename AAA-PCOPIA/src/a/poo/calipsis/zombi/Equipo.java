package a.poo.calipsis.zombi;

import java.io.Serializable;

public abstract class Equipo implements Serializable {
    protected String nombre;

    // Constructor
    public Equipo(String nombre) {
        this.nombre = nombre;
    }

    // Getter y Setter
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Método abstracto para mostrar detalles del equipo
    public abstract String detalles();
}
