package com.mycompany.uposports;

import bbdd.AbonoDAO;
import bbdd.AnuncianteDAO;
import clases.Abono;
import clases.Anunciante;
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
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Theme("mytheme")
@Title("Abono")
@PreserveOnRefresh  //Si volvemos a cargar la url, no perdemos la sesión

public class AnuncianteUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        final VerticalLayout layoutMostrarAnunciantes = new VerticalLayout();//Creamos un layout vertical
        final HorizontalLayout layoutH = new HorizontalLayout();//Creamos un layout horizontal
        final HorizontalLayout layoutHLabelabelTitulo = new HorizontalLayout();//Creamos un layout horizontal
        final HorizontalLayout layoutH2 = new HorizontalLayout();//Creamos un layout horizontal

        //RECUPERAMOS LA SESION Y SI NO HAY SESION NOS REDIRIGE A LA PÁGINA DE INICIO DE SESIÓN
        WrappedSession session = getSession().getSession();
        if (session.getAttribute("nombreUsuario") == null) {
            getUI().getPage().setLocation("/login");
        }

        Button crearAnunciante = new Button("Crear Anunciante", FontAwesome.PLUS_CIRCLE);//Botón para crear abono
        crearAnunciante.addClickListener(e -> {//Acción del botón
            crearAnunciante(vaadinRequest);//Accedemos al método crearAbono
        });

        Label l = new Label("<h1 style='text-weight:bold;text-align:center;margin:auto;    padding-right: 100px;'>UPOSports</h2>", ContentMode.HTML);
        Label labelEntidad = new Label("<h2 style='text-weight:bold;margin:0'>Anunciantes - </h2>", ContentMode.HTML);
        layoutHLabelabelTitulo.addComponent(l);

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

        if (layoutMostrarAnunciantes.getComponentIndex(layoutH) == -1) {//Si el layout horizontal que contiene los botones no se ha añadido, se añaden
            layoutH.addComponents(layoutHLabelabelTitulo, buttonReservas, buttonCliente, buttonAbonos, buttonInstalacion, buttonMateriales, buttonEmpleados, buttonAnunciantes, buttonLogout);//Añadimos los componentes al layout horizontal
            //Le metemos margen y espaciado, para mostrarlo posteriormente.
            layoutH2.setMargin(true);
            layoutH2.setSpacing(true);
            layoutH2.addComponents(labelEntidad, crearAnunciante);
            layoutMostrarAnunciantes.addComponents(layoutH, layoutH2);
        }

        Table table = new Table();//Creamos la tabla donde meteremos las instancias
        table.setSizeFull();
        Label label = new Label("<h2 style='margin-top:0'> Anunciantes Registrados </h2>", ContentMode.HTML);

        List<Anunciante> listaAnunciantes;
        try {
            listaAnunciantes = AnuncianteDAO.mostrarAnunciantes();
            if (listaAnunciantes.size() > 0) {//Si hay elementos en la lista de abonos
                layoutMostrarAnunciantes.addComponent(label);
                //Añadimos las columnas de la tabla
                table.addContainerProperty("Anunciante", String.class, "");
                table.addContainerProperty("Precio Contrato (€)", Double.class, "");
                table.addContainerProperty("Fecha Inicio", String.class, "");
                table.addContainerProperty("Fecha Fin", String.class, "");
                table.addContainerProperty("Modificar", Button.class, "");
                table.addContainerProperty("Eliminar", Button.class, "");
                for (int i = 0; i < listaAnunciantes.size(); i++) {//Mientras haya elementos por recorrer
                    Anunciante anunciante = listaAnunciantes.get(i);//Obtenemos el objeto de la lista

                    Button buttonModificar = new Button("Modificar", FontAwesome.EDIT);//Creamos el botón modificar
                    buttonModificar.addClickListener(e -> {//Acción del botón
                        editarAnunciante(vaadinRequest, anunciante);//Método para editar la instalación
                    });

                    Button buttonEliminar = new Button("Eliminar", FontAwesome.CLOSE);//Creamos el botón eliminar
                    buttonEliminar.addClickListener(e -> {
                        try {
                            AnuncianteDAO.eliminarAnunciante(anunciante); //Eliminamos el objeto de la lista de instalaciones
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(AnuncianteUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        init(vaadinRequest);//Volvemos a ejecutar el método principal
                        Notification.show("Anunciante - Nombre: " + anunciante.getAnunciante(), "Eliminado con éxito",
                                Notification.Type.TRAY_NOTIFICATION);
                    });
                    //Añadimos la fila a la tabla
                    table.addItem(new Object[]{anunciante.getAnunciante(), anunciante.getPrecioContrato(), dateFormat.format(anunciante.getFechaIni()), dateFormat.format(anunciante.getFechaFin()), buttonModificar, buttonEliminar}, i);
                    layoutMostrarAnunciantes.addComponent(table);//Lo añadimos al layout vertical
                }
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(AbonoUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Le añadimos margen y espciado, para mostrarlo posteriormente.
        layoutMostrarAnunciantes.setMargin(true);
        layoutMostrarAnunciantes.setSpacing(true);
        setContent(layoutMostrarAnunciantes);

    }

    protected void crearAnunciante(VaadinRequest vaadinRequest) {//Método para crear abonos
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout layoutTextField = new HorizontalLayout();
        final HorizontalLayout layoutBotones = new HorizontalLayout();//Creamos un vertical layout
        final TextField anunciante = new TextField();
        Label l = new Label("<h2>Crear Anunciante</h2>", ContentMode.HTML);
        layout.addComponent(l);
        anunciante.setCaption("Nombre anunciante:");
        anunciante.setIcon(FontAwesome.TAG);//Icono
        final TextField precioContrato = new TextField();
        precioContrato.setCaption("Precio del contrato (€):");
        precioContrato.setIcon(FontAwesome.MONEY);
        DateField fechaIni = new DateField("Fecha inicio del contrato:");
        fechaIni.setIcon(FontAwesome.HOURGLASS_START);
        DateField fechaFin = new DateField("Fecha fin del contrato:");
        fechaFin.setIcon(FontAwesome.HOURGLASS_END);
        Button buttonRegistrar = new Button("Registrar", FontAwesome.CHECK);

        buttonRegistrar.addClickListener(e -> {
            vaadinRequest.setAttribute("anunciante", anunciante.getValue());
            vaadinRequest.setAttribute("precioContrato", precioContrato.getValue());
            vaadinRequest.setAttribute("fechaIni", fechaIni.getValue());
            vaadinRequest.setAttribute("fechaFin", fechaFin.getValue());
            if (comprobarDatos(vaadinRequest) == true) {
                try {
                    registrarAnunciante(vaadinRequest);//Se lanza el método modificar abono
                } catch (UnknownHostException | ParseException ex) {
                    Logger.getLogger(AnuncianteUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                init(vaadinRequest);
                //Notificacion de tipo bandeja para notificar la correcta operación.
                Notification.show("Anunciante - Nombre: " + anunciante.getValue(), "Creado con éxito",
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

        layoutTextField.addComponents(anunciante, precioContrato, fechaIni, fechaFin);
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
    protected void editarAnunciante(VaadinRequest vaadinRequest, Anunciante anun) {
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout layoutTextField = new HorizontalLayout();
        final HorizontalLayout layoutBotones = new HorizontalLayout();//Creamos un vertical layout
        final TextField anunciante = new TextField();
        Label l = new Label("<h2>Modificar Anunciante</h2>", ContentMode.HTML);
        layout.addComponent(l);
        anunciante.setCaption("Nombre anunciante:");
        anunciante.setValue(anun.getAnunciante());//Insertamos en el campo el valor del atributo tipo
        anunciante.setIcon(FontAwesome.TAG);//Icono
        final TextField precioContrato = new TextField();
        precioContrato.setCaption("Precio del contrato (€):");
        precioContrato.setValue(anun.getPrecioContrato().toString());//Insertamos en el campo el valor del atributo duración
        precioContrato.setIcon(FontAwesome.CALENDAR);
        DateField fechaIni = new DateField("Fecha inicio del contrato:");
        fechaIni.setValue(anun.getFechaIni());//Insertamos en el campo el valor del atributo coste
        fechaIni.setIcon(FontAwesome.HOURGLASS_START);
        DateField fechaFin = new DateField("Fecha fin del contrato:");
        fechaIni.setIcon(FontAwesome.HOURGLASS_END);
        fechaFin.setValue(anun.getFechaFin());//Insertamos en el campo el valor del atributo coste;
        Button buttonRegistrar = new Button("Modificar", FontAwesome.EDIT);

        buttonRegistrar.addClickListener(e -> {
            vaadinRequest.setAttribute("anunciante", anunciante.getValue());
            vaadinRequest.setAttribute("precioContrato", precioContrato.getValue());
            vaadinRequest.setAttribute("fechaIni", fechaIni.getValue());
            vaadinRequest.setAttribute("fechaFin", fechaFin.getValue());
            if (comprobarDatos(vaadinRequest) == true) {
                try {
                    modificarAnunciante(vaadinRequest, anun);//Se lanza el método modificar abono
                } catch (UnknownHostException ex) {
                    Logger.getLogger(AnuncianteUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                init(vaadinRequest);
                //Notificacion de tipo bandeja para notificar la correcta operación.
                Notification.show("Anunciante - Nombre: " + anunciante.getValue(), "Modificado con éxito",
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

        layoutTextField.addComponents(anunciante, precioContrato, fechaIni, fechaFin);
        layoutTextField.setSpacing(true);
        layoutTextField.setMargin(true);
        layout.addComponents(layoutTextField, layoutBotones);//Añadimos los componentes al layout
        //Le añadimos margen y espciado, para mostrarlo posteriormente
        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);
    }

    protected void modificarAnunciante(VaadinRequest vaadinRequest, Anunciante anunciante) throws UnknownHostException {//Método para guardar los datos modificados en memoria, no hay persistencia de momento
        Anunciante aux = new Anunciante();
        aux.setAnunciante((String) vaadinRequest.getAttribute("anunciante"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo tipo del objeto abono
        aux.setPrecioContrato(Double.parseDouble((String) vaadinRequest.getAttribute("precioContrato")));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo duración del objeto abono
        aux.setFechaIni((Date) vaadinRequest.getAttribute("fechaIni"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo coste del objeto abono
        aux.setFechaFin((Date) vaadinRequest.getAttribute("fechaFin"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo coste del objeto abono
        AnuncianteDAO.actualizarAnunciante(aux, anunciante);
    }

    protected void registrarAnunciante(VaadinRequest vaadinRequest) throws UnknownHostException, ParseException {//Método para registrar los datos en memoria, no hay persistencia de momento
        Anunciante aux = new Anunciante();//Creamos un nuevo objeto abono
        aux.setAnunciante((String) vaadinRequest.getAttribute("anunciante"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo tipo del objeto abono
        aux.setPrecioContrato(Double.parseDouble((String) vaadinRequest.getAttribute("precioContrato")));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo duración del objeto abono
        aux.setFechaIni((Date) vaadinRequest.getAttribute("fechaIni"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo coste del objeto abono
        aux.setFechaFin((Date) vaadinRequest.getAttribute("fechaFin"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo coste del objeto abono
        AnuncianteDAO.insertarAnunciante(aux);

    }

    //Método para comprobar los datos introducidos en los formularios
    protected boolean comprobarDatos(VaadinRequest vaadinRequest) {
        boolean b = false;//Variable booleana inicializada a false
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            //Comprobamos si algún campo está vacío
            if ((String) vaadinRequest.getAttribute("anunciante") != "" && (String) vaadinRequest.getAttribute("precioContrato") != "") {
                //Comprobamos si la capacidad es numérica llamando al métdo isInteger
                if (isDouble((String) vaadinRequest.getAttribute("precioContrato")) == true) {
                    if (dateFormat.format((Date) vaadinRequest.getAttribute("fechaIni")).compareTo(dateFormat.format((Date) vaadinRequest.getAttribute("fechaFin"))) < 0) {
                        b = true;//Si se satisface todas las condiciones, la variables es true
                    } else {
                        Notification.show("Error Fechas", "La fecha de inicio debe ser inferior a la fecha de finalización",
                                Notification.Type.WARNING_MESSAGE);
                    }
                } else {//Si la duración o el coste no es numérica
                    //Notificacion de tipo Warning interactiva para el usuario.
                    Notification.show("Error Datos Introducidos", "El precio del contrato debe ser numéricos",
                            Notification.Type.WARNING_MESSAGE);
                }
            } else {//En caso de campo vacío, mostramos 2 tipos de error uno fijo y otro interactivo (para el proyecto final debatiremos este aspecto)
                //Notificacion de tipo Warning interactiva para el usuario.
                Notification.show("Campo vacío", "Debe rellenar todos los campos",
                        Notification.Type.WARNING_MESSAGE);
            }
        } catch (NullPointerException e) {

            Notification.show("Error Fechas", "Introduzca la fecha y hora haciendo click en el calendario",
                    Notification.Type.WARNING_MESSAGE);
        }
        return b;
    }

    protected static boolean isDouble(String cadena) {
        try {//Intentamos parsear el la cadena a float, si se satisface, devolvemos true
            Double.parseDouble(cadena);
            return true;
        } catch (NumberFormatException nfe) {//De lo contrario, captura la excepción y devolvemos false
            return false;
        }
    }

    @WebServlet(urlPatterns = "/Anunciante/*", name = "Anunciante", asyncSupported = true)
    @VaadinServletConfiguration(ui = AnuncianteUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

}
