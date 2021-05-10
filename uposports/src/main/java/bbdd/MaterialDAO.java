/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bbdd;

import static bbdd.EmpleadoDAO.buscarEmpleado;
import clases.Material;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;

/**
 *
 * @author manum
 */
public class MaterialDAO {
    
     private static DBCollection materialInit() throws UnknownHostException{
                     // Conectar al servidor MongoDB
            MongoClient mongoClient = new MongoClient("localhost", 27017);

            // Conectar a la base de datos
            DB db = mongoClient.getDB("uposports");

            //Acceder coleccion "Abonos"*/
            DBCollection collection = db.getCollection("materiales");
            
            return collection;
    }
     public static void creaMaterial(Material material) throws UnknownHostException{
         //Crea documento
            DBCollection collection = materialInit();
            BasicDBObject document; //CREAMOS un documento tipo BasicDBObject
            document = new BasicDBObject();
            //Le añadimos los campos con los valores pasados por parámetro 
            document.append("nombre", material.getNombre());
            document.append("descripcion", material.getDescripcion());
            document.append("unidades", material.getUnidades());
          

            //Obtencion coleccion "Cliente"
           collection.insert(document);
        System.out.println("Documento Material insertado: " + document + "\n");
            System.out.println("Material insertado: "+document);
    }
    
     public static void consultaMaterial(DB db, DBCursor  cursor){
         DBCollection collection = db.getCollection("Material"); //Obtenemos la coleccion Cliente de la Base de Datos
         // Obtenemos todos los documentos de la coleccion
            cursor = collection.find();
            //Recorrido de todos los elementos de la coleccion
            System.out.println("Recorrido de la coleccion:");
            int i = 0;
            DBObject elemento;
            while (cursor.hasNext()) {
                i++;
                elemento = cursor.next();
                System.out.println(String.format("Material %d leido: %s", i, elemento)); //Mostramos por pantalla documento a documento de la coleccion
            }
     }
     
     public static void eliminaMaterial(DBCollection collection, String nombre){
            System.out.println(String.format("Buscando documento Empleado dni %s para eliminar...", nombre));
        collection.remove(buscarMaterial(collection, nombre));//Elimina el documento que recibe del método buscarAbono, pasándole la colección y el tipo de Abono.
        System.out.println("Documento Material eliminado\n");
     }
    
      public static DBObject buscarMaterial(DBCollection collection, String nombre) {
        BasicDBObject searchQuery = new BasicDBObject().append("nombre", nombre);//Creamos la query que será los documentos que contengan como atributo "tipo" el que recibe como parámetro el método
        DBCursor cursor = collection.find(searchQuery);//Los elementos que cumplan la condicion de searchQuery se introducen en cursor
        DBObject elemento = cursor.next();//Solamente debemos tener uno, ya que le pasamos el tipo que es nuestro ID.
        System.out.println(String.format("Documento Material encontrado: %s", elemento));
        return elemento;//Devolvemos el documento que será útil para otras operaciones CRUD
    }
      
      public static void actualizarMaterial(DBCollection collection, DBObject material, String campo, String nuevoValor) {
        BasicDBObject newDocument = new BasicDBObject();//Instanciamos un nuevo documento
        newDocument.append("$set", new BasicDBObject().append(campo, nuevoValor));//Actualizamos el campo con el nuevoValor (ambos atributos se reciben por parámetro)
        collection.update(material, newDocument);//Actualizamos el documento "abono" recibido por parámetro con el nuevo campo del documento creado
        System.out.println("Documento Empleado actualizado correctamente\n");
    }

    //Método para actualizar un campo de tipo int de la colección AbonosDAO (método sobrecargado)
    public static void actualizarMaterial(DBCollection collection, DBObject material, String campo, int nuevoValor) {
        BasicDBObject newDocument = new BasicDBObject();
        newDocument.append("$set", new BasicDBObject().append(campo, nuevoValor));
        collection.update(material, newDocument);
        System.out.println("Documento Empleados actualizado correctamente\n");
    }
}
