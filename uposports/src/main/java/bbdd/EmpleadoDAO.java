/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bbdd;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 *
 * @author manum
 */
public class EmpleadoDAO {
     public static void creaEmpleado(DB db,String dni,String nombre, String apellidos, String telefono){
         //Crea documento
            BasicDBObject document; //CREAMOS un documento tipo BasicDBObject
            document = new BasicDBObject();
            //Le añadimos los campos con los valores pasados por parámetros
            document.append("dni", dni);   
            document.append("nombre", nombre);
            document.append("apellidos", apellidos);
            document.append("telefono", telefono);
          

            //Obtencion coleccion "Cliente"
            DBCollection collection = db.getCollection("Empleado"); //Obtenemos la coleccion Cliente de la Base de Datos

            //Inserta documento en la coleccion Cliente
            collection.insert(document);
            System.out.println("Empleado insertado: "+document);
    }
    
     public static void consultaEmoleados(DB db, DBCursor  cursor){
         DBCollection collection = db.getCollection("Empleado"); //Obtenemos la coleccion Cliente de la Base de Datos
         // Obtenemos todos los documentos de la coleccion
            cursor = collection.find();
            //Recorrido de todos los elementos de la coleccion
            System.out.println("Recorrido de la coleccion:");
            int i = 0;
            DBObject elemento;
            while (cursor.hasNext()) {
                i++;
                elemento = cursor.next();
                System.out.println(String.format("Empleado %d leido: %s", i, elemento)); //Mostramos por pantalla documento a documento de la coleccion
            }
     }
     
     public static void eliminaEmpleado(DB db,DBCursor  cursor, String dni){
         DBCollection collection = db.getCollection("Empleado"); //Obtenemos la coleccion Cliente de la Base de Datos            
            DBObject elemento;
            cursor=collection.find((DBObject) new BasicDBObject().append("dni", dni)); //Obtenemos los documentos que coincidan con el dni pasado por parámetro
            while(cursor.hasNext()){
                collection.remove(cursor.next()); //Eliminamos los documentos que coinciden con el dni pasado por parámetro
            }
     }
     
     public static void actualizaNombreEmpleado(DB db,String dni,String nombre){
         DBCollection collection = db.getCollection("Empleado"); //Obtenemos la coleccion Cliente de la Base de Datos
         //Actualizacion del valor de un campo
            BasicDBObject newDocument = new BasicDBObject();
            // Indica el atributo nombre y su valor a establecer ($set)
            newDocument.append("$set", new BasicDBObject().append("nombre", nombre));
            // Indica el filtro a usar para aplicar la modificacion
            BasicDBObject searchQuery = new BasicDBObject().append("dni", dni);
            collection.update(searchQuery, newDocument);
            System.out.println("Nombre del empleado actualizado correctamente");
     }
    
    
    
}
