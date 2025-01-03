package a.poo.calipsis.zombi;

import java.io.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class Inventario implements Serializable{
    private Set<Arma> armas;
    private Set<Provisiones> provisiones;
    private Set<Equipo> equipo;

    public Inventario() {
        this.armas = new HashSet<>();
        this.provisiones = new HashSet<>();
        this.equipo= new HashSet<>();
    }

    public Set<Equipo> getEquipos() {
           Set<Equipo> todosLosEquipos = new HashSet<>();
           todosLosEquipos.addAll(armas);      
           todosLosEquipos.addAll(provisiones); 
           return todosLosEquipos;
    }
  
    public Set<Provisiones> getProvisiones() {  return provisiones;  }
    
    public Set<Arma> getArmas() { return armas; }
   
    public void listarArmas() {
        if (armas.isEmpty()) {
            System.out.println("No hay armas en el inventario");
            return;
        }
        System.out.println("Armas disponibles:");
        for (Arma arma : armas) {
            System.out.println(arma);
        }
    }

    public void listarProvisiones() {
        if (provisiones.isEmpty()) {
            System.out.println("No hay provisiones en el inventario");
            return;
        }
        System.out.println("Provisiones disponibles:");
        for (Provisiones provision : provisiones) {
            System.out.println(provision);
        }
    }

    public void agregarArma(Arma nuevaArma) {
        armas.add(nuevaArma);
        System.out.println(nuevaArma.getNombre() + " se ha agregado al inventario");
    }

    public void agregarProvision(Provisiones nuevaProvision) {
        provisiones.add(nuevaProvision);
        System.out.println(nuevaProvision.getNombre() + " se ha agregado al inventario");
    }
     
    public void cargarDesdeFichero(String nombreFichero) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(nombreFichero))) {
            String linea;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                String tipo = partes[0].trim(); 

                if (tipo.equalsIgnoreCase("Arma")) {
                    String nombre = partes[1].trim();
                    int daño = Integer.parseInt(partes[2].trim());
                    int alcance = Integer.parseInt(partes[3].trim());
                    int numeroDados = Integer.parseInt(partes[4].trim());
                    int valorExito = Integer.parseInt(partes[5].trim());
                    Arma arma = new Arma(nombre, daño, alcance, numeroDados, valorExito);
                    armas.add(arma);
                    System.out.println("Arma cargada: " + nombre);
                } else if (tipo.equalsIgnoreCase("Provision")) {
                    String nombre = partes[1].trim();
                    int cantidad = Integer.parseInt(partes[2].trim());
                    String fechaStr = partes[3].trim(); 
                    LocalDate fecha = LocalDate.parse(fechaStr, formatter); 
                    Provisiones provision = new Provisiones(nombre, cantidad, fecha);
                    provisiones.add(provision);
                    System.out.println("Provision cargada: " + nombre);
                }
            }
            System.out.println("Inventario cargado desde el fichero: " + nombreFichero);
        } catch (IOException e) {
            System.err.println("Error al cargar el inventario: " + e.getMessage());
        }
    }

    public void guardarEnFichero(String nombreFichero) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreFichero))) {
            for (Arma arma : armas) {
                String linea = String.join(",", 
                    "Arma", 
                    arma.getNombre(), 
                    String.valueOf(arma.getPotencia()), 
                    String.valueOf(arma.getAlcance()),
                    String.valueOf(arma.getValorExito()), 
                    String.valueOf(arma.getNumeroDados()));
                bw.write(linea);
                bw.newLine();
            }
            for (Provisiones provision : provisiones) {
                String fechaCaducidad = provision.getCaducidad().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String linea = String.join(",", 
                    "Provisión", 
                    provision.getNombre(), 
                    String.valueOf(provision.getValorEnergetico()), 
                    fechaCaducidad);
                bw.write(linea);
                bw.newLine();
            }

            System.out.println("Inventario guardado en el fichero: " + nombreFichero);
        } catch (IOException e) {
            System.err.println("Error al guardar el inventario: " + e.getMessage());
        }
    }
}
