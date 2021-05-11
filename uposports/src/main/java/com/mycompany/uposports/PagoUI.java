
package com.mycompany.uposports;

import clases.Pago;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WrappedSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;
import javax.servlet.annotation.WebServlet;

@Theme("mytheme")
@Title("Pago")
public class PagoUI extends UI {
     @Override
    protected void init(VaadinRequest vaadinRequest) {
        //Empezamos obteniendo la sesión y creando una lista de empleados para
        //ir mentiendo cuando se vayan creando
                //RECUPERAMOS LA SESION Y SI NO HAY SESION NOS REDIRIGE A LA PÁGINA DE INICIO DE SESIÓN
        WrappedSession session = getSession().getSession();
        if (session.getAttribute("nombreUsuario") == null) {
            getUI().getPage().setLocation("/login");
        }
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
         setContent(form);
    }

    @WebServlet(urlPatterns = "/Pago/*", name = "ServletGestionPago", asyncSupported = true)
    @VaadinServletConfiguration(ui = PagoUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
        
    }
}



