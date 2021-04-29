package adri;

import com.vaadin.annotations.PreserveOnRefresh;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
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

@Theme("mytheme")
@Title("Instalacion")
@PreserveOnRefresh //Si volvemos a cargar la url, no perdemos la sesión

public class InstalacionUI extends UI {

    final static List<Instalacion> listaInstalaciones = new ArrayList<>();//Creamos una lista de abonos, donde se irán guardando y será compartida por todos los usuarios, necesario recargar la pag para ver cambios de otros usuarios
    Label errorTipo = new Label("La capacidad debe ser numérica");//Etiqueta error de tipo 
    Label errorCampoVacio = new Label("Los campos no pueden estar vacíos");//Etiqueta derror de campo vacío

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layoutMostrarInstalaciones = new VerticalLayout();//Creamos un layout vertical
        final HorizontalLayout layoutH = new HorizontalLayout();//Creamos un layout horizontal

        Button crearInstalacion = new Button("Crear Instalacion", FontAwesome.PLUS_CIRCLE);//Botón para crear instalación
        crearInstalacion.addClickListener(e -> {//Acción del botón
            crearInstalacion(vaadinRequest);//Accedemos al método crearInstalación
        });

        Button buttonInstalacion = new Button("Abonos", FontAwesome.MONEY);//Botón para acceder a la entidad abono
        buttonInstalacion.addClickListener(e -> {//Acción del botón
            getUI().getPage().setLocation("/Abono");//Accedemos a la entidad abono
        });

        Button buttonLogout = new Button("Cerrar Sesión", FontAwesome.SIGN_OUT);//Botón para acceder a la entidad abono
        buttonLogout.addClickListener(e -> {
            VaadinSession.getCurrent().getSession().invalidate();//Eliminamos la sesión
            getUI().getPage().setLocation("/");//Accedemos a la página principal
        });

        if (layoutMostrarInstalaciones.getComponentIndex(layoutH) == -1) {//Si el layout horizontal que contiene los botones no se ha añadido, se añaden
            layoutH.addComponents(crearInstalacion, buttonInstalacion, buttonLogout);//Añadimos los componentes al layout horizontal
            //Le metemos margen y espaciado, para mostrarlo posteriormente.
            layoutH.setMargin(true);
            layoutH.setSpacing(true);
            layoutMostrarInstalaciones.addComponent(layoutH);
        }
        Table table = new Table();//Creamos la tabla donde meteremos las instancias
        if (listaInstalaciones.size() > 0) {//Si hay elementos en la lista de instalaciones
            //Añadimos las columnas de la tabla
            table.addContainerProperty("Nombre", String.class, "");
            table.addContainerProperty("Descripcion", String.class, "");
            table.addContainerProperty("Capacidad", Integer.class, "");
            table.addContainerProperty("Editar", Button.class, "");
            table.addContainerProperty("Eliminar", Button.class, "");

            for (int i = 0; i < listaInstalaciones.size(); i++) {//Mientras haya elementos por recorrer

                Instalacion instalacion = listaInstalaciones.get(i);//Obtenemos el objeto de la lista

                Button buttonModificar = new Button("Modificar", FontAwesome.EDIT);//Creamos el botón modificar
                buttonModificar.addClickListener(e -> {//Acción del botón
                    editarInstalacion(vaadinRequest, instalacion);//Método para editar la instalación
                });

                Button buttonEliminar = new Button("Eliminar", FontAwesome.CLOSE);//Creamos el botón eliminar
                buttonEliminar.addClickListener(e -> {//Acción del botón
                    listaInstalaciones.remove(instalacion);//Eliminamos el objeto de la lista de instalaciones
                    Notification.show("Instalación - Nombre: " + instalacion.getNombre(), "Eliminada con éxito",
                            Notification.Type.TRAY_NOTIFICATION);
                    init(vaadinRequest);//Volvemos a ejecutar el método principal
                });
                //Añadimos la fila a la tabla
                table.addItem(new Object[]{instalacion.getNombre(), instalacion.getDescripcion(), instalacion.getCapacidad(), buttonModificar, buttonEliminar}, i);
                layoutMostrarInstalaciones.addComponents(table);//Lo añadimos al layout vertical
            }
        }

