package clases;

public class Abono {

    private String tipo;
    private Integer duracion;
    private Double precio;

    public Abono() {

    }

    @Override
    public String toString() {
        return "Abono{" + "tipo=" + tipo + ", duracion=" + duracion + ", precio=" + precio + '}';
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

}
