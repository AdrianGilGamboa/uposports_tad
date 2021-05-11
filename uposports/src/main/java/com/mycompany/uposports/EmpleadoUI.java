package com.mycompany.uposports;

import bbdd.EmpleadoDAO;
import clases.Empleado;
import com.vaadin.annotations.PreserveOnRefresh;
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
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;

@Theme("mytheme")
@Title("Inicio")
@PreserveOnRefresh
public class EmpleadoUI extends UI {

    final static List<Empleado> listaEmpleados = new ArrayList<>();//Creamos una lista de abonos, donde se irán guardando y será compartida por todos los usuarios, necesario recargar la pag para ver cambios de otros usuarios

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        //Empezamos obteniendo la sesión y creando una lista de empleados para
        //ir mentiendo cuando se vayan creando
        final VerticalLayout layoutMostrarEmpleados = new VerticalLayout();//Creamos un layout vertical
        final HorizontalLayout layoutH = new HorizontalLayout();//Creamos un layout horizontal
        final HorizontalLayout layoutHLabelabelTitulo = new HorizontalLayout();//Creamos un layout horizontal
        final HorizontalLayout layoutH2 = new HorizontalLayout();//Creamos un layout horizontal

        //RECUPERAMOS LA SESION Y SI NO HAY SESION NOS REDIRIGE A LA PÁGINA DE INICIO DE SESIÓN
        WrappedSession session = getSession().getSession();
        if (session.getAttribute("nombreUsuario") == null) {
            getUI().getPage().setLocation("/login");
        }

        Button crearEmpleado = new Button("Crear Empleado", FontAwesome.PLUS_CIRCLE);//Botón para crear abono
        crearEmpleado.addClickListener(e -> {//Acción del botón
            crearEmpleado(vaadinRequest);//Accedemos al método crearAbono
        });
        Label l = new Label("<h1 style='text-weight:bold;text-align:center;margin:auto;    padding-right: 100px;'>UPOSports</h2>", ContentMode.HTML);
        Label labelEntidad = new Label("<h2 style='text-weight:bold;margin:0'>Empleados - </h2>", ContentMode.HTML);
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

        Button buttonLogout = new Button("Cerrar Sesión", FontAwesome.SIGN_OUT);//Botón para cerrar sesión
        buttonLogout.addClickListener(e -> {//Acción del botón
            VaadinSession.getCurrent().getSession().invalidate();//Eliminamos la sesión
            getUI().getPage().setLocation("/");//Accedemos a la página principal
        });
        
                Button buttonAnunciantes = new Button("Anunciantes", FontAwesome.BELL);//Botón para acceder a la entidad instalaciones
        buttonAnunciantes.addClickListener(e -> {//Acción del botón
            getUI().getPage().setLocation("/Anunciante");//Accedemos a la entidad abono
        });

        if (layoutMostrarEmpleados.getComponentIndex(layoutH) == -1) {//Si el layout horizontal que contiene los botones no se ha añadido, se añaden
            layoutH.addComponents(layoutHLabelabelTitulo, buttonReservas, buttonCliente, buttonAbonos, buttonInstalacion, buttonMateriales, buttonEmpleados, buttonAnunciantes, buttonLogout);//Añadimos los componentes al layout horizontal
            //Le metemos margen y espaciado, para mostrarlo posteriormente.
            layoutH2.setMargin(true);
            layoutH2.setSpacing(true);
            layoutH2.addComponents(labelEntidad, crearEmpleado);
            layoutMostrarEmpleados.addComponents(layoutH, layoutH2);
        }
        Table table = new Table();//Creamos la tabla donde meteremos las instancias
        table.setSizeFull();

