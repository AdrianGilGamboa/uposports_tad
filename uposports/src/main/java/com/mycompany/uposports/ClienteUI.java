package com.mycompany.uposports;

import clases.Cliente;
import com.mycompany.uposports.*;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WrappedSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import javax.servlet.annotation.WebServlet;
import java.util.*;

@Theme("mytheme")
public class ClienteUI extends UI {

    public static ArrayList<Cliente> listaClientes = new ArrayList(); //LISTA DONDE ESTARÁN ALMACENADOS TODOS LOS CLIENTES QUE CREEMOS
    public VerticalLayout layout = new VerticalLayout(); //LAYOUT PRINCIPAL

    @Override
    protected void init(VaadinRequest request) {       
        //layout.removeAllComponents();
        //RECUPERAMOS LA SESION Y SI NO HAY SESION NOS REDIRIGE A LA PÁGINA DE INICIO DE SESIÓN
        WrappedSession session = getSession().getSession();
        if (session.getAttribute("nombreUsuario") == null) {
            getUI().getPage().setLocation("/login");
        }
          
        //CREAMOS UNA TABLA DONDE APARECERÁ LA LISTA DE CLIENTES
        layout.removeAllComponents();
        Table tabla = new Table();
        tabla.addContainerProperty("Nombre", String.class, null);
        tabla.addContainerProperty("Apellidos", String.class, null);
        tabla.addContainerProperty("DNI", String.class, null);
        tabla.addContainerProperty("Teléfono", String.class, null);
        tabla.addContainerProperty("Codigo Postal", String.class, null);
        tabla.addContainerProperty("Eliminar", Button.class, null);
        tabla.addContainerProperty("Editar", Button.class, null);
        Button botonAdd = new Button("Añadir Cliente"); //BOTÓN PARA AÑADIR CLIENTES 
        layout.addComponent(botonAdd);
        Iterator it = listaClientes.iterator();
        int i = 0;
        //BUCLE PARA AÑADIR TODOS LOS CLIENTES A LA TABLA
        while (it.hasNext()) {
            Button eliminar = new Button("Eliminar");
            Button editar = new Button("Editar");
            Cliente aux = (Cliente) it.next();
            tabla.addItem(new Object[]{aux.getNombre(), aux.getApellidos(), aux.getDni(), aux.getTelefono(), aux.getCodigoPostal(), eliminar, editar}, i);
            i++;
            eliminar.addClickListener(e -> {  //AÑADIMOS EL BOTON DE ELIMINAR POR CADA CLIENTE
                listaClientes.remove(aux); //Elimina el cliente de la lista
                init(request);
            });

            editar.addClickListener(e -> { //AÑADIMOS EL BOTON DE EDITAR POR CADA CLIENTE
                editarCliente(request, aux); //EJECUTA LA FUNCION EDITARCLIENTE
            });
        }
        Button volver = new Button("Volver", FontAwesome.ARROW_LEFT);
        layout.addComponents(tabla, volver);
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);

        volver.addClickListener(e -> {
            getUI().getPage().setLocation("/"); //REDIRECCIONA A LA CLASE MENU
        });

