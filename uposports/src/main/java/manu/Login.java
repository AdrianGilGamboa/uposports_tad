package manu;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WrappedSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
@Title("Login")
public class Login extends UI implements Broadcaster.BroadcastListener {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Broadcaster.register(this);
        //Se crea el textfield para que se introduzca el nombre de usuario y 
        //se valida para que sea necesario introducir un nombre para continuar
        final FormLayout form = new FormLayout();
        //Se obtiene la sesion 
        WrappedSession sesion = getSession().getSession();
        TextField tf1 = new TextField("Usuario: ");
        tf1.setIcon(FontAwesome.USER);
        tf1.setRequired(true);
        //Añadimos la validacion de requiered
        tf1.addValidator(new NullValidator("Must be given", false));
        form.addComponent(tf1);
        //Creamos el boton de enviar en el que se redireccionará a la pagina de inicio
        Button b = new Button("Enviar");
        b.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                //Se crea la sesion del usuario
                sesion.setAttribute("Usuario", tf1.getValue());
                getUI().getPage().setLocation("/Principal");
                
                //Esta parte pertenece al log para poder enviar el mensaje al log
                Date mydate = new Date();
                //Con este broadcast mandamos el mensaje al log
                Broadcaster.broadcast((String) sesion.getAttribute("Usuario")); // Broadcast the message

                new SimpleDateFormat("yyyy-MM-dd hh:mm").format(mydate);

            }

        });
        form.addComponent(b);
        form.setMargin(true);
        form.setSpacing(true);

        setContent(form);
    }
//Esto pertenece al log
    @Override
    public void receiveBroadcast(final String message) {
        access(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    @WebServlet(urlPatterns = {"/Login/*", "/VAADIN/*"}, name = "ServletLogin", asyncSupported = true)
    @VaadinServletConfiguration(ui = Login.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
