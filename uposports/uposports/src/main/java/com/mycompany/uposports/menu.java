
package com.mycompany.uposports;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WrappedSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import javax.servlet.annotation.WebServlet;

@Theme("mytheme")

public class menu extends UI{

    @Override
    protected void init(VaadinRequest request) {
        WrappedSession session = getSession().getSession(); //Recuperamos la sesión que tenemos
        final VerticalLayout layout = new VerticalLayout(); //Creamos el layout principal donde se incluirá todos los elementos de la`página      
        final HorizontalLayout horizontal = new HorizontalLayout(); 
        Button botonCerrar = new Button("Cerrar Sesión"); //Boton para cerrar la sesion
        Button botonClientes = new Button("Gestionar Clientes"); //Boton para ir a la página de gestión de clientes
        horizontal.setSpacing(true);  
        horizontal.addComponent(botonClientes);
        layout.addComponent(new Label("Bienvenido "+session.getAttribute("nombreUsuario"))); //Añadimos un mensaje de bienvenida
        layout.addComponents(horizontal,botonCerrar); //Añadimos los botones
        layout.setMargin(true);
        layout.setSpacing(true);        
        setContent(layout);
        
        botonCerrar.addClickListener(e ->{ //Establecemos lo que hace el boton cerrar
            session.invalidate(); //Elimina la sesion
            getUI().getPage().setLocation("/inicioSesion"); //Redirige al iniciar sesion
        });
        
        botonClientes.addClickListener(e ->{  //Establecemos lo que hace el boton clientes
            getUI().getPage().setLocation("/gestionaCliente"); //Redirige a la clase de gestionar clientes
        });
    }
    @WebServlet(urlPatterns = {"/menu/*"}, name = "menuServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = menu.class, productionMode = false)
    public static class menuServlet extends VaadinServlet {
    }
    
}