        try {
            if (!EmpleadoDAO.mostrarEmpleados().isEmpty()) {//Si hay elementos en la lista de abonos
                //Añadimos las columnas de la tabla
                table.addContainerProperty("DNI", String.class, "");
                table.addContainerProperty("Nombre", String.class, "");
                table.addContainerProperty("Apellidos", String.class, "");
                table.addContainerProperty("Telefono", Integer.class, "");
                
                table.addContainerProperty("Modificar", Button.class, "");
                table.addContainerProperty("Eliminar", Button.class, "");
                for (int i = 0; i < EmpleadoDAO.mostrarEmpleados().size(); i++) {//Mientras haya elementos por recorrer
                    Empleado empleado = EmpleadoDAO.mostrarEmpleados().get(i);//Obtenemos el objeto de la lista
                    
                    Button buttonModificar = new Button("Modificar", FontAwesome.EDIT);//Creamos el botón modificar
                    buttonModificar.addClickListener(e -> {//Acción del botón
                        editarEmpleado(vaadinRequest, empleado);//Método para editar la instalación
                    });
                    
                    Button buttonEliminar = new Button("Eliminar", FontAwesome.CLOSE);//Creamos el botón eliminar
                    buttonEliminar.addClickListener(e -> {
                        try {
                        //Acción del botón
                        //listaEmpleados.remove(empleado);
                        EmpleadoDAO.eliminarEmpleados(empleado);//Eliminamos el objeto de la BBDD
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(EmpleadoUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        init(vaadinRequest);//Volvemos a ejecutar el método principal
                        Notification.show("Empleado - Dni: " + empleado.getDni(), "Eliminado con éxito",
                                Notification.Type.TRAY_NOTIFICATION);
                    });
                    //Añadimos la fila a la tabla
                    table.addItem(new Object[]{empleado.getDni(), empleado.getNombre(), empleado.getApellidos(), empleado.getTelefono(), buttonModificar, buttonEliminar}, i);
                    
                    layoutMostrarEmpleados.addComponent(table);//Lo añadimos al layout vertical
                }
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(EmpleadoUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        layoutMostrarEmpleados.setMargin(true);
        layoutMostrarEmpleados.setSpacing(true);
        setContent(layoutMostrarEmpleados);

    }

    protected void crearEmpleado(VaadinRequest vaadinRequest) {//Método para crear abonos
        final VerticalLayout layout = new VerticalLayout();//Creamos un vertical layout
        final HorizontalLayout layoutBotones = new HorizontalLayout();
        final HorizontalLayout layoutTextField = new HorizontalLayout();

        final TextField dni = new TextField();//Campo para insertar el tipo
        dni.setCaption("DNI:");//Texto que se muestra en dicho campo
        dni.setIcon(FontAwesome.ADN);//Icono
        final TextField nombre = new TextField();//Campo para insertar la duracion
        nombre.setCaption("Nombre:");//Texto que se muestra en dicho campo
        nombre.setIcon(FontAwesome.USER);
        final TextField apellidos = new TextField();//Campo para insertar el coste
        apellidos.setCaption("Apellidos:");//Texto que se muestra en dicho campo
        apellidos.setIcon(FontAwesome.USER);
        final TextField telefono = new TextField();//Campo para insertar el coste
        telefono.setCaption("Telefono:");//Texto que se muestra en dicho campo
        telefono.setIcon(FontAwesome.PHONE);
        Button buttonRegistrar = new Button("Registrar", FontAwesome.CHECK);//Creamo el botón para registrar 
        buttonRegistrar.addClickListener(e -> {//Acción del botón
            vaadinRequest.setAttribute("dni", dni.getValue());//Añadimos en la petición el valor del campo tipo
            vaadinRequest.setAttribute("nombre", nombre.getValue());//Añadimos en la petición el valor del campo duración
            vaadinRequest.setAttribute("apellidos", apellidos.getValue());//Añadimos en la petición el valor del campo coste
            vaadinRequest.setAttribute("telefono", telefono.getValue());//Añadimos en la petición el valor del campo telefono

            if (comprobarDatos(vaadinRequest, layout) == true) {
                try {
                    //Se comprueban los datos, y si son correctos...
                    registrarEmpleado(vaadinRequest);//Se envían los datos a registro de abono
                } catch (UnknownHostException ex) {
                    Logger.getLogger(EmpleadoUI.class.getName()).log(Level.SEVERE, null, ex);
                }

                init(vaadinRequest);//Se lanza el método principal
                //Notificacion de tipo bandeja para notificar la correcta operación.
                Notification.show("Empleado - Dni " + dni.getValue(), "Registrado con éxito",
                        Notification.Type.TRAY_NOTIFICATION);
            }

        });
        Button buttonCancelar = new Button("Cancelar", FontAwesome.CLOSE);//Nuevo botón para cancelar
        buttonCancelar.addClickListener(e -> {//Acción del botón
            init(vaadinRequest);//Se lanza el método principal
        });
        layoutBotones.addComponents(buttonCancelar, buttonRegistrar);
        layoutBotones.setSpacing(true);

        layoutTextField.addComponents(dni, nombre, apellidos, telefono);
        layoutTextField.setSpacing(true);
        layoutTextField.setMargin(true);

        layout.addComponents(layoutTextField, layoutBotones);//Añadimos los componentes al layout
        //Le añadimos margen y espciado, para mostrarlo posteriormente
        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);

    }

    protected void editarEmpleado(VaadinRequest vaadinRequest, Empleado empleado) {
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout layoutBotones = new HorizontalLayout();
        final TextField dni = new TextField();//Campo para insertar el tipo
        dni.setCaption("DNI:");//Texto que se muestra en dicho campo
        dni.setIcon(FontAwesome.ADN);//Icono
        dni.setValue(empleado.getDni());
        final TextField nombre = new TextField();//Campo para insertar la duracion
        nombre.setCaption("Nombre:");//Texto que se muestra en dicho campo
        nombre.setIcon(FontAwesome.USER);
        nombre.setValue(empleado.getNombre());
        final TextField apellidos = new TextField();//Campo para insertar el coste
        apellidos.setCaption("Apellidos:");//Texto que se muestra en dicho campo
        apellidos.setIcon(FontAwesome.USER);
        apellidos.setValue(empleado.getApellidos());
        final TextField telefono = new TextField();//Campo para insertar el coste
        telefono.setCaption("Telefono:");//Texto que se muestra en dicho campo
        telefono.setIcon(FontAwesome.PHONE);
        telefono.setValue(Integer.toString(empleado.getTelefono()));
        Button buttonRegistrar = new Button("Modificar", FontAwesome.EDIT);

        buttonRegistrar.addClickListener(e -> {
            vaadinRequest.setAttribute("dni", dni.getValue());//Añadimos en la petición el valor del campo tipo
            vaadinRequest.setAttribute("nombre", nombre.getValue());//Añadimos en la petición el valor del campo duración
            vaadinRequest.setAttribute("apellidos", apellidos.getValue());//Añadimos en la petición el valor del campo coste
            vaadinRequest.setAttribute("telefono", telefono.getValue());//Añadimos en la petición el valor del campo telefono

            if (comprobarDatos(vaadinRequest, layout) == true) {
                try {
                    modificarEmpleado(vaadinRequest, empleado);//Se lanza el método modificar abono
                } catch (UnknownHostException ex) {
                    Logger.getLogger(EmpleadoUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                init(vaadinRequest);
                //Notificacion de tipo bandeja para notificar la correcta operación.
                Notification.show("Empleado - DNI: " + dni.getValue(), "Modificado con éxito",
                        Notification.Type.TRAY_NOTIFICATION);
            }

        });
        Button buttonCancelar = new Button("Cancelar", FontAwesome.CLOSE);
        buttonCancelar.addClickListener(e -> {
            init(vaadinRequest);
        });
        layoutBotones.addComponents(buttonCancelar, buttonRegistrar);
        layoutBotones.setSpacing(true);

        layout.addComponents(dni, nombre, apellidos, telefono, layoutBotones);
        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);
    }

    protected void modificarEmpleado(VaadinRequest vaadinRequest, Empleado empleado) throws UnknownHostException {//Método para guardar los datos modificados en memoria, no hay persistencia de momento
        Empleado aux = new Empleado();
        aux.setDni((String) vaadinRequest.getAttribute("dni"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo tipo del objeto abono
        aux.setNombre((String) vaadinRequest.getAttribute("nombre"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo duración del objeto abono
        aux.setApellidos((String) vaadinRequest.getAttribute("apellidos"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo coste del objeto abono
        aux.setTelefono(Integer.parseInt((String) (vaadinRequest.getAttribute("telefono"))));
        EmpleadoDAO.actualizarEmpleado(aux, empleado);
    }

    protected void registrarEmpleado(VaadinRequest vaadinRequest) throws UnknownHostException {//Método para registrar los datos en memoria, no hay persistencia de momento
        Empleado empleado = new Empleado();//Creamos un nuevo objeto abono
        empleado.setDni((String) vaadinRequest.getAttribute("dni"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo tipo del objeto abono
        empleado.setNombre((String) vaadinRequest.getAttribute("nombre"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo duración del objeto abono
        empleado.setApellidos((String) vaadinRequest.getAttribute("apellidos"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo coste del objeto abono
        empleado.setTelefono(Integer.parseInt((String) (vaadinRequest.getAttribute("telefono"))));
//Obtenemos de la petición el tipo de abono y lo introducimos en el campo coste del objeto abono        
        EmpleadoDAO.insertarEmpleado(empleado);//Añadimos el objeto a la BBDD
    }

    protected boolean comprobarDatos(VaadinRequest vaadinRequest, VerticalLayout layout) {
        boolean b = false;//Variable booleana inicializada a false
        //Comprobamos si algún campo está vacío
        if ((String) vaadinRequest.getAttribute("dni") != "" && (String) vaadinRequest.getAttribute("nombre") != "" && (String) vaadinRequest.getAttribute("apellidos") != "" && (String) vaadinRequest.getAttribute("telefono") != "") {
            //Comprobamos si la capacidad es numérica llamando al métdo isInteger
            if (isInteger((String) vaadinRequest.getAttribute("telefono")) == true) {
                b = true;//Si se satisface todas las condiciones, la variables es true
            } else {//Si la duración o el coste no es numérica
                //Notificacion de tipo Warning interactiva para el usuario.
                Notification.show("Error Datos Introducidos", "El teléfono debe ser numéricos",
                        Notification.Type.WARNING_MESSAGE);
            }
        } else {//En caso de campo vacío, mostramos 2 tipos de error uno fijo y otro interactivo (para el proyecto final debatiremos este aspecto)

            //Notificacion de tipo Warning interactiva para el usuario.
            Notification.show("Campo vacío", "Debe rellenar todos los campos",
                    Notification.Type.WARNING_MESSAGE);
        }
        return b;
    }

    protected static boolean isInteger(String cadena) {
        try {//Intentamos parsear el la cadena a entero, si se satisface, devolvemos true
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {//De lo contrario, captura la excepción y devolvemos false
            return false;
        }
    }

    @WebServlet(urlPatterns = "/Empleado/*", name = "ServletPrincipal", asyncSupported = true)
    @VaadinServletConfiguration(ui = EmpleadoUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
