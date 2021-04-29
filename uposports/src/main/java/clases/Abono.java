package clases;

//Clase para la instancia de Abono, con su constructor, toString y getters y setters.

public class Abono {

    private String tipo;
    private Integer duracion;
    private Float coste;

    public Abono() {

    }

    @Override
    public String toString() {
        return "Abono{" + "tipo=" + tipo + ", duracion=" + duracion + ", coste=" + coste + '}';
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

    public Float getCoste() {
        return coste;
    }

    public void setCoste(Float coste) {
        this.coste = coste;
    }

}
