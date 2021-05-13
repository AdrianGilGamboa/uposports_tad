package bbdd;

import clases.Abono;
import clases.Cliente;
import clases.Reserva;
import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ClienteDAO {

    private static DBCollection clienteInit() throws UnknownHostException {
        MongoClient mongoClient = new MongoClient("mongodb", 27017);
        // Conectar a la base de datos
        DB db = mongoClient.getDB("uposports");
        DBCollection collection = db.getCollection("Clientes");
        return collection;
    }

    //Método estático para la creación de clientes   
    public static void creaCliente(Cliente c) throws UnknownHostException {
        //Crea documento
        BasicDBObject document; //CREAMOS un documento tipo BasicDBObject
        document = new BasicDBObject();
        //Le añadimos los campos con los valores pasados por parámetros
        document.append("dni", c.getDni());
        document.append("nombre", c.getNombre());
        document.append("apellidos", c.getApellidos());
        document.append("telefono", c.getTelefono());
        document.append("CP", c.getCodigoPostal());
        document.append("abono", c.getAbono().getTipo());
        //Inserta documento en la coleccion Cliente
        clienteInit().insert(document);
        System.out.println("Cliente insertado: " + document);
    }

    //Método para mostrar todos los documentos de la colección Clientes
    public static ArrayList<Cliente> consultaClientes() throws UnknownHostException {
        ArrayList listaClientes = new ArrayList();
        DBCursor cursor = null;
        // Obtenemos todos los documentos de la coleccion
        cursor = clienteInit().find();
        //Recorrido de todos los elementos de la coleccion
        System.out.println("Recorrido de la coleccion:");
        int i = 0;
        DBObject elemento;
        while (cursor.hasNext()) {
            i++;
            Cliente aux = new Cliente();
            elemento = cursor.next();
            aux.setNombre((String) elemento.get("nombre"));
            aux.setApellidos((String) elemento.get("apellidos"));
            aux.setDni((String) elemento.get("dni"));
            aux.setTelefono((String) elemento.get("telefono"));
            aux.setCodigoPostal((String) elemento.get("CP"));
            aux.setAbono(AbonoDAO.buscarAbono((String) elemento.get("abono")));
            listaClientes.add(aux);
        }
        return listaClientes;
    }

    //Eliminar el documento Cliente que recibe como parámetro
    public static void eliminaCliente(Cliente c) throws UnknownHostException {
        DBCursor cursor = null;
        DBObject elemento;
        cursor = clienteInit().find((DBObject) new BasicDBObject().append("dni", c.getDni())); //Obtenemos los documentos que coincidan con el dni del Cliente
        while (cursor.hasNext()) {
            clienteInit().remove(cursor.next()); //Eliminamos los documentos que coinciden con el dni pasado por parámetro
        }
    }

    //Actualiza el documento cliente que recibe como parámetro
    public static void actualizaCliente(Cliente c, Cliente viejo) throws UnknownHostException {
        //Actualizacion del valor de un campo
        BasicDBObject newDocument = new BasicDBObject();
        BasicDBObject aux = new BasicDBObject();

        // Indica el atributo nombre y su valor a establecer ($set)
        newDocument.append("$set", aux.append("telefono", c.getTelefono()));
        newDocument.append("$set", aux.append("nombre", c.getNombre()));
        newDocument.append("$set", aux.append("apellidos", c.getApellidos()));
        newDocument.append("$set", aux.append("dni", c.getDni()));
        newDocument.append("$set", aux.append("CP", c.getCodigoPostal()));
        newDocument.append("$set", aux.append("abono", c.getAbono().getTipo()));
        // Indica el filtro a usar para aplicar la modificacion
        DBObject searchQuery = new BasicDBObject().append("dni", viejo.getDni());
        //Si existe alguna reserva para este cliente, se le modifica el dni de la reserva
        if (!viejo.getDni().equals(c.getDni())) {
            ArrayList<Reserva> listaReservas = ReservaDAO.consultaReservas();
            if (listaReservas.size() > 0) {
                for (int i = 0; i < listaReservas.size(); i++) {
                    if (listaReservas.get(i).getCliente().getDni().equals(viejo.getDni())) {
                        ReservaDAO.actualizaReservaDNI(c.getDni(), listaReservas.get(i));
                    }
                }
            }
        }
        clienteInit().update(searchQuery, newDocument);
        System.out.println("Nombre del cliente actualizado correctamente");
    }

    //Actualizar el tipo de los abonos de los clientes, cuando se modifique el tipo de un abono
    public static void actualizaClienteAbono(String abono, Cliente viejo) throws UnknownHostException {
        //Actualizacion del valor de un campo
        BasicDBObject newDocument = new BasicDBObject();
        BasicDBObject aux = new BasicDBObject();

        // Indica el atributo nombre y su valor a establecer ($set)
        newDocument.append("$set", aux.append("telefono", viejo.getTelefono()));
        newDocument.append("$set", aux.append("nombre", viejo.getNombre()));
        newDocument.append("$set", aux.append("apellidos", viejo.getApellidos()));
        newDocument.append("$set", aux.append("dni", viejo.getDni()));
        newDocument.append("$set", aux.append("CP", viejo.getCodigoPostal()));
        newDocument.append("$set", aux.append("abono", abono));
        // Indica el filtro a usar para aplicar la modificacion
        DBObject searchQuery = new BasicDBObject().append("dni", viejo.getDni());
        clienteInit().update(searchQuery, newDocument);
        System.out.println("Nombre del cliente actualizado correctamente");
    }

    //Método para comprobar si un cliente tiene un abono
    public static boolean clientesConAbono(Abono a) throws UnknownHostException {
        BasicDBObject searchQuery = new BasicDBObject().append("abono", a.getTipo());//Creamos la query que será los documentos que contengan como atributo "dni" el que recibe como parámetro el método
        DBCursor cursor = clienteInit().find(searchQuery);//Los elementos que cumplan la condicion de searchQuery se introducen en cursor        
        if (cursor.hasNext()) {
            return true;
        } else {
            return false;
        }
    }

    //Método para buscar un Cliente por su dni
    public static Cliente buscarCliente(String dni) throws UnknownHostException {
        DBCollection collection = clienteInit();
        BasicDBObject searchQuery = new BasicDBObject().append("dni", dni);//Creamos la query que será los documentos que contengan como atributo "dni" el que recibe como parámetro el método
        DBCursor cursor = collection.find(searchQuery);//Los elementos que cumplan la condicion de searchQuery se introducen en cursor
        if (cursor.hasNext()) {
            DBObject elemento = cursor.next();//Solamente debemos tener uno, ya que le pasamos el dni que es nuestro ID.
            System.out.println(String.format("Cliente encontrado: %s", elemento));
            Cliente aux = new Cliente();
            aux.setNombre((String) elemento.get("nombre"));
            aux.setApellidos((String) elemento.get("apellidos"));
            aux.setDni((String) elemento.get("dni"));
            aux.setTelefono((String) elemento.get("telefono"));
            aux.setCodigoPostal((String) elemento.get("CP"));
            aux.setAbono(AbonoDAO.buscarAbono((String) elemento.get("abono")));
            return aux; //Devolvemos el cliente
        } else {
            return null;
        }

    }
}
