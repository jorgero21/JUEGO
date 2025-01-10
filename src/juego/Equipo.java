package a.poo.calipsis.zombi;

import java.io.Serializable;

public abstract class Equipo implements Serializable {
    protected String nombre;

    public Equipo(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public abstract String detalles();
}
