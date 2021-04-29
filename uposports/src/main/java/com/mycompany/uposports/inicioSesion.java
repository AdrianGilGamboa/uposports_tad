package com.mycompany.uposports;

import com.vaadin.annotations.Push;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WrappedSession;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.atmosphere.cpr.Broadcaster;

@Theme("mytheme")
@Push(PushMode.AUTOMATIC)
public class inicioSesion extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        WrappedSession session = getSession().getSession();    //Cremos el atributo para la sesion     
        final VerticalLayout layout = new VerticalLayout();  //Creamos el layout principal donde se incluirá todos los elementos de la`página      
        final TextField name = new TextField();     //Creamos el campo de texto donde el usuario introducirá su nombre
        name.setCaption("Nombre de usuario:");  
        name.setIcon(FontAwesome.USER);         

        Button button = new Button("Iniciar Sesión");   //Creamos el botón para que el usuario inicie sesión            
        layout.addComponents(name, button); //Añadimos los componentes anteriores al layout
        layout.setMargin(true);
        layout.setSpacing(true);        
        setContent(layout); //Añadimos el layout a la pagina
        
        button.addClickListener( e -> {   //Aquí definiremos lo que hace el boton
            session.setAttribute("nombreUsuario", name.getValue()); //Establecemos el atributo de sesion 
            Date myDate = new Date();
            getUI().getPage().setLocation("/menu"); //Redireccion a la clase menu
            
            
        });  
    }

    @WebServlet(urlPatterns = {"/*","/VAADIN/*"}, name = "inicioSesionServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = inicioSesion.class, productionMode = false)
    public static class inicioSesionServlet extends VaadinServlet {
    }
}
