package bbdd;

import clases.Reserva;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;

public class ReservaDAO {

    private static DBCollection reservasInit() throws UnknownHostException {
        // Conectar al servidor MongoDB
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        // Conectar a la base de datos
        DB db = mongoClient.getDB("uposports");

        //Acceder coleccion "Abonos"*/
        DBCollection collection = db.getCollection("Reservas");

        return collection;
    }

    public static void creaReserva(Reserva r) throws UnknownHostException {
        //Crea documento
        BasicDBObject document; //CREAMOS un documento tipo BasicDBObject
        document = new BasicDBObject();
        //Le añadimos los campos con los valores pasados por parámetros
        document.append("id", r.getId_reserva());
        document.append("inicio", r.getInicioReserva());
        document.append("fin", r.getFinReserva());
        document.append("cliente", r.getCliente().getDni());
        document.append("instalacion", r.getInstalacion().getNombre());

        //Inserta documento en la coleccion Reserva
        reservasInit().insert(document);
        System.out.println("Reserva insertada: " + document);
    }

    public static ArrayList<Reserva> consultaReservas() throws UnknownHostException {
        // Obtenemos todos los documentos de la coleccion
        DBCursor cursor = reservasInit().find();
        ArrayList<Reserva> listaReserva = new ArrayList();
        //Recorrido de todos los elementos de la coleccion
        System.out.println("Recorrido de la coleccion:");
        int i = 0;
        DBObject elemento;
        while (cursor.hasNext()) {
            i++;
            elemento = cursor.next();
            Reserva aux = new Reserva();
            aux.setId_reserva((int) elemento.get("id"));
            aux.setInicioReserva((Date) elemento.get("inicio"));
            aux.setFinReserva((Date) elemento.get("fin"));
            aux.setCliente(ClienteDAO.buscarCliente((String) elemento.get("cliente")));
            aux.setInstalacion(InstalacionDAO.buscarInstalacion((String) elemento.get("instalacion")));
            listaReserva.add(aux);
            System.out.println(String.format("Reserva %d leido: %s", i, elemento));  //Mostramos por pantalla documento a documento de la coleccion
        }
        return listaReserva;
    }

    public static void eliminaReserva(Reserva r) throws UnknownHostException {
        //Creamos el documento que queremos buscar para eliminarlo con el nombre pasado por parámetro
        reservasInit().remove(new BasicDBObject().append("id", r.getId_reserva()));//Elimina el documento que recibe del método buscarAbono, pasándole la colección y el anunciante.
    }

    public static void actualizaReserva(Reserva nueva, Reserva vieja) throws UnknownHostException {
        //Actualizacion del valor de un campo
        BasicDBObject newDocument = new BasicDBObject();
        BasicDBObject aux = new BasicDBObject();
        // Indica el atributo hora y su valor a establecer ($set)
        newDocument.append("$set", aux.append("id", nueva.getId_reserva()));
        newDocument.append("$set", aux.append("inicio", nueva.getInicioReserva()));
        newDocument.append("$set", aux.append("fin", nueva.getFinReserva()));
        newDocument.append("$set", aux.append("cliente", nueva.getCliente().getDni()));
        newDocument.append("$set", aux.append("instalacion", nueva.getInstalacion().getNombre()));
        // Indica el filtro a usar para aplicar la modificacion
        BasicDBObject searchQuery = new BasicDBObject().append("id", vieja.getId_reserva());
        reservasInit().update(searchQuery, newDocument);
    }

    public static Reserva buscarReserva(int id) throws UnknownHostException {
        BasicDBObject searchQuery = new BasicDBObject().append("id", id);
        DBCursor cursor = reservasInit().find(searchQuery);//Los elementos que cumplan la condicion de searchQuery se introducen en cursor
        if (cursor.hasNext()) {
            DBObject elemento = cursor.next();//Solamente debemos tener uno, ya que le pasamos el tipo que es nuestro ID.
            Reserva aux = new Reserva();
            aux.setId_reserva((int) elemento.get("id"));
            aux.setInicioReserva((Date) elemento.get("inicio"));
            aux.setFinReserva((Date) elemento.get("fin"));
            aux.setCliente(ClienteDAO.buscarCliente((String) elemento.get("cliente")));
            aux.setInstalacion(InstalacionDAO.buscarInstalacion((String) elemento.get("instalacion")));
            return aux;
        } else {
            return null;
        }
    }

    public static int siguienteID() throws UnknownHostException {
        // Obtenemos todos los documentos de la coleccion
        DBCursor cursor = reservasInit().find();
        ArrayList<Reserva> listaReserva = new ArrayList();
        //Recorrido de todos los elementos de la coleccion
        int i = 0;
        DBObject elemento;
        int max = 0;
        if (!cursor.hasNext()) {
            return 1;
        } else {
            while (cursor.hasNext()) {
                elemento = cursor.next();
                if (max < (int) elemento.get("id")) {
                    max = (int) elemento.get("id");
                }
            }
            return max+1;
        }
    }
}
