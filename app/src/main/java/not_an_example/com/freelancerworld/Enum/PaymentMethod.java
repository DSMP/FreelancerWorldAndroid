package not_an_example.com.freelancerworld.Enum;

/**
 * Created by user on 2017-11-20.
 */

public enum PaymentMethod {
    ANY, NOSERVICE, CARD, TRANSFER, BITCOIN;


    public static PaymentMethod fromString(String name) {
        switch (name) {
            case "No Service" : return NOSERVICE;
            case "Card" : return CARD;
            case "Transfer" : return TRANSFER;
            case "BitCoin" : return BITCOIN;
            case "Any" : return ANY;
        }
        return null;
    }

    @Override
    public String toString() {
        switch (this) {
            case NOSERVICE: return "No Service";
            case CARD: return "Card";
            case TRANSFER: return "Transfer";
            case BITCOIN: return "BitCoin";
            case ANY: return "Any";
        }
        return "Unknown";
    }
}
