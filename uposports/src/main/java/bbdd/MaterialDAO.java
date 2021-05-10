/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bbdd;

import clases.Material;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 *
 * @author manum
 */
public class MaterialDAO {
     public static void creaMaterial(DB db,String nombre, String descripcion, int unidades){
         //Crea documento
            BasicDBObject document; //CREAMOS un documento tipo BasicDBObject
            document = new BasicDBObject();
            //Le añadimos los campos con los valores pasados por parámetro 
            document.append("nombre", nombre);
            document.append("descripcion", descripcion);
            document.append("unidades", unidades);
          

            //Obtencion coleccion "Cliente"
            DBCollection collection = db.getCollection("Nateruak"); //Obtenemos la coleccion Cliente de la Base de Datos

            //Inserta documento en la coleccion Cliente
            collection.insert(document);
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
     
     public static void eliminaMaterial(DB db,DBCursor  cursor, String nombre){
         DBCollection collection = db.getCollection("Material"); //Obtenemos la coleccion Cliente de la Base de Datos            
            DBObject elemento;
            cursor=collection.find((DBObject) new BasicDBObject().append("nombre", nombre)); //Obtenemos los documentos que coincidan con el dni pasado por parámetro
            while(cursor.hasNext()){
                collection.remove(cursor.next()); //Eliminamos los documentos que coinciden con el dni pasado por parámetro
            }
     }
     
     public static void actualizaNombreMaterial(DB db,BasicDBObject material){
         DBCollection collection = db.getCollection("Material"); //Obtenemos la coleccion Cliente de la Base de Datos
            BasicDBObject replaceMaterial = new BasicDBObject();
            replaceMaterial.append("nombre", "Balón de futbol");
            replaceMaterial.append("descripcion", "Pelota para jugar al fútbol");
            replaceMaterial.append("unidades", 6);
            //Actualizamos el documento(Material) que queramos con el replaceDocument que hemos creado
            collection.update(material, replaceMaterial);
            System.out.println("Material a actualizar: " + material);
            System.out.println("Material actualizado: " + replaceMaterial);
    
}
}
