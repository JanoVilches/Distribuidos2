import java.util.ArrayList;

public class Paciente{
    Long id;
    String nombre;
    String rut;
    int edad;
    ArrayList<String> enfermedades;
    ArrayList<String> procAsignados;
    ArrayList<String> procCompletados;
    ArrayList<String> examRealizados;
    ArrayList<String> examNoRealizados;
    ArrayList<String> medRecetados;
    ArrayList<String> medSuminist;
    
    public Paciente() {
    }
    
    public Paciente(Long id, String nombre, String rut, int edad, ArrayList<String> enfermedades, ArrayList<String> procAsignados, ArrayList<String> procCompletados, ArrayList<String> examRealizados, ArrayList<String> examNoRealizados, ArrayList<String> medRecetados, ArrayList<String> medSuminist) {
        this.id = id;
        this.nombre = nombre;
        this.rut = rut;
        this.edad = edad;
        this.enfermedades = enfermedades;
        this.procAsignados = procAsignados;
        this.procCompletados = procCompletados;
        this.examRealizados = examRealizados;
        this.examNoRealizados = examNoRealizados;
        this.medRecetados = medRecetados;
        this.medSuminist = medSuminist;
    }
    
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getRut() {
       return this.rut;
    }
    
    public void setRut(String rut) {
        this.rut = rut;
    }
    
    public int getEdad() {
       return this.edad;
    }
    
    public void setEdad(int edad) {
        this.edad = edad;
    }
    
    public ArrayList<String> getEnfermedades() {
        return this.enfermedades;
    }
    
    public void setEnfermedades(ArrayList<String> enfermedades) {
        this.enfermedades = enfermedades;
    }
    
    public ArrayList<String> getProcAsignados() {
        return this.procAsignados;
    }
    
    public void setProcAsignados(ArrayList<String> procAsignados) {
        this.procAsignados = procAsignados;
    }
    
    public ArrayList<String> getProcCompletados() {
        return this.procCompletados;
    }
    
    public void setProcCompletados(ArrayList<String> procCompletados) {
        this.procCompletados = procCompletados;
    }
    
    public ArrayList<String> getExamRealizados() {
        return this.examRealizados;
    }
    
    public void setExamRealizados(ArrayList<String> examRealizados) {
        this.examRealizados = examRealizados;
    }
    
    public ArrayList<String> getExamNoRealizados() {
        return this.examNoRealizados;
    }
    
    public void setExamNoRealizados(ArrayList<String> examNoRealizados) {
        this.examNoRealizados = examNoRealizados;
    }
    
    public ArrayList<String> getMedRecetados() {
        return this.medRecetados;
    }
    
    public void setMedRecetados(ArrayList<String> medRecetados) {
        this.medRecetados = medRecetados;
    }
    
    public ArrayList<String> getMedSuminist() {
        return this.medSuminist;
    }
    
    public void setMedSuminist(ArrayList<String> medSuminist) {
        this.medSuminist = medSuminist;
    }
}