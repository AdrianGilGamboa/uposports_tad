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
import com.vaadin.server.WrappedSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;


@Theme("mytheme")
@Title("Inicio")
@PreserveOnRefresh
public class EmpleadoUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        //Empezamos obteniendo la sesión y creando una lista de empleados para
        //ir mentiendo cuando se vayan creando
        WrappedSession sesion = getSession().getSession();
        Empleado e = new Empleado();
        List<Empleado> lista = new ArrayList<Empleado>();
        final FormLayout form = new FormLayout();
        //Añadimos el Label de bienvenida
        form.addComponent(new Label("Bienvenido: " + (String) sesion.getAttribute("Usuario") + ". ¿Que operación desea realizar?. Si quiere ver la lista de empleados, pulse en ver empleados"));

        //Creamos el boton "Crear empleado" donde implementaremos la funcion de crear un empleado
        Button b = new Button(" Crear Empleado");
        Label aux = new Label();
        b.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                //Añadimos por cada atributo del empleado su textfield
                TextField tf1 = (new TextField("DNI"));
                form.addComponent(tf1);
                TextField tf2 = (new TextField("Nombre"));
                form.addComponent(tf2);
                TextField tf3 = (new TextField("Apellidos"));
                form.addComponent(tf3);
                TextField tf4 = (new TextField("Teléfono"));
                form.addComponent(tf4);
                //Una vez terminado, creamos el boton de añadir
                Button b2 = new Button("Añadir");
                b2.addClickListener(new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        //Con este boton, vamos a guardar al empleado
                        //en la lista de empleados
                        Empleado e = new Empleado();
                        e.setDni(tf1.getValue());
                        e.setNombre(tf2.getValue());
                        e.setApellidos(tf3.getValue());
                        e.setTelefono(tf4.getValue());
                        //con el add añadimos al empleado en la lista
                        lista.add(e);
                        //Mostramos una notificación avisando de que
                        //la creación del empelado se ha realizado correctamente
                        Notification notif = new Notification("Empleado creado correctamente", Notification.Type.HUMANIZED_MESSAGE);
                        notif.show(Page.getCurrent());

                        notif.setDelayMsec(4000);
                        //Eliminamos los componentes de la vista
                        //cuando se ha acabado de interactuar con el formulario
                        //para que sea mas comodo y dinámico el manejo de la página
                        form.removeComponent(tf1);
                        form.removeComponent(tf2);
                        form.removeComponent(tf3);
                        form.removeComponent(tf4);
                        form.removeComponent(b2);

                    }
                });
                form.addComponent(b2);

            }
        });
//Se crea el boton de ver empleados el cual, al pulsarse, imprimirá la lista de todos los 
//empleados disponibles
        Button b3 = new Button("Ver empleados");
        b3.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Label l1 = (new Label("<b>EMPLEADOS DISPONIBLES</b>", ContentMode.HTML));
                form.addComponent(l1);
                //Recorremos la lista de empleados para mostrarlos a todos
                for (Object o : lista) {
                    //Cast del Objeto a la Clase Empleado
                    Empleado e = (Empleado) o;
                    Label l2 = (new Label("Nombre: " + (e.getNombre() + " Apellidos: " + e.getApellidos()) + " DNI: " + e.getDni() + " Teléfono: " + e.getTelefono()));
                    form.addComponent(l2);

                }

            }

        }
        );
        //Este boton elimina un empleado de la lista de empleados
        Button b4 = new Button("Eliminar empleados");
        b4.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Label l1 = (new Label("<b>EMPLEADOS DISPONIBLES PARA BORRAR</b>", ContentMode.HTML));
                form.addComponent(l1);
                for (Object o : lista) {
                    Empleado e = (Empleado) o;
                    // Cast del Objeto a la Clase Empleado
                    Label l2 = new Label("Nombre: " + (e.getNombre() + " Apellidos: " + e.getApellidos()) + " DNI: " + e.getDni() + " Teléfono: " + e.getTelefono());
                    form.addComponent(l2);
                    //Cuando eliminamos, eliminamos tambien tanto al empleado de la vista
                    //como sus diferentes componentes para que la página se vea mas limpia
                    Button b5 = new Button("Eliminar");
                    b5.addClickListener(new Button.ClickListener() {
                        public void buttonClick(Button.ClickEvent event) {
                            lista.remove(e);
                            form.removeComponent(l2);
                            form.removeComponent(b5);
                        }
                    });
                    form.addComponent(b5);

                }

            }

        }
        );

        //Este boton muestra todos los empleados disponibles para ser modificados
        //y modificarlos si se cree conveniente
        Button b6 = new Button("Modificar empleados");
        b6.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                for (Object o : lista) {
                    Label l1 = (new Label("<b>EMPLEADOS DISPONIBLES PARA MODIFICAR</b>", ContentMode.HTML));
                    form.addComponent(l1);
                    //Cast del Objeto a la Clase Empleado
                    Empleado e = (Empleado) o;
                    Label l = (new Label("Nombre: " + (e.getNombre() + " Apellidos: " + e.getApellidos()) + " DNI: " + e.getDni() + " Teléfono: " + e.getTelefono()));
                    form.addComponent(l);
                    //Al darle al boton de modificar, se muestra el formulario
                    Button b7 = new Button("Modificar");
                    b7.addClickListener(new Button.ClickListener() {
                        public void buttonClick(Button.ClickEvent event) {
                            TextField tf1 = (new TextField("DNI"));
                            form.addComponent(tf1);
                            TextField tf2 = (new TextField("Nombre"));
                            form.addComponent(tf2);
                            TextField tf3 = (new TextField("Apellidos"));
                            form.addComponent(tf3);
                            TextField tf4 = (new TextField("Teléfono"));
                            form.addComponent(tf4);
                            form.removeComponent(l);
//Al aceptar la modificación, se reemplaza los datos del empleado anterior por los datos
//introducidos en la modificación y, como anteriormente, se eliminan los componentes
//para un mejor aspecto y manejo de la página
                            Button b8 = new Button("Modificar");
                            b8.addClickListener(new Button.ClickListener() {
                                public void buttonClick(Button.ClickEvent event) {

                                    e.setDni(tf1.getValue());
                                    e.setNombre(tf2.getValue());
                                    e.setApellidos(tf3.getValue());
                                    e.setTelefono(tf4.getValue());
                                    form.removeComponent(tf1);
                                    form.removeComponent(tf2);
                                    form.removeComponent(tf3);
                                    form.removeComponent(tf4);
                                    form.removeComponent(b8);
                                    form.removeComponent(b7);
                                    form.removeComponent(l);
                                }
                            });
                            form.addComponent(b8);

                        }
                    });
                    form.addComponent(b7);

                }
            }

        }
        );
        //Con este boton, cerramos la sesión y volvemos a la página de Login
        Button finSesion = new Button("Cerrar Sesión");
        finSesion.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                sesion.invalidate();
                getUI().getPage().setLocation("/Login");

            }

        }
        );
        form.addComponent(finSesion);
        form.addComponent(b);
        form.addComponent(b3);
        form.addComponent(b4);
        form.addComponent(b6);
        form.setMargin(
                true);
        form.setSpacing(
                true);

        setContent(form);
    }

    @WebServlet(urlPatterns = "/Empleado/*", name = "ServletPrincipal", asyncSupported = true)
    @VaadinServletConfiguration(ui = EmpleadoUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}