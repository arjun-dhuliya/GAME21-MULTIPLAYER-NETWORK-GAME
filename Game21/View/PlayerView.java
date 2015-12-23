package Game21.View;

import Game21.Model.Player;
import Game21.Model.Result;

import java.io.Serializable;
import java.util.Scanner;

/**
 * @author Arjun Dhuliya
 */
public class PlayerView implements Serializable {

    /***
     *
     */
    public final Player currentPlayer;

    /***
     *
     */
    public PlayerView() {
        this.currentPlayer = getPlayerInfo();
        //this.currentPlayer = currentPlayer;
    }

    private Player getPlayerInfo() {
        System.out.println("Enter Player Name and Money(separated by a space):");
        Scanner scanner = new Scanner(System.in);
        return new Player(scanner.next(), scanner.nextInt());
    }

    /***
     * @return
     */
    public Player.MOVE getNextMove() {
        int enteredChoice;
        do {
            System.out.println("****************************************\n" + currentPlayer);
//            System.out.println("Cards on hand: "+currentPlayer.cardsOnHand);
            System.out.println("Your Next Move");
            int choice = 1;
            for (Player.MOVE move :
                    Player.MOVE.values()) {
                System.out.println(choice++ + ". " + move);
            }
            Scanner scanner = new Scanner(System.in);
            enteredChoice = scanner.nextInt();
        } while (enteredChoice < 1 || enteredChoice > Player.MOVE.values().length);

        return Player.MOVE.values()[enteredChoice - 1];
    }

    public void showResult(Result result) {
        switch (result) {
            case WIN:
                System.out.println(this.currentPlayer + ", You Won");
                break;
            case LOSS:
                System.out.println(this.currentPlayer + ", You Lost");
                break;
            case DRAW:
                System.out.println(this.currentPlayer + ", It's a Draw.");
                break;
        }
    }

    public void DisplayMessage(String msg) {
        System.out.println(msg);
    }

    public String getServerIp() {
        System.out.print("Enter Server Id: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }
}
