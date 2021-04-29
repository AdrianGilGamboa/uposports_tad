package clases;

//Clase para la instancia de Instalacion, con su constructor, toString y getters y setters.
public class Instalacion {

    private String nombre;
    private String descripcion;
    private Integer capacidad;

    public Instalacion() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    @Override
    public String toString() {
        return "Instalacion{" + "nombre=" + nombre + ", descripcion=" + descripcion + ", capacidad=" + capacidad + '}';
    }

   

}
