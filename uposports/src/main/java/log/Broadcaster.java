/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//Patrón Singleton a través de la clase auxiliar Broadcaster, así tenemos únicamente una instancia.
public class Broadcaster {
    //Lista de listeners de broadcasters
    private static final List<BroadcastListener> listeners = new CopyOnWriteArrayList<BroadcastListener>();

    //Cualquiera que llame y se registre se queda añadido
    public static void register(BroadcastListener listener) {
        listeners.add(listener);
    }

    //Por el contrario, se elimina de la lista de listeners
    public static void unregister(BroadcastListener listener) {
        listeners.remove(listener);
    }

    public static void broadcast(final String message) {
        for (BroadcastListener listener : listeners) {
            listener.receiveBroadcast(message);
        }
    }

    //Interfaz del broadcaster
    public interface BroadcastListener {

        public void receiveBroadcast(String message);
    }
}
