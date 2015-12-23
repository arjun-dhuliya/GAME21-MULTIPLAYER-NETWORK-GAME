/**
 * @author Arjun Dhuliya
 */
package Game21.View;

import Game21.Controller.GameController;
import Game21.Model.Dealer;

import java.io.Serializable;
import java.util.Scanner;

public class DealerView implements Serializable {

    public DealerView() {
//        super(dealer);
    }

    public int howManyPlayerOnTable() {
        System.out.println("How many Players on Table? ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    public void printMessage(String msg) {
        System.out.println(msg);
    }

    public void DisplayMessage(String msg) {
        System.out.println(msg);
    }
}
