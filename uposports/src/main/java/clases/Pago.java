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
    private Date fechaHora;
    private Double cantidad;
    private Double importeDevolver;
    
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


    public Double getImporteDevolver() {
        return importeDevolver;
    }

    public void setImporteDevolver(Double importeDevolver) {
        this.importeDevolver = importeDevolver;
    }
    
    

   

    @Override
    public String toString() {
        return "pagoTarjeta{" + ", fecha=" + fechaHora + ", cantidad=" + cantidad + '}';
    }

    
    
}
