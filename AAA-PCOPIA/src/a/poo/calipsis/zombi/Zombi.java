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
    AlmacenAtaques almacen = new AlmacenAtaques(); 
    String rutaAlmacenAtaques = "ruta/a/almacen"; 
   
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
    
    public int getId() {return id;}

    public int getActivaciones() {  return tipo.getActivaciones(); }
    
    public int getAguante(){  return tipo.getAguante(); }
    
    public boolean isVivo() {  return vivo; }
   
    public void setVivo(boolean vivo) {  this.vivo = vivo; }

    public Coordenada getPosicion() {  return posicion; }

    public TipoZombi getTipo() {   return tipo;}

    public boolean isNormal() {  return esNormal; }

    public boolean isBerserker() { return esBerserker; }

    public boolean isToxico() { return esToxico; }

    public Map<Superviviente, Set<TipoDeHerida>> getEstadisticasHeridos() {  return supervivientesHeridos;}
    
    public int compareTo(Zombi otroZombi) {   return Integer.compare(this.id, otroZombi.id);  }
    
    public void añadirSupervivienteHerido(Superviviente superviviente, TipoDeHerida tipoDeHerida) {
        supervivientesHeridos.putIfAbsent(superviviente, new HashSet<>());
        supervivientesHeridos.get(superviviente).add(tipoDeHerida);
        System.out.println("El zombi " + id + " registro al superviviente " + superviviente.getNombre() +   " con una herida de tipo " + tipoDeHerida);
    }
    
    public void mostrarEstadisticas() {
        System.out.println("Estadisticas de heridos para el Zombi ID: " + id);
        for (Map.Entry<Superviviente, Set<TipoDeHerida>> entry : supervivientesHeridos.entrySet()) {
            Superviviente superviviente = entry.getKey();
            Set<TipoDeHerida> tiposDeHerida = entry.getValue();
            System.out.println("- Superviviente: " + superviviente.getNombre());
            System.out.println("  Tipos de heridas: " + tiposDeHerida);
        }
    }
    
    public void mostrarHeridosPorZombi(int zombiId) {
        if (this.id == zombiId) {
            mostrarEstadisticas();
        } else {
            System.out.println("No se encontraron heridas para el Zombi con ID: " + zombiId);
        }
    }

    public void activar() {
        System.out.println("El zombi " + id + " del tipo " + tipo + " se activa");
    }

    public boolean recibirAtaque(int potencia, boolean esAtaqueADistancia) {
        if (!vivo) {
            System.out.println("El zombi " + id + " ya esta muerto");
            return false;
        }
        if (esBerserker && esAtaqueADistancia) {
            System.out.println("El zombi " + id + " es inmune a ataques a distancia");
            return false;
        }
        if (potencia >= tipo.getAguante()) {
            vivo = false;
            System.out.println("El zombi " + id + " ha sido eliminado");
            if (esToxico) {
                System.out.println("El zombi toxico causa una herida al atacante!");
            }
            return true;
        } else {
            System.out.println("El ataque no fue suficiente para eliminar al zombi " + id + ".");
            return false;
        }
    }
    
    private int calcularDistancia(Coordenada coord1, Coordenada coord2) {
        return Math.abs(coord1.getFila() - coord2.getFila()) + Math.abs(coord1.getColumna() - coord2.getColumna());
    }

    public void actualizarListaSupervivientes(List<Superviviente> supervivientes) {
        supervivientes.removeIf(s -> !s.isVivo());
    }
    @Override
    public void moverse(){
        System.out.println(id+"en accion");
    }
    public void moverse(Zombi zombi, List<Superviviente> supervivientes, Tablero tablero) {
        int activaciones = zombi.getActivaciones(); 
        while (activaciones > 0) {
            moverse();
            Superviviente objetivo = null;
            int distanciaMinima = Integer.MAX_VALUE;
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
                   System.out.println("No quedan supervivientes en el juego. Fin de la activacion del zombi");
                   return; 
               }
            continue; 
            }
            Coordenada posicionSuperviviente = objetivo.getCoordenadas();

            if (this.posicion.equals(posicionSuperviviente)) {
                System.out.println("El zombi " + id + " esta en la misma casilla que el superviviente " + objetivo.getNombre());
                atacar(tablero, almacen,rutaAlmacenAtaques,objetivo); 
                activaciones--; 
                tablero.mostrarTablero();
                continue;
            }
            Coordenada posicionAnterior = new Coordenada(this.posicion.getFila(), this.posicion.getColumna());
            int nuevaFila = this.posicion.getFila();
            int nuevaColumna = this.posicion.getColumna();
             if (nuevaFila != posicionSuperviviente.getFila()) {
            nuevaFila += (nuevaFila < posicionSuperviviente.getFila()) ? 1 : -1; 
        } else if (nuevaColumna != posicionSuperviviente.getColumna()) {
            nuevaColumna += (nuevaColumna < posicionSuperviviente.getColumna()) ? 1 : -1; 
        }
            
            if (tablero.esPosicionValida(nuevaFila, nuevaColumna)) {
                this.posicion.setFila(nuevaFila);
                this.posicion.setColumna(nuevaColumna);
                tablero.actualizarTablero(zombi, posicionAnterior, this.posicion);

                System.out.println("El zombi " + id + " se movio de " + posicionAnterior + " a " + this.posicion +  " hacia el superviviente mas cercano: " + objetivo.getNombre());
            } else {
                System.out.println("El zombi no puede moverse a una posicion ocupada o fuera del tablero");
                break;
            }
            activaciones--; 
        }
    }
    
    public void acercarseAlSupervivienteSimulacion(Zombi zombi, List<Superviviente> supervivientes, Tablero tablero) {
        if (supervivientes.isEmpty()) {
            System.out.println("No hay supervivientes para perseguir");
            return;
        }
        int activaciones = zombi.getActivaciones(); 
        while (activaciones > 0) {
            Superviviente objetivo = null;
            int distanciaMinima = Integer.MAX_VALUE;
            for (Superviviente s : supervivientes) {
                Coordenada posicionSuperviviente = s.getCoordenadas();
                int distancia = calcularDistancia(this.posicion, posicionSuperviviente);
                if (distancia < distanciaMinima) {
                    distanciaMinima = distancia;
                    objetivo = s;
                }
            }
           if (objetivo == null || !objetivo.isVivo()) {
            System.out.println("Superviviente no valido, buscando otro objetivo...");
            actualizarListaSupervivientes(supervivientes); 
             if (supervivientes.isEmpty()) {
                System.out.println("No quedan supervivientes en el juego. Fin de la activacion del zombi");
                return; 
            }
            continue; 
}
            Coordenada posicionSuperviviente = objetivo.getCoordenadas();
            if (this.posicion.equals(posicionSuperviviente)) {
                System.out.println("El zombi " + id + " esta en la misma casilla que el superviviente " + objetivo.getNombre());
                atacar(tablero, almacen,rutaAlmacenAtaques,objetivo);
                activaciones--; 
                tablero.mostrarTablero();
                continue;
            }

            // Si no está en la misma casilla, intenta moverse
            Coordenada posicionAnterior = new Coordenada(this.posicion.getFila(), this.posicion.getColumna());
            int nuevaFila = this.posicion.getFila();
            int nuevaColumna = this.posicion.getColumna();

             if (nuevaFila != posicionSuperviviente.getFila()) {
            nuevaFila += (nuevaFila < posicionSuperviviente.getFila()) ? 1 : -1; 
        } else if (nuevaColumna != posicionSuperviviente.getColumna()) {
            nuevaColumna += (nuevaColumna < posicionSuperviviente.getColumna()) ? 1 : -1; 
        }
            if (tablero.esPosicionValida(nuevaFila, nuevaColumna)) {
                this.posicion.setFila(nuevaFila);
                this.posicion.setColumna(nuevaColumna);
                tablero.actualizarTablero(zombi, posicionAnterior, this.posicion);
                System.out.println("El zombi " + id + " se movio de " + posicionAnterior + " a " + this.posicion +  " hacia el superviviente mAs cercano: " + objetivo.getNombre());
                tablero.mostrarTablero();
            } else {
                System.out.println("El zombi no puede moverse a una posiciOn ocupada o fuera del tablero");
                break;
            }
            activaciones--; 
        }
    }
    @Override
   public void atacar(){
        System.out.println("Ataque del zombi");
   }
   
    public void atacar(Tablero tablero, AlmacenAtaques almacen, String rutaAlmacenAtaques,Superviviente objetivo) {
        atacar();
        System.out.println("El zombi " + id +" ha mordido al superviviente " + objetivo.getNombre());
        objetivo.recibirMordedura(this);
    }

   @Override
    public Coordenada getCoordenadas() { return posicion; }

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
        return "ID: " + id + " - Tipo: " + tipo + " - Normal: " + esNormal +   " - Berserker: " + esBerserker + " - Toxico: " + esToxico +     " - Posicion: (" + posicion+ ")";
    }
    
}
