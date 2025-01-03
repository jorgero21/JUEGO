/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package a.poo.calipsis.zombi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.util.Scanner;

public class Superviviente implements Activable, Serializable {
    
    private String nombre;
    private boolean vivo; 
    private int zombis_eliminados;
    private int heridas_recibidas;
    private static final int MAX_HERIDAS = 2;
    private static final int MAX_ACCIONES = 3;
    private static final int MAX_INVENTARIO=5;
    private int acciones=MAX_ACCIONES;
    private List<Arma> armasActivas;
    private Coordenada coordenada; 
    private Tablero tablero;
    private Juego juego;
    private List<Equipo> inventario;
    private String tipoDañado;
    private List<Zombi> historico = new ArrayList<>();
    private List<Zombi> actual = new ArrayList<>();
    private List<TipoDeHerida> heridasRecibidas; 
    private Set<Equipo> equiposEntregados = new HashSet<>();
    private List<Zombi> zombisEliminados = new ArrayList<>();
    
    public Superviviente(String nombre) {
        this.nombre = nombre;
        this.vivo = true;
        this.inventario = new ArrayList<>();
        this.heridasRecibidas = new ArrayList<>();
        this.armasActivas = new ArrayList<>();  
        this.zombis_eliminados = 0;
        this.heridas_recibidas = 0;
        this.coordenada = new Coordenada(0, 0); 
    }
    
    public Superviviente(String nombre, Coordenada coordenada) {
        this.nombre = nombre;
        this.vivo = true;
        this.inventario = new ArrayList<>();
        this.armasActivas = new ArrayList<>();
        this.zombis_eliminados = 0;
        this.heridas_recibidas = 0;
        this.heridasRecibidas = new ArrayList<>();
        this.coordenada = coordenada; 
    }
 
    public enum TipoDeHerida {
        MORDEDURA,HERIDA
    }
    
    @Override
    public Coordenada getCoordenadas() { return coordenada; }
    
    public boolean equipoEntregado(Equipo equipo) {  return equiposEntregados.contains(equipo); }

    public void agregarEquipoEntregado(Equipo equipo) {  equiposEntregados.add(equipo); }

    public String getNombre() {  return nombre; }
    
    public List<Zombi> getZombisEliminados() {  return zombisEliminados; }
    
    public void agregarZombisEliminados(Zombi zombi) {
        if (this.zombisEliminados == null) this.zombisEliminados = new ArrayList<>();

        this.zombisEliminados.add(zombi);
    }

    public int getZombis_eliminados() {  return zombis_eliminados; }

    public int getHeridas_recibidas() {  return heridas_recibidas;}

    public void setZombis_eliminados(int zombis_eliminados) { this.zombis_eliminados = zombis_eliminados; }

    public void setHeridas_recibidas(int heridas_recibidas) {  this.heridas_recibidas = heridas_recibidas; }

    public boolean isVivo() {  return vivo; }

    public void setVivo(boolean vivo) { this.vivo = vivo;}
    
    public int getAcciones() { return acciones; }
    
    public Set<Arma> getArmas() {
        Set<Arma> armas = new HashSet<>();
        for (Equipo equipo : inventario) {
            if (equipo instanceof Arma) {
                armas.add((Arma) equipo);
            }
        }
        return armas;
    }

    public Set<Provisiones> getProvisiones() {
        Set<Provisiones> provisiones = new HashSet<>();
        for (Equipo equipo : inventario) {
            if (equipo instanceof Provisiones) {
                provisiones.add((Provisiones) equipo);
            }
        }
        return provisiones;
    }

    public List<Arma> getArmasActivas() { return armasActivas;}

    public List<Equipo> getInventario() {  return inventario;  }

    public void setTablero(Tablero tablero) {   this.tablero = tablero;  }
    
    public List<TipoDeHerida> getHeridasRecibidas() {  return heridasRecibidas;}
      
    public String getTipoLesion() { return tipoDañado; }

    public List<Zombi> getHistorico() {  return historico;}

    public List<Zombi> getActual() {    return actual;  }

    public void restarAccion(){   this.acciones-=1;  }
    
     public void restarAccionesExtras(int extras){   this.acciones-=1+extras; }

    public void reiniciarAcciones() { this.acciones = 3;
    }

