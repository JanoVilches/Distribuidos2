import java.net.*;
import java.io.*;

public class Cliente {
    public static void main(String[] args) {
        
        String HOST = "localhost";
        int PUERTO = 50051;

        Socket sc;

        DataInputStream entrada = null;
        DataOutputStream salida = null;

        String mensajeRecibido;

        try {

            sc = new Socket(HOST, PUERTO); //conectar al localhost

            System.out.println("Conectado...");

            entrada = new DataInputStream(sc.getInputStream());

            salida = new DataOutputStream(sc.getOutputStream());
            salida.flush();

            mensajeRecibido = (String) entrada.readUTF();
            System.out.println(mensajeRecibido);

            //enviamos el mensaje
            salida.writeUTF("hola que tal !!");
            salida.flush();

            mensajeRecibido = (String) entrada.readUTF();
            System.out.println(mensajeRecibido);

            mensajeRecibido = (String) entrada.readUTF();
            System.out.println(mensajeRecibido);

            //cerramos conexion
            sc.close();

            
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
        }
    }
}