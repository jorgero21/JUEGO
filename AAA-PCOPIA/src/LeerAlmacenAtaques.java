import a.poo.calipsis.zombi.AlmacenAtaques;
import a.poo.calipsis.zombi.Ataque;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class LeerAlmacenAtaques {

    public static void main(String[] args) {
        try {
            // Ruta del archivo a leer
            String rutaArchivo = "guardados/pñ_ataques.dat";
            System.out.println("Intentando cargar archivo: " + rutaArchivo);

            AlmacenAtaques almacenAtaques;
            try ( // Crear flujo de entrada para el archivo
                    FileInputStream fileInputStream = new FileInputStream(rutaArchivo)) {
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                // Leer el objeto deserializado
                almacenAtaques = (AlmacenAtaques) objectInputStream.readObject();
                System.out.println("Archivo cargado con éxito.");
                // Cerrar los flujos
                objectInputStream.close();
            }

            // Imprimir el contenido de manera legible
            List<Ataque> ataques = almacenAtaques.getAtaques();
            System.out.println("Número de ataques cargados: " + ataques.size());
            for (Ataque ataque : ataques) {
                System.out.println(ataque);
            }

        } catch (IOException | ClassNotFoundException e) {
        }
    }
}
