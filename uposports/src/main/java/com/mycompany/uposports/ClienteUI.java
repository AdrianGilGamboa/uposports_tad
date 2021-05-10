package com.mycompany.uposports;

import bbdd.AbonosDAO;
import bbdd.ClienteDAO;
import clases.Abono;
import clases.Cliente;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.net.UnknownHostException;
import javax.servlet.annotation.WebServlet;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Theme("mytheme")
@Title("Cliente")
public class ClienteUI extends UI {

    public static ArrayList<Cliente> listaClientes = new ArrayList(); //LISTA DONDE ESTARÁN ALMACENADOS TODOS LOS CLIENTES QUE CREEMOS
    public VerticalLayout layout = new VerticalLayout(); //LAYOUT PRINCIPAL
    final HorizontalLayout layoutH = new HorizontalLayout();//Creamos un layout horizontal
    final HorizontalLayout layoutHLabelabelTitulo = new HorizontalLayout();//Creamos un layout horizontal
    final HorizontalLayout layoutH2 = new HorizontalLayout();//Creamos un layout horizontal

    @Override
    protected void init(VaadinRequest request) {
        try {
            layout.removeAllComponents();
            layoutHLabelabelTitulo.removeAllComponents();
            layoutH2.removeAllComponents();
            //RECUPERAMOS LA SESION Y SI NO HAY SESION NOS REDIRIGE A LA PÁGINA DE INICIO DE SESIÓN
            WrappedSession session = getSession().getSession();
            if (session.getAttribute("nombreUsuario") == null) {
                getUI().getPage().setLocation("/login");
            }

            Label l = new Label("<h1 style='text-weight:bold;text-align:center;margin:auto;    padding-right: 100px;'>UPOSports</h2>", ContentMode.HTML);
            Label labelEntidad = new Label("<h2 style='text-weight:bold;margin:0'>Clientes - </h2>", ContentMode.HTML);

            Button buttonAbonos = new Button("Abonos", FontAwesome.MONEY);//Botón para acceder a la entidad abono
            buttonAbonos.addClickListener(e -> {//Acción del botón
                getUI().getPage().setLocation("/Abono");//Accedemos a la entidad abono
            });

            Button buttonCliente = new Button("Clientes", FontAwesome.MALE);//Botón para acceder a la entidad instalaciones
            buttonCliente.addClickListener(e -> {//Acción del botón
                getUI().getPage().setLocation("/Cliente");//Accedemos a la entidad abono
            });

            Button buttonInstalacion = new Button("Instalaciones", FontAwesome.BUILDING);//Botón para acceder a la entidad instalaciones
            buttonInstalacion.addClickListener(e -> {//Acción del botón
                getUI().getPage().setLocation("/Instalacion");//Accedemos a la entidad abono
            });

            Button buttonEmpleados = new Button("Empleados", FontAwesome.USERS);//Botón para acceder a la entidad instalaciones
            buttonEmpleados.addClickListener(e -> {//Acción del botón
                getUI().getPage().setLocation("/Empleado");//Accedemos a la entidad abono
            });

            Button buttonMateriales = new Button("Materiales", FontAwesome.ARCHIVE);//Botón para acceder a la entidad instalaciones
            buttonMateriales.addClickListener(e -> {//Acción del botón
                getUI().getPage().setLocation("/Material");//Accedemos a la entidad abono
            });

            Button buttonReservas = new Button("Reservas", FontAwesome.CALENDAR);//Botón para acceder a la entidad instalaciones
            buttonReservas.addClickListener(e -> {//Acción del botón
                getUI().getPage().setLocation("/Reserva");//Accedemos a la entidad abono
            });

            Button buttonLogout = new Button("Cerrar Sesión", FontAwesome.SIGN_OUT);//Botón para cerrar sesión
            buttonLogout.addClickListener(e -> {//Acción del botón
                VaadinSession.getCurrent().getSession().invalidate();//Eliminamos la sesión
                getUI().getPage().setLocation("/");//Accedemos a la página principal
            });

            Button botonAdd = new Button("Crear Cliente", FontAwesome.PLUS_CIRCLE); //BOTÓN PARA AÑADIR CLIENTES
            layoutH2.setMargin(true);
            layoutH2.setSpacing(true);
            layoutH2.addComponents(labelEntidad, botonAdd);

            layoutHLabelabelTitulo.addComponent(l);
            layoutH.addComponents(layoutHLabelabelTitulo, buttonReservas, buttonCliente, buttonAbonos, buttonInstalacion, buttonMateriales, buttonEmpleados, buttonLogout);//Añadimos los componentes al layout horizontal
            layoutH2.setMargin(true);
            layoutH2.setSpacing(true);
            layoutH2.addComponents(labelEntidad, botonAdd);
            layout.addComponents(layoutH, layoutH2);//Añadimos los componentes al layout horizontal

            //CREAMOS UNA TABLA DONDE APARECERÁ LA LISTA DE CLIENTES
            Table tabla = new Table();
            tabla.setSizeFull();
            tabla.addContainerProperty("Nombre", String.class, null);
            tabla.addContainerProperty("Apellidos", String.class, null);
            tabla.addContainerProperty("DNI", String.class, null);
            tabla.addContainerProperty("Teléfono", String.class, null);
            tabla.addContainerProperty("Codigo Postal", String.class, null);
            tabla.addContainerProperty("Abono", String.class, null);
            tabla.addContainerProperty("Editar", Button.class, null);
            tabla.addContainerProperty("Eliminar", Button.class, null);

            ArrayList clientes = ClienteDAO.consultaClientes();
            Iterator it = clientes.iterator();

            int i = 0;
            //BUCLE PARA AÑADIR TODOS LOS CLIENTES A LA TABLA
            if (!clientes.isEmpty()) {
                while (it.hasNext()) {
                    Button eliminar = new Button("Eliminar", FontAwesome.CLOSE);
                    Button editar = new Button("Editar", FontAwesome.EDIT);
                    Cliente aux = (Cliente) it.next();
                    tabla.addItem(new Object[]{aux.getNombre(), aux.getApellidos(), aux.getDni(), aux.getTelefono(), aux.getCodigoPostal(), aux.getAbono().getTipo(), editar, eliminar}, i);
                    i++;
                    eliminar.addClickListener(e -> {
                        try {
                            //AÑADIMOS EL BOTON DE ELIMINAR POR CADA CLIENTE
                            //listaClientes.remove(aux); //Elimina el cliente de la lista                    
                            ClienteDAO.eliminaCliente(aux);
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(ClienteUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        init(request);
                    });

                    editar.addClickListener(e -> {
                        try {
                            //AÑADIMOS EL BOTON DE EDITAR POR CADA CLIENTE
                            editarCliente(request, aux); //EJECUTA LA FUNCION EDITARCLIENTE
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(ClienteUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
                //tabla.setHeight("auto");
                layout.addComponent(tabla);
            }
            layout.setMargin(true);
            layout.setSpacing(true);
            setContent(layout);

            botonAdd.addClickListener(e -> {
                try {
                    addCliente(request);
                    // getUI().getPage().setLocation("/nuevoCliente");  //REDIRECCIONA A LA CLASE nuevoCliente            
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ClienteUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClienteUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addCliente(VaadinRequest request) throws UnknownHostException { //METODO QUE AÑADE UN CLIENTE A LA LISTA
        //CREAMOS UN FORMULARIO PARA QUE EL USUARIO INTRODUZCA LOS DATOS DEL CLIENTE A CREAR
        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout datos = new HorizontalLayout();
        Label l = new Label("<h2 style='text-weight:bold;'>Nuevo Cliente</h2>", ContentMode.HTML);
        layout.addComponent(l);
        TextField nombre = new TextField("Introduzca el nombre:");
        TextField apellidos = new TextField("Introduzca los Apellidos:");
        TextField dni = new TextField("Introduzca el DNI:");
        TextField telef = new TextField("Introduzca el Telefono:");
        TextField cp = new TextField("Introduzca el Código Postal:");
        OptionGroup abono = new OptionGroup("Abono:");
        ArrayList<Abono> listaAbonos = AbonosDAO.mostrarAbonos();
        for (int i = 0; i < listaAbonos.size(); i++) {
            System.out.println(listaAbonos.get(i).getTipo());
            abono.addItems(listaAbonos.get(i).getTipo());
        }
        datos.addComponents(nombre, apellidos, dni, telef, cp, abono);
        datos.setMargin(true);
        datos.setSpacing(true);
        Button enviar = new Button("Guardar", FontAwesome.CHECK);

        enviar.addClickListener(e -> { //UNA VEZ PULSADO EL BOTÓN SE CREA EL CLIENTE Y LO AÑADIMOS A LA LISTA
            if (nombre.getValue().equals("") || apellidos.getValue().equals("") || dni.getValue().equals("") || telef.getValue().equals("") || cp.getValue().equals("")) {
                Notification.show("Campo vacío", "No se permiten campos vacíos",
                        Notification.Type.WARNING_MESSAGE);
            } else {
                Cliente aux = new Cliente();
                aux.setNombre(nombre.getValue());
                aux.setApellidos(apellidos.getValue());
                aux.setDni(dni.getValue());
                aux.setTelefono(telef.getValue());
                aux.setCodigoPostal(cp.getValue());
                try {
                    aux.setAbono(AbonosDAO.buscarAbono((String) abono.getValue()));
                    //listaClientes.add(aux);
                    ClienteDAO.creaCliente(aux);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ClienteUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                Notification.show("Cliente - DNI: " + aux.getDni(), "Registrado con éxito",
                        Notification.Type.TRAY_NOTIFICATION);
                init(request);
            }
        });
        Button cancelar = new Button("Cancelar", FontAwesome.CLOSE);
        //REDIRECCIONA A LA CLASE gestionaCLiente
        cancelar.addClickListener(e -> {
            init(request);
        });
        HorizontalLayout horiz = new HorizontalLayout();
        horiz.addComponents(cancelar, enviar);
        horiz.setSpacing(true);
        horiz.setMargin(true);
        layout.addComponents(datos, horiz);
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);

    }

    public void editarCliente(VaadinRequest request, Cliente cliente) throws UnknownHostException {
        layout.removeAllComponents();
        //CREAMOS UN FORMULARIO PARA PODER EDITAR EL CLIENTE
        HorizontalLayout datos = new HorizontalLayout();
        Label l = new Label("<h2>Editar Cliente</h2>", ContentMode.HTML);
        layout.addComponent(l);
        TextField nombre = new TextField("Introduzca el nombre:");
        nombre.setValue(cliente.getNombre());
        TextField apellidos = new TextField("Introduzca los Apellidos:");
        apellidos.setValue(cliente.getApellidos());
        TextField dni = new TextField("Introduzca el DNI:");
        dni.setValue(cliente.getDni());
        TextField telef = new TextField("Introduzca el Telefono:");
        telef.setValue(cliente.getTelefono());
        TextField cp = new TextField("Introduzca el Código Postal:");
        cp.setValue(cliente.getCodigoPostal());
        OptionGroup abono = new OptionGroup("Abono:");
        ArrayList<Abono> listaAbonos = AbonosDAO.mostrarAbonos();
        for (int i = 0; i < listaAbonos.size(); i++) {
            System.out.println(listaAbonos.get(i).getTipo());
            abono.addItems(listaAbonos.get(i).getTipo());
        }
        datos.addComponents(nombre, apellidos, dni, telef, cp, abono);
        datos.setMargin(true);
        datos.setSpacing(true);
        Button enviar = new Button("Enviar", FontAwesome.CHECK);
        //EDITAMOS TODOS LOS CAMPOS QUE HAYA MODIFICADO EL USUARIO Y VOLVEMOS A INSERTAR EL CLIENTE EN LA LISTA
        enviar.addClickListener(e -> {
            if (nombre.getValue().equals("") || apellidos.getValue().equals("") || dni.getValue().equals("") || telef.getValue().equals("") || cp.getValue().equals("")) {
                Notification.show("Campo vacío", "No se permiten campos vacíos",
                        Notification.Type.WARNING_MESSAGE);
            } else {
                Cliente aux = new Cliente();
                aux.setNombre(nombre.getValue());
                aux.setApellidos(apellidos.getValue());
                aux.setDni(dni.getValue());
                aux.setTelefono(telef.getValue());
                aux.setCodigoPostal(cp.getValue());
                try {
                    aux.setAbono(AbonosDAO.buscarAbono((String) abono.getValue()));
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ClienteUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    ClienteDAO.actualizaCliente(aux, cliente);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ClienteUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                //listaClientes.remove(cliente);
                //addCliente(aux);
                init(request);
            }

        });
        Button cancelar = new Button("Cancelar", FontAwesome.CLOSE);
        //REDIRECCIONA A LA CLASE gestionaCLiente
        cancelar.addClickListener(e -> {
            init(request);
        });
        HorizontalLayout horiz = new HorizontalLayout();
        horiz.addComponents(cancelar, enviar);
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
