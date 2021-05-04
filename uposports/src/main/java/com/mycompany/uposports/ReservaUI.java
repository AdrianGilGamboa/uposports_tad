package com.mycompany.uposports;

import clases.Reserva;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WrappedSession;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.servlet.annotation.WebServlet;
import java.util.*;

@Theme("mytheme")
public class ReservaUI extends UI {

    public static ArrayList<Reserva> listaReservas = new ArrayList(); //LISTA DONDE ESTARÁN ALMACENADOS TODAS LAS RESERVAS QUE CREEMOS
    public VerticalLayout layout = new VerticalLayout(); //LAYOUT PRINCIPAL

    @Override
    protected void init(VaadinRequest request) {
        layout.removeAllComponents();
        //RECUPERAMOS LA SESION Y SI NO HAY SESION NOS REDIRIGE A LA PÁGINA DE INICIO DE SESIÓN
        WrappedSession session = getSession().getSession();
        if (session.getAttribute("nombreUsuario") == null) {
            getUI().getPage().setLocation("/inicioSesion");
        }

        //CREAMOS UNA TABLA DONDE APARECERÁ LA LISTA DE CLIENTES
        Table tabla = new Table();
        tabla.addContainerProperty("Fecha Reserva", String.class, null);
        tabla.addContainerProperty("Hora Inicio", String.class, null);
        tabla.addContainerProperty("Hora Fin", String.class, null);
        tabla.addContainerProperty("Eliminar", Button.class, null);
        tabla.addContainerProperty("Editar", Button.class, null);
        tabla.setWidth("50%"); //ESTABLECEMOS EL ANCHO DE LA TABLA
        Button botonAdd = new Button("Añadir Reserva"); //BOTÓN PARA AÑADIR RESERVA 
        layout.addComponent(botonAdd);
        Iterator it = listaReservas.iterator();
        int i = 0;
        //BUCLE PARA AÑADIR TODAS LAS RESERVA A LA TABLA
        while (it.hasNext()) {
            Button eliminar = new Button("Eliminar");
            Button editar = new Button("Editar");
            Reserva aux = (Reserva) it.next();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
            DateFormat horaFormat = new SimpleDateFormat("HH:mm");
            tabla.addItem(new Object[]{dateFormat.format(aux.getInicioReserva()), horaFormat.format(aux.getInicioReserva()), horaFormat.format(aux.getFinReserva()), eliminar, editar}, i);
            i++;
            eliminar.addClickListener(e -> {  //AÑADIMOS EL BOTON DE ELIMINAR POR CADA Reserva
                listaReservas.remove(aux); //Elimina la reserva de la lista
                init(request);
            });

            editar.addClickListener(e -> { //AÑADIMOS EL BOTON DE EDITAR POR CADA CLIENTE
                editarReserva(aux); //EJECUTA LA FUNCION EDITARCLIENTE
            });
        }
        Button volver = new Button("Volver", FontAwesome.ARROW_LEFT);
        layout.addComponents(tabla, volver);
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);

        volver.addClickListener(e -> {
            getUI().getPage().setLocation("/menu"); //REDIRECCIONA A LA CLASE MENU
        });

        botonAdd.addClickListener(e -> {
            creaReserva();
        });

    }

    public static void addReserva(Reserva r) {     //METODO QUE AÑADE UNA RESERVA A LA LISTA
        listaReservas.add(r);
    }
    public void creaReserva(){
         //CREAMOS UN FORMULARIO PARA QUE EL USUARIO INTRODUZCA LOS DATOS DE LA RESERVA A CREAR
        layout.removeAllComponents();
        HorizontalLayout datos = new HorizontalLayout();
        Label l = new Label("<h2 style='text-weight:bold;'>Nueva Reserva</h2>",ContentMode.HTML);
        layout.addComponent(l);
        DateField inicioReserva = new DateField("Introduzca el inicio de la reserva");
        inicioReserva.setResolution(Resolution.MINUTE);
        DateField finReserva = new DateField("Introduzca el fin de la reserva");
        finReserva.setResolution(Resolution.MINUTE);
        datos.addComponents(inicioReserva,finReserva);
        datos.setMargin(true);
        datos.setSpacing(true);
        Button enviar = new Button("Enviar",FontAwesome.SEND);
        
        enviar.addClickListener(e -> { //UNA VEZ PULSADO EL BOTÓN SE CREA LA RESERVA Y LA AÑADIMOS A LA LISTA
            Reserva aux=new Reserva();
            aux.setInicioReserva(inicioReserva.getValue());
            aux.setFinReserva(finReserva.getValue());
            addReserva(aux);
            getUI().getPage().setLocation("/gestionaReserva");
        });
        Button volver = new Button("Volver",FontAwesome.ARROW_LEFT);
        volver.addClickListener( e -> {
           getUI().getPage().setLocation("/gestionaReserva");
        });
        HorizontalLayout horiz = new HorizontalLayout();
        horiz.addComponents(volver,enviar);
        horiz.setSpacing(true);
        horiz.setMargin(true);
        layout.addComponents(datos,horiz);
        layout.setMargin(true);
        layout.setSpacing(true);        
        setContent(layout);
    }

    public void editarReserva(Reserva r) {
        layout.removeAllComponents();
        //CREAMOS UN FORMULARIO PARA PODER EDITAR LA RESERVA
        HorizontalLayout datos = new HorizontalLayout();
        Label l = new Label("<h2>Editar Cliente</h2>", ContentMode.HTML);
        layout.addComponent(l);

        DateField inicioReserva = new DateField("Introduzca el inicio de la reserva");
        inicioReserva.setResolution(Resolution.MINUTE);
        inicioReserva.setValue(r.getInicioReserva());
        DateField finReserva = new DateField("Introduzca el fin de la reserva");
        finReserva.setResolution(Resolution.MINUTE);
        finReserva.setValue(r.getFinReserva());
        datos.addComponents(inicioReserva, finReserva);
        datos.setMargin(true);
        datos.setSpacing(true);
        Button enviar = new Button("Enviar", FontAwesome.SEND);
        //EDITAMOS TODOS LOS CAMPOS QUE HAYA MODIFICADO EL USUARIO Y VOLVEMOS A INSERTAR EL CLIENTE EN LA LISTA
        enviar.addClickListener(e -> {
            Reserva aux = new Reserva();
            aux.setInicioReserva(inicioReserva.getValue());
            aux.setFinReserva(finReserva.getValue());
            listaReservas.remove(r);
            addReserva(aux);
            getUI().getPage().setLocation("/gestionaReserva");
        });
        Button volver = new Button("Volver", FontAwesome.ARROW_LEFT);
        //REDIRECCIONA A LA CLASE ReservaUI
        volver.addClickListener(e -> {
            getUI().getPage().setLocation("/gestionaReserva");
        });

        HorizontalLayout horiz = new HorizontalLayout();
        horiz.addComponents(volver, enviar);

        horiz.setSpacing(true);
        horiz.setMargin(true);
        layout.addComponents(datos, horiz);
        layout.setMargin(true);
        layout.setSpacing(true);
    }

    @WebServlet(urlPatterns = {"/Reserva/*"}, name = "gestionaReservaServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = ReservaUI.class, productionMode = false)
    public static class gestionaReservaServlet extends VaadinServlet {

    }

}
