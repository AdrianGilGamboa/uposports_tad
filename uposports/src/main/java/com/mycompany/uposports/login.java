package com.mycompany.uposports;

import log.Broadcaster;
import com.vaadin.annotations.Push;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WrappedSession;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("mytheme")
@Title("Login")
@Push(PushMode.AUTOMATIC) //Envía push en modo automático al navegador (es el por defecto)

public class login extends UI implements Broadcaster.BroadcastListener {

    Label errorCampoVacio = new Label("Campo vacío - Inserte un nombre de usuario");//Etiqueta de error

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        WrappedSession session = getSession().getSession();//Creamos una sesión
        if (session.getAttribute("nombreUsuario") != null) {//Si ya tenemos una sesión en el navegador
            mostrarEntidades(session);//mostramos el menú de las entidades
        } else {//Si no hay sesión en el navegador, se muestra el campo para acceder
            final VerticalLayout layout = new VerticalLayout();//Layout vertical

            final TextField name = new TextField();//TextField para introducir nombre de usuario
            name.setCaption("Nombre de usuario:");
            name.setIcon(FontAwesome.USER);//Icono

            Button button = new Button("Acceder", FontAwesome.SIGN_IN);//Botón para guardar el nombre de usuario
            button.addClickListener(e -> {//Acción que realiza el botón de guardar
                if (name.getValue() != "") {
                    session.setAttribute("nombreUsuario", name.getValue());//Incorporamos el nombre de usuario como atributo de la sesion
                    //Broadcaster.register(this);
                    Broadcaster.broadcast(name.getValue());//mandamos como mensaje el nombre de usuario
                    mostrarEntidades(session);//Lanzamos el metodo mostrarEntidades
                } else {//En caso de campo vacío, mostramos 2 tipos de error uno fijo y otro interactivo (para el proyecto final debatiremos este aspecto)
                    if (layout.getComponentIndex(errorCampoVacio) == -1) {//Error fijo en forma de etiqueta, si ya existe no se crea otro

                        layout.addComponentAsFirst(errorCampoVacio);//Se añade al principio de todo
                    }
                    //Notificacion de tipo Warning interactiva para el usuario.
                    Notification.show("Campo vacío", "Inserte un nombre de usuario",
                            Notification.Type.WARNING_MESSAGE);
                }

            });

            layout.addComponents(name, button);//Añadimos los componentes al layout
            //Le metemos el espaciado y margen
            layout.setMargin(true);
            layout.setSpacing(true);

            setContent(layout);//Mostramos el contenido del layout
        }
    }

    //Mostramos los distintos tipos de entidades que tenemos para acceder
    protected void mostrarEntidades(WrappedSession session) {
        final HorizontalLayout layoutEntidades = new HorizontalLayout();//Creamo layout horizontal
        //Creamos 3 botones
        Button buttonAbonos = new Button("Abonos", FontAwesome.MONEY);
        Button buttonInstalaciones = new Button("Instalaciones", FontAwesome.BUILDING);
        Button botonClientes = new Button("Gestionar Clientes"); //Boton para ir a la página de gestión de clientes

        Button buttonLogout = new Button("Cerrar Sesión", FontAwesome.SIGN_OUT);

        buttonAbonos.addClickListener(a -> {//Acción del botón
            getUI().getPage().setLocation("/Abono");//Redirección a página abono
        });

        buttonInstalaciones.addClickListener(a -> {//Acción del botón
            getUI().getPage().setLocation("/Instalacion");//Redirección a página instalación
        });

        botonClientes.addClickListener(e -> {  //Establecemos lo que hace el boton clientes
            getUI().getPage().setLocation("/gestionaCliente"); //Redirige a la clase de gestionar clientes
        });

        buttonLogout.addClickListener(a -> {//Acción del botón logout
            session.invalidate();//Se destruye la sesion
            getUI().getPage().setLocation("/");//Página principal (login)
        });

        //Añadimos los componentes al layout y le ponemos margen y espaciado
        layoutEntidades.addComponents(buttonAbonos, buttonInstalaciones, botonClientes, buttonLogout);
        layoutEntidades.setMargin(true);
        layoutEntidades.setSpacing(true);

        setContent(layoutEntidades);//Mostramos le contenido del layout

    }

    //Se ejecuta el método access para el log que recibe el push (en este caso no realiza nada, se realiza en el log)
    @Override
    public void receiveBroadcast(final String message) {
        access(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    @WebServlet(urlPatterns = "/*", name = "Login", asyncSupported = true)
    @VaadinServletConfiguration(ui = login.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
