/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.uposports;

import clases.Material;
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
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author manum
 */
@Theme("mytheme")
@Title("Material")
public class MaterialUI extends UI {
     
      final static List<Material> listaMateriales = new ArrayList<>();//Creamos una lista de materiales, donde se irán guardando y será compartida por todos los usuarios, necesario recargar la pag para ver cambios de otros usuarios

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layoutMostrarMateriales = new VerticalLayout();//Creamos un layout vertical
        final HorizontalLayout layoutH = new HorizontalLayout();//Creamos un layout horizontal
        final HorizontalLayout layoutHLabelabelTitulo = new HorizontalLayout();//Creamos un layout horizontal
        final HorizontalLayout layoutH2 = new HorizontalLayout();//Creamos un layout horizontal

                //RECUPERAMOS LA SESION Y SI NO HAY SESION NOS REDIRIGE A LA PÁGINA DE INICIO DE SESIÓN
        WrappedSession session = getSession().getSession();
        if (session.getAttribute("nombreUsuario") == null) {
            getUI().getPage().setLocation("/login");
        }
        
        Button crearMaterial = new Button("Crear Material", FontAwesome.PLUS_CIRCLE);//Botón para crear abono
        crearMaterial.addClickListener(e -> {//Acción del botón
            crearMaterial(vaadinRequest);//Accedemos al método crearAbono
        });

        Label l = new Label("<h1 style='text-weight:bold;text-align:center;margin:auto;    padding-right: 100px;'>UPOSports</h2>", ContentMode.HTML);
        Label labelEntidad = new Label("<h2 style='text-weight:bold;margin:0'>Material - </h2>", ContentMode.HTML);
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

        Button buttonEmpleados = new Button("Empleados", FontAwesome.MALE);//Botón para acceder a la entidad instalaciones
        buttonEmpleados.addClickListener(e -> {//Acción del botón
            getUI().getPage().setLocation("/Empleado");//Accedemos a la entidad abono
        });

        Button buttonMateriales = new Button("Materiales", FontAwesome.ARCHIVE);//Botón para acceder a la entidad instalaciones
        buttonMateriales.addClickListener(e -> {//Acción del botón
            getUI().getPage().setLocation("/Material");//Accedemos a la entidad abono
        });

        Button buttonReservas = new Button("Reservas", FontAwesome.CALENDAR);//Botón para acceder a la entidad instalaciones
        buttonReservas.addClickListener(e -> {//Acción del botón
            getUI().getPage().setLocation("/Reservas");//Accedemos a la entidad abono
        });

        Button buttonLogout = new Button("Cerrar Sesión", FontAwesome.SIGN_OUT);//Botón para cerrar sesión
        buttonLogout.addClickListener(e -> {//Acción del botón
            VaadinSession.getCurrent().getSession().invalidate();//Eliminamos la sesión
            getUI().getPage().setLocation("/");//Accedemos a la página principal
        });

        if (layoutMostrarMateriales.getComponentIndex(layoutH) == -1) {//Si el layout horizontal que contiene los botones no se ha añadido, se añaden
            layoutH.addComponents(layoutHLabelabelTitulo, buttonInstalacion, buttonCliente, buttonAbonos,buttonEmpleados,buttonMateriales,buttonReservas, buttonLogout);//Añadimos los componentes al layout horizontal
            //Le metemos margen y espaciado, para mostrarlo posteriormente.
            layoutH2.setMargin(true);
            layoutH2.setSpacing(true);
            layoutH2.addComponents(labelEntidad, crearMaterial);
            layoutMostrarMateriales.addComponents(layoutH, layoutH2);
        }

        Table table = new Table();//Creamos la tabla donde meteremos las instancias

