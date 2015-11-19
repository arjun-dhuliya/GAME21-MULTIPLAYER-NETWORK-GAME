package Game21.Model;

import java.io.Serializable;

/**
 * @author Arjun Dhuliya
 */
public class Message implements Serializable {
    public final Type messageType;
    public final Object messageBody;

    public Message(Type messageType, Object messageBody) {
        this.messageType = messageType;
        this.messageBody = messageBody;
    }

    public enum Type {
        INTRODUCE, GET_NEXT_MOVE, REQUEST_HIT, REQUEST_DOUBLE, SIGNAL_HOLD,
        INTRODUCTION_ACCEPTED, ISSUE_TWO_CARDS, ISSUE_CARD, RESULT, HOLD_ACCEPTED, END_OF_GAME
    }
}
