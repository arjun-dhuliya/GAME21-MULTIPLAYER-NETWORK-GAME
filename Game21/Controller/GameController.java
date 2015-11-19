package Game21.Controller;

import Game21.Model.*;
import Game21.View.DealerView;

import java.io.*;
import java.net.Inet4Address;
import java.net.ServerSocket;

/**
 * @author Arjun Dhuliya
 */
public class GameController implements Serializable {
    private static final Object lock = new Object();
    private static Dealer dealer;
    private final DealerView dealerView;
    private final ConnectionInfo[] connectionInfos;
    private final int numberOfPlayers;
    private ServerSocket serverSocket;
    private int connectedCount = 0;
    private boolean gameFinished;

    /***
     *
     */
    public GameController() {
        this.gameFinished = false;
        dealer = Dealer.getDealerInstance(); // Give Dealer big Money
        this.dealerView = new DealerView();
        numberOfPlayers = dealerView.howManyPlayerOnTable();
        connectionInfos = new ConnectionInfo[numberOfPlayers];
    }

    /***
     *
     */
    public void startServer() {
        try {
            dealerView.DisplayMessage("Waiting for players on Ip " + Inet4Address.getLocalHost().getHostAddress());
            this.serverSocket = new ServerSocket(Constant.PORT_NUMBER, numberOfPlayers);
            try {
                //wait for connection and setup connection
                waitForConnection();
                //get all player info
                getIntroduction();
                //serve the first two cards to all players
                serveTwoCards();
                if (didLastMoveFinishedGame()) {
                    broadCastWinner();
                } else {
                    whileGameActive();
                }
            } finally {
                serverSocket.close();
                // closeThisConnection();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            closeServer();
        }
    }

    private void closeServer() {
        if(!serverSocket.isClosed()){
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     *
     */
    private void serveTwoCards() {
        Thread[] intro = new Thread[numberOfPlayers];
        for (int index = 0; index < numberOfPlayers; index++) {
            final int finalIndex = index;
            intro[index] = new Thread(new Runnable() {
                @Override
                public void run() {
                    serveTopTwoCardsAction(connectionInfos[finalIndex]);
                }
            });
            intro[index].start();
        }
        for (Thread t :
                intro) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * @param connectionInfo
     */
    private void serveTopTwoCardsAction(ConnectionInfo connectionInfo) {
        try {
            synchronized (lock) {
                if (connectionInfo.playerObject.canRequestHit()) {
                    Card[] cards = dealer.serveTopTwoCards();

                    connectionInfo.outputStream = new ObjectOutputStream(connectionInfo.connection.getOutputStream());
                    connectionInfo.outputStream.writeObject(new Message(Message.Type.ISSUE_TWO_CARDS, cards));
                    connectionInfo.outputStream.flush();
                    connectionInfo.playerObject.receiveCards(cards);

                    dealerView.DisplayMessage("Updated on Dealer side:" + connectionInfo.playerObject + "");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     *
     */
    private void getIntroduction() {
        Thread[] intro = new Thread[numberOfPlayers];
        for (int index = 0; index < numberOfPlayers; index++) {
            final int finalIndex = index;
            intro[index] = new Thread(new Runnable() {
                @Override
                public void run() {
                    askForPlayerInfo(connectionInfos[finalIndex]);
                }
            });
            intro[index].start();
        }
        for (Thread t :
                intro) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * @param connectionInfo
     */
    private void askForPlayerInfo(ConnectionInfo connectionInfo) {
        try {
            dealerView.DisplayMessage("Asking for info");
            connectionInfo.outputStream.writeObject(new Message(Message.Type.INTRODUCE, ""));
            connectionInfo.outputStream.flush();
            connectionInfo.inputStream = new ObjectInputStream(connectionInfo.connection.getInputStream());
            Message message = (Message) connectionInfo.inputStream.readObject();
            dealerView.DisplayMessage("Got Message back");
            if (message.messageType == Message.Type.INTRODUCE) {
                connectionInfo.playerObject = ((Player) message.messageBody);
                dealerView.DisplayMessage("Got Info " + connectionInfo.playerObject);
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.dealerView.DisplayMessage("CAught IO");
            System.exit(1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            this.dealerView.DisplayMessage("CAught Class");
            System.exit(1);
        }
    }

    /***
     *
     */
    private void closeThisConnection(ConnectionInfo connectionInfo) {
        this.dealerView.DisplayMessage("Closing connection -> " + connectionInfo.playerObject);
        try {
            connectionInfo.outputStream.close();
            connectionInfo.inputStream.close();
            connectionInfo.connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     *
     */
    private void whileGameActive() {
        while (!gameFinished) {
            int activeCount = getActivePlayerCount();
            Thread[] threads;
            if (activeCount > 0) {
                threads = new Thread[activeCount];
                int threadIndex = 0;
                for (final ConnectionInfo connectInfo :
                        connectionInfos) {
                    if (!connectInfo.playerObject.hold && !connectInfo.playerObject.bust) {
                        threads[threadIndex] = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getNextMove(connectInfo);
                            }
                        });
                        synchronized (this) {
                            threads[threadIndex++].start();
                        }
                    }
                }
                for (Thread thread :
                        threads) {
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                gameFinished = true;
                broadCastWinner();
                break;
            }
        }
    }

    /***
     * @param connectInfo
     */
    private void getNextMove(ConnectionInfo connectInfo) {
        try {

            OutputStream stream = connectInfo.connection.getOutputStream();
            connectInfo.outputStream = new ObjectOutputStream(stream);
            connectInfo.outputStream.writeObject(new Message(Message.Type.GET_NEXT_MOVE, ""));
            connectInfo.outputStream.flush();
            connectInfo.inputStream = new ObjectInputStream(connectInfo.connection.getInputStream());
            Message message = (Message) connectInfo.inputStream.readObject();
            switch (message.messageType) {
                case REQUEST_HIT:
                    synchronized (lock) {
                        if (connectInfo.playerObject.canRequestHit()) {
                            Card card = dealer.serveTopCard();
                            stream = connectInfo.connection.getOutputStream();
                            connectInfo.outputStream = new ObjectOutputStream(stream);
                            connectInfo.outputStream.writeObject(new Message(Message.Type.ISSUE_CARD, card));
                            connectInfo.outputStream.flush();
                            connectInfo.playerObject.receiveCards(card);
                            dealerView.DisplayMessage(connectInfo.playerObject + "");
                        }
                    }
                    break;
                case REQUEST_DOUBLE:
                    synchronized (lock) {
                        if (connectInfo.playerObject.canRequestHit()) {
                            Card card = dealer.serveTopCard();
                            connectInfo.playerObject.hold = true;
                            stream = connectInfo.connection.getOutputStream();
                            connectInfo.outputStream = new ObjectOutputStream(stream);
                            connectInfo.outputStream.writeObject(new Message(Message.Type.ISSUE_CARD, card));
                            connectInfo.outputStream.flush();
                            connectInfo.playerObject.receiveCards(card);
                            dealerView.DisplayMessage(connectInfo.playerObject + "");
                        }
                    }
                    break;
                case SIGNAL_HOLD:
                    synchronized (lock) {
                        if (!connectInfo.playerObject.bust && !connectInfo.playerObject.hold) {
                            connectInfo.playerObject.hold = true;
                            stream = connectInfo.connection.getOutputStream();
                            connectInfo.outputStream = new ObjectOutputStream(stream);
                            connectInfo.outputStream.writeObject(new Message(Message.Type.HOLD_ACCEPTED, ""));
                            connectInfo.outputStream.flush();
                            dealerView.DisplayMessage(connectInfo.playerObject + "");
                        }
                    }
            }

        } catch (IOException e) {
            e.printStackTrace();
            this.dealerView.DisplayMessage("Caught IO ");
            System.exit(1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            this.dealerView.DisplayMessage("Caught Class ");
            System.exit(1);
        }
    }

    /***
     * @return
     */
    private int getActivePlayerCount() {
        int activePlayerRemaining = 0;
        for (ConnectionInfo info :
                connectionInfos) {
            if (!info.playerObject.hold && !info.playerObject.bust)
                activePlayerRemaining++;
        }
        return activePlayerRemaining;
    }

    private boolean didLastMoveFinishedGame() {
        int activePlayers = 0;
        for (ConnectionInfo connectionInfo :
                connectionInfos) {
            if (!connectionInfo.playerObject.bust && !connectionInfo.playerObject.hold) {
                activePlayers++;
            }
        }
        if (activePlayers > 0)
            return false;
        else
            return true;
    }


    /***
     *
     */
    private void waitForConnection() {
        try {
            synchronized (lock) {
                while (true) {
                    dealerView.printMessage("Waiting for connection...");
                    connectionInfos[connectedCount] = new ConnectionInfo(serverSocket.accept());
                    dealerView.printMessage("Now Connected to " +
                            connectionInfos[connectedCount].connection.getInetAddress().getHostName());
                    connectionInfos[connectedCount].outputStream = new ObjectOutputStream(
                            connectionInfos[connectedCount].connection.getOutputStream());
                    connectionInfos[connectedCount].inputStream = new ObjectInputStream(
                            connectionInfos[connectedCount].connection.getInputStream());
                    connectedCount++;
                    if (connectedCount == numberOfPlayers) {
                        lock.notify();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadCastWinner() {
        boolean draw = true;
        Player winner = null;
        for (ConnectionInfo connectionInfo :
                connectionInfos) {
            if (!connectionInfo.playerObject.bust) {
                if (winner == null) {
                    winner = connectionInfo.playerObject;
                    draw = false;
                } else if (connectionInfo.playerObject.calculateCardsValue() > winner.calculateCardsValue()) {
                    winner = connectionInfo.playerObject;
                    draw = false;
                } else if (connectionInfo.playerObject.calculateCardsValue() == winner.calculateCardsValue()) {
                    draw = true;
                }
            }
        }
        final boolean isItDraw = draw;
        final Player whoWon = winner;
        Thread[] intro = new Thread[numberOfPlayers];
        for (int index = 0; index < numberOfPlayers; index++) {
            final int finalIndex = index;
            intro[index] = new Thread(new Runnable() {
                @Override
                public void run() {
                    declareWinner(connectionInfos[finalIndex], isItDraw, whoWon);
                }
            });
            intro[index].start();
        }
        for (Thread t :
                intro) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * @param connectionInfo
     * @param draw
     * @param winner
     */
    private void declareWinner(ConnectionInfo connectionInfo, boolean draw, Player winner) {

        try {
            connectionInfo.outputStream = new ObjectOutputStream(connectionInfo.connection.getOutputStream());
            if (draw) {
                connectionInfo.outputStream.writeObject(new Message(Message.Type.RESULT, Result.DRAW));
                dealerView.DisplayMessage(connectionInfo.playerObject + " DRAW");
            } else if (connectionInfo.playerObject.equals(winner)) {
                connectionInfo.outputStream.writeObject(new Message(Message.Type.RESULT, Result.WIN));
                dealerView.DisplayMessage(connectionInfo.playerObject + " WON");
            } else {
                connectionInfo.outputStream.writeObject(new Message(Message.Type.RESULT, Result.LOSS));
                dealerView.DisplayMessage(connectionInfo.playerObject + " LOST");

            }
            closeThisConnection(connectionInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
