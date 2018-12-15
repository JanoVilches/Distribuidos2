
public class Enfermero{
    private Long ID;
    private String Nombre;
    private String Apellido;
    private Long estudios;
    private Long experiencia;


    public Enfermero() {
    }

    public Enfermero(Long ID, String Nombre, String Apellido, Long estudios, Long experiencia) {
        this.ID = ID;
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.estudios = estudios;
        this.experiencia = experiencia;
    }

    public Long getID() {
        return this.ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return this.Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellido() {
        return this.Apellido;
    }

    public void setApellido(String Apellido) {
        this.Apellido = Apellido;
    }

    public Long getEstudios() {
        return this.estudios;
    }

    public void setEstudios(Long estudios) {
        this.estudios = estudios;
    }

    public Long getExperiencia() {
        return this.experiencia;
    }

    public void setExperiencia(Long experiencia) {
        this.experiencia = experiencia;
    }
}