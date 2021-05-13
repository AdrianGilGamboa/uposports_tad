package clases;

public class Cliente {

    private String dni;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String codigoPostal;
    private Abono abono;

    public Cliente() {
    }

    public Cliente(String dni, String nombre, String apellidos, String telefono, String codigoPostal, Abono abono) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.codigoPostal = codigoPostal;
        this.abono = abono;
    }

    public Cliente(String dni, String nombre) {
        this.dni = dni;
        this.nombre = nombre;
    }

    public Abono getAbono() {
        return abono;
    }

    public void setAbono(Abono abono) {
        this.abono = abono;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
}
