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

            System.out.println(max_prioridad);
        
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

        System.out.println("Ingrese la cantidad de maquinas existentes:");
        respuesta = scan.nextInt();
        IP = scan.nextLine();

        for (int i = 0; i < respuesta; i++) {
            System.out.println("Ingrese IP  y puerto de la maquina, separados por un espacio:");
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

        //Creacion de los Thread Servidor y Cliente que realizaran todo el codigo.

        //falta crear todo el codigo del ServerThread.
        /*ServerThread server = new ServerThread(); //se crea el Thread servidor, que realiza el algortimo. Definir los parametros cuando se tenga listo.
        server.star();
        Thread.sleep(2000);
        System.out.println("Presione Enter para iniciar el servidor, cuando esten todos otros servidores inicializados");
        String iniciar = scan.nextLine();
        System.out.println("Dando tiempo para inicializar el resto de servidores");
        Thread.sleep(10000);*/

        //Se hace flooding de mi prioridad al resto de los servidores.

        for (int i = 0; i < maquinas.size(); i++) {//flooding de las pripridades.
            try{
                //se necesita, ip y puerto de origen, ip y puerto de destino, nombre, y mensaje: en este caso es el la ip del server + su prioridad.
                ClienteThread c = new ClienteThread(IP, PUERTO, maquinas.get(i)[0], Integer.parseInt(maquinas.get(i)[1]), "cliente1", IP + "," + Integer.toString(max_prioridad));
                c.start();
            }
            catch(NumberFormatException a){
                System.out.println("Error al enviar un cliente al port "+ maquinas.get(i)[1]);
                System.out.println(a);
            }
        }

    }
}