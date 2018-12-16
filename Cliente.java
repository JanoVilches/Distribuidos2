import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        
        Scanner scan = new Scanner(System.in);

        String IP = "";
        int PUERTO = 0;

        System.out.println("Ingrese la IP de la maquina:");

        try{
            IP = scan.nextLine();
        }catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
        }

        System.out.println("Ingrese el puerto de esta maquina:");

        try{
            PUERTO = scan.nextInt();
        }catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
        }

        Socket sc;

        DataInputStream entrada = null;
        DataOutputStream salida = null;

        String mensajeRecibido;

        try {

            sc = new Socket(IP, PUERTO); //conectar al localhost

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