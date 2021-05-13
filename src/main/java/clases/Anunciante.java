package clases;

import java.util.Date;

public class Anunciante {

    private String anunciante;
    private Double precioContrato;
    private Date fechaIni;
    private Date fechaFin;

    public Anunciante() {
    }

    public Anunciante(String anunciante, Double precioContrato, Date fechaIni, Date fechaFin) {
        this.anunciante = anunciante;
        this.precioContrato = precioContrato;
        this.fechaIni = fechaIni;
        this.fechaFin = fechaFin;
    }

    public String getAnunciante() {
        return anunciante;
    }

    public void setAnunciante(String anunciante) {
        this.anunciante = anunciante;
    }

    public Double getPrecioContrato() {
        return precioContrato;
    }

    public void setPrecioContrato(Double precioContrato) {
        this.precioContrato = precioContrato;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

}
