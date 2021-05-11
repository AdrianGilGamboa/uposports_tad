package log;

import log.Broadcaster.BroadcastListener;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

@Theme("mytheme")
@Title("Log")
@PreserveOnRefresh //Si volvemos a cargar la url, no perdemos la sesión
@Push(PushMode.AUTOMATIC) //Envía push en modo automático al navegador (es el por defecto)

public class log extends UI implements BroadcastListener {

    Grid grid = new Grid();//Creamos un grid

    SimpleDateFormat fFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm");//Formateo de fecha que incorporaremos

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        final VerticalLayout layout = new VerticalLayout();//Layout vertical
        Label lTitulo = new Label("<h1 style='text-weight:bold;text-align:center;margin:auto;padding-right: 100px;'>UPOSports</h2>", ContentMode.HTML);
        Label labelInfo = new Label("<h2 style='text-weight:bold;margin:0'>Control de acceso al sistema </h2></br>", ContentMode.HTML);

        //Insertamos las columnas de fecha y usuario
        grid.addColumn("Fecha y Hora", String.class);
        grid.addColumn("Usuario (DNI - Nombre y Apellidos)", String.class);
        grid.setSizeFull();

        Broadcaster.register(this);//Registramos el broadcaster
        layout.addComponents(lTitulo, labelInfo, grid);//Añadimos el grid al layout

        //Añadimos margenes y espaciados, y finalmente se muestra.
        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);
    }

    //En este caso, si recibimos los listeners y se van a mostrar en un grid.
    @Override
    public void receiveBroadcast(final String message) {
        access(new Runnable() {
            @Override
            public void run() {
                Date date = new Date();//Fecha y hora actual
                grid.addRow((String) fFecha.format(date), (String) message);//Se crea una nueva fila la fecha y hora de acceso al sistema y el usuario
            }
        });
    }

    //Método que se quita del registro cuando se elimina el UI
    @Override
    public void detach() {
        Broadcaster.unregister(this);
        super.detach();
    }

    @WebServlet(urlPatterns = "/log/*", name = "Log", asyncSupported = true)
    @VaadinServletConfiguration(ui = log.class, productionMode = false, heartbeatInterval = 10)
    public static class MyUIServlet extends VaadinServlet {
    }
}
