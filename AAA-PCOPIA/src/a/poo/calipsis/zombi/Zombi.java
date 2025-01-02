package a.poo.calipsis.zombi;

import a.poo.calipsis.zombi.Superviviente.TipoDeHerida;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Zombi implements Activable, Serializable {

    private int id;
    private boolean vivo;
    private TipoZombi tipo;
    private boolean esNormal;
    private boolean esBerserker;
    private boolean esToxico;
    private Coordenada posicion;
    Tablero tablero;
    Juego juego;
    private Map<Superviviente, Set<TipoDeHerida>> supervivientesHeridos;
    AlmacenAtaques almacen = new AlmacenAtaques(); // Crea la instancia de AlmacenAtaques
    String rutaAlmacenAtaques = "ruta/a/almacen"; // Establece la ruta
   
    public Zombi(int id, TipoZombi tipo, boolean esNormal, boolean esBerserker, boolean esToxico,Coordenada posicion) {
        this.id = id++;
        this.tipo = tipo;
        this.vivo = true;
        this.esNormal=esNormal;
        this.esBerserker = esBerserker;
        this.esToxico = esToxico;
        this.posicion = posicion;
        this.supervivientesHeridos =  new HashMap<>();
    }
    
    public int getId() {
        return id;
    }

    public int getActivaciones() {
        return tipo.getActivaciones();
    }
    
    public int getAguante(){
        return tipo.getAguante();
    }
    
    public boolean isVivo() {
        return vivo;
    }
   
    public void setVivo(boolean vivo) {
        this.vivo = vivo;
    }

    public Coordenada getPosicion() {
        return posicion;
    }

    public TipoZombi getTipo() {
        return tipo;
    }

    public boolean isNormal() {
        return esNormal;
    }

    public boolean isBerserker() {
        return esBerserker;
    }

    public boolean isToxico() {
        return esToxico;
    }

    // Obtener estadísticas de un zombi
    public Map<Superviviente, Set<TipoDeHerida>> getEstadisticasHeridos() {
        return supervivientesHeridos;
    }
    
    public int compareTo(Zombi otroZombi) {
        return Integer.compare(this.id, otroZombi.id);
    }
    
    // Método para añadir un superviviente herido con un tipo de herida
    public void añadirSupervivienteHerido(Superviviente superviviente, TipoDeHerida tipoDeHerida) {
        supervivientesHeridos.putIfAbsent(superviviente, new HashSet<>());
        supervivientesHeridos.get(superviviente).add(tipoDeHerida);

        System.out.println("El zombi " + id + " registró al superviviente " + superviviente.getNombre() +
                " con una herida de tipo " + tipoDeHerida);
    }
    
    public void mostrarEstadisticas() {
        System.out.println("Estadísticas de heridos para el Zombi ID: " + id);
        for (Map.Entry<Superviviente, Set<TipoDeHerida>> entry : supervivientesHeridos.entrySet()) {
            Superviviente superviviente = entry.getKey();
            Set<TipoDeHerida> tiposDeHerida = entry.getValue();

            System.out.println("- Superviviente: " + superviviente.getNombre());
            System.out.println("  Tipos de heridas: " + tiposDeHerida);
        }
    }

    // Método para mostrar las estadísticas de un zombi específico por ID
    public void mostrarHeridosPorZombi(int zombiId) {
        if (this.id == zombiId) {
            mostrarEstadisticas();
        } else {
            System.out.println("No se encontraron heridas para el Zombi con ID: " + zombiId);
        }
    }

    public void activar() {
        System.out.println("El zombi " + id + " del tipo " + tipo + " se activa.");
        // Aquí iría la lógica de movimiento o ataque.
    }

    public boolean recibirAtaque(int potencia, boolean esAtaqueADistancia) {
        if (!vivo) {
            System.out.println("El zombi " + id + " ya está muerto.");
            return false;
        }

        // Manejo de inmune a distancia
        if (esBerserker && esAtaqueADistancia) {
            System.out.println("El zombi " + id + " es inmune a ataques a distancia.");
            return false;
        }

        // Verificar si el ataque supera el aguante
        if (potencia >= tipo.getAguante()) {
            vivo = false;
            System.out.println("El zombi " + id + " ha sido eliminado.");

            // Si es tóxico, causa daño
            if (esToxico) {
                System.out.println("¡El zombi tóxico causa una herida al atacante!");
            }
            return true;
        } else {
            System.out.println("El ataque no fue suficiente para eliminar al zombi " + id + ".");
            return false;
        }
    }
    
    // Método para calcular la distancia entre dos coordenadas
    private int calcularDistancia(Coordenada coord1, Coordenada coord2) {
        return Math.abs(coord1.getFila() - coord2.getFila()) + Math.abs(coord1.getColumna() - coord2.getColumna());
    }

    public void actualizarListaSupervivientes(List<Superviviente> supervivientes) {
        supervivientes.removeIf(s -> !s.isVivo()); // Elimina supervivientes no vivos
    }
    @Override
    public void moverse(Zombi zombi, List<Superviviente> supervivientes, Tablero tablero) {
        

        int activaciones = zombi.getActivaciones(); // Número de activaciones que el zombi puede realizar en un turno
        while (activaciones > 0) {
            Superviviente objetivo = null;
            int distanciaMinima = Integer.MAX_VALUE;

            // Buscar al superviviente más cercano
            for (Superviviente s : supervivientes) {
                Coordenada posicionSuperviviente = s.getCoordenadas();
                int distancia = calcularDistancia(this.posicion, posicionSuperviviente);

                if (distancia < distanciaMinima) {
                    distanciaMinima = distancia;
                    objetivo = s;
                }
            }

           if (objetivo == null ) {
             if (supervivientes.isEmpty()) {
                System.out.println("No quedan supervivientes en el juego. Fin de la activación del zombi.");
                return; // Salir del método de activación del zombi
            }

            continue; // Continúa buscando otros supervivientes
}

            Coordenada posicionSuperviviente = objetivo.getCoordenadas();

            // Si está en la misma casilla que el superviviente, intenta morder
            if (this.posicion.equals(posicionSuperviviente)) {
                
                System.out.println("El zombi " + id + " está en la misma casilla que el superviviente " + objetivo.getNombre());
                atacar(tablero, almacen,rutaAlmacenAtaques,objetivo); // Consume una activación al atacar
              
                activaciones--; // Reducir el número de activaciones restantes
                tablero.mostrarTablero();
                continue;
            }

            // Si no está en la misma casilla, intenta moverse
            Coordenada posicionAnterior = new Coordenada(this.posicion.getFila(), this.posicion.getColumna());
            int nuevaFila = this.posicion.getFila();
            int nuevaColumna = this.posicion.getColumna();

             if (nuevaFila != posicionSuperviviente.getFila()) {
            nuevaFila += (nuevaFila < posicionSuperviviente.getFila()) ? 1 : -1; // Mover hacia arriba o abajo
        } else if (nuevaColumna != posicionSuperviviente.getColumna()) {
            nuevaColumna += (nuevaColumna < posicionSuperviviente.getColumna()) ? 1 : -1; // Mover hacia izquierda o derecha
        }
            // Verificar si el movimiento es válido
            if (tablero.esPosicionValida(nuevaFila, nuevaColumna)) {
                this.posicion.setFila(nuevaFila);
                this.posicion.setColumna(nuevaColumna);
                tablero.actualizarTablero(zombi, posicionAnterior, this.posicion);

                System.out.println("El zombi " + id + " se movió de " + posicionAnterior + " a " + this.posicion + 
                                   " hacia el superviviente más cercano: " + objetivo.getNombre());
            } else {
                System.out.println("El zombi no puede moverse a una posición ocupada o fuera del tablero.");
                break;
            }

            activaciones--; // Reducir el número de activaciones restantes por el movimiento
        }
    }
    
    public void acercarseAlSupervivienteSimulacion(Zombi zombi, List<Superviviente> supervivientes, Tablero tablero) {
        if (supervivientes.isEmpty()) {
            System.out.println("No hay supervivientes para perseguir.");
            return;
        }

        int activaciones = zombi.getActivaciones(); // Número de activaciones que el zombi puede realizar en un turno
        while (activaciones > 0) {
            Superviviente objetivo = null;
            int distanciaMinima = Integer.MAX_VALUE;

            // Buscar al superviviente más cercano
            for (Superviviente s : supervivientes) {
                Coordenada posicionSuperviviente = s.getCoordenadas();
                int distancia = calcularDistancia(this.posicion, posicionSuperviviente);

                if (distancia < distanciaMinima) {
                    distanciaMinima = distancia;
                    objetivo = s;
                }
            }

           if (objetivo == null || !objetivo.isVivo()) {
            System.out.println("Superviviente no válido, buscando otro objetivo...");
            actualizarListaSupervivientes(supervivientes); // Asegurar lista actualizada
             if (supervivientes.isEmpty()) {
                System.out.println("No quedan supervivientes en el juego. Fin de la activación del zombi.");
                return; // Salir del método de activación del zombi
            }

            continue; // Continúa buscando otros supervivientes
}

            Coordenada posicionSuperviviente = objetivo.getCoordenadas();

            // Si está en la misma casilla que el superviviente, intenta morder
            if (this.posicion.equals(posicionSuperviviente)) {
                
                System.out.println("El zombi " + id + " está en la misma casilla que el superviviente " + objetivo.getNombre());
                atacar(tablero, almacen,rutaAlmacenAtaques,objetivo); // Consume una activación al atacar
              
                activaciones--; // Reducir el número de activaciones restantes
                tablero.mostrarTablero();
                continue;
            }

            // Si no está en la misma casilla, intenta moverse
            Coordenada posicionAnterior = new Coordenada(this.posicion.getFila(), this.posicion.getColumna());
            int nuevaFila = this.posicion.getFila();
            int nuevaColumna = this.posicion.getColumna();

             if (nuevaFila != posicionSuperviviente.getFila()) {
            nuevaFila += (nuevaFila < posicionSuperviviente.getFila()) ? 1 : -1; // Mover hacia arriba o abajo
        } else if (nuevaColumna != posicionSuperviviente.getColumna()) {
            nuevaColumna += (nuevaColumna < posicionSuperviviente.getColumna()) ? 1 : -1; // Mover hacia izquierda o derecha
        }
            // Verificar si el movimiento es válido
            if (tablero.esPosicionValida(nuevaFila, nuevaColumna)) {
                this.posicion.setFila(nuevaFila);
                this.posicion.setColumna(nuevaColumna);
                tablero.actualizarTablero(zombi, posicionAnterior, this.posicion);

                System.out.println("El zombi " + id + " se movió de " + posicionAnterior + " a " + this.posicion + 
                                   " hacia el superviviente más cercano: " + objetivo.getNombre());
            } else {
                System.out.println("El zombi no puede moverse a una posición ocupada o fuera del tablero.");
                break;
            }

            activaciones--; // Reducir el número de activaciones restantes por el movimiento
        }
    }
   
    // Método para atacar al superviviente
    public void atacar(Tablero tablero, AlmacenAtaques almacen, String rutaAlmacenAtaques,Superviviente objetivo) {
        System.out.println("El zombi " + id + " ha mordido al superviviente " + objetivo.getNombre());
        objetivo.recibirMordedura(this);
         // Después de que el superviviente reciba la herida y potencialmente muera, se verifica el fin del juego
    }

    
    public void moverse() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


    @Override
    public Coordenada getCoordenadas() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


    public static Zombi desdeString(String texto) {
        String[] partes = texto.split(" - ");
        int id = Integer.parseInt(partes[0].split(": ")[1]);
        TipoZombi tipo = TipoZombi.valueOf(partes[1].split(": ")[1].trim());
        boolean esNormal = Boolean.parseBoolean(partes[2].split(": ")[1]);
        boolean esBerserker = Boolean.parseBoolean(partes[3].split(": ")[1]);
        boolean esToxico = Boolean.parseBoolean(partes[4].split(": ")[1]);
        String[] coordenadas = partes[5].split(": ")[1].replace("(", "").replace(")", "").split(", ");
        Coordenada posicion = new Coordenada(Integer.parseInt(coordenadas[0]), Integer.parseInt(coordenadas[1]));
        return new Zombi(id, tipo, esNormal, esBerserker, esToxico, posicion);
    }

    @Override
    public String toString() {
        return "ID: " + id + " - Tipo: " + tipo + " - Normal: " + esNormal + 
               " - Berserker: " + esBerserker + " - Tóxico: " + esToxico + 
               " - Posición: (" + posicion+ ")";
    }
    
}
