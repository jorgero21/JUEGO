package a.poo.calipsis.zombi;

import java.time.LocalDate;

public class Provisiones extends Equipo {
    private int valorEnergetico;
    private LocalDate caducidad;

    public Provisiones(String nombre, int valorEnergetico, LocalDate caducidad) {
        super(nombre); 
        this.valorEnergetico = valorEnergetico;
        this.caducidad = caducidad;
    }

    public int getValorEnergetico() {
        return valorEnergetico;
    }

    public LocalDate getCaducidad() {
        return caducidad;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Provisiones that = (Provisiones) obj;
        return getNombre().equalsIgnoreCase(that.getNombre()); // Usa getNombre() de la superclase
    }
    @Override
    public String detalles() {
        return String.format("Nombre: %s, Valor Energetico: %d kCal, Caducidad: %s", getNombre(), valorEnergetico, caducidad);
    }
    @Override
    public String toString() {
        return String.format("Provisiones{nombre='%s', valorEnergetico=%d kCal, caducidad=%s}", getNombre(), valorEnergetico, caducidad);
    }

   
}
