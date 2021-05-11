
package bbdd;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;


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
    
    public static void creaReserva(DB db,String nombre,String hora, int dia, int mes,int año){
         //Crea documento
            BasicDBObject document; //CREAMOS un documento tipo BasicDBObject
            document = new BasicDBObject();
            //Le añadimos los campos con los valores pasados por parámetros
            document.append("nombre", nombre);
            document.append("hora", hora);
            document.append("dia", dia);
            document.append("mes", mes);
            document.append("year", año);

            //Obtencion coleccion "Reserva"
            DBCollection collection = db.getCollection("Reserva"); //Obtenemos la coleccion Reserva de la Base de Datos

            //Inserta documento en la coleccion Reserva
            collection.insert(document);
            System.out.println("Reserva insertada: "+document);
    }
    
     public static void consultaReservas(DB db, DBCursor  cursor){
         DBCollection collection = db.getCollection("Reserva"); //Obtenemos la coleccion Reserva de la Base de Datos
         // Obtenemos todos los documentos de la coleccion
            cursor = collection.find();
            //Recorrido de todos los elementos de la coleccion
            System.out.println("Recorrido de la coleccion:");
            int i = 0;
            DBObject elemento;
            while (cursor.hasNext()) {
                i++;
                elemento = cursor.next();
                System.out.println(String.format("Reserva %d leido: %s", i, elemento));  //Mostramos por pantalla documento a documento de la coleccion
            }
     }
     
     public static void eliminaReserva(DB db,DBCursor  cursor, String nombre){
         DBCollection collection = db.getCollection("Reserva"); //Obtenemos la coleccion Reserva de la Base de Datos
         //Creamos el documento que queremos buscar para eliminarlo con el nombre pasado por parámetro
            cursor=collection.find((DBObject) new BasicDBObject().append("nombre", nombre));
            while(cursor.hasNext()){
                collection.remove(cursor.next()); //Borramos los documentos que coinciden con el parametro nombre
            }
     }
     
     public static void actualizaHoraReserva(DB db,String nombre,String hora){
         DBCollection collection = db.getCollection("Reserva"); //Obtenemos la coleccion Reserva de la Base de Datos
         //Actualizacion del valor de un campo
            BasicDBObject newDocument = new BasicDBObject();
            // Indica el atributo hora y su valor a establecer ($set)
            newDocument.append("$set", new BasicDBObject().append("hora", hora));
            // Indica el filtro a usar para aplicar la modificacion
            BasicDBObject searchQuery = new BasicDBObject().append("nombre", nombre);
            collection.update(searchQuery, newDocument);
            System.out.println("Hora de la reserva "+nombre+" actualizada correctamente");
     }
}