        if (listaMateriales.size() > 0) {//Si hay elementos en la lista de abonos
            //Añadimos las columnas de la tabla
            table.addContainerProperty("Nombre", String.class, "");
            table.addContainerProperty("Descripción)", String.class, "");
            table.addContainerProperty("Jnidades", Integer.class, "");
            table.addContainerProperty("Editar", Button.class, "");
            table.addContainerProperty("Eliminar", Button.class, "");
            for (int i = 0; i < listaMateriales.size(); i++) {//Mientras haya elementos por recorrer
                Material material = listaMateriales.get(i);//Obtenemos el objeto de la lista

                Button buttonModificar = new Button("Modificar", FontAwesome.EDIT);//Creamos el botón modificar
                buttonModificar.addClickListener(e -> {//Acción del botón
                    editarAbono(vaadinRequest, material);//Método para editar la instalación
                });

                Button buttonEliminar = new Button("Eliminar", FontAwesome.CLOSE);//Creamos el botón eliminar
                buttonEliminar.addClickListener(e -> {//Acción del botón
                    listaMateriales.remove(material);//Eliminamos el objeto de la lista de instalaciones
                    init(vaadinRequest);//Volvemos a ejecutar el método principal
                    Notification.show("Material - Nombre: " + material.getNombre(), "Eliminado con éxito",
                            Notification.Type.TRAY_NOTIFICATION);
                });
                //Añadimos la fila a la tabla
                table.addItem(new Object[]{material.getNombre(), material.getDescripcion(), material.getUnidades(), buttonModificar, buttonEliminar}, i);

                layoutMostrarMateriales.addComponent(table);//Lo añadimos al layout vertical
            }
        }
        //Le añadimos margen y espciado, para mostrarlo posteriormente.
         layoutMostrarMateriales.setMargin(true);
         layoutMostrarMateriales.setSpacing(true);
        setContent( layoutMostrarMateriales);

    }

    protected void crearMaterial(VaadinRequest vaadinRequest) {//Método para crear abonos
        final VerticalLayout layout = new VerticalLayout();//Creamos un vertical layout
        final HorizontalLayout layoutBotones = new HorizontalLayout();//Creamos un vertical layout
        final HorizontalLayout layoutTextField = new HorizontalLayout();

        final TextField nombre = new TextField();//Campo para insertar el tipo
        nombre.setCaption("Nombre:");//Texto que se muestra en dicho campo
        nombre.setIcon(FontAwesome.TAG);//Icono
        final TextField descripcion = new TextField();//Campo para insertar la duracion
        descripcion.setCaption("Descripción:");//Texto que se muestra en dicho campo
        descripcion.setIcon(FontAwesome.PARAGRAPH);
        final TextField unidades = new TextField();//Campo para insertar el coste
        unidades.setCaption("Unidades:");//Texto que se muestra en dicho campo
        unidades.setIcon(FontAwesome.STACK_OVERFLOW);
        Button buttonRegistrar = new Button("Registrar", FontAwesome.CHECK);//Creamo el botón para registrar 
        buttonRegistrar.addClickListener(e -> {//Acción del botón
            vaadinRequest.setAttribute("nombre", nombre.getValue());//Añadimos en la petición el valor del campo tipo
            vaadinRequest.setAttribute("descripcion", descripcion.getValue());//Añadimos en la petición el valor del campo duración
            vaadinRequest.setAttribute("unidades", unidades.getValue());//Añadimos en la petición el valor del campo coste
            if (comprobarDatos(vaadinRequest, layout) == true) {//Se comprueban los datos, y si son correctos...
                registrarAbono(vaadinRequest);//Se envían los datos a registro de abono

                init(vaadinRequest);//Se lanza el método principal
                //Notificacion de tipo bandeja para notificar la correcta operación.
                Notification.show("Material - Nombre: " + nombre.getValue(), "Registrado con éxito",
                        Notification.Type.TRAY_NOTIFICATION);
            }

        });
        Button buttonCancelar = new Button("Cancelar", FontAwesome.CLOSE);//Nuevo botón para cancelar
        buttonCancelar.addClickListener(e -> {//Acción del botón
            init(vaadinRequest);//Se lanza el método principal
        });

        layoutBotones.addComponents(buttonCancelar,buttonRegistrar);
        layoutBotones.setSpacing(true);
         layoutTextField.addComponents(nombre, descripcion, unidades);
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
    protected void editarAbono(VaadinRequest vaadinRequest, Material material) {
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout layoutBotones = new HorizontalLayout();//Creamos un vertical layout
        final TextField nombre = new TextField();//Campo para insertar el tipo
        nombre.setCaption("Nombre:");//Texto que se muestra en dicho campo
        nombre.setIcon(FontAwesome.TAG);//Icono
        final TextField descripcion = new TextField();//Campo para insertar la duracion
        descripcion.setCaption("Descripción:");//Texto que se muestra en dicho campo
        descripcion.setIcon(FontAwesome.PARAGRAPH);
        final TextField unidades = new TextField();//Campo para insertar el coste
        unidades.setCaption("Unidades:");//Texto que se muestra en dicho campo
        unidades.setIcon(FontAwesome.STACK_OVERFLOW);
        Button buttonRegistrar = new Button("Modificar", FontAwesome.EDIT);

        buttonRegistrar.addClickListener(e -> {
             vaadinRequest.setAttribute("nombre", nombre.getValue());//Añadimos en la petición el valor del campo tipo
            vaadinRequest.setAttribute("descripcion", descripcion.getValue());//Añadimos en la petición el valor del campo duración
            vaadinRequest.setAttribute("unidades", unidades.getValue());//Añadimos en la petición el valor del campo coste
            if (comprobarDatos(vaadinRequest, layout) == true) {
                modificarAbono(vaadinRequest, material);//Se lanza el método modificar abono
                init(vaadinRequest);
                //Notificacion de tipo bandeja para notificar la correcta operación.
                Notification.show("Material - Nombre: " + nombre.getValue(), "Modificado con éxito",
                        Notification.Type.TRAY_NOTIFICATION);
            }

        });
        Button buttonCancelar = new Button("Cancelar", FontAwesome.CLOSE);
        buttonCancelar.addClickListener(e -> {
            init(vaadinRequest);
        });
layoutBotones.addComponents(buttonCancelar,buttonRegistrar);
        layoutBotones.setSpacing(true);
        layout.addComponents(nombre, descripcion, unidades, layoutBotones);
        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);
    }

    protected void modificarAbono(VaadinRequest vaadinRequest, Material material) {//Método para guardar los datos modificados en memoria, no hay persistencia de momento
        material.setNombre((String) vaadinRequest.getAttribute("nombre"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo tipo del objeto abono
        material.setDescripcion((String) vaadinRequest.getAttribute("descripcion"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo duración del objeto abono
        material.setUnidades(Integer.parseInt((String)vaadinRequest.getAttribute("unidades")));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo coste del objeto abono
    }

    protected void registrarAbono(VaadinRequest vaadinRequest) {//Método para registrar los datos en memoria, no hay persistencia de momento
        Material material = new Material();//Creamos un nuevo objeto abono
        material.setNombre((String) vaadinRequest.getAttribute("nombre"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo tipo del objeto abono
        material.setDescripcion((String) vaadinRequest.getAttribute("descripcion"));//Obtenemos de la petición el tipo de abono y lo introducimos en el campo duración del objeto abono
        material.setUnidades(Integer.parseInt((String)vaadinRequest.getAttribute("unidades")));
        listaMateriales.add(material);//Añadimos el objeto a la lista de abonos

    }

    //Método para comprobar los datos introducidos en los formularios
    protected boolean comprobarDatos(VaadinRequest vaadinRequest, VerticalLayout layout) {
        boolean b = false;//Variable booleana inicializada a false
        //Comprobamos si algún campo está vacío
        if ((String) vaadinRequest.getAttribute("nombre") != "" && (String) vaadinRequest.getAttribute("descripcion") != "" && (String) vaadinRequest.getAttribute("unidades") != "") {
            //Comprobamos si la capacidad es numérica llamando al métdo isInteger
            if (isInteger((String) vaadinRequest.getAttribute("unidades")) == true) {
                b = true;//Si se satisface todas las condiciones, la variables es true
            } else {//Si la duración o el coste no es numérica
                //Notificacion de tipo Warning interactiva para el usuario.
                Notification.show("Error Datos Introducidos", "Las unidades deben ser númericas",
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



    @WebServlet(urlPatterns = "/Material/*", name = "ServletGestionMaterial", asyncSupported = true)
    @VaadinServletConfiguration(ui = MaterialUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}


