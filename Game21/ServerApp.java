package Game21;

import Game21.Controller.GameController;

import java.io.Serializable;

/**
 * @author Arjun Dhuliya
 */
public class ServerApp implements Serializable {
    /****
     * main program of Server
     * @param args
     */
    public static void main(String[] args) {
        GameController gameController = new GameController();
        gameController.startServer();
    }
}
