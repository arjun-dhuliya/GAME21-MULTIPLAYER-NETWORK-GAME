package Game21.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Arjun Dhuliya
 */
public class Player implements Serializable {
    private final ArrayList<Card> cardsOnHand;
    private final String name;
    public boolean hold;
    public boolean bust;

    /***
     * @param name
     * @param moneyLeft
     */
    public Player(String name, int moneyLeft) {
        this.name = name;
        this.cardsOnHand = new ArrayList<>(2);
        hold = false;
        bust = false;
    }

    /***
     * @param cards
     */
    public void receiveCards(Card[] cards) {
        Collections.addAll(this.cardsOnHand, cards);
        if (calculateCardsValue() > 21) {
            this.bust = true;
            this.hold = true;
        }
    }

    /***
     * @param card
     */
    public void receiveCards(Card card) {
        this.cardsOnHand.add(card);
        if (calculateCardsValue() > 21) {
            this.bust = true;
            this.hold = true;
        }
    }

    /***
     *
     */
    private void arrangeCards() {
        ArrayList<Card> allAces = new ArrayList<>();
        for (int index = 0; index < cardsOnHand.size(); index++) {
            if (cardsOnHand.get(index).value == Card.ACE) {
                Card aceCard = cardsOnHand.remove(index);
                allAces.add(aceCard);
            }
        }
//        for (Card card :
//                cardsOnHand) {
//            if (card.value == Card.ACE){
//                cardsOnHand.remove(card);
//                allAces.add(card);
//            }
//        }

        for (Card aceCard :
                allAces) {
            cardsOnHand.add(aceCard);
        }

    }

    /***
     *
     */
    public boolean canRequestHit() {
        if (!hold && !bust) {
            return true;
        }
        return false;
    }

    /***
     * @return
     */
    public int calculateCardsValue() {
        int total = 0;
        arrangeCards();
        for (Card card : cardsOnHand
                ) {
            total += card.getNumericValue(total);
        }
        return total;
    }

    /***
     * @return
     */
    @Override
    public String toString() {
        String str = "Player " + name + " Hand->" + cardsOnHand;
//        for (Card card:cardsOnHand
//             ) {
//            str+=", "+card;
//        }
        str += ", Total=>" + calculateCardsValue();
        if (bust)
            str += " BUSTED!!!";
        return str;
    }

    /***
     * all possible Moves for a Player
     */
    public enum MOVE {
        REQUEST_HIT, REQUEST_DOUBLE, SIGNAL_HOLD
    }
}
