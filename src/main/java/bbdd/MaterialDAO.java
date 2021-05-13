package bbdd;

import clases.Material;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MaterialDAO {

    private static DBCollection materialInit() throws UnknownHostException {
        // Conectar al servidor MongoDB
        MongoClient mongoClient = new MongoClient("mongodb", 27017);

        // Conectar a la base de datos
        DB db = mongoClient.getDB("uposports");

        //Acceder coleccion "Material"*/
        DBCollection collection = db.getCollection("Materiales");

        return collection;
    }

    //Crea un documento Material 
    public static void creaMaterial(Material material) throws UnknownHostException {
        //Crea documento
        DBCollection collection = materialInit();
        BasicDBObject document; //CREAMOS un documento tipo BasicDBObject
        document = new BasicDBObject();
        //Le añadimos los campos con los valores pasados por parámetro 
        document.append("nombre", material.getNombre());
        document.append("descripcion", material.getDescripcion());
        document.append("unidades", material.getUnidades());
        document.append("instalacion", material.getInstalacion().getNombre());

        collection.insert(document);
        System.out.println("Documento Material insertado: " + document + "\n");
        System.out.println("Material insertado: " + document);
    }

    //Obtenemos todos los documentos de la colección Material
    public static ArrayList<Material> consultaMateriales() throws UnknownHostException {
        DBCursor cursor = materialInit().find();// Obtenemos todos los documentos de la coleccion
        ArrayList<Material> listaMateriales = new ArrayList();
        //Recorrido de todos los elementos de la coleccion
        System.out.println("Recorrido de la coleccion:");
        int i = 0;
        DBObject elemento;
        while (cursor.hasNext()) {
            i++;
            elemento = cursor.next();
            Material aux = new Material();
            aux.setNombre((String) elemento.get("nombre"));
            aux.setDescripcion((String) elemento.get("descripcion"));
            aux.setUnidades((Integer) elemento.get("unidades"));
            aux.setInstalacion(InstalacionDAO.buscarInstalacion((String) elemento.get("instalacion")));
            listaMateriales.add(aux);
            System.out.println(String.format("Material %d leido: %s", i, elemento)); //Mostramos por pantalla documento a documento de la coleccion
        }
        return listaMateriales;
    }

    //Elimina un documento Material
    public static void eliminaMaterial(Material m) throws UnknownHostException {
        System.out.println(String.format("Buscando documento Empleado dni %s para eliminar...", m.getNombre()));
        materialInit().remove(new BasicDBObject().append("nombre", m.getNombre()));//Elimina el documento que recibe del método buscarMaterial, pasándole la colección y el tipo de Material.
        System.out.println("Documento Material eliminado\n");
    }

    //Busca un Material por su nombre
    public static Material buscarMaterial(String nombre) throws UnknownHostException {
        BasicDBObject searchQuery = new BasicDBObject().append("nombre", nombre);//Creamos la query que será los documentos que contengan como atributo "nombre" el que recibe como parámetro el método
        DBCursor cursor = materialInit().find(searchQuery);//Los elementos que cumplan la condicion de searchQuery se introducen en cursor
        if (cursor.hasNext()) {
            DBObject elemento = cursor.next();//Solamente debemos tener uno, ya que le pasamos el tipo que es nuestro ID.
            Material aux = new Material();
            aux.setNombre((String) elemento.get("nombre"));
            aux.setDescripcion((String) elemento.get("descripcion"));
            aux.setUnidades((Integer) elemento.get("unidades"));
            aux.setInstalacion(InstalacionDAO.buscarInstalacion((String) elemento.get("instalacion")));
            System.out.println(String.format("Documento Material encontrado: %s", elemento));
            return aux;//Devolvemos el documento que será útil para otras operaciones CRUD
        } else {
            return null;
        }
    }



    //Método para actualizar un documento Material
    public static void actualizarMaterial(Material nuevo, Material viejo) throws UnknownHostException {
        BasicDBObject newDocument = new BasicDBObject();
        BasicDBObject searchQuery = new BasicDBObject().append("nombre", viejo.getNombre());
        BasicDBObject aux = new BasicDBObject();
        newDocument.append("$set", aux.append("nombre", nuevo.getNombre()));
        newDocument.append("$set", aux.append("descripcion", nuevo.getDescripcion()));
        newDocument.append("$set", aux.append("unidades", nuevo.getUnidades()));
        newDocument.append("$set", aux.append("instalacion", nuevo.getInstalacion().getNombre()));
        materialInit().update(searchQuery, newDocument);
        System.out.println("Documento Material actualizado correctamente\n");
    }

    //Actualiza el nombre de la Instalación de los documentos Materiales
    public static void actualizarMaterialInstalacion(String instalacion, Material viejo) throws UnknownHostException {
        BasicDBObject newDocument = new BasicDBObject();
        BasicDBObject searchQuery = new BasicDBObject().append("nombre", viejo.getNombre());
        BasicDBObject aux = new BasicDBObject();
        newDocument.append("$set", aux.append("nombre", viejo.getNombre()));
        newDocument.append("$set", aux.append("descripcion", viejo.getDescripcion()));
        newDocument.append("$set", aux.append("unidades", viejo.getUnidades()));
        newDocument.append("$set", aux.append("instalacion", instalacion));
        materialInit().update(searchQuery, newDocument);
        System.out.println("Documento Material actualizado correctamente\n");
    }
}
