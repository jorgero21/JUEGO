package a.poo.calipsis.zombi;

import java.io.Serializable;

public class Coordenada implements Serializable {
    private int fila;
    private int columna;

    public Coordenada(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }
    public int getFila() { return fila; }

    public void setFila(int fila) {   this.fila = fila; }

    public int getColumna() {   return columna; }

    public void setColumna(int columna) {   this.columna = columna; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Coordenada other = (Coordenada) obj;
        if (this.fila != other.fila) {
            return false;
        }
        return this.columna == other.columna;
    }
    
    @Override
    public String toString() {
        return "(" + fila + ", " + columna + ")";
    }
}
