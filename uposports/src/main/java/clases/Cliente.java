package clases;


public class Cliente {

    //CREAMOS LOS ATRIBUTOS DE LA CLASE CLIENTE SUS GETTER Y SETTER
    private String dni;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String codigoPostal;
    private Abono abono;

    public Abono getAbono() {
        return abono;
    }

    public void setAbono(Abono abono) {
        this.abono = abono;
    }

   /* public Abono setAbono(String tipo) {
        boolean encontrado=false;
        int i=0;
        Abono a = null;
        while (encontrado != true) {
            if(AbonoUI.listaAbonos.get(i).getTipo().equals(tipo)){
                a = AbonoUI.listaAbonos.get(i);
                encontrado=true;
            }
        }
        this.abono = a;
        return a;
    }*/

    public Cliente(String dni, String nombre) {
        this.dni = dni;
        this.nombre = nombre;
    }

    public Cliente() {
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