    public void recibirDaño() {
        heridas_recibidas++;
        if (heridas_recibidas >= MAX_HERIDAS) {
            vivo = false;
            setVivo(false);
            System.out.println(nombre + " ha recibido 2 heridas y ha sido eliminado");
        if (tablero != null) {
            tablero.eliminarSuperviviente(this); 
        }
        } else {
            System.out.println(nombre + " ha recibido una herida. Total de heridas: " + heridas_recibidas);
        }
    }

    public int contarProvisiones() {
        int cantidadProvisiones = 0;
        for (Equipo equipo : this.getInventario()) {
            if (equipo instanceof Provisiones) {
                cantidadProvisiones++;  
            }
        }
        return cantidadProvisiones;
    }

    public void recibirMordedura(Zombi zombi) {
        System.out.println("Mordedura causada por Zombi: " + zombi.getId());
        heridasRecibidas.add(TipoDeHerida.MORDEDURA); 
        zombi.añadirSupervivienteHerido(this,TipoDeHerida.MORDEDURA);
        recibirDaño();
    }
 
    public void recibirHerida(Zombi zombi) {
          System.out.println("Herida causada por Zombi: " + zombi.getId());
        heridasRecibidas.add(TipoDeHerida.HERIDA); 
           zombi.añadirSupervivienteHerido(this,TipoDeHerida.HERIDA);
        recibirDaño ();
    }
    
    public void reiniciarInventario() { this.inventario.clear(); }

    public void reiniciarEstado() {
        this.vivo = true;
        this.zombis_eliminados = 0;
        reiniciarAcciones(); 
    }

    public void agregarZombiEliminado(Zombi zombi) {
            historico.add(zombi);
            actual.add(zombi);
    }
    
    @Override
    public void moverse() {
        if (this.tablero == null) { 
            System.out.println("Error: El tablero no esta asociado al superviviente");
            return;
        }
        int zombisEnCasillaOrigen = tablero.contarZombisEnCasilla(coordenada);
        System.out.println("Zombis en la casilla origen: " + zombisEnCasillaOrigen);
        int costoTotal = 1 + zombisEnCasillaOrigen; 
        if (this.acciones < costoTotal) { 
            System.out.println("No tienes suficientes acciones para moverte. Requiere " + costoTotal + " acciones");
            return;
        }else{
             System.out.println("Puedes moverte");
        }
        Coordenada nuevaPosicion = new Coordenada(coordenada.getFila(), coordenada.getColumna());
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hacia donde quieres moverte? (arriba/abajo/izquierda/derecha):");
        String direccion = scanner.nextLine();

        switch (direccion.toLowerCase()) {
            case "arriba":
                nuevaPosicion.setColumna(nuevaPosicion.getColumna() + 1);
                break;
            case "abajo":
                nuevaPosicion.setColumna(nuevaPosicion.getColumna() - 1);
                break;
            case "izquierda":
                nuevaPosicion.setFila(nuevaPosicion.getFila() - 1);
                break;
            case "derecha":
                nuevaPosicion.setFila(nuevaPosicion.getFila() + 1);
                break;
            default:
                System.out.println("Direccion no valida. Intenta de nuevo.");
                return;
        }

        System.out.println("Intentando mover a: " + nuevaPosicion);
        if (this.tablero.esPosicionValida(nuevaPosicion)) {
            tablero.actualizarTablero(this, coordenada, nuevaPosicion);
            this.coordenada = nuevaPosicion;
            for (int i = 0; i < costoTotal; i++) {
                restarAccion();
            }
            System.out.println(this.nombre + " se ha movido a " + nuevaPosicion + " gastando " + costoTotal + " acciones");
        } else {
            System.out.println("Movimiento no valido. Estas tratando de salir del tablero");
        }
    }

     @Override
    public void atacar() {
            System.out.println(nombre + " se prepara para atacar");
    }
  
