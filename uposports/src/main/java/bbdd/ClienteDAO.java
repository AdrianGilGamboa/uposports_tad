
package bbdd;
import java.net.UnknownHostException;
import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.net.UnknownHostException;
import static javafx.scene.Cursor.cursor;
import javax.swing.text.Document;

public class ClienteDAO {
    //Método estático para la creación de clientes
    public static void creaCliente(DB db,String dni,String nombre, String apellidos, String telefono, String cp){
         //Crea documento
            BasicDBObject document; //CREAMOS un documento tipo BasicDBObject
            document = new BasicDBObject();
            //Le añadimos los campos con los valores pasados por parámetros
            document.append("dni", dni);   
            document.append("nombre", nombre);
            document.append("apellidos", apellidos);
            document.append("telefono", telefono);
            document.append("CP", cp);

            //Obtencion coleccion "Cliente"
            DBCollection collection = db.getCollection("Cliente"); //Obtenemos la coleccion Cliente de la Base de Datos

            //Inserta documento en la coleccion Cliente
            collection.insert(document);
            System.out.println("Cliente insertado: "+document);
    }
    
     public static void consultaClientes(DB db, DBCursor  cursor){
         DBCollection collection = db.getCollection("Cliente"); //Obtenemos la coleccion Cliente de la Base de Datos
         // Obtenemos todos los documentos de la coleccion
            cursor = collection.find();
            //Recorrido de todos los elementos de la coleccion
            System.out.println("Recorrido de la coleccion:");
            int i = 0;
            DBObject elemento;
            while (cursor.hasNext()) {
                i++;
                elemento = cursor.next();
                System.out.println(String.format("Cliente %d leido: %s", i, elemento)); //Mostramos por pantalla documento a documento de la coleccion
            }
     }
     
     public static void eliminaCliente(DB db,DBCursor  cursor, String dni){
         DBCollection collection = db.getCollection("Cliente"); //Obtenemos la coleccion Cliente de la Base de Datos            
            DBObject elemento;
            cursor=collection.find((DBObject) new BasicDBObject().append("dni", dni)); //Obtenemos los documentos que coincidan con el dni pasado por parámetro
            while(cursor.hasNext()){
                collection.remove(cursor.next()); //Eliminamos los documentos que coinciden con el dni pasado por parámetro
            }
     }
     
     public static void actualizaNombreCliente(DB db,String dni,String nombre){
         DBCollection collection = db.getCollection("Cliente"); //Obtenemos la coleccion Cliente de la Base de Datos
         //Actualizacion del valor de un campo
            BasicDBObject newDocument = new BasicDBObject();
            // Indica el atributo nombre y su valor a establecer ($set)
            newDocument.append("$set", new BasicDBObject().append("nombre", nombre));
            // Indica el filtro a usar para aplicar la modificacion
            BasicDBObject searchQuery = new BasicDBObject().append("dni", dni);
            collection.update(searchQuery, newDocument);
            System.out.println("Nombre del cliente actualizado correctamente");
     }
}
