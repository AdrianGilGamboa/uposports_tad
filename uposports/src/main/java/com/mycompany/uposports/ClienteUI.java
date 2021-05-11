package com.mycompany.uposports;

import bbdd.AbonoDAO;
import bbdd.ClienteDAO;
import bbdd.PagoDAO;
import clases.Abono;
import clases.Cliente;
import clases.Pago;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
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
            layoutH.removeAllComponents();
            //RECUPERAMOS LA SESION Y SI NO HAY SESION NOS REDIRIGE A LA PÁGINA DE INICIO DE SESIÓN
            WrappedSession session = getSession().getSession();
            if (session.getAttribute("nombreUsuario") == null) {
                getUI().getPage().setLocation("/login");
            }

            Label l = new Label("<h1 style='text-weight:bold;margin:auto;    padding-right: 100px;'>UPOSports</h2>", ContentMode.HTML);
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

            Button buttonAnunciantes = new Button("Anunciantes", FontAwesome.BELL);//Botón para acceder a la entidad instalaciones
            buttonAnunciantes.addClickListener(e -> {//Acción del botón
                getUI().getPage().setLocation("/Anunciante");//Accedemos a la entidad abono
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
            layoutH.addComponents(layoutHLabelabelTitulo, buttonReservas, buttonCliente, buttonAbonos, buttonInstalacion, buttonMateriales, buttonEmpleados, buttonAnunciantes, buttonLogout);//Añadimos los componentes al layout horizontal
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
            tabla.addContainerProperty("Modificar", Button.class, null);
            tabla.addContainerProperty("Eliminar", Button.class, null);

            ArrayList clientes = ClienteDAO.consultaClientes();
            Iterator it = clientes.iterator();

            int i = 0;
            //BUCLE PARA AÑADIR TODOS LOS CLIENTES A LA TABLA
            if (!clientes.isEmpty()) {
                while (it.hasNext()) {
                    Button eliminar = new Button("Eliminar", FontAwesome.CLOSE);
                    Button editar = new Button("Modificar", FontAwesome.EDIT);
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
        TextField nombre = new TextField("Nombre:");
        nombre.setIcon(FontAwesome.USER);
        TextField apellidos = new TextField("Apellidos:");
        apellidos.setIcon(FontAwesome.USER);
        TextField dni = new TextField("DNI:");
        dni.setIcon(FontAwesome.KEY);
        TextField telef = new TextField("Telefono:");
        telef.setIcon(FontAwesome.PHONE);
        TextField cp = new TextField("Código Postal:");
        cp.setIcon(FontAwesome.MAP_MARKER);
        OptionGroup abono = new OptionGroup("Abono:");
        abono.setIcon(FontAwesome.TAG);
        ArrayList<Abono> listaAbonos = AbonoDAO.mostrarAbonos();
        for (int i = 0; i < listaAbonos.size(); i++) {
            System.out.println(listaAbonos.get(i).getTipo());
            abono.addItems(listaAbonos.get(i).getTipo() + " - " + listaAbonos.get(i).getPrecio() + " (€)");
        }
        datos.addComponents(nombre, apellidos, dni, telef, cp, abono);
        datos.setMargin(true);
        datos.setSpacing(true);
        Button enviar = new Button("Acceder al pago", FontAwesome.SIGN_IN);
        Button cancelar = new Button("Cancelar", FontAwesome.CLOSE);
        //REDIRECCIONA A LA CLASE gestionaCLiente
        cancelar.addClickListener(e -> {
            init(request);
        });
        enviar.addClickListener(e -> { //UNA VEZ PULSADO EL BOTÓN SE CREA EL CLIENTE Y LO AÑADIMOS A LA LISTA
            if (comprobarDatos(nombre.getValue(), apellidos.getValue(), dni.getValue(), telef.getValue(), cp.getValue(), (String) abono.getValue()) == false) {

            } else {
                int index = abono.getValue().toString().indexOf("-");
                String tipoAbono = abono.getValue().toString().substring(0, index - 1);
                Cliente aux = new Cliente();
                aux.setNombre(nombre.getValue());
                aux.setApellidos(apellidos.getValue());
                aux.setDni(dni.getValue());
                aux.setTelefono(telef.getValue());
                aux.setCodigoPostal(cp.getValue());
                try {
                    aux.setAbono(AbonoDAO.buscarAbono(tipoAbono));
                    // System.out.println("Abono:" + abono.getValue());
                    //listaClientes.add(aux);

                    //Inicio proceso Pago
                    
                    Label label1 = new Label("<h3> Seleccione una opción: </h3>", ContentMode.HTML);
                    Label label2 = new Label("<h1 style='text-weight:bold;margin:0'>UPOSports</h1>"
                            + "<br/><br/><h3>Importe a abonar: " + "<b>" + aux.getAbono().getPrecio() + " €</b></h3><br/>", ContentMode.HTML);
                    final VerticalLayout form = new VerticalLayout();
                    final HorizontalLayout layH = new HorizontalLayout();
                    //Añadimos el Label de bienvenida
                    //Creamos el boton "Crear empleado" donde implementaremos la funcion de crear un empleado
                    Button bEfectivo = new Button("Pago en efectivo");

                    bEfectivo.addClickListener(b -> {
                        form.removeAllComponents();
                        layH.removeAllComponents();
                        //Añadimos por cada atributo del empleado su textfield
                        final TextField tf = new TextField("Importe Abonado:");
                        final Label devolucion = new Label("",ContentMode.HTML);
                        tf.addTextChangeListener(new FieldEvents.TextChangeListener(){
                            public void textChange(TextChangeEvent event){
                                Double aDevolver = Double.parseDouble(event.getText()) - aux.getAbono().getPrecio() ;
                                devolucion.setValue("Importe a devolver: <b>" + aDevolver + " €</b>");
                                
                            }
                        });
                        tf.setTextChangeEventMode(TextChangeEventMode.LAZY); 
                        Button bFinalizar = new Button("Finalizar", FontAwesome.CHECK);
                        bFinalizar.addClickListener(a -> {
                        Pago pago = new Pago();
                        pago.setFecha(new Date());
                        pago.setCantidad(Double.parseDouble(tf.getValue()));
                        try {
                            PagoDAO.insertarPago(pago);
                            ClienteDAO.creaCliente(aux);
                            Notification.show("Cliente - DNI: " + aux.getDni(), "Registrado con éxito",
                                    Notification.Type.TRAY_NOTIFICATION);
                            Notification.show("Pago realizado con éxito",
                                    Notification.Type.HUMANIZED_MESSAGE);
                            init(request);
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(ClienteUI.class.getName()).log(Level.SEVERE, null, ex);
                        }


                    });
                        layH.addComponents(bFinalizar);
                        layH.setSpacing(true);
                        //layH.setMargin(true);
                        form.addComponents(label2, tf, devolucion, layH);
                        form.setMargin(true);
                        form.setSpacing(true);
                        setContent(form);
                        //getUI().getPage().setLocation("/Cliente");
                    });

                    Button bTarjeta = new Button("Pago con tarjeta");
                    bTarjeta.addClickListener(a -> {
                        Pago pago = new Pago();
                        pago.setFecha(new Date());
                        pago.setCantidad(aux.getAbono().getPrecio());
                        try {
                            PagoDAO.insertarPago(pago);
                            ClienteDAO.creaCliente(aux);
                            Notification.show("Cliente - DNI: " + aux.getDni(), "Registrado con éxito",
                                    Notification.Type.TRAY_NOTIFICATION);
                            Notification.show("Pago realizado con éxito",
                                    Notification.Type.HUMANIZED_MESSAGE);
                            init(request);
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(ClienteUI.class.getName()).log(Level.SEVERE, null, ex);
                        }


                    });
                    layH.addComponents(bEfectivo, bTarjeta);
                    layH.setSpacing(true);
                    //layH.setMargin(true);
                    form.addComponents(label2, label1, layH);
                    form.setMargin(true);
                    form.setSpacing(true);
                    setContent(form);
                    //Fin proceso pago

                } catch (UnknownHostException ex) {
                    Logger.getLogger(ClienteUI.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
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
        Label l = new Label("<h2>Modificar Cliente</h2>", ContentMode.HTML);
        layout.addComponent(l);
        TextField nombre = new TextField("Nombre:");
                nombre.setIcon(FontAwesome.USER);

        nombre.setValue(cliente.getNombre());
        TextField apellidos = new TextField("Apellidos:");
                apellidos.setIcon(FontAwesome.USER);

        apellidos.setValue(cliente.getApellidos());
        TextField dni = new TextField("DNI:");
                dni.setIcon(FontAwesome.KEY);

        dni.setValue(cliente.getDni());
        TextField telef = new TextField("Telefono:");
                telef.setIcon(FontAwesome.PHONE);

        telef.setValue(cliente.getTelefono());
        TextField cp = new TextField("Código Postal:");
                cp.setIcon(FontAwesome.MAP_MARKER);

        cp.setValue(cliente.getCodigoPostal());
        OptionGroup abono = new OptionGroup("Abono:");
        abono.setIcon(FontAwesome.TAG);
        ArrayList<Abono> listaAbonos = AbonoDAO.mostrarAbonos();
        for (int i = 0; i < listaAbonos.size(); i++) {
            System.out.println(listaAbonos.get(i).getTipo());

            abono.addItems(listaAbonos.get(i).getTipo() + " - " + listaAbonos.get(i).getPrecio() + " (€)");
            if(cliente.getAbono().getTipo().equals(listaAbonos.get(i).getTipo())){
            abono.select(listaAbonos.get(i).getTipo() + " - " + listaAbonos.get(i).getPrecio() + " (€)");
            }

        }
        datos.addComponents(nombre, apellidos, dni, telef, cp, abono);
        datos.setMargin(true);
        datos.setSpacing(true);
        Button enviar = new Button("Modificar", FontAwesome.EDIT);
        //EDITAMOS TODOS LOS CAMPOS QUE HAYA MODIFICADO EL USUARIO Y VOLVEMOS A INSERTAR EL CLIENTE EN LA LISTA
        enviar.addClickListener(e -> { //UNA VEZ PULSADO EL BOTÓN SE CREA EL CLIENTE Y LO AÑADIMOS A LA LISTA
            if (comprobarDatos(nombre.getValue(), apellidos.getValue(), dni.getValue(), telef.getValue(), cp.getValue(), (String) abono.getValue()) == false) {

            } else {
                int index = abono.getValue().toString().indexOf("-");
                String tipoAbono = abono.getValue().toString().substring(0, index - 1);
                Cliente aux = new Cliente();
                aux.setNombre(nombre.getValue());
                aux.setApellidos(apellidos.getValue());
                aux.setDni(dni.getValue());
                aux.setTelefono(telef.getValue());
                aux.setCodigoPostal(cp.getValue());
                try {
                    aux.setAbono(AbonoDAO.buscarAbono(tipoAbono));
                    // System.out.println("Abono:" + abono.getValue());
                    //listaClientes.add(aux);
                    if(aux.getAbono().getTipo().equals(cliente.getAbono().getTipo())){
                    ClienteDAO.actualizaCliente(aux,cliente);
                                    Notification.show("Cliente - DNI: " + aux.getDni(), "Modificado con éxito",
                        Notification.Type.TRAY_NOTIFICATION);
                                    init(request);
                    }else{
                        //Inicio proceso Pago
                    
                    Label label1 = new Label("<h3> Seleccione una opción: </h3>", ContentMode.HTML);
                    Label label2 = new Label("<h1 style='text-weight:bold;margin:0'>UPOSports</h1>"
                            + "<br/><br/><h3>Importe a abonar: " + "<b>" + aux.getAbono().getPrecio() + " €</b></h3><br/>", ContentMode.HTML);
                    final VerticalLayout form = new VerticalLayout();
                    final HorizontalLayout layH = new HorizontalLayout();
                    //Añadimos el Label de bienvenida
                    //Creamos el boton "Crear empleado" donde implementaremos la funcion de crear un empleado
                    Button bEfectivo = new Button("Pago en efectivo");

                    bEfectivo.addClickListener(b -> {
                        form.removeAllComponents();
                        layH.removeAllComponents();
                        //Añadimos por cada atributo del empleado su textfield
                        final TextField tf = new TextField("Importe Abonado:");
                        final Label devolucion = new Label("",ContentMode.HTML);
                        tf.addTextChangeListener(new FieldEvents.TextChangeListener(){
                            public void textChange(TextChangeEvent event){
                                Double aDevolver = Double.parseDouble(event.getText()) - aux.getAbono().getPrecio() ;
                                devolucion.setValue("Importe a devolver: <b>" + aDevolver + " €</b>");
                                
                            }
                        });
                        tf.setTextChangeEventMode(TextChangeEventMode.LAZY); 
                        Button bFinalizar = new Button("Finalizar", FontAwesome.CHECK);
                        bFinalizar.addClickListener(a -> {
                        Pago pago = new Pago();
                        pago.setFecha(new Date());
                        pago.setCantidad(Double.parseDouble(tf.getValue()));
                        try {
                            PagoDAO.insertarPago(pago);
                            ClienteDAO.actualizaCliente(aux,cliente);
                            Notification.show("Cliente - DNI: " + aux.getDni(), "Modificado con éxito",
                                    Notification.Type.TRAY_NOTIFICATION);
                            Notification.show("Pago realizado con éxito",
                                    Notification.Type.HUMANIZED_MESSAGE);
                            init(request);
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(ClienteUI.class.getName()).log(Level.SEVERE, null, ex);
                        }


                    });
                        layH.addComponents(bFinalizar);
                        layH.setSpacing(true);
                        //layH.setMargin(true);
                        form.addComponents(label2, tf, devolucion, layH);
                        form.setMargin(true);
                        form.setSpacing(true);
                        setContent(form);
                        //getUI().getPage().setLocation("/Cliente");
                    });

                    Button bTarjeta = new Button("Pago con tarjeta");
                    bTarjeta.addClickListener(a -> {
                        Pago pago = new Pago();
                        pago.setFecha(new Date());
                        pago.setCantidad(aux.getAbono().getPrecio());
                        try {
                            PagoDAO.insertarPago(pago);
                            ClienteDAO.actualizaCliente(aux,cliente);
                            Notification.show("Cliente - DNI: " + aux.getDni(), "Modificado con éxito",
                                    Notification.Type.TRAY_NOTIFICATION);
                            Notification.show("Pago realizado con éxito",
                                    Notification.Type.HUMANIZED_MESSAGE);
                            init(request);
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(ClienteUI.class.getName()).log(Level.SEVERE, null, ex);
                        }


                    });
                    layH.addComponents(bEfectivo, bTarjeta);
                    layH.setSpacing(true);
                    //layH.setMargin(true);
                    form.addComponents(label2, label1, layH);
                    form.setMargin(true);
                    form.setSpacing(true);
                    setContent(form);
                    //Fin proceso pago
                        ClienteDAO.actualizaCliente(aux,cliente);
                    }
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ClienteUI.class.getName()).log(Level.SEVERE, null, ex);
                }

                //init(request);
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

    //Método para comprobar los datos introducidos en los formularios
    protected boolean comprobarDatos(String nombre, String apellidos, String dni, String telef, String cp, String abono) {
        boolean b = false;//Variable booleana inicializada a false
        //Comprobamos si algún campo está vacío
        if (nombre != "" && apellidos != "" && dni != "" && telef != "" && cp != "") {
            if (abono != null) {
                //Comprobamos si la capacidad es numérica llamando al métdo isInteger
                if (isInteger(telef) == true && isInteger(cp) == true) {
                    if(telef.length() == 9 && cp.length() == 5){
                      b = true;//Si se satisface todas las condiciones, la variables es true

                    }else{
                        //Notificacion de tipo Warning interactiva para el usuario.
                    Notification.show("Error Formato datos", "Revise el código postal y el teléfono",
                            Notification.Type.WARNING_MESSAGE);
                    }
                } else {//Si la duración o el precio no es numérica
                    //Notificacion de tipo Warning interactiva para el usuario.
                    Notification.show("Error Datos Introducidos", "El teléfono y el código postal deben ser numéricos",
                            Notification.Type.WARNING_MESSAGE);
                }
            } else {
                Notification.show("Seleccione un abono", "El cliente debe tener una abono asociado",
                        Notification.Type.WARNING_MESSAGE);
            }

        } else {//En caso de campo vacío, mostramos 2 tipos de error uno fijo y otro interactivo (para el proyecto final debatiremos este aspecto)
            //Notificacion de tipo Warning interactiva para el usuario.
            Notification.show("Campo vacío", "Debe rellenar todos los campos",
                    Notification.Type.WARNING_MESSAGE);

        }
        return b;
    }

    //Método para comprobar valor numérico
    protected static boolean isInteger(String cadena) {
        try {//Intentamos parsear el la cadena a entero, si se satisface, devolvemos true
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {//De lo contrario, captura la excepción y devolvemos false
            return false;
        }
    }


    @WebServlet(urlPatterns = {"/Cliente/*"}, name = "gestionaClienteServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = ClienteUI.class, productionMode = false)
    public static class gestionaClienteServlet extends VaadinServlet {

    }

}
