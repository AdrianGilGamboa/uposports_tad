package bbdd;

import clases.Empleado;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class EmpleadoDAO {
    //Crea documento

    private static DBCollection empleadoInit() throws UnknownHostException {
        // Conectar al servidor MongoDB
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        // Conectar a la base de datos
        DB db = mongoClient.getDB("uposports");

        //Acceder coleccion "Abonos"*/
        DBCollection collection = db.getCollection("Empleados");

        return collection;
    }

    //Método para añadir un documento nuevo a la colección AbonosDAO
    public static void insertarEmpleado(Empleado empleado) throws UnknownHostException {

        DBCollection collection = empleadoInit();
        BasicDBObject document = new BasicDBObject();//Instanciamos el nuevo documento
        //Insertamos los 3 atributos del nuevo documento Abono
        document.append("dni", empleado.getDni());
        document.append("nombre", empleado.getNombre());
        document.append("apellidos", empleado.getApellidos());
        document.append("telefono", empleado.getTelefono());

        //Insertamos el documento en la colección AbonosDAO
        collection.insert(document);
        System.out.println("Documento Empleado insertado: " + document + "\n");
    }

    //Método para mostrar todos los documentos de la colección AbonosDAO
    public static ArrayList<Empleado> mostrarEmpleados() throws UnknownHostException {
        DBCursor cursor = empleadoInit().find();// Obtenemos todos los documentos de la coleccion AbonosDAO
        ArrayList<Empleado> listaEmpleados = new ArrayList();
        DBObject elemento;
        //Recorrido de todos los elementos de la coleccion AbonosDAO
        System.out.println("Recorriendo la colección Empleados:");
        int i = 0;//Variable iteradora
        while (cursor.hasNext()) {//Mientras haya documentos
            i++;//Incrementamos la variable
            elemento = cursor.next();//Guardamos el documento en "elemento"
            Empleado aux = new Empleado();
            aux.setNombre((String) elemento.get("nombre"));
            aux.setApellidos((String) elemento.get("apellidos"));
            aux.setDni((String) elemento.get("dni"));
            aux.setTelefono((Integer) elemento.get("telefono"));
            listaEmpleados.add(aux);
            System.out.println(String.format("Documento Empleado_%d leido: %s", i, elemento));//Se muestra por pantalla el documento
        }
        System.out.println("Fin de la colección Empleados\n");
        return listaEmpleados;
    }

    /*
    //Método para actualizar un campo de tipo String de la colección AbonosDAO
    public static void actualizarEmpleado(DBCollection collection, DBObject empleado, String campo, String nuevoValor) {
        BasicDBObject newDocument = new BasicDBObject();//Instanciamos un nuevo documento
        newDocument.append("$set", new BasicDBObject().append(campo, nuevoValor));//Actualizamos el campo con el nuevoValor (ambos atributos se reciben por parámetro)
        collection.update(empleado, newDocument);//Actualizamos el documento "abono" recibido por parámetro con el nuevo campo del documento creado
        System.out.println("Documento Empleado actualizado correctamente\n");
    }
     */
    //Método para actualizar un campo de tipo int de la colección AbonosDAO (método sobrecargado)
    public static void actualizarEmpleado(Empleado nuevo, Empleado viejo) throws UnknownHostException {
        BasicDBObject newDocument = new BasicDBObject();
        BasicDBObject aux = new BasicDBObject();
        newDocument.append("$set", aux.append("nombre", nuevo.getNombre()));
        newDocument.append("$set", aux.append("apellidos", nuevo.getApellidos()));
        newDocument.append("$set", aux.append("dni", nuevo.getDni()));
        newDocument.append("$set", aux.append("telefono", nuevo.getTelefono()));
        DBObject searchQuery = new BasicDBObject().append("dni", viejo.getDni());
        empleadoInit().update(searchQuery, newDocument);
        System.out.println("Documento Empleados actualizado correctamente\n");
    }

    //Método para eliminar un documento de la colección AbonosDAO
    public static void eliminarEmpleados(Empleado e) throws UnknownHostException {
        System.out.println(String.format("Buscando documento Empleado dni %s para eliminar...", e.getDni()));
        empleadoInit().remove(new BasicDBObject().append("dni", e.getDni()));
        System.out.println("Documento Empleado eliminado\n");
    }

    //Método para buscar un documento abono en la colección AbonosDAO
    public static Empleado buscarEmpleado(String dni) throws UnknownHostException {
        BasicDBObject searchQuery = new BasicDBObject().append("dni", dni);//Creamos la query que será los documentos que contengan como atributo "tipo" el que recibe como parámetro el método
        DBCursor cursor = empleadoInit().find(searchQuery);//Los elementos que cumplan la condicion de searchQuery se introducen en cursor
        if (cursor.hasNext()) {
            Empleado aux = new Empleado();
            DBObject elemento = cursor.next();//Solamente debemos tener uno, ya que le pasamos el tipo que es nuestro ID.
            aux.setNombre((String) elemento.get("nombre"));
            aux.setApellidos((String) elemento.get("apellidos"));
            aux.setDni((String) elemento.get("dni"));
            aux.setTelefono((Integer) elemento.get("telefono"));
            System.out.println(String.format("Documento Empleado encontrado: %s", elemento));
            return aux;//Devolvemos el documento que será útil para otras operaciones CRUD
        } else {
            return null;
        }
    }
}
