import java.util.ArrayList;

public class Requerimiento{
    Long id;
    String cargo;
    ArrayList<ArrayList<String>> pacientes;

    public Requerimiento() {
    }

    public Requerimiento(Long id, String cargo, ArrayList<ArrayList<String>> pacientes) {
        this.id = id;
        this.cargo = cargo;
        this.pacientes = pacientes;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCargo() {
        return this.cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public ArrayList<ArrayList<String>> getPacientes() {
        return this.pacientes;
    }

    public void setPacientes(ArrayList<ArrayList<String>> pacientes) {
        this.pacientes = pacientes;
    }
}