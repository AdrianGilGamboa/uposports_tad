
package bbdd;
import clases.Abono;
import clases.Cliente;
import java.net.UnknownHostException;
import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.net.UnknownHostException;
import java.util.ArrayList;
import static javafx.scene.Cursor.cursor;
import javax.swing.text.Document;
import org.omg.CORBA.ORB;

public class ClienteDAO{
    
    private static DBCollection clienteInit() throws UnknownHostException{
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        // Conectar a la base de datos
        DB db = mongoClient.getDB("uposports");
        DBCollection collection = db.getCollection("Clientes");
        return collection;
    }
    
    //Método estático para la creación de clientes   
    public static void creaCliente(Cliente c) throws UnknownHostException{
         //Crea documento
            BasicDBObject document; //CREAMOS un documento tipo BasicDBObject
            document = new BasicDBObject();
            //Le añadimos los campos con los valores pasados por parámetros
            document.append("dni", c.getDni());   
            document.append("nombre", c.getNombre());
            document.append("apellidos", c.getApellidos());
            document.append("telefono", c.getTelefono());
            document.append("CP", c.getCodigoPostal());
            document.append("abono", c.getAbono().getTipo());
            //Inserta documento en la coleccion Cliente
            clienteInit().insert(document);
            System.out.println("Cliente insertado: "+document);
    }
    
     public static ArrayList<Cliente> consultaClientes() throws UnknownHostException{
         ArrayList listaClientes = new ArrayList();
         DBCursor cursor = null;
         // Obtenemos todos los documentos de la coleccion
            cursor = clienteInit().find();
            //Recorrido de todos los elementos de la coleccion
            System.out.println("Recorrido de la coleccion:");
            int i = 0;
            DBObject elemento;
            while (cursor.hasNext()) {
                i++;
                Cliente aux = new Cliente();
                elemento = cursor.next();
                aux.setNombre((String) elemento.get("nombre"));
                aux.setApellidos((String) elemento.get("apellidos"));
                aux.setDni((String) elemento.get("dni"));
                aux.setTelefono((String) elemento.get("telefono"));
                aux.setCodigoPostal((String) elemento.get("CP"));
                aux.setAbono(AbonoDAO.buscarAbono((String) elemento.get("abono")));
                listaClientes.add(aux);
            }
            return listaClientes;
     }
     
     public static void eliminaCliente(Cliente c) throws UnknownHostException{   
         DBCursor cursor = null;
            DBObject elemento;
            cursor=clienteInit().find((DBObject) new BasicDBObject().append("dni", c.getDni())); //Obtenemos los documentos que coincidan con el dni del Cliente
            while(cursor.hasNext()){
                clienteInit().remove(cursor.next()); //Eliminamos los documentos que coinciden con el dni pasado por parámetro
            }
     }
     
     public static void actualizaCliente(Cliente c, Cliente viejo) throws UnknownHostException{
         //Actualizacion del valor de un campo
            BasicDBObject newDocument = new BasicDBObject();
            BasicDBObject aux = new BasicDBObject();
            
            // Indica el atributo nombre y su valor a establecer ($set)
            newDocument.append("$set", aux.append("telefono", c.getTelefono()));
            newDocument.append("$set", aux.append("nombre", c.getNombre()));
            newDocument.append("$set", aux.append("apellidos", c.getApellidos()));
            newDocument.append("$set", aux.append("dni", c.getDni()));
            newDocument.append("$set", aux.append("CP", c.getCodigoPostal()));
            newDocument.append("$set", aux.append("abono", c.getAbono().getTipo()));
            // Indica el filtro a usar para aplicar la modificacion
            DBObject searchQuery = new BasicDBObject().append("dni", viejo.getDni());
            
            clienteInit().update(searchQuery, newDocument);
            System.out.println("Nombre del cliente actualizado correctamente");
     }
     //Método para buscar un documento abono en la colección AbonoDAO
    public static boolean clientesConAbono(Abono a) throws UnknownHostException {
        BasicDBObject searchQuery = new BasicDBObject().append("abono", a.getTipo());//Creamos la query que será los documentos que contengan como atributo "tipo" el que recibe como parámetro el método
        DBCursor cursor = clienteInit().find(searchQuery);//Los elementos que cumplan la condicion de searchQuery se introducen en cursor        
        if(cursor.hasNext())
            return true;
        else
            return false;
    }
}