    public void atacar(Tablero tablero, AlmacenAtaques almacen, String rutaAlmacenAtaques, Superviviente objetivo)  {

        if (armasActivas.isEmpty()) {
            System.out.println("No tienes armas activas. Usa el comando 'elegirArmaActiva' para activar una");
            return;
        }

        atacar();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Selecciona un arma activa para atacar:");
        for (int i = 0; i < armasActivas.size(); i++) {
            System.out.println((i + 1) + ". " + armasActivas.get(i).getNombre() + 
                               " (Alcance: " + armasActivas.get(i).getAlcance() + 
                               ", Dados: " + armasActivas.get(i).getNumeroDados() + 
                               ", Potencia: " + armasActivas.get(i).getPotencia() + ")");
        }
        int seleccion = -1;
        while (seleccion < 1 || seleccion > armasActivas.size()) {
            System.out.print("Ingresa el numero correspondiente al arma: ");
            if (scanner.hasNextInt()) {
                seleccion = scanner.nextInt();
                if (seleccion < 1 || seleccion > armasActivas.size()) {
                    System.out.println("Seleccion invalida. Elige un numero entre 1 y " + armasActivas.size() + ".");
                }
            } else {
                System.out.println("Entrada invalida. Por favor, ingresa un numero");
                scanner.next(); 
            }
        }
        Arma armaSeleccionada = armasActivas.get(seleccion - 1);
        Ataque ataque = new Ataque(armaSeleccionada.getNumeroDados());
        int exitos = ataque.lanzarDados(armaSeleccionada.getValorExito()); 
        if (exitos > 0) {
            System.out.println("Se han obtenido " + exitos + " exitos");
            while (exitos > 0) {
                String decision = "";
                while (true) {
                    System.out.println("Deseas usar el siguiente exito para atacar? (s/n)");
                    decision = scanner.next();
                    if (decision.equalsIgnoreCase("s")) {
                        System.out.println("Introduce las coordenadas de la casilla objetivo:");
                        tablero.mostrarTablero();

                        int fila = -1;
                        while (fila < 0) {
                            System.out.print("Fila: ");
                            if (scanner.hasNextInt()) {
                                fila = scanner.nextInt();
                                if (fila < 0) {
                                    System.out.println("Por favor, introduce un numero positivo para la fila");
                                }
                            } else {
                                System.out.println("Entrada invalida. Debes ingresar un numero");
                                scanner.next(); 
                            }
                        }

                        int columna = -1;
                        while (columna < 0) {
                            System.out.print("Columna: ");
                            if (scanner.hasNextInt()) {
                                columna = scanner.nextInt();
                                if (columna < 0) {
                                    System.out.println("Por favor, introduce un numero positivo para la columna");
                                }
                            } else {
                                System.out.println("Entrada invalida. Debes ingresar un numero");
                                scanner.next(); 
                            }
                        }
                        Casilla casillaObjetivo = tablero.getCasilla(fila, columna);
                        if (casillaObjetivo == null) {
                            System.out.println("La casilla objetivo no existe. Intenta de nuevo");
                            continue; 
                        }
                        int distancia = tablero.calcularDistancia(this.coordenada, casillaObjetivo.getCoordenadas());
                        if (armaSeleccionada.getAlcance() == 0 && distancia > 0) {
                            System.out.println("El arma es de cuerpo a cuerpo. Debes atacar una casilla donde estes");
                            continue; 
                        }
                        if (armaSeleccionada.getAlcance() > 0 && distancia > armaSeleccionada.getAlcance()) {
                            System.out.println("La casilla esta fuera del alcance del arma");
                            continue; 
                        }
                        List<Zombi> zombisEnCasilla = new ArrayList<>();
                        for (Object entidad : casillaObjetivo.getEntidades()) {
                            if (entidad instanceof Zombi) {
                                zombisEnCasilla.add((Zombi) entidad);
                            }
                        }
                        if (zombisEnCasilla.isEmpty()) {
                            System.out.println("No hay zombis en la casilla seleccionada");
                            continue; 
                        }
                        for (Zombi z : zombisEnCasilla) {
                            if (z.isBerserker() && armaSeleccionada.getAlcance() > 0) {
                                this.restarAccion();
                                System.out.println("El zombi Berserker es inmune a ataques a distancia");
                                exitos--; 
                                continue;
                            }
                            if (z.getAguante() <= armaSeleccionada.getPotencia()) {
                                this.restarAccion();
                                System.out.println("El zombi " + z.getTipo() + " ha sido eliminado");
                                agregarZombiEliminado(z);
                                exitos--;
                                z.setVivo(false);
                                if (z.isToxico() && this.coordenada.equals(casillaObjetivo.getCoordenadas())) {
                                    System.out.println("El zombi toxico te ha causado una herida al ser eliminado!");
                                    recibirHerida(z);
                                }
                                casillaObjetivo.eliminarEntidad(z);
                                tablero.mostrarTablero();
                                agregarZombisEliminados(z);
                                zombis_eliminados++;
                                almacen.registrarAtaque(ataque, rutaAlmacenAtaques);
                                System.out.println("Numero de ataques: " + almacen.getAtaques2().size());
                            } else {
                                System.out.println("El zombi " + z.getTipo() + " es demasiado resistente para este ataque");
                                this.restarAccion();
                                exitos--; 
                            }
                            if (exitos == 0) {
                                break;
                            }
                        }

                        if (exitos == 0) {
                            System.out.println("No hay mas exitos disponibles");
                        }
                    }else if (decision.equalsIgnoreCase("n")) {
                        exitos--;  
                        System.out.println("El exito no se utilizara");
                        break; 
                    }else {
                        System.out.println("Entrada invalida. Por favor, ingresa 's' para usar el exito o 'n' para no usarlo");
                    }
                }

            }

        } else {
            System.out.println("No se consiguieron exitos. El ataque ha fallado");
        }
    }

