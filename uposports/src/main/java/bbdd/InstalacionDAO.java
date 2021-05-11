package bbdd;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;

public class InstalacionDAO {

        private static DBCollection instalacionesInit() throws UnknownHostException {
        // Conectar al servidor MongoDB
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        // Conectar a la base de datos
        DB db = mongoClient.getDB("uposports");

        //Acceder coleccion "Abonos"*/
        DBCollection collection = db.getCollection("Instalaciones");

        return collection;
    }
    
    //Método para añadir un documento nuevo a la colección InstalacionDAO
    public static void insertarInstalacion(DBCollection collection, String nombre, String descripcion, int capacidad) {
        BasicDBObject document = new BasicDBObject();//Instanciamos el nuevo documento
        //Insertamos los 3 atributos del nuevo documento Instalacion
        document.append("nombre", nombre);
        document.append("descripcion", descripcion);
        document.append("capacidad", capacidad);
        //Insertamos el documento en la colección        
        collection.insert(document);
        System.out.println("Documento Instalacion insertado: " + document + "\n");
    }

    //Método para mostrar todos los documentos de la colección InstalacionDAO
    public static void mostrarInstalaciones(DBCollection collection) {
        DBCursor cursor = collection.find();// Obtenemos todos los documentos de la coleccion Abonos
        DBObject elemento;
        //Recorrido de todos los elementos de la coleccion InstalacionDAO
        System.out.println("Recorriendo la colección Instalaciones:");
        int i = 0;//Variable iteradora
        while (cursor.hasNext()) {
            i++;//Incrementamos la variable
            elemento = cursor.next();//Guardamos el documento en "elemento"
            System.out.println(String.format("Documento Instalacion_%d leido: %s", i, elemento));
        }
        System.out.println("Fin de la colección Abonos\n");

    }

    //Método para actualizar un campo de tipo String de la colección InstalacionDAO
    public static void actualizarInstalacion(DBCollection collection, DBObject instalacion, String campo, String nuevoValor) {
        BasicDBObject newDocument = new BasicDBObject();//Instanciamos un nuevo documento
        newDocument.append("$set", new BasicDBObject().append(campo, nuevoValor));//Actualizamos el campo con el nuevoValor (ambos atributos se reciben por parámetro)
        collection.update(instalacion, newDocument);//Actualizamos el documento "instalacion" recibido por parámetro con el nuevo campo del documento creado
        System.out.println("Documento Abono actualizado correctamente\n");
    }

    //Método para actualizar un campo de tipo int de la colección InstalacionDAO (método sobrecargado)
    public static void actualizarInstalacion(DBCollection collection, DBObject instalacion, String campo, int nuevoValor) {
        BasicDBObject newDocument = new BasicDBObject();
        newDocument.append("$set", new BasicDBObject().append(campo, nuevoValor));
        collection.update(instalacion, newDocument);
        System.out.println("Documento Abono actualizado correctamente\n");
    }

    //Método para eliminar un documento de la colección Abonos
    public static void eliminarInstalacion(DBCollection collection, String nombre) {
        System.out.println(String.format("Buscando documento Instalacion nombre %s para eliminar...", nombre));
        collection.remove(buscarInstalacion(collection, nombre));//Elimina el documento que recibe del método buscarInstalacion, pasándole la colección y el tipo de Instalacion.
        System.out.println("Documento Abono eliminado\n");
    }

    //Método para buscar un documento abono en la colección Abonos
    public static DBObject buscarInstalacion(DBCollection collection, String nombre) {
        BasicDBObject searchQuery = new BasicDBObject().append("nombre", nombre);//Creamos la query que será los documentos que contengan como atributo "nombre" el que recibe como parámetro el método
        DBCursor cursor = collection.find(searchQuery);//Los elementos que cumplan la condicion de searchQuery se introducen en cursor
        DBObject elemento = cursor.next();//Solamente debemos tener uno, ya que le pasamos el tipo que es nuestro ID.
        System.out.println(String.format("Documento Instalacion encontrado: %s", elemento));
        return elemento;//Devolvemos el documento que será útil para otras operaciones CRUD
    }
}
