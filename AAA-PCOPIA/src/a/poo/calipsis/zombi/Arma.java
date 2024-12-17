package a.poo.calipsis.zombi;

import java.util.Objects;

public class Arma extends Equipo {
    private int potencia;
    private int alcance;
    private int numeroDados;
    private int valorExito;

    // Constructor
    public Arma(String nombre, int potencia, int alcance, int numeroDados, int valorExito) {
        super(nombre); // Usa el nombre de la clase base
        this.potencia = potencia;
        this.alcance = alcance;
        this.numeroDados = numeroDados;
        this.valorExito = valorExito;
    }

    // Getters
    public int getPotencia() {
        return potencia;
    }

    public int getAlcance() {
        return alcance;
    }

    public int getNumeroDados() {
        return numeroDados;
    }

    public int getValorExito() {
        return valorExito;
    }

    // Sobrescribir equals y hashCode basados en el nombre de la superclase
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Arma arma = (Arma) obj;
        return getNombre().equalsIgnoreCase(arma.getNombre()); // Usa getNombre() de la superclase
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNombre().toLowerCase());
    }

    // Sobrescribir toString
    @Override
    public String toString() {
        return String.format("Arma{nombre='%s', potencia=%d, alcance=%d, numeroDados=%d, valorExito=%d}",
                getNombre(), potencia, alcance, numeroDados, valorExito);
    }

    @Override
    public String detalles() {
        return String.format("Nombre: %s, Potencia: %d, Alcance: %d, Dados: %d, Valor Éxito: %d",
                getNombre(), potencia, alcance, numeroDados, valorExito);
    }
}
