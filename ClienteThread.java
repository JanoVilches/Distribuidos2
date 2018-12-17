import java.net.*;
import java.io.*;

public class ClienteThread implements Runnable {
    private Thread client;
    private Socket sc = null;
    private DataInputStream entrada = null;
    private DataOutputStream salida = null;
    private String ipOrigen; //IP desde donde fue creado
    private int puertoOrigen; //Puerto desde donde fue creado
    private String ipDestino; //IP a donde enviara el mensaje
    private int puertoDestino; //Puerto a donde enviará el mensaje
    private String nombre; 
    private String mensaje; //mensaje en forma string

    public ClienteThread (String ipO, int puertoO, String ipD, int puertoD, String nombre, String mensaje) {
        this.ipOrigen = ipO;
        this.puertoOrigen = puertoO;
        this.ipDestino = ipD;
        this.puertoDestino = puertoD;
        this.nombre = nombre;
        this.mensaje = mensaje;
    }

    public void run() {
        try {
            sc = new Socket(ipDestino, puertoDestino); //se crea la conexion con el destino.

            salida = new DataOutputStream(sc.getOutputStream());
            salida.writeUTF(mensaje); //envio del mensaje en formato String.
            salida.flush();
            salida.close();
            sc.close();
        } catch (Exception e) {
            try{
                System.out.println("Error al conectarse con el servidor");
                sc = new Socket(ipOrigen, puertoOrigen); //nos conectamos al servidor que nos llamó.
                System.out.println("Enviando reporte de error en la conexion con " + Integer.toString(puertoDestino) + ", se necesita coordinar nuevamente"); //cambiar luego a IP.
                salida = new DataOutputStream(sc.getOutputStream());
                salida.writeUTF("404");//sera la notificacion de error si se cae una maquina y no se logra conectar.

            }
            catch(ConnectException ex){
                System.out.println(ex);
            }
            catch(UnknownHostException u){
                System.out.println(u);
            }
            catch(IOException i){
                System.out.println(i);
            }
     }
    }

    public void start(){
        if (client == null) {
            client = new Thread (this, nombre);
            client.start ();
        }
    }
}