        botonAdd.addClickListener(e -> {
            addCliente(request);
            // getUI().getPage().setLocation("/nuevoCliente");  //REDIRECCIONA A LA CLASE nuevoCliente
        });
    }
    


    public void addCliente(VaadinRequest request) { //METODO QUE AÑADE UN CLIENTE A LA LISTA
        //CREAMOS UN FORMULARIO PARA QUE EL USUARIO INTRODUZCA LOS DATOS DEL CLIENTE A CREAR
        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout datos = new HorizontalLayout();
        Label l = new Label("<h2 style='text-weight:bold;'>Nuevo Cliente</h2>", ContentMode.HTML);
        layout.addComponent(l);
        TextField nombre = new TextField("Introduzca el nombre");
        nombre.setRequired(true);
        nombre.addValidator(new NullValidator("Campo obligatorio",false));
        TextField apellidos = new TextField("Introduzca los Apellidos");
        apellidos.setRequired(true);
        apellidos.addValidator(new NullValidator("Campo obligatorio",false));
        TextField dni = new TextField("Introduzca el DNI");
        dni.setRequired(true);
        dni.addValidator(new NullValidator("Campo obligatorio",false));
        TextField telef = new TextField("Introduzca el Telefono");
        telef.setRequired(true);
        telef.addValidator(new NullValidator("Campo obligatorio",false));
        TextField cp = new TextField("Introduzca el Código Postal");
        cp.setRequired(true);
        cp.addValidator(new NullValidator("Campo obligatorio",true));
        datos.addComponents(nombre, apellidos, dni, telef, cp);
        datos.setMargin(true);
        datos.setSpacing(true);
        Button enviar = new Button("Guardar", FontAwesome.SEND);

        enviar.addClickListener(e -> { //UNA VEZ PULSADO EL BOTÓN SE CREA EL CLIENTE Y LO AÑADIMOS A LA LISTA
            if(nombre.getValue().equals("") || apellidos.getValue().equals("") || dni.getValue().equals("") || telef.getValue().equals("") || cp.getValue().equals("")){
                Notification.show("Campo vacío", "No se permiten campos vacíos",
                            Notification.Type.WARNING_MESSAGE);
            }else{
            Cliente aux = new Cliente();
            aux.setNombre(nombre.getValue());
            aux.setApellidos(apellidos.getValue());
            aux.setDni(dni.getValue());
            aux.setTelefono(telef.getValue());
            aux.setCodigoPostal(cp.getValue());
            // ClienteUI.addCliente(aux);
            //layout.addComponent(new Label("Se va a añadir "+ClienteUI.listaClientes.get(0).getNombre()));
            // getUI().getPage().setLocation("/gestionaCliente");
            listaClientes.add(aux);
            init(request);
            }
        });
        Button volver = new Button("Volver", FontAwesome.ARROW_LEFT);
        volver.addClickListener(e -> {
            init(request);
        });
        HorizontalLayout horiz = new HorizontalLayout();
        horiz.addComponents(volver, enviar);
        horiz.setSpacing(true);
        horiz.setMargin(true);
        layout.addComponents(datos, horiz);
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);

    }

    public void editarCliente(VaadinRequest request, Cliente cliente) {
        layout.removeAllComponents();
        //CREAMOS UN FORMULARIO PARA PODER EDITAR EL CLIENTE
        HorizontalLayout datos = new HorizontalLayout();
        Label l = new Label("<h2>Editar Cliente</h2>", ContentMode.HTML);
        layout.addComponent(l);
        TextField nombre = new TextField("Introduzca el nombre");
        nombre.setRequired(true);
        nombre.addValidator(new NullValidator("Campo obligatorio",false));
        nombre.setValue(cliente.getNombre());
        TextField apellidos = new TextField("Introduzca los Apellidos");
        apellidos.setRequired(true);
        apellidos.addValidator(new NullValidator("Campo obligatorio",false));
        apellidos.setValue(cliente.getApellidos());
        TextField dni = new TextField("Introduzca el DNI");
        dni.setRequired(true);
        dni.addValidator(new NullValidator("Campo obligatorio",false));
        dni.setValue(cliente.getDni());
        TextField telef = new TextField("Introduzca el Telefono");
        telef.setRequired(true);
        telef.addValidator(new NullValidator("Campo obligatorio",false));
        telef.setValue(cliente.getTelefono());
        TextField cp = new TextField("Introduzca el Código Postal");
        cp.setRequired(true);
        cp.addValidator(new NullValidator("Campo obligatorio",false));
        cp.setValue(cliente.getCodigoPostal());
        datos.addComponents(nombre, apellidos, dni, telef, cp);
        datos.setMargin(true);
        datos.setSpacing(true);
        Button enviar = new Button("Enviar", FontAwesome.SEND);
        //EDITAMOS TODOS LOS CAMPOS QUE HAYA MODIFICADO EL USUARIO Y VOLVEMOS A INSERTAR EL CLIENTE EN LA LISTA
        enviar.addClickListener(e -> {
            if(nombre.getValue().equals("") || apellidos.getValue().equals("") || dni.getValue().equals("") || telef.getValue().equals("") || cp.getValue().equals("")){
                Notification.show("Campo vacío", "No se permiten campos vacíos",
                            Notification.Type.WARNING_MESSAGE);
            }else{
                cliente.setNombre(nombre.getValue());
            cliente.setApellidos(apellidos.getValue());
            cliente.setDni(dni.getValue());
            cliente.setTelefono(telef.getValue());
            cliente.setCodigoPostal(cp.getValue());
            //listaClientes.remove(cliente);
            //addCliente(aux);
            init(request);
            }            
            
        });
        Button volver = new Button("Volver", FontAwesome.ARROW_LEFT);
        //REDIRECCIONA A LA CLASE gestionaCLiente
        volver.addClickListener(e -> {
            getUI().getPage().setLocation("/gestionaCliente");
        });
        HorizontalLayout horiz = new HorizontalLayout();
        horiz.addComponents(volver, enviar);
        horiz.setSpacing(true);
        horiz.setMargin(true);
        layout.addComponents(datos, horiz);
        layout.setMargin(true);
        layout.setSpacing(true);
    }

    @WebServlet(urlPatterns = {"/Cliente/*"}, name = "gestionaClienteServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = ClienteUI.class, productionMode = false)
    public static class gestionaClienteServlet extends VaadinServlet {

    }

}
