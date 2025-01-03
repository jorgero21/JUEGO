
package a.poo.calipsis.zombi;

/**
 *
 * @author Jorge
 */
public enum TipoZombi {
    CAMINANTE(1, 1),
    CORREDOR(1, 2),
    ABOMINACION(3, 1);

    private final int aguante;
    private int activaciones;

    TipoZombi(int aguante, int activaciones) {
        this.aguante = aguante;
        this.activaciones = activaciones;
    }

    public int getAguante() {
        return aguante;
    }

    public int getActivaciones() {
        return activaciones;
    }
     public void restarActivaciones() {
        activaciones--;
    }
   
}
