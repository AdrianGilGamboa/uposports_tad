package com.mycompany.uposports;

import bbdd.InstalacionDAO;
import bbdd.MaterialDAO;
import clases.Instalacion;
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
import com.vaadin.ui.OptionGroup;
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
@Title("Material")
public class MaterialUI extends UI {

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
        crearMaterial.addClickListener(e -> {
            try {
                //Acción del botón
                crearMaterial(vaadinRequest);//Accedemos al método crearAbono
            } catch (UnknownHostException ex) {
                Logger.getLogger(MaterialUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        //MENU
        Label l = new Label("<h1 style='text-weight:bold;margin:auto;    padding-right: 100px;'>UPOSports</h2>", ContentMode.HTML);
        Label labelEntidad = new Label("<h2 style='text-weight:bold;margin:0'>Materiales - </h2>", ContentMode.HTML);
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

        //FIN MENU
        Label label = new Label("<h2 style='margin-top:0'> Materiales Registrados </h2>", ContentMode.HTML);

        if (layoutMostrarMateriales.getComponentIndex(layoutH) == -1) {//Si el layout horizontal que contiene los botones no se ha añadido, se añaden
            layoutH.addComponents(layoutHLabelabelTitulo, buttonReservas, buttonCliente, buttonAbonos, buttonInstalacion, buttonMateriales, buttonEmpleados, buttonAnunciantes, buttonLogout);//Añadimos los componentes al layout horizontal
            //Le metemos margen y espaciado, para mostrarlo posteriormente.
            layoutH2.setMargin(true);
            layoutH2.setSpacing(true);
            layoutH2.addComponents(labelEntidad, crearMaterial);
            layoutMostrarMateriales.addComponents(layoutH, layoutH2);
        }

        Table table = new Table();//Creamos la tabla donde meteremos las instancias
        table.setSizeFull();

        try {
            if (!MaterialDAO.consultaMateriales().isEmpty()) {//Si hay elementos en la lista de abonos
                layoutMostrarMateriales.addComponent(label);
                //Añadimos las columnas de la tabla
                table.addContainerProperty("Nombre", String.class, "");
                table.addContainerProperty("Descripción", String.class, "");
                table.addContainerProperty("Unidades", Integer.class, "");
                table.addContainerProperty("Instalacion", String.class, "");
                table.addContainerProperty("Modificar", Button.class, "");
                table.addContainerProperty("Eliminar", Button.class, "");
                for (int i = 0; i < MaterialDAO.consultaMateriales().size(); i++) {//Mientras haya elementos por recorrer
                    Material material = MaterialDAO.consultaMateriales().get(i);//Obtenemos el objeto de la lista

                    Button buttonModificar = new Button("Modificar", FontAwesome.EDIT);//Creamos el botón modificar
                    buttonModificar.addClickListener(e -> {
                        try {
                            //Acción del botón
                            editarMaterial(vaadinRequest, material);//Método para editar la instalación
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(MaterialUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });

                    Button buttonEliminar = new Button("Eliminar", FontAwesome.CLOSE);//Creamos el botón eliminar
                    buttonEliminar.addClickListener(e -> {
                        try {
                            //Acción del botón
                            MaterialDAO.eliminaMaterial(material);      //Eliminamos el objeto de la BBDD
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(MaterialUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        init(vaadinRequest);//Volvemos a ejecutar el método principal
                        Notification.show("Material - Nombre: " + material.getNombre(), "Eliminado con éxito",
                                Notification.Type.TRAY_NOTIFICATION);
                    });
                    //Añadimos la fila a la tabla
                    String ins;
                    if (material.getInstalacion() != null) {
                        ins = material.getInstalacion().getNombre();
                    } else {
                        ins = "-";
                    }
                    table.addItem(new Object[]{material.getNombre(), material.getDescripcion(), material.getUnidades(), ins, buttonModificar, buttonEliminar}, i);

                    layoutMostrarMateriales.addComponent(table);//Lo añadimos al layout vertical
                }
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(MaterialUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Le añadimos margen y espciado, para mostrarlo posteriormente.
        layoutMostrarMateriales.setMargin(true);
        layoutMostrarMateriales.setSpacing(true);
        setContent(layoutMostrarMateriales);

    }

    protected void crearMaterial(VaadinRequest vaadinRequest) throws UnknownHostException {//Método para crear Material
        final VerticalLayout layout = new VerticalLayout();//Creamos un vertical layout
        final HorizontalLayout layoutBotones = new HorizontalLayout();//Creamos un vertical layout
        final HorizontalLayout layoutTextField = new HorizontalLayout();
        Label l = new Label("<h2>Nuevo Material</h2>", ContentMode.HTML);
        layout.addComponent(l);

        final TextField nombre = new TextField();//Campo para insertar el nombre
        nombre.setCaption("Nombre:");
        nombre.setIcon(FontAwesome.TAG);//Icono
        final TextField descripcion = new TextField();//Campo para insertar la descripción
        descripcion.setCaption("Descripción:");
        descripcion.setIcon(FontAwesome.FILE_TEXT);
        final TextField unidades = new TextField();//Campo para insertar las unidades
        unidades.setCaption("Unidades:");
        unidades.setIcon(FontAwesome.STACK_OVERFLOW);
        OptionGroup instalacion = new OptionGroup("Instalaciones:");//Radio Button para la instalación
        ArrayList<Instalacion> listaInstalaciones = InstalacionDAO.mostrarInstalaciones();
        for (int i = 0; i < listaInstalaciones.size(); i++) {
            System.out.println(listaInstalaciones.get(i).getNombre());
            instalacion.addItems(listaInstalaciones.get(i).getNombre());
        }
        Button buttonRegistrar = new Button("Registrar", FontAwesome.CHECK);//Creamo el botón para registrar 
        buttonRegistrar.addClickListener(e -> {//Acción del botón
            vaadinRequest.setAttribute("nombre", nombre.getValue());//Añadimos en la petición el valor del campo nombre
            vaadinRequest.setAttribute("descripcion", descripcion.getValue());//Añadimos en la petición el valor del campo descripción
            vaadinRequest.setAttribute("unidades", unidades.getValue());//Añadimos en la petición el valor del campo unidades
            vaadinRequest.setAttribute("instalacion", instalacion.getValue());
            try {
                if (comprobarId(vaadinRequest)) {
                    if (comprobarDatos(vaadinRequest, layout) == true) {
                        //Se comprueban los datos, y si son correctos...
                        registrarMaterial(vaadinRequest);//Se envían los datos a registro de abono  
                        init(vaadinRequest);//Se lanza el método principal
                        //Notificacion de tipo bandeja para notificar la correcta operación.
                        Notification.show("Material - Nombre: " + nombre.getValue(), "Registrado con éxito",
                                Notification.Type.TRAY_NOTIFICATION);
                    }
                }
            } catch (UnknownHostException ex) {
                Logger.getLogger(MaterialUI.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        Button buttonCancelar = new Button("Cancelar", FontAwesome.CLOSE);//Nuevo botón para cancelar
        buttonCancelar.addClickListener(e -> {//Acción del botón
            init(vaadinRequest);//Se lanza el método principal
        });

        layoutBotones.addComponents(buttonCancelar, buttonRegistrar);
        layoutBotones.setSpacing(true);
        layoutBotones.setMargin(true);
        layoutTextField.addComponents(nombre, descripcion, unidades, instalacion);
        layoutTextField.setSpacing(true);
        layoutTextField.setMargin(true);
        layout.addComponents(layoutTextField, layoutBotones);//Añadimos los componentes al layout
        //Le añadimos margen y espciado, para mostrarlo posteriormente
        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);

    }

    //Exactamente igual que el método de crear Material, con la peculiaridad de que a este se le pasa el objeto abono y se prerellenan los campos con sus valores actuales.
    //Ya se ha comentado en el método anterior que realiza cada línea de código.
    protected void editarMaterial(VaadinRequest vaadinRequest, Material material) throws UnknownHostException {
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout layoutTextField = new HorizontalLayout();
        final HorizontalLayout layoutBotones = new HorizontalLayout();//Creamos un vertical layout
        Label l = new Label("<h2>Modificar Material</h2>", ContentMode.HTML);
        layout.addComponent(l);
        final TextField nombre = new TextField();//Campo para insertar el nombre
        nombre.setCaption("Nombre:");//Texto que se muestra en dicho campo
        nombre.setIcon(FontAwesome.TAG);//Icono
        nombre.setValue(material.getNombre());
        final TextField descripcion = new TextField();//Campo para insertar la descripción
        descripcion.setCaption("Descripción:");//Texto que se muestra en dicho campo
        descripcion.setIcon(FontAwesome.FILE_TEXT);
        descripcion.setValue(material.getDescripcion());
        final TextField unidades = new TextField();//Campo para insertar las unidades
        unidades.setCaption("Unidades:");//Texto que se muestra en dicho campo
        unidades.setIcon(FontAwesome.STACK_OVERFLOW);
        unidades.setValue(Integer.toString(material.getUnidades()));
        Button buttonRegistrar = new Button("Modificar", FontAwesome.EDIT);
        OptionGroup instalacion = new OptionGroup("Instalaciones:");
        ArrayList<Instalacion> listaInstalaciones = InstalacionDAO.mostrarInstalaciones();
        for (int i = 0; i < listaInstalaciones.size(); i++) {
            System.out.println(listaInstalaciones.get(i).getNombre());
            instalacion.addItems(listaInstalaciones.get(i).getNombre());
        }
        if (material.getInstalacion() != null) {
            instalacion.setValue(material.getInstalacion().getNombre());
        }

        buttonRegistrar.addClickListener(e -> {
            vaadinRequest.setAttribute("instalacion", instalacion.getValue());//Añadimos en la petición el valor del campo instalacion
            vaadinRequest.setAttribute("nombre", nombre.getValue());//Añadimos en la petición el valor del campo nombre
            vaadinRequest.setAttribute("descripcion", descripcion.getValue());//Añadimos en la petición el valor del campo descripción
            vaadinRequest.setAttribute("unidades", unidades.getValue());//Añadimos en la petición el valor del campo unidades
            if (comprobarDatos(vaadinRequest, layout) == true) {
                try {
                    modificarMaterial(vaadinRequest, material);//Se lanza el método modificar abono
                } catch (UnknownHostException ex) {
                    Logger.getLogger(MaterialUI.class.getName()).log(Level.SEVERE, null, ex);
                }
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
        layoutBotones.addComponents(buttonCancelar, buttonRegistrar);
        layoutBotones.setSpacing(true);
        layoutBotones.setMargin(true);
        layoutTextField.addComponents(nombre, descripcion, unidades, instalacion);
        layoutTextField.setSpacing(true);
        layoutTextField.setMargin(true);
        layout.addComponents(layoutTextField, layoutBotones);//Añadimos los componentes al layout
        //Le añadimos margen y espciado, para mostrarlo posteriormente
        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);
    }

    protected void modificarMaterial(VaadinRequest vaadinRequest, Material material) throws UnknownHostException {//Método para guardar los datos modificados en memoria, no hay persistencia de momento
        Material aux = new Material();
        aux.setNombre((String) vaadinRequest.getAttribute("nombre"));//Obtenemos de la petición el nombre de material y lo introducimos
        aux.setDescripcion((String) vaadinRequest.getAttribute("descripcion"));//Obtenemos de la petición la descripción del material y lo introducimos
        aux.setUnidades(Integer.parseInt((String) vaadinRequest.getAttribute("unidades")));//Obtenemos de la petición las unidades del material y lo introducimos
        aux.setInstalacion(InstalacionDAO.buscarInstalacion((String) vaadinRequest.getAttribute("instalacion")));//Obtenemos el nombre de la instalación y la introducimos
        MaterialDAO.actualizarMaterial(aux, material);
    }

    protected void registrarMaterial(VaadinRequest vaadinRequest) throws UnknownHostException {//Método para registrar los datos en memoria, no hay persistencia de momento
        Material material = new Material();//Creamos un nuevo objeto material
        material.setNombre((String) vaadinRequest.getAttribute("nombre"));//Obtenemos de la petición el nombre de material y lo introducimos
        material.setDescripcion((String) vaadinRequest.getAttribute("descripcion"));//Obtenemos de la petición la descripción del material y lo introducimos
        material.setUnidades(Integer.parseInt((String) vaadinRequest.getAttribute("unidades")));//Obtenemos de la petición las unidades del material y lo introducimos
        material.setInstalacion(InstalacionDAO.buscarInstalacion((String) vaadinRequest.getAttribute("instalacion")));//Obtenemos el nombre de la instalación y la introducimos
        MaterialDAO.creaMaterial(material); //Añadimos el objeto a la BBDD
    }

    //Método para comprobar los datos introducidos en los formularios
    protected boolean comprobarDatos(VaadinRequest vaadinRequest, VerticalLayout layout) {
        boolean b = false;//Variable booleana inicializada a false
        //Comprobamos si algún campo está vacío
        if ((String) vaadinRequest.getAttribute("nombre") != "" && (String) vaadinRequest.getAttribute("descripcion") != "" && (String) vaadinRequest.getAttribute("unidades") != "") {
            //Comprobamos si las unidades es numérica llamando al métdo isInteger
            if (isInteger((String) vaadinRequest.getAttribute("unidades")) == true) {
                //Si no se selecciona Instalación
                if (vaadinRequest.getAttribute("instalacion") != null) {
                    b = true;//Si se satisface todas las condiciones, la variables es true
                } else {
                    Notification.show("Seleccione una instalación", "El material debe tener una instalación asociada",
                            Notification.Type.WARNING_MESSAGE);
                }
            } else {//Si no es numérica
                //Notificacion de tipo Warning interactiva para el usuario.
                Notification.show("Error Datos Introducidos", "Las unidades deben ser númericas",
                        Notification.Type.WARNING_MESSAGE);
            }
        } else {

            //Notificacion de tipo Warning interactiva para el usuario.
            Notification.show("Campo vacío", "Debe rellenar todos los campos",
                    Notification.Type.WARNING_MESSAGE);
        }
        return b;
    }

    //Comprobar ID es único
    protected boolean comprobarId(VaadinRequest vaadinRequest) throws UnknownHostException {
        boolean b = false;
        ArrayList<Material> listaM = MaterialDAO.consultaMateriales();
        if (listaM.isEmpty()) {
            b = true;//Si se satisface todas las condiciones, la variables es true

        }
        if (MaterialDAO.buscarMaterial((String) vaadinRequest.getAttribute("nombre")) == null) {
            b = true;//Si se satisface todas las condiciones, la variables es true

        } else {
            Notification.show("Nombre Material Existente", "Material Registrado con este Nombre",
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
