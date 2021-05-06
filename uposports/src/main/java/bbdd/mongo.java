/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bbdd;

import clases.Empleado;
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
public class mongo {
     
       //Creación de los empleados
        Empleado em = new Empleado("33355F", "Manuel", "Molina Fuentes", 3444);
        Empleado em2 = new Empleado("11111D", "Francisco", "Delgado Martin", 683974824);
        Empleado em3 = new Empleado("77777R", "Mariano", "Sanchez Guijón", 666333888);

        //Creación de los materiales
        Material m1 = new Material("Mancuernas", "Mancuernas para hacer ejercicio", 5);
        Material m2 = new Material("Bicicleta estatica", "Bicicleta para realizar cardio", 10);
        Material m3 = new Material("Cinta", "Cinta para correr", 7);

        //Creacion de la ultima coleccion, pagoTarjeta
  
        public void baseDatos(){
        //Accedemos al cliente y a la base de datos
        DBCursor cursor = null;
        try {
            MongoClient mongoClient = new MongoClient("localhost", 27017);

            // Conectar a la base de datos
            DB db = mongoClient.getDB("uposports");
            System.out.println("......Conectado a la base de datos.......");

            System.out.println("\n");
            System.out.println("--------------COLECCIÓN: EMPLEADOS--------------");
            System.out.println("\n");
            //Se crean los documentos con la información correspondiente
            //Documento 1 -> Empleado 1
            BasicDBObject document;
            document = new BasicDBObject();
            document.append("dni", em.getDni());
            document.append("nombre", em.getNombre());
            document.append("apellidos", em.getApellidos());
            document.append("telefono", em.getTelefono());

            //Empleado 2
            BasicDBObject d2 = new BasicDBObject();
            d2.append("dni", em2.getDni());
            d2.append("nombre", em2.getNombre());
            d2.append("apellidos", em2.getApellidos());
            d2.append("telefono", em2.getTelefono());

            //Empleado 3
            BasicDBObject d3 = new BasicDBObject();
            d3.append("dni", em3.getDni());
            d3.append("nombre", em3.getNombre());
            d3.append("apellidos", em3.getApellidos());
            d3.append("telefono", em3.getTelefono());

            //Obtenemos la coleccion
            DBCollection collectionEmpleado = db.getCollection("Empleado");

            System.out.println("--------------INSERTAR INFORMACIÓN--------------");
            System.out.println("\n");

            //INSERTAMOS LA INFORMACIÓN
            //Insertamos el primer empleado
            collectionEmpleado.insert(document);
            System.out.println("Empleado insertado: " + document);

            //Insertamos el segundo empleado
            collectionEmpleado.insert(d2);
            System.out.println("Empleado insertado: " + d2);

            //Insertamos el tercer empleado
            collectionEmpleado.insert(d3);
            System.out.println("Empleado insertado: " + d3);

            System.out.println("\n");
            System.out.println("--------------LEER INFORMACIÓN--------------");
            System.out.println("\n");

            //LEEMOS LA INFORMACIÓN
            cursor = collectionEmpleado.find();
            //Recorrido de todos los elementos de la coleccion
            System.out.println("Recorrido de la coleccion:");
            int i = 0;
            DBObject elemento;
            while (cursor.hasNext()) {
                i++;
                elemento = cursor.next();
                System.out.println(String.format("Empleado %d leido: %s", i, elemento));
            }
            System.out.println("\n");
            System.out.println("--------------BORRAR INFORMACIÓN--------------");
            System.out.println("\n");

            //BORRAR INFORMACIÓN
            collectionEmpleado.remove(d2);
            System.out.println("Empleado borrado con los datos: " + d2);

            System.out.println("\n");
            System.out.println("--------------ACTUALIZAR INFORMACIÓN--------------");
            System.out.println("\n");

            //ACTUALIZAR INOFORMACIÓN
            //Creamos un documento(Empleado) de reemplazo para poder actualizar la información
            BasicDBObject replaceDocument = new BasicDBObject();
            replaceDocument.append("dni", "33333Z");
            replaceDocument.append("nombre", "Daniel");
            replaceDocument.append("apellidos", "Luque Zugaza");
            replaceDocument.append("telefono", "648391625");
            //Actualizamos el documento(Empleado) que queramos con el replaceDocument que hemos creado
            collectionEmpleado.update(document, replaceDocument);
            System.out.println("Empleado a actualizar: " + document);
            System.out.println("Empleado actualizado: " + replaceDocument);

            System.out.println("\n");
            System.out.println("--------------ACTUALIZAR UN CAMPO--------------");
            System.out.println("\n");

            //ACTUALIZAMOS SOLO UN CAMPO, EN ESTE CASO EL DNI
            //Actualizacion del valor de un campo
            BasicDBObject newDocument = new BasicDBObject();
            // Indica el atributo y su valor a establecer ($set)
            newDocument.append("$set", new BasicDBObject().append("dni", "88888A"));
            // Indica el filtro a usar para aplicar la modificacion
            BasicDBObject searchQuery = new BasicDBObject().append("dni", "33333Z");
            collectionEmpleado.update(searchQuery, newDocument);
            System.out.println("Documentos actualizados correctamente" + searchQuery + " " + newDocument);

            System.out.println("\n");
            System.out.println("--------------LEER INFORMACIÓN--------------");
            System.out.println("\n");

            //LEEMOS LA INFORMACIÓN DE LA COLECCION EMPLEADOS OTRA VEZ
            i = 0;
            cursor = collectionEmpleado.find();
            while (cursor.hasNext()) {
                i++;
                elemento = cursor.next();
                System.out.println(String.format("Empleado %d leido: %s", i, elemento));
            }
            System.out.println("\n");
            System.out.println("--------------COLECCION 2: MATERIAL--------------");

            //Vamos a hacer lo mismo para la colección MATERIAL
            //Material1
            BasicDBObject material1;
            material1 = new BasicDBObject();
            material1.append("nombre", m1.getNombre());
            material1.append("descripcion", m1.getDescripcion());
            material1.append("unidades", m1.getUnidades());

            //Material 2
            BasicDBObject material2;
            material2 = new BasicDBObject();
            material2.append("nombre", m2.getNombre());
            material2.append("descripcion", m2.getDescripcion());
            material2.append("unidades", m2.getUnidades());

            //Material 3
            BasicDBObject material3;
            material3 = new BasicDBObject();
            material3.append("nombre", m3.getNombre());
            material3.append("descripcion", m3.getDescripcion());
            material3.append("unidades", m3.getUnidades());

            //Obtenemos la coleccion
            DBCollection collectionMaterial = db.getCollection("Material");
            System.out.println("\n");

            System.out.println("--------------INSERTAR INFORMACIÓN--------------");
            System.out.println("\n");

            //INSERTAMOS LA INFORMACIÓN
            //Insertamos el primer material
            collectionMaterial.insert(material1);
            System.out.println("Material insertado: " + material1);

            //Insertamos el segundo material
            collectionMaterial.insert(material2);
            System.out.println("Material insertado: " + material2);

            //Insertamos el tercer material
            collectionMaterial.insert(material3);
            System.out.println("Material insertado: " + material3);
            System.out.println("\n");

            System.out.println("--------------LEER INFORMACIÓN--------------");
            System.out.println("\n");

            //LEEMOS LA INFORMACIÓN
            cursor = collectionMaterial.find();
            //Recorrido de todos los elementos de la coleccion
            System.out.println("Recorrido de la coleccion:");
            i = 0;
            while (cursor.hasNext()) {
                i++;
                elemento = cursor.next();
                System.out.println(String.format("Material %d leido: %s", i, elemento));
            }
            System.out.println("\n");

            System.out.println("--------------BORRAR INFORMACIÓN--------------");
            System.out.println("\n");

            //BORRAR
            collectionMaterial.remove(material3);
            System.out.println("Material borrado con los datos: " + material3);

            System.out.println("\n");

            System.out.println("--------------ACTUALIZAR INFORMACIÓN--------------");
            System.out.println("\n");

            //ACTUALIZAR
            //ACTUALIZAR INOFORMACIÓN
            //Creamos un documento(Material) de reemplazo para poder actualizar la información
            BasicDBObject replaceMaterial = new BasicDBObject();
            replaceMaterial.append("nombre", "Balón de futbol");
            replaceMaterial.append("descripcion", "Pelota para jugar al fútbol");
            replaceMaterial.append("unidades", 6);
            //Actualizamos el documento(Material) que queramos con el replaceDocument que hemos creado
            collectionMaterial.update(material2, replaceMaterial);
            System.out.println("Material a actualizar: " + material2);
            System.out.println("Material actualizado: " + replaceMaterial);

            System.out.println("\n");

            System.out.println("--------------ACTUALIZAR UN CAMPO(en este caso unidades)--------------");
            System.out.println("\n");

            //ACTUALIZAMOS SOLO UN CAMPO, EN ESTE CASO EL NUMERO DE UNIDADES
            //Actualizacion del valor de un campo
            BasicDBObject newMaterial = new BasicDBObject();
            // Indica el atributo y su valor a establecer ($set)
            newMaterial.append("$set", new BasicDBObject().append("unidades", 12));
            // Indica el filtro a usar para aplicar la modificacion
            BasicDBObject searchQuery2 = new BasicDBObject().append("unidades", 6);
            collectionEmpleado.update(searchQuery, newMaterial);
            System.out.println("Documentos actualizados correctamente: "+ searchQuery + " " + newDocument);

            System.out.println("\n");
            System.out.println("--------------LEER INFORMACIÓN--------------");
            System.out.println("\n");

            //VOLVEMOS A LEER LA INFORMACIÓN
            cursor = collectionMaterial.find();
            //Recorrido de todos los elementos de la coleccion
            System.out.println("Recorrido de la coleccion:");
            i = 0;
            while (cursor.hasNext()) {
                i++;
                elemento = cursor.next();
                System.out.println(String.format("Documento %d leido: %s", i, elemento));
            }
            System.out.println("\n");
  
        } catch (UnknownHostException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
        