        //Le añadimos margen y espciado, para mostrarlo posteriormente.
        layoutMostrarInstalaciones.setMargin(true);
        layoutMostrarInstalaciones.setSpacing(true);
        setContent(layoutMostrarInstalaciones);

    }

    protected void crearInstalacion(VaadinRequest vaadinRequest) {//Método para crear instalaciones
        final VerticalLayout layout = new VerticalLayout();//Creamos un vertical layout
        final TextField nombre = new TextField();//Campo para insertar el nombre
        nombre.setCaption("Nombre:");//Texto que se muestra en dicho campo
        nombre.setIcon(FontAwesome.TAG);
        final TextField descripcion = new TextField();//Campo para insertar la descripción
        descripcion.setCaption("Descripción:");//Texto que se muestra en dicho campo
        descripcion.setIcon(FontAwesome.FILE_TEXT_O);
        final TextField capacidad = new TextField();//Campo para insertar la capacidad
        capacidad.setCaption("Capacidad:");//Texto que se muestra en dicho campo
        capacidad.setIcon(FontAwesome.USERS);
        Button buttonRegistrar = new Button("Registrar", FontAwesome.CHECK);//Creamo el botón para registrar
        buttonRegistrar.addClickListener(e -> {//Acción del botón
            vaadinRequest.setAttribute("nombre", nombre.getValue());//Añadimos en la petición el valor del campo nombre
            vaadinRequest.setAttribute("descripcion", descripcion.getValue());//Añadimos en la petición el valor del campo descripción
            vaadinRequest.setAttribute("capacidad", capacidad.getValue());//Añadimos en la petición el valor del campo capacidad
            if (comprobarDatos(vaadinRequest, layout) == true) {//Se comprueban los datos, y si son correctos...
                registrarInstalacion(vaadinRequest);//Se envían los datos a registro de instalación
                init(vaadinRequest);//Se lanza el método principal
                //Notificacion de tipo bandeja para notificar la correcta operación.
                Notification.show("Instalación - Nombre: " + nombre.getValue(), "Registrado con éxito",
                        Notification.Type.TRAY_NOTIFICATION);
            }

        });
        Button buttonCancelar = new Button("Cancelar", FontAwesome.CLOSE);//Nuevo botón para cancelar
        buttonCancelar.addClickListener(e -> {//Acción del botón
            init(vaadinRequest);//Se lanza el método principal
        });

        layout.addComponents(nombre, descripcion, capacidad, buttonRegistrar, buttonCancelar);//Añadimos los componentes al layout
        //Le añadimos margen y espciado, para mostrarlo posteriormente.

        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);
    }

    //Exactamente igual que el método de crear instalación, con la peculiaridad de que a este se le pasa el objeto instalación y se prerellenan los campos con sus valores actuales.
    //Ya se ha comentado en el método anterior que realiza cada línea de código.
    protected void editarInstalacion(VaadinRequest vaadinRequest, Instalacion instalacion) {
        final VerticalLayout layout = new VerticalLayout();
        final TextField nombre = new TextField();
        nombre.setCaption("Nombre: ");
        nombre.setValue(instalacion.getNombre());//Insertamos en el campo el valor del atributo nombre
        nombre.setIcon(FontAwesome.TAG);
        final TextField descripcion = new TextField();
        descripcion.setCaption("Descripción: ");
        descripcion.setValue(instalacion.getDescripcion());//Insertamos en el campo el valor del atributo descripción
        descripcion.setIcon(FontAwesome.FILE_TEXT);
        final TextField capacidad = new TextField();
        capacidad.setCaption("Capacidad:");
        capacidad.setValue(instalacion.getCapacidad().toString());//Insertamos en el campo el valor del atributo capacidad
        capacidad.setIcon(FontAwesome.USERS);
        Button buttonRegistrar = new Button("Modificar", FontAwesome.EDIT);
        buttonRegistrar.addClickListener(e -> {
            vaadinRequest.setAttribute("nombre", nombre.getValue());
            vaadinRequest.setAttribute("descripcion", descripcion.getValue());
            vaadinRequest.setAttribute("capacidad", capacidad.getValue());
            if (comprobarDatos(vaadinRequest, layout) == true) {
                modificarInstalacion(vaadinRequest, instalacion);
                init(vaadinRequest);
                //Notificacion de tipo bandeja para notificar la correcta operación.
                Notification.show("Instalación - Nombre: " + nombre.getValue(), "Modificado con éxito",
                        Notification.Type.TRAY_NOTIFICATION);
            }

        });
        Button buttonCancelar = new Button("Cancelar", FontAwesome.ARROW_LEFT);
        buttonCancelar.addClickListener(e -> {
            init(vaadinRequest);
        });

        layout.addComponents(nombre, descripcion, capacidad, buttonRegistrar, buttonCancelar);
        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);

    }

    protected void modificarInstalacion(VaadinRequest vaadinRequest, Instalacion instalacion) {//Método para guardar los datos modificados en memoria, no hay persistencia de momento
        instalacion.setNombre((String) vaadinRequest.getAttribute("nombre"));//Obtenemos de la petición el tipo de instalación y lo introducimos en el campo nombre del objeto instalación
        instalacion.setDescripcion((String) vaadinRequest.getAttribute("descripcion"));//Obtenemos de la petición el tipo de instalación y lo introducimos en el campo descripcion del objeto instalación
        instalacion.setCapacidad(Integer.parseInt((String) vaadinRequest.getAttribute("capacidad")));//Obtenemos de la petición el tipo de instalación y lo introducimos en el campo capacidad del objeto instalación
    }

    protected void registrarInstalacion(VaadinRequest vaadinRequest) {//Método para registrar los datos en memoria, no hay persistencia de momento
        Instalacion instalacion = new Instalacion();//Creamos un nuevo objeto instalación
        instalacion.setNombre((String) vaadinRequest.getAttribute("nombre"));//Obtenemos de la petición el tipo de instalación y lo introducimos en el campo nombre del objeto instalación
        instalacion.setDescripcion((String) vaadinRequest.getAttribute("descripcion"));//Obtenemos de la petición el tipo de instalación y lo introducimos en el campo descripcion del objeto instalación
        instalacion.setCapacidad(Integer.parseInt((String) vaadinRequest.getAttribute("capacidad")));//Obtenemos de la petición el tipo de instalación y lo introducimos en el campo capacidad del objeto instalación
        listaInstalaciones.add(instalacion);//Añadimos el objeto a la lista de instalaciones
    }

    //Método para comprobar los datos introducidos en los formularios
    protected boolean comprobarDatos(VaadinRequest vaadinRequest, VerticalLayout layout) {
        boolean b = false;//Variable booleana inicializada a false
        //Comprobamos si algún campo está vacío
        if ((String) vaadinRequest.getAttribute("nombre") != "" && (String) vaadinRequest.getAttribute("descripcion") != "" && (String) vaadinRequest.getAttribute("capacidad") != "") {
            //Comprobamos si la capacidad es numérica llamando al métdo isInteger
            if (isInteger((String) vaadinRequest.getAttribute("capacidad")) == true) {
                b = true;//Si se satisface todas las condiciones, la variables es true
            } else {//Si la capacidad no es numérica
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

    //Método para comprobar valor numérico
    protected static boolean isInteger(String cadena) {
        try {//Intentamos parsear el la cadena a entero, si se satisface, devolvemos true
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {//De lo contrario, captura la excepción y devolvemos false
            return false;
        }
    }

    @WebServlet(urlPatterns = "/Instalacion/*", name = "Instalacion", asyncSupported = true)
    @VaadinServletConfiguration(ui = InstalacionUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

}
