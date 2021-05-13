package bbdd;

import clases.Anunciante;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class AnuncianteDAO {

    private static DBCollection anuncianteInit() throws UnknownHostException {
        // Conectar al servidor MongoDB
        MongoClient mongoClient = new MongoClient("mongodb", 27017);

        // Conectar a la base de datos
        DB db = mongoClient.getDB("uposports");

        //Acceder coleccion "Anunciantes"*/
        DBCollection collection = db.getCollection("Anunciantes");

        return collection;
    }

    //Método para añadir un documento nuevo a la colección Anunciantes
    public static void insertarAnunciante(Anunciante anunciante) throws ParseException, UnknownHostException {
        DBCollection collection = anuncianteInit();
        BasicDBObject document = new BasicDBObject();//Instanciamos el nuevo documento
        //Insertamos los 4 atributos del nuevo documento Anunciante
        document.append("anunciante", anunciante.getAnunciante());
        document.append("precioContrato", anunciante.getPrecioContrato());
        document.append("fechaIni", anunciante.getFechaIni());
        document.append("fechaFin", anunciante.getFechaFin());
        //Insertamos el documento en la colección Anunciantes
        collection.insert(document);
        System.out.println("Documento Anunciante insertado: " + document + "\n");
    }

    //Método para mostrar todos los documentos de la colección Anunciantes
    public static ArrayList<Anunciante> mostrarAnunciantes() throws UnknownHostException {
        ArrayList<Anunciante> listaAnunciantes = new ArrayList<>();
        DBCollection collection = anuncianteInit();
        DBCursor cursor = collection.find();// Obtenemos todos los documentos de la coleccion Abonos
        DBObject elemento;
        //Recorrido de todos los elementos de la coleccion Anunciantes
        System.out.println("Recorriendo la colección Anunciantes:");
        int i = 0;//Variable iteradora
        while (cursor.hasNext()) {
            i++;
            elemento = cursor.next();//Guardamos el documento en "elemento"
            Anunciante anunciante = new Anunciante();
            anunciante.setAnunciante((String) elemento.get("anunciante"));
            anunciante.setPrecioContrato((double) elemento.get("precioContrato"));
            anunciante.setFechaIni((Date) elemento.get("fechaIni"));
            anunciante.setFechaFin((Date) elemento.get("fechaFin"));
            listaAnunciantes.add(anunciante);
            System.out.println(String.format("Documento Anunciante_%d leido: %s", i, elemento));//Se muestra por pantalla el documento
        }
        System.out.println("Fin de la colección Anunciantes\n");
        return listaAnunciantes;

    }

    //Método para actualizar un Anunciante
    public static void actualizarAnunciante(Anunciante nuevo, Anunciante viejo) throws UnknownHostException {
        BasicDBObject newDocument = new BasicDBObject();//Instanciamos un nuevo documento
        BasicDBObject aux = new BasicDBObject();
        newDocument.append("$set", aux.append("anunciante", nuevo.getAnunciante()));
        newDocument.append("$set", aux.append("precioContrato", nuevo.getPrecioContrato()));
        newDocument.append("$set", aux.append("fechaIni", nuevo.getFechaIni()));
        newDocument.append("$set", aux.append("fechaFin", nuevo.getFechaFin()));
        DBObject searchQuery = new BasicDBObject().append("anunciante", viejo.getAnunciante());
        anuncianteInit().update(searchQuery, newDocument);//Actualizamos el documento "instalacion" recibido por parámetro con el nuevo campo del documento creado
        System.out.println("Documento Anunciante actualizado correctamente\n");
    }

    //Método para eliminar un documento de la colección Anunciante
    public static void eliminarAnunciante(Anunciante anunciante) throws UnknownHostException {
        System.out.println(String.format("Buscando documento Abono tipo %s para eliminar...", anunciante.getAnunciante()));
        anuncianteInit().remove(new BasicDBObject().append("anunciante", anunciante.getAnunciante()));//Elimina el documento que recibe del método buscarAbono, pasándole la colección y el anunciante.
        System.out.println("Documento Anunciante eliminado\n");
    }

    //Método para buscar un documento Anunciante en la colección Anunciante
    public static Anunciante buscarAnunciante(String anunciante) throws UnknownHostException {
        DBCollection collection = anuncianteInit();
        BasicDBObject searchQuery = new BasicDBObject().append("anunciante", anunciante);//Creamos la query que será los documentos que contengan como atributo "anunciante" el que recibe como parámetro el método
        DBCursor cursor = collection.find(searchQuery);//Los elementos que cumplan la condicion de searchQuery se introducen en cursor
        DBObject elemento = cursor.next();//Solamente debemos tener uno, ya que le pasamos el tipo que es nuestro ID.
        System.out.println(String.format("Documento Anunciante encontrado: %s", elemento));
        Anunciante aux = new Anunciante();
        aux.setAnunciante((String) elemento.get("anunciante"));
        aux.setPrecioContrato((double) elemento.get("precioContrato"));
        aux.setFechaIni((Date) elemento.get("fechaIni"));
        aux.setFechaFin((Date) elemento.get("fechaFin"));
        return aux; //Devolvemos el Anunciante

    }
}
