package Game21.Model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * @author Arjun Dhuliya
 */
public class ConnectionInfo implements Serializable {
    public final Socket connection;
    public Player playerObject;
    public ObjectOutputStream outputStream;
    public ObjectInputStream inputStream;

    public ConnectionInfo(Socket connection) {
        this.connection = connection;
    }
}