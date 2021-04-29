
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
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import javax.servlet.annotation.WebServlet;
import java.util.*;

@Theme("mytheme")
public class gestionaCliente extends UI{
    public static ArrayList<Cliente> listaClientes = new ArrayList(); //LISTA DONDE ESTARÁN ALMACENADOS TODOS LOS CLIENTES QUE CREEMOS
    public VerticalLayout layout = new VerticalLayout(); //LAYOUT PRINCIPAL
    @Override
    protected void init(VaadinRequest request) {
        layout.removeAllComponents();
        //RECUPERAMOS LA SESION Y SI NO HAY SESION NOS REDIRIGE A LA PÁGINA DE INICIO DE SESIÓN
        WrappedSession session = getSession().getSession(); 
        if(session.getAttribute("nombreUsuario") == null){
           getUI().getPage().setLocation("/inicioSesion");
        }
       
        //CREAMOS UNA TABLA DONDE APARECERÁ LA LISTA DE CLIENTES
       Table tabla = new Table();
       tabla.addContainerProperty("Nombre", String.class, null);
       tabla.addContainerProperty("Apellidos", String.class, null);
       tabla.addContainerProperty("DNI", String.class, null);
       tabla.addContainerProperty("Teléfono", String.class, null);
       tabla.addContainerProperty("Codigo Postal", String.class, null);
       tabla.addContainerProperty("Eliminar", Button.class, null);
       tabla.addContainerProperty("Editar", Button.class, null);
       tabla.setWidth("50%"); //ESTABLECEMOS EL ANCHO DE LA TABLA
       Button botonAdd = new Button("Añadir Cliente"); //BOTÓN PARA AÑADIR CLIENTES 
       layout.addComponent(botonAdd);
       Iterator it = listaClientes.iterator();
       int i=0;
       //BUCLE PARA AÑADIR TODOS LOS CLIENTES A LA TABLA
       while(it.hasNext()){
           Button eliminar = new Button("Eliminar");
           Button editar = new Button("Editar");
           Cliente aux=(Cliente) it.next();
           tabla.addItem(new Object[]{aux.getNombre(),aux.getApellidos(),aux.getDni(),aux.getTelefono(),aux.getCodigoPostal(),eliminar,editar},i);  
           i++;
           eliminar.addClickListener(e->{  //AÑADIMOS EL BOTON DE ELIMINAR POR CADA CLIENTE
            listaClientes.remove(aux); //Elimina el cliente de la lista
               init(request);
           });
           
           editar.addClickListener(e -> { //AÑADIMOS EL BOTON DE EDITAR POR CADA CLIENTE
               editarCliente(aux); //EJECUTA LA FUNCION EDITARCLIENTE
           });
       }
       Button volver = new Button("Volver",FontAwesome.ARROW_LEFT);
       layout.addComponents(tabla,volver);
       layout.setMargin(true);
       layout.setSpacing(true);        
       setContent(layout);
       
       volver.addClickListener( e -> { 
           getUI().getPage().setLocation("/menu"); //REDIRECCIONA A LA CLASE MENU
       });
       
       botonAdd.addClickListener(e -> {
            getUI().getPage().setLocation("/nuevoCliente");  //REDIRECCIONA A LA CLASE nuevoCliente
       });
    }   
     
    public static void addCliente(Cliente cliente){ //METODO QUE AÑADE UN CLIENTE A LA LISTA
        listaClientes.add(cliente);
    }
    
    public void editarCliente(Cliente cliente){ 
        layout.removeAllComponents(); 
        //CREAMOS UN FORMULARIO PARA PODER EDITAR EL CLIENTE
        HorizontalLayout datos = new HorizontalLayout();
        Label l = new Label("<h2>Editar Cliente</h2>",ContentMode.HTML);
        layout.addComponent(l);
        TextField nombre = new TextField("Introduzca el nombre");
        nombre.setValue(cliente.getNombre());
        TextField apellidos = new TextField("Introduzca los Apellidos");
        apellidos.setValue(cliente.getApellidos());
        TextField dni = new TextField("Introduzca el DNI");
        dni.setValue(cliente.getDni());
        TextField telef = new TextField("Introduzca el Telefono");
        telef.setValue(cliente.getTelefono());
        TextField cp = new TextField("Introduzca el Código Postal");
        cp.setValue(cliente.getCodigoPostal());
        datos.addComponents(nombre,apellidos,dni,telef,cp);
        datos.setMargin(true);
        datos.setSpacing(true);
        Button enviar = new Button("Enviar",FontAwesome.SEND);
        //EDITAMOS TODOS LOS CAMPOS QUE HAYA MODIFICADO EL USUARIO Y VOLVEMOS A INSERTAR EL CLIENTE EN LA LISTA
        enviar.addClickListener(e -> {
            Cliente aux=new Cliente();
            aux.setNombre(nombre.getValue());
            aux.setApellidos(apellidos.getValue());
            aux.setDni(dni.getValue());
            aux.setTelefono(telef.getValue());
            aux.setCodigoPostal(cp.getValue());
            listaClientes.remove(cliente);
            addCliente(aux);
            getUI().getPage().setLocation("/gestionaCliente");
        });
        Button volver = new Button("Volver",FontAwesome.ARROW_LEFT);
        //REDIRECCIONA A LA CLASE gestionaCLiente
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
    }
    
    @WebServlet(urlPatterns = {"/gestionaCliente/*"}, name = "gestionaClienteServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = gestionaCliente.class, productionMode = false)
    public static class gestionaClienteServlet extends VaadinServlet {
        
    }
    
}
