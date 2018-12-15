import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Server {
    
    public static void main(String[] args) {
    
        try { //lecutra de el archivo Json con los Trabajadores del Hospital.

            JSONParser parser = new JSONParser();

            Object obj = parser.parse(new FileReader("personal.json")); //te crea un objeto con todo

            JSONObject jsonObject = (JSONObject) obj; //lo pasa a tipo object (tipo de Json)

            JSONArray  DocJson = (JSONArray) jsonObject.get("Doctor"); //lee el array Json

            int i = 0; 

            //Listas para almacenar todos los doctores, enfermeros y paramedicos.
            ArrayList<Doctor> Doctores = new ArrayList<Doctor>();
            ArrayList<Enfermero> Enfermeros = new ArrayList<Enfermero>();
            ArrayList<Paramedico> Paramedicos = new ArrayList<Paramedico>();

            long max_prioridad = 0; //maxima prioridad de alguno de los doctores.
            long prioridad = 0;

            for (i = 0; i < DocJson.size(); i++) { //lo recorre y crea las instancias de los Doc
                JSONObject Doc = (JSONObject) DocJson.get(i);
                long id = (Long) Doc.get("id");
                String nombre = (String) Doc.get("nombre");
                String apellido = (String) Doc.get("apellido");
                long estudios = (Long) Doc.get("estudios");
                long experiencia = (Long) Doc.get("experiencia");
                Doctor doc = new Doctor(id, nombre, apellido, estudios, experiencia);
                Doctores.add(doc);
    
                prioridad += (estudios + experiencia); //se busca la maxima prioridad de los doctores.
                if (prioridad > max_prioridad) {
                    max_prioridad = prioridad;
                }

                prioridad = 0;
            }

            JSONArray  EnfJson = (JSONArray) jsonObject.get("enfermero");//leo el array Json de enf.

            for (i = 0; i < EnfJson.size(); i++) { //instancio nuevamente
                JSONObject Enf = (JSONObject) EnfJson.get(i);
                long id = (Long) Enf.get("id");
                String nombre = (String) Enf.get("nombre");
                String apellido = (String) Enf.get("apellido");
                long estudios = (Long) Enf.get("estudios");
                long experiencia = (Long) Enf.get("experiencia");
                Enfermero doc = new Enfermero(id, nombre, apellido, estudios, experiencia);
                Enfermeros.add(doc);
                i ++;
            }

            JSONArray  ParamJson = (JSONArray) jsonObject.get("enfermero");// array Json paramed.

            i = 0;

            for (i = 0; i < ParamJson.size(); i++) { //instancio
                JSONObject Param = (JSONObject) ParamJson.get(i);
                long id = (Long) Param.get("id");
                String nombre = (String) Param.get("nombre");
                String apellido = (String) Param.get("apellido");
                long estudios = (Long) Param.get("estudios");
                long experiencia = (Long) Param.get("experiencia");
                Paramedico doc = new Paramedico(id, nombre, apellido, estudios, experiencia);
                Paramedicos.add(doc);
                i ++;
            }

            System.out.println(Doctores.size());
            System.out.println(max_prioridad);
            System.out.println(Enfermeros.size());
            System.out.println(Paramedicos.size());
        
        } catch (FileNotFoundException e) {
			System.out.println("Error: "+e.getMessage());
		} catch (IOException e) {
			System.out.println("Error: "+e.getMessage());
		} catch (ParseException e) {
			System.out.println("Error: "+e.getMessage());
        }

        //Se solicita la ip y puerto de la maquina.
        
        Scanner scan = new Scanner(System.in);

        String IP = "";
        int PUERTO = 0;
        boolean coordinador = false;
        int respuesta;
        ArrayList<String[]> maquinas = new ArrayList<String[]>();
        String[] datos;

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

        System.out.println("Ingrese la cantidad de maquinas existentes: ");
        respuesta = scan.nextInt();
        for (int i = 0; i < respuesta; i++) {
            System.out.println("Ingrese IP  y puerto de la maquina, separados por un espacio: ");
            IP = scan.nextLine();
            datos = IP.split(" ");
            maquinas.add(datos); 
        }

        System.out.println(maquinas.size());

        //Se solicita saber si esta maquina sera la que realize el algoritmo de bull.

        System .out.print("Este servidor debe partir con la comunicacion? 1: Si / 0: No: ");
        respuesta = scan.nextInt();
        if (respuesta == 1){
            coordinador = true;
            System.out.println("Este servidor iniciara las llamadas");
        }

        ServerSocket sc;
        Socket so;

        DataInputStream entrada = null;
        DataOutputStream salida = null;

        String mensajeRecibido;

        try{

            sc = new ServerSocket(PUERTO); //crea soquete servidor que escuchara en el puerto dado

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