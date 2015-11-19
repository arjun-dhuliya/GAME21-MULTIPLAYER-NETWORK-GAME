package Game21.Controller;

import Game21.Model.*;
import Game21.View.PlayerView;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author Arjun Dhuliya
 */
public class PlayerController implements Serializable {
    private static String serverId;
    private final PlayerView view;
    private Socket connection;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    /***
     *
     */
    public PlayerController() {
        view = new PlayerView();
        serverId = view.getServerIp();
    }

    /***
     *
     */
    public void startRunning() {
        try {
            connectToDealer();
            setupStreams();
            listenToDealerAndRespond();
        } finally {
            close();
        }
    }

    /***
     *
     */
    private void listenToDealerAndRespond() {
        while (true) {
            try {
                Message message = (Message) inputStream.readObject();
                switch (message.messageType) {
                    case INTRODUCE:
                        outputStream = new ObjectOutputStream(connection.getOutputStream());
                        view.DisplayMessage("Dealer wants Introduction,Introducing Myself!!\n" + this.view.currentPlayer);
                        outputStream.writeObject(new Message(Message.Type.INTRODUCE, this.view.currentPlayer));
                        break;
                    case GET_NEXT_MOVE:
                        outputStream = new ObjectOutputStream(connection.getOutputStream());
                        Player.MOVE nextMove = this.nextMove();
                        switch (nextMove) {

                            case REQUEST_HIT:
                                outputStream.writeObject(new Message(Message.Type.REQUEST_HIT, ""));
                                break;
                            case REQUEST_DOUBLE:
                                outputStream.writeObject(new Message(Message.Type.REQUEST_DOUBLE, ""));
                                break;
                            case SIGNAL_HOLD:
                                outputStream.writeObject(new Message(Message.Type.SIGNAL_HOLD, ""));
                                break;
                        }
                        break;
                    case INTRODUCTION_ACCEPTED:
                        break;
                    case ISSUE_TWO_CARDS:
                        this.view.currentPlayer.receiveCards(((Card[]) message.messageBody));
                        view.DisplayMessage("After Received First Two: " + this.view.currentPlayer);
                        break;
                    case ISSUE_CARD:
                        this.view.currentPlayer.receiveCards(((Card) message.messageBody));
                        view.DisplayMessage("After Receiving HIT: " + this.view.currentPlayer);
                        break;
                    case RESULT:
                        switch (((Result) message.messageBody)) {
                            case WIN:
                                this.view.showResult(Result.WIN);
                                break;
                            case LOSS:
                                this.view.showResult(Result.LOSS);
                                break;
                            case DRAW:
                                this.view.showResult(Result.DRAW);
                                break;
                        }
                        break;
                    case HOLD_ACCEPTED:
                        this.view.currentPlayer.hold = true;
                        break;
                    case END_OF_GAME:
                        this.view.DisplayMessage("Exiting");
                        this.close();
                        break;
                }
                outputStream.flush();
                if (!connection.isClosed())
                    inputStream = new ObjectInputStream(connection.getInputStream());

            } catch (EOFException e) {
                this.close();
                System.exit(0);
            } catch (IOException e) {
                view.DisplayMessage("Caught Exception while Read -  Write IO");
                e.printStackTrace();
                System.exit(1);
            } catch (ClassNotFoundException e) {
                view.DisplayMessage("Caught Exception while Read -  Write Class");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }


    /***
     *
     */
    private void setupStreams() {
        try {
            outputStream = new ObjectOutputStream(connection.getOutputStream());
            inputStream = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     *
     */
    private void connectToDealer() {
        view.DisplayMessage("Attempting to connect to Dealer at " + Constant.SERVER_IP + ":" + Constant.PORT_NUMBER);
        try {
            connection = new Socket(InetAddress.getByName(serverId), Constant.PORT_NUMBER);
            view.DisplayMessage("Connected to " + connection.getInetAddress().getHostName());
        } catch (IOException e) {
            view.DisplayMessage("Caught exception while connecting");
            e.printStackTrace();
        }
    }

    /***
     *
     */
    private void close() {
        try {
            if (!connection.isClosed()) {
                outputStream.close();
                inputStream.close();
                connection.close();
                this.view.DisplayMessage("Bye Bye...");
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /***
     * @return
     */
    private Player.MOVE nextMove() {
        return view.getNextMove();
    }

}
