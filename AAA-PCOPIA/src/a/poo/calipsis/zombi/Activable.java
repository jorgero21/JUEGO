package a.poo.calipsis.zombi;

import java.util.List;

public interface Activable {
    void moverse(Zombi zombi, List<Superviviente> supervivientes, Tablero tablero);        
    void atacar(Tablero tablero, AlmacenAtaques almacen, String rutaAlmacenAtaques,Superviviente objetivo);         
    Coordenada getCoordenadas();
}

