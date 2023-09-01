package tdt4100_gg_prosjekt;

public class Card {

        
    private final int value;
    private final char suit;

    public Card(int value, char suit) throws IllegalArgumentException {
        if (isCardValid(suit, value)) {
            this.suit = suit;
            this.value = value;
        }
        else {
            throw new IllegalArgumentException("Not valid input for constructing card");
        }
    }

    //her er validatorer

    private boolean isCardValid(char suit, int value){
        return this.isSuitValid(suit) && this.isFaceValid(value);
    }

    private boolean isFaceValid(int value) {
        return value >= 1 && value <= 13;
    }

    private boolean isSuitValid(char suit) {
        return String.valueOf(suit).matches("[SHDC]");
    }

    //her kommer gettere

    public char getSuit() {
        return this.suit;
    }


    //dette er vanlig kortverdi
    public int getValue() {
        return this.value;
    }

    //dette er blackjack-verdien av kortet
    public int getBlackJackValue() {

        if (this.value > 10) {
            return 10;
        }else if(this.value > 1){
            return this.value;
        }else{
            return 11;
        }

    }


    @Override
    public String toString() {
        return this.suit + String.valueOf(this.value);
    }
}
