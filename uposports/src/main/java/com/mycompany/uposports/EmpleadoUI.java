/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.uposports;

import clases.Empleado;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;


@Theme("mytheme")
@Title("Inicio")
@PreserveOnRefresh
public class EmpleadoUI extends UI {

    final static List<Empleado> listaEmpleados = new ArrayList<>();//Creamos una lista de abonos, donde se irán guardando y será compartida por todos los usuarios, necesario recargar la pag para ver cambios de otros usuarios
    Label errorTipo = new Label("El telefono debe ser un entero");//Etiqueta error de tipo 
    Label errorCampoVacio = new Label("Los campos no pueden estar vacíos");//Etiqueta derror de campo vacío

       
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        //Empezamos obteniendo la sesión y creando una lista de empleados para
        //ir mentiendo cuando se vayan creando
        final VerticalLayout layoutMostrarEmpleados = new VerticalLayout();//Creamos un layout vertical
        final HorizontalLayout layoutH = new HorizontalLayout();//Creamos un layout horizontal
        final HorizontalLayout layoutHLabelabelTitulo = new HorizontalLayout();//Creamos un layout horizontal
        final HorizontalLayout layoutH2 = new HorizontalLayout();//Creamos un layout horizontal
        Button crearEmpleado = new Button("Crear Empleado", FontAwesome.PLUS_CIRCLE);//Botón para crear abono
        crearEmpleado.addClickListener(e -> {//Acción del botón
            crearEmpleado(vaadinRequest);//Accedemos al método crearAbono
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

        Button buttonEmpleados = new Button("Empleados", FontAwesome.BUILDING);//Botón para acceder a la entidad instalaciones
        buttonEmpleados.addClickListener(e -> {//Acción del botón
            getUI().getPage().setLocation("/Empleado");//Accedemos a la entidad abono
        });

        Button buttonMateriales = new Button("Materiales", FontAwesome.BUILDING);//Botón para acceder a la entidad instalaciones
        buttonMateriales.addClickListener(e -> {//Acción del botón
            getUI().getPage().setLocation("/Material");//Accedemos a la entidad abono
        });

        Button buttonReservas = new Button("Reservas", FontAwesome.BUILDING);//Botón para acceder a la entidad instalaciones
        buttonReservas.addClickListener(e -> {//Acción del botón
            getUI().getPage().setLocation("/Material");//Accedemos a la entidad abono
        });

        Button buttonLogout = new Button("Cerrar Sesión", FontAwesome.SIGN_OUT);//Botón para cerrar sesión
        buttonLogout.addClickListener(e -> {//Acción del botón
            VaadinSession.getCurrent().getSession().invalidate();//Eliminamos la sesión
            getUI().getPage().setLocation("/");//Accedemos a la página principal
        });
         if (layoutMostrarEmpleados.getComponentIndex(layoutH) == -1) {//Si el layout horizontal que contiene los botones no se ha añadido, se añaden
            layoutH.addComponents(layoutHLabelabelTitulo, buttonInstalacion, buttonCliente, buttonAbonos, buttonLogout);//Añadimos los componentes al layout horizontal
            //Le metemos margen y espaciado, para mostrarlo posteriormente.
            layoutH2.setMargin(true);
            layoutH2.setSpacing(true);
            layoutH2.addComponents(labelEntidad, crearEmpleado);
            layoutMostrarEmpleados.addComponents(layoutH, layoutH2);
        }
          Table table = new Table();//Creamos la tabla donde meteremos las instancias

        if (listaEmpleados.size() > 0) {//Si hay elementos en la lista de abonos
            //Añadimos las columnas de la tabla
            table.addContainerProperty("Nombre", String.class, "");
            table.addContainerProperty("Nombre", String.class, "");
            table.addContainerProperty("Apellidos", String.class, "");
            table.addContainerProperty("Telefono", int.class, "");

            table.addContainerProperty("Editar", Button.class, "");
            table.addContainerProperty("Eliminar", Button.class, "");
            for (int i = 0; i < listaEmpleados.size(); i++) {//Mientras haya elementos por recorrer
                Empleado empleado = listaEmpleados.get(i);//Obtenemos el objeto de la lista

                Button buttonModificar = new Button("Modificar", FontAwesome.EDIT);//Creamos el botón modificar
                buttonModificar.addClickListener(e -> {//Acción del botón
                    editarEmpleado(vaadinRequest, empleado);//Método para editar la instalación
                });

                Button buttonEliminar = new Button("Eliminar", FontAwesome.CLOSE);//Creamos el botón eliminar
                buttonEliminar.addClickListener(e -> {//Acción del botón
                    listaEmpleados.remove(empleado);//Eliminamos el objeto de la lista de instalaciones
                    init(vaadinRequest);//Volvemos a ejecutar el método principal
                    Notification.show("Empleado - Dni: " + empleado.getDni(), "Eliminado con éxito",
                            Notification.Type.TRAY_NOTIFICATION);
                });
                //Añadimos la fila a la tabla
                table.addItem(new Object[]{empleado.getDni(), empleado.getNombre(), empleado.getApellidos(), empleado.getTelefono(), buttonModificar, buttonEliminar}, i);

                layoutMostrarEmpleados.addComponent(table);//Lo añadimos al layout vertical
            }
        }
         layoutMostrarEmpleados.setMargin(true);
        layoutMostrarEmpleados.setSpacing(true);
        setContent(layoutMostrarEmpleados);
        
    }
    protected void crearEmpleado(VaadinRequest vaadinRequest) {//Método para crear abonos
        final VerticalLayout layout = new VerticalLayout();//Creamos un vertical layout
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

            if (comprobarDatos(vaadinRequest, layout) == true) {//Se comprueban los datos, y si son correctos...
                registrarEmpleado(vaadinRequest);//Se envían los datos a registro de abono

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

        layout.addComponents(dni, nombre, apellidos,telefono, buttonRegistrar, buttonCancelar);//Añadimos los componentes al layout
        //Le añadimos margen y espciado, para mostrarlo posteriormente
        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);

    }
    
    protected void editarEmpleado(VaadinRequest vaadinRequest, Empleado empleado) {
        final VerticalLayout layout = new VerticalLayout();
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
        Button buttonRegistrar = new Button("Modificar", FontAwesome.EDIT);

        buttonRegistrar.addClickListener(e -> {
            vaadinRequest.setAttribute("dni", dni.getValue());//Añadimos en la petición el valor del campo tipo
            vaadinRequest.setAttribute("nombre", nombre.getValue());//Añadimos en la petición el valor del campo duración
            vaadinRequest.setAttribute("apellidos", apellidos.getValue());//Añadimos en la petición el valor del campo coste
            vaadinRequest.setAttribute("telefono", telefono.getValue());//Añadimos en la petición el valor del campo telefono

            if (comprobarDatos(vaadinRequest, layout) == true) {
                modificarEmpleado(vaadinRequest, empleado);//Se lanza el método modificar abono
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

        layout.addComponents(dni, nombre, apellidos,telefono, buttonRegistrar, buttonCancelar);
        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);
    }
    
     protected void modificarEmpleado(VaadinRequest vaadinRequest, Empleado empleado) {//Método para guardar los datos modificados en memoria, no hay persistencia de momento
        empleado.setDni((String) vaadinRequest.getAttribute("dni"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo tipo del objeto abono
        empleado.setNombre((String) vaadinRequest.getAttribute("nombre"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo duración del objeto abono
        empleado.setApellidos((String) vaadinRequest.getAttribute("apellidos"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo coste del objeto abono
        empleado.setTelefono((vaadinRequest.getAttribute("telefono")));
    }
    protected void registrarEmpleado(VaadinRequest vaadinRequest) {//Método para registrar los datos en memoria, no hay persistencia de momento
        Empleado empleado = new Empleado();//Creamos un nuevo objeto abono
        empleado.setDni((String) vaadinRequest.getAttribute("dni"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo tipo del objeto abono
        empleado.setNombre((String) vaadinRequest.getAttribute("nombre"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo duración del objeto abono
        empleado.setApellidos((String) vaadinRequest.getAttribute("apellidos"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo coste del objeto abono
        empleado.setTelefono((vaadinRequest.getAttribute("telefono")));
//Obtenemos de la petición el tipo de abono y lo introducimos en el campo coste del objeto abono
        listaEmpleados.add(empleado);//Añadimos el objeto a la lista de abonos

    }
     protected boolean comprobarDatos(VaadinRequest vaadinRequest, VerticalLayout layout) {
        boolean b = false;//Variable booleana inicializada a false
        //Comprobamos si algún campo está vacío
        if ((String) vaadinRequest.getAttribute("dni") != "" && (String) vaadinRequest.getAttribute("nombre") != "" && (String) vaadinRequest.getAttribute("apellidos") != ""  && (String) vaadinRequest.getAttribute("telefono") != "") {
            //Comprobamos si la capacidad es numérica llamando al métdo isInteger
            if (isInteger((String) vaadinRequest.getAttribute("telefono")) == true) {
                b = true;//Si se satisface todas las condiciones, la variables es true
            } else {//Si la duración o el coste no es numérica
                if (layout.getComponentIndex(errorTipo) == -1) {//Si no se ha añadido el componente al layout

                    layout.addComponentAsFirst(errorTipo);//Añadimos el camponente al layout
                }
                //Notificacion de tipo Warning interactiva para el usuario.
                Notification.show("Error Datos Introducidos", "La duracion y el coste deben ser numéricos",
                        Notification.Type.WARNING_MESSAGE);

            }
        } else {//En caso de campo vacío, mostramos 2 tipos de error uno fijo y otro interactivo (para el proyecto final debatiremos este aspecto)
            if (layout.getComponentIndex(errorCampoVacio) == -1) {//Si no se ha añadido el componente al layout

                layout.addComponentAsFirst(errorCampoVacio);//Añadimos el camponente al layout
            }
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
