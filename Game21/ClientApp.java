package Game21;

import Game21.Controller.PlayerController;

import java.io.Serializable;

/**
 * @author Arjun Dhuliya
 */
public class ClientApp implements Serializable {
    /***
     * main program of Client
     * @param args
     */
    public static void main(String[] args) {
        PlayerController playerController = new PlayerController();
        playerController.startRunning();
    }
}
