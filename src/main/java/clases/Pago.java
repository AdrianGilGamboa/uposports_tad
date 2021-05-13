
package clases;

import java.util.Date;


public class Pago {

    private Date fechaHora;
    private Double cantidad;

    public Pago() {
    }

    public Date getFecha() {
        return fechaHora;
    }

    public void setFecha(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

}
