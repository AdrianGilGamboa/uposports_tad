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
            listaReserva.add(aux);
            System.out.println(String.format("Reserva %d leido: %s", i, elemento));  //Mostramos por pantalla documento a documento de la coleccion
        }
        return listaReserva;
    }

    public static void eliminaReserva(DB db, DBCursor cursor, String nombre) {
        DBCollection collection = db.getCollection("Reserva"); //Obtenemos la coleccion Reserva de la Base de Datos
        //Creamos el documento que queremos buscar para eliminarlo con el nombre pasado por parámetro
        cursor = collection.find((DBObject) new BasicDBObject().append("nombre", nombre));
        while (cursor.hasNext()) {
            collection.remove(cursor.next()); //Borramos los documentos que coinciden con el parametro nombre
        }
    }

    public static void actualizaHoraReserva(DB db, String nombre, String hora) {
        DBCollection collection = db.getCollection("Reserva"); //Obtenemos la coleccion Reserva de la Base de Datos
        //Actualizacion del valor de un campo
        BasicDBObject newDocument = new BasicDBObject();
        // Indica el atributo hora y su valor a establecer ($set)
        newDocument.append("$set", new BasicDBObject().append("hora", hora));
        // Indica el filtro a usar para aplicar la modificacion
        BasicDBObject searchQuery = new BasicDBObject().append("nombre", nombre);
        collection.update(searchQuery, newDocument);
        System.out.println("Hora de la reserva " + nombre + " actualizada correctamente");
    }

    public static int siguienteID() {

        return 0;

    }
}
