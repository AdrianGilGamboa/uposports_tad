/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.epd_evaluable;

/**
 *
 * @author manum
 */
public class pagoTarjeta {
    private String entidadBancaria;

    public pagoTarjeta(String entidadBancaria) {
        this.entidadBancaria = entidadBancaria;
    }

    public String getEntidadBancaria() {
        return entidadBancaria;
    }

    public void setEntidadBancaria(String entidadBancaria) {
        this.entidadBancaria = entidadBancaria;
    }

    @Override
    public String toString() {
        return "pagoTarjeta{" + "entidadBancaria=" + entidadBancaria + '}';
    }
    
}
