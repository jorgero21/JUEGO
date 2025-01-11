package juego;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

public class Ataque implements Serializable{
    private int[] dados;      
    private String resultado; 
    private int zombisEliminados;  
    
    public Ataque(int numDados) {
        this.dados = new int[numDados];
        this.resultado = "";
        this.zombisEliminados = 0;  
    }

    public int[] getDados() {
        return dados;
    }

  

    public int lanzarDados(int valorExito) {
        Random random = new Random();
        int exitos = 0;
        System.out.println("Lanzando " + dados.length + " dados...");

        for (int i = 0; i < dados.length; i++) {
            dados[i] = random.nextInt(6) + 1; 
            System.out.println("Resultado del dado " + (i + 1) + ": " + dados[i]);

            if (dados[i] >= valorExito) {
                exitos++;
            }
        }
        resultado = "Se obtuvo " + exitos + " exitos";
        System.out.println(resultado);
        return exitos;
    }
    public void registrarZombiEliminado() {
        zombisEliminados++;
    }
    public int getZombisEliminados() {
        return zombisEliminados;
    }
    public String getResultado() {
        return resultado + " | Zombis eliminados: " + zombisEliminados;
    }
    
    @Override 
    public String toString() { 
        return "Ataque:\n" +"Dados lanzados: " + Arrays.toString(dados) + "\n" +"Resultado: " + resultado +"\n"+"Zombis eliminados: " + zombisEliminados;
}
    }
