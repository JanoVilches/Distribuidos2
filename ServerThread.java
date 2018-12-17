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
    private ArrayList<Requerimiento> requerimientos;
    private ArrayList<Paciente> pacientes;
    //String[] seran tipo IP, Puerto, prioridad
    private ArrayList<String[]> maquinasMayores, maquinas;
    private Long prioridad;
    private int puerto;
    private boolean isCoordinador;
    private String ip;
    private String[] coordinador;

    public ServerThread(ArrayList<Doctor> doc, ArrayList<Enfermero> enf, ArrayList<Paramedico> par, ArrayList<Requerimiento> req, ArrayList<Paciente> pac,  Long maxPrioridad, ArrayList<String[]> maq, int port, boolean cord, String ipO){
        this.doctores = doc;
        this.enfermero = enf;
        this.paramedico = par;
        this.requerimientos = req;
        this.pacientes = pac;
        this.prioridad = maxPrioridad;
        this.puerto = port;
        this.maquinasMayores = new ArrayList<String[]>();
        this.isCoordinador = cord;
        this.ip = ipO;
        this.maquinas = maq;
    }

    public void run(){
        ServerSocket serv = null;
        Socket sock = null;
        DataInputStream input = null;
        String mensaje = "";
        String [] messageElements = null;
        boolean vaciando = false;
        boolean elecciones= false;

        try{
            serv = new ServerSocket(puerto);
            serv.setSoTimeout(15000);
            //guardar la prioridad de las maquinas superiores
            //por cada máquina
            for(int i = 0; i < maquinas.size(); ++i){
                //aceptar la conexión
                sock = serv.accept();
                //leer el mensaje
                input = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
                mensaje = input.readUTF();
                messageElements = mensaje.split(",");
                //si el recibido es mayor
                if(prioridad < Long.parseLong(messageElements[2])){
                    //se busca y se agrega su prioridad
                    for(int j = 0; j < maquinas.size(); ++j){
                        if(messageElements[0].equals(maquinas.get(j)[0]) && messageElements[1].equals(maquinas.get(j)[1])){
                            String[] aux = new String[3];
                            //ip
                            aux[0] = maquinas.get(j)[0];
                            //puerto
                            aux[1] = maquinas.get(j)[1];
                            aux[2] = messageElements[2];
                            maquinasMayores.add(aux);
                        }
                    }
                }
                System.out.println("maquinas");
                System.out.println(Integer.toString(maquinasMayores.size()));
                sock.close();
                input.close();
            }
        } catch (Exception e) {}


        //mensaje de inicio de elección "isAlive:IP:Puerto"
        //mensaje de respuesta "yes"
        //mensaje de elección "electo:IP:puerto"

        //si esta maquina es la que inicia la llamada
        if(isCoordinador){
            for(int i = 0; i < maquinasMayores.size(); ++i){
                //enviar mensajes a las otras máquinas para saber si viven
                //en un futuro borrar puerto
                ClienteThread client = new ClienteThread(ip, puerto, maquinasMayores.get(i)[0], Integer.parseInt(maquinasMayores.get(i)[1]), "", "isAlive:" + ip + ":" + Integer.toString(puerto));
                client.start();
            }
            //por cada maquina a la que se le envio un mensaje
            for(int i = 0; i < maquinasMayores.size(); ++i){
                //verifica la respuesta
                try{
                    sock = serv.accept();
                    //leer el mensaje
                    input = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
                    mensaje = input.readUTF();
                    //si recibe una respuesta de que una maquina superior esta viva, entonces no es el coordinador
                    if(mensaje.equals("yes")){
                        isCoordinador = false;
                    }
                    sock.close();
                    input.close();
                } catch (Exception e) {}
            }
            //si no hay maquina mayor, entonces es la coordinadora y se lo envia a todos
            if(maquinasMayores.size() == 0){
                for(int i = 0; i < maquinas.size(); ++i){
                    ClienteThread clientEleccion = new ClienteThread(ip, puerto, maquinas.get(i)[0], Integer.parseInt(maquinas.get(i)[1]), "", "electo:" + ip + ":" + Integer.toString(puerto));
                    clientEleccion.start();
                }
                ClienteThread clientEleccion = new ClienteThread(ip, puerto, ip, puerto, "", "electo:" + ip + ":" + Integer.toString(puerto));
                clientEleccion.start();
                try {
                    sock = serv.accept();
                    //leer el mensaje
                    input = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
                    mensaje = input.readUTF();
                    messageElements = mensaje.split(":");
                } catch (Exception e) {
                    //TODO: handle exception
                }
            }
        }
        //si no fue elegida para inciar
        else{
            try{
                //espera un mensaje, puede ser si esta vivo o un mensaje anunciando al coordinador
                isCoordinador = true;
                sock = serv.accept();
                //leer el mensaje
                input = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
                mensaje = input.readUTF();
                messageElements = mensaje.split(":");
                //mientras reciba un isAlive o un si estoy vivo
                if (messageElements[0].equals("electo")) {
                    isCoordinador = false;
                }
                while(messageElements[0].equals("isAlive") || messageElements[0].equals("yes")){
                    //si recibe un isAlive
                    if(messageElements[0].equals("isAlive")){
                        //responde que si
                        ClienteThread clientYes = new ClienteThread(ip, puerto, messageElements[1], Integer.parseInt(messageElements[2]), "", "yes");
                        clientYes.start();
                        //le envia un isAlive a todas las maquinas mayores
                        for(int i = 0; i < maquinasMayores.size(); ++i){
                            ClienteThread clientAlive = new ClienteThread(ip, puerto, maquinasMayores.get(i)[0], Integer.parseInt(maquinasMayores.get(i)[1]), "", "isAlive:" + ip + ":" + Integer.toString(puerto));
                            clientAlive.start();
                        }
                        //si era la mas grande, entonces es la coordinadora
                        if(maquinasMayores.size() == 0){
                            //avisa que es la coordinadora
                            for(int i = 0; i < maquinas.size(); ++i){
                                ClienteThread clientEleccion = new ClienteThread(ip, puerto, maquinas.get(i)[0], Integer.parseInt(maquinas.get(i)[1]), "", "electo:" + ip + ":" + Integer.toString(puerto));
                                clientEleccion.start();
                            }
                            ClienteThread clientEleccion = new ClienteThread(ip, puerto, ip, puerto, "", "electo:" + ip + ":" + Integer.toString(puerto));
                            clientEleccion.start();
                            try {
                                sock = serv.accept();
                                //leer el mensaje
                                input = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
                                mensaje = input.readUTF();
                                messageElements = mensaje.split(":");
                            } catch (Exception e) {
                                //TODO: handle exception
                            }
                        }
                    }
                    //si recibe un si, entonces no es la coordinadora
                    if(messageElements[0].equals("yes")){
                        isCoordinador = false;
                    }
                    sock.close();
                    input.close();
                    sock = serv.accept();
                    input = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
                    mensaje = input.readUTF();
                    messageElements = mensaje.split(":");
                }
            } catch (Exception e) {}
        }
//
//INICIO POST ELECCIONES

        while(true){
            try{
                messageElements = mensaje.split(":");
                //si reciben un mensaje de anuncio de coordinador
                if(messageElements[0].equals("electo")){
                    System.out.println("coordinador");
                    System.out.println(Arrays.toString(messageElements));
                    if(isCoordinador){
                        System.out.println("soy el coordinador con prioridad " + Long.toString(prioridad));
                    }
                    elecciones = false;
                    //almacenan el coordinador
                    coordinador = new String[2];
                    coordinador[0] = messageElements[1];
                    coordinador[1] = messageElements[2];
                    sock.close();
                    input.close();
                    //vaciar cola de mensajes (sacar los yes e isAlive que queden de la eleccion)
                    vaciando = true;
                    while(vaciando){
                        sock = serv.accept();
                        input = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
                        mensaje = input.readUTF();
                        messageElements = mensaje.split(":");
                        //si habia un isAlive lo responde para que la otra maquina no crea que esta muerto
                        if(messageElements[0].equals("isAlive")){
                            ClienteThread clientYes = new ClienteThread(ip, puerto, messageElements[1], Integer.parseInt(messageElements[2]), "", "yes");
                            clientYes.start();
                        }

                        sock.close();
                        input.close();
                    }
                }
                //si llega un mensaje de tarea lista
                else if(false/*tarea lista*/){
                    //enviar siguiente tarea
                }

                //si llega un mensaje de hacer tarea
                else if(false/*hacer tarea*/){
                    //agregar a la cola de tareas
                    //hacer siguiente tarea
                }
                
                //si se recibe un isAlive
                else if(messageElements[0].equals("isAlive")){
                    System.out.println("Iniciando Elecciones");
                    //se inicia el proceso de elecciones desde esta maquina
                    elecciones = true;
                    //dice que si esta viva
                    ClienteThread clientYes = new ClienteThread(ip, puerto, messageElements[1], Integer.parseInt(messageElements[2]), "", "yes");
                    clientYes.start();
                    //envia mensaje de isAlive a los mayores
                    for(int i = 0; i < maquinasMayores.size(); ++i){
                        ClienteThread clientAlive = new ClienteThread(ip, puerto, maquinasMayores.get(i)[0], Integer.parseInt(maquinasMayores.get(i)[1]), "", "isAlive:" + ip + ":" + Integer.toString(puerto));
                        clientAlive.start();
                    }
                    isCoordinador = true;
                    //comienza a revisar todos los mensajes que lleguen producto de la elección
                    vaciando = true;
                    while(vaciando){
                        sock = serv.accept();
                        input = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
                        mensaje = input.readUTF();
                        messageElements = mensaje.split(":");
                        //si una mayor esta viva, entonces no coordina
                        if(messageElements[0].equals("yes")){
                            isCoordinador = false;
                        }
                        //si recibe un isAlive, entonces responde y envia a los mayores
                        else if(messageElements[0].equals("isAlive")){
                            //dice que si esta viva
                            clientYes = new ClienteThread(ip, puerto, messageElements[1], Integer.parseInt(messageElements[2]), "", "yes");
                            clientYes.start();
                            for(int i = 0; i < maquinasMayores.size(); ++i){
                                ClienteThread clientAlive = new ClienteThread(ip, puerto, maquinasMayores.get(i)[0], Integer.parseInt(maquinasMayores.get(i)[1]), "", "isAlive:" + ip + ":" + Integer.toString(puerto));
                                clientAlive.start();
                            }
                        }
                        //si recibe un anuncio de coordinador, lo actualiza y termina de vaciar
                        else if(messageElements[0].equals("electo")){
                            isCoordinador = false;
                            coordinador[0] = messageElements[1];
                            coordinador[1] = messageElements[2];
                        }
                        sock.close();
                        input.close();
                    }
                }
                //si se recibe un 404 cuando se hace un ping a la maquina coordinadora
                else if(messageElements[0].equals("404") && !isCoordinador){
                    System.out.println("Iniciando Elecciones");
                    sock.close();
                    input.close();
                    //iniciar elecciones
                    elecciones = true;
                    //le pregunta a todas las maquinas mayores si estan vivas
                    for(int i = 0; i < maquinasMayores.size(); ++i){
                        ClienteThread clientAlive = new ClienteThread(ip, puerto, maquinasMayores.get(i)[0], Integer.parseInt(maquinasMayores.get(i)[1]), "", "isAlive:" + ip + ":" + Integer.toString(puerto));
                        clientAlive.start();
                    }
                    isCoordinador = true;
                    //comienza a revisar todos los mensajes que lleguen producto de la elección
                    vaciando = true;
                    while(vaciando){
                        sock = serv.accept();
                        input = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
                        mensaje = input.readUTF();
                        messageElements = mensaje.split(":");
                        //si una mayor esta viva, entonces no coordina
                        if(messageElements[0].equals("yes")){
                            isCoordinador = false;
                        }
                        //si recibe un isAlive, entonces responde y envia a los mayores
                        else if(messageElements[0].equals("isAlive")){
                            //dice que si esta viva
                            ClienteThread clientYes = new ClienteThread(ip, puerto, messageElements[1], Integer.parseInt(messageElements[2]), "", "yes");
                            clientYes.start();
                            for(int i = 0; i < maquinasMayores.size(); ++i){
                                ClienteThread clientAlive = new ClienteThread(ip, puerto, maquinasMayores.get(i)[0], Integer.parseInt(maquinasMayores.get(i)[1]), "", "isAlive:" + ip + ":" + Integer.toString(puerto));
                                clientAlive.start();
                            }
                        }
                        //si recibe un anuncio de coordinador, lo actualiza y termina de vaciar
                        else if(messageElements[0].equals("electo")){
                            isCoordinador = false;
                            coordinador[0] = messageElements[1];
                            coordinador[1] = messageElements[2];
                        }
                        sock.close();
                        input.close();
                    }
                }

                else if(messageElements[0].equals("")){
                    //nada unu
                }

                sock.close();
                input.close();
                //siguiente mensaje
                sock = serv.accept();
                input = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
                mensaje = input.readUTF();

            }
            //si pasaron mas de 15 segundos sin recibir un mensaje
            catch (SocketTimeoutException s){
                //si estabamos vaciando la cola de mensajes
                if(vaciando){
                    //dejamos de vaciar
                    System.out.println("vaciado");
                    vaciando = false;
                    mensaje = "";

                    if(elecciones && isCoordinador){
                        for(int i = 0; i < maquinas.size(); ++i){
                            ClienteThread clientEleccion = new ClienteThread(ip, puerto, maquinas.get(i)[0], Integer.parseInt(maquinas.get(i)[1]), "", "electo:" + ip + ":" + Integer.toString(puerto));
                            clientEleccion.start();
                        }
                        ClienteThread clientEleccion = new ClienteThread(ip, puerto, ip, puerto, "", "electo:" + ip + ":" + Integer.toString(puerto));
                        clientEleccion.start();
                    }
                    //enviar siguiente tarea
                }
                //si no estabamos en elecciones
                else if(!elecciones){
                    //se hace ping para saber si la máquina coordinadora esta viva
                    ClienteThread clientVef = new ClienteThread(ip, puerto, coordinador[0], Integer.parseInt(coordinador[1]), "", "");
                    clientVef.start();
                    mensaje = "";
                }
            } catch (Exception e) {}
        }
    }

    public void start(){
        if(server == null){
            server = new Thread(this, "test");
            server.start();
        }
    }
}