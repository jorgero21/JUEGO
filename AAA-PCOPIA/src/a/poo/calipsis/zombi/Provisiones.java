package a.poo.calipsis.zombi;

import java.time.LocalDate;
import java.util.Objects;

public class Provisiones extends Equipo {
    private int valorEnergetico;
    private LocalDate caducidad;

    // Constructor
    public Provisiones(String nombre, int valorEnergetico, LocalDate caducidad) {
        super(nombre); // Usa el atributo 'nombre' de la clase base
        this.valorEnergetico = valorEnergetico;
        this.caducidad = caducidad;
    }

    // Getters
    public int getValorEnergetico() {
        return valorEnergetico;
    }

    public LocalDate getCaducidad() {
        return caducidad;
    }

    // Sobrescribir equals y hashCode basados en el atributo 'nombre' de la superclase
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Provisiones that = (Provisiones) obj;
        return getNombre().equalsIgnoreCase(that.getNombre()); // Usa getNombre() de la superclase
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNombre().toLowerCase());
    }

    // Sobrescribir toString para una representación legible
    @Override
    public String toString() {
        return String.format("Provisiones{nombre='%s', valorEnergetico=%d kCal, caducidad=%s}",
                getNombre(), valorEnergetico, caducidad);
    }

    @Override
    public String detalles() {
        return String.format("Nombre: %s, Valor Energético: %d kCal, Caducidad: %s",
                getNombre(), valorEnergetico, caducidad);
    }
}
