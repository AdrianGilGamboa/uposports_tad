package com.mycompany.uposports;

import bbdd.ClienteDAO;
import bbdd.InstalacionDAO;
import bbdd.ReservaDAO;
import clases.Instalacion;
import clases.Reserva;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.servlet.annotation.WebServlet;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Theme("mytheme")
@Title("Reserva")
public class ReservaUI extends UI {

    public static ArrayList<Reserva> listaReservas = new ArrayList(); //LISTA DONDE ESTARÁN ALMACENADOS TODAS LAS RESERVAS QUE CREEMOS
    public VerticalLayout layout = new VerticalLayout(); //LAYOUT PRINCIPAL
    final HorizontalLayout layoutHLabelabelTitulo = new HorizontalLayout();//Creamos un layout horizontal
    final HorizontalLayout layoutH2 = new HorizontalLayout();//Creamos un layout horizontal
    final HorizontalLayout layoutH = new HorizontalLayout();//Creamos un layout horizontal
    final HorizontalLayout layoutH3 = new HorizontalLayout();//Creamos un layout horizontal
    Label label = new Label("<h2 style='margin-top:0'> Reservas Registradas </h2>", ContentMode.HTML);

    @Override
    protected void init(VaadinRequest request) {
        layoutH3.removeAllComponents();
        layout.removeAllComponents();
        layoutHLabelabelTitulo.removeAllComponents();
        layoutH2.removeAllComponents();
        layoutH.removeAllComponents();
        //RECUPERAMOS LA SESION Y SI NO HAY SESION NOS REDIRIGE A LA PÁGINA DE INICIO DE SESIÓN
        WrappedSession session = getSession().getSession();
        if (session.getAttribute("nombreUsuario") == null) {
            getUI().getPage().setLocation("/inicioSesion");
        }

        Label l = new Label("<h1 style='text-weight:bold;margin:auto;    padding-right: 100px;'>UPOSports</h2>", ContentMode.HTML);
        Label labelEntidad = new Label("<h2 style='text-weight:bold;margin:0'>Reservas - </h2>", ContentMode.HTML);

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

        Button buttonAnunciantes = new Button("Anunciantes", FontAwesome.BELL);//Botón para acceder a la entidad instalaciones
        buttonAnunciantes.addClickListener(e -> {//Acción del botón
            getUI().getPage().setLocation("/Anunciante");//Accedemos a la entidad abono
        });

        Button botonAdd = new Button("Crear Reserva", FontAwesome.PLUS_CIRCLE); //BOTÓN PARA AÑADIR CLIENTES
        botonAdd.addClickListener(e -> {
            try {
                creaReserva(request);
            } catch (UnknownHostException ex) {
                Logger.getLogger(ReservaUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        //CREAMOS UNA TABLA DONDE APARECERÁ LA LISTA DE CLIENTES
        Table tabla = new Table();
        tabla.setSizeFull();
        tabla.addContainerProperty("Fecha Reserva", String.class, null);
        tabla.addContainerProperty("Hora Inicio", String.class, null);
        tabla.addContainerProperty("Hora Fin", String.class, null);
        tabla.addContainerProperty("Nombre Cliente", String.class, null);
        tabla.addContainerProperty("Apellidos Cliente", String.class, null);
        tabla.addContainerProperty("Instalacion", String.class, null);
        tabla.addContainerProperty("Modificar", Button.class, null);
        tabla.addContainerProperty("Eliminar", Button.class, null);

        Label labelFiltros = new Label("<h2 style='text-weight:bold;margin:0 0 0 100px;'>Filtros:  </h2>", ContentMode.HTML);

        Button buttonReservasHoy = new Button("Reservas para hoy");//Botón para acceder a la entidad abono
        buttonReservasHoy.addClickListener(e -> {//Acción del botón
            mostrarReservasHoy(tabla, request);
        });

        Button buttonReservasFuturas = new Button("Reservas pendientes");//Botón para acceder a la entidad abono
        buttonReservasFuturas.addClickListener(e -> {//Acción del botón
            mostrarReservasFuturas(tabla, request);
        });

        Button buttonReservasEnCurso = new Button("Reservas en curso");//Botón para acceder a la entidad abono
        buttonReservasEnCurso.addClickListener(e -> {//Acción del botón
            mostrarReservasEnCurso(tabla, request);
        });

        Button buttonTodasReservas = new Button("Borrar Filtros");//Botón para acceder a la entidad abono
        buttonTodasReservas.addClickListener(e -> {//Acción del botón
            mostrarReservas(tabla, request);
        });

        layoutHLabelabelTitulo.addComponent(l);
        layoutH.addComponents(layoutHLabelabelTitulo, buttonReservas, buttonCliente, buttonAbonos, buttonInstalacion, buttonMateriales, buttonEmpleados, buttonAnunciantes, buttonLogout);//Añadimos los componentes al layout horizontal
        layoutH2.setMargin(true);
        layoutH2.setSpacing(true);
        layoutH2.addComponents(labelEntidad, botonAdd);
        layoutH3.addComponents(label, labelFiltros, buttonReservasHoy, buttonReservasFuturas, buttonReservasEnCurso, buttonTodasReservas);
        layoutH3.setSpacing(true);
        layout.addComponents(layoutH, layoutH2, layoutH3);//Añadimos los componentes al layout horizontal

        mostrarReservas(tabla, request);

    }

    public void mostrarReservas(Table tabla, VaadinRequest request) {
        try {
            layoutH3.removeComponent(label);
            label = new Label("<h2 style='margin-top:0'> Reservas Registradas </h2>", ContentMode.HTML);
            layoutH3.addComponentAsFirst(label);
            Iterator it;
            it = ReservaDAO.consultaReservas().iterator();

            int i = 0;
            //BUCLE PARA AÑADIR TODAS LAS RESERVA A LA TABLA
            if (!ReservaDAO.consultaReservas().isEmpty()) {
                tabla.removeAllItems();
                while (it.hasNext()) {
                    Button eliminar = new Button("Eliminar", FontAwesome.CLOSE);
                    Button editar = new Button("Modificar", FontAwesome.EDIT);
                    Reserva aux = (Reserva) it.next();
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    DateFormat horaFormat = new SimpleDateFormat("HH:mm");
                    tabla.addItem(new Object[]{dateFormat.format(aux.getInicioReserva()), horaFormat.format(aux.getInicioReserva()),
                        horaFormat.format(aux.getFinReserva()), aux.getCliente().getNombre(), aux.getCliente().getApellidos(), aux.getInstalacion().getNombre(), editar, eliminar}, i);
                    i++;
                    eliminar.addClickListener(e -> {
                        try {
                            //AÑADIMOS EL BOTON DE ELIMINAR POR CADA Reserva
                            ReservaDAO.eliminaReserva(aux);//Elimina la reserva de la bbdd
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(ReservaUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        init(request);
                    });
                    
                    editar.addClickListener(e -> { try {
                        //AÑADIMOS EL BOTON DE EDITAR POR CADA CLIENTE
                        editarReserva(aux, request); //EJECUTA LA FUNCION EDITAR CLIENTE
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(ReservaUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
                layout.addComponent(tabla);
            }
            layout.setMargin(true);
            layout.setSpacing(true);
            setContent(layout);

        } catch (UnknownHostException ex) {
            Logger.getLogger(ReservaUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarReservasHoy(Table tabla, VaadinRequest request) {
        try {
            layoutH3.removeComponent(label);
            label = new Label("<h2 style='margin-top:0'> Reservas Registradas - Hoy </h2>", ContentMode.HTML);
            layoutH3.addComponentAsFirst(label);
            Iterator it;
            it = ReservaDAO.consultaReservas().iterator();

            int i = 0;
            //BUCLE PARA AÑADIR TODAS LAS RESERVA A LA TABLA
            if (!ReservaDAO.consultaReservas().isEmpty()) {
                tabla.removeAllItems();
                while (it.hasNext()) {
                    Button delete = new Button("Eliminar", FontAwesome.CLOSE);
                    Button update = new Button("Modificar", FontAwesome.EDIT);
                    Reserva aux = (Reserva) it.next();
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    DateFormat horaFormat = new SimpleDateFormat("HH:mm");
                    Date date = new Date();
                    if (dateFormat.format(aux.getInicioReserva()).equals(dateFormat.format(date))) {
                        tabla.addItem(new Object[]{dateFormat.format(aux.getInicioReserva()), horaFormat.format(aux.getInicioReserva()),
                            horaFormat.format(aux.getFinReserva()), aux.getCliente().getNombre(), aux.getCliente().getApellidos(), aux.getInstalacion().getNombre(), update, delete}, i);
                        i++;
                        delete.addClickListener(a -> {
                            try {
                                //AÑADIMOS EL BOTON DE ELIMINAR POR CADA Reserva
                                ReservaDAO.eliminaReserva(aux);//Elimina la reserva de la bbdd
                            } catch (UnknownHostException ex) {
                                Logger.getLogger(ReservaUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            init(request);
                        });

                        update.addClickListener(a -> { try {
                            //AÑADIMOS EL BOTON DE EDITAR POR CADA CLIENTE
                            editarReserva(aux, request); //EJECUTA LA FUNCION EDITAR CLIENTE
                            } catch (UnknownHostException ex) {
                                Logger.getLogger(ReservaUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    }
                }
                layout.addComponent(tabla);
            }
            layout.setMargin(true);
            layout.setSpacing(true);
            setContent(layout);

        } catch (UnknownHostException ex) {
            Logger.getLogger(ReservaUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarReservasFuturas(Table tabla, VaadinRequest request) {
        layoutH3.removeComponent(label);
        label = new Label("<h2 style='margin-top:0'> Reservas Registradas - Próximas </h2>", ContentMode.HTML);
        layoutH3.addComponentAsFirst(label);
        try {
            Iterator it;
            it = ReservaDAO.consultaReservas().iterator();

            int i = 0;
            //BUCLE PARA AÑADIR TODAS LAS RESERVA A LA TABLA
            if (!ReservaDAO.consultaReservas().isEmpty()) {
                tabla.removeAllItems();
                while (it.hasNext()) {
                    Button delete = new Button("Eliminar", FontAwesome.CLOSE);
                    Button update = new Button("Modificar", FontAwesome.EDIT);
                    Reserva aux = (Reserva) it.next();
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    DateFormat horaFormat = new SimpleDateFormat("HH:mm");
                    DateFormat dateHoraFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Date date = new Date();
                    if (dateHoraFormat.format(aux.getInicioReserva()).compareTo(dateHoraFormat.format(date)) > 0) {
                        tabla.addItem(new Object[]{dateFormat.format(aux.getInicioReserva()), horaFormat.format(aux.getInicioReserva()),
                            horaFormat.format(aux.getFinReserva()), aux.getCliente().getNombre(), aux.getCliente().getApellidos(), aux.getInstalacion().getNombre(), update, delete}, i);
                        i++;
                        delete.addClickListener(a -> {
                            try {
                                //AÑADIMOS EL BOTON DE ELIMINAR POR CADA Reserva
                                ReservaDAO.eliminaReserva(aux);//Elimina la reserva de la bbdd
                            } catch (UnknownHostException ex) {
                                Logger.getLogger(ReservaUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            init(request);
                        });

                        update.addClickListener(a -> { try {
                            //AÑADIMOS EL BOTON DE EDITAR POR CADA CLIENTE
                            editarReserva(aux, request); //EJECUTA LA FUNCION EDITAR CLIENTE
                            } catch (UnknownHostException ex) {
                                Logger.getLogger(ReservaUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    }
                }
                layout.addComponent(tabla);
            }
            layout.setMargin(true);
            layout.setSpacing(true);
            setContent(layout);

        } catch (UnknownHostException ex) {
            Logger.getLogger(ReservaUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarReservasEnCurso(Table tabla, VaadinRequest request) {
        layoutH3.removeComponent(label);
        label = new Label("<h2 style='margin-top:0'> Reservas Registradas - En curso </h2>", ContentMode.HTML);
        layoutH3.addComponentAsFirst(label);
        try {
            Iterator it;
            it = ReservaDAO.consultaReservas().iterator();

            int i = 0;
            //BUCLE PARA AÑADIR TODAS LAS RESERVA A LA TABLA
            if (!ReservaDAO.consultaReservas().isEmpty()) {
                tabla.removeAllItems();
                while (it.hasNext()) {
                    Button delete = new Button("Eliminar", FontAwesome.CLOSE);
                    Button update = new Button("Modificar", FontAwesome.EDIT);
                    Reserva aux = (Reserva) it.next();
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    DateFormat horaFormat = new SimpleDateFormat("HH:mm");
                    DateFormat dateHoraFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Date date = new Date();
                    if (dateHoraFormat.format(aux.getInicioReserva()).compareTo(dateHoraFormat.format(date)) <= 0
                            && dateHoraFormat.format(aux.getFinReserva()).compareTo(dateHoraFormat.format(date)) > 0) {
                        tabla.addItem(new Object[]{dateFormat.format(aux.getInicioReserva()), horaFormat.format(aux.getInicioReserva()),
                            horaFormat.format(aux.getFinReserva()), aux.getCliente().getNombre(), aux.getCliente().getApellidos(), aux.getInstalacion().getNombre(), update, delete}, i);
                        i++;
                        delete.addClickListener(a -> {
                            try {
                                //AÑADIMOS EL BOTON DE ELIMINAR POR CADA Reserva
                                ReservaDAO.eliminaReserva(aux);//Elimina la reserva de la bbdd
                            } catch (UnknownHostException ex) {
                                Logger.getLogger(ReservaUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            init(request);
                        });

                        update.addClickListener(a -> { try {
                            //AÑADIMOS EL BOTON DE EDITAR POR CADA CLIENTE
                            editarReserva(aux, request); //EJECUTA LA FUNCION EDITAR CLIENTE
                            } catch (UnknownHostException ex) {
                                Logger.getLogger(ReservaUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    }
                }
                layout.addComponent(tabla);
            }
            layout.setMargin(true);
            layout.setSpacing(true);
            setContent(layout);

        } catch (UnknownHostException ex) {
            Logger.getLogger(ReservaUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void addReserva(Reserva r) throws UnknownHostException {     //METODO QUE AÑADE UNA RESERVA A LA LISTA
        ReservaDAO.creaReserva(r);
    }

    public void creaReserva(VaadinRequest request) throws UnknownHostException {
        //CREAMOS UN FORMULARIO PARA QUE EL USUARIO INTRODUZCA LOS DATOS DE LA RESERVA A CREAR
        layout.removeAllComponents();
        HorizontalLayout datos = new HorizontalLayout();
        Label l = new Label("<h2 style='text-weight:bold;'>Nueva Reserva</h2>", ContentMode.HTML);
        layout.addComponent(l);
        DateField inicioReserva = new DateField("Inicio de la reserva:");
        inicioReserva.setResolution(Resolution.MINUTE);
        inicioReserva.setIcon(FontAwesome.HOURGLASS_START);
        DateField finReserva = new DateField("Fin de la reserva:");
        finReserva.setResolution(Resolution.MINUTE);
        finReserva.setIcon(FontAwesome.HOURGLASS_END);
        TextField dni = new TextField("DNI cliente:");
        dni.setIcon(FontAwesome.KEY);
        OptionGroup instalacion = new OptionGroup("Instalaciones:");
        ArrayList<Instalacion> listaInstalaciones = InstalacionDAO.mostrarInstalaciones();
        for (int i = 0; i < listaInstalaciones.size(); i++) {
            System.out.println(listaInstalaciones.get(i).getNombre());
            instalacion.addItems(listaInstalaciones.get(i).getNombre());
        }
        datos.addComponents(inicioReserva, finReserva, dni, instalacion);
        datos.setMargin(true);
        datos.setSpacing(true);
        Button enviar = new Button("Guardar", FontAwesome.CHECK);

        enviar.addClickListener(e -> {
            try {
                //UNA VEZ PULSADO EL BOTÓN SE CREA LA RESERVA Y LA AÑADIMOS A LA LISTA
                if (comprobarDatos((Date) inicioReserva.getValue(), (Date) finReserva.getValue()) == true && comprobarCliente(dni.getValue())) {
                    try {
                        Reserva aux = new Reserva();
                        aux.setInicioReserva(inicioReserva.getValue());
                        aux.setFinReserva(finReserva.getValue());
                        aux.setCliente(ClienteDAO.buscarCliente(dni.getValue()));
                        aux.setInstalacion(InstalacionDAO.buscarInstalacion((String) instalacion.getValue()));
                        addReserva(aux);
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Notification.show("Reserva - Fecha: " + dateFormat.format(inicioReserva.getValue()), "Registrado con éxito",
                                Notification.Type.TRAY_NOTIFICATION);
                        init(request);
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(ReservaUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (UnknownHostException ex) {
                Logger.getLogger(ReservaUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        Button volver = new Button("Cancelar", FontAwesome.CLOSE);
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

    
    public void editarReserva(Reserva r, VaadinRequest request) throws UnknownHostException {
        layout.removeAllComponents();
        //CREAMOS UN FORMULARIO PARA PODER EDITAR LA RESERVA
        HorizontalLayout datos = new HorizontalLayout();
        Label l = new Label("<h2>Modificar Reserva</h2>", ContentMode.HTML);
        layout.addComponent(l);

        DateField inicioReserva = new DateField("Inicio de la reserva:");
        inicioReserva.setResolution(Resolution.MINUTE);
        inicioReserva.setValue(r.getInicioReserva());
        inicioReserva.setIcon(FontAwesome.HOURGLASS_START);
        DateField finReserva = new DateField("Fin de la reserva:");
        finReserva.setResolution(Resolution.MINUTE);
        finReserva.setIcon(FontAwesome.HOURGLASS_END);
        finReserva.setValue(r.getFinReserva());
        TextField dni = new TextField("DNI cliente:");
        dni.setIcon(FontAwesome.KEY);
        dni.setValue(r.getCliente().getDni());
        OptionGroup instalacion = new OptionGroup("Instalaciones:");
        ArrayList<Instalacion> listaInstalaciones = InstalacionDAO.mostrarInstalaciones();
        for (int i = 0; i < listaInstalaciones.size(); i++) {
            System.out.println(listaInstalaciones.get(i).getNombre());
            instalacion.addItems(listaInstalaciones.get(i).getNombre());
        }
        instalacion.setValue(r.getInstalacion().getNombre());
        datos.addComponents(inicioReserva, finReserva,dni,instalacion);
        datos.setMargin(true);
        datos.setSpacing(true);
        Button enviar = new Button("Modificar", FontAwesome.EDIT);
        //EDITAMOS TODOS LOS CAMPOS QUE HAYA MODIFICADO EL USUARIO Y VOLVEMOS A INSERTAR EL CLIENTE EN LA LISTA
        enviar.addClickListener(e -> {
            try {
                if (comprobarDatos((Date) inicioReserva.getValue(), (Date) finReserva.getValue()) == true && comprobarCliente(dni.getValue())) {
                    Reserva aux;
                        aux = new Reserva();
                        
                        aux.setInicioReserva(inicioReserva.getValue());
                        aux.setFinReserva(finReserva.getValue());
                        aux.setCliente(ClienteDAO.buscarCliente(dni.getValue()));
                        aux.setInstalacion(InstalacionDAO.buscarInstalacion((String) instalacion.getValue()));
                        aux.setId_reserva(r.getId_reserva());
                        ReservaDAO.actualizaReserva(aux, r);
                        init(request);
                }
            } catch (UnknownHostException ex) {
                Logger.getLogger(ReservaUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        Button volver = new Button("Cancelar", FontAwesome.CLOSE);
        //REDIRECCIONA A LA CLASE ReservaUI
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
    }

    //Método para comprobar los datos introducidos en los formularios
    protected boolean comprobarDatos(Date ini, Date fin) {
        boolean b = false;//Variable booleana inicializada a false
        try {
            //Comprobamos si algún campo está vacío
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            if ((dateFormat.format(ini).equals(dateFormat.format(fin)))) {
                b = true;

            } else {//En caso de campo vacío, mostramos 2 tipos de error uno fijo y otro interactivo (para el proyecto final debatiremos este aspecto)
                //Notificacion de tipo Warning interactiva para el usuario.
                Notification.show("Error Fechas", "La fecha de inicio y fin deben ser iguales",
                        Notification.Type.WARNING_MESSAGE);
            }
            return b;
        } catch (NullPointerException e) {

            Notification.show("Error Fechas", "Introduzca la fecha y hora haciendo click en el calendario",
                    Notification.Type.WARNING_MESSAGE);
        }
        return b;
    }

    protected boolean comprobarCliente(String dni) throws UnknownHostException {
        if (ClienteDAO.buscarCliente(dni) == null) {
            Notification.show("Cliente no encontrado", "El dni no corresponde con ningún cliente registrado",
                    Notification.Type.WARNING_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    @WebServlet(urlPatterns = {"/Reserva/*"}, name = "gestionaReservaServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = ReservaUI.class, productionMode = false)
    public static class gestionaReservaServlet extends VaadinServlet {

    }

}
