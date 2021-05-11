/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 
package clases;

/**
 *
 * @author manum
 */
public class Material {
    private String nombre;
    private String descripcion;
    private int unidades;
    private Instalacion instalacion;

    public Material(String nombre, String descripcion, int unidades, Instalacion i) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.unidades = unidades;
        this.instalacion =i;
        
    }

    public Instalacion getInstalacion() {
        return instalacion;
    }

    public void setInstalacion(Instalacion instalacion) {
        this.instalacion = instalacion;
    }

    public Material() {

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

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }
   
      @Override
    public String toString() {
        return "Material{" + "nombre=" + nombre + ", descripcion=" + descripcion + ", unidades=" + unidades + '}';
    }
}
