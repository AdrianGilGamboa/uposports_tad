
package clases;

import bbdd.ReservaDAO;
import java.util.Date;


public class Reserva {
    private int id_reserva;
    private Date inicioReserva;
    private Date finReserva;
    private Cliente cliente;
    private Instalacion instalacion;
    
    public Reserva(){
        id_reserva=ReservaDAO.siguienteID();
    }

    public int getId_reserva() {
        return id_reserva;
    }

    public void setId_reserva(int id_reserva) {
        this.id_reserva = id_reserva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Instalacion getInstalacion() {
        return instalacion;
    }

    public void setInstalacion(Instalacion instalacion) {
        this.instalacion = instalacion;
    }

    public Date getInicioReserva() {
        return inicioReserva;
    }

    public void setInicioReserva(Date inicioReserva) {
        this.inicioReserva = inicioReserva;
    }

    public Date getFinReserva() {
        return finReserva;
    }

    public void setFinReserva(Date finReserva) {
        this.finReserva = finReserva;
    }

}
