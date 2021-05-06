/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.uposports;

import clases.Pago;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WrappedSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author manum
 * */
public class PagoUI extends UI {
     @Override
    protected void init(VaadinRequest vaadinRequest) {
        //Empezamos obteniendo la sesión y creando una lista de empleados para
        //ir mentiendo cuando se vayan creando
        WrappedSession sesion = getSession().getSession();
        final FormLayout form = new FormLayout();
        //Añadimos el Label de bienvenida
        //Creamos el boton "Crear empleado" donde implementaremos la funcion de crear un empleado
        Button b = new Button("Pagar");
        b.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                //Añadimos por cada atributo del empleado su textfield
               Pago p = new Pago();
                        getUI().getPage().setLocation("/login");   
            }  
        });
//Se crea el boton de ver empleados el cual, al pulsarse, imprimirá la lista de todos los 
//empleados disponibles
        Button b3 = new Button("Pagar con tarjeta");
        b3.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                 getUI().getPage().setLocation("/login");   

                }
        }
        );
        form.addComponent(b);
        form.addComponent(b3);
    }

    @WebServlet(urlPatterns = "/gestionPago/*", name = "ServletGestionPago", asyncSupported = true)
    @VaadinServletConfiguration(ui = PagoUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
        
    }
}



