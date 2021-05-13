package bbdd;

import clases.Instalacion;
import clases.Material;
import clases.Reserva;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class InstalacionDAO {

    private static DBCollection instalacionesInit() throws UnknownHostException {

// Conectar al servidor MongoDB
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        // Conectar a la base de datos
        DB db = mongoClient.getDB("uposports");

        //Acceder coleccion "Instalaciones"*/
        DBCollection collection = db.getCollection("Instalaciones");

        return collection;
    }

    //Método para añadir un documento nuevo a la colección Instalacion
    public static void insertarInstalacion(Instalacion i) throws UnknownHostException {
        BasicDBObject document = new BasicDBObject();//Instanciamos el nuevo documento
        //Insertamos los 3 atributos del nuevo documento Instalacion
        document.append("nombre", i.getNombre());
        document.append("descripcion", i.getDescripcion());
        document.append("capacidad", i.getCapacidad());
        //Insertamos el documento en la colección        
        instalacionesInit().insert(document);
        System.out.println("Documento Instalacion insertado: " + document + "\n");
    }

    //Método para mostrar todos los documentos de la colección Instalacion
    public static ArrayList<Instalacion> mostrarInstalaciones() throws UnknownHostException {
        DBCursor cursor = instalacionesInit().find();// Obtenemos todos los documentos de la coleccion Instalacion
        DBObject elemento;
        ArrayList<Instalacion> listaInstalaciones = new ArrayList();
        //Recorrido de todos los elementos de la coleccion Instalacion
        System.out.println("Recorriendo la colección Instalaciones:");
        int i = 0;//Variable iteradora
        while (cursor.hasNext()) {
            i++;//Incrementamos la variable
            elemento = cursor.next();//Guardamos el documento en "elemento"
            Instalacion aux = new Instalacion();
            aux.setNombre((String) elemento.get("nombre"));
            aux.setDescripcion((String) elemento.get("descripcion"));
            aux.setCapacidad((Integer) elemento.get("capacidad"));
            listaInstalaciones.add(aux);
            System.out.println(String.format("Documento Instalacion_%d leido: %s", i, elemento));
        }
        System.out.println("Fin de la colección Instalación\n");
        return listaInstalaciones;

    }


    //Método para actualizar documento Instalación
    public static void actualizarInstalacion(Instalacion nueva, Instalacion vieja) throws UnknownHostException {
        BasicDBObject newDocument = new BasicDBObject();
        BasicDBObject aux = new BasicDBObject();
        BasicDBObject query = new BasicDBObject("nombre", vieja.getNombre());
        newDocument.append("$set", aux.append("nombre", nueva.getNombre()));
        newDocument.append("$set", aux.append("descripcion", nueva.getDescripcion()));
        newDocument.append("$set", aux.append("capacidad", nueva.getCapacidad()));
        //Si se ha modificado el nombre de la instalación, se recorre las reservas y materiales para modificar ese valor a los documenos que contengan la instalación
        if (!vieja.getNombre().equals(nueva.getNombre())) {
            ArrayList<Reserva> listaReservas = ReservaDAO.consultaReservas();
            ArrayList<Material> listaMateriales = MaterialDAO.consultaMateriales();
            if (listaReservas.size() > 0) {
                for (int i = 0; i < listaReservas.size(); i++) {
                    if (listaReservas.get(i).getInstalacion().getNombre().equals(vieja.getNombre())) {
                        ReservaDAO.actualizaReservaInstalacion(nueva.getNombre(), listaReservas.get(i));
                    }
                }
            }
            if (listaMateriales.size() > 0) {
                for (int i = 0; i < listaMateriales.size(); i++) {
                    if (listaMateriales.get(i).getInstalacion().getNombre().equals(vieja.getNombre())) {
                        MaterialDAO.actualizarMaterialInstalacion(nueva.getNombre(), listaMateriales.get(i));
                    }
                }
            }
        }
        instalacionesInit().update(query, newDocument);
        System.out.println("Documento Instalación actualizado correctamente\n");
    }

    //Método para eliminar un documento de la colección Instalacion
    public static void eliminarInstalacion(Instalacion i) throws UnknownHostException {
        System.out.println(String.format("Buscando documento Instalacion nombre %s para eliminar...", i.getNombre()));
        instalacionesInit().remove(new BasicDBObject("nombre", i.getNombre()));//Elimina el documento que recibe del método buscarInstalacion, pasándole la colección y el tipo de Instalacion.
        System.out.println("Documento Instalación eliminado\n");
    }

    //Método para buscar un documento Instalación en la colección Instalacion por su nombre
    public static Instalacion buscarInstalacion(String nombre) throws UnknownHostException {
        BasicDBObject searchQuery = new BasicDBObject().append("nombre", nombre);//Creamos la query que será los documentos que contengan como atributo "nombre" el que recibe como parámetro el método
        DBCursor cursor = instalacionesInit().find(searchQuery);//Los elementos que cumplan la condicion de searchQuery se introducen en cursor
        if (cursor.hasNext()) {
            DBObject elemento = cursor.next();//Solamente debemos tener uno, ya que le pasamos el nombre que es nuestro ID.
            System.out.println(String.format("Documento Instalacion encontrado: %s", elemento));
            Instalacion aux = new Instalacion();
            aux.setNombre((String) elemento.get("nombre"));
            aux.setDescripcion((String) elemento.get("descripcion"));
            aux.setCapacidad((Integer) elemento.get("capacidad"));
            return aux;//Devolvemos el documento que será útil para otras operaciones CRUD
        } else {
            return null;
        }
    }
}
