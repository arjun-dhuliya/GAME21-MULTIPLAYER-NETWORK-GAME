package Game21.Model;

import java.io.Serializable;

/**
 * @author Arjun Dhuliya
 */
public class Dealer extends Player implements Serializable {
    private static Dealer dealer = null;
    private final Deck deck;

    /***
     * @param name
     * @param moneyLeft
     */
    private Dealer(String name, int moneyLeft) {

        super("Dealer", 99999999);
        deck = Deck.getDeckInstance();
        System.out.println("Original Deck: ");
        printDeck();
        System.out.println("Shuffling Deck");
        shuffleDeckCards();
        System.out.println("Deck after shuffle");
        printDeck();
    }

    public static Dealer getDealerInstance() {
        if (dealer == null)
            return dealer = new Dealer("Dealer", 99999999);
        return dealer;
    }

    /***
     * @return
     */
    public Card[] serveTopTwoCards() {
        return new Card[]{Deck.cards.remove(0), Deck.cards.remove(0)};
    }

    /***
     * @return
     */
    public Card serveTopCard() {
        return Deck.cards.remove(0);
    }

    /***
     *
     */
    private void printDeck() {
        int index = 0;
        for (Card card : Deck.cards
                ) {
            if (++index % Deck.MAX_CARD_PER_SUIT == 0)
                System.out.println("<" + card + ">");
            else
                System.out.print("<" + card + ">, ");
        }
        System.out.println("Reshuffling");
        this.shuffleDeckCards();
    }

    /***
     *
     */
    private void shuffleDeckCards() {
        this.deck.shuffleDeck();
    }
}
