package com.mycompany.uposports;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WrappedSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import javax.servlet.annotation.WebServlet;
import java.util.*;

@Theme("mytheme")
public class nuevoCliente extends UI{
    @Override
    protected void init(VaadinRequest request) {
        //RECUPERAMOS LA SESION Y SI NO HAY SESION NOS REDIRIGE A LA PÁGINA DE INICIO DE SESIÓN
        WrappedSession session = getSession().getSession();
        if(session.getAttribute("nombreUsuario") == null){
            getUI().getPage().setLocation("/inicioSesion");
        }
        //CREAMOS UN FORMULARIO PARA QUE EL USUARIO INTRODUZCA LOS DATOS DEL CLIENTE A CREAR
       VerticalLayout layout = new VerticalLayout();
        HorizontalLayout datos = new HorizontalLayout();
        Label l = new Label("<h2 style='text-weight:bold;'>Nuevo Cliente</h2>",ContentMode.HTML);
        layout.addComponent(l);
        TextField nombre = new TextField("Introduzca el nombre");
        TextField apellidos = new TextField("Introduzca los Apellidos");
        TextField dni = new TextField("Introduzca el DNI");
        TextField telef = new TextField("Introduzca el Telefono");
        TextField cp = new TextField("Introduzca el Código Postal");
        datos.addComponents(nombre,apellidos,dni,telef,cp);
        datos.setMargin(true);
        datos.setSpacing(true);
        Button enviar = new Button("Enviar",FontAwesome.SEND);
        
        enviar.addClickListener(e -> { //UNA VEZ PULSADO EL BOTÓN SE CREA EL CLIENTE Y LO AÑADIMOS A LA LISTA
            Cliente aux=new Cliente();
            aux.setNombre(nombre.getValue());
            aux.setApellidos(apellidos.getValue());
            aux.setDni(dni.getValue());
            aux.setTelefono(telef.getValue());
            aux.setCodigoPostal(cp.getValue());
            gestionaCliente.addCliente(aux);
            layout.addComponent(new Label("Se va a añadir "+gestionaCliente.listaClientes.get(0).getNombre()));
            getUI().getPage().setLocation("/gestionaCliente");
        });
        Button volver = new Button("Volver",FontAwesome.ARROW_LEFT);
        volver.addClickListener( e -> {
           getUI().getPage().setLocation("/gestionaCliente");
        });
        HorizontalLayout horiz = new HorizontalLayout();
        horiz.addComponents(volver,enviar);
        horiz.setSpacing(true);
        horiz.setMargin(true);
        layout.addComponents(datos,horiz);
        layout.setMargin(true);
        layout.setSpacing(true);        
        setContent(layout);
        
    }
    
    
    @WebServlet(urlPatterns = {"/nuevoCliente/*"}, name = "nuevoClienteServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = nuevoCliente.class, productionMode = false)
    public static class nuevoClienteServlet extends VaadinServlet {
    }
}
