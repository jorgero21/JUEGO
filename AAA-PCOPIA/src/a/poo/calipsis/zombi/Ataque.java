/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package a.poo.calipsis.zombi;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

public class Ataque implements Serializable{
    private int[] dados;      // Resultados de los lanzamientos de dados
    private String resultado; // Resumen del ataque

    public Ataque(int numDados) {
        this.dados = new int[numDados];
        this.resultado = "";
    }

    public int[] getDados() {
        return dados;
    }

    public String getResultado() {
        return resultado;
    }

    public int lanzarDados(int valorExito) {
        Random random = new Random();
        int exitos = 0;

        System.out.println("Lanzando " + dados.length + " dados...");

        for (int i = 0; i < dados.length; i++) {
            dados[i] = random.nextInt(6) + 1; // Resultado de un dado (1-6)
            System.out.println("Resultado del dado " + (i + 1) + ": " + dados[i]);

            if (dados[i] >= valorExito) {
                exitos++;
            }
        }

        resultado = "Se obtuvo " + exitos + " exitos";
        System.out.println(resultado);
        return exitos;
    }
    
    @Override 
    public String toString() { 
        return "Ataque:\n" +
           "Dados lanzados: " + Arrays.toString(dados) + "\n" +
           "Resultado: Se obtuvieron " + resultado + " éxitos\n";
}
    }