    public void atacarSimulacion(Tablero tablero, AlmacenAtaques almacen, Juego j) throws IOException {
        if (armasActivas.isEmpty()) {
            System.out.println("No tienes armas activas. Usa el comando 'elegirArmaActiva' para activar una");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Selecciona un arma activa para atacar:");
        for (int i = 0; i < armasActivas.size(); i++) {
            System.out.println((i + 1) + ". " + armasActivas.get(i).getNombre() + 
                               " (Alcance: " + armasActivas.get(i).getAlcance() + 
                               ", Dados: " + armasActivas.get(i).getNumeroDados() + 
                               ", Potencia: " + armasActivas.get(i).getPotencia() + ")");
        }
        int seleccion = scanner.nextInt();
        if (seleccion < 1 || seleccion > armasActivas.size()) {
            System.out.println("Seleccion invalida. Ataque cancelado");
            return;
        }
        Arma armaSeleccionada = armasActivas.get(seleccion - 1);
        Ataque ataque = new Ataque(armaSeleccionada.getNumeroDados());
        int exitos = ataque.lanzarDados(armaSeleccionada.getValorExito()); 
        if (exitos > 0) {
            System.out.println("Se han obtenido " + exitos + " exitos");

            while (exitos > 0) {
                System.out.println("Deseas usar el siguiente exito para atacar? (s/n)");
                String decision = scanner.next();
                if (decision.equalsIgnoreCase("s")) {
                    System.out.println("Introduce las coordenadas de la casilla objetivo:");
                    tablero.mostrarTablero();
                    System.out.print("Fila: ");
                    int fila = scanner.nextInt();
                    System.out.print("Columna: ");
                    int columna = scanner.nextInt();
                    Casilla casillaObjetivo = tablero.getCasilla(fila, columna);
                    if (casillaObjetivo == null) {
                        System.out.println("La casilla objetivo no existe. Intenta de nuevo");
                        continue; 
                    }
                    int distancia = tablero.calcularDistancia(this.coordenada, casillaObjetivo.getCoordenadas());
                    if (armaSeleccionada.getAlcance() == 0 && distancia > 0) {
                        System.out.println("El arma es de cuerpo a cuerpo. Debes atacar una casilla donde estes");
                        continue; 
                    }
                    if (armaSeleccionada.getAlcance() > 0 && distancia > armaSeleccionada.getAlcance()) {
                        System.out.println("La casilla está fuera del alcance del arma");
                        continue; 
                    }
                    List<Zombi> zombisEnCasilla = new ArrayList<>();
                    for (Object entidad : casillaObjetivo.getEntidades()) {
                        if (entidad instanceof Zombi) {
                            zombisEnCasilla.add((Zombi) entidad);
                        }
                    }
                    if (zombisEnCasilla.isEmpty()) {
                        System.out.println("No hay zombis en la casilla seleccionada");
                        continue; 
                    }
                    for (Zombi z : zombisEnCasilla) {
                        if (z.isBerserker() && armaSeleccionada.getAlcance() > 0) {
                            this.restarAccion();
                            System.out.println("El zombi Berserker es inmune a ataques a distancia");
                            exitos--; 
                            continue;
                        }
                        if (z.getAguante() <= armaSeleccionada.getPotencia()) {
                            this.restarAccion();
                            System.out.println("El zombi " + z.getTipo() + " ha sido eliminado");
                            agregarZombiEliminado(z);
                            exitos--;
                            z.setVivo(false);
                            if (z.isToxico() && this.coordenada.equals(casillaObjetivo.getCoordenadas())) {
                                System.out.println("El zombi toxico te ha causado una herida al ser eliminado!");
                                recibirHerida(z);
                            }
                            casillaObjetivo.eliminarEntidad(z); 
                            tablero.mostrarTablero();
                            agregarZombisEliminados(z);
                            zombis_eliminados++;
                        } else {
                            System.out.println("El zombi " + z.getTipo() + " es demasiado resistente para este ataque");
                            this.restarAccion();
                            exitos--; 
                        }
                        if (exitos == 0) {
                            break;
                        }
                    }
                    if (exitos == 0) {
                        System.out.println("No hay mas exitos disponibles");
                    }

                } else if (decision.equalsIgnoreCase("n")) {
                System.out.println("El exito no se utilizara");
                exitos--; 
                continue; 
                } else {
                    System.out.println("Entrada invalida. Por favor, ingresa 's' para usar el exito o 'n' para no usarlo");
                }
            }
        } else {
            System.out.println("No se consiguieron exitos. El ataque ha fallado");
        }
        almacen.registrarAtaque(ataque, j);
        System.out.println("Numero de ataques: " + almacen.getAtaques().size());
    }

    public boolean buscarEquipo(Equipo equipo) {
        if (getAcciones() > 0){
            if (inventario.size() < MAX_INVENTARIO) {
                inventario.add(equipo);
                System.out.println(nombre + " ha encontrado " + equipo.getNombre());
                return true;
            } else {
            System.out.println("No puedes llevar mas equipo. Revisa tu inventario");
             }
        }else{
            System.out.println("No puedes buscar equipo. Revisa tus acciones o espacio en el inventario");
        }
        return false;
    }
    
    public void buscar(Inventario almacenInventario) {
        Coordenada posicionActual = this.getCoordenadas();  
        Casilla casilla = tablero.obtenerCasilla(posicionActual);
        if (casilla.fueBuscada()) {
            System.out.println("Ya se ha buscado en esta casilla. No puedes buscar nuevamente");
            return;
        }
        casilla.marcarComoBuscada();
        System.out.println("Buscando en la casilla...");
        int probabilidad = new Random().nextInt(100) + 1; 
        if (probabilidad <= 100) { // 90% de probabilidad de éxito (ajusté el porcentaje al 80%)
            Set<Equipo> equiposDisponibles = almacenInventario.getEquipos(); 
            if (!equiposDisponibles.isEmpty()) {
                Equipo equipoAleatorio = null;
                boolean equipoValido = false;
                while (!equipoValido) {
                    equipoAleatorio = (Equipo) equiposDisponibles.toArray()[new Random().nextInt(equiposDisponibles.size())];
                    if (!this.equipoEntregado(equipoAleatorio)) {
                        if (this.buscarEquipo(equipoAleatorio)) {
                            this.agregarEquipoEntregado(equipoAleatorio);
                            System.out.println("Has encontrado " + equipoAleatorio.getNombre() + " y lo has añadido a tu inventario!");
                            equipoValido = true;  
                        } else {
                            System.out.println("No puedes llevar mas equipo");
                            equipoValido = true;  
                        }
                    } else {
                        System.out.println("Ya tienes este equipo en tu inventario, buscando otro...");
                    }
                }
            } else {
                System.out.println("No hay equipo disponible en esta casilla");
            }
        } else {
            System.out.println("No has encontrado nada en esta busqueda");
        }
        this.restarAccion();
    }
    
    public void seleccionarEquipoManualmente(Inventario almacenInventario) {
        Scanner scanner = new Scanner(System.in);
        Set<Equipo> equiposDisponibles = almacenInventario.getEquipos();
        if (equiposDisponibles.isEmpty()) {
            System.out.println("No hay equipos disponibles para seleccionar");
            return;
        }
        System.out.println("Equipos disponibles para seleccionar:");
        List<Equipo> listaEquiposDisponibles = new ArrayList<>(equiposDisponibles); 
        for (int i = 0; i < listaEquiposDisponibles.size(); i++) {
            Equipo equipo = listaEquiposDisponibles.get(i);
            System.out.println((i + 1) + ". " + equipo.getNombre());
        }
        int maxEquipos = 5;
        List<Equipo> seleccionados = new ArrayList<>();
        while (seleccionados.size() < maxEquipos) {
            System.out.println("\nSelecciona un equipo ingresando su numero (o ingresa 0 para finalizar):");

            int opcion = -1;
            while (true) {
                if (scanner.hasNextInt()) {
                    opcion = scanner.nextInt();
                    scanner.nextLine();
                    break;
                } else {
                    System.out.println("Entrada no valida. Debes ingresar un numero entero");
                    scanner.nextLine(); 
                }
            }
            if (opcion == 0) {
                System.out.println("Has finalizado la seleccion de equipos");
                break;
            }
            if (opcion < 1 || opcion > listaEquiposDisponibles.size()) {
                System.out.println("Selección invalida. Intenta nuevamente");
                continue;
            }
            Equipo equipoSeleccionado = listaEquiposDisponibles.get(opcion - 1);
            if (seleccionados.contains(equipoSeleccionado)) {
                System.out.println("Ya has seleccionado este equipo. Elige otro");
            } else {
                seleccionados.add(equipoSeleccionado);
                System.out.println("Has sumado " + equipoSeleccionado.getNombre() + " a tu inventario");
            }
        }
        for (Equipo equipo : seleccionados) {
            if (this.buscarEquipo(equipo)) {
                this.agregarEquipoEntregado(equipo); 
                System.out.println("No puedes llevar mas equipo, inventario lleno");
                break;
            }
        }
    }

    public void elegirArmaActiva() {
        Scanner scanner = new Scanner(System.in);
        List<Arma> armasEnInventario = inventario.stream().filter(item ->item instanceof Arma).map(item ->(Arma)item).toList();
        if (armasEnInventario.isEmpty()) {
            System.out.println("No tienes armas en tu inventario");
            return;
        }
        System.out.println("Armas disponibles en tu inventario:");
        for (int i = 0; i < armasEnInventario.size(); i++) {
            System.out.println((i + 1) + ". " + armasEnInventario.get(i).getNombre());
        }
        System.out.println("\nTus armas activas actuales:");
        for (int i = 0; i < armasActivas.size(); i++) {
            System.out.println((i + 1) + ". " + armasActivas.get(i).getNombre());
        }
        int seleccion = -1;
        while (seleccion < 1 || seleccion > armasEnInventario.size()) {
            System.out.println("\nSelecciona un arma para activarla (ingresa el numero):");
            if (scanner.hasNextInt()) {
                seleccion = scanner.nextInt();
                if (seleccion < 1 || seleccion > armasEnInventario.size()) {
                    System.out.println("Seleccion invalida. Debes elegir un numero entre 1 y " + armasEnInventario.size() + ".");
                }
            } else {
                System.out.println("Entrada invalida. Debes ingresar un numero");
                scanner.next(); 
            }
        }
        Arma armaSeleccionada = armasEnInventario.get(seleccion - 1);

        if (armasActivas.size() >= 2) {
            System.out.println("\nYa tienes dos armas activas:");
            for (int i = 0; i < armasActivas.size(); i++) {
                System.out.println((i + 1) + ". " + armasActivas.get(i).getNombre());
            }
            int reemplazo = -1;
            while (reemplazo < 0 || reemplazo > armasActivas.size()) {
                System.out.println("Cual deseas reemplazar? (1 o 2, o 0 para cancelar):");

                if (scanner.hasNextInt()) {
                    reemplazo = scanner.nextInt();
                    if (reemplazo < 0 || reemplazo > armasActivas.size()) {
                        System.out.println("Seleccion invalida. Debes elegir 1, 2, o 0 para cancelar");
                    }
                } else {
                    System.out.println("Entrada invalida. Debes ingresar un numero");
                    scanner.next();
                }
            }
            if (reemplazo == 0) {
                System.out.println("No se realizo ningun cambio");
                return;
            }

            armasActivas.set(reemplazo - 1, armaSeleccionada);
            System.out.println("Has reemplazado tu arma activa con " + armaSeleccionada.getNombre());
        } else {
            armasActivas.add(armaSeleccionada);
            System.out.println("Has activado el arma " + armaSeleccionada.getNombre());
        }
    }
    
    public void guardarAlmacen(AlmacenAtaques almacen, String archivo, Juego j) throws IOException {
        File file = new File(archivo);
        File directorio = file.getParentFile();
        if (directorio != null && !directorio.exists()) {
            if (directorio.mkdirs()) {
                System.out.println("Directorio creado: " + directorio.getAbsolutePath());
            } else {
                System.out.println("No se pudo crear el directorio");
            }
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(almacen);
            System.out.println("Almacen guardado en: " + archivo);
        } catch (IOException e) {
            System.out.println("Error al guardar el almacen: " + e.getMessage());
            throw e;
        }
    }
 
    public void guardarHistorico() throws IOException {
        File archivo = new File("src/supervivientes/historico/" + nombre + "_historico.txt");
         archivo.getParentFile().mkdirs();
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo,true))) {
            writer.println("Historico:\n");
            for (Zombi zombi : this.historico) {
                writer.println(zombi.toString() + "\n");
            }
        }
        System.out.println("Historico guardado con exito como: " + nombre + "_historico.txt");
    }
    
    public static Superviviente cargarHistorico(String nombre) throws IOException {
        File archivo = new File("src/supervivientes/historico/" + nombre + "_historico.txt");
        if (!archivo.exists()) {
            System.out.println("Archivo HISTORICO no encontrado: " + nombre + "_historico.txt");
           return new Superviviente(nombre);
        }
         Superviviente superviviente = new Superviviente(nombre);
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.equals("Histórico:") && !linea.isBlank()) {
                    Zombi zombi = Zombi.desdeString(linea);
                    superviviente.historico.add(zombi);
                }
            }
        }
        System.out.println("Histórico cargado con exito desde: " + nombre + "_historico.txt");
        return superviviente;
    }

    public void guardarActual() throws IOException {
        File archivo = new File("src/supervivientes/actual/" + nombre + "_actual.txt");
        archivo.getParentFile().mkdirs();
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            writer.println("Actual:\n");
            for (Zombi zombi : this.actual) {
                writer.println(zombi.toString() + "\n");
            }
        }
        System.out.println("Actual guardado con exito como: " + nombre + "_actual.txt");
    }

    public static Superviviente cargarActual(String nombre) throws IOException {
        File archivo = new File("src/supervivientes/actual/" + nombre + "_actual.txt");
        if (!archivo.exists()) {
            System.out.println("Archivo ACTUAL no encontrado: " + nombre + "actual.txt");
           return new Superviviente(nombre);
        }
         Superviviente superviviente = new Superviviente(nombre);
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.equals("Actual:") && !linea.isBlank()) {
                    Zombi zombi = Zombi.desdeString(linea);
                    superviviente.actual.add(zombi);
                }
            }
        }

        System.out.println("Actual cargado con exito desde: " + nombre + "actual.txt");
        return superviviente;
    }

     public void guardarAlmacen(AlmacenAtaques almacen, String rutaPorDefecto, String rutaAlmacenAtaques) throws IOException {
    String ruta = (rutaAlmacenAtaques != null && !rutaAlmacenAtaques.isEmpty()) ? rutaAlmacenAtaques : rutaPorDefecto;

    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
        oos.writeObject(almacen); 
        System.out.println("Almacén de ataques guardado exitosamente en: " + ruta);
    } catch (IOException e) {
        System.out.println("Error al guardar el almacen de ataques: " + e.getMessage());
        throw e; 
    }
}

    @Override
    public String toString() {
        return "Superviviente: " + nombre + ", Estado: " + (vivo ? "VIVO" : "ELIMINADO") + ", Zombis Eliminados: " + zombis_eliminados + ", Heridas Recibidas: " + heridas_recibidas +  ", Coordenadas: " + coordenada;
    }

   
}
