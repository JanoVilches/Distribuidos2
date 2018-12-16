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
        
        //Listas para almacenar todos los doctores, enfermeros y paramedicos.
        ArrayList<Doctor> Doctores = new ArrayList<Doctor>();
        ArrayList<Enfermero> Enfermeros = new ArrayList<Enfermero>();
        ArrayList<Paramedico> Paramedicos = new ArrayList<Paramedico>();
        ArrayList<Requerimiento> Requerimientos = new ArrayList<Requerimiento>();
        ArrayList<Paciente> Pacientes = new ArrayList<Paciente>();

        long max_prioridad = 0; //maxima prioridad de alguno de los doctores.
    
        try { //lecutra de el archivo Json con los Trabajadores del Hospital.

            JSONParser parser = new JSONParser();

            Object obj = parser.parse(new FileReader("personal.json")); //te crea un objeto con todo

            JSONObject jsonObject = (JSONObject) obj; //lo pasa a tipo object (tipo de Json)

            JSONArray  DocJson = (JSONArray) jsonObject.get("Doctor"); //lee el array Json 

            long prioridad = 0;

            for (int i = 0; i < DocJson.size(); i++) { //lo recorre y crea las instancias de los Doc
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

            for (int i = 0; i < EnfJson.size(); i++) { //instancio nuevamente
                JSONObject Enf = (JSONObject) EnfJson.get(i);
                long id = (Long) Enf.get("id");
                String nombre = (String) Enf.get("nombre");
                String apellido = (String) Enf.get("apellido");
                long estudios = (Long) Enf.get("estudios");
                long experiencia = (Long) Enf.get("experiencia");
                Enfermero doc = new Enfermero(id, nombre, apellido, estudios, experiencia);
                Enfermeros.add(doc);
            }

            JSONArray  ParamJson = (JSONArray) jsonObject.get("Paramedico");// array Json paramed.

            for (int i = 0; i < ParamJson.size(); i++) { //instancio
                JSONObject Param = (JSONObject) ParamJson.get(i);
                long id = (Long) Param.get("id");
                String nombre = (String) Param.get("nombre");
                String apellido = (String) Param.get("apellido");
                long estudios = (Long) Param.get("estudios");
                long experiencia = (Long) Param.get("experiencia");
                Paramedico doc = new Paramedico(id, nombre, apellido, estudios, experiencia);
                Paramedicos.add(doc);
            }
        
        } catch (FileNotFoundException e) {
			System.out.println("Error: "+e.getMessage());
		} catch (IOException e) {
			System.out.println("Error: "+e.getMessage());
		} catch (ParseException e) {
			System.out.println("Error: "+e.getMessage());
        }

        //Lectura del Json con los requerimientos
        try {

            JSONParser parser = new JSONParser();

            Object obj = parser.parse(new FileReader("requerimientos.json")); //te crea un objeto con todo

            JSONObject jsonObject = (JSONObject) obj; //lo pasa a tipo object (tipo de Json)

            JSONArray  ReqJson = (JSONArray) jsonObject.get("requerimientos"); //lee el array Json

            for (int i = 0; i < ReqJson.size(); i++) {
                JSONObject req = (JSONObject) ReqJson.get(i);
                long id = (Long) req.get("id");
                String cargo = (String) req.get("cargo");
                JSONArray tarea = (JSONArray) req.get("pacientes");
                ArrayList<ArrayList<String>> pacientes = new ArrayList<ArrayList<String>>();
                
                for (int j = 0; j < tarea.size(); j++) {
                    JSONObject aux = (JSONObject) tarea.get(j);
                    Set keys = aux.keySet();
                    Object [] llave = keys.toArray();
                    String str = (String) aux.get(llave[0]);
                    ArrayList<String> aux2 = new ArrayList<String>();
                    aux2.add((String) llave[0]);
                    aux2.add(str);
                    pacientes.add(aux2);
                }
                
                Requerimiento doc = new Requerimiento(id,cargo,pacientes);
                Requerimientos.add(doc);
            }


        } catch (FileNotFoundException e) {
			System.out.println("Error: "+e.getMessage());
		} catch (IOException e) {
			System.out.println("Error: "+e.getMessage());
		} catch (ParseException e) {
			System.out.println("Error: "+e.getMessage());
        }

        try { //lecutra de el archivo Json con los pacientes

            JSONParser parser = new JSONParser();

            Object obj = parser.parse(new FileReader("pacientes.json")); //te crea un objeto con todo

            JSONArray pacientes = (JSONArray) obj;

            for (int i = 0; i < pacientes.size(); i++) {
                JSONObject paciente = (JSONObject) pacientes.get(i);
                
                Long id = (Long) paciente.get("paciente_id");
                
                ArrayList datos = (ArrayList) paciente.get("datos personales");
                JSONObject dataPerso = (JSONObject) datos.get(0);
                String nombre = (String) dataPerso.get("nombre");
                String rut = (String) dataPerso.get("rut");
                int edad = Integer.parseInt((String) dataPerso.get("edad"));
                
                ArrayList<String> enfermedades = new ArrayList<String>();
                JSONArray enf = (JSONArray) paciente.get("enfermedades");
                for (int j = 0; j < enf.size(); j++) {
                    enfermedades.add((String) enf.get(j));
                }

                ArrayList<String> procAsignados = new ArrayList<String>();
                ArrayList<String> procCompletados = new ArrayList<String>();
                JSONArray proc = (JSONArray) paciente.get("tratamientos/procedimientos");
                JSONObject asig = (JSONObject) proc.get(0);
                JSONArray asigArr = (JSONArray) asig.get("asignados");
                JSONObject comp = (JSONObject) proc.get(1);
                JSONArray compArr = (JSONArray) comp.get("completados");

                for (int j = 0; j < asigArr.size() ; j++) {
                    procAsignados.add((String) asigArr.get(j));
                }
                
                for (int j = 0; j < compArr.size() ; j++) {
                    procCompletados.add((String) compArr.get(j));
                }

                ArrayList<String> examRealizados = new ArrayList<String>();
                ArrayList<String> examNoRealizados = new ArrayList<String>();
                JSONArray exam = (JSONArray) paciente.get("examenes");
                JSONObject real = (JSONObject) exam.get(0);
                JSONArray realArr = (JSONArray) real.get("realizados");
                JSONObject noreal = (JSONObject) exam.get(1);
                JSONArray norealArr = (JSONArray) noreal.get("no realizados");

                for (int j = 0; j < realArr.size() ; j++) {
                    examRealizados.add((String)realArr.get(j));
                }
                
                for (int j = 0; j < norealArr.size() ; j++) {
                    examNoRealizados.add((String)norealArr.get(j));
                }

                ArrayList<String> medRecetados = new ArrayList<String>();
                ArrayList<String> medSuminst = new ArrayList<String>();
                JSONArray med = (JSONArray) paciente.get("medicamentos");
                JSONObject recet = (JSONObject) med.get(0);
                JSONArray recetArr = (JSONArray) recet.get("recetados");
                JSONObject sumin = (JSONObject) med.get(1);
                JSONArray suminArr = (JSONArray) sumin.get("suministrados");

                for (int j = 0; j < recetArr.size() ; j++) {
                    medRecetados.add((String)recetArr.get(j));
                }
                
                for (int j = 0; j < suminArr.size() ; j++) {
                    medSuminst.add((String)suminArr.get(j));
                }

                Paciente doc = new Paciente(id,nombre,rut,edad,enfermedades,procAsignados,procCompletados,examRealizados,examNoRealizados,medRecetados,medSuminst);
                Pacientes.add(doc);
            }
        
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
        String auxIP = "";
        int PUERTO = 0;
        boolean coordinador = false;
        int respuesta;
        ArrayList<String[]> maquinas = new ArrayList<String[]>();
        String[] datos = null;

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
        auxIP = scan.nextLine();

        for (int i = 0; i < respuesta; i++) {
            System.out.println("Ingrese IP  y puerto de la maquina, separados por un espacio:");
            auxIP = scan.nextLine();
            datos = auxIP.split(" ");
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

         //se crea el Thread servidor, que realiza el algortimo. Definir los parametros cuando se tenga listo.
        ServerThread server = new ServerThread(Doctores, Enfermeros, Paramedicos, max_prioridad, maquinas, PUERTO, coordinador, IP);
        server.start();
        try{
            Thread.sleep(2000);
        } catch (Exception e){}
        System.out.println("Presione Enter para iniciar el servidor, cuando esten todos otros servidores inicializados");
        String iniciar = scan.nextLine();
        System.out.println("Dando tiempo para inicializar el resto de servidores");
        try{
            Thread.sleep(10000);
        } catch (Exception e){}

        //Se hace flooding de mi prioridad al resto de los servidores.

        for (int i = 0; i < maquinas.size(); i++) {//flooding de las pripridades.
            try{
                //se necesita, ip y puerto de origen, ip y puerto de destino, nombre, y mensaje: en este caso es el la ip del server + su prioridad.
                ClienteThread c = new ClienteThread(IP, PUERTO, maquinas.get(i)[0], Integer.parseInt(maquinas.get(i)[1]), "cliente1", IP + "," + Long.toString(max_prioridad));
                c.start();
            }
            catch(NumberFormatException a){
                System.out.println("Error al enviar un cliente al port "+ maquinas.get(i)[1]);
                System.out.println(a);
            }
        }

    }
}