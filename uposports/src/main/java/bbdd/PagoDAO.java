/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bbdd;

import clases.Abono;
import clases.Pago;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;

/**
 *
 * @author adrii_gil99
 */
public class PagoDAO {

    private static DBCollection pagosInit() throws UnknownHostException {
        // Conectar al servidor MongoDB
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        // Conectar a la base de datos
        DB db = mongoClient.getDB("uposports");

        //Acceder coleccion "Abonos"*/
        DBCollection collection = db.getCollection("Pagos");

        return collection;
    }

    public static void insertarPago(Pago pago) throws UnknownHostException {

        DBCollection collection = pagosInit();
        BasicDBObject document = new BasicDBObject();//Instanciamos el nuevo documento
        //Insertamos los 3 atributos del nuevo documento Abono
        document.append("fechaHora", pago.getFecha());
        document.append("cantidad", pago.getCantidad());
        //Insertamos el documento en la colección AbonoDAO
        collection.insert(document);
        System.out.println("Documento Pago insertado: " + document + "\n");
    }

}
