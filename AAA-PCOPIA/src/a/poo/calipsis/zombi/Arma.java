package a.poo.calipsis.zombi;

public class Arma extends Equipo {
    private int potencia;
    private int alcance;
    private int numeroDados;
    private int valorExito;

    public Arma(String nombre, int potencia, int alcance, int numeroDados, int valorExito) {
        super(nombre); 
        this.potencia = potencia;
        this.alcance = alcance;
        this.numeroDados = numeroDados;
        this.valorExito = valorExito;
    }

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Arma arma = (Arma) obj;
        return getNombre().equalsIgnoreCase(arma.getNombre()); 
    }
    @Override
    public String detalles() {
        return String.format("Nombre: %s, Potencia: %d, Alcance: %d, Dados: %d, Valor Exito: %d", getNombre(), potencia, alcance, numeroDados, valorExito);
    }
    
    @Override
    public String toString() {
        return String.format("Arma{nombre='%s', potencia=%d, alcance=%d, numeroDados=%d, valorExito=%d}",  getNombre(), potencia, alcance, numeroDados, valorExito);
    }
}
