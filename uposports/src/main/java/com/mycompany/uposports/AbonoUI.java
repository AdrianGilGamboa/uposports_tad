package com.mycompany.uposports;

import bbdd.AbonosDAO;
import clases.Abono;
import com.vaadin.annotations.PreserveOnRefresh;
import javax.servlet.annotation.WebServlet;

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
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Theme("mytheme")
@Title("Abono")
@PreserveOnRefresh  //Si volvemos a cargar la url, no perdemos la sesión

public class AbonoUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layoutMostrarAbonos = new VerticalLayout();//Creamos un layout vertical
        final HorizontalLayout layoutH = new HorizontalLayout();//Creamos un layout horizontal
        final HorizontalLayout layoutHLabelabelTitulo = new HorizontalLayout();//Creamos un layout horizontal
        final HorizontalLayout layoutH2 = new HorizontalLayout();//Creamos un layout horizontal

        //RECUPERAMOS LA SESION Y SI NO HAY SESION NOS REDIRIGE A LA PÁGINA DE INICIO DE SESIÓN
        WrappedSession session = getSession().getSession();
        if (session.getAttribute("nombreUsuario") == null) {
            getUI().getPage().setLocation("/login");
        }

        Button crearAbono = new Button("Crear Abono", FontAwesome.PLUS_CIRCLE);//Botón para crear abono
        crearAbono.addClickListener(e -> {//Acción del botón
            crearAbono(vaadinRequest);//Accedemos al método crearAbono
        });

        Label l = new Label("<h1 style='text-weight:bold;text-align:center;margin:auto;    padding-right: 100px;'>UPOSports</h2>", ContentMode.HTML);
        Label labelEntidad = new Label("<h2 style='text-weight:bold;margin:0'>Abonos - </h2>", ContentMode.HTML);
        layoutHLabelabelTitulo.addComponent(l);

        Button buttonAbonos = new Button("Abonos", FontAwesome.MONEY);//Botón para acceder a la entidad abono
        buttonAbonos.addClickListener(e -> {//Acción del botón
            getUI().getPage().setLocation("/Abono");//Accedemos a la entidad abono
        });

        Button buttonCliente = new Button("Clientes", FontAwesome.USERS);//Botón para acceder a la entidad instalaciones
        buttonCliente.addClickListener(e -> {//Acción del botón
            getUI().getPage().setLocation("/Cliente");//Accedemos a la entidad abono
        });

        Button buttonInstalacion = new Button("Instalaciones", FontAwesome.BUILDING);//Botón para acceder a la entidad instalaciones
        buttonInstalacion.addClickListener(e -> {//Acción del botón
            getUI().getPage().setLocation("/Instalacion");//Accedemos a la entidad abono
        });

        Button buttonEmpleados = new Button("Empleados", FontAwesome.GROUP);//Botón para acceder a la entidad instalaciones
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

        if (layoutMostrarAbonos.getComponentIndex(layoutH) == -1) {//Si el layout horizontal que contiene los botones no se ha añadido, se añaden
            layoutH.addComponents(layoutHLabelabelTitulo, buttonInstalacion, buttonCliente, buttonAbonos, buttonEmpleados, buttonMateriales, buttonReservas, buttonLogout);//Añadimos los componentes al layout horizontal
            //Le metemos margen y espaciado, para mostrarlo posteriormente.
            layoutH2.setMargin(true);
            layoutH2.setSpacing(true);
            layoutH2.addComponents(labelEntidad, crearAbono);
            layoutMostrarAbonos.addComponents(layoutH, layoutH2);
        }

        Table table = new Table();//Creamos la tabla donde meteremos las instancias
        table.setSizeFull();
        Label label = new Label("<h2 style='margin-top:0'> Abonos Registrados </h2>", ContentMode.HTML);

        List<Abono> listaAbonos;
        try {
            listaAbonos = AbonosDAO.mostrarAbonos();
            if (listaAbonos.size() > 0) {//Si hay elementos en la lista de abonos
                layoutMostrarAbonos.addComponent(label);
                //Añadimos las columnas de la tabla
                table.addContainerProperty("Tipo", String.class, "");
                table.addContainerProperty("Duración(meses)", Integer.class, "");
                table.addContainerProperty("Coste(€)", Double.class, "");
                table.addContainerProperty("Editar", Button.class, "");
                table.addContainerProperty("Eliminar", Button.class, "");
                for (int i = 0; i < listaAbonos.size(); i++) {//Mientras haya elementos por recorrer
                    Abono abono = listaAbonos.get(i);//Obtenemos el objeto de la lista

                    Button buttonModificar = new Button("Modificar", FontAwesome.EDIT);//Creamos el botón modificar
                    buttonModificar.addClickListener(e -> {//Acción del botón
                        editarAbono(vaadinRequest, abono);//Método para editar la instalación
                    });

                    Button buttonEliminar = new Button("Eliminar", FontAwesome.CLOSE);//Creamos el botón eliminar
                    buttonEliminar.addClickListener(e -> {
                        try {
                            //Acción del botón
                            //listaAbonos.remove(abono);
                            if(!AbonosDAO.eliminarAbono(abono)) //Eliminamos el objeto de la lista de instalaciones
                            Notification.show("Abono Asociado", "Debe eliminar todas las asocianciones de este abono con Clientes",
                                    Notification.Type.ERROR_MESSAGE);
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(AbonoUI.class.getName()).log(Level.SEVERE, null, ex);

                        }
                        init(vaadinRequest);//Volvemos a ejecutar el método principal
                        Notification.show("Abono - Tipo: " + abono.getTipo(), "Eliminado con éxito",
                                Notification.Type.TRAY_NOTIFICATION);
                    });
                    //Añadimos la fila a la tabla
                    table.addItem(new Object[]{abono.getTipo(), abono.getDuracion(), abono.getCoste(), buttonModificar, buttonEliminar}, i);
                    layoutMostrarAbonos.addComponent(table);//Lo añadimos al layout vertical
                }
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(AbonoUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Le añadimos margen y espciado, para mostrarlo posteriormente.
        layoutMostrarAbonos.setMargin(true);
        layoutMostrarAbonos.setSpacing(true);
        setContent(layoutMostrarAbonos);

    }

    protected void crearAbono(VaadinRequest vaadinRequest) {//Método para crear abonos
        final VerticalLayout layout = new VerticalLayout();//Creamos un vertical layout
        final HorizontalLayout layoutTextField = new HorizontalLayout();
        final HorizontalLayout layoutBotones = new HorizontalLayout();//Creamos un vertical layout
        Label l = new Label("<h2>Nuevo Abono</h2>", ContentMode.HTML);
        layout.addComponent(l);
        final TextField tipo = new TextField();//Campo para insertar el tipo
        tipo.setCaption("Tipo:");//Texto que se muestra en dicho campo
        tipo.setIcon(FontAwesome.TAG);//Icono
        final TextField duracion = new TextField();//Campo para insertar la duracion
        duracion.setCaption("Duración (meses):");//Texto que se muestra en dicho campo
        duracion.setIcon(FontAwesome.CALENDAR);
        final TextField coste = new TextField();//Campo para insertar el coste
        coste.setCaption("Coste (€):");//Texto que se muestra en dicho campo
        coste.setIcon(FontAwesome.EURO);
        Button buttonRegistrar = new Button("Registrar", FontAwesome.CHECK);//Creamo el botón para registrar 
        buttonRegistrar.addClickListener(e -> {//Acción del botón
            vaadinRequest.setAttribute("tipo", tipo.getValue());//Añadimos en la petición el valor del campo tipo
            vaadinRequest.setAttribute("duracion", duracion.getValue());//Añadimos en la petición el valor del campo duración
            vaadinRequest.setAttribute("coste", coste.getValue());//Añadimos en la petición el valor del campo coste
            if (comprobarDatos(vaadinRequest, layout) == true) {
                try {
                    //Se comprueban los datos, y si son correctos...
                    registrarAbono(vaadinRequest);//Se envían los datos a registro de abono
                } catch (UnknownHostException ex) {
                    Logger.getLogger(AbonoUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                init(vaadinRequest);//Se lanza el método principal
                //Notificacion de tipo bandeja para notificar la correcta operación.
                Notification.show("Abono - Tipo: " + tipo.getValue(), "Registrado con éxito",
                        Notification.Type.TRAY_NOTIFICATION);
            }

        });
        Button buttonCancelar = new Button("Cancelar", FontAwesome.CLOSE);//Nuevo botón para cancelar
        buttonCancelar.addClickListener(e -> {//Acción del botón
            init(vaadinRequest);//Se lanza el método principal
        });
        layoutBotones.addComponents(buttonCancelar, buttonRegistrar);
        layoutBotones.setSpacing(true);
        layoutBotones.setMargin(true);

        layoutTextField.addComponents(tipo, duracion, coste);
        layoutTextField.setSpacing(true);
        layoutTextField.setMargin(true);
        layout.addComponents(layoutTextField, layoutBotones);//Añadimos los componentes al layout
        //Le añadimos margen y espciado, para mostrarlo posteriormente
        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);

    }

    //Exactamente igual que el método de crear abono, con la peculiaridad de que a este se le pasa el objeto abono y se prerellenan los campos con sus valores actuales.
    //Ya se ha comentado en el método anterior que realiza cada línea de código.
    protected void editarAbono(VaadinRequest vaadinRequest, Abono abono) {
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout layoutTextField = new HorizontalLayout();
        final HorizontalLayout layoutBotones = new HorizontalLayout();//Creamos un vertical layout
        final TextField tipo = new TextField();
        Label l = new Label("<h2>Modificar Abono</h2>", ContentMode.HTML);
        layout.addComponent(l);
        tipo.setCaption("Tipo:");
        tipo.setValue(abono.getTipo());//Insertamos en el campo el valor del atributo tipo
        tipo.setIcon(FontAwesome.TAG);//Icono
        final TextField duracion = new TextField();
        duracion.setCaption("Duración (meses):");
        duracion.setValue(abono.getDuracion().toString());//Insertamos en el campo el valor del atributo duración
        duracion.setIcon(FontAwesome.CALENDAR);
        final TextField coste = new TextField();
        coste.setCaption("Coste:");
        coste.setValue(abono.getCoste().toString());//Insertamos en el campo el valor del atributo coste
        coste.setIcon(FontAwesome.DOLLAR);
        Button buttonRegistrar = new Button("Modificar", FontAwesome.EDIT);

        buttonRegistrar.addClickListener(e -> {
            vaadinRequest.setAttribute("tipo", tipo.getValue());
            vaadinRequest.setAttribute("duracion", duracion.getValue());
            vaadinRequest.setAttribute("coste", coste.getValue());
            if (comprobarDatos(vaadinRequest, layout) == true) {
                try {
                    modificarAbono(vaadinRequest, abono);//Se lanza el método modificar abono
                } catch (UnknownHostException ex) {
                    Logger.getLogger(AbonoUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                init(vaadinRequest);
                //Notificacion de tipo bandeja para notificar la correcta operación.
                Notification.show("Abono - Tipo: " + tipo.getValue(), "Modificado con éxito",
                        Notification.Type.TRAY_NOTIFICATION);
            }

        });
        Button buttonCancelar = new Button("Cancelar", FontAwesome.CLOSE);
        buttonCancelar.addClickListener(e -> {
            init(vaadinRequest);
        });

        layoutBotones.addComponents(buttonCancelar, buttonRegistrar);
        layoutBotones.setSpacing(true);
        layoutBotones.setMargin(true);

        layoutTextField.addComponents(tipo, duracion, coste);
        layoutTextField.setSpacing(true);
        layoutTextField.setMargin(true);
        layout.addComponents(layoutTextField, layoutBotones);//Añadimos los componentes al layout
        //Le añadimos margen y espciado, para mostrarlo posteriormente
        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);
    }

    protected void modificarAbono(VaadinRequest vaadinRequest, Abono abono) throws UnknownHostException {//Método para guardar los datos modificados en memoria, no hay persistencia de momento
        Abono aux = new Abono();
        aux.setTipo((String) vaadinRequest.getAttribute("tipo"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo tipo del objeto abono
        aux.setDuracion(Integer.parseInt((String) vaadinRequest.getAttribute("duracion")));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo duración del objeto abono
        aux.setCoste(Double.parseDouble((String) vaadinRequest.getAttribute("coste")));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo coste del objeto abono
        AbonosDAO.actualizarAbono(aux, abono);
    }

    protected void registrarAbono(VaadinRequest vaadinRequest) throws UnknownHostException {//Método para registrar los datos en memoria, no hay persistencia de momento
        Abono abono = new Abono();//Creamos un nuevo objeto abono
        abono.setTipo((String) vaadinRequest.getAttribute("tipo"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo tipo del objeto abono
        abono.setDuracion(Integer.parseInt((String) vaadinRequest.getAttribute("duracion")));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo duración del objeto abono
        abono.setCoste(Double.parseDouble((String) vaadinRequest.getAttribute("coste")));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo coste del objeto abono
        //listaAbonos.add(abono);//Añadimos el objeto a la lista de abonos
        AbonosDAO.insertarAbono(abono);

    }

    //Método para comprobar los datos introducidos en los formularios
    protected boolean comprobarDatos(VaadinRequest vaadinRequest, VerticalLayout layout) {
        boolean b = false;//Variable booleana inicializada a false
        //Comprobamos si algún campo está vacío
        if ((String) vaadinRequest.getAttribute("tipo") != "" && (String) vaadinRequest.getAttribute("duracion") != "" && (String) vaadinRequest.getAttribute("coste") != "") {
            //Comprobamos si la capacidad es numérica llamando al métdo isInteger
            if (isInteger((String) vaadinRequest.getAttribute("duracion")) == true && isFloat((String) vaadinRequest.getAttribute("coste")) == true) {
                b = true;//Si se satisface todas las condiciones, la variables es true
            } else {//Si la duración o el coste no es numérica
                //Notificacion de tipo Warning interactiva para el usuario.
                Notification.show("Error Datos Introducidos", "La duración y el coste deben ser numéricos",
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

    protected static boolean isFloat(String cadena) {
        try {//Intentamos parsear el la cadena a float, si se satisface, devolvemos true
            Float.parseFloat(cadena);
            return true;
        } catch (NumberFormatException nfe) {//De lo contrario, captura la excepción y devolvemos false
            return false;
        }
    }

    @WebServlet(urlPatterns = "/Abono/*", name = "Abono", asyncSupported = true)
    @VaadinServletConfiguration(ui = AbonoUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

}
