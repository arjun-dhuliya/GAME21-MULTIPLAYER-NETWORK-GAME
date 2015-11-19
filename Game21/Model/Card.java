package Game21.Model;

import java.io.Serializable;

/**
 * @author Arjun Dhuliya
 */
public class Card implements Serializable {
    public static final char ACE = 'A';
    final char value;
    private final Suit suit;

    /***
     * @param suit
     * @param value
     */
    public Card(Suit suit, char value) {
        this.suit = suit;
        this.value = value;
    }

    /***
     * @param total
     * @return
     */
    public int getNumericValue(int total) {
        switch (this.value) {
            case ACE:
                if (total > 10)
                    return 1;
                else
                    return 11;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case 'T':
                return 10;
            case 'J':
                return 11;
            case 'Q':
                return 12;
            case 'K':
                return 13;
            default:
                return -1;
        }
    }

    /***
     * @return
     */
    @Override
    public String toString() {
        switch (this.value) {
            case ACE:
                return "A of " + suit.suitToChar(suit);
            case '2':
                return "2 of " + suit.suitToChar(suit);
            case '3':
                return "3 of " + suit.suitToChar(suit);
            case '4':
                return "4 of " + suit.suitToChar(suit);
            case '5':
                return "5 of " + suit.suitToChar(suit);
            case '6':
                return "6 of " + suit.suitToChar(suit);
            case '7':
                return "7 of " + suit.suitToChar(suit);
            case '8':
                return "8 of " + suit.suitToChar(suit);
            case '9':
                return "9 of " + suit.suitToChar(suit);
            case 'T':
                return "10 of " + suit.suitToChar(suit);
            case 'J':
                return "J of " + suit.suitToChar(suit);
            case 'Q':
                return "Q of " + suit.suitToChar(suit);
            case 'K':
                return "K of " + suit.suitToChar(suit);
            default:
                return "Invalid Card";
        }
    }

}

