/**
 * @author Arjun Dhuliya
 */
package Game21.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Deck implements Serializable {
    static final int MAX_CARD_PER_SUIT = 13;
    static ArrayList<Card> cards;
    private static Deck deckInstance = null;
    private final int MAX_CARDS_COUNT = 52;


    /***
     *
     */
    private Deck() {
        cards = new ArrayList<>(MAX_CARDS_COUNT);
        for (Suit suit : Suit.values()) {
            for (int code = 1; code <= MAX_CARD_PER_SUIT; code++) {
                if (code == 1)
                    cards.add(new Card(suit, 'A'));
                else if (code == 10)
                    cards.add(new Card(suit, 'T'));
                else if (code == 11)
                    cards.add(new Card(suit, 'J'));
                else if (code == 12)
                    cards.add(new Card(suit, 'Q'));
                else if (code == 13)
                    cards.add(new Card(suit, 'K'));
                else
                    cards.add(new Card(suit, (char) (code + 48)));
            }
        }
    }

    /***
     * @return
     */
    public static Deck getDeckInstance() {
        if (deckInstance == null)
            return deckInstance = new Deck();
        return deckInstance;
    }

    /***
     * shuffle deck three times
     */
    public void shuffleDeck() {
        Collections.shuffle(cards);
        Collections.shuffle(cards);
        Collections.shuffle(cards);
    }
}



