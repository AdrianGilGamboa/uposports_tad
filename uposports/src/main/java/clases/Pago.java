/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.util.Date;

/**
 *
 * @author manum
 */
public class Pago {
    private Date fecha;
    private float cantidad;
    private Date hora;
    private float importeDevolver;
    public Pago() {
    }
    
        

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

   

    @Override
    public String toString() {
        return "pagoTarjeta{" + ", fecha=" + fecha + ", cantidad=" + cantidad + ", hora=" + hora + '}';
    }

    
    
}
