package Game21.Model;

import java.io.Serializable;

/**
 * @author Arjun Dhuliya
 */
public enum Suit implements Serializable {
    Hearts,
    Diamonds,
    Clubs,
    Spades; // semicolon needed when fields / methods follow

    public char suitToChar(Suit suit) {
        switch (suit) {
            case Hearts:
                return (char) 9829;
            case Diamonds:
                return (char) 9830;
            case Clubs:
                return (char) 9827;
            case Spades:
                return (char) 9824;
        }
        return '~';
    }


}