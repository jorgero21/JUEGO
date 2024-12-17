/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package a.poo.calipsis.zombi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlmacenAtaques implements Serializable{
    private List<Ataque> ataques;
    private List<Ataque> ataques2;

    // Constructor
    public AlmacenAtaques() {
        this.ataques = new ArrayList<>();
        this.ataques2 = new ArrayList<>();
    }

    public void setAtaques2(List<Ataque> ataques2) {
        this.ataques2 = ataques2;
    }
    
    // Método para obtener todos los ataques
    public List<Ataque> getAtaques() {
        return ataques;
    }
      public List<Ataque> getAtaques2() {
        return ataques2;
    }
    
    
    // Método para registrar un ataque
    public void registrarAtaque(Ataque ataque, Juego j) {
        ataques.add(ataque);
        System.out.println("Ataque registrado: " + ataque.getResultado());
    }

   
    public void mostrarHistorial() {
    for (Ataque ataque : ataques) {
        System.out.println("Dados: " + Arrays.toString(ataque.getDados()));
        System.out.println("Resultado: " + ataque.getResultado());
    }
}
      public void mostrarHistorial2() {
    for (Ataque ataque : ataques2) {
        System.out.println("Dados: " + Arrays.toString(ataque.getDados()));
        System.out.println("Resultado: " + ataque.getResultado());
    }
}

    void registrarAtaque(Ataque ataque2, String rutaAlmacenAtaques) {
        ataques2.add(ataque2);
        System.out.println("Ataque registrado: " + ataque2.getResultado());
    }

}
