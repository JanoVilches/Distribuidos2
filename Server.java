import java.net.*;
import java.io.*;
import java.util.*;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Server {
    
    public static void main(String[] args) {
    
        //Se solicita la ip y puerto de la maquina.
        
        Scanner scan = new Scanner(System.in);

        int PUERTO = 0;

        System.out.println("Ingrese el puerto de esta maquina:");

        try{
            PUERTO = scan.nextInt();
        }catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
        }

        ServerSocket sc;
        Socket so;

        DataInputStream entrada = null;
        DataOutputStream salida = null;

        String mensajeRecibido;

        try{

            sc = new ServerSocket(PUERTO); //crea soquete servidor que escuchara en el puerto dado.

            so = new Socket();

            System.out.println("Esperando una conexion");

            so = sc.accept();

            //Se espera una conexion por parte del cliente.

            System.out.println("Un cliente se ha conectado");
            
            entrada = new DataInputStream(so.getInputStream());

            salida = new DataOutputStream(so.getOutputStream());

            salida.flush();//no se que hace.

            System.out.println("Confirmando conexion al cliente....");

            salida.writeUTF("Conexión exitosa...n envia un mensaje :D");
            salida.flush();

            mensajeRecibido = (String) entrada.readUTF();

            System.out.println(mensajeRecibido);

            salida.writeUTF("Se recibio tu mensaje.n Terminando conexion...");

            salida.writeUTF("Gracias por conectarte, adios!");

            System.out.println("Cerrando conexión...");

            sc.close();//Aqui se cierra la conexión con el cliente

        } catch(Exception e ){
            System.out.println("Error: "+e.getMessage());
        }

    }
}