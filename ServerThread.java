import java.util.Timer;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

//ordenarlo entero, solo tiene las funciones que probe anteriormente para la conexion.

public class ServerThread implements Runnable{
    private Thread server;
    private ArrayList<Doctor> doctores;
    private ArrayList<Enfermero> enfermero;
    private ArrayList<Paramedico> paramedico;
    //String[] seran tipo IP, prioridad
    private ArrayList<String[]> maquinas;
    private Long prioridad;
    private int puerto;
    private boolean coordinador;

    public ServerThread(ArrayList<Doctor> doc, ArrayList<Enfermero> enf, ArrayList<Paramedico> par, Long maxPrioridad, ArrayList<String[]> maq, int port, boolean cord){
        this.doctores = doc;
        this.enfermero = enf;
        this.paramedico = par;
        this.prioridad = maxPrioridad;
        this.puerto = port;
        this.maquinas = new ArrayList<String[]>();
        this.coordinador = cord;
        for(int i = 0; i < maq.size(); ++i){
            //cambiar a 2 mas adelante
            String[] aux = new String[3];
            //IP
            aux[0] = maq.get(i)[0];
            //puerto, borrar más tarde
            aux[1] = maq.get(i)[1];
            maquinas.add(aux);
        }
    }

    public void run(){
        ServerSocket serv;
        Socket sock;
        DataInputStream input;

        try{
            serv = new ServerSocket(puerto);
            //guardar la prioridad de las maquinas superiores
            //por cada máquina
            for(int i = 0; i < maquinas.size(); ++i){
                String line;
                String[] aux;
                //aceptar la conexión
                sock = serv.accept();
                //leer el mensaje
                input = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
                line = input.readUTF();
                aux = line.split(",");
                //si el recibido es mayor
                if(prioridad < Long.parseLong(aux[1])){
                    System.out.println(prioridad);
                    //se busca y se agrega su prioridad
                    for(int j = 0; j < maquinas.size(); ++j){
                        if(aux[0].equals(maquinas.get(i)[0])){
                            //cambiar a 1 mas adelante
                            maquinas.get(i)[2] = aux[1];
                        }
                    }
                }
                //si no es mayor
                else{
                    //se busca y se elimina
                    for(int j = 0; j < maquinas.size(); ++j){
                        if(Objects.equals(aux[0], maquinas.get(i)[0])){
                            j+= maquinas.size();
                            maquinas.remove(i);

                        }
                    }
                }
                sock.close();
                input.close();
            }
        } catch (Exception e) {}
        
        for(int i = 0; i < maquinas.size(); ++i){
            System.out.println(Arrays.toString(maquinas.get(i)));
        }
        System.out.println(maquinas.size());

        //si esta maquina es la que inicia la llamada
        if(coordinador){
            coordinador = false;
            for(int i = 0; i < maquinas.size(); ++i){
                //enviar mensajes a las otras máquinas
            }
        }
    }

    public void start(){
        if(server == null){
            server = new Thread(this, "test");
            server.start();
        }
    }

    /*
    ServerSocket sc;
    Socket so;

    DataInputStream entrada = null;
    DataOutputStream salida = null;

    String mensajeRecibido;

    try{

        sc = new ServerSocket(puerto); //crea soquete servidor que escuchara en el puerto dado

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
    */
